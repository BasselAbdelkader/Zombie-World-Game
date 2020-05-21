package game;

public enum WeaponStats {
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
	
	public String weaponName() {
		return weaponName;
	}
	public char weaponChar() {
		return weaponChar;
	}
	public int weaponDamage() {
		return weaponDamage;
	}
	public String weaponVerb() {
		return weaponVerb;
	}
}
