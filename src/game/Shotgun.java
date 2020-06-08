package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;

/**
 * Shotgun weapon
 */
public class Shotgun extends RangedWeapon {
	protected final int MAX_AMMO = 40;
	protected final double HIT_CHANCE = 0.75;
	
	/**
	 * Constructor, instantiate with fields defined in WeaponStats.SHOTGUN
	 */
	public Shotgun() {
		super(WeaponStats.SHOTGUN.weaponName(),
				WeaponStats.SHOTGUN.weaponChar(),
				WeaponStats.SHOTGUN.weaponDamage(),
				WeaponStats.SHOTGUN.weaponVerb()
		);
	}
	
	/**
	 * Increases ammo by amount given, up to MAX_AMMO
	 */
	@Override
	public void increaseAmmo(int amount) {
		ammo = Math.min(ammo + amount, MAX_AMMO);
	}
	
	/**
	 * @return Hit chance of shotgun blast
	 */
	public double hitChance() {
		return HIT_CHANCE;
	}
	
	/**
	 * Allows player to fire the weapon after picking it up.
	 * Prevents firing if out of ammo
	 * Notifies player of current ammo
	 */
	public void tick(Location currentLocation, Actor actor) {
		if (actor instanceof Player) {
			ticksPassed += 1;
			
			// Add ShotgunBlastAction after player picks it up
			if (ticksPassed == 1) {
				allowableActions.add(new ShotgunBlastAction(this));
			}
			
			// Check actor's inventory for ammo and load all suitable ammo
			loadAmmo(actor);
			// Prevent firing if out of ammo
			// Notify player by printing to their display
			if (ammo == 0) {
				clearActions();
			} else {
				// We have ammo, add Action to fire if not already present
				if (allowableActions.size() == 0) {
					allowableActions.add(new ShotgunBlastAction(this));					
				}
			}
			// Notify player of this weapon's ammo state
			((Player) actor).display().println(ammoMessage(ammo));
		}
		
	}
	
	/**
	 * Ensure Player cannot fire the weapon before picking it up, 
	 */
	public void tick(Location currentLocation) {
		// If item is on the ground, return it to same state as before it was picked up, but keep ammo
		allowableActions.clear();
		ticksPassed = 0;
	}
}
