import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private String courseId;
    private String courseName;
    private int credit;
    private String department;

    public Course(String courseId, String courseName, int credit, String department) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credit = credit;
        this.department = department;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredit() { return credit; }
    public String getDepartment() { return department; }

    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCredit(int credit) { this.credit = credit; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() { return courseName + " (" + courseId + ")"; }
}