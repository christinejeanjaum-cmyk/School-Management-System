package model;

public class Grades {
    private int grade_id;
    private int student_id;
    private int subj_id;
    private int offering_id;
    private double first_grading;
    private double second_grading;
    private double third_grading;
    private double fourth_grading;
    private double prelim;
    private double midterm;
    private double finals;
    private double average;
    private String remarks;

    public Grades() {}

    public int getGradeId() { return grade_id; }
    public void setGradeId(int grade_id) { this.grade_id = grade_id; }
    public int getStudentId() { return student_id; }
    public void setStudentId(int student_id) { this.student_id = student_id; }
    public int getSubjId() { return subj_id; }
    public void setSubjId(int subj_id) { this.subj_id = subj_id; }
    public int getOfferingId() { return offering_id; }
    public void setOfferingId(int offering_id) { this.offering_id = offering_id; }
    public double getFirstGrading() { return first_grading; }
    public void setFirstGrading(double first_grading) { this.first_grading = first_grading; }
    public double getSecondGrading() { return second_grading; }
    public void setSecondGrading(double second_grading) { this.second_grading = second_grading; }
    public double getThirdGrading() { return third_grading; }
    public void setThirdGrading(double third_grading) { this.third_grading = third_grading; }
    public double getFourthGrading() { return fourth_grading; }
    public void setFourthGrading(double fourth_grading) { this.fourth_grading = fourth_grading; }
    public double getPrelim() { return prelim; }
    public void setPrelim(double prelim) { this.prelim = prelim; }
    public double getMidterm() { return midterm; }
    public void setMidterm(double midterm) { this.midterm = midterm; }
    public double getFinals() { return finals; }
    public void setFinals(double finals) { this.finals = finals; }
    public double getAverage() { return average; }
    public void setAverage(double average) { this.average = average; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
