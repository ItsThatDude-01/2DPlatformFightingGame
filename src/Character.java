import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import javafx.animation.PauseTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import java.util.Random;


public class Character extends Pane{
	//CHARACTER ELEMENTS
	private int characterID;
	private double characterWidth = 85;
	private double characterHeight = 85;
	private Rectangle character = new Rectangle(characterWidth,characterHeight);
	
	//IMAGES INDICATING CHARACTER DIRECITON
	private Image leftImageDefault;
	private Image rightImageDefault;
	private Image leftImageDamaged;
	private Image rightImageDamaged;
	private Image leftImage;
	private Image rightImage;
	private Image respawnImage;
	
	//CHARACTER X & Y
	private double x;
	private double y;
	private double velocityX;
	private double velocityY;
	private double spawnX;
	
	//BOOLEANS
	private boolean hitstun;
	private boolean enableDown = true;
	private boolean enableLeft = true;
	private boolean enableRight = true;
	private boolean jump = false;
	private boolean doubleJump = true;
	private boolean specialJump = true;
	private boolean gravity;
	private boolean lastInputLeft;
	private boolean isRespawning;
	private boolean canDamage = true;
	
	//CHARACTER VALUES
	private final double FALL_ACCELERATION = 2.1;
	private final double MAX_FALL_SPEED = 12.5;
	private final double JUMP_SPEED = -40;
	private final double ATTACK_MOTION_Y = 30;
	private final double ATTACK_MOTION_X = 18;
	private final double DEFAULT_SPEED_X = 11;
	private final double COLLISION_SPEED_X = DEFAULT_SPEED_X/2;
	private double speedX = DEFAULT_SPEED_X;
	
	//CHARACTER ATTACK AND DAMAGE VARIABLE
	private final double LAUNCH_ANGLE = Math.PI/5;
	private final double DAMAGE = 11.5;
	private final double HITSTUN_STOP = 5;
	private final double launchSpeedMin = 25;
	
	//CHARACTER UI
	private double characterPercent;
	private Text displayPercent;
	private int stocks = 4;
	private Text displayStocks;
	
	//CHARACTER ATTACK ELEMENTS
	private double attackWidthX = 15;
	private double attackHeightX = 100;
	private Rectangle attackX = new Rectangle(attackWidthX,attackHeightX);
	
	//CHARACTER SOUNDS
	private AudioClip jumpSound = new AudioClip(this.getClass().getResource("Jump.wav").toExternalForm());
	private AudioClip damageSound = new AudioClip(this.getClass().getResource("Damage.wav").toExternalForm());
	private AudioClip attackSound = new AudioClip(this.getClass().getResource("Dash.wav").toExternalForm());
	
	private double attackWidthY = 100;
	private double attackHeightY = 15;
	private Rectangle attackY = new Rectangle(attackWidthY,attackHeightY);
	
	PauseTransition delay = new PauseTransition(Duration.seconds(2));
	PauseTransition damageDelay = new PauseTransition(Duration.seconds(.5));
	
