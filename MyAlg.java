import java.util.Random;

public class MyAlg{
	char[][] boardIn;
	char[][] boardOut;
	static char me = 'X';
	static char rival = 'O';


/*
	public String autoDrop(char[][] board){
		char random1 = (char)(rand.nextInt(board.length-1)+'A');
		int random2 = rand.nextInt(board.length-1)+1;
		String result = "";
		return random1 + (result + random2);
	}
*/

	//The main method to make the drop to Renju.java
	public String autoDrop(char[][] board){
		this.boardIn = board;
		int index[] = new int[2];

/**/	int depth = 1;

		int score = this.bestMove(index,board,depth,true);

		char row = (char)(index[0]+'A');
		int col = index[1]+1;
		String result = "";

/*debug*/		System.out.println("score of this move = " + score);
		
		return row + (result + col);
	}

	//MinMax alg for best move
	public int bestMove(int[] index, char[][] boardIn, int depth, boolean me){
		//stop the recursion by returning the board without change
		if((depth == 0) || (isFull(boardIn))){
			return score(boardIn);
		}
		if(me == true){
			int value = -99999999;
			for(int i = 0; i < boardIn.length; i++){
				for(int j = 0; j < boardIn[0].length; j++){
					char[][] board = boardIn;
					board[i][j] = 'X';
					int score = bestMove(index, board, depth-1, false);
					if(value <= score){
						boardIn = board;
						index[0] = i;
						index[1] = j;
						value = score;
					}
				}
			}
			return value;
		}else{
			int value = 99999999;
			for(int i = 0; i < boardIn.length; i++){
				for(int j = 0; j < boardIn[0].length; j++){
					char[][] board = boardIn;
					board[i][j] = 'O';
					int score = bestMove(index, board, depth-1, true);
					if(value >= score){
						boardIn = board;
						value = score;
					}
				}
			}
			return value;

		}

	}

	//FIXME:check the score of the board
	static int score(char[][] board){
		char symbol[] = {MyAlg.rival, MyAlg.me};
		int[] me = new int[20];
		int mecount = 0;
		int[] rival = new int[20];
		int ricount = 0;
		//first check O then check X
		for(int k = 0; k < symbol.length; k++){
			
			//check horizontally and vertically
			for(int i = 1; i < board.length; i++){
				int countH = 0;
				int countV = 0;
/*debug*///       System.out.println("");
			for(int j = 1; j < board.length; j++){
					//horizontal
					if(board[i][j] == symbol[k] && 
					   j != 1 && 
					   board[i][j-countH-1] == '-'){
						countH++;
/*debug*/  //				System.out.println("CountH++, now countH is " + countH);
					}else{
						if(countH != 0 &&
						   board[i][j] == '-'){
							if(symbol[k] == 'O'){
								rival[ricount] = countH;
								ricount++;
							}else{
								me[mecount] = countH;
								mecount++;
							}
						}
/*debug*/	//					System.out.print(countH + " ");
						countH = 0;
					}
					//vertical
					if(board[j][i] == symbol[k] && 
				       j != 1 &&
					   board[j-countV-1][i] == '-'){
						countV++;
					}else{
						if(countV != 0 &&
						   board[j][i] == '-'){
							if(symbol[k] == 'O'){
								rival[ricount] = countV;
								ricount++;
							}else{
								me[mecount] = countV;
								mecount++;
							}
						}
						countV = 0;
					}
				}
			}
				//check diagonally, upper triangle
			for(int i = 1; i < board.length; i++){
				int countUL = 0;
				int countUR = 0;
				int countBL = 0;
				int countBR = 0;
				for(int j = 0; j+i < board.length; j++){
					
					//from up left to bottom right(upper triangle)
					if(board[1+j][i+j] == symbol[k] &&
					   board[j-countUR][i+j-countUR-1] == '-'){
						countUR++;
					}else{
						if(countUR != 0 &&
						   board[1+j][i+j] == '-'){
							if(symbol[k] == 'O'){
								rival[ricount] = countUR;
								ricount++;
							}else{
								me[mecount] = countUR;
								mecount++;
							}
						}
						countUR = 0;
					}
					//from up right to bottom left(upper triangle)
					if(board[1+j][board.length-i-j] == symbol[k] &&
					   board.length-i-j+countUL+1 != board.length &&
					   board[j-countUL][board.length-i-j+countUL+1] == '-'){
						countUL++;
					}else{
						if(countUL != 0 &&
						   board[1+j][board.length-i-j] == '-'){
							if(symbol[k] == 'O'){
								rival[ricount] = countUL;
								ricount++;
							}else{
								me[mecount] = countUL;
								mecount++;
							}
						}
						countUL = 0;
					}
					//from bottom left to up right(lower triangle)
					if(board[board.length-1-j][i+j] == symbol[k] &&
					   board.length-j+countBR != board.length &&
					   board[board.length-j+countBR][i+j-countBR-1] == '-'){
						countBR++;
					}else{
						if(countBR != 0 &&
						   board[board.length-j-1][i+j] == '-'){
							if(symbol[k] == 'O'){
								rival[ricount] = countBR;
								ricount++;
							}else{
								me[mecount] = countBR;
								mecount++;
							}
						}
						countBR = 0;
					}
					//from bottom right to up left(lower triangle)
					if(board[board.length-1-j][board.length-i-j] == symbol[k] &&
					   board.length-j+countBL != board.length &&
					   board.length-i-j+1+countBL != board.length &&
					   board[board.length-j+countBL][board.length-i-j+1+countBL] == '-'){
						countBL++;
					}else{
						if(countBL != 0 &&
						   board[board.length-j-1][board.length-i-j] == '-'){
							if(symbol[k] == 'O'){
								rival[ricount] = countBL;
								ricount++;
							}else{
								me[mecount] = countBL;
								mecount++;
							}
						}
	
						countBL = 0;
					}
				}
			}
		}

		return finalScore(me,rival);
	}

