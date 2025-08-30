import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, AtomicInteger> counters = new HashMap<>();

    
    public String nextStudentId(String deptId) {
        String semesterCode = getSemesterCode();
        String key = semesterCode + "-" + deptId;
        counters.putIfAbsent(key, new AtomicInteger(1));

        int serial = counters.get(key).getAndIncrement();
        return String.format("%s-%s-%03d", semesterCode, deptId, serial);
    }

    private String getSemesterCode() {
        LocalDate now = LocalDate.now();
        int year = now.getYear() % 100; 

        int month = now.getMonthValue();
        int semesterDigit;
        if (month >= 1 && month <= 4) semesterDigit = 1;
        else if (month >= 5 && month <= 8) semesterDigit = 2;
        else semesterDigit = 3;

        return year + "" + semesterDigit;
    }

    private final AtomicInteger courseSeq = new AtomicInteger(200);
    public String nextCourseId() {
        return "SWE" + "-" + courseSeq.getAndIncrement();
    }
}
