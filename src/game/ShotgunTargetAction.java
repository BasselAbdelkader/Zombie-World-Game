package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

public class ShotgunTargetAction extends Action implements ShotgunAction {
	private String fireDirection;
	
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
	public String getTargetedDirection() {
		return fireDirection;
	}
	@Override
	public String execute(Actor actor, GameMap map) {
		// TODO Auto-generated method stub
		return actor + " aims Shotgun towards " + fireDirection;
	}

	@Override
	public String menuDescription(Actor actor) {
		// TODO Auto-generated method stub
		return "Aim and Fire Shotgun towards " + fireDirection;
	}

}
