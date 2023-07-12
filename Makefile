DOCKER_ORG := ga4gh
DOCKER_REPO := ga4gh-starter-kit-drs
DOCKER_TAG := $(shell cat build.gradle | grep "^version" | cut -f 2 -d ' ' | sed "s/'//g")
DOCKER_IMG := ${DOCKER_ORG}/${DOCKER_REPO}:${DOCKER_TAG}
DEVDB := ga4gh-starter-kit.dev.db
JAR := build/libs/ga4gh-starter-kit-drs-${TAG}.jar

Nothing:
	@echo "No target provided. Stop"

# remove local dev db
.PHONY: clean-sqlite
clean-sqlite:
	@rm -f ${DEVDB}

.PHONY: clean-psql
clean-psql:
	psql -c "drop database starter_kit_db;" -U postgres

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
	@sqlite3 ${DEVDB} < database/sqlite/create-tables.sql

# populate the sqlite database with test data
.PHONY: sqlite-db-populate-dev-dataset
sqlite-db-populate-dev-dataset:
	@sqlite3 ${DEVDB} < database/sqlite/add-dev-dataset.sql

.PHONY: sqlite-db-refresh
sqlite-db-refresh: clean-sqlite sqlite-db-build sqlite-db-populate-dev-dataset

.PHONY: psql-db-build
psql-db-build:
	@psql -c "create database starter_kit_db;" -U postgres
	@psql starter_kit_db < database/postgresql/create-tables.sql -U postgres

.PHONY: psql-db-populate
psql-db-populate:
	@psql starter_kit_db < database/postgresql/add-dev-dataset.sql -U postgres

.PHONY: psql-db-build-populate
psql-db-build-populate: psql-db-build psql-db-populate

.PHONY: psql-db-refresh
psql-db-refresh: clean-psql psql-db-build-populate

# create jar file
.PHONY: jar-build
jar-build:
	@./gradlew bootJar

# execute jar file
.PHONY: jar-run
jar-run:
	java -jar ${JAR}

# build docker image
.PHONY: docker-build
docker-build: jar-build
	docker build --platform linux/amd64 -t ${DOCKER_IMG} --build-arg VERSION=${DOCKER_TAG} .

# push image to docker hub
.PHONY: docker-publish
docker-publish:
	docker image push ${DOCKER_IMG}
