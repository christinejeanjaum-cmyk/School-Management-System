package model;

public class SubjectOffering {
    private int offering_id;
    private int subj_id;
    private int section_id;
    private int teacher_id;
    private String school_year;
    private int semester;

    public SubjectOffering() {}

    public SubjectOffering(int offering_id, int subj_id, int section_id, int teacher_id, String school_year, int semester) {
        this.offering_id = offering_id;
        this.subj_id = subj_id;
        this.section_id = section_id;
        this.teacher_id = teacher_id;
        this.school_year = school_year;
        this.semester = semester;
    }

    public int getOfferingId() { return offering_id; }
    public void setOfferingId(int offering_id) { this.offering_id = offering_id; }
    public int getSubjectId() { return subj_id; }
    public void setSubjectId(int subj_id) { this.subj_id = subj_id; }
    public int getSectionId() { return section_id; }
    public void setSectionId(int section_id) { this.section_id = section_id; }
    public int getTeacherId() { return teacher_id; }
    public void setTeacherId(int teacher_id) { this.teacher_id = teacher_id; }
    public String getSchoolYear() { return school_year; }
    public void setSchoolYear(String school_year) { this.school_year = school_year; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}
