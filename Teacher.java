package model;

public class Teacher {
    private int teacher_id;
    private String name;
    private String subject_taught;
    private String contact_no;
    private String email;
    private String level;

    public Teacher() {}

    public Teacher(int teacher_id, String name, String subject_taught, String contact_no, String email, String level) {
        this.teacher_id = teacher_id;
        this.name = name;
        this.subject_taught = subject_taught;
        this.contact_no = contact_no;
        this.email = email;
        this.level = level;
    }

    public int getTeacherId() { return teacher_id; }
    public void setTeacherId(int teacher_id) { this.teacher_id = teacher_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubjectTaught() { return subject_taught; }
    public void setSubjectTaught(String subject_taught) { this.subject_taught = subject_taught; }
    public String getContactNo() { return contact_no; }
    public void setContactNo(String contact_no) { this.contact_no = contact_no; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
