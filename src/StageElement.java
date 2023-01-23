import javafx.scene.layout.Pane;
//ABSTRACT CLASS FOR POLYMORPHISM
abstract public class StageElement extends Pane{
	public abstract void testCollision(Character character, boolean down);
}