package org.ga4gh.starterkit.drs.config;

import org.ga4gh.starterkit.common.config.DatabaseProps;

import java.util.Properties;

import static org.ga4gh.starterkit.common.constant.DatabasePropsConstants.*;

/**
 * @author dashrath
 */
public class DrsDatabaseProps extends DatabaseProps {

    private String url;
    private String username;
    private String password;
    private String poolSize;
    private String showSQL;

    // constants for mysql db type
    public static final String MYSQL_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_DIALECT = "org.hibernate.dialect.MySQL8Dialect";

    public Properties getAllProperties() {
        Properties props = new Properties();

        // set common properties across any db type: url, username, password,
        // pool_size, show_sql,
        props.setProperty("hibernate.connection.url", getUrl());

        if (!getUsername().equals("")) {
            props.setProperty("hibernate.connection.username", getUsername());
        }

        if (!getPassword().equals("")) {
            props.setProperty("hibernate.connection.password", getPassword());
        }

        props.setProperty("hibernate.connection.pool_size", getPoolSize());
        props.setProperty("hibernate.show_sql", getShowSQL());

        // set hardcoded properties: current_session_context_class
        props.setProperty("hibernate.current_session_context_class", DEFAULT_CURRENT_SESSION_CONTEXT_CLASS);

        // infer database type (ie sqlite or postgresql) from the db connection url
        DatabaseType dbtype = getDatabaseTypeFromUrl(getUrl());

        switch(dbtype) {
            case mysql:
                assignMySQLProperties(props);
                break;

            case postrges:
                assignPostgresProperties(props);
                break;

            case sqlite:
                assignSqliteProperties(props);
                break;
        }

        return props;
    }

    private DatabaseType getDatabaseTypeFromUrl(String url) {

        if (url.startsWith("jdbc:sqlite")) {
            return DatabaseType.sqlite;
        }

        if (url.startsWith("jdbc:postgresql")) {
            return DatabaseType.postrges;
        }

        if (url.startsWith("jdbc:mysql")) {
            return DatabaseType.mysql;
        }

        throw new IllegalArgumentException("Invalid JDBC URL: MUST be a valid 'sqlite', 'postgresql', or 'mysql' JDBC URL");
    }

    private void assignSqliteProperties(Properties props) {
        props.setProperty("hibernate.connection.driver_class", SQLITE_DRIVER_CLASS);
        props.setProperty("hibernate.dialect", SQLITE_DIALECT);
        props.setProperty("hibernate.connection.date_class", SQLITE_DATE_CLASS);
    }

    private void assignPostgresProperties(Properties props) {
        props.setProperty("hibernate.connection.driver_class", POSTGRES_DRIVER_CLASS);
        props.setProperty("hibernate.dialect", POSTGRES_DIALECT);
    }

    private void assignMySQLProperties(Properties props) {
        props.setProperty("hibernate.connection.driver_class", MYSQL_DRIVER_CLASS);
        props.setProperty("hibernate.dialect", MYSQL_DIALECT);
    }
}
