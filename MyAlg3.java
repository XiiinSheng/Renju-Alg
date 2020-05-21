/***************************************************************************
 * File Name: MyAlg3.java	
 * Copyright: Xin Sheng
 * Description: Renju AI for Renju.java. 
 *              Depth 3. (one comp's move, one rival's move, one comp's move, 
 *				calculate score)
 *              Calculates Renju best game theory using MinMax Theorem.
 ***************************************************************************/

import java.util.Random;
import java.util.*;

public class MyAlg3{
	//symbol for computer's piece and rival's piece
	static char me = 'X';
	static char rival = 'O';

	//MinMax Theorem depth
	public static final int depth = 3;

	public String getVersion(){
		return "V3";
	}

/* Original random drop method

	public String autoDrop(char[][] board){
		char random1 = (char)(rand.nextInt(board.length-1)+'A');
		int random2 = rand.nextInt(board.length-1)+1;
		String result = "";
		return random1 + (result + random2);
	}
*/

	/**
	 * The main method to make the drop to Renju.java
	 *
	 * @param initialBoard The state of board to consider.
	 *
	 * @return the position to move.
	 */
	public String autoDrop(char[][] initialBoard){
		//the array to store best move position
		int index[] = new int[2];

		int depth = MyAlg3.depth;
		int score = 0;
		int[] alphaBeta = {-999999999,999999999};
	
		//at the start of each round place pieces at center
		if(isAlmostEmpty(initialBoard)){
			//num to track whether a position is visited and full
			int i = 1;

			index[0] = initialBoard.length/2;
			index[1] = index[0];

			//if a position is occupied, go diagonally
			while(initialBoard[index[0]][index[1]] != '-'){
				index[0] = index[0]+i;
				index[1] = index[0];
			}
		}else{
			score = this.bestMove(index,initialBoard,depth,true,alphaBeta);
		}
	
		//convert index into characters to be returned
		char row = (char)(index[0]+'@');
		int col = index[1];
/*debug*/  	    System.out.println("index: " + index[0] + " " + index[1]);
		String result = "";

/*debug*///		System.out.println("score of this move = " + score);
		
		return row + (result + col);
	}

