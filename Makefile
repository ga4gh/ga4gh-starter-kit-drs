ORG := $(shell cat project.properties | grep "docker.organization" | cut -f 2 -d '=')
REPO := $(shell cat project.properties | grep "docker.repo" | cut -f 2 -d '=')
TAG := $(shell cat project.properties | grep "docker.version" | cut -f 2 -d '=')
IMG := ${ORG}/${REPO}:${TAG}
DEVDB := drsdev.db

Nothing:
	@echo "No target provided. Stop"

.PHONY: clean
clean:
	@rm ${DEVDB}
	
.PHONY: run-development
run-development:
	@sqlite3 ${DEVDB} < database/sqlite/create-schema.migrations.sql
	@sqlite3 ${DEVDB} < database/sqlite/populate-tables.migrations.sql

.PHONY: run-production
run-production:
	@echo "cannot run in production yet"

.PHONY: docker-build
docker-build:
	docker build -t ${IMG} .

.PHONY: docker-publish
docker-publish: docker-build
	docker image push ${IMG}
