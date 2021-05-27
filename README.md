<img src="https://www.ga4gh.org/wp-content/themes/ga4gh-theme/gfx/GA-logo-horizontal-tag-RGB.svg" alt="GA4GH Logo" style="width: 400px;"/>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![Java 11+](https://img.shields.io/badge/java-11+-blue.svg?style=flat-square)](https://www.java.com)
[![Gradle 6.1+](https://img.shields.io/badge/gradle-6.1+-blue.svg?style=flat-square)](https://gradle.org/)
[![Travis (.org) branch](https://img.shields.io/travis/ga4gh/ga4gh-starter-kit-drs/master.svg?style=flat-square)](https://travis-ci.org/ga4gh/ga4gh-starter-kit-drs)
![Codecov](https://img.shields.io/codecov/c/github/ga4gh/ga4gh-starter-kit-drs?style=flat-square)

# GA4GH Starter Kit DRS

Part of the GA4GH Starter Kit. Open source reference implementation of the GA4GH [Data Repository Service (DRS) specification](https://github.com/ga4gh/data-repository-service-schemas)

## Running the DRS service

### Docker

We recommend running the DRS service as a docker container for most contexts. Images can be downloaded from [docker hub](https://hub.docker.com/repository/docker/ga4gh/ga4gh-starter-kit-drs) and run, for example:

Pull the image:
```
docker pull ga4gh/ga4gh-starter-kit-drs:latest
```

Run container with default settings:
```
docker run -p 8080:8080 ga4gh/ga4gh-starter-kit-drs:latest
```

OR, run container with config file overriding defaults
```
docker run -p 8080:8080 ga4gh/ga4gh-starter-kit-drs:latest java -jar ga4gh-starter-kit-drs.jar -c path/to/config.yml
```

### Native

The service can also be installed locally in cases where docker deployments are not possible, or for development of the codebase. Native installations require:
* Java 11+
* Gradle 6.1.1+
* SQLite (for creating the dev database)

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

## Development

```
make sqlite-db-refresh
```

## Configuration

Please the the [Configuration page](./CONFIGURATION.md) for instructions on how to configure the DRS service with custom properties.

## Datasets

The following datasets are currently contained in this repo for development and testing:
* Test data from the [htsjdk](https://github.com/samtools/htsjdk)
* Open dataset of 384 Phenopackets: [Paper](https://pubmed.ncbi.nlm.nih.gov/32755546/), [Dataset](https://zenodo.org/record/3905420#.YArkBzpKhPZ)