	//MinMax alg for best move
	public int bestMove(int[] index, char[][] boardIn, int depth, boolean me, int[] alphaBeta){
	    //copy down the board so that the param won't change all the time	
		char[][] boardStatic = new char[boardIn.length][boardIn.length];
		int[] thisAB = {alphaBeta[0], alphaBeta[1]};
		copy(boardStatic,boardIn);
		//stop the recursion when: reach end of depth, or board is full(draw), 
		//or one player wins
		if((depth == 0) 
			|| (isFull(boardStatic)) 
			|| (score(boardIn) >= 9999999) 
			|| (score(boardIn) <= -9999999)){
			return score(boardIn);
		}
		//computer's turn
		if(me == true){
			//set initial score bound, computer wants higher score as possible
			int value = -999999999;
			for(int i = 1; i < boardIn.length; i++){
				for(int j = 1; j < boardIn[0].length; j++){
					if(boardStatic[i][j] == '-'){
						//try each empty spot on the board
						char[][] board1 = new char[boardStatic.length][boardStatic.length];
						copy(board1,boardStatic);
						board1[i][j] = 'X';
						int[] newIndex = new int[2];
						//recursively calculate the score of the move
						int score = bestMove(newIndex, board1, depth-1, false, thisAB);
/*debug*///					System.out.println("ME: depth: " + depth + ", index = " + i + " " + j + ", score = " + score);
						//record the index of highest score 
						if(value < score){
							boardIn = board1;
							index[0] = i;
							index[1] = j;
							value = score;
							//change alpha
							thisAB[0] = Math.max(thisAB[0],score);
/*debug*///					System.out.println("Index: " + index[0] + " " + index[1]);
						}
						//if alpha > beta, no need to proceed
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
			//rival's turn
			//set initial value as max, rival wants the score to be minimum
			int value = 999999999;
			for(int i = 1; i < boardIn.length; i++){
				for(int j = 1; j < boardIn[0].length; j++){
					if(boardStatic[i][j] == '-'){
						//check each empty spot on the board
						char[][] board2 = new char[boardStatic.length][boardStatic.length];
						copy(board2,boardStatic);
						board2[i][j] = 'O';
						//recursively calculating the score of board
						int score = bestMove(index, board2, depth-1, true, thisAB);
/*debug*///					System.out.println("RIVAL: depth: " + depth + ", index = " + i + " " + j + ", score = " + score);
						//record the smallest score attempt
						if(value > score){
							boardIn = board2;
							value = score;
							thisAB[1] = Math.min(thisAB[1],score);
/*debug*///					System.out.println("Index: " + index[0] + " " + index[1]);
						}
						//if alpha > beta, no need to proceed
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

	/**
	 * Scan the board and generate array for calculating total score, and call
	 * finalScore for calculating score. 
	 *
	 * @param board the board to calculate score. 
	 * 
	 * @return int of score. 
	 */
	static int score(char[][] board){
		char symbol[] = {MyAlg3.rival, MyAlg3.me}; //symbol of pieces
		int[] me = new int[8];     //array to record connected pieces
		int[] rival = new int[8];  //array to record connected pieces

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
						//if it's my piece, add count and update emptySide
						countH++;
						if(j != 1 && 
					       board[i][j-1] == '-'){
							emptySideH++;
						}
						//if reach the end of board, add score to array
						if(j == board.length-1 && countH > 1 && (emptySideH != 0 || countH >= 5)){
							if(symbol[k] == 'O'){
								rival[countH]++;
							}else{
								me[countH]++;
							}
						}
	
/*debug*/  //				System.out.println("CountH++, now countH is " + countH);
					}else{
						//if it's not my piece, update emptySide
						if(countH != 0 &&
						   board[i][j] == '-'){
						   	emptySideH++;
						}
						//add score
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
						//if it's my piece, add count and update emptySide
						countV++;
						if(j != 1 &&
						   board[j-1][i] == '-'){
							emptySideV++;
						}
						//add score if reach end of board
						if(j == board.length-1 && countV > 1 && (emptySideV != 0 || countV >= 5)){
							if(symbol[k] == 'O'){
								rival[countV]++;
							}else{
								me[countV]++;
							}
						}
	
					}else{
						//if it's not my piece, update emptySide
						if(countV != 0 &&
						   board[j][i] == '-'){
						   	emptySideV++;
						}
						//add score
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
						//if it's my piece, add count and update emptySide
						countUR++;
						if(board[j][i+j-1] == '-'){
							emptySideUR++;
						}
						//add score
						if(i+j == board.length-1 && countUR > 1 && (emptySideUR != 0 || countUR >= 5)){
							if(symbol[k] == 'O'){
								rival[countUR]++;
							}else{
								me[countUR]++;
							}
						}
	
					}else{
						//if it's not my piece, update emptySide
						if(countUR != 0 &&
						   board[1+j][i+j] == '-'){
							emptySideUR++;
						}
						//add score
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
						//if it's my piece, add count and update emptySide
						countUL++;
						if(board.length-i-j+1 != board.length &&
					       board[j][board.length-i-j+1] == '-'){
							emptySideUL++;
						}
						//add score if reach end of board
						if(board.length-i-j == 1 && countUL > 1 && (emptySideUL != 0 || countUL >= 5)){
							if(symbol[k] == 'O'){
								rival[countUL]++;
							}else{
								me[countUL]++;
							}
						}
	

					}else{
						//if it's not my piece, update emptySide
						if(countUL != 0 &&
						   board[1+j][board.length-i-j] == '-'){
						   	emptySideUL++;
						}
						//add score
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
						//if it's my piece, add count and update emptySide
						countBR++;
						if(board.length-j != board.length &&
  					   	   board[board.length-j][i+j-1] == '-'){
							emptySideBR++;
						}
						//add score if reach end of board
						if(i+j == board.length-1 && countBR > 1 && (emptySideBR != 0 || countBR >= 5)){
							if(symbol[k] == 'O'){
								rival[countBR]++;
							}else{
								me[countBR]++;
							}
						}
	
					}else{
						//if it's not my piece, update emptySide
						if(countBR != 0 &&
						   board[board.length-j-1][i+j] == '-'){
						   	emptySideBR++;
						}
						//add score
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
						//if it's my piece, add count and update emptySide
						countBL++;
						if(board.length-j != board.length &&
					       board.length-i-j+1 != board.length &&
					       board[board.length-j][board.length-i-j+1] == '-'){
							emptySideBL++;
						}
						//add score if reach end of board
						if(board.length-i-j == 1 && countBL > 1 && (emptySideBL != 0 || countBL >= 5)){
							if(symbol[k] == 'O'){
								rival[countBL]++;
							}else{
								me[countBL]++;
							}
						}
	
					}else{
						//if it's not my piece, update emptySide
						if(countBL != 0 &&
						   board[board.length-j-1][board.length-i-j] == '-'){
						   	emptySideBL++;
						}
						//add score
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

	/**
	 * Calculate the final score from the array of counts.
	 *
	 * @param me Array of connected piece counts of computer. 
	 * @param rival Array of connected piece counts of rival. 
	 *
	 * @return int value of final score. 
	 */
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
			score = score + 30000;
		}else if(me[0] >= 2){
			score = score + 50000;
		}
		//add the score of continuous four
		if(me[4] == 1){
			score = score + 30000;
		}else if(me[4] >= 2){
			score = score + 60000;
		}
		//add the score of continuous four that's empty on both sides
		if(me[1] == 1){
			score = score + 800000;
		}else if(me[1] >= 2){
			score = score + 900000;
		}
		//add the score of fives
		if(me[5] != 0 || me[6] != 0 || me[7] != 0){
			score += 99999999;
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
			score = score - 60000;
		}else if(rival[0] >= 2){
			score = score - 80000;
		}
		//add the score of continuous four
		if(rival[4] == 1){
			score = score - 60000;
		}else if(rival[4] >= 2){
			score = score - 80000;
		}
		//add the score of continuous four that's empty on both sides
		if(rival[1] == 1){
			score = score - 1000000;
		}else if(rival[1] >= 2){
			score = score - 1200000;
		}
		//add the score of fives
		if(rival[5] != 0 || rival[6] != 0 || rival[7] != 0 ){
			score = score -99999999;
		}

/*debug*/// System.out.println("Final: " + score);
		return score;
	
	}

	/**
	 * Check whether the board is full.
	 *
	 * @param board The board to be checked. 
	 *
	 * @return true if no empty spot is on the board, false if there's any 
	 * empty spot. 
	 */
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

	/**
	 * Copy the array from a to b.
	 *
	 * @param a The array to be copied. 
	 * @param b The array to contain a copy.
	 */
	static void copy(char[][] a, char[][] b){
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b.length; j++){
				a[i][j] = b[i][j];
			}
		}
	}

	/** 
	 * Check whether a board is almost empty.
	 * 
	 * @param board The board to check. 
	 *
	 * @return true if there's only 0 or 1 piece on the board; false if there
	 * are more than 1 pieces on the board. 
	 */
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
		MyAlg3 obj = new MyAlg3();

//		System.out.println(obj.autoDrop(board));
		
		System.out.println(score(obj.test));
		System.out.println(obj.autoDrop(obj.test));
	}


   char[][] test = {{' ','1','2','3','4','5','6','7','8','9',':',';'},
                    {'A','-','-','-','-','-','-','-','-','-','-','-'},
                    {'B','-','-','-','-','-','-','-','-','-','-','-'},
                    {'C','-','-','-','-','-','-','-','-','-','-','-'},
                    {'D','-','-','-','-','-','-','-','-','-','-','-'},
                    {'E','-','-','-','O','-','-','-','-','-','-','-'},
				    {'F','-','-','-','-','O','-','-','-','-','-','-'},
	 				{'G','-','-','-','-','-','O','-','-','-','-','-'},
					{'H','-','-','-','-','-','-','O','-','-','-','-'},
					{'I','-','-','-','-','-','-','-','-','-','-','-'},
					{'J','-','-','-','-','-','-','-','-','-','-','-'},
					{'K','-','-','-','-','-','-','-','-','-','-','-'}};

}
