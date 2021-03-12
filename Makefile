ORG := $(shell cat project.properties | grep "docker.organization" | cut -f 2 -d '=')
REPO := $(shell cat project.properties | grep "docker.repo" | cut -f 2 -d '=')
TAG := $(shell cat project.properties | grep "docker.version" | cut -f 2 -d '=')
IMG := ${ORG}/${REPO}:${TAG}
DEVDB := drsdev.db
JAR := drs-server.jar

Nothing:
	@echo "No target provided. Stop"

# remove local dev db
.PHONY: clean-sqlite
clean-sqlite:
	@rm -f ${DEVDB}

# remove local jar
.PHONY: clean-jar
clean-jar:
	@rm -f ${JAR}

# remove local dev db and jar
.PHONY: clean-all
clean-all: clean-sqlite clean-jar

# create the sqlite database
.PHONY: sqlite-db-create
sqlite-db-build: clean-sqlite
	@sqlite3 ${DEVDB} < database/sqlite/create-schema.migrations.sql

# populate the sqlite database with test data
.PHONY: sqlite-db-populate
sqlite-db-populate:
	@sqlite3 ${DEVDB} < database/sqlite/populate-tables.migrations.sql

# create jar file
.PHONY: jar-build
jar-build:
	@./gradlew bootJar

# execute jar file
.PHONY: jar-run
jar-run:
	java -jar ${JAR}

# run application in development mode locally
.PHONY: run-development-local
run-development-local: clean-all sqlite-db-build sqlite-db-populate jar-build
	# TODO add jar-run to list of commands

# run application in development mode (to be used inside docker container)
.PHONY: run-development-docker
run-development-docker: sqlite-db-build sqlite-db-populate
	# TODO add jar-run to list of commands
	/bin/bash

# run application in production mode
.PHONY: run-production
run-production:
	@echo "cannot run in production yet"

# build docker image
.PHONY: docker-build
docker-build: jar-build
	docker build -t ${IMG} .

# create docker container and run server in development mode
.PHONY: docker-run-development
docker-run-development: docker-build
	docker run -it --rm ${IMG}

# create docker container and run server in production mode
.PHONY: docker-run-production
docker-run-production:
	@echo "not implemented yet"

# push image to docker hub
.PHONY: docker-publish
docker-publish: docker-build
	docker image push ${IMG}
