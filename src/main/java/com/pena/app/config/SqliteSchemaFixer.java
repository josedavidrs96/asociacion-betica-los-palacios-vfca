package com.pena.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Asegura que en SQLite existan las columnas requeridas con valores por defecto,
 * porque SQLite no puede añadir columnas NOT NULL sin DEFAULT mediante ALTER TABLE.
 */
@Component
@Order(0)
public class SqliteSchemaFixer implements CommandLineRunner {

    private final DataSource dataSource;

    public SqliteSchemaFixer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection c = dataSource.getConnection();
             Statement st = c.createStatement()) {

            // users.enabled
            if (tableExists(st, "users") && !columnExists(st, "users", "enabled")) {
                st.execute("ALTER TABLE users ADD COLUMN enabled INTEGER NOT NULL DEFAULT 1");
            }

            // bus_pass.active
            if (tableExists(st, "bus_pass") && !columnExists(st, "bus_pass", "active")) {
                st.execute("ALTER TABLE bus_pass ADD COLUMN active INTEGER NOT NULL DEFAULT 1");
            }

            // member.email_verified y member.address_verified
            if (tableExists(st, "member")) {
                if (!columnExists(st, "member", "email_verified")) {
                    st.execute("ALTER TABLE member ADD COLUMN email_verified INTEGER NOT NULL DEFAULT 0");
                }
                if (!columnExists(st, "member", "address_verified")) {
                    st.execute("ALTER TABLE member ADD COLUMN address_verified INTEGER NOT NULL DEFAULT 0");
                }
            }
        }
    }

    private boolean tableExists(Statement st, String table) throws Exception {
        try (ResultSet rs = st.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'")) {
            return rs.next();
        }
    }

    private boolean columnExists(Statement st, String table, String column) throws Exception {
        try (ResultSet rs = st.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (rs.next()) {
                String colName = rs.getString("name");
                if (column.equalsIgnoreCase(colName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
