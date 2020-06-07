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

public class ShotgunBlastAction extends Action {
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

		// Ask player which direction to fire in
		Actions options = new Actions();
		// possible directions
		// Requires exit.name() to be a cardinal direction, NOT the name of a place
		for (Exit exit : map.locationOf(actor).getExits()) {
			try {
				options.add(new ShotgunTargetAction(exit.getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		// calc hit chance
		// damage them
		// Return result of firing shotgun in chosen direction (everyone who was damaged)
		// TEMP RETURN
		return actor + " blasts their Shotgun " + direction;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Setup Shotgun";
	}

}