	//CHARACTER CONSTRUCTOR
	public Character(int playerID)
	{
		characterID = playerID;
		//VERSION 1 - BLUE
		if(playerID == 1)
		{
			attackX.setFill(Color.DARKBLUE);
			attackY.setFill(Color.DARKBLUE);
			leftImageDefault = new Image("BlueCubeLeft.png",characterWidth,characterHeight,true,true);
			rightImageDefault = new Image("BlueCubeRight.png",characterWidth,characterHeight,true,true);
			leftImageDamaged = new Image("BlueCubeHitLeft.png",characterWidth,characterHeight,true,true);
			rightImageDamaged = new Image("BlueCubeHitRight.png",characterWidth,characterHeight,true,true);
			respawnImage = new Image("BlueCubeGlow.png", characterWidth, characterHeight, true, true);
			displayPercent = new Text(400,850,"PLAYER "+characterID+": "+characterPercent+"%");
			displayPercent.setFill(Color.MIDNIGHTBLUE);
			displayStocks = new Text(400,875,"STOCKS: "+stocks);
			displayStocks.setFill(Color.MIDNIGHTBLUE);
			spawnX = (1920-getCharacterWidth())/2-550;
		}
		//VERSION 2 - RED
		else if(playerID == 2)
		{
			attackX.setFill(Color.DARKRED);
			attackY.setFill(Color.DARKRED);
			leftImageDefault = new Image("RedCubeLeft.png",characterWidth,characterHeight,true,true);
			rightImageDefault = new Image("RedCubeRight.png",characterWidth,characterHeight,true,true);
			leftImageDamaged = new Image("RedCubeHitLeft.png",characterWidth,characterHeight,true,true);
			rightImageDamaged = new Image("RedCubeHitRight.png",characterWidth,characterHeight,true,true);
			respawnImage = new Image("RedCubeGlow.png", characterWidth, characterHeight, true, true);
			lastInputLeft = true;
			displayPercent = new Text(1300,850,"PLAYER "+characterID+": "+characterPercent+"%");
			displayPercent.setFill(Color.DARKRED);
			displayStocks = new Text(1300,875,"STOCKS: "+stocks);
			displayStocks.setFill(Color.DARKRED);
			spawnX = (1920-getCharacterWidth())/2+550;
		}
		displayPercent.setFont(Font.font("impact",40));
		displayStocks.setFont(Font.font("impact",30));
		//MAKES THESE ELEMENTS NODES
		this.getChildren().add(attackX);
		this.getChildren().add(attackY);
		this.getChildren().add(character);
		this.getChildren().add(displayPercent);
		this.getChildren().add(displayStocks);
		hide(attackX);
		hide(attackY);
	}
	
	//GET ACCURATE BOUNDS
	public Bounds getBounds() {
	Bounds bound =  new BoundingBox(x,  y, characterWidth, characterHeight);
	return bound; 
	}
	
	//GETTER AND SETTER FOR X
	public void setX(double x)
	{
		this.x=x;
		character.setX(x);
	}
	
	public double getX() {
		return x;
	}
	
	//GETTER AND SETTER FOR Y
	public void setY(double y)
	{
		this.y=y;
		character.setY(y);
	}
	
	public double getY() {
		return y;
	}
	
	//SET VELOCITY Y
	public void stopGravity()
	{
		velocityY = 0;
		gravity = false;
	}
	
	//GET CHARACTER WIDTH AND HEIGHT
	public double getCharacterWidth()
	{
		return characterWidth;
	}
	
	public double getCharacterHeight()
	{
		return characterHeight;
	}
	
	//GET CHARACTER Y VELOCITY
	public double getVelocityY()
	{
		return velocityY;
	}
	
	//ENABLES JUMP
	public void enableJump()
	{
		jump = true;
	}
	
	//ENABES DOUBLE JUMP
	public void enableDoubleJump()
	{
		doubleJump = true;
	}
	
	public void enableSpecialJump()
	{
		specialJump = true;
	}
	
	//REFRESHES ALL JUMPS
	public void refreshJumps()
	{
		enableJump();
		enableDoubleJump();
		enableSpecialJump();
	}
	
	public int getID()
	{
		return characterID;
	}
	
	//ATTACKING METHODS
	private void addPercent(double percent)
	{
		//PREVENT MULTIPLE HITS
		if(canDamage)
		{
			damageSound.setVolume(0.5);
			damageSound.play();
			characterPercent += percent;
			canDamage = false;
		}
		damageDelay.setOnFinished(event ->canDamage = true);
		damageDelay.play();
		
	}
	
	private boolean isRespawning()
	{
		return isRespawning;
	}
	
