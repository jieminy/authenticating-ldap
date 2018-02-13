package com.dao.impl;

import com.common.Globals;
import com.dao.IUserDao;
import com.entity.Roles;
import com.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.CollectingAuthenticationErrorCallback;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao implements IUserDao{

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

    public List<User> getAllUsers(){
        String filter = "(objectClass=inetOrgPerson)";
        List<User> userList = ldapTemplate.search("ou=Users", filter, new UserAttributesMapper());
        for (User user : userList) {
            filter = "(member= uid="+user.getUid()+", ou=Users, dc=maxcrc, dc=com)";
            List<Globals.Role> roles = ldapTemplate.search("ou=UserRelations", filter, new RoleAttributesMapper());
            user.setRole(roles.get(0));
        }
        return userList;
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
    public void deleteGroupRelation() {
        ldapTemplate.unbind("cn=programers, ou=GroupRelations");
    }

    /***
     * 更新用户description 属性
     * @param user
     * @return
     */
    public boolean updateDesription(User user){
        try{
            Attribute attribute = new BasicAttribute("street",user.getStreet());
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
            ldapTemplate.rebind(buildDn(user),null,buildUserAttributes(user));
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
            attribute = attributes.get("mail");
            if(attribute != null){
                user.setMail((String) attribute.get());
            }
            attribute = attributes.get("mobile");
            if(attribute != null){
                user.setMobile((String) attribute.get());
            }
            attribute = attributes.get("postaladdress");
            if(attribute != null){
                user.setPostaladdress((String) attribute.get());
            }
            attribute = attributes.get("postofficebox");
            if(attribute != null){
                user.setPostofficebox((String) attribute.get());
            }
            attribute = attributes.get("street");
            if(attribute != null){
                user.setStreet((String) attribute.get());
            }
            return user;
        }
    }
    private class RoleAttributesMapper implements AttributesMapper{

        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            Globals.Role role = Globals.Role.TESTER;
            Attribute attr = attributes.get("cn");
            String strRole = (String)attr.get();
            if("testers".equals(strRole)){
                role = Globals.Role.TESTER;
            }else if("developers".equals(strRole)){
                role = Globals.Role.DEVELOPER;
            }else if("designers".equals(strRole)){
                role = Globals.Role.DESIGNER;
            }else if("programer".equals(strRole)){
                role = Globals.Role.PROGRAMER;
            }
            return role;
        }
    }

    //tag 管理分组
    /**
     * 添加分组
     * @param dn
     * @throws Exception
     */
    public void addGroup(String dn) throws Exception{
        ldapTemplate.bind(dn,null,builtGroupAttributes());
    }

    /**
     * 构建分组Attributes
     * @return
     */
    private Attributes builtGroupAttributes(){
        BasicAttribute basicAttribute = new BasicAttribute("objectClass");
        basicAttribute.add("top");
        basicAttribute.add("organizationalUnit");
        //用户属性
        Attributes attributes = new BasicAttributes();
        attributes.put(basicAttribute);
        return attributes;
    }

    //tag end 管理分组

    //tag 管理分组关系
    public void createGroupRelation(String member,String cn){
        ldapTemplate.bind("cn="+cn+",ou=GroupRelations",null,builtUserRelAttributes(member));
    }


    public void addGroupRelation(String cn,List<String> members){
        DirContextOperations ctx =   ldapTemplate.lookupContext("cn="+cn+",ou=GroupRelations");
        for(String member : members)
        {
            ctx.addAttributeValue("member","ou="+member+",ou=Groups,dc=maxcrc,dc=com");
        }
        ldapTemplate.modifyAttributes(ctx);
    }
    public void removeGroupRelation(String cn,List<String> members){
        DirContextOperations ctx =   ldapTemplate.lookupContext("cn="+cn+",ou=GroupRelations");
        for(String member : members)
        {
            ctx.removeAttributeValue("member","ou="+member+",ou=Groups,dc=maxcrc,dc=com");
        }
        ldapTemplate.modifyAttributes(ctx);
    }

    private Attributes builtGroupRelAttributes(String member){
        BasicAttribute basicAttribute = new BasicAttribute("objectClass");
        basicAttribute.add("top");
        basicAttribute.add("groupOfNames");
        Attributes attributes = new BasicAttributes();
        attributes.put(basicAttribute);
        attributes.put("member","ou="+member+",ou=Groups,dc=maxcrc,dc=com");
        return attributes;
    }
    //tag end 管理分组关系

    //tag 管理用户
    /***
     * 添加用户
     * @param user
     * @return
     */
    public void addUser(User user) throws Exception{
        ldapTemplate.bind(buildDn(user),null,buildUserAttributes(user));
    }

    /***
     * 构建dn
     * @param user
     * @return
     */
    private String buildDn(User user){
        return "userid="+user.getUid()+",ou=Users";
    }

    /***
     * 设置属性
     * @param user
     * @return
     */
    private Attributes buildUserAttributes(User user){
        //基类设置
        BasicAttribute basicAttribute = new BasicAttribute("objectClass");
        basicAttribute.add("top");
        basicAttribute.add("inetOrgPerson");
        //用户属性
        Attributes attributes = new BasicAttributes();
        attributes.put(basicAttribute);
        if(user.getUid()!=null){
            attributes.put("userid",user.getUid());
        }
        if(user.getPassword()!=null){
            attributes.put("userPassword",user.getPassword());
        }
        if(user.getSn()!=null){
            attributes.put("sn",user.getSn());
        }
        if(user.getCn()!=null){
            attributes.put("cn",user.getCn());
        }
        if(user.getMail()!=null){
            attributes.put("mail",user.getMail());
        }
        if(user.getMobile()!=null){
            attributes.put("mobile",user.getMobile());
        }
        if(user.getStreet()!=null){
            attributes.put("street",user.getStreet());
        }
        if(user.getPostaladdress()!=null){
            attributes.put("postaladdress",user.getPostaladdress());
        }
        if(user.getPostofficebox()!=null){
            attributes.put("postofficebox",user.getPostofficebox());

        }
        return attributes;
    }
    //tag end 管理用户

    //tag 管理userRelation
    public void createUserRelation(String member,String cn){
        ldapTemplate.bind("cn="+cn+",ou=UserRelations",null,builtGroupRelAttributes(member));
    }


    public void addUserRelation(String cn,List<String> members){
        DirContextOperations ctx =   ldapTemplate.lookupContext("cn="+cn+",ou=UserRelations");
        for(String member : members)
        {
            ctx.addAttributeValue("member","uid="+member+",ou=Users,dc=maxcrc,dc=com");
        }
        ldapTemplate.modifyAttributes(ctx);
    }
    public void removeUserRelation(String cn,List<String> members){
        DirContextOperations ctx =   ldapTemplate.lookupContext("cn="+cn+",ou=UserRelations");
        for(String member : members)
        {
            ctx.removeAttributeValue("member","uid="+member+",ou=Users,dc=maxcrc,dc=com");
        }
        ldapTemplate.modifyAttributes(ctx);
    }

    private Attributes builtUserRelAttributes(String member){
        BasicAttribute basicAttribute = new BasicAttribute("objectClass");
        basicAttribute.add("top");
        basicAttribute.add("groupOfNames");
        Attributes attributes = new BasicAttributes();
        attributes.put(basicAttribute);
        attributes.put("member","uid="+member+",ou=Users,dc=maxcrc,dc=com");
        return attributes;
    }
}
