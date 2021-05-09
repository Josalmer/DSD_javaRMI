#!/bin/sh -e

echo
java -cp . -Djava.security.policy=server.policy Client 4

