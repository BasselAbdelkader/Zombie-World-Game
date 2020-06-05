package game;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

/**
 * Non-portable Item which gives the actor an option to move to another map
 */
public class Vehicle extends Item {
	/**
	 * Constructor
	 * @param targetLocation Location on destination map
	 * @param name Name of destination map
	 */
	public Vehicle(Location targetLocation, String name) {
		super("Car", '=', false);
		allowableActions.add(new TravelAction(targetLocation, name));
	}
}
