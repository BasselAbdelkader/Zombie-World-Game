package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Menu;

/**
 * Handles setup and firing the Shotgun
 */
public class ShotgunBlastAction extends Action implements ShotgunAction {
	protected RangedWeapon parentItem;
	
	/**
	 * Constructor, stores reference to parent item for updating it's ammo
	 * @param item Parent item
	 */
	public ShotgunBlastAction(RangedWeapon item) {
		parentItem = item;
	}
	
	/**
	 * Get list of locations connected to current 90 degress in given direction.
	 * (eg. if direction = North, returns = North-West, North, NorthEast)
	 * @param current current location
	 * @param direction direction the shot is fired
	 * @return list of locations adjacent to current that are 90 degrees in the given direction
	 */
	private ArrayList<Location> coneInDirection(Location current, String direction) {
		List<Exit> all = current.getExits();
		int directionIndex = -1;
		for (Exit e : all) {
			if (e.getName() == direction) {
				directionIndex = all.indexOf(e);
			}
		}
		
		ArrayList<Location> locationsInCone = new ArrayList<Location>();
		int i = directionIndex-1;
		while (locationsInCone.size() < 3) {
			if (i < 0) {
				i += all.size();
			}
			if (i > all.size()-1) {
				i %= all.size();
			}
			locationsInCone.add(all.get(i).getDestination());
			i += 1;
		}
		
		return locationsInCone;
	}
	
	/**
	 * @param start location where player fires the shot
	 * @param direction direction player chose to fire in
	 * @return list of locations in range of the Shotgun blast.
	 */
	private HashSet<Location> locationsInRange(Location start, String direction) {
		final int MAX_RANGE = 3;
		HashSet<Location> visited = new HashSet<Location>();
		ArrayList<Location> queue = new ArrayList<Location>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Location current = queue.get(0);
			queue.remove(0);
			
			visited.add(current);
			ArrayList<Location> nextLocations = coneInDirection(current, direction);
			for (Location next : nextLocations) {
				if (!visited.contains(next) &&
					Math.abs(start.x() - next.x()) <= MAX_RANGE &&
					Math.abs(start.y() - next.y()) <= MAX_RANGE) {
					queue.add(next);
				}
			}
		}
		return visited;
	}
	
	/**
	 * Asks player which direction fire in, then fires in chosen direction and returns damage log of actors affected
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		// Only player can fire shotgun
		if (!(actor instanceof Player)) {
			return null;
		}
		
		Menu menu = ((Player) actor).menu();
		Display display = ((Player) actor).display();
		
		// Notify Player firing shotgun process has begin
		display.println(actor + " loads their shotgun");

		Actions options = new Actions();
		// Get possible directions to fire in
		// Requires exit.name() to be a cardinal direction, NOT the name of a place
		for (Exit exit : map.locationOf(actor).getExits()) {
			try {
				options.add(new ShotgunTargetAction(exit.getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Ask player which direction to fire in
		ShotgunTargetAction chosenDirection = (ShotgunTargetAction) menu.showMenu(actor, options, display);
		// Notify Player of direction chosen
		display.println(chosenDirection.execute(actor, map));
		
		String direction = chosenDirection.getTargetedDirection();
		// Get all locations in chosen direction
		HashSet<Location> locationsInRange = locationsInRange(map.locationOf(actor), direction);
		// Get actors(except Player) in those locations
		ArrayList<Actor> actorsInRange = new ArrayList<Actor>();
		for (Location here : locationsInRange) {
			if (here.containsAnActor() &&
				!(here.getActor() instanceof Player)) {
				actorsInRange.add(here.getActor());
			}
		}
		
		// Set this so AttackAction uses this weapon's damage and hit chances
		((Player) actor).setActiveWeapon(parentItem);
		
		String victimLog = "";
		String completeLog = "";
		boolean hitSomeone = false;
		for (Actor victim : actorsInRange) {
			// Damage the actor
			hitSomeone = true;
			victimLog = new AttackAction(victim).execute(actor, map);
			completeLog += victimLog + "\n";
		}
		if (hitSomeone) {
			completeLog += actor + " blasted at " + actorsInRange.size() + " target(s)";
		} else {
			completeLog += actor + " blasted at nobody";
		}
		
		// Decrement this weapon's ammo
		parentItem.decreaseAmmo(1);
		
		// Reset this to allow Player to use melee weapons in future turns
		((Player) actor).resetActiveWeapon();
		
		return completeLog;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Setup and Fire Shotgun";
	}

}
