package dkeep.logic;

import java.io.Serializable;
/**
 * ComportamentoGuarda.java - Class that is responsible for storing a guard movement routine and moving him.
 */
public class ComportamentoGuarda implements Serializable{
	char mov[];
	int i = 0;

	/**
	 * Constructor of the class ComportamentoGuarda.
	 * @param mov Array of chars that represents the Guarda movement.
	 */
	public ComportamentoGuarda(char mov[]) {
		this.mov = mov;
	}
	
	
	/**
	 * Method to invert the input for the guard movement and adjust the current position in the movement array.
	 * @param input Direction from the movement array.
	 * @return Inverted input.
	 */
	private char invertInput(char input){
		i--;
		if (i < 0)
			i = mov.length - 1;
		input = mov[i];
		switch (input) {
		case 's':return'w';
		case 'a':return'd';
		case 'd':return'a';
		case 'w':return's';
		}
		System.out.println("erro em invert input");
		return ' ';
	}
	
	private boolean moveCondition(int x, int y,char[][] temp){
		if (temp[y][x] == 'x') {
			return true;
		}
		if (temp[y][x] == 'i') {
			return true;
		}
		return false;
	}

	private void caseStuff(char input,int x,int y){
		switch (input) {
		case 's':y++;break;
		case 'w':y--;break;
		case 'd':x++;break;
		case 'a':x--;break;
		default: System.out.println("default input");
		}
	}
	
	private void caseStuffGuarda(char input,Guarda g){
		switch (input) {
		case 's':g.y++;break;
		case 'w':g.y--;break;
		case 'd':g.x++;break;
		case 'a':g.x--;break;
		default: System.out.println("default input");
		}
	}
	
	/**
	 * Method responsible for moving a Guarda.
	 * @param dir Flag to indicate the direction the Guarda is taking in its movement array.
	 * @param guarda Guard that is going to move.
	 * @param b Board in which the to move the Guarda.
	 */
	public void move(Guarda guarda, Board b) {
		int x= guarda.getX(),y=guarda.getY();
		boolean dir = guarda.getDir();
		char temp[][] = b.getBoard();
		char input = mov[i];
		if (!dir) {input = invertInput(input);}
		else {i++;}
		if (i == mov.length){i = 0;}
		caseStuffGuarda(input, guarda);
		caseStuff(input, x, y);

		if (moveCondition(x, y, temp)) {return;}
		guarda.setCurrent((char) b.refresh(guarda, input));

	}

}
