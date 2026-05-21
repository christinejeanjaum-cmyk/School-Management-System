package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Standalone bar graph panel for the Teacher Dashboard.
 * It displays enrolled students per grade level (Grade 7 through Grade 12)
 * and colors the bars from darkest green for the largest count to lightest green for the smallest.
 */
public final class TeacherDashboardBarGraph extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/knhs_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final int[] GRADE_ORDER = {7, 8, 9, 10, 11, 12};
    private static final int BAR_WIDTH = 40;
    private static final int BAR_SPACING = 30;
    private static final int LEFT_MARGIN = 70;
    private static final int RIGHT_MARGIN = 40;
    private static final int TOP_MARGIN = 40;
    private static final int BOTTOM_MARGIN = 80;
    private static final Color BACKGROUND = Color.WHITE;
    private static final Color AXIS_COLOR = Color.BLACK;
    private static final Color LABEL_COLOR = Color.BLACK;
    private static final Color[] BAR_COLORS = {
        Color.decode("#006400"),
        Color.decode("#2E8B57"),
        Color.decode("#32CD32"),
        Color.decode("#66CD00"),
        Color.decode("#7CCD7C"),
        Color.decode("#90EE90")
    };
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font BAR_LABEL_FONT = new Font("Arial", Font.PLAIN, 12);

    private final Map<Integer, GradeData> gradeData = new LinkedHashMap<>();

    public TeacherDashboardBarGraph(final String teacherUsername) {
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(640, 420));
        initializeGradeData();
        loadDataForTeacher(teacherUsername);
    }

    private void initializeGradeData() {
        for (int grade : GRADE_ORDER) {
            gradeData.put(grade, new GradeData(grade));
        }
    }

    private void loadDataForTeacher(final String teacherUsername) {
        Objects.requireNonNull(teacherUsername, "teacherUsername must not be null");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // MySQL driver is expected to be available on the classpath.
        }

        final String teacherIdSql = "SELECT teacher_id FROM users WHERE username = ? AND role = 'teacher' LIMIT 1";
        final String sectionCountsSql = "SELECT s.grade_level, sec.section_name, COUNT(DISTINCT e.student_no) AS student_count "
                + "FROM jhs_enrollment_subjects es "
                + "JOIN jhs_enrollment e ON es.enroll_id = e.enroll_id "
                + "JOIN jhs_students s ON e.student_no = s.student_no "
                + "JOIN jhs_sections sec ON s.section_id = sec.section_id "
                + "WHERE es.teacher_id = ? "
                + "GROUP BY s.grade_level, sec.section_id, sec.section_name "
                + "UNION ALL "
                + "SELECT s.grade_level, sec.section_name, COUNT(DISTINCT e.student_no) AS student_count "
                + "FROM shs_enrollment_subjects es "
                + "JOIN shs_enrollment e ON es.enroll_id = e.enroll_id "
                + "JOIN shs_students s ON e.student_no = s.student_no "
                + "JOIN shs_sections sec ON s.section_id = sec.section_id "
                + "WHERE es.teacher_id = ? "
                + "GROUP BY s.grade_level, sec.section_id, sec.section_name";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement teacherIdStatement = connection.prepareStatement(teacherIdSql)) {

            teacherIdStatement.setString(1, teacherUsername);
            try (ResultSet resultSet = teacherIdStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return;
                }
                final int teacherId = resultSet.getInt("teacher_id");

                try (PreparedStatement sectionCountsStatement = connection.prepareStatement(sectionCountsSql)) {
                    sectionCountsStatement.setInt(1, teacherId);
                    sectionCountsStatement.setInt(2, teacherId);
                    try (ResultSet records = sectionCountsStatement.executeQuery()) {
                        final List<GradeData> collected = new ArrayList<>();
                        while (records.next()) {
                            final int gradeLevel = records.getInt("grade_level");
                            final String sectionName = records.getString("section_name");
                            final int studentCount = records.getInt("student_count");
                            if (gradeData.containsKey(gradeLevel)) {
                                collected.add(new GradeData(gradeLevel, sectionName, studentCount));
                            }
                        }
                        assignTopSectionPerGrade(collected);
                    }
                }
            }
        } catch (SQLException ignored) {
            // If the database is unavailable, the graph still renders with zero values.
        }
    }

    private void assignTopSectionPerGrade(final List<GradeData> collected) {
        for (GradeData gradeEntry : collected) {
            final GradeData existing = gradeData.get(gradeEntry.gradeLevel);
            if (existing == null) {
                continue;
            }
            if (gradeEntry.studentCount > existing.studentCount
                    || (gradeEntry.studentCount == existing.studentCount
                    && gradeEntry.sectionName.compareToIgnoreCase(existing.sectionName) < 0)) {
                gradeData.put(gradeEntry.gradeLevel, gradeEntry);
            }
        }
        final List<GradeData> sortedGrades = new ArrayList<>(gradeData.values());
        sortedGrades.sort(Comparator.comparingInt(GradeData::getStudentCount).reversed()
                .thenComparing(GradeData::getGradeLevel));
        for (int index = 0; index < sortedGrades.size(); index++) {
            sortedGrades.get(index).setBarColor(BAR_COLORS[index]);
        }
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(BACKGROUND);
            g.fillRect(0, 0, getWidth(), getHeight());
            drawTitle(g);
            drawAxes(g);
            drawBars(g);
        } finally {
            g.dispose();
        }
    }

    private void drawTitle(final Graphics2D g) {
        g.setFont(TITLE_FONT);
        g.setColor(LABEL_COLOR);
        final String title = "ENROLLED STUDENTS PER GRADE LEVEL";
        final int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - titleWidth) / 2, TOP_MARGIN - 10);
    }

    private void drawAxes(final Graphics2D g) {
        final int width = getWidth();
        final int height = getHeight();
        final int yAxisX = LEFT_MARGIN;
        final int xAxisY = height - BOTTOM_MARGIN;
        g.setColor(AXIS_COLOR);
        g.drawLine(yAxisX, TOP_MARGIN, yAxisX, xAxisY);
        g.drawLine(yAxisX, xAxisY, width - RIGHT_MARGIN, xAxisY);
    }

    private void drawBars(final Graphics2D g) {
        final int chartHeight = getHeight() - TOP_MARGIN - BOTTOM_MARGIN;
        final int maxCount = gradeData.values().stream().mapToInt(GradeData::getStudentCount).max().orElse(0);
        final int barBaseY = getHeight() - BOTTOM_MARGIN;
        final int totalBars = GRADE_ORDER.length;
        final int chartWidth = totalBars * BAR_WIDTH + (totalBars - 1) * BAR_SPACING;
        final int startX = LEFT_MARGIN;

        g.setFont(BAR_LABEL_FONT);
        for (int index = 0; index < GRADE_ORDER.length; index++) {
            final int grade = GRADE_ORDER[index];
            final GradeData gradeInfo = gradeData.get(grade);
            final int x = startX + index * (BAR_WIDTH + BAR_SPACING);
            final int count = gradeInfo.studentCount;
            final int barHeight = maxCount == 0 ? 0 : (int) ((double) count / maxCount * chartHeight);
            final int y = barBaseY - barHeight;

            g.setColor(gradeInfo.barColor);
            g.fillRect(x, y, BAR_WIDTH, barHeight);
            g.setColor(AXIS_COLOR);
            g.drawRect(x, y, BAR_WIDTH, barHeight);

            if (count > 0) {
                final String topLabel = gradeInfo.sectionName + " " + count;
                final FontMetrics metrics = g.getFontMetrics();
                final int labelWidth = metrics.stringWidth(topLabel);
                final int labelX = x + (BAR_WIDTH - labelWidth) / 2;
                final int labelY = y - 8;
                g.setColor(LABEL_COLOR);
                g.drawString(topLabel, labelX, Math.max(labelY, TOP_MARGIN + metrics.getAscent()));
            }

            final String gradeLabel = "Grade " + grade;
            final FontMetrics metrics = g.getFontMetrics();
            final int labelWidth = metrics.stringWidth(gradeLabel);
            final int labelX = x + (BAR_WIDTH - labelWidth) / 2;
            final int labelY = barBaseY + metrics.getHeight() + 4;
            g.setColor(LABEL_COLOR);
            g.drawString(gradeLabel, labelX, labelY);
        }
    }

    private static final class GradeData {
        private final int gradeLevel;
        private final String sectionName;
        private final int studentCount;
        private Color barColor;

        GradeData(final int gradeLevel) {
            this(gradeLevel, "", 0);
        }

        GradeData(final int gradeLevel, final String sectionName, final int studentCount) {
            this.gradeLevel = gradeLevel;
            this.sectionName = sectionName == null ? "" : sectionName;
            this.studentCount = studentCount;
            this.barColor = BAR_COLORS[5];
        }

        int getGradeLevel() {
            return gradeLevel;
        }

        int getStudentCount() {
            return studentCount;
        }

        void setBarColor(final Color color) {
            if (color != null) {
                barColor = color;
            }
        }
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final TeacherDashboardBarGraph graph = new TeacherDashboardBarGraph("teacher1");
            final JFrame frame = new JFrame("Teacher Dashboard - Enrolled Students Per Grade Level");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(graph);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
