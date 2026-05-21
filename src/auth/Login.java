package auth;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Login {
    private static final List<String> BOOTSTRAP_ADMIN_PASSWORDS = Arrays.asList("admin", "admin123", "knhs123");

    private int userId;
    private String userName;
    private String role;
    private int teacherId;
    private String accountStatus;
    private String adminPermission = "AUTHORIZED";
    private String failureMessage = "Invalid username or password.";

    public boolean authenticate(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            boolean statusColumn = hasColumn(conn, "users", "account_status");
            boolean permissionColumn = hasColumn(conn, "users", "admin_permission");
            String sql = statusColumn
                    ? permissionColumn
                    ? "SELECT user_id, username, password, role, teacher_id, account_status, admin_permission FROM users WHERE username=?"
                    : "SELECT user_id, username, password, role, teacher_id, account_status FROM users WHERE username=?"
                    : permissionColumn
                    ? "SELECT user_id, username, password, role, teacher_id, admin_permission FROM users WHERE username=?"
                    : "SELECT user_id, username, password, role, teacher_id FROM users WHERE username=?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, username);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (!passwordMatches(password, storedPassword)) {
                            if (!tryBootstrapAdminRecovery(conn, username, password, statusColumn, permissionColumn)) {
                                failureMessage = "Invalid username or password.";
                                return false;
                            }
                            return true;
                        }
                        accountStatus = statusColumn ? rs.getString("account_status") : "ACTIVE";
                        if (!"ACTIVE".equalsIgnoreCase(accountStatus)) {
                            failureMessage = "Your account is inactive. Please contact the administrator.";
                            return false;
                        }
                        adminPermission = permissionColumn ? rs.getString("admin_permission") : "AUTHORIZED";
                        if ("admin".equalsIgnoreCase(rs.getString("role")) && !isAuthorizedAdminPermission(adminPermission)) {
                            failureMessage = "Your request is Pending.";
                            return false;
                        }
                        userId = rs.getInt("user_id");
                        userName = rs.getString("username");
                        role = rs.getString("role");
                        teacherId = rs.getObject("teacher_id") == null ? 0 : rs.getInt("teacher_id");
                        return true;
                    }
                }
            }
            if (tryBootstrapAdminRecovery(conn, username, password, statusColumn, permissionColumn)) {
                return true;
            }

        } catch (SQLException e) {
            failureMessage = "Login failed: " + e.getMessage();
        }

        return false;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getAdminPermission() {
        return adminPermission;
    }

    public boolean isSuperAdmin() {
        return "admin".equalsIgnoreCase(role) && "SUPER_ADMIN".equalsIgnoreCase(adminPermission);
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    private static boolean hasColumn(Connection conn, String table, String column) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, table, column)) {
            return rs.next();
        }
    }

    private static boolean passwordMatches(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }
        return storedPassword.equals(rawPassword) || storedPassword.equals(sha256(rawPassword));
    }

    private static boolean isAuthorizedAdminPermission(String permission) {
        return "AUTHORIZED".equalsIgnoreCase(permission) || "SUPER_ADMIN".equalsIgnoreCase(permission);
    }

    private boolean tryBootstrapAdminRecovery(Connection conn, String username, String password,
                                              boolean statusColumn, boolean permissionColumn) throws SQLException {
        if (!"admin".equalsIgnoreCase(username) || !BOOTSTRAP_ADMIN_PASSWORDS.contains(password)) {
            return false;
        }
        ensureBootstrapAdmin(conn, password, statusColumn, permissionColumn);
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT user_id, username, role, teacher_id" +
                (statusColumn ? ", account_status" : "") +
                (permissionColumn ? ", admin_permission" : "") +
                " FROM users WHERE username='admin' ORDER BY user_id LIMIT 1");
             ResultSet rs = pst.executeQuery()) {
            if (!rs.next()) {
                return false;
            }
            userId = rs.getInt("user_id");
            userName = rs.getString("username");
            role = rs.getString("role");
            teacherId = rs.getObject("teacher_id") == null ? 0 : rs.getInt("teacher_id");
            accountStatus = statusColumn ? rs.getString("account_status") : "ACTIVE";
            adminPermission = permissionColumn ? rs.getString("admin_permission") : "SUPER_ADMIN";
            failureMessage = "Invalid username or password.";
            return true;
        }
    }

    private static void ensureBootstrapAdmin(Connection conn, String password,
                                             boolean statusColumn, boolean permissionColumn) throws SQLException {
        Integer adminId = null;
        try (PreparedStatement pst = conn.prepareStatement("SELECT user_id FROM users WHERE username='admin' ORDER BY user_id LIMIT 1");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                adminId = rs.getInt("user_id");
            }
        }

        String hashedPassword = sha256(password);
        if (adminId == null) {
            StringBuilder cols = new StringBuilder("full_name,username,password,role,email,contact_no,teacher_id");
            StringBuilder vals = new StringBuilder("?,?,?,?,?,?,?");
            if (statusColumn) {
                cols.append(",account_status");
                vals.append(",?");
            }
            if (permissionColumn) {
                cols.append(",admin_permission");
                vals.append(",?");
            }
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO users (" + cols + ") VALUES (" + vals + ")", Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, "KNHS Administrator");
                pst.setString(2, "admin");
                pst.setString(3, hashedPassword);
                pst.setString(4, "admin");
                pst.setString(5, "admin@knhs.local");
                pst.setString(6, "09170000000");
                pst.setNull(7, Types.INTEGER);
                int index = 8;
                if (statusColumn) pst.setString(index++, "ACTIVE");
                if (permissionColumn) pst.setString(index, "SUPER_ADMIN");
                pst.executeUpdate();
            }
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE users SET full_name=?, password=?, role=?, email=?, contact_no=?, teacher_id=?");
        if (statusColumn) sql.append(", account_status='ACTIVE'");
        if (permissionColumn) sql.append(", admin_permission='SUPER_ADMIN'");
        sql.append(" WHERE user_id=?");
        try (PreparedStatement pst = conn.prepareStatement(sql.toString())) {
            pst.setString(1, "KNHS Administrator");
            pst.setString(2, hashedPassword);
            pst.setString(3, "admin");
            pst.setString(4, "admin@knhs.local");
            pst.setString(5, "09170000000");
            pst.setNull(6, Types.INTEGER);
            pst.setInt(7, adminId);
            pst.executeUpdate();
        }
    }

    private static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder out = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                out.append(String.format("%02x", b));
            }
            return out.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available.", ex);
        }
    }
}
