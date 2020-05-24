package game;

public class RisingFromTheDead {
    private boolean isKilled;

    public boolean isKilled() {
        return isKilled;
    }

    public void setKilled(boolean killed) {
        isKilled = killed;
    }

    public Zombie riseTheDead(boolean isKilled){
        return new Zombie("Zombie");
    }
}