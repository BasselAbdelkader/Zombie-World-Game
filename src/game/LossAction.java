package game;

import edu.monash.fit2099.engine.*;

/**
 * An Action that kills the player
 *
 */
public class LossAction extends Action {   ///

    public LossAction() {
    }

    @Override
    public String execute(Actor actor, GameMap map) {

        map.removeActor(actor);
        return menuDescription(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " loses the game";
    }

    @Override
    public String hotkey() {
        return "w";
    } //is not used
}
