package game;

import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Cannot be used as melee weapon, uses ammo.
 */
public abstract class RangedWeapon extends WeaponItem {
	protected int ammo;
	protected final int MIN_AMMO = 0;
	
	/**
	 * Constructor
	 * @param name Weapon name
	 * @param displayChar Weapon display char
	 * @param damage Weapon damage
	 * @param verb Weapon verb
	 */
	public RangedWeapon(String name, char displayChar, int damage, String verb) {
		super(name, displayChar, damage, verb);
	}
	/**
	 * Prevents Ranged Weapon from being used as melee weapon by AttackAction
	 */
	@Override
	public Weapon asWeapon() {
		return null;
	}
	
	/**
	 * @return Current amount of ammo in the weapon
	 */
	public int currentAmmo() {
		return ammo;
	}
	
	/**
	 * Decreases ammo in the weapon, will not decrease ammo below MIN_AMMO
	 * @param amount to decrease
	 */
	public void decreaseAmmo(int amount) {
		ammo = Math.max(ammo - amount, MIN_AMMO);
	}
	
	public abstract void increaseAmmo(int amount);
}
