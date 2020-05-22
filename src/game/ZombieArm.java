package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Weapon meant to be dropped by zombies, can be picked up by anyone, can be crafted into
 * a better version if in the Player's inventory for an amount of turns.
 */
public class ZombieArm extends WeaponItem {
	protected int ticksPassed = 0;
	/**
	 * Number of turns the item must be in Player's inventory until it becomes craftable.
	 */
	private final int CRAFTABLE_TURNS = 3;
	
	public ZombieArm() {
		super("Zombie Arm", 'a', 15, "slaps");
	}
	
	/**
	 * Adds a CraftWeaponAction to the allowableActions attribute after being in Player's
	 * inventory for an amount of turns.
	 */
	public void tick(Location currentLocation, Actor actor) {
		// Player can only craft the arm 3 turns after picking it up.
		if (ticksPassed == CRAFTABLE_TURNS) {
			allowableActions.add(new CraftWeaponAction(
				this, WeaponStats.ZOMBIE_CLUB));
		}
		// Only tick when in Player inventory
		if (actor instanceof Player) {
			ticksPassed += 1;
		}
	}
}
