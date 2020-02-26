/************************
* 		Renju game
* Description: two-player board game that one player wins when forms certain number of pieces in a line
* 			   winning condition: one player gets 5 pieces(default) in a row/col/diagonal
* Mode: 	   Single player: one user compete with the computer
* 		   	   Multiplayer: two users compete with each other
* Board Size:  configured, choose from 1 to 14
* Computer:    MyAlg.class
* Author:      Xin Sheng
*               

*************************/

import java.io.*;
import java.util.*;
import java.util.Random;

public class Renju{
	static String displayA = "         --> Player O                Player X     ";  
	static String displayB = "             Player O                Player X <-- ";
	static String displayC = "         --> Player O                Computer X     ";
	static String displayD = "             Player O                Computer X <-- ";
	static char o = 'O';           // player 1
	static char x = 'X';           // player 2
	static int SIZE = 0;       	// size of board: 1-14
	static int WINCOND = 5;        // number of pieces to win: 1-9 (by default 5)
	static int MODE = 0;           // 1 is single player mode, 2 is multiplayer mode
	static char turn = '\0';
	static MyAlg comp = new MyAlg();
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		char win = '\0';
		Renju obj = new Renju();

		// ask, create, initialize, and print the board
		gameInit(in);
/**/	char board[][] = new char[Renju.SIZE+1][Renju.SIZE+1];
		boardInit(board);
		Renju.turn = turnInit();
		printBoard(board);

		while(win == '\0'){
			oneDrop(in,board);
			printBoard(board);
			win = checkWin(win,board);
		}

	}

		//prompt the player to choose mode, size, winCondition
	static void gameInit(Scanner in){
		System.out.println("Welcome to Renju Game (by Xin Sheng)");
		while(Renju.MODE != 1 && Renju.MODE != 2){
			System.out.println("Choose mode:\n1: single player mode\n2: multiplayer mode");
			String inputMode = in.nextLine();
			try{
				Renju.MODE = Integer.parseInt(inputMode);
			}catch(Exception e){}
		}

		while(Renju.SIZE < 1 || Renju.SIZE > 14){
			System.out.println("Choose the size(length of size) of board: 1-14");
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
				Renju.WINCOND = Integer.parseInt(inputCond);
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
	static void printBoard(char[][] board){
		for(int i = 0; i < 2; i++){
			System.out.println("");
		}
		printTurn();
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
		System.out.print("   Drop:  ");
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
	static void printTurn(){
		System.out.println("");
		if(Renju.turn == 'A'){
			Renju.turn = 'B';
			if(Renju.MODE == 2){
				System.out.println(Renju.displayB);
			}else{
				System.out.println(Renju.displayD);
			}
		}else if(Renju.turn == 'B'){
			Renju.turn = 'A';
			if(Renju.MODE == 2){
				System.out.println(Renju.displayA);
			}else{
				System.out.println(Renju.displayC);
			}
		}
		System.out.println("");
	}
	
	//prompt one drop from user or computer, 
	//and process it onto the board
	static void oneDrop(Scanner in, char[][] board){
		String input;
		int row = -1;
		int col = -1;
		while(row < 0 || row >= board.length
				|| col < 0 || col >= board[1].length
				|| board[row][col] != '-'){
			if(Renju.MODE == 1 && Renju.turn == 'B'){
				input = Renju.comp.autoDrop(board);
				System.out.println("Computer input: " + input);
			}else{
				input = in.nextLine();
				System.out.println("Your input: " + input);
			}
			row = (int)(input.charAt(0)-'@');
			String column = input.substring(1,input.length());
			if(column.length() == 1){
				col = (int)(column.charAt(0)-'0');
			}else if(column.length() == 2){
				col = (int)((column.charAt(0)-'0')*10)+(int)((column.charAt(1)-'0'));
			}else{
				col = -1;
			}
			System.out.println("Row: " + row + " Col: " + col);
		}

		if(Renju.turn == 'A'){
			board[row][col] = Renju.o;
		}else{
			board[row][col] = Renju.x;
		}

	}

	static char checkWin(char win, char[][] board){
		return win;
	}

}

