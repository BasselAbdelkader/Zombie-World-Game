package game;

import edu.monash.fit2099.engine.Action;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
/**
 * Stores and allows access to the Actor targeted by the Sniper, does not alter game state outside this class.
 */
public class SniperTargetAction extends Action {
	private Actor target;
	
	public SniperTargetAction(Actor targetActor) {
		target = targetActor;
	}
	
	public Actor getTarget() {
		return target;
	}
	
	@Override
	public String execute(Actor actor, GameMap map) {
		return actor + " targets " + target;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Target '" + target.getDisplayChar() + "' " + target;
	}

}
