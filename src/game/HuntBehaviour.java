package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

// Newly added imports
import edu.monash.fit2099.engine.DoNothingAction;

/**
 * Returns a MoveAction that will take the Actor closer to the nearest instance of a target class.
 * 
 * This uses a breadth-first search algorithm and is based on code written for the
 * FIT2099 assignment in S2 2019 by Spike.
 * 
 * @author ram
 *
 */
public class HuntBehaviour implements Behaviour {

	private Class<?> targetClass;
	private String targetName; 
	private int maxRange;
	private HashSet<Location> visitedLocations = new HashSet<Location>();
	
	public HuntBehaviour(Class<?> cls, int range) {
		this.targetClass = cls;
		this.targetName = targetClass.getSimpleName();
		this.maxRange = range;
	}
	
	private Action hunt(Actor actor, Location here) {
		visitedLocations.clear();
		ArrayList<Location> now = new ArrayList<Location>();
		
		now.add(here);
		
		ArrayList<ArrayList<Location>> layer = new ArrayList<ArrayList<Location>>();
		layer.add(now);

		for (int i = 0; i<maxRange; i++) {
			layer = getNextLayer(actor, layer);
			Location there = search(layer);
			if (there != null)
				return there.getMoveAction(actor, "towards a " + targetName, null);
		}

		return null;
	}

	private ArrayList<ArrayList<Location>> getNextLayer(Actor actor, ArrayList<ArrayList<Location>> layer) {
		ArrayList<ArrayList<Location>> nextLayer = new ArrayList<ArrayList<Location>>();

		for (ArrayList<Location> path : layer) {
			List<Exit> exits = new ArrayList<Exit>(path.get(path.size() - 1).getExits());
			Collections.shuffle(exits);
			for (Exit exit : path.get(path.size() - 1).getExits()) {
				Location destination = exit.getDestination();
				if (!destination.getGround().canActorEnter(actor) || visitedLocations.contains(destination))
					continue;
				visitedLocations.add(destination);
				ArrayList<Location> newPath = new ArrayList<Location>(path);
				newPath.add(destination);
				nextLayer.add(newPath);
			}
		}
		return nextLayer;
	}
	
	private Location search(ArrayList<ArrayList<Location>> layer) {

		for (ArrayList<Location> path : layer) {
			if (containsTarget(path.get(path.size() - 1))) {
				return path.get(1);
			}
		}
		return null;
	}
	
	private boolean containsTarget(Location here) {
		return (here.getActor() != null &&
				targetClass.isInstance(here.getActor()));
	}



	@Override
	public Action getAction(Actor actor, GameMap map) {
		// If actor is a zombie, check it's legs before moving
		if (actor.hasCapability(ZombieCapability.UNDEAD)) {
			// Check if there are any targets to hunt,
			// return early if there aren't
			Action hunt = hunt(actor, map.locationOf(actor));
			if (hunt == null) {
				return null;
			}

			// initialize zombieAction
			// default case: zombie has 0 legs, do nothing
			Action zombieAction = new DoNothingAction();
			// already checked that actor is a zombie, safe to downcast Actor to Zombie
			if (((Zombie) actor).legs() == 2) {
				// zombie has 2 legs, hunt normally
				zombieAction = hunt;

			} else if (((Zombie) actor).legs() == 1) {
				// zombie has 1 leg, only hunt if it skipped previous turn
				if (((Zombie) actor).hasSkipped()) {
					zombieAction = hunt;
				}
				((Zombie) actor).toggleSkip();
			}
			// appropriate action should have been set,
			// if zombie has 0 legs, zombieAction should be the default case
			return zombieAction;

		} else {
		// else actor is not a zombie, hunt normally
			return hunt(actor, map.locationOf(actor));
		}
	}

}
