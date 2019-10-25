#!/bin/bash
echo "Stopping tomcat"
echo "---------------"
sh ./catalina.sh stop
echo "Starting tomcat again"
echo "---------------"
sh ./catalina.sh start