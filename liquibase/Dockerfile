FROM liquibase/liquibase:4.33.0

COPY dbchangelog.xml .

CMD ["liquibase", "--changeLogFile=dbchangelog.xml", "update"]
