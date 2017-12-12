package hello;

import User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;

@RestController
public class HomeController {
//    @Autowired
//    private LdapTemplate ldapTemplate;

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page!";
    }

//    @RequestMapping("ldap")
//    public List<User> testLdap(){
//        AttributesMapper am = new AttributesMapper() {
//            @Override
//            public Object mapFromAttributes(Attributes attributes) throws NamingException {
//                User user = new User();
//                Attribute attribute = attributes.get("description");
//                if(attribute!=null){
//                    user.setDescription((String)attribute.get());
//                }
//                return user;
//            }
//        };
//        String filter = "(&(ou=People))";
//        List<User> list = ldapTemplate.search("ou=People",filter,am);
//        return list;
//    }


}
