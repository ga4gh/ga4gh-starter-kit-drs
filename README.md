<img src="https://www.ga4gh.org/wp-content/themes/ga4gh-theme/gfx/GA-logo-horizontal-tag-RGB.svg" alt="GA4GH Logo" style="width: 400px;"/>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![Java 11+](https://img.shields.io/badge/java-11+-blue.svg?style=flat-square)](https://www.java.com)
[![Gradle 7.3.2+](https://img.shields.io/badge/gradle-7.3.2+-blue.svg?style=flat-square)](https://gradle.org/)
[![GitHub Actions](https://img.shields.io/github/workflow/status/ga4gh/ga4gh-starter-kit-drs/Standard%20Tests/main)](https://github.com/ga4gh/ga4gh-starter-kit-drs/actions)
[![Codecov](https://img.shields.io/codecov/c/github/ga4gh/ga4gh-starter-kit-drs?style=flat-square)](https://app.codecov.io/gh/ga4gh/ga4gh-starter-kit-drs)

# GA4GH Starter Kit DRS

Part of the GA4GH Starter Kit. Open source reference implementation of the GA4GH [Data Repository Service (DRS) specification](https://github.com/ga4gh/data-repository-service-schemas)

## Running the DRS service

### Docker

We recommend running the DRS service as a docker container for most contexts. Images can be downloaded from [docker hub](https://hub.docker.com/repository/docker/ga4gh/ga4gh-starter-kit-drs). To download the image and run a container:

Pull the image:
```
docker pull ga4gh/ga4gh-starter-kit-drs:latest
```

Run container with default settings:
```
docker run -p 4500:4500 ga4gh/ga4gh-starter-kit-drs:latest
```

OR, run container with config file overriding defaults
```
docker run -p 4500:4500 ga4gh/ga4gh-starter-kit-drs:latest java -jar ga4gh-starter-kit-drs.jar -c path/to/config.yml
```

### Native

The service can also be installed locally in cases where docker deployments are not possible, or for development of the codebase. Native installations require:
* Java 11+
* Gradle 7.3.2+
* SQLite (for creating the dev database)

First, clone the repository from Github:
```
git clone https://github.com/ga4gh/ga4gh-starter-kit-drs.git
cd ga4gh-starter-kit-drs
```

The service can be run in development mode directly via gradle:

Run with all defaults
```
./gradlew bootRun
```

Run with config file
```
./gradlew bootRun --args="--config path/to/config.yml"
```

Alternatively, the service can be built as a jar and run:

Build jar:
```
./gradlew bootJar
```

Run with all defaults
```
java -jar build/libs/ga4gh-starter-kit-drs-${VERSION}.jar
```

Run with config file
```
java -jar build/libs/ga4gh-starter-kit-drs-${VERSION}.jar --config path/to/config.yml
```

### Confirm server is running

Whether running via docker or natively on a local machine, confirm the DRS API is up running by visiting its `service-info` endpoint, you should receive a valid `ServiceInfo` response.

```
GET http://localhost:4500/ga4gh/drs/v1/service-info

Response:
{
    "id": "org.ga4gh.starterkit.drs",
    "name": "GA4GH Starter Kit DRS Service",
    "description": "An open source, community-driven implementation of the GA4GH Data Repository Service (DRS)API specification.",
    "contactUrl": "mailto:info@ga4gh.org",
    "documentationUrl": "https://github.com/ga4gh/ga4gh-starter-kit-drs",
    "createdAt": "2020-01-15T12:00:00",
    "updatedAt": "2020-01-15T12:00:00",
    "environment": "test",
    "version": "0.1.0",
    "type": {
        "group": "org.ga4gh",
        "artifact": "drs",
        "version": "1.1.0"
    },
    "organization": {
        "name": "Global Alliance for Genomics and Health",
        "url": "https://ga4gh.org"
    }
}
```

## Development

Additional setup steps to run the DRS server in a local environment for development and testing.

### Setup dev database

A local SQLite database must be set up for running the DRS service in a development context. If `make` and `sqlite3` are already installed on the system `PATH`, this database can be created and populated with a dev dataset by simply running: 

```
make sqlite-db-refresh
```

This will create a SQLite database named `ga4gh-starter-kit.dev.db` in the current directory.

If `make` and/or `sqlite` are not installed, [this file](./database/sqlite/create-tables.sql) contains SQLite commands for creating the database schema, and [this file](./database/sqlite/add-dev-dataset.sql) contains SQLite commands for populating it with the dev dataset.

Confirm the DRS service can connect to the dev database by submitting a `DRS id` to the `/objects/{object_id}` endpoint. For example, a `DRS id` of `b8cd0667-2c33-4c9f-967b-161b905932c9` represents a root `DRS bundle` for a phenopacket test dataset:

```
GET http://localhost:4500/ga4gh/drs/v1/objects/b8cd0667-2c33-4c9f-967b-161b905932c9

Response:
{
    "id": "b8cd0667-2c33-4c9f-967b-161b905932c9",
    "description": "Open dataset of 384 phenopackets",
    "created_time": "2021-03-12T20:00:00Z",
    "name": "phenopackets.test.dataset",
    "size": 143601,
    "updated_time": "2021-03-13T12:30:45Z",
    "version": "1.0.0",
    "self_uri": "drs://localhost:4500/b8cd0667-2c33-4c9f-967b-161b905932c9",
    "contents": [
        {
            "name": "phenopackets.mundhofir.family",
            "drs_uri": [
                "drs://localhost:4500/1af5cdcf-898c-4dbc-944e-1ac95e82c0ea"
            ],
            "id": "1af5cdcf-898c-4dbc-944e-1ac95e82c0ea"
        },
        {
            "name": "phenopackets.zhang.family",
            "drs_uri": [
                "drs://localhost:4500/355a74bd-6571-4d4a-8602-a9989936717f"
            ],
            "id": "355a74bd-6571-4d4a-8602-a9989936717f"
        },
        {
            "name": "phenopackets.cao.family",
            "drs_uri": [
                "drs://localhost:4500/a1dd4ae2-8d26-43b0-a199-342b64c7dff6"
            ],
            "id": "a1dd4ae2-8d26-43b0-a199-342b64c7dff6"
        },
        {
            "name": "phenopackets.lalani.family",
            "drs_uri": [
                "drs://localhost:4500/c69a3d6c-4a28-4b7c-b215-0782f8d62429"
            ],
            "id": "c69a3d6c-4a28-4b7c-b215-0782f8d62429"
        }
    ]
}
```

**NOTE:** If running via docker, the dev database is already bundled within the container.

**NOTE:** The unit and end-to-end test suite is predicated on a preconfigured database. The SQLite dev database must be present for tests to pass.

## Configuration

Please see the [Configuration page](./CONFIGURATION.md) for instructions on how to configure the DRS service with custom properties.

## Dev datasets

Multiple datasets are currently contained in this repo for development and testing. Raw bytes are located in `src/test/resources` and the SQLite dev database is preconfigured with `DrsObjects` for each of these files. The following datasets are included:
* Test data from the [htsjdk](https://github.com/samtools/htsjdk)
  - see the [datasets page](./DATASETS.md#htsjdk) for a list of `DRS IDs` for `DrsObjects` in the htsjdk dataset
* Open dataset of 384 Phenopackets: [Paper](https://pubmed.ncbi.nlm.nih.gov/32755546/), [Dataset homepage](https://zenodo.org/record/3905420#.YArkBzpKhPZ)

  - see the [datasets page](./DATASETS.md#Phenopackets) for a list of `DRS IDs` for `DrsObjects` in the Phenopackets dataset

## Changelog

### v0.3.1

* Fixed a bug where admin requests to create a controlled access DRS object (i.e. with visas) did not complete successfully

### v0.3.0

* DRS object batch requests
* Passport support - Passport mediated auth to DRS objects (using Starter Kit implementation of Passports)
* Auth info - Discover Passport broker(s) and visa(s) for requested controlled access DRS Objects (single object and bulk request)

### v0.2.2

* patched log4j dependencies to v2.16.0 to avoid [Log4j Vulnerability](https://www.cisa.gov/uscert/apache-log4j-vulnerability-guidance)