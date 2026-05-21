package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DBConnection {
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DATABASE = "knhs_db";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_TIMEZONE = "Asia/Manila";
    private static final Properties CONFIG = loadConfig();

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(buildUrl(), dbUser(), dbPassword());
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL JDBC driver not found. Add mysql-connector-java.jar to the classpath.", ex);
        }
    }

    private static String buildUrl() {
        String host = config("DB_HOST", DEFAULT_HOST);
        String port = config("DB_PORT", DEFAULT_PORT);
        String database = config("DB_NAME", DEFAULT_DATABASE);
        String timezone = config("DB_TIMEZONE", DEFAULT_TIMEZONE);
        return "jdbc:mysql://" + host + ":" + port + "/" + database
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=" + timezone;
    }

    private static String dbUser() {
        return config("DB_USER", DEFAULT_USER);
    }

    private static String dbPassword() {
        return config("DB_PASSWORD", DEFAULT_PASSWORD);
    }

    private static String config(String key, String defaultValue) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        String prop = CONFIG.getProperty(key);
        return prop == null || prop.isBlank() ? defaultValue : prop.trim();
    }

    private static Properties loadConfig() {
        Properties props = new Properties();
        for (Path candidate : new Path[]{
                Paths.get("db.properties"),
                Paths.get("config", "db.properties"),
                Paths.get("SCHOOL MANAGEMENT SYSTEM", "db.properties"),
                Paths.get("SCHOOL MANAGEMENT SYSTEM", "config", "db.properties")}) {
            if (!Files.exists(candidate)) {
                continue;
            }
            try (InputStream in = Files.newInputStream(candidate)) {
                props.load(in);
                break;
            } catch (IOException ignored) {
            }
        }
        return props;
    }
}
