package game;
/**
 * Class used to store and retrieve weapon data.
 */
public enum WeaponStats {
	// Construct a constant with the fields: name, char, damage, verb
	// which will be used to instantiate a CraftedWeapon
	// Easily add new weapons to the game by adding more constants.
	ZOMBIE_CLUB("Zombie Club", 'C', 20, "clubs"),
	ZOMBIE_MACE("Zombie Mace", 'M', 25, "smashes"),
	SNIPER_RIFLE("Sniper Rifle", '*', 40, "snipes"),
	SHOTGUN("Shotgun", '&', 34, "blasts");
	
	private final String weaponName;
	private final char weaponChar;
	private final int weaponDamage;
	private final String weaponVerb;
	
	private WeaponStats(String newName, char newChar, int newDamage, String newVerb) {
		weaponName = newName;
		weaponChar = newChar;
		weaponDamage = newDamage;
		weaponVerb = newVerb;
	}
	/**
	 * @return name of current weapon
	 */
	public String weaponName() {
		return weaponName;
	}
	/**
	 * @return char of current weapon
	 */
	public char weaponChar() {
		return weaponChar;
	}
	/**
	 * @return damage of current weapon
	 */
	public int weaponDamage() {
		return weaponDamage;
	}
	/**
	 * @return verb of current weapon
	 */
	public String weaponVerb() {
		return weaponVerb;
	}
}
