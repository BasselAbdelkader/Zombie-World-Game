package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Weapon meant to be dropped by zombies, can be picked up by anyone, can be crafted into
 * a better version if in the Player's inventory for an amount of turns.
 */
public class ZombieLeg extends WeaponItem {
	protected int ticksPassed = 0;
	/**
	 * Number of turns the item must be in Player's inventory until it becomes craftable.
	 */
	private final int CRAFTABLE_TURNS = 4;

	public ZombieLeg() {
		super("Zombie Leg", 'l', 16, "whips");
	}
	
	/**
	 * Adds a CraftWeaponAction to the allowableActions attribute after being in Player's
	 * inventory for an amount of turns.
	 */
	public void tick(Location currentLocation, Actor actor) {
		// Player can only craft the leg 4 turns after picking it up.
		if (ticksPassed == CRAFTABLE_TURNS) {
			allowableActions.add(new CraftWeaponAction(
				this, WeaponStats.ZOMBIE_MACE));
		}
		// Only tick when in player inventory
		if (actor instanceof Player) {
			ticksPassed += 1;
		}
	}
	
}
