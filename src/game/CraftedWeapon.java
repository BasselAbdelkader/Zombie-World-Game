package game;

import edu.monash.fit2099.engine.WeaponItem;

/**
 * Craft a new WeaponItem with the given parameters.
 */
public class CraftedWeapon extends WeaponItem {
	/**
	 * Constructor
	 * @param name String as weapon name
	 * @param char char as display character
	 * @param int int as weapon damage
	 * @param String String as weapon verb
	 */
	public CraftedWeapon(String name, char displayChar, int damage, String verb) {
		super(name, displayChar, damage, verb);
	}
}
