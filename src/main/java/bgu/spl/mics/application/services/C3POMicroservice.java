package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Ewoks ewoks;

	
    public C3POMicroservice() {
        super("C3PO");
        ewoks = Ewoks.getInstance();
    }
    //Microservice C3PO subscribe to AttackEvent and supplies specific callback
    //Microservice C3PO subscribe to Terminate Broadcast and supplies specific callback
    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (c) -> {
            // executes attack mission
            c.executeAttack();
            complete(c, true);
            Diary diary=Diary.getInstance();
            //sets finish time when finished
            diary.setC3POFinish(System.currentTimeMillis());
            //increases total attacks by one after the attack execution
            diary.incrementTotalAttacks();
        });
        //subscribes to termination broadcast
        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {
            terminate();
            //sets termination time
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
        });
        Main.countDownLatch.countDown();
    }
}
