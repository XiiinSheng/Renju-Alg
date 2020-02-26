import java.util.Random;

public class MyAlg{
	static Random rand = new Random();
	String autoDrop(char[][] board){
		char random1 = (char)(rand.nextInt(board.length-1)+'A');
		int random2 = rand.nextInt(board.length-1)+1;
		String result = "";
		return random1 + (result + random2);
	}

	public static void main(String[] args){}
}