	static int finalScore(int[] me, int[] rival){
		int score = 0;
		int two = 0;
		int three = 0;
		int four = 0;
/*debug*//*   System.out.print("me: "); 
			for(int i = 0; i < me.length; i++){
				System.out.print(me[i] + " ");
			}
			System.out.println("");
			System.out.print("rival: ");
			for(int j = 0; j < rival.length; j++){
				System.out.println(rival[j] + " ");
			}
*/		for(int i = 0; i < me.length; i++){
			if(me[i] == 1){
				score = score + 1;
			}else if(me[i] == 2){
				two++;
			}else if(me[i] == 3){
				three++;
			}else if(me[i] == 4){
				four++;
			}else if(me[i] == 5){
				score = 99999999;
			}
		}
		//add the score of continuous two
		if(two == 1){
			score = score + 200;
		}else if(two == 2){
			score = score + 500;
		}
		//add the score of continuous three
		if(three == 1){
			score = score + 1000;
		}else if(three == 2){
			score = score + 2000;
		}
		//add the score of continuous four
		if(four == 1){
			score = score + 5000;
		}else if(four == 2){
			score = score + 10000;
		}

/*debug*///	System.out.println("me: " + score);

		two = 0;
		three = 0;
		four = 0;
		for(int i = 0; i < rival.length; i++){
			if(rival[i] == 1){
				score = score - 1;
			}else if(rival[i] == 2){
				two++;
			}else if(rival[i] == 3){
				three++;
			}else if(rival[i] == 4){
				four++;
			}else if(rival[i] == 5){
				score = -99999999;
			}
		}
		//add the score of continuous two
		if(two == 1){
			score = score - 200;
		}else if(two == 2){
			score = score - 500;
		}
		//add the score of continuous three
		if(three == 1){
			score = score - 1000;
		}else if(three == 2){
			score = score - 2000;
		}
		//add the score of continuous four
		if(four == 1){
			score = score - 5000;
		}else if(four == 2){
			score = score - 10000;
		}

/*debug*/// System.out.println("Final: " + score);
		return score;
	
	}

	//FIXME
	static boolean isFull(char[][] board){
		int emptyCount = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board.length; j++){
				if(board[i][j] == '-'){
					emptyCount++;
				}
			}
		}
		if(emptyCount == 0){
			return true;
		}else{
			return false;
		}
	}


	public static void main(String[] args){
		MyAlg obj = new MyAlg();
		System.out.println(score(obj.testFiveR));
		
		System.out.println(obj.autoDrop(obj.test));
	}


	char[][] test = {{' ','1','2','3','4','5','6'},
					{'A','-','-','-','-','-','-'},
					{'B','-','-','-','-','-','-'},
					{'C','-','-','-','-','-','-'},
					{'D','-','-','-','-','-','-'},
					{'E','-','-','-','-','-','-'},
					{'F','-','-','-','-','-','-'}};


	char[][] testFiveR = {{' ','1','2','3','4','5','6','7'},
					 {'A','-','-','-','-','-','-','-'},
					 {'B','-','-','-','-','-','-','-'},
					 {'C','-','O','O','O','O','O','-'},
					 {'D','-','-','-','-','-','-','-'},
					 {'E','-','-','-','-','-','-','-'},
					 {'F','-','-','-','-','-','-','-'},
					 {'G','-','-','-','-','-','-','-'}};


	char[][] testFiveM = {{' ','1','2','3','4','5','6'},
					 {'A','-','-','-','-','-','-'},
					 {'B','-','-','-','-','-','-'},
					 {'C','-','X','X','X','X','X'},
					 {'D','-','-','-','-','-','-'},
					 {'E','-','-','-','-','-','-'},
					 {'F','-','-','-','-','-','-'}};


	char[][] testFourR = {{' ','1','2','3','4','5','6'},
					 {'A','-','-','-','-','-','-'},
					 {'B','-','-','-','-','-','-'},
					 {'C','-','O','O','O','O','O'},
					 {'D','-','-','-','-','-','-'},
					 {'E','-','-','-','-','-','-'},
					 {'F','-','-','-','-','-','-'}};


}
