#!/bin/bash

if [ "$mode" = "development" ]; then
    sqlite3 /usr/src/app/test.db < /usr/src/app/database/create-schema.migrations.sql \
    && python scripts/populate-tables.py /usr/src/app/resources/ /usr/src/app/test.db;
fi
java -jar drs-server.jar --mode "$mode"
