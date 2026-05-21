package model;

public class Enrollment {
    private int enroll_id;
    private String student_no;
    private String lrn;
    private String school_year;
    private String semester;
    private String date_enrolled;
    private String status;
    private int student_id;
    private int sy_id;
    private String track;
    private String strand;

    public Enrollment() {}

    public int getEnrollId() { return enroll_id; }
    public void setEnrollId(int enroll_id) { this.enroll_id = enroll_id; }
    public String getStudentNo() { return student_no; }
    public void setStudentNo(String student_no) { this.student_no = student_no; }
    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }
    public String getSchoolYear() { return school_year; }
    public void setSchoolYear(String school_year) { this.school_year = school_year; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public String getDateEnrolled() { return date_enrolled; }
    public void setDateEnrolled(String date_enrolled) { this.date_enrolled = date_enrolled; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getStudentId() { return student_id; }
    public void setStudentId(int student_id) { this.student_id = student_id; }
    public int getSyId() { return sy_id; }
    public void setSyId(int sy_id) { this.sy_id = sy_id; }
    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }
    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }
}
