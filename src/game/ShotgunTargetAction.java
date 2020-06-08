package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Action that stores one of 8 possible cardinal directions the Shotgun can be fired in
 */
public class ShotgunTargetAction extends Action implements ShotgunAction {
	private String fireDirection;
	
	/**
	 * Constructor
	 * @param direction String representing one of 8 cardinal directions
	 * @throws Exception direction is not one of 8 cardinal directions
	 */
	public ShotgunTargetAction(String direction) throws Exception {
		if (direction == "North" 	  ||
			direction == "North-East" ||
			direction == "East" 	  ||
			direction == "South-East" ||
			direction == "South"	  ||
			direction == "South-West" ||
			direction == "West"		  ||
			direction == "North-West") {
			fireDirection = direction;
		} else {
			throw new Exception("Invalid firing direction provided");
		}
	}
	
	/**
	 * @return Direction to fire Shotgun in
	 */
	public String getTargetedDirection() {
		return fireDirection;
	}
	
	/**
	 * Only returns a string describing which direction the Shotgun is aimed at. No IO outside this class
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		return actor + " aims Shotgun towards " + fireDirection;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Aim and Fire Shotgun towards " + fireDirection;
	}

}
