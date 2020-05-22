package game;
/**
 * Class used to store and retrieve weapon data.
 */
public enum WeaponStats {
	// Construct a constant with the fields: name, char, damage, verb
	// which will be used to instantiate a CraftedWeapon
	// Easily add new weapons to the game by adding more constants.
	ZOMBIE_CLUB("Zombie Club", 'C', 20, "clubs"),
	ZOMBIE_MACE("Zombie Mace", 'M', 25, "smashes");
	
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
	 * Get name of current weapon
	 */
	public String weaponName() {
		return weaponName;
	}
	/**
	 * Get char of current weapon
	 */
	public char weaponChar() {
		return weaponChar;
	}
	/**
	 * Get damage of current weapon
	 */
	public int weaponDamage() {
		return weaponDamage;
	}
	/**
	 * Get verb of current weapon
	 */
	public String weaponVerb() {
		return weaponVerb;
	}
}
