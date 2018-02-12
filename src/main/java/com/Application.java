package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@SpringBootApplication
public class Application {
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
//    public MessageSource messageSource() {
//        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasenames("classpath:/properties");
//        messageSource.setUseCodeAsDefaultMessage(true);
//        messageSource.setDefaultEncoding("UTF-8");
//        messageSource.setCacheSeconds(5);
//        return messageSource;
//    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
