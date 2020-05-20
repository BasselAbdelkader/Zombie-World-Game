package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;

import java.util.List;

public class PickUpBehaviour implements Behaviour {

	@Override
	public Action getAction(Actor actor, GameMap map) {
        // downcast actor to Zombie to access arms
        /// can add test
        Zombie zActor = (Zombie) actor;
        // Only pickup weapon if zombie doesn't currently have one
        // and zombie is able to
        if (zActor.getWeapon() instanceof IntrinsicWeapon &&
            zActor.arms() >= 1) {
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
