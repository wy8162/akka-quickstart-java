package y.w;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import y.w.test.PeopleActor;
import y.w.test.PeopleActor.Command;
import y.w.test.PeopleActor.HelloMessage;
import y.w.test.PeopleActor.TerminalMessage;

public class Main {
    private final ActorSystem system;
    private final ActorRef helloActor1;
    private final ActorRef helloActor2;
    private final LoggingAdapter log;

    public Main(final ActorSystem system) {
        this.system = system;

        this.log = Logging.getLogger(system, getClass().getName());

        this.helloActor1 = createHelloActor("1");
        this.helloActor2 = createHelloActor("2");
    }

    protected ActorRef createHelloActor(final String name) {
        return system.actorOf(PeopleActor.props(), "helloActor" + name);
    }

    public static void main(String[] args)
            throws InterruptedException, IOException, TimeoutException {

        final ActorSystem system = ActorSystem.create("AkkaSystem");

        final Main mainApp = new Main(system);

        mainApp.run();
    }

    private void run() throws IOException, TimeoutException, InterruptedException {
        commandLoop();
        Await.ready(system.whenTerminated(), Duration.Inf());
    }

    private void commandLoop() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = in.readLine();
            if (line == null) {
                system.terminate();
                break;
            } else {
                Command cmd = getMessage(line);
                if (cmd instanceof TerminalMessage) {
                    system.terminate();
                    break;
                }

                helloActor1.tell(cmd, ActorRef.noSender());
                helloActor2.tell(cmd, ActorRef.noSender());
            }
        }
    }

    private Command getMessage(final String line) {
        Pattern quitPattern = Pattern.compile("quit|q");

        if (quitPattern.matcher(line).matches()) return new TerminalMessage();
        return new HelloMessage(line);
    }
}
