package model;

public class Subject {
    private int subj_id;
    private String subj_code;
    private String subj_name;
    private int grade_level;
    private String track;
    private String strand;
    private int semester;
    private int units;
    private String description;

    public Subject() {}

    public Subject(int subj_id, String subj_code, String subj_name, int grade_level,
                   String track, String strand, int semester, int units, String description) {
        this.subj_id = subj_id;
        this.subj_code = subj_code;
        this.subj_name = subj_name;
        this.grade_level = grade_level;
        this.track = track;
        this.strand = strand;
        this.semester = semester;
        this.units = units;
        this.description = description;
    }

    public int getSubjId() { return subj_id; }
    public void setSubjId(int subj_id) { this.subj_id = subj_id; }
    public String getSubjCode() { return subj_code; }
    public void setSubjCode(String subj_code) { this.subj_code = subj_code; }
    public String getSubjName() { return subj_name; }
    public void setSubjName(String subj_name) { this.subj_name = subj_name; }
    public int getGradeLevel() { return grade_level; }
    public void setGradeLevel(int grade_level) { this.grade_level = grade_level; }
    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }
    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
