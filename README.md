# ldap-testing



1. Create LDIF file (./fake-tree.ldif)
```
cat <<EOF > ./fake-tree.ldif
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
EOF
```

2. Set podman container as rootful
```
podman machine stop
podman machine set --rootful
podman machine start
```

3. Copy the LDIF file into the running container
```
podman run --name openldap-custom \
-p 389:389 \
-v ./fake-tree.ldif:/container/service/slapd/assets/config/bootstrap/ldif/custom/fake-tree.ldif \
--detach docker.io/osixia/openldap:latest --copy-service
```

4. ldapsearch domain tree query
```
ldapsearch -x -H "ldap://localhost:389" \
  -b "dc=example,dc=org" \
  -D "cn=admin,dc=example,dc=org" \
  -w "admin"
```

5. ldapsearch user query
```
ldapsearch -x -H "ldap://localhost:389" \
-b "dc=example,dc=org" \
-D "cn=admin,dc=example,dc=org"   \
-w "admin" "uid=alice"
```