	private void playAttackSound()
	{
		if(!attackSound.isPlaying())
		{
			attackSound.play();
		}
	}
	//UPDATES CHARACTER MOVEMENT
	public void update(boolean up, boolean down, boolean left, boolean right, Bounds stage, Character anotherCharacter)
	{
		if(!isRespawning)
		{
			if(characterPercent <= 50.0)
			{
				leftImage = leftImageDefault;
				rightImage = rightImageDefault;
			}
			else
			{
				leftImage = leftImageDamaged;
				rightImage = rightImageDamaged;
			}
			if(hitstun)
			{
				if(lastInputLeft)
				{
					character.setFill(Color.DARKGREY);
				}
				else
				{
					character.setFill(Color.DARKGREY);
				}
				if(velocityX>=0)
				{
					velocityX-=3;
					if(velocityX<=HITSTUN_STOP)
					{
						hitstun = false;
					}
				}
				else {
					velocityX+=3;
					if(velocityX>=-HITSTUN_STOP)
					{
						hitstun = false;
					}
				}
				
				if(velocityY<=0)
				{
					velocityY+=3;
					if(velocityY>=HITSTUN_STOP)
					{
						hitstun = false;
					}
				}
				else
				{
					if(velocityY<=HITSTUN_STOP)
					{
						hitstun = false;
					}
					if(getBounds().intersects(stage))
					{
						stopGravity();
					}
					velocityY-=3;
	
				}
				setX(x+velocityX);
				setY(y+velocityY);
				}
			else
			{
				//CHARACTER MOVEMENT
				//CHARACTER ORIENTATION (LEFT & RIGHT)
				if(left)
				{
					lastInputLeft=true;
					character.setFill(new ImagePattern(leftImage));
				}
				else if(right)
				{
					lastInputLeft=false;
					character.setFill(new ImagePattern(rightImage));
				}
				else if(lastInputLeft)
				{
					character.setFill(new ImagePattern(leftImage));
				}
				else
				{
					character.setFill(new ImagePattern(rightImage));
				}
				
				//---UPDATES CHARACTER X POSITION---
				if(enableLeft && left)
				{
					velocityX=-speedX;
				}
				else if(enableRight && right)
				{
					velocityX=speedX;
				}
				
			}
			//COLLISIONS WITH OTHER CHARACTER
			if(getBounds().intersects(anotherCharacter.getBounds()) && !anotherCharacter.isRespawning())
			{
				//DETECTS IF CHARACTER IS ABOVE ANOTHER CHARACTER, ENSURES THAT CHARACTER DOES NOT SINK INTO OTHER CHARACTER
				if(y < anotherCharacter.getY() && getBounds().getMaxY()>=anotherCharacter.getBounds().getMinY())
				{
					enableDown = false;
					speedX = DEFAULT_SPEED_X;
					velocityY = 0;
					setY(anotherCharacter.getBounds().getMinY()-characterHeight);
					refreshJumps();
				}
				//ENABLES PLAYER TO MOVE LEFT AND RIGHT WHILE UNDERNEATH ANOTHER CHARACTER
				else if(y > anotherCharacter.getY() && getBounds().getMinY()<=anotherCharacter.getBounds().getMaxY())
				{
					enableLeft = true;
					enableRight = true;
					speedX = DEFAULT_SPEED_X;
				}
				//DETECTS IF PLAYER IS COLLIDING ON THE RIGHT IF SO, DISABLE LEFT
				else if(x > anotherCharacter.getX() && getBounds().getMinX() <= anotherCharacter.getBounds().getMaxX())
				{
					speedX = COLLISION_SPEED_X;
					setX(anotherCharacter.getBounds().getMaxX());
					enableLeft = false;
				}
				//DETECTS IF PLAYER IS COLLIDING ON THE LEFT IF SO, DISABLE RIGHT
				else if(x < anotherCharacter.getX() && getBounds().getMaxX() >= anotherCharacter.getBounds().getMinX())
				{
					speedX = COLLISION_SPEED_X;
					setX(anotherCharacter.getBounds().getMinX()-characterWidth);
					enableRight = false;
				}
			}
			else
			{
				speedX = DEFAULT_SPEED_X;
			}
			
			
			
			//---UPDATES CHARACTER Y POSITION---
			
			//BASIC JUMP COMMAND
			if(up && (jump || doubleJump) && !hitstun)
			{
				jumpSound.play();
				//CONSUMES DOUBLE JUMP IF JUMP IS ALREADY USED
				if(!jump)
				{
					doubleJump = false;
				}
				//JUMPING SETS JUMP TO FALSE AND SET VELOCITY Y TO JUMP SPEED
				jump = false;
				velocityY=JUMP_SPEED;
			}
			
			
			//IF CHARACTER INTERSECTS THE STAGE
			if(getBounds().intersects(stage) && !up)
			{
				//IF ABOVE, ENSURES USER DOES NOT CLIP INTO THE FLOOR AND RESTORES BOTH JUMPS
				if(getBounds().getMaxY()-characterWidth/2< stage.getMinY() && getBounds().getMaxY()>=stage.getMinY())
				{
					velocityY=0;
					setY(stage.getMinY()-characterHeight);
					refreshJumps();
				}
				//IF TO THE LEFT, ENSURES THAT CHARACTER CANNOT MOVE RIGHT
				else if(getBounds().getMinX() < stage.getMinX() && getBounds().getMaxX()>=stage.getMinX())
				{
					if(getBounds().getMaxY()<stage.getMaxY()-stage.getHeight()/2)
					{
						velocityY=0;
						enableDoubleJump();
					}
					else {
						velocityY += FALL_ACCELERATION;
					}
					enableRight = false;
					setX(stage.getMinX()-characterWidth);
				}
				//IF TO THE RIGHT, ENSURES THAT CHARACTER CANNOT MOVE LEFT
				else if(getBounds().getMaxX() > stage.getMaxX() && getBounds().getMinX()<=stage.getMaxX())
				{
					if(getBounds().getMaxY()<stage.getMaxY()-stage.getHeight()/2)
					{
						velocityY=0;
						enableDoubleJump();
					}
					else {
						velocityY += FALL_ACCELERATION;
					}
					enableLeft = false;
					setX(stage.getMaxX());
				}
				//IF BELOW, ENSURES CHARACTER DOES NOT CLIP INTO THE STAGE
				else if(getBounds().getMaxY() > stage.getMaxY() && getBounds().getMinY()<=stage.getMaxY())
				{
					setY(stage.getMaxY());
					velocityY += FALL_ACCELERATION;
				}
			}
			//IF CHARACTER PRESSES DOWN, THE CHARACTER WILL "FASTFALL"
			else if(enableDown && down && gravity)
			{
				velocityY+=FALL_ACCELERATION*1.5;
				if(velocityY>MAX_FALL_SPEED*2)
				{
					velocityY=MAX_FALL_SPEED*2;
				}
			}
			//REGULAR GRAVITY
			else if(gravity)
			{
				velocityY+=FALL_ACCELERATION;
				if(velocityY>MAX_FALL_SPEED)
				{
					velocityY=MAX_FALL_SPEED;
				}
			}
			setX(x+velocityX);
			setY(y+velocityY);
			
			//ENABLES ALL KEY COMMANDS AND UPDATES CHARACTER X AND Y
			if(!hitstun)
			{
				enableRight = true;
				enableLeft = true;
				enableDown = true;
				velocityX = 0;
			}
			gravity = true;
		}
		displayPercent.setText("PLAYER "+characterID+": "+characterPercent+"%");
		displayStocks.setText("STOCKS: "+stocks);

	}
	
