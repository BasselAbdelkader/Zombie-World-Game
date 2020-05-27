package game;
import java.util.list;
public class Food extends PortableItem {
    // new portable item with the name food
    public Food (){
        super("Food",'$');
        super.allowableActions.add(new EatFoodAction());
    }
}
