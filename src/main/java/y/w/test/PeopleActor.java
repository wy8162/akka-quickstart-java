package y.w.test;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import java.util.ArrayList;
import java.util.List;

public class PeopleActor extends AbstractLoggingActor {

    List<ActorRef> kids = new ArrayList<>();

    public static Props props() {
        return Props.create(PeopleActor.class, PeopleActor::new);
    }

    public PeopleActor() {

    }

    @Override
    public void preStart() throws Exception, Exception {
        super.preStart();
        log().debug("Inside preStart");
    }

    @Override
    public void postStop() throws Exception, Exception {
        super.postStop();
        log().debug("Inside postStop");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(HelloMessage.class, o -> log().info("Thanks for " + o.message))
                .match(
                        ActorRef.class,
                        actorRef -> {
                            target = actorRef;
                            getSender().tell("done", getSelf());
                        })
                .matchAny(this::unhandled)
                .build();
    }

    @Override
    public void unhandled(Object message) {
        super.unhandled(message);

        log().error("Not handled : " + message.toString());
    }

    interface Command {}

    static class HelloMessage implements Command {
        public final String message;

        public HelloMessage(String message) {
            this.message = message;
        }
    }

    static class TerminalMessage implements Command {}
}