	//UPDATES CHARACTER ATTACKS
	public void updateAttack(boolean up, boolean down, boolean left, boolean right, boolean attack, Character anotherCharacter)
	{	
		if(!isRespawning && !hitstun)
		{
			//LEFT ATTACK
			if(left && attack)
			{
				playAttackSound();
				velocityX=-ATTACK_MOTION_X;
				enableLeft = false;
				enableRight = false;
				attackX.getParent().toFront();
				attackX.setX(x-attackWidthX);
				centerY(attackX,attackHeightX); 
				hide(attackY);
			}
			//RIGHT ATTACK
			else if(right && attack)
			{
				playAttackSound();
				velocityX=ATTACK_MOTION_X;
				enableLeft = false;
				enableRight = false;
				attackX.getParent().toFront();
				attackX.setX(x+characterWidth);
				centerY(attackX,attackHeightX);
				hide(attackY);
			}
			//UP ATTACK
			else if(up && attack)
			{
				playAttackSound();
				if(specialJump)
				{
					stopGravity();
					velocityY=-ATTACK_MOTION_Y;
					specialJump = false;
				}
				attackY.getParent().toFront();
				centerX(attackY,attackWidthY);
				attackY.setY(y-attackHeightY);
				hide(attackX);
			}
			//DOWN ATTACK
			else if(down && attack)
			{
				playAttackSound();
				attackY.getParent().toFront();
				centerX(attackY,attackWidthY);
				attackY.setY(y+characterHeight);
				hide(attackX);
			}
			else if(attack)
			{
				playAttackSound();
				if(lastInputLeft) {
					velocityX=-ATTACK_MOTION_X;
					enableLeft = false;
					enableRight = false;
					attackX.getParent().toFront();
					attackX.setX(x-attackWidthX);
					centerY(attackX,attackHeightX);
					hide(attackY);
				}
				else
				{
					velocityX=ATTACK_MOTION_X;
					enableLeft = false;
					enableRight = false;
					attackX.getParent().toFront();
					attackX.setX(x+characterWidth);
					centerY(attackX,attackHeightX);
					hide(attackY);
				}
			}
			//HIDE ATTACK
			else
			{
				hide(attackX);
				hide(attackY);
			}
			
			if(!anotherCharacter.isRespawning())
			{
				if(anotherCharacter.getBounds().intersects(attackX.getBoundsInLocal())&&!hitstun)
				{
					anotherCharacter.addPercent(DAMAGE);
					if(x < anotherCharacter.getX() && attackX.getBoundsInLocal().getMaxX()>=anotherCharacter.getBounds().getMinX())
					{
						//anotherCharacter.setX(attackX.getBoundsInLocal().getMaxX());
						anotherCharacter.launchRight();
					}
					else if(x > anotherCharacter.getX() && attackX.getBoundsInLocal().getMinX()<=anotherCharacter.getBounds().getMaxX())
					{
						//anotherCharacter.setX(attackX.getBoundsInLocal().getMinX()-anotherCharacter.getCharacterWidth());
						anotherCharacter.launchLeft();
					}
				}
				else if(anotherCharacter.getBounds().intersects(attackY.getBoundsInLocal())&&!hitstun)
				{
					anotherCharacter.addPercent(DAMAGE);
					if(y > anotherCharacter.getY() && attackY.getBoundsInLocal().getMinY()<=anotherCharacter.getBounds().getMaxY())
					{
						anotherCharacter.setY(attackY.getBoundsInLocal().getMinY()-anotherCharacter.getCharacterHeight());
						anotherCharacter.launchUp();
					}
					else if(y < anotherCharacter.getY() && attackY.getBoundsInLocal().getMaxY()>=anotherCharacter.getBounds().getMinY())
					{
						anotherCharacter.stopGravity();
						anotherCharacter.launchDown();
						stopGravity();
						setY(y-attackHeightY);
						velocityY=JUMP_SPEED;
					}
				}
			}
		}
		
	}
	
