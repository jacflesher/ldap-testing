#!/bin/sh

timeout() {
    dur="$1"
    shift

    "$@" &
    cmd=$!

    ( sleep "$dur"; kill "$cmd" 2>/dev/null ) &
    timer=$!

    wait "$cmd" 2>/dev/null
    status=$?

    kill "$timer" 2>/dev/null
    return $status
}

#podman run --name openldap-custom \
#-p 1389:389 \
#--detach "docker.io/osixia/openldap:latest" \
#--copy-service && \

if podman start openldap-custom; then
    COUNT=0
    while :; do
        if timeout 1 nc -vz localhost 1389 >/dev/null 2>&1; then
            ldapadd -x -H "ldap://localhost:1389" -D "cn=admin,dc=example,dc=org" -w "admin" -f ./fake-tree.ldif
            break 1
        fi
        COUNT=$((COUNT + 1))
        if [ "$COUNT" -eq 10 ]; then
            break 1
        fi
    done
else
    printf '%s\n' "start openldap-custom -- failed" >&2
    exit 1
fi


