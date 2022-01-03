public class Number {
    int val;
    boolean completed;

    public Number(int v) {
        val = v;
        completed = false;
    }

    public String toString() {
        return ("val: " + val + "\tcompleted? " + completed);
    }
}