	//HIDE THE ATTACK RECTTANGLES
	public void hide(Rectangle attack)
	{
		attack.setX(1500);
		attack.setY(1500);
	}
	
	//CENTER THE RECTANGLE X
	public void centerX(Rectangle attack, double attackWidth)
	{
		attack.setX(x+(characterWidth-attackWidth)/2);
	}
	
	//CENTTER THE RECTANGLE Y
	public void centerY(Rectangle attack, double attackHeight)
	{
		attack.setY(y+(characterHeight-attackHeight)/2);
		
	}
	
	//LAUCNHES CHARACTER RIGHT
	public void launchRight()
	{
		double launchSpeed = characterPercent*5/6;
		if(launchSpeed < launchSpeedMin)
		{
			launchSpeed = launchSpeedMin;
		}
		velocityX=Math.cos(LAUNCH_ANGLE)*launchSpeed;
		velocityY=-Math.sin(LAUNCH_ANGLE)*launchSpeed;
		hitstun=true;
	}
	
	//LAUCNEHS CHARACTTER LEFT
	public void launchLeft()
	{
		double launchSpeed = characterPercent*5/6;
		if(launchSpeed < launchSpeedMin)
		{
			launchSpeed = launchSpeedMin;
		}
		velocityX=-Math.cos(LAUNCH_ANGLE)*launchSpeed;
		velocityY=-Math.sin(LAUNCH_ANGLE)*launchSpeed;
		hitstun=true;
	}
	
	//LAUNCHES CHARACTER UP
	public void launchUp()
	{
		double launchSpeed = characterPercent*1/2;
		if(launchSpeed < launchSpeedMin)
		{
			launchSpeed = launchSpeedMin;
		}
		velocityY=-launchSpeed;
		hitstun=true;
	}
	
	//LAUNCHES CHARACTER DOWN
	public void launchDown()
	{
		double launchSpeed = characterPercent;
		velocityY=launchSpeed;
		hitstun=true;
	}
	
	//RESPAWNS CHARACTER
	public void respawn()
	{
		hide(attackX);
		hide(attackY);
		velocityX = 0;
		velocityY = 0;
		stocks--;
		characterPercent = 0;
		setX(spawnX);
		setY(50);
		isRespawning = true;
		delay.setOnFinished( event ->isRespawning = false);
		delay.play();
		character.setFill(new ImagePattern(respawnImage));
	}
	
	
}