import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String department;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Student(String id, String name, int age, String department) {
        super(id, name, age);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<Enrollment> getEnrollments() { return enrollments; }

    public String getStudentId() {
        return getId(); 
    }
    

    public void addGrade(String courseId, Grade grade) {
        for (Enrollment e : enrollments) {
            if (e.getCourse().getCourseId().equals(courseId)) {
                e.setGrade(grade);
                return;
            }
        }
    }
}
