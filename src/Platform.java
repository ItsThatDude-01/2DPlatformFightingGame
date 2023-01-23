import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class Platform extends StageElement{
	//PLATFORM VARAIBLES
	private double platformWidth = 300;
	private double platformHeight = 30;
	private Image platformImage = new Image("Arena_Platform.png",platformWidth,platformHeight,true,true);
	private Rectangle platform = new Rectangle(platformWidth,platformHeight);
	private double y;
	
	//PLATFORM CONSTRUCTOR
	public Platform(double x, double y)
	{
		setX(x);
		setY(y);
		platform.setFill(new ImagePattern(platformImage));
		this.getChildren().add(platform);
	}
	
	//SET PLATFORM X AND Y
	public void setX(double x)
	{
		platform.setX(x);
	}
	
	public void setY(double y)
	{
		this.y=y;
		platform.setY(y);
	}

	//PLATFORM TEST COLLIISON METHOD
	public void testCollision(Character character, boolean down) {
		// TODO Auto-generated method stub
		if(platform.getBoundsInLocal().intersects(character.getBounds())&&!down)
		{
			//ONLY OCCOURS WHEN PLAYER IS MOVING DOWNWARDS AND IS ABOVE THE PLATOFORM
			if(platform.getBoundsInLocal().getMaxY() >= character.getBounds().getMaxY() && platform.getBoundsInLocal().getMinY() <= character.getBounds().getMaxY() && character.getVelocityY()>=0)
			{
				//PLAYERR IS HELD ABOVE THE PLATOFORM AND BOTH JUMPS ARE REFRESHED
				character.stopGravity();
				character.setY(y-character.getCharacterHeight());
				character.enableJump();
				character.enableDoubleJump();
			}
		}
		
	}

}