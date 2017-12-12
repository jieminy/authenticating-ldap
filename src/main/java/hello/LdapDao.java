package hello;

import User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.List;
@Repository
public class LdapDao {
    @Autowired
    private LdapTemplate ldapTemplate;

    /***
     * 根据uid查询
     * @param uid
     * @return
     */
    public  User getUserByUid(String uid){
        String filter = "(uid="+uid+")";
        List<User> userList = ldapTemplate.search("ou=people", filter, new AttributesMapper() {
            @Override
            public User mapFromAttributes(Attributes attributes) throws NamingException {
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
                return user;
            }
        });
        return userList.get(0);
    }


    public boolean addUser(User user){
        try{
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
            ldapTemplate.bind("uid="+user.getUid()+",ou=people",null,attributes);
            return true;
        }
        catch (Exception e){
            return  false;
        }
    }
    public boolean deleteUser(String username) {
        try {
            ldapTemplate.unbind("uid=" + username.trim());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
