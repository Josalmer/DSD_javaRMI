#!/bin/sh -e
echo
echo "Lanzando el ligador de RMI... "
rmiregistry &

echo
echo "Compilando con javac ..."
javac *.java

sleep 2

echo
echo "Lanzando Replicas"
java -cp . -Djava.rmi.server.codebase=file:./ -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy MSF