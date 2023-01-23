import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

//MAIN GAME STAGE
public class GameStage extends Pane{
	//GAME STAGE VARAIBLES
	private double stageWidth = 1200;
	private double stageHeight = 175;
	private Image stageImage = new Image("Arena_Platform_Main.png",stageWidth,stageHeight,true,true);
	private Image backgroundImage = new Image("Arena_BG.png",1920,1010,true,true);
	private ImageView background = new ImageView(backgroundImage);
	private Rectangle stage = new Rectangle(stageWidth,stageHeight);
	private double x;
	private double y;
	
	//GAME STAGE CONSTRUCTOR
	public GameStage(double x, double y)
	{
		this.getChildren().add(background);
		this.x = x;
		this.y = y;
		stage.setX(x);
		stage.setY(y);
		stage.setFill(new ImagePattern(stageImage));
		this.getChildren().add(stage);
		
	}
	
	//ACCURATE BOUNDS FOR GAME STAGE
	public Bounds getBounds() {
	Bounds bound =  new BoundingBox(x,  y, stageWidth, stageHeight);
	return bound; 
	}

}