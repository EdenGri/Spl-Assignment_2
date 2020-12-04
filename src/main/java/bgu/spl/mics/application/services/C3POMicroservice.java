package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.AttackEventCallback;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

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
    private int numOfAttack;
    private Ewoks ewoks;
    private CountDownLatch latch;

	
    public C3POMicroservice(CountDownLatch latch) {
        super("C3PO");
        ewoks = Ewoks.getInstance();
        numOfAttack=0;
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        AttackEventCallback AttackEventCB=new AttackEventCallback();
        subscribeEvent(AttackEvent.class, AttackEventCB);
        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {         //did this is microservice idk if should do it here? todo check
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }
}
