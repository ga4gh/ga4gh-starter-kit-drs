FROM openjdk:11.0.10 as builder

USER root

##################################################
# INSTALLATION OF DEPENDENCIES
##################################################

WORKDIR /usr/src/dependencies

RUN apt update
RUN apt install build-essential -y

# INSTALL SQLITE3
RUN wget https://www.sqlite.org/2021/sqlite-autoconf-3340100.tar.gz \
    && tar -zxf sqlite-autoconf-3340100.tar.gz \
    && cd sqlite-autoconf-3340100 \
    && ./configure \
    && make \
    && make install

FROM openjdk:11.0.10

USER root

ARG mode

WORKDIR /usr/src/app
COPY --from=builder /usr/local/bin/sqlite3 /usr/local/bin/sqlite3
COPY build/drs-server.jar drs-server.jar
COPY src/test/resources/ /usr/src/app/resources/
COPY database/sqlite/create-schema.migrations.sql /usr/src/app/database/create-schema.migrations.sql
COPY scripts /usr/src/app/scripts

ENV mode=${mode}
CMD ["scripts/run.sh"]
