# ldap-testing



1. Create LDIF file containing the following contents (./fake-tree.ldif)
```
# 1. Organizational Units
dn: ou=People,dc=example,dc=org
objectClass: organizationalUnit
ou: People

dn: ou=Groups,dc=example,dc=org
objectClass: organizationalUnit
ou: Groups

dn: ou=IT,ou=People,dc=example,dc=org
objectClass: organizationalUnit
ou: IT

dn: ou=HR,ou=People,dc=example,dc=org
objectClass: organizationalUnit
ou: HR

# 2. Users in IT
dn: cn=Alice Admin,ou=IT,ou=People,dc=example,dc=org
objectClass: inetOrgPerson
cn: Alice Admin
sn: Admin
givenName: Alice
mail: alice@example.org
userPassword: password
uid: alice

dn: cn=Bob Builder,ou=IT,ou=People,dc=example,dc=org
objectClass: inetOrgPerson
cn: Bob Builder
sn: Builder
givenName: Bob
mail: bob@example.org
userPassword: password
uid: bob

# 3. Users in HR
dn: cn=Charlie Check,ou=HR,ou=People,dc=example,dc=org
objectClass: inetOrgPerson
cn: Charlie Check
sn: Check
givenName: Charlie
mail: charlie@example.org
userPassword: password
uid: charlie

# 4. Groups
dn: cn=SysAdmins,ou=Groups,dc=example,dc=org
objectClass: groupOfNames
cn: SysAdmins
member: cn=Alice Admin,ou=IT,ou=People,dc=example,dc=org

dn: cn=AllStaff,ou=Groups,dc=example,dc=org
objectClass: groupOfNames
cn: AllStaff
member: cn=Alice Admin,ou=IT,ou=People,dc=example,dc=org
member: cn=Bob Builder,ou=IT,ou=People,dc=example,dc=org
member: cn=Charlie Check,ou=HR,ou=People,dc=example,dc=org
```


1. Run the openldap image
```
podman run --name openldap-custom \
--p 1389:389 \
--detach "docker.io/osixia/openldap:latest" \
--copy-service
```

1. Update running image with LDIF data
```
ldapadd -x -H "ldap://localhost:389" -D "cn=admin,dc=example,dc=org" -w "admin" -f ./fake-tree.ldif
```


1. ldapsearch domain tree query for test
```
ldapsearch -x -H "ldap://localhost:389" \
  -b "dc=example,dc=org" \
  -D "cn=admin,dc=example,dc=org" \
  -w "admin"
```

1. ldapsearch user query for test
```
ldapsearch -x -H "ldap://localhost:389" \
-b "dc=example,dc=org" \
-D "cn=admin,dc=example,dc=org"   \
-w "admin" "uid=alice"
```
