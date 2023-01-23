import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Blastzone extends StageElement{
	
	//CREATING BLAST ZONE
	public static double width = 2400;
	public static double height = 1800;
	private Rectangle blastzone = new Rectangle(width,height);
	private AudioClip blastzoneSound = new AudioClip(this.getClass().getResource("KO.wav").toExternalForm());
	private Image blueDeath = new Image("BlueCubeDeath.png",1920,1010,true,true);
	private ImageView death = new ImageView(blueDeath);
	private boolean gameJustStart = true;
	//private Image redDeath = new Image("RedCubeDeath.png",characterWidth,characterHeight,true,true);
	PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
	public Blastzone(double x, double y)
	{
		blastzone.setX(x);
		blastzone.setY(y-220);
		blastzone.setFill(Color.TRANSPARENT);
		this.getChildren().add(blastzone);
		this.getChildren().add(death);
	}
	
	@Override
	public void testCollision(Character character, boolean down) {
		// TODO Auto-generated method stub
		if(!blastzone.getBoundsInLocal().intersects(character.getBounds()))
		{
			blastzoneSound.setVolume(.5);
			blastzoneSound.play();
			character.respawn();
			if(character.getID()==1)
			{
				death.setImage(blueDeath);
				death.getParent().toFront();
				delay.setOnFinished(event -> hideFlash());
				delay.play();
			}
		}
		else if(gameJustStart)
		{
			hideFlash();
			gameJustStart=false;
		}
		
	}
	
	public void hideFlash()
	{
		death.getParent().toBack();
	}

}
