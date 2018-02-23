# Kakuro
This project was completed by Phil Smith and Bobby Palmer during the fall 2017 semester for <i>CSC 331 - Object Oriented Programming and Design</i> at the University of North Carolina Wilmington. The project requirements were fairly concise. Our professor uploaded a picture of the "Conceptis Kakuro" puzzle game from a news paper and told us to write a program that only allows a user to enter valid values into each of its puzzle squares. Apart from that, it simply had to be able to read a puzzle of arbitrary size from a file, save an incomplete puzzle back to a file, clear the puzzle board, and implement one extra feature of our own choosing. 

<h2>The Project</h2>
The program opens with a landing page where the user can select a random game (a randomly generated 6x6, 8x8, or 10x10 puzzle board), or to load a game from a saved file. We gave it the name <i>Taidana Kakuro</i>, Japanese for lazy kakuro, because the way we implemented it, it's not much of a challenge. The user can also select from any of the normal menu options.
<br/>
<img src="https://raw.githubusercontent.com/No-Life-King/Kakuro/master/Resources/img/landing_page.png"/>

If the user selects "new" from the game menu, the puzzle below will be loaded, which is a virtual copy of the picture of the puzzle that our professor uploaded as an example. Gameplay is simple, the user clicks on any square and the valid numbers for each square will drop down below. Just as in normal kakuro, the row values must add up to the sum to the left, and the column values must add up to the sum on top. No value may be repeated in any row or column. Our gameboard automatically adjusts to entered values. 

<img src="https://raw.githubusercontent.com/No-Life-King/Kakuro/master/Resources/img/the_puzzle.png"/>

If a button from the "new board" menu is clicked, a new puzzle of the chosen size will be randomly generated. At any time during gameplay, the "cheat" key may be pressed and it will automatically fill out one square for the user if that square has only one possible value. This action may also be triggered by pressing the spacebar.

<img src="https://raw.githubusercontent.com/No-Life-King/Kakuro/master/Resources/img/random_board.png"/>

<h2>How To</h2>
The source code for this project exists within an E(fx)clipse project, so E(fx)clipse may be used to import and run the project from source. Alternatively, the "Executable Jar" folder contents may be downloaded and then the .jar can simply be double-clicked to be run (as long as it exists within the same path as the "Resources" folder and as long as the path to java is configured within your OS). Otherwise, you can run the .jar from command-line with: <code>java -jar Taidana\ Kakuro.jar</code>
or just import the code and resources into any JavaFX compatible IDE. 
