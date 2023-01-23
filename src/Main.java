import java.io.File;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;

public class Main extends Application  {
	//WINDOW WIDTH AND HEIGHT
	private static int width = 1920;
	private static int height = 1010;
	private Group root; 
	//---KEYBOARD VARIABLES---
	//PLAYER 1
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean upPressed = false;
	private boolean attack1;
	private boolean enableAttack1 = true;
	
	//PLAYER 2
	private boolean w;
	private boolean a;
	private boolean s;
	private boolean d;
	private boolean wPressed = false;
	private boolean attack2;
	private boolean enableAttack2 = true;
	
	//---STAGE ELEMENTS---
	
	//MAIN STAGE
	private GameStage gameStage = new GameStage((width-1200)/2,height-300);
	
	//Music/Sound Objects
	private Media song = new Media(this.getClass().getResource("Background.mp3").toExternalForm());
	private MediaPlayer backgroundMusic = new MediaPlayer(song);
	
	//scene variables
	Stage window;
	Scene scene, scene1, scene2;
	
	
	
	
	//STAGE ELEMENTS (PLATFORMS AND BASTZONES) - POLYMORPHISM
	StageElement[] stagePieces = 
		{
				new Platform((width-300)/2,height-625),
				new Platform((width-300)/2+550,height-500),
				new Platform((width-300)/2-550,height-500),
				new Blastzone((width-Blastzone.width)/2,(height-Blastzone.height)/2),
		};
	
	//CHARACTERS
	private Character boxy = new Character(1);
	private Character boxy2 = new Character(2);
	
	
	public static void main(String[] args) {
		//launch GUI menu and start method
		launch(args);
	}
	
	public void run(){
		//ADDING STAGE AND STAGE ELEMENTS
		add(gameStage);
		for(int i = 0; i < stagePieces.length; i++)
		{
			add(stagePieces[i]);
		}
		
		//ADDING CHARACTERS
		add(boxy);
		add(boxy2);
		boxy.respawn();
		boxy2.respawn();
		
		//ADDING MUSIC
		backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
		backgroundMusic.setVolume(0.7);
		backgroundMusic.play();
		
		//TIMELINE FOR GAME
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		timeline.setRate(60);
		
		//KEYBBOARD INPUTS
		root.setOnKeyPressed(keyPressed);
		root.setOnKeyReleased(keyReleased);
		}
	
	//EVENT HANDLERS
	//KEYBOARD
	EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>()
	{

		public void handle(KeyEvent event) {
			//P1 - DOWN
			if(event.getCode()==KeyCode.DOWN)
			{
				down = true;
			}
			//P1 - LEFT
			if(event.getCode()==KeyCode.LEFT)
			{
				left = true;
			}
			//P1 - RIGHT
			if(event.getCode()==KeyCode.RIGHT)
			{
				right = true;
			}
			//P1 - UP
			if(event.getCode()==KeyCode.UP && !upPressed)
			{
				up = true;
			}
			//P1 - ATTACK
			if(enableAttack1 && event.getCode()==KeyCode.PERIOD)
			{
				attack1 = true;
				enableAttack1 = false;
				attack1Animation.play();
				enable1.play();
			}
			
			//P2 - UP
			if(event.getCode()==KeyCode.W && !wPressed)
			{
				w = true;
			}
			//P2 - DOWN
			if(event.getCode()==KeyCode.S)
			{
				s = true;
			}
			//P2 - LEFT
			if(event.getCode()==KeyCode.A)
			{
				a = true;
			}
			//P2 - RIGHT
			if(event.getCode()==KeyCode.D)
			{
				d = true;
			}
			if(event.getCode()==KeyCode.R)
			{
				boxy.respawn();
				boxy2.respawn();
			}
			//P2 - ATTACK
			if(enableAttack2 && event.getCode()==KeyCode.F)
			{
				attack2 = true;
				enableAttack2 = false;
				attack2Animation.play();
				enable2.play();
			}
			
		}
		};
		
