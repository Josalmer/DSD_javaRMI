#!/bin/sh -e
echo
echo "Lanzando un cliente"

echo
java -cp . -Djava.security.policy=server.policy Client

