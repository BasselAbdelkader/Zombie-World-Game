package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Menu;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.*;

/**
 * Class representing the Player.
 */
public class Player extends Human {
	private Display gameDisplay;
	private boolean allowDisplayAccess = false;

	// If activeWeapon is not null, activeWeapon is used by Player to attack
	private Weapon activeWeapon;

	private Menu menu = new Menu();

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}

	/**
	 * @return Return Player's current Hp
	 */
	public int hp() {
		return hitPoints;
	}

	/**
	 * @return Display object
	 */
	public Display display() {
		if (allowDisplayAccess) {
			return gameDisplay;
		}
		return null;
	}

	/**
	 * @return Return menu object
	 */
	public Menu menu() {
		return menu;
	}

	/**
	 * Sets the Player's active weapon, if set will be prioritized by getWeapon()
	 * @param weapon
	 */
	public void setActiveWeapon(RangedWeapon weapon) {
		if (weapon != null) {
			activeWeapon = weapon;
		}
	}

	/**
	 * Resets Player's activeWeapon to null
	 */
	public void resetActiveWeapon() {
		activeWeapon = null;
	}

	/**
	 * Return the weapon that should be used to Attack. If activeWeapon has not been set, use original Actor getWeapon()
	 */
	@Override
	public Weapon getWeapon() {
		if (activeWeapon != null) {
			return activeWeapon;
		}
		return super.getWeapon();
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// Expose game display to be used by RangedWeapons to print information
		gameDisplay = display;
		allowDisplayAccess = true;
		  ///added part ---
        if (checkWin(map)) {
            map.removeActor(this);
            return new WinAction();
        }
        if (checkLoss(map)) {
            map.removeActor(this);
            return new LossAction();
        }
        createVoodoo(map);
        ///added part ---
          actions.add(new QuitAction());
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		Action chosenAction = menu.showMenu(this, actions, display);
		return menu.showMenu(this, actions, display);



	}
	private boolean checkWin(GameMap map) {
        int zombies = 0;
        for (int x: map.getXRange()) {
            for (int y : map.getYRange()) {
                Actor actor = map.at(x, y).getActor();
                if (actor != null) {
                    if (actor instanceof Zombie || actor instanceof Voodoo) {
                        zombies++;
                    }
                }
            }
        }
        return zombies == 0;
    }
    private boolean checkLoss(GameMap map) {
        int humans = 0;
        for (int x: map.getXRange()) {
            for (int y : map.getYRange()) {
                Actor actor = map.at(x, y).getActor();
                if (actor != null) {
                    if (actor instanceof Human) {
                        humans++;
                    }
                }
            }
        }
        return humans <= 1;  //must exist player and at least one human more
    }
     private Voodoo voodoo;

    private void createVoodoo(GameMap map) {
        if (voodoo != null && voodoo.isKilled()) return;

        for (int x: map.getXRange()) {
            for (int y : map.getYRange()) {
                Actor actor = map.at(x, y).getActor();
                if (actor instanceof Voodoo) {
                    return; //Voodoo exists on the map
                }
            }
        }

        ///voodoo not killed and does not exist on the map
        double prob = Math.random();
        if (prob < 0.05) { //5% probability
            //put Voodoo Somewhere at the edge
            for (int x: map.getXRange()) {
                if (!map.at(x, 0).containsAnActor()) {
                    System.out.println("Voodoo created!");
                    voodoo = new Voodoo("Mambo Marie");
                    map.at(x, 0).addActor(voodoo);
                    return;
                }
            }
            for (int y : map.getYRange()) {
                if (!map.at(0, y).containsAnActor()) {
                    System.out.println("Voodoo created!");
                    voodoo = new Voodoo("Mambo Marie");
                    map.at(0, y).addActor(voodoo);
                    return;
                }
            }
        }
    }


}
