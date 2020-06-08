package game;

import edu.monash.fit2099.engine.Item;

/**
 * Ammo for Ranged Weapons, is a portable item, instantiated AmmoBoxes can only be used by a single type of RangedWeapon
 */
public class AmmoBox extends Item {
	protected int capacity;
	protected String ammoType;
	/**
	 * Constructor
	 * @param weapon RangedWeapon this ammo is fired by
	 * @param amount Amount of times RangedWeapon can be fired after picking this up
	 */
	public AmmoBox(RangedWeapon weapon, int amount) {
		super(weapon.toString() + " ammo", 'b', true);
		capacity = amount;
		ammoType = weapon.toString();
	}
	
	/** 
	 * @return String representing name of weapon this ammo is used by
	 */
	public String ammoType() {
		return ammoType;
	}
	/**
	 * @return Amount of ammo contained in the AmmoBox
	 */
	public int amount() {
		return capacity;
	}
}
