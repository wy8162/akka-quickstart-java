package y.w.c1.message;

public class MyNameIs implements Command {
    final private String name;

    public MyNameIs(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
