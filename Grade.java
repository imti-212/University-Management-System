import java.io.Serializable;

public enum Grade implements Serializable {
    A_PLUS(4.0), A(3.75), A_MINUS(3.50), B_PLUS(3.25), B(3.0), B_MINUS(2.75),
    C_PLUS(2.50), F_FAIL(0.0);

    public final double points;
    Grade(double p) { this.points = p; }

    @Override public String toString() {
        switch (this) {
            case A_PLUS: return "A+";
            case A: return "A";
            case A_MINUS: return "A-";
            case B_PLUS: return "B+";
            case B: return "B";
            case B_MINUS: return "B-";
            case C_PLUS: return "C+";
            case F_FAIL: return "F";
            default: return name();
        }
    }
}