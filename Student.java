package model;

import java.sql.Date;

public class Student {
    private int student_id;
    private String student_no;
    private String lrn;
    private String last_name;
    private String first_name;
    private String middle_name;
    private String gender;
    private Date birthdate;
    private String address;
    private String contact_no;
    private String guardian_name;
    private String guardian_contact;
    private int grade_level;
    private String track;
    private String strand;
    private int section_id;
    private Date date_enrolled;
    private String status;

    public Student() {}

    public int getStudentId() { return student_id; }
    public void setStudentId(int student_id) { this.student_id = student_id; }
    public String getStudentNo() { return student_no; }
    public void setStudentNo(String student_no) { this.student_no = student_no; }
    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }
    public String getLastName() { return last_name; }
    public void setLastName(String last_name) { this.last_name = last_name; }
    public String getFirstName() { return first_name; }
    public void setFirstName(String first_name) { this.first_name = first_name; }
    public String getMiddleName() { return middle_name; }
    public void setMiddleName(String middle_name) { this.middle_name = middle_name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthdate() { return birthdate; }
    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNo() { return contact_no; }
    public void setContactNo(String contact_no) { this.contact_no = contact_no; }
    public String getGuardianName() { return guardian_name; }
    public void setGuardianName(String guardian_name) { this.guardian_name = guardian_name; }
    public String getGuardianContact() { return guardian_contact; }
    public void setGuardianContact(String guardian_contact) { this.guardian_contact = guardian_contact; }
    public int getGradeLevel() { return grade_level; }
    public void setGradeLevel(int grade_level) { this.grade_level = grade_level; }
    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }
    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }
    public int getSectionId() { return section_id; }
    public void setSectionId(int section_id) { this.section_id = section_id; }
    public Date getDateEnrolled() { return date_enrolled; }
    public void setDateEnrolled(Date date_enrolled) { this.date_enrolled = date_enrolled; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
