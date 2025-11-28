package com.jdap.jdap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

    @Value("${spring.ldap.urls}")
    private String ldapUrls;

    @Value("${spring.security.ldap.manager-dn}")
    private String managerDn;

    @Value("${spring.security.ldap.manager-password}")
    private String managerPassword;


    // Explicitly defines the ContextSource used for modifications
    @Bean
    public ContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrls);

        // Use the Manager DN and Password for binding
        contextSource.setUserDn(managerDn);
        contextSource.setPassword(managerPassword);

        // This is key: tell it where to start searches
        // The base is often defined separately, but setting it here is clean
//        contextSource.setBase("");

        return contextSource;
    }

    // Explicitly defines the LdapTemplate used for the UserService
    @Bean
    public LdapTemplate ldapTemplate(ContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}

