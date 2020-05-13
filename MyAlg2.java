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

	public String getVersion(){
		return "V2";
	}
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

/**/	int depth = 2;
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
/*debug*/   System.out.println("index: " + index[0] + " " + index[1]);
		String result = "";

/*debug*/		System.out.println("score of this move = " + score);
		
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
						int[] newIndex = new int[2];
						int score = bestMove(newIndex, board1, depth-1, false, thisAB);
/*debug*///					System.out.println("ME: depth: " + depth + ", index = " + i + " " + j + ", score = " + score);
						if(value < score){
							boardIn = board1;
							index[0] = i;
							index[1] = j;
							value = score;
							thisAB[0] = Math.max(thisAB[0],score);
/*debug*///					System.out.println("Index: " + index[0] + " " + index[1]);
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
/*debug*///					System.out.println("RIVAL: depth: " + depth + ", index = " + i + " " + j + ", score = " + score);
						if(value > score){
							boardIn = board2;
							value = score;
							thisAB[1] = Math.min(thisAB[1],score);
/*debug*///					System.out.println("Index: " + index[0] + " " + index[1]);
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
		char symbol[] = {MyAlg2.rival, MyAlg2.me};
		int[] me = new int[8];
		int[] rival = new int[8];

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
						if(j == board.length-1 && countH > 1 && (emptySideH != 0 || countH >= 5)){
							if(symbol[k] == 'O'){
								rival[countH]++;
							}else{
								me[countH]++;
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
								num = 0;
							}else if(emptySideH == 2 && countH == 4){
								num = 1;
							}else{
								num = countH;
							}
							
							if(symbol[k] == 'O'){
								rival[num]++;
							}else{
								me[num]++;
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
						if(j == board.length-1 && countV > 1 && (emptySideV != 0 || countV >= 5)){
							if(symbol[k] == 'O'){
								rival[countV]++;
							}else{
								me[countV]++;
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
								num = 0;
							}else if(emptySideV == 2 && countV == 4){
								num = 1;
							}else{
								num = countV;
							}

							if(symbol[k] == 'O'){
								rival[num]++;
							}else{
								me[num]++;
							}
						}
						countV = 0;
						emptySideV = 0;
					}
				}
			
				//check diagonally
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
						if(i+j == board.length-1 && countUR > 1 && (emptySideUR != 0 || countUR >= 5)){
							if(symbol[k] == 'O'){
								rival[countUR]++;
							}else{
								me[countUR]++;
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
								num = 0;
							}else if(emptySideUR == 2 && countUR == 4){
								num = 1;
							}else{
								num = countUR;
							}
	
							if(symbol[k] == 'O'){
								rival[num]++;
							}else{
								me[num]++;
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
						if(board.length-i-j == 1 && countUL > 1 && (emptySideUL != 0 || countUL >= 5)){
							if(symbol[k] == 'O'){
								rival[countUL]++;
							}else{
								me[countUL]++;
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
								num = 0;
							}else if(emptySideUL == 2 && countUL == 4){
								num = 1;
							}else{
								num = countUL;
							}
	
							if(symbol[k] == 'O'){
								rival[num]++;
							}else{
								me[num]++;
							}
						}
						countUL = 0;
						emptySideUL = 0;
					}
					
					if(i+j+1 < board.length){
						i = i + 1;
					//from bottom left to up right(lower triangle)
					if(board[board.length-1-j][i+j] == symbol[k]){
						countBR++;
						if(board.length-j != board.length &&
  					   	   board[board.length-j][i+j-1] == '-'){
							emptySideBR++;
						}
						if(i+j == board.length-1 && countBR > 1 && (emptySideBR != 0 || countBR >= 5)){
							if(symbol[k] == 'O'){
								rival[countBR]++;
							}else{
								me[countBR]++;
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
								num = 0;
							}else if(emptySideBR == 2 && countBR == 4){
								num = 1;
							}else{
								num = countBR;
							}
	
							if(symbol[k] == 'O'){
								rival[num]++;
							}else{
								me[num]++;
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
						if(board.length-i-j == 1 && countBL > 1 && (emptySideBL != 0 || countBL >= 5)){
							if(symbol[k] == 'O'){
								rival[countBL]++;
							}else{
								me[countBL]++;
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
								num = 0;
							}else if(emptySideBL == 2 && countBL == 4){
								num = 1;
							}else{
								num = countBL;
							}
	
							if(symbol[k] == 'O'){
								rival[num]++;
							}else{
								me[num]++;
							}
						}
	
						countBL = 0;
						emptySideBL = 0;
					}
					i = i - 1;
					}
				}
			}
		}

		return finalScore(me,rival);
	}

	static int finalScore(int[] me, int[] rival){
		int score = 0;

/*debug*//*   System.out.print("me: "); 
			for(int i = 0; i < me.length; i++){
				System.out.print(me[i] + " ");
			}
			System.out.println("");
			System.out.print("rival: ");
			for(int j = 0; j < rival.length; j++){
				System.out.print(rival[j] + " ");
			}
*/

		//add the score of continuous two
		if(me[2] == 1){
			score = score + 200;
		}else if(me[2] == 2){
			score = score + 400;
		}else if(me[2] == 3){
			score = score + 600;
		}else if(me[2] >= 4){
			score = score + 800;
		}
		//add the score of continuous three
		if(me[3] == 1){
			score = score + 1000;
		}else if(me[3] >= 2){
			score = score + 2000;
		}
		//add the score of continuous three that's empty on both sides
		if(me[0] == 1){
			score = score + 20000;
		}else if(me[0] >= 2){
			score = score + 30000;
		}
		//add the score of continuous four
		if(me[4] == 1){
			score = score + 30000;
		}else if(me[4] >= 2){
			score = score + 60000;
		}
		//add the score of continuous four that's empty on both sides
		if(me[1] == 1){
			score = score + 1000000;
		}else if(me[1] >= 2){
			score = score + 1200000;
		}
		//add the score of fives
		if(me[5] != 0 || me[6] != 0 || me[7] != 0){
			score = 99999999;
		}

/*debug*///	System.out.println("me: " + score);

	//add the score of continuous two
		if(rival[2] == 1){
			score = score - 210;
		}else if(rival[2] == 2){
			score = score - 410;
		}else if(rival[2] == 3){
			score = score - 610;
		}else if(rival[2] >= 4){
			score= score - 810;
		}
		//add the score of continuous three
		if(rival[3] == 1){
			score = score - 1010;
		}else if(rival[3] >= 2){
			score = score - 2010;
		}
		//add the score of continuous three that's empty on both sides
		if(rival[0] == 1){
			score = score - 20000;
		}else if(rival[0] >= 2){
			score = score - 35000;
		}
		//add the score of continuous four
		if(rival[4] == 1){
			score = score - 30000;
		}else if(rival[4] >= 2){
			score = score - 50000;
		}
		//add the score of continuous four that's empty on both sides
		if(rival[1] == 1){
			score = score - 900000;
		}else if(rival[1] >= 2){
			score = score - 110000;
		}
		//add the score of fives
		if(rival[5] != 0 || rival[6] != 0 || rival[7] != 0 ){
			score = -99999999;
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
					 {'C','-','O','O','-','-','-','-'},
					 {'D','-','-','O','-','-','-','-'},
					 {'E','-','-','-','O','-','-','-'},
					 {'F','-','-','-','-','-','-','-'},
					 {'G','-','-','-','-','-','-','-'}};


	char[][] testFiveM = {{' ','1','2','3','4','5','6'},
					 {'A','-','-','-','-','-','-'},
					 {'B','-','-','-','-','-','-'},
					 {'C','-','-','X','X','X','X'},
					 {'D','-','-','-','-','-','-'},
					 {'E','-','-','-','-','-','-'},
					 {'F','-','-','-','-','-','-'}};


   char[][] testFourR = {{' ','1','2','3','4','5','6','7','8','9',':',';'},
                        {'A','-','-','-','-','-','-','-','-','-','-','-'},
                        {'B','-','-','-','-','-','-','-','-','-','-','-'},
                        {'C','-','-','-','-','-','-','-','-','-','-','-'},
                        {'D','-','-','-','-','-','-','-','-','-','-','-'},
                        {'E','-','-','-','-','-','O','-','-','-','-','-'},
					    {'F','-','-','-','X','-','-','O','-','-','-','-'},
						{'G','-','-','-','-','X','-','-','O','-','-','-'},
						{'H','-','-','-','-','-','X','-','-','-','-','-'},
						{'I','-','-','-','-','-','-','-','-','-','-','-'},
						{'J','-','-','-','-','-','-','-','-','-','-','-'},
						{'K','-','-','-','-','-','-','-','-','-','-','-'}};

}
