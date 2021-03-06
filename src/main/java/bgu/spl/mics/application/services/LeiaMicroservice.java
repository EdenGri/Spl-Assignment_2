package bgu.spl.mics.application.services;
import java.util.ArrayList;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    //Microservice Leia subscribe to Terminate Broadcast and supplies specific callback
    //for each attack Leia send AttackEvent
    //Leia wait until all the attackEvent will resolve
    //Leia send DeactivationEvent and wait until it will resolve
    //Leia send BombDestroyerEvent
    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {
            terminate();
            //sets termination time
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
        });

        ArrayList<Future<Boolean>> attacksStatuses = new ArrayList<>();

    	for (Attack attack:attacks) {
            AttackEvent newAttackEvent = new AttackEvent(attack);
            Future futureAttack = sendEvent(newAttackEvent);
            attacksStatuses.add(futureAttack);
        }
    	for (Future futureAttack:attacksStatuses){
    	    futureAttack.get();
        }

        Future futureDeactivation = sendEvent(new DeactivationEvent());
    	futureDeactivation.get();

        Future futureBombDestroyer = sendEvent(new BombDestroyerEvent());

    }
}






