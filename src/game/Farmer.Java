package game;

public class Farmer extends Human{
    private boolean isStandingPatchOfDirt;
    private boolean isLeftAlone;
    private boolean isStandingUnripeCrop;
    private boolean isStandingRipeCrop;

    public boolean isStandingPatchOfDirt() {
        return isStandingPatchOfDirt;
    }

    public void setStandingPatchOfDirt(boolean standingPatchOfDirt) {
        isStandingPatchOfDirt = standingPatchOfDirt;
    }

    public boolean isLeftAlone() {
        return isLeftAlone;
    }

    public void setLeftAlone(boolean leftAlone) {
        isLeftAlone = leftAlone;
    }

    public boolean isStandingUnripeCrop() {
        return isStandingUnripeCrop;
    }

    public void setStandingUnripeCrop(boolean standingUnripeCrop) {
        isStandingUnripeCrop = standingUnripeCrop;
    }

    public boolean isStandingRipeCrop() {
        return isStandingRipeCrop;
    }

    public void setStandingRipeCrop(boolean standingRipeCrop) {
        isStandingRipeCrop = standingRipeCrop;
    }

    public void sowingACrop(){

    }
    public void ripenCrop(){

    }
    public void fertilize(){

    }
}