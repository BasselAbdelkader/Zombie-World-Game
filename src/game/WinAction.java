package game;

import edu.monash.fit2099.engine.*;

/**
 * An Action that kills the player
 *
 */
public class WinAction extends Action {   ///

    public WinAction() {
    }

    @Override
    public String execute(Actor actor, GameMap map) {

        map.removeActor(actor);
        return menuDescription(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " wins the game";
    }

    @Override
    public String hotkey() {
        return "w";
    } //is not used
}
