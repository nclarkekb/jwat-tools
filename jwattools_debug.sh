#!/bin/sh
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 -Xms256m -Xmx1024m -XX:PermSize=64M -XX:MaxPermSize=256M -jar target/jwat-tools-*-jar-with-dependencies.jar $@
