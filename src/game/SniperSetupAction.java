package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Menu;

/**
 * Initial Action present on Sniper Rifle, implements starting the sniping process
 */
public class SniperSetupAction extends Action implements SniperAction {
	private SniperRifle parentItem;
	/**
	 * Constructor, stores reference to parent item
	 */
	public SniperSetupAction(SniperRifle item) {
		parentItem = item;
	}
	
	// Returns Actions containing one SniperTargetAction for every Zombie on the given map
	private Actions acquireTargets(GameMap map) {
		Actions targets = new Actions();
 		for (int x : map.getXRange()) {
 			for (int y : map.getYRange()) {
 				Location here = map.at(x, y);
 				Actor actor = map.getActorAt(here);
 				// Add to target selection if there is a Zombie at this location
 				if (actor != null && actor.hasCapability(ZombieCapability.UNDEAD)) {
 					targets.add(new SniperTargetAction(map.getActorAt(here)));
 					// Return early if 26 targets have been acquired
 	 				// because Menu.showMenu() can only generate hotkeys for 26 options
 	 				if (targets.size() == 26) {
 	 					return targets;
 	 				}
 				}
 			}
 		}
 		return targets;
	}
	
	/**
	 * Asks player to choose target, then asks player whether to fire immediately or aim for one turn
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		// Only the Player can fire the Sniper Rifle
		if (!(actor instanceof Player)) {
			return null;
		}
		
		Player player = (Player) actor;
		Display display = player.display();
		Menu menu = player.menu();
		
		// Indicate that sniping process has begun
		display.println(actor + " loads their Sniper Rifle");
		
 		Actions targets = acquireTargets(map);
 		SniperTargetAction chosenTarget;
 		if (targets.size() > 1) {
 			// Ask player which Zombie to target
 			chosenTarget = (SniperTargetAction) menu.showMenu(player, targets, display); 			
 		} else {
 			// Return early/skip turn if no targets on the map
 			return actor + " looked through their scope and found no Zombies, sniping aborted";
 		}
 		parentItem.setTarget(chosenTarget.getTarget());
 		
 		// Notify player of which target was chosen
 		display.println(chosenTarget.execute(player, map));
 		
 		// Generate options: Fire immediately or aim for one turn
 		Actions snipingActions = new Actions();
 		// Fire immediately, 0 turns spent aiming
 		snipingActions.add(new SniperFireAction(parentItem));
 		// Spend current turn aiming
 		snipingActions.add(new SniperAimAction(parentItem));
 		// Ask player which option to take
		Action chosenAction = menu.showMenu(player, snipingActions, display);
		
		// If player chose to aim, store player's current hp for concentration check
		if (chosenAction instanceof SniperAimAction) {
			parentItem.setStartHp(player.hp());
		}
		
		// Clear existing actions in the item, execute the chosen action
		parentItem.clearActions();
		try {
			parentItem.addSniperAction(chosenAction);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Return result of executing chosen action
		return chosenAction.execute(actor, map);
	}

	@Override
	public String menuDescription(Actor actor) {
		return "Setup Sniper Rifle";
	}

	/**
	 * This method is not used in this class, only here for consistency
	 */
	@Override
	public int tickStarted() {
		return 0;
	}

}
