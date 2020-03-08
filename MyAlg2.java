/***********************
*	Renju AI
*	Copyright: Xin Sheng
*	Description: calculates Renju best game theory using MinMax Theorem
*/

import java.util.Random;
import java.util.*;

public class MyAlg2{
	static char me = 'X';
	static char rival = 'O';


/* Original random drop method

	public String autoDrop(char[][] board){
		char random1 = (char)(rand.nextInt(board.length-1)+'A');
		int random2 = rand.nextInt(board.length-1)+1;
		String result = "";
		return random1 + (result + random2);
	}
*/

	//The main method to make the drop to Renju.java
	public String autoDrop(char[][] initialBoard){
		int index[] = new int[2];

/**/	int depth = 3;
		int score = 0;
		int[] alphaBeta = {-999999999,999999999};

		if(isAlmostEmpty(initialBoard)){
			int i = 1;
			index[0] = initialBoard.length/2;
			index[1] = index[0];
			while(initialBoard[index[0]][index[1]] != '-'){
				index[0] = index[0]+i;
				index[1] = index[0];
				i++;
			}
		}else{
			score = this.bestMove(index,initialBoard,depth,true,alphaBeta);
		}

		char row = (char)(index[0]+'@');
		int col = index[1];
/*debug*///   System.out.println("index: " + index[0] + " " + index[1]);
		String result = "";

/*debug*///		System.out.println("score of this move = " + score);
		
		return row + (result + col);
	}

	//MinMax alg for best move
	public int bestMove(int[] index, char[][] boardIn, int depth, boolean me, int[] alphaBeta){
		char[][] boardStatic = new char[boardIn.length][boardIn.length];
		int[] thisAB = {alphaBeta[0], alphaBeta[1]};
		copy(boardStatic,boardIn);
		//stop the recursion by returning the board without change
		if((depth == 0) || (isFull(boardStatic)) || (score(boardIn) >= 9999999) || (score(boardIn) <= -9999999)){
			return score(boardIn);
		}
		if(me == true){
			int value = -999999999;
			for(int i = 1; i < boardIn.length; i++){
				for(int j = 1; j < boardIn[0].length; j++){
					if(boardStatic[i][j] == '-'){
						char[][] board1 = new char[boardStatic.length][boardStatic.length];
						copy(board1,boardStatic);
						board1[i][j] = 'X';
						int score = bestMove(index, board1, depth-1, false, thisAB);
						if(value < score){
							boardIn = board1;
							index[0] = i;
							index[1] = j;
							value = score;
							thisAB[0] = Math.max(thisAB[0],score);
						}
						if(thisAB[0] >= thisAB[1]){
							int min = Math.min(thisAB[0], thisAB[1]);
							alphaBeta[1] = Math.min(min, alphaBeta[1]);
							break;
						}
					}
				}
				if(thisAB[0] >= thisAB[1]){
					break;
				}
			}
			return value;
		}else{
			int value = 999999999;
			for(int i = 1; i < boardIn.length; i++){
				for(int j = 1; j < boardIn[0].length; j++){
					if(boardStatic[i][j] == '-'){
						char[][] board2 = new char[boardStatic.length][boardStatic.length];
						copy(board2,boardStatic);
						board2[i][j] = 'O';
						int score = bestMove(index, board2, depth-1, true, thisAB);
						if(value > score){
							boardIn = board2;
							value = score;
							thisAB[1] = Math.min(thisAB[1],score);
						}
						if(thisAB[0] >= thisAB[1]){
							int max = Math.max(thisAB[0], thisAB[1]);
							alphaBeta[0] = Math.max(max, alphaBeta[0]);
							break;
						}
					}
				}
				if(thisAB[0] >= thisAB[1]){
					break;
				}
			}
			return value;

		}

	}

