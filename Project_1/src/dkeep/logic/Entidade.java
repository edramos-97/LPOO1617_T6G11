package dkeep.logic;

public abstract class Entidade {
	int x,y;
	char tag=' ';
	char current=' ';
	
	public Entidade(int x, int y,char tag){
		this.x = x;
		this.y = y;
		this.tag = tag;
	}

	public char getTag() {
		return tag;
	}

	public void setTag(char tag) {
		this.tag = tag;
	}

	public char getCurrent() {
		return current;
	}

	public void setCurrent(char current) {
		this.current = current;
	}

	public abstract boolean print(String level,char current,Board b);
	
	public abstract void move(char direction, Board b);
	
}