package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

public class SniperFireAction extends Action implements SniperAction {
	private SniperRifle parentItem;
	private int tickStarted;
	
	public SniperFireAction(SniperRifle item) {
		parentItem = item;
		tickStarted = item.currentTick();
	}

	public int tickStarted() {
		return tickStarted;
	}
	
	@Override
	public String execute(Actor actor, GameMap map) {
		// calculate hit chance
		// calculate damage
		// reset state
		
		// After firing, the sniping process is over, player must setup again on the next turn
		parentItem.clearActions();
		parentItem.resetProcess();
		// try {
		// 	parentItem.addSniperAction(new SniperSetupAction(parentItem));
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		
		int ammoLeft = parentItem.currentAmmo();
		
		System.out.println("damage " + parentItem.target() + " logic then reset");
		// TEMP RETURN MISSING DAMAGE TAKEN
		return actor + " sniped " + parentItem.target() + ", remaining sniper ammo: " + ammoLeft;
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Fire sniper at " + parentItem.target();
	}

}
