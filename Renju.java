/************************
* Renju game
* Description: two-player board game that one player wins when forms certain number of pieces in a line
* 			   winning condition: one 
*               

*************************/

import java.io.*;
import java.util.*;
import java.util.Random;

public class Renju{
	static String displayA = "         --> Player O                Player X     ";   //show that it's player 1's turn
	static String displayB = "             Player O                Player X <-- ";   //show that it's player 2's turn
	static char o = 'O';           // player 1
	static char x = 'X';           // player 2
	static int SIZE = 0;       	// size of board: 1-15
	static int WINCOND = 5;        // number of pieces to win: 1-9 (by default 5)
	static int MODE = 0;           // 1 is single player mode, 2 is multiplayer mode
	static char turn = '\0';
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		char win = '\0';
		Renju obj = new Renju();

		gameInit(in);
		// create, initialize, and print the board
		char board[][] = new char[Renju.SIZE+1][Renju.SIZE+1];
		boardInit(board);
		Renju.turn = turnInit();

		printBoard(board,Renju.turn);


	}

		//prompt the player to choose mode, size, winCondition
	static void gameInit(Scanner in){
		while(Renju.MODE != 1 && Renju.MODE != 2){
			System.out.println("Choose mode:\n1: single player mode\n2: multiplayer mode");
			String inputMode = in.nextLine();
			try{
				Renju.MODE = Integer.parseInt(inputMode);
			}catch(Exception e){}
		}

		while(Renju.SIZE < 1 || Renju.SIZE > 15){
			System.out.println("Choose the size(length of size) of board: 1-15");
			String inputSize = in.nextLine();
			try{
				Renju.SIZE = Integer.parseInt(inputSize);
			}catch(Exception e){}
		}

		//set the default WINCOND to 5 pieces, so it's a useless loop
		while(Renju.WINCOND < 1 || Renju.WINCOND > 9){
			System.out.println("Choose the condition(number of pieces) to win: 1-9");
			String inputCond = in.nextLine();
			try{
				Renju.WINCOND = INteger.parseInt(inputCond);
			}catch(Exception e){}
		}
	}


	//initialize the empty board
	static void boardInit(char[][] board){
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
	}

	//print the board and role
	static void printBoard(char[][] board, char turn){
		for(int i = 0; i < 3; i++){
			System.out.println("");
		}
		printTurn(turn);
		System.out.println("");
		for(int i = 0; i < board.length; i++){
			System.out.print("        ");
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] > '9' && board[i][j] < '@'){
					System.out.print("1" + (char)(board[i][j]-10) + "  ");
				}else{
					System.out.print(board[i][j] + "   ");
				}
			}
			System.out.println("\n");
		}
	}

	//randomly initialize the first player to start
	static char turnInit(){
		Random rand = new Random();
		int i = rand.nextInt(2);
		if(i == 0){
			return 'A';
		}else{
			return 'B';
		}
	}

	//print the role of player
	static void printTurn(char turn){
		System.out.println("");
		if(turn == 'A'){
			turn = 'B';
			System.out.println(Renju.displayB);
		}else if(turn == 'B'){
			turn = 'A';
			System.out.println(Renju.displayA);
		}
		System.out.println("");
	}
		
}

