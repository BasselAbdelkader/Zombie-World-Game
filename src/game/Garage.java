package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;

/**
 * Ground that can only be entered by the Player
 * A Vehicle is added here so it can only be used by the Player and other actors cannot block it.
 */
public class Garage extends Ground {
	public Garage() {
		super('^');
	}
	
	@Override
	public boolean canActorEnter(Actor actor) {
		if (actor instanceof Player) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean blocksThrownObjects() {
		return true;
	}
}
