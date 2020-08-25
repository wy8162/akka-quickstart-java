package y.w;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskTimeoutException;
import akka.pattern.Patterns;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import y.w.c1.message.Command;
import y.w.c1.message.HelloMessage;
import y.w.c1.message.MyNameIs;
import y.w.c1.message.TerminalMessage;
import y.w.c1.message.WhatsYourName;
import y.w.test.PeopleActor;

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
            throws InterruptedException, IOException, TimeoutException, ExecutionException {

        final ActorSystem system = ActorSystem.create("AkkaSystem");

        final Main mainApp = new Main(system);

        mainApp.run();
    }

    private void run()
            throws IOException, TimeoutException, InterruptedException, ExecutionException {
        commandLoop();
        Await.ready(system.whenTerminated(), Duration.Inf());
    }

    private void commandLoop()
            throws IOException, TimeoutException, InterruptedException, ExecutionException {
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

                ActorSelection helloSelect1 = system.actorSelection("akka://AkkaSystem/user/helloActor1");
                helloSelect1.tell(cmd, ActorRef.noSender());
                ActorSelection helloSelect2 = system.actorSelection("akka://AkkaSystem/user/helloActor2");
                helloSelect2.tell(cmd, ActorRef.noSender());

                log.info(String.format("Actor %s is %s running", "akka://AkkaSystem/user/helloActor1", helloActor1.isTerminated() ? "NOT" : ""));
                log.info(String.format("Actor %s is %s running", "akka://AkkaSystem/user/helloActor2", helloActor2.isTerminated() ? "NOT" : ""));

                CompletionStage<Object> completionStage = Patterns.ask(helloActor1, new WhatsYourName(), java.time.Duration.ofSeconds(5));

                MyNameIs myNameIs = null;
                try {
                    myNameIs = (MyNameIs) completionStage.toCompletableFuture().get(6, TimeUnit.SECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    log.info("Caught AskTimeoutException. HelloActor1 is now " + (helloActor1.isTerminated() ? "terminated" : "ok"));
                }

                log.info(String.format("Actor %s responded %s", helloActor1.toString(), myNameIs.getName()));

            }
        }
    }

    private Command getMessage(final String line) {
        Pattern quitPattern = Pattern.compile("quit|q");

        if (quitPattern.matcher(line).matches()) return new TerminalMessage();
        return new HelloMessage(line);
    }
}
