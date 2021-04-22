ORG := $(shell cat build.gradle | grep "^group" | cut -f 2 -d ' ' | sed "s/'//g")
REPO := $(shell cat settings.gradle | grep "rootProject.name" | cut -f 3 -d ' ' | sed "s/'//g")
TAG := $(shell cat build.gradle | grep "^version" | cut -f 2 -d ' ' | sed "s/'//g")
IMG := ${ORG}/${REPO}:${TAG}
DEVDB := ga4gh-starter-kit.dev.db
JAR := ga4gh-starter-kit-drs.jar

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
.PHONY: sqlite-db-build
sqlite-db-build: clean-sqlite
	@sqlite3 ${DEVDB} < database/sqlite/create-schema.migrations.sql

# populate the sqlite database with test data
.PHONY: sqlite-db-populate-dev-dataset
sqlite-db-populate-dev-dataset:
	@sqlite3 ${DEVDB} < database/sqlite/populate-dev-dataset.migrations.sql

.PHONY: sqlite-db-refresh
sqlite-db-refresh: clean-sqlite sqlite-db-build sqlite-db-populate-dev-dataset

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
run-development-local: clean-all sqlite-db-build sqlite-db-populate-dev-dataset jar-build jar-run

# run application in development mode (to be used inside docker container)
.PHONY: run-development-docker
run-development-docker: sqlite-db-build sqlite-db-populate-dev-dataset
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
