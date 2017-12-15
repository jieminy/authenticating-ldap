package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import javax.naming.Context;
import java.util.HashMap;

@SpringBootApplication
public class Application {

    @Bean
    @ConfigurationProperties(prefix = "ldap.contextSource")
    public LdapContextSource contextSource(){
        LdapContextSource contextSource = new LdapContextSource();
        HashMap<String,Object> env = new HashMap<>();
        env.put("java.naming.ldap.factory.socket",this.getClass().getPackage().getName()+".DummySSLSocketFactory");
        env.put(Context.SECURITY_PROTOCOL,"ssl");
        contextSource.setBaseEnvironmentProperties(env);
        return  contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource){
        return new LdapTemplate(contextSource);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
