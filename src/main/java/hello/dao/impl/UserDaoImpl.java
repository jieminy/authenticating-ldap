package hello.dao.impl;

import hello.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.CollectingAuthenticationErrorCallback;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.List;
@Repository
public class UserDaoImpl {
    @Autowired
    private LdapTemplate ldapTemplate;

    /***
     * 根据uid查询
     * @param uid
     * @return
     */
    public User getUserByUid(String uid){
        String filter = "(uid="+uid+")";
        List<User> userList = ldapTemplate.search("ou=People", filter, new UserAttributesMapper());
        return userList.get(0);
    }

    /***
     * 添加用户
     * @param user
     * @return
     */
    public boolean addUser(User user){
        try{
            ldapTemplate.bind(buildDn(user),null,buildAttributes(user));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    /***
     * 根据uid删除用户
     * @param user
     * @return
     */
    public boolean deleteUser(User user) {
        try {
            ldapTemplate.unbind(buildDn(user));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * 更新用户description 属性
     * @param user
     * @return
     */
    public boolean updateDesription(User user){
        try{
            Attribute attribute = new BasicAttribute("description",user.getDescription());
            ModificationItem modificationItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,attribute);
            ldapTemplate.modifyAttributes(buildDn(user),new ModificationItem[]{modificationItem});
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * 暴力更新 先删除原有的
     * @param user
     * @return
     */
    public boolean updateUser(User user){
        try{
            ldapTemplate.rebind(buildDn(user),null,buildAttributes(user));
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * 认证
     * @param uid
     * @return
     */
    public boolean authentication(String uid){
        CollectingAuthenticationErrorCallback errorCallback = new CollectingAuthenticationErrorCallback();
        String filter =  "(&(objectclass=inetOrgPerson)(uid=" + uid + "))";
        boolean result = ldapTemplate.authenticate("",filter,"123",errorCallback);
        if(result == false){
            errorCallback.getError().printStackTrace();
        }
        return  result;
    }

    public boolean authenticateRole(String uid){
        CollectingAuthenticationErrorCallback errorCallback = new CollectingAuthenticationErrorCallback();
        String filter =  "(&(ou=Users)(uid=" + uid + "))";
        boolean result = ldapTemplate.authenticate("",filter,"",errorCallback);
        if(result == false){
            errorCallback.getError().printStackTrace();
        }
        return  result;
    }

    /***
     * 构建dn
     * @param user
     * @return
     */
    private String buildDn(User user){
        return "uid="+user.getUid()+",ou=People";
    }

    /***
     * 设置属性
     * @param user
     * @return
     */
    private Attributes buildAttributes(User user){
        //基类设置
        BasicAttribute basicAttribute = new BasicAttribute("objectClass");
        basicAttribute.add("top");
        basicAttribute.add("person");
        basicAttribute.add("organizationalPerson");
        basicAttribute.add("inetOrgPerson");
        //用户属性
        Attributes attributes = new BasicAttributes();
        attributes.put(basicAttribute);
        attributes.put("sn",user.getSn());
        attributes.put("cn",user.getCn());
        attributes.put("userPassword",user.getUserPassword());
        attributes.put("description",user.getDescription());
        return attributes;
    }

    /***
     * 查询结果类
     */
    private class UserAttributesMapper implements AttributesMapper {
        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            User user = new User();
            Attribute attribute = attributes.get("uid");
            if(attribute != null){
                user.setUid((String) attribute.get());
            }
            attribute = attributes.get("sn");
            if(attribute != null){
                user.setSn((String) attribute.get());
            }
            attribute = attributes.get("cn");
            if(attribute != null){
                user.setCn((String) attribute.get());
            }
            attribute = attributes.get("description");
            if(attribute != null){
                user.setDescription((String) attribute.get());
            }
            return user;
        }
    }
}
