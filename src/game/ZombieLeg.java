package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.WeaponItem;

public class ZombieLeg extends WeaponItem {
	protected int ticksPassed = 0;
	
	public ZombieLeg() {
		super("Zombie Leg", 'l', 16, "slaps");
	}
	
	public void tick(Location currentLocation, Actor actor) {
		// Player can only craft the leg 4 turns after picking it up.
		if (ticksPassed == 4) {
			allowableActions.add(new CraftWeaponAction(
				this, WeaponStats.ZOMBIE_MACE));
		}
		// Only tick when in player inventory
		if (actor instanceof Player) {
			ticksPassed += 1;
		}
	}
	
}