	//FIXME:check the score of the board
	static int score(char[][] board){
		char symbol[] = {MyAlg.rival, MyAlg.me};
		int[] me = new int[50];
		int mecount = 0;
		int[] rival = new int[50];
		int ricount = 0;
		//first check O then check X
		for(int k = 0; k < symbol.length; k++){
			
			//check horizontally and vertically
			for(int i = 1; i < board.length; i++){
				int countH = 0;
				int countV = 0;
				int emptySideH = 0;
				int emptySideV = 0;
/*debug*///       System.out.println("");
				for(int j = 1; j < board.length; j++){
					//horizontal
					if(board[i][j] == symbol[k]){
						countH++;
						if(j != 1 && 
					       board[i][j-1] == '-'){
							emptySideH++;
						}
						if(countH > 1 && (emptySideH != 0) && (j == board.length-1)){
							if(symbol[k] == 'O'){
								rival[ricount] = countH;
								ricount++;
							}else{
								me[mecount] = countH;
								mecount++;
							}
						}
	
/*debug*/  //				System.out.println("CountH++, now countH is " + countH);
					}else{
						if(countH != 0 &&
						   board[i][j] == '-'){
						   	emptySideH++;
						}
						if(countH > 1 && (emptySideH != 0 || countH >= 5)){
							int num = 0;
							if(emptySideH == 2 && countH == 3){
								num = 10;
							}else if(emptySideH == 1 && countH == 4){
								num = 11;
							}else{
								num = countH;
							}
							
							if(symbol[k] == 'O'){
								rival[ricount] = num;
								ricount++;
							}else{
								me[mecount] = num;
								mecount++;
							}
						}
/*debug*/	//					System.out.print(countH + " ");
						countH = 0;
/*debug*/// 				System.out.println("EmptySideH = " + emptySideH);
						emptySideH = 0;
					}
					//vertical
					if(board[j][i] == symbol[k]){
						countV++;
						if(j != 1 &&
						   board[j-1][i] == '-'){
							emptySideV++;
						}
						if(countV > 1 && emptySideV != 0 && j == board.length-1){
							if(symbol[k] == 'O'){
								rival[ricount] = countV;
								ricount++;
							}else{
								me[mecount] = countV;
								mecount++;
							}
						}
	
					}else{
						if(countV != 0 &&
						   board[j][i] == '-'){
						   	emptySideV++;
						}
						if(countV > 1 && (emptySideV != 0 || countV >= 5)){
								int num = 0;
							if(emptySideV == 2 && countV == 3){
								num = 10;
							}else if(emptySideV == 2 && countV == 4){
								num = 11;
							}else{
								num = countV;
							}

							if(symbol[k] == 'O'){
								rival[ricount] = num;
								ricount++;
							}else{
								me[mecount] = num;
								mecount++;
							}
						}
						countV = 0;
						emptySideV = 0;
					}
				}
			}
				//check diagonally, upper triangle
			for(int i = 1; i < board.length; i++){
				int countUL = 0;
				int countUR = 0;
				int countBL = 0;
				int countBR = 0;
				int emptySideUL = 0;
				int emptySideUR = 0;
				int emptySideBL = 0;
				int emptySideBR = 0;
				for(int j = 0; j+i < board.length; j++){
					
					//from up left to bottom right(upper triangle)
					if(board[1+j][i+j] == symbol[k]){
						countUR++;
						if(board[j][i+j-1] == '-'){
							emptySideUR++;
						}
						if(countUR > 1 && emptySideUR != 0 && i+j == board.length-1){
							if(symbol[k] == 'O'){
								rival[ricount] = countUR;
								ricount++;
							}else{
								me[mecount] = countUR;
								mecount++;
							}
						}
	
					}else{
						if(countUR != 0 &&
						   board[1+j][i+j] == '-'){
							emptySideUR++;
						}
						if(countUR > 1 && (emptySideUR != 0 || countUR >= 5)){
							int num = 0;
							if(emptySideUR == 2 && countUR == 3){
								num = 10;
							}else if(emptySideUR == 2 && countUR == 4){
								num = 11;
							}else{
								num = countUR;
							}
	
							if(symbol[k] == 'O'){
								rival[ricount] = num;
								ricount++;
							}else{
								me[mecount] = num;
								mecount++;
							}
						}
						countUR = 0;
						emptySideUR = 0;
					}
					//from up right to bottom left(upper triangle)
					if(board[1+j][board.length-i-j] == symbol[k]){
						countUL++;
						if(board.length-i-j+1 != board.length &&
					       board[j][board.length-i-j+1] == '-'){
							emptySideUL++;
						}
						if(countUL > 1 && emptySideUL != 0 && board.length-i-j == 1){
							if(symbol[k] == 'O'){
								rival[ricount] = countUL;
								ricount++;
							}else{
								me[mecount] = countUL;
								mecount++;
							}
						}
	

					}else{
						if(countUL != 0 &&
						   board[1+j][board.length-i-j] == '-'){
						   	emptySideUL++;
						}
						if(countUL > 1 && (emptySideUL != 0 || countUL >= 5)){
							int num = 0;
							if(emptySideUL == 2 && countUL == 3){
								num = 10;
							}else if(emptySideUL == 2 && countUL == 4){
								num = 11;
							}else{
								num = countUL;
							}
	
							if(symbol[k] == 'O'){
								rival[ricount] = num;
								ricount++;
							}else{
								me[mecount] = num;
								mecount++;
							}
						}
						countUL = 0;
						emptySideUL = 0;
					}
					//from bottom left to up right(lower triangle)
					if(board[board.length-1-j][i+j] == symbol[k]){
						countBR++;
						if(board.length-j != board.length &&
  					   	   board[board.length-j][i+j-1] == '-'){
							emptySideBR++;
						}
						if(countBR > 1 && emptySideBR != 0 && i+j == board.length-1){
							if(symbol[k] == 'O'){
								rival[ricount] = countBR;
								ricount++;
							}else{
								me[mecount] = countBR;
								mecount++;
							}
						}
	
					}else{
						if(countBR != 0 &&
						   board[board.length-j-1][i+j] == '-'){
						   	emptySideBR++;
						}
						if(countBR > 1 && (emptySideBR != 0 || countBR >= 5)){
							int num = 0;
							if(emptySideBR == 2 && countBR == 3){
								num = 10;
							}else if(emptySideBR == 2 && countBR == 4){
								num = 11;
							}else{
								num = countBR;
							}
	
							if(symbol[k] == 'O'){
								rival[ricount] = num;
								ricount++;
							}else{
								me[mecount] = num;
								mecount++;
							}
						}
						countBR = 0;
						emptySideBR = 0;
					}
					//from bottom right to up left(lower triangle)
					if(board[board.length-1-j][board.length-i-j] == symbol[k]){
						countBL++;
						if(board.length-j != board.length &&
					       board.length-i-j+1 != board.length &&
					       board[board.length-j][board.length-i-j+1] == '-'){
							emptySideBL++;
						}
						if(countBL > 1 && emptySideBL != 0 && board.length-i-j == 1){
							if(symbol[k] == 'O'){
								rival[ricount] = countBL;
								ricount++;
							}else{
								me[mecount] = countBL;
								mecount++;
							}
						}
	
					}else{
						if(countBL != 0 &&
						   board[board.length-j-1][board.length-i-j] == '-'){
						   	emptySideBL++;
						}
						if(countBL > 1 && (emptySideBL != 0 || countBL >= 5)){
							int num = 0;
							if(emptySideBL == 2 && countBL == 3){
								num = 10;
							}else if(emptySideBL == 2 && countBL == 4){
								num = 11;
							}else{
								num = countBL;
							}
	
							if(symbol[k] == 'O'){
								rival[ricount] = num;
								ricount++;
							}else{
								me[mecount] = num;
								mecount++;
							}
						}
	
						countBL = 0;
						emptySideBL = 0;
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
		int emptyThree = 0;
		int four = 0;
		int emptyFour = 0;

/*debug*/   System.out.print("me: "); 
			for(int i = 0; i < me.length; i++){
				System.out.print(me[i] + " ");
			}
			System.out.println("");
			System.out.print("rival: ");
			for(int j = 0; j < rival.length; j++){
				System.out.print(rival[j] + " ");
			}


		for(int i = 0; i < me.length; i++){
			if(me[i] == 2){
				two++;
			}else if(me[i] == 3){
				three++;
			}else if(me[i] == 4){
				four++;
			}else if(me[i] == 10){
				emptyThree++;
			}else if(me[i] == 11){
				emptyFour++;
			}else if(me[i] >= 5){
				score = 999999999;
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
		//add the score of continuous three that's empty on both sides
		if(emptyThree == 1){
			score = score + 30000;
		}else if(emptyThree == 2){
			score = score + 65000;
		}
		//add the score of continuous four
		if(four == 1){
			score = score + 30000;
		}else if(four == 2){
			score = score + 60000;
		}
		//add the score of continuous four that's empty on both sides
		if(emptyFour == 1){
			score = score + 600000;
		}else if(emptyFour == 2){
			score = score + 800000;
		}

/*debug*///	System.out.println("me: " + score);

		two = 0;
		three = 0;
		emptyThree = 0;
		four = 0;
		emptyFour = 0;

		for(int i = 0; i < rival.length; i++){
			if(rival[i] == 2){
				two++;
			}else if(rival[i] == 3){
				three++;
			}else if(rival[i] == 4){
				four++;
			}else if(rival[i] == 10){
				emptyThree++;
			}else if(rival[i] == 11){
				emptyFour++;
			}else if(rival[i] >= 5){
				score = -99999999;
			}
		}
/*debug*/ System.out.println("Two: " + two);
	//add the score of continuous two
		if(two == 1){
			score = score - 210;
		}else if(two == 2){
			score = score - 510;
		}
		//add the score of continuous three
		if(three == 1){
			score = score - 1010;
		}else if(three == 2){
			score = score - 2010;
		}
		//add the score of continuous three that's empty on both sides
		if(emptyThree == 1){
			score = score - 40000;
		}else if(emptyThree == 2){
			score = score - 70000;
		}
		//add the score of continuous four
		if(four == 1){
			score = score - 40000;
		}else if(four == 2){
			score = score - 70000;
		}
		//add the score of continuous four that's empty on both sides
		if(emptyFour == 1){
			score = score - 800100;
		}else if(emptyFour == 2){
			score = score - 1000200;
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

	static void copy(char[][] a, char[][] b){
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b.length; j++){
				a[i][j] = b[i][j];
			}
		}
	}

	static boolean isAlmostEmpty(char[][] board){
		int fullSpot = 0;
		for(int i = 1; i < board.length; i++){
			for(int j = 1; j < board.length; j++){
				if(board[i][j] != '-'){
					fullSpot++;
				}
				if(fullSpot > 1){
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args){
		MyAlg2 obj = new MyAlg2();

/*
char[][] board = new char[16][16];
char row = '@';
int col = 1;
for(int i = 0; i < board.length; i++){
for(int j = 0; j < board[i].length; j++){
if(j == 0){
board[i][j] = row;
row = (char)(row+1);
}else if(i == 0){
board[i][j] = (char)(col+'0');
col++;
}else{
board[i][j] = '-';
}
}
}
board[0][0] = ' ';
board[5][5] = 'O';
		System.out.println(score(board));
*/

//		System.out.println(obj.autoDrop(board));
		
		System.out.println(score(obj.testFourR));
		System.out.println(obj.autoDrop(obj.testFourR));
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
					 {'C','-','O','-','-','-','-','-'},
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
					 {'A','O','-','-','-','-','-'},
					 {'B','-','O','-','-','-','-'},
					 {'C','-','-','-','-','-','-'},
					 {'D','-','-','-','O','-','-'},
					 {'E','-','-','-','-','O','-'},
					 {'F','-','-','-','-','-','-'}};


}
