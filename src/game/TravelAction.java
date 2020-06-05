package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

/**
 * Action which moves the actor to another map.
 */
public class TravelAction extends Action {
	private Location destinationLocation;
	private String destinationName;
	
	/**
	 * Constructor
	 * @param newLocation Location to move the actor to
	 * @param newMapName Name of destination map
	 */
	public TravelAction(Location newLocation, String newMapName) {
		destinationLocation = newLocation;
		destinationName = newMapName;
	}
	
	@Override
	public String execute(Actor actor, GameMap map) {
		map.moveActor(actor, destinationLocation);
		return actor + " traveled to" + destinationName;
	}
	
	@Override
	public String menuDescription(Actor actor) {
		return "travel to " + destinationName;
	}

}
