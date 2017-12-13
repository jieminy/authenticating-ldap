package hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
