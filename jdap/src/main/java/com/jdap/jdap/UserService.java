package com.jdap.jdap;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.Name;

@Service
public class UserService {
    private final LdapTemplate ldapTemplate;

    public UserService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void createUser(String uid, String cn, String sn, String password, String organizationalUnit) {
        String parentDn = "ou=" + organizationalUnit + ",ou=People,dc=example,dc=org";

        Name dn = LdapNameBuilder.newInstance(parentDn) // Use the dynamic parentDn
                .add("cn", cn)
                .build();

        Attributes attrs = new BasicAttributes(true);

        attrs.put("objectClass", "top");
        attrs.put("objectClass", "person");
        attrs.put("objectClass", "organizationalPerson");
        attrs.put("objectClass", "inetOrgPerson");
        attrs.put("cn", cn);
        attrs.put("sn", sn);
        attrs.put("uid", uid);
        attrs.put("userPassword", password);

        ldapTemplate.bind(dn, null, attrs);
    }

    public void deleteUser(String cn, String organizationalUnit) {
        String parentDn = "ou=" + organizationalUnit + ",ou=People,dc=example,dc=org";
        Name dn = LdapNameBuilder.newInstance("ou=IT,ou=People,dc=example,dc=org")
                .add("cn", cn)
                .build();

        ldapTemplate.unbind(dn);
    }



}
