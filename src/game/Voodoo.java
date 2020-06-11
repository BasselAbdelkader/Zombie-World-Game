package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;

/**
 * A Voodoo.
 *
 * The Voodoo
 *
 * @author ram
 *
 */
public class Voodoo extends ZombieActor { ////
    private int age = 0;
    private boolean killed;

    private Behaviour[] behaviours = {
            new WanderBehaviour()
    };

    public Voodoo(String name) {
        super(name, 'V', 5, ZombieCapability.UNDEAD);
    } ///


    /**
     *
     * @param actions list of possible Actions
     * @param lastAction previous Action, if it was a multiturn action
     * @param map the map where the current Zombie is
     * @param display the Display where the Zombie's utterances will be displayed
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

        age++;
        if (age % 10 == 0) { //
            System.out.println("5 Zombies created!!!");
            for (int i = 0; i < 5; i++) { //create 5 new Zombie
                int x;
                int y;
                int tries = 0;
                do {
                    tries ++;
                    x = (int) Math.floor(Math.random() * map.getXRange().max());
                    y = (int) Math.floor(Math.random() * map.getYRange().max());
                } while (map.at(x, y).containsAnActor() && tries < 10); //maybe there is no place for new actor?

                if (tries < 10) { //place found
                    map.at(x,  y).addActor(new Zombie("Zombie" + (i+1)));
                }
            }
            if (age == 30) {
                //vanish!
                map.removeActor(this);
                System.out.println("Voodoo temporary vanishes!");
            }
        } else {
            for (Behaviour behaviour : behaviours) {
                Action action = behaviour.getAction(this, map);
                if (action != null)
                    return action;
            }
        }
        return new DoNothingAction();
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public boolean isKilled() {
        return killed;
    }
}
