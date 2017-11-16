package kakuro;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;

public class LandingPage {

	private GameBoard g;
	private GridPane appContent = new GridPane();
	Image title = new Image("File:Resources/img/title.png");
	Image loadGame1 = new Image("File:Resources/img/loadgame1.png");
	Image random1 = new Image("File:Resources/img/random1.png");
	Image exit1 = new Image("File:Resources/img/exit1.png");
	Image loadGame2 = new Image("File:Resources/img/loadgame2.png");
	Image random2 = new Image("File:Resources/img/random2.png");
	Image exit2 = new Image("File:Resources/img/exit2.png");

	/**
	 * Constructor generates a grid-pane of action buttons for the landing page.
	 * @param gB: Reference to the game-board object.
	 */
	public LandingPage(GameBoard gB) {
		g = gB;
		
		BackgroundImage bI = new BackgroundImage(new Image("File:Resources/img/background.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		          
		//Sets background to image.
		appContent.setBackground(new Background(bI));

		appContent.setVgap(5);
		appContent.setPadding(new Insets(5, 10, 5, 10));

		// Title Button Generation
		{
			Button button1 = new Button();
			button1.setGraphic(new ImageView(title));
			appContent.add(button1, 0, 0);
			GridPane.setHalignment(button1, HPos.CENTER);
			GridPane.setMargin(button1, new Insets(0, 0, 10, 0));
		}

		// Random Button Generation / Event Handling
		{
			Button button2 = new Button();
			button2.getStyleClass().add("landing-button");
			button2.setGraphic(new ImageView(random1));

			button2.setOnMouseEntered(event -> {
					button2.setGraphic(new ImageView(random2));
	        	});

			button2.setOnMouseExited(event -> {
				button2.setGraphic(new ImageView(random1));
	    	});

			int[] sizes = {6, 8, 10};
			int randomBoardSize = sizes[(int) (Math.random() * 3)];
			button2.setOnMouseClicked(event -> g.generateRandomBoard(randomBoardSize));

			appContent.add(button2, 0, 1);
			GridPane.setHalignment(button2, HPos.CENTER);
		}

		// Load Button Generation / Event Handling
		{
			Button button3 = new Button();
			button3.getStyleClass().add("landing-button");
			button3.setGraphic(new ImageView(loadGame1));

			button3.setOnMouseEntered(event -> {
				button3.setGraphic(new ImageView(loadGame2));
			});

			button3.setOnMouseExited(event -> {
				button3.setGraphic(new ImageView(loadGame1));
			});

			button3.setOnMouseClicked(event -> g.open());

			appContent.add(button3, 0, 2);
			GridPane.setHalignment(button3, HPos.CENTER);
		}

		// Exit Button Generation / Event Handling
		{
			Button button4 = new Button();
			button4.getStyleClass().add("landing-button");
			button4.setGraphic(new ImageView(exit1));

			button4.setOnMouseEntered(event -> {
				button4.setGraphic(new ImageView(exit2));
			});

			button4.setOnMouseExited(event -> {
				button4.setGraphic(new ImageView(exit1));
			});

			button4.setOnMouseClicked(event -> Platform.exit());

			appContent.add(button4, 0, 3);
			GridPane.setHalignment(button4, HPos.CENTER);
		}
	}

	/**
	 * @return: Returns the landing page as a GUI object to be placed on Screen.
	 */
	public GridPane getLandingPage() {
		return appContent;
	}

}
