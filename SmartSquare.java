import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SmartSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 13;
	public boolean checked = false;
	public int num_bombs = 0;
	

	public SmartSquare(int x, int y, GameBoard board) //Constuctor and passes varaibles onto the super class GameSquare
	{
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}

	public void clicked()
	{
		if(checked == false) { //Checks if the button has been clicked before or has been unveiled
			checked = true; 
			if(thisSquareHasBomb == false) { //Checks if the square has a bomb in it
				num_bombs = checkadjbomb(); //If not then it detects how many bombs are in the surrounding adjacent squares
				changepic(num_bombs);
				if(num_bombs == 0) { //Checks if the bomb count was 0 and if it should remove an area instead of 1 square
					clearspace(xLocation, yLocation); //Calls he clearspace method which will remove all other squares that have 0 bombs around it that are connected to this one
				}
			}	
			else {
				setImage("images/bomb.png"); //If it does have a bomb in the square then replace the image with a bomb
				gameover(); //Calls the gameover method which disables all buttons and shows the bombs
			}		
		}
	}
	
	public int checkadjbomb() {
		SmartSquare game = null; //Creates the instance variable
		
		int bombcount = 0;
		
		for(int i = xLocation-1; i <= xLocation+1; i++) { //Loops to check the 8 adjacent squares around this one
			for(int k = yLocation-1; k <= yLocation+1; k++) {
				game = (SmartSquare) board.getSquareAt(i,k); //Fills the instance variable with the new adjacent instance to be check
				if(game != null && game.thisSquareHasBomb == true) { //If there is a bomb in the adjacent square being checked then add one to the bomb count
					bombcount++;
				}
			}	
		}
		return(bombcount); //Returns the number of bombs in the adjacent area
	}
	
	public void changepic(int bombnum) {
		setImage("images/"+bombnum+".png"); //Calls the GameSquare method to change the image in the jbutton with the correct number of bombs around
	}
	
	public void clearspace(int oldx, int oldy) { //The method that reveals all other boxes with 0 bombs in the surrounding area 
		SmartSquare game = null;
		for (int x = oldx-1; x <=(oldx+1); x++) { //Checks adjacent sqaures to see if any of the adjacent squares have no boms adjacent to them
	          for (int y= oldy-1; y<=(oldy+1); y++) {
	        	  game = (SmartSquare) board.getSquareAt(x,y);
	        	  if(game != null && game.checkadjbomb() == 0 && game.checked == false ) {
	        		  game.checked = true;  //If the instance returned is not null and has no bombs adjacent to it and hasn't been clicked then change the jbutton image
	        		  game.changepic(0);
	        		  clearspace(x,y); //Runs clearspace again but inputs the current x and y coordinates to find any squares adjacent to this one with no bombs adjacent to them 
	        	  }
	        	  else if(game != null && game.thisSquareHasBomb == false) { //if it does have a bomb adjacent to it then display this as an edge and change the image to how many
	        		  game.changepic(game.checkadjbomb());  
	        	  }
	          }      
		}
	}
	public void gameover() { //This methods finds all the bombs and displays them while disabling all buttons so the game is over
		int x = 0;
		int y = 0;
		SmartSquare game = (SmartSquare) board.getSquareAt(x,y);
		for (x = 0; game != null; x++) { //Loops to check if the square has a bomb in it
			for (y = 0; game != null; y++) {
		     	game = (SmartSquare) board.getSquareAt(x,y);
		      	game.checked = true; //Since the game is over it labels the square as checked so the user can't click it (disabling it)
		      	if(game.thisSquareHasBomb == true) { //Detects if the square has a bomb
		        	game.setImage("images/bomb.png"); //If it does then change the image of the jbutton
		        }
		        if(board.getSquareAt(x,y+1) == null) { //Checks if its reached the maximum width
		        	y=0; //If it has reached the end of the y axis then reset it and break and repeat for the next line on x axis
		        	break;
		        }
			}
		    if(board.getSquareAt(x+1,0) == null) {  //Checks if its reached the maximum height
		    	break; //If it has then break as its checked the whole board
		    }
		}
	}
	
	
}