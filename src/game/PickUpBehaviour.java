package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;
import java.util.List;

/**
 * Behaviour meant to be used by Zombie class
 * Enable Zombies to pickup a weapon at their current location, if it is able to
 */
public class PickUpBehaviour implements Behaviour {
    /**
     * Zombie must have at least this number of arms to pickup a weapon.
     */
    private final int MIN_ARMS = 1;

    /**
     * Returns a PickUpItemAction if the Zombie has no weapon and is able to pickup a weapon.
     */
	@Override
	public Action getAction(Actor actor, GameMap map) {
        // downcast actor to Zombie to access arms
        /// can add test
        Zombie zActor = (Zombie) actor;
        // Only pickup weapon if zombie doesn't currently have one
        // and zombie is able to
        if (zActor.getWeapon() instanceof IntrinsicWeapon &&
            zActor.arms() >= MIN_ARMS) {
            List<Item> itemAtCurrent = map.locationOf(zActor).getItems();
            for (Item item : itemAtCurrent) {
                if (item.asWeapon() != null) {
                    return item.getPickUpAction();
                }
            }
         }
		return null;
	}

}