		//KEY RELEASES
		EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode()==KeyCode.DOWN)
				{
					down = false;
				}
				if(event.getCode()==KeyCode.LEFT)
				{
					left = false;
				}
				if(event.getCode()==KeyCode.RIGHT)
				{
					right = false;
				}
				if(event.getCode()==KeyCode.UP)
				{
					up = false;
					upPressed = false;
				}
				
				if(event.getCode()==KeyCode.W)
				{
					w = false;
					wPressed = false; 
				}
				if(event.getCode()==KeyCode.S)
				{
					s = false;
				}
				if(event.getCode()==KeyCode.A)
				{
					a = false;
				}
				if(event.getCode()==KeyCode.D)
				{
					d = false;
				}
				}
		};
		
		//SETS ATTACK TO FALSE AFTER 80 MILLISWCONDS
		Timeline attack1Animation = new Timeline(new KeyFrame(Duration.millis(110), event -> attack1 = false));
		Timeline attack2Animation = new Timeline(new KeyFrame(Duration.millis(110), event -> attack2 = false));
		
		//ENABLES ATTACK AGAIN AFTER 5 SECONDS
		Timeline enable1 = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> enableAttack1 = true));
		Timeline enable2 = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> enableAttack2 = true));
		
		//MAIN GAME TIMELINE
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			//UPDATES STAGE ELEMENTS
			for(int i = 0; i < stagePieces.length; i++)
			{
				stagePieces[i].testCollision(boxy, down);
				stagePieces[i].testCollision(boxy2, s);
			}
			//UPDATES CHARACTERS
			boxy.update(up, down, left, right, gameStage.getBounds(), boxy2);
			boxy2.update(w, s, a, d, gameStage.getBounds(), boxy);
			boxy.updateAttack(upPressed, down, left, right, attack1,boxy2);
			boxy2.updateAttack(wPressed, s, a, d, attack2,boxy);
			
			//ENSURES THAT UP IS ONLY TAPPED AND NOT HELD DOWN
			if(up)
			{
				up = false;
				upPressed = true;
			}
			if(w)
			{
				w = false;
				wPressed = true;
			}
			
			}));
	
		//ADD NODES TO STAGE
		public void add(Node node) {
		root.getChildren().add(node);
		}
	
	
	
	
	@Override
	public void start(Stage stage) throws Exception {
		//When an application is launched, an initial stage is created and
		//passed to this method
		stage.setTitle("BOX-ing");
		//Creates a scene class that contains all content for a Scene
		//Scene is set to a root
		 root = new Group();
		Scene scene = new Scene(root);
		//Scene is passed to the stage
		stage.setScene(scene);
		//Canvas object allows you to draw shapes and images
		menuStart(stage, scene);
		//root method to add canvas to Scene
		
		//method to add custom elements to stage
		run();
	}
	
	//method to run the start menu
	public void menuStart(Stage stage, Scene scene) {
		
		Image titleImage = new Image("TitleScreen.png",1920,1010,true,true);
		ImageView titleScreen = new ImageView(titleImage);
	
		//create the start button
		Button button = new Button("START");
		button.setTranslateX(835);
		button.setTranslateY(360);
		button.setMaxWidth(250);
		button.setMaxHeight(460);
		button.setOnAction(e -> stage.setScene(scene));
		
		//create the help button
		Button button1 = new Button("HELP");
		button1.setTranslateX(835);
		button1.setTranslateY(360);
		button1.setMaxWidth(250);
		button1.setMaxHeight(460);
		button1.setOnAction(e -> stage.setScene(scene));
		
		//add the exit button
		Button button2 = new Button("EXIT");
		button2.setTranslateX(1665);
		button2.setTranslateY(925);
		button2.setMaxWidth(250);
		button2.setMaxHeight(460);
		button2.setOnAction(e -> stage.close());
		
		Canvas canvas = new Canvas(width,height);
		root.getChildren().add(canvas);
		root.requestFocus();
		
		
		
		
		stage.show();
		//add the buttons to the scene 
		VBox layout = new VBox(0);
		layout.getChildren().add(titleScreen);
		layout.getChildren().addAll(button);
		layout.getChildren().addAll(button1);
		layout.getChildren().addAll(button2);
		scene1 = new Scene(layout, width,height);
		
		//setting the scene
		stage.setScene(scene1);
		//show the stage
		stage.show();
		
	}
	
}