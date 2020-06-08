package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Fires the Sniper Rifle by reusing AttackAction
 */
public class SniperFireAction extends Action implements SniperAction {
	private SniperRifle parentItem;
	private int tickStarted;
	
	/**
	 * Constructor, stores reference to item and current value of the tick the item is currently on
	 * @param item
	 */
	public SniperFireAction(SniperRifle item) {
		parentItem = item;
		tickStarted = item.currentTick();
	}
	/**
	 * @return tick number that this Action was created
	 */
	public int tickStarted() {
		return tickStarted;
	}
	
	/**
	 * Fires the Sniper Rifle, returns damage log
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		// Set this so AttackAction uses this weapon's damage and hit chances
		((Player) actor).setActiveWeapon(parentItem);
		
		String damageLog = new AttackAction(parentItem.target()).execute(actor, map);
		
		// Reset this to allow Player to use melee weapons in future turns
		((Player) actor).resetActiveWeapon();
		
		// Decrement this weapon's ammo
		parentItem.decreaseAmmo(1);
		
		// After firing, the sniping process is over, player must setup again on the next turn
		parentItem.resetProcess();
		
		return damageLog;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Fire sniper at " + parentItem.target();
	}

}
