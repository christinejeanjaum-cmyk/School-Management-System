package model;

public class User {
    private int user_id;
    private String full_name;
    private String username;
    private String password;
    private String role;
    private String email;
    private String contact_no;
    private int teacher_id;
    private String account_status;

    public User() {}

    public User(int user_id, String full_name, String username, String password, String role, String email,
                String contact_no, int teacher_id, String account_status) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.contact_no = contact_no;
        this.teacher_id = teacher_id;
        this.account_status = account_status;
    }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }
    public String getFullName() { return full_name; }
    public void setFullName(String full_name) { this.full_name = full_name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContactNo() { return contact_no; }
    public void setContactNo(String contact_no) { this.contact_no = contact_no; }
    public int getTeacherId() { return teacher_id; }
    public void setTeacherId(int teacher_id) { this.teacher_id = teacher_id; }
    public String getAccountStatus() { return account_status; }
    public void setAccountStatus(String account_status) { this.account_status = account_status; }
}
