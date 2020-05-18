package game;

import java.util.ArrayList;
import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

//Newly added imports
import edu.monash.fit2099.engine.DoNothingAction;

/**
 * Allows an Actor to wander around at random.
 * 
 * @author ram
 *
 */
public class WanderBehaviour implements Behaviour {
	
	private Random random = new Random();


	/**
	 * Returns a MoveAction to wander to a random location, if possible.  
	 * If no movement is possible, returns null.
	 * 
	 * @param actor the Actor enacting the behaviour
	 * @param map the map that actor is currently on
	 * @return an Action, or null if no MoveAction is possible
	 */
	@Override
	public Action getAction(Actor actor, GameMap map) {
		ArrayList<Action> actions = new ArrayList<Action>();
		
		for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
            	actions.add(exit.getDestination().getMoveAction(actor, "around", exit.getHotKey()));
            }
        }
		
		if (!actions.isEmpty()) {
			// the actor has a location to wander to, so
			// if actor is a zombie, check legs before wander
			if (actor.hasCapability(ZombieCapability.UNDEAD)) {
				// Initialize default case: zombie does nothing
				Action zombieAction = new DoNothingAction();
				// already checked that actor is a zombie, safe to downcast Actor to Zombie
				if (((Zombie) actor).legs() == 2) {
					// zombie has 2 legs, wander normally
					zombieAction = actions.get(random.nextInt(actions.size()));
				} else if (((Zombie) actor).legs() == 1) {
					// zombie has 1 leg, only wander if it skipped previous turn
					if (((Zombie) actor).hasSkipped()) {
						zombieAction = actions.get(random.nextInt(actions.size()));
					}
					((Zombie) actor).toggleSkip();
				}
				// appropriate action should have been set,
				// if zombie has 0 legs, zombieAction should be the default case
				return zombieAction;

			} else {
			// else actor is not a zombie, wander normally
				return actions.get(random.nextInt(actions.size()));
			}
		}
		else {
			return null;
		}

	}

}
