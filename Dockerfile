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

WORKDIR /usr/src/app
COPY --from=builder /usr/local/bin/sqlite3 /usr/local/bin/sqlite3
COPY build/drs-server.jar drs-server.jar

CMD ["java", "-jar", "drs-server.jar", "--mode", "development"]
