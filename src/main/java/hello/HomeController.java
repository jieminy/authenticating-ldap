package hello;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.HTMLDocument;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
public class HomeController {
//    @Autowired
//    private LdapTemplate ldapTemplate;

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page!";
    }



    @RequestMapping("/user")
    public String getUser(){
        String roles="";
        List<GrantedAuthority> authorities = ( List<GrantedAuthority> )SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("Authority" + grantedAuthority.getAuthority());
            roles+=grantedAuthority.getAuthority();
        }
        return "当前用户："+SecurityContextHolder.getContext().getAuthentication().getName()+" 用户权限: "+roles;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @PreAuthorize("authenticated and hasPermission('develplers')")
    public String pemissionValid(){
        return "拥有ROLE_DEVELOPERS权限 可以访问";
    }
    @PreAuthorize("authenticated and hasPermission('ROLE_TESTERS')")
    @RequestMapping("/nopem")
    public String pemissioInvalid(){
        return "未拥有ROLE_TESTERS权限 拒绝访问";
    }

}
