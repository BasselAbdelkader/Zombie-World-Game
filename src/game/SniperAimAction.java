package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Aims the SniperRifle, then provides Player option to continue aiming or fire on the next turn
 *
 */
public class SniperAimAction extends Action implements SniperAction {
	private SniperRifle parentItem;
	private int tickStarted;
	
	/**
	 * Constructor, stores reference to item and current value of the tick the item is currently on
	 * @param item
	 */
	public SniperAimAction(SniperRifle item) {
		parentItem = item;
		tickStarted = item.currentTick();
	}
	
	/**
	 * @return tick number that this Action was created
	 */
	@Override
	public int tickStarted() {
		return tickStarted;
	}
	
	/**
	 * Aims the Sniper Rifle, then asks player whether to continue aiming or fire
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		parentItem.aim();
		// On the next turn, player can either fire or aim another round
		// unless player already spent 2 turns aiming, then firing is the only option(checked in addAimAction()))
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
