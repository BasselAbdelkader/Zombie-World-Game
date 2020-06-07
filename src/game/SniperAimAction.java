package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

public class SniperAimAction extends Action implements SniperAction {
	private SniperRifle parentItem;
	private int tickStarted;

	public SniperAimAction(SniperRifle item) {
		parentItem = item;
		tickStarted = item.currentTick();
	}

	public int tickStarted() {
		return tickStarted;
	}
	
	@Override
	public String execute(Actor actor, GameMap map) {
		parentItem.aim();
		// On the next turn, player can either fire or aim another round
		// unless player already spent 2 turns aiming, then firing is the only option
		parentItem.clearActions();
		parentItem.addFireAction();
		parentItem.addAimAction();
		
		int aimingTurnsLeft = parentItem.MAX_AIMING_TURNS - parentItem.turnsSpentAiming();
		return actor + " aims at " + parentItem.target() + ", Aiming turns left: " + aimingTurnsLeft;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "aim at " + parentItem.target();
	}

}
