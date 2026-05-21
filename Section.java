package model;

public class Section {
    private int section_id;
    private String section_name;
    private int grade_level;
    private String track;
    private String strand;
    private String adviser_name;
    private String room_number;

    public Section() {}

    public Section(int section_id, String section_name, int grade_level, String track,
                   String strand, String adviser_name, String room_number) {
        this.section_id = section_id;
        this.section_name = section_name;
        this.grade_level = grade_level;
        this.track = track;
        this.strand = strand;
        this.adviser_name = adviser_name;
        this.room_number = room_number;
    }

    public int getSectionId() { return section_id; }
    public void setSectionId(int section_id) { this.section_id = section_id; }
    public String getSectionName() { return section_name; }
    public void setSectionName(String section_name) { this.section_name = section_name; }
    public int getGradeLevel() { return grade_level; }
    public void setGradeLevel(int grade_level) { this.grade_level = grade_level; }
    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }
    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }
    public String getAdviserName() { return adviser_name; }
    public void setAdviserName(String adviser_name) { this.adviser_name = adviser_name; }
    public String getRoomNumber() { return room_number; }
    public void setRoomNumber(String room_number) { this.room_number = room_number; }
}
