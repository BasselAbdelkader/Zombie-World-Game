package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;

public class Shotgun extends RangedWeapon {
	protected int ticksPassed = 0;
	
	protected final int MAX_AMMO = 40;
	
	public Shotgun() {
		super(WeaponStats.SHOTGUN.weaponName(),
				WeaponStats.SHOTGUN.weaponChar(),
				WeaponStats.SHOTGUN.weaponDamage(),
				WeaponStats.SHOTGUN.weaponVerb()
		);
	}
	
	@Override
	public void increaseAmmo(int amount) {
		ammo = Math.min(ammo + amount, MAX_AMMO);
	}
	
	public void tick(Location currentLocation, Actor actor) {
		if (actor instanceof Player) {
			ticksPassed += 1;
		}
		// Add ShotgunBlastAction after player picks it up
		if (ticksPassed == 1) {
			allowableActions.add(new ShotgunBlastAction());
		}
		// Remove Action to fire if out of ammo
		// Notify player by printing to their display
		
	}
}
