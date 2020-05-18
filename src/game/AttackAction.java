package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;

// Newly added imports
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.WeaponItem;
import edu.monash.fit2099.engine.Location;
import java.util.List;


/**
 * Special Action for attacking other Actors.
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		// **Original implementation in the else block of this first if statement**
		// if actor is a zombie, need to handle bite attack and
		// picking up weapon before attacking
		if (actor.hasCapability(ZombieCapability.UNDEAD)) {
			// Downcast actor to a zombie since we need to check it's arms
			Zombie zActor = (Zombie) actor;
			// Initialize default zombie weapon
			Weapon zWeapon = zActor.getIntrinsicWeapon();
			// Look for a weapon in zombie's inventory
			// only if zombie can wield it (has at least 1 arm)
			if (zActor.arms() >= 1) {
				zWeapon = zActor.getWeapon();
				if (zWeapon instanceof IntrinsicWeapon) {
				// Zombie does not have a weapon
				// Try to find a weapon at the zombie's location
					Location currentLocation = map.locationOf(zActor);
					List<Item> items = currentLocation.getItems();
					for (Item item : items) {
						if (item instanceof WeaponItem) {
							System.out.println("never");
							// found a usable weapon at zombie's location
							zActor.addItemToInventory(item);
							currentLocation.removeItem(item);
							zWeapon = item.asWeapon();
						}
					}
				}
			}
			// calculate hit chances for different weapons
			float chance = rand.nextFloat();
			// success flag true when hit is successful
			boolean success = false;
			// punches have 40% hit chance
			if (zWeapon.verb() == "punches") {
				if (chance > 0.6) {
					success = true;
				}
			} else if (zWeapon.verb() == "bites") {
			// bites have 80% hit chance
				if (chance > 0.2) {
					success = true;
				}
			} else {
				// weapons have 90% hit chance with 2 arms
				if (zActor.arms() == 2) {
					if (chance > 0.1) {
						success = true;
					}
				} else {
				// weapons have 45% hit chance with 2 arms
					if (chance > 0.45) {
						success = true;
					}
				}
			}

			// REPEATED CODE to be cleaned later
			if (success) {
				int damage = zWeapon.damage();
				String result = actor + " " + zWeapon.verb() + " " + target + " for " + damage + " damage.";
				target.hurt(damage);
				if (!target.isConscious()) {
					Item corpse = new PortableItem("dead " + target, '%');
					map.locationOf(target).addItem(corpse);
					
					Actions dropActions = new Actions();
					for (Item item : target.getInventory())
						dropActions.add(item.getDropAction());
					for (Action drop : dropActions)		
						drop.execute(target, map);
					map.removeActor(target);	
					
					result += System.lineSeparator() + target + " is killed.";
					return result;
				}
				return result;

			} else {
				return actor + " misses " + target + ".";
			}
			
		} else {
			// Everything in this else block is the original implementation
			Weapon weapon = actor.getWeapon();

			if (rand.nextBoolean()) {
				return actor + " misses " + target + ".";
			}

			int damage = weapon.damage();
			String result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";

			target.hurt(damage);
			if (!target.isConscious()) {
				Item corpse = new PortableItem("dead " + target, '%');
				map.locationOf(target).addItem(corpse);
				
				Actions dropActions = new Actions();
				for (Item item : target.getInventory())
					dropActions.add(item.getDropAction());
				for (Action drop : dropActions)		
					drop.execute(target, map);
				map.removeActor(target);	
				
				result += System.lineSeparator() + target + " is killed.";
				return result;
			}
		}
		// TEMPORARY CODE
		 System.out.println(actor);
		 return "should not be here";
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks " + target;
	}
}
