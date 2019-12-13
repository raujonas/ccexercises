#!/bin/bash
if [ "$DBMODE" = "jdbc" ]
then
    while ! mysqladmin ping -h"$DBHOST" --silent; do
        sleep 1
    done
    java -jar notebookapp-0.3.0.jar db migrate config.yml
fi
java -jar notebookapp-0.3.0.jar server config.yml
