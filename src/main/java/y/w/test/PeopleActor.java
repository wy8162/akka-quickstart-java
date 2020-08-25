package y.w.test;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import y.w.c1.message.HelloMessage;
import y.w.c1.message.MyNameIs;
import y.w.c1.message.WhatsYourName;

public class PeopleActor extends AbstractLoggingActor {
    ActorRef target;

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
                .match(HelloMessage.class, m -> log().info("Thanks for " + m.getMessage()))
                .match(WhatsYourName.class, m -> sender().tell(getMyName() , self()))
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

    private MyNameIs getMyName() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new MyNameIs("My name is " + self().toString());
    }
}
