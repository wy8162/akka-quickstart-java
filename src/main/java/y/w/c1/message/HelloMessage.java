package y.w.c1.message;

public class HelloMessage implements Command {
    final private String message;

    public HelloMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
