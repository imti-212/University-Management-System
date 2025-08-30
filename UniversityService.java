import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class UniversityService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Student> students = new HashMap<>();
    private Map<String, Course> courses = new HashMap<>();
    private List<Enrollment> enrollments = new ArrayList<>();

    private IdGenerator idGen = new IdGenerator();

    private static final String STUDENT_FILE = "students.dat";
    private static final String COURSE_FILE = "courses.dat";
    private static final String ENROLLMENT_FILE = "enrollments.dat";

    public Student addStudent(String name, int age, String department) {
        String id = idGen.nextStudentId("134");
        Student s = new Student(id, name, age, department);
        students.put(id, s);
        saveStudents();
        return s;
    }

    public void updateStudent(String id, String name, int age, String department) {
        Student s = students.get(id);
        if (s != null) {
            s.setName(name);
            s.setAge(age);
            s.setDepartment(department);
            saveStudents();
        }
    }

    public Collection<Student> allStudents() {
        return students.values();
    }

    public Student getStudent(String id) {
        return students.get(id);
    }

    public Course addCourse(String name, int credit, String department) {
        String id = idGen.nextCourseId();
        Course c = new Course(id, name, credit, department);
        courses.put(id, c);
        saveCourses();
        return c;
    }

    public void updateCourse(String id, String name, int credit, String department) {
        Course c = courses.get(id);
        if (c != null) {
            c.setCourseName(name);
            c.setCredit(credit);
            c.setDepartment(department);
            saveCourses();
        }
    }

    public Collection<Course> allCourses() {
        return courses.values();
    }

    public Course getCourse(String id) {
        return courses.get(id);
    }

    public void enroll(String studentId, String courseId) {
        Student s = students.get(studentId);
        Course c = courses.get(courseId);
        if (s != null && c != null) {
            for (Enrollment existing : enrollments) {
                if (existing.getStudent().equals(s) && existing.getCourse().equals(c)) {
                    JOptionPane.showMessageDialog(null, "Student is already enrolled in this course");
                    return;
                }
            }

            Enrollment e = new Enrollment(s, c);
            enrollments.add(e);
            s.getEnrollments().add(e);

            saveEnrollments();
        }
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void assignGrade(String studentId, String courseId, Grade grade) {
        for (Enrollment e : enrollments) {
            if (e.getStudent().getId().equals(studentId) &&
                e.getCourse().getCourseId().equals(courseId)) {
                e.setGrade(grade);
                e.getStudent().addGrade(courseId, grade);
                saveEnrollments();
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STUDENT_FILE))) {
            students = (Map<String, Student>) in.readObject();
        } catch (Exception ignored) {}

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(COURSE_FILE))) {
            courses = (Map<String, Course>) in.readObject();
        } catch (Exception ignored) {}

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ENROLLMENT_FILE))) {
            enrollments = (List<Enrollment>) in.readObject();
        } catch (Exception ignored) {}

        for (Enrollment e : enrollments) {
            Student s = students.get(e.getStudent().getId());
            if (s != null) {
                s.getEnrollments().add(e);
            }
        }
    }


    public void saveStudents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STUDENT_FILE))) {
            out.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCourses() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(COURSE_FILE))) {
            out.writeObject(courses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveEnrollments() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ENROLLMENT_FILE))) {
            out.writeObject(enrollments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        saveStudents();
        saveCourses();
        saveEnrollments();
    }

    public List<Student> studentsInCourse(String courseId) {
        List<Student> list = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getCourse().getCourseId().equals(courseId)) {
                list.add(e.getStudent());
            }
        }
        return list;
    }


    public double calculateGPA(String studentId) {
        double points = 0.0;
        int credits = 0;

        for (Enrollment e : enrollments) {
            if (e.getStudent().getId().equals(studentId) && e.getGrade() != null) {
                points += e.getGrade().points * e.getCourse().getCredit();
                credits += e.getCourse().getCredit();
            }
        }
        return credits == 0 ? 0.0 : points / credits;
    }

    public double calculateGPA(Student s) {
        return calculateGPA(s.getId());
    }

    public double gradeInCourse(String studentId, String courseId) {
        for (Enrollment e : enrollments) {
            if (e.getStudent().getId().equals(studentId) && e.getCourse().getCourseId().equals(courseId)) {
                return e.getGrade() == null ? 0.0 : e.getGrade().points;
            }
        }
        return 0.0;
    }


    public void deleteEnrollment(String studentId, String courseId) {
        Iterator<Enrollment> it = enrollments.iterator();
        while (it.hasNext()) {
            Enrollment e = it.next();
            if (e.getStudent().getId().equals(studentId) &&
                e.getCourse().getCourseId().equals(courseId)) {
                it.remove(); 
                e.getStudent().getEnrollments().remove(e); 
                saveEnrollments();
                return;
            }
        }
    }


    public void deleteCourse(String courseId) {
        courses.remove(courseId);

        Iterator<Enrollment> it = enrollments.iterator();
        while (it.hasNext()) {
            Enrollment e = it.next();
            if (e.getCourse().getCourseId().equals(courseId)) {
                it.remove();
                e.getStudent().getEnrollments().remove(e);
            }
        }

        saveCourses();
        saveEnrollments();
    }

    public void deleteStudent(String studentId) {
        students.remove(studentId);

        Iterator<Enrollment> it = enrollments.iterator();
        while (it.hasNext()) {
            Enrollment e = it.next();
            if (e.getStudent().getId().equals(studentId)) {
                it.remove();
            }
        }

        saveStudents();
        saveEnrollments();
    }


    public static UniversityService loadOrCreate() {
        UniversityService service = new UniversityService();
        service.load();
        return service;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            UniversityService service = UniversityService.loadOrCreate();

            new LoginFrame(service).setVisible(true);
        });
    }
}
