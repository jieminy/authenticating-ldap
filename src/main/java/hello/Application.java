package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application {
//    @Value("${ldap.url}")
//    private String ldapUrl;
//
//    @Value("${ldap.base}")
//    private String ldapBase;
//
//    @Value("${ldap.userDn}")
//    private String ldapUserDn;
//
//    @Value("${ldap.userPwd}")
//    private String ldapUserPwd;
//
//    @Bean
//    public LdapTemplate ldapTemplate(){
//        return new LdapTemplate(contextSource());
//    }

    @Bean
    @ConfigurationProperties(prefix = "ldap.contextSource")
    public LdapContextSource contextSource(){
        LdapContextSource contextSource = new LdapContextSource();
        return  contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource){
        return new LdapTemplate(contextSource);
    }
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
