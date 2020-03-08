/************************
* 		Renju game
* Description: two-player board game that one player wins when forms certain number of pieces in a line
* 			   winning condition: one player gets 5 pieces(default) in a row/col/diagonal
* Mode: 	   Single player: one user compete with the computer
 		   	   Multiplayer: two users compete with each other
* Board Size:  configured, choose from 5 to 16
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
	static int SIZE = 0;       	// size of board: WINCOND-16
	static int WINCOND = 5;        // number of pieces to win: 1-9 (by default 5)
	static int MODE = 0;           // 1 is single player mode, 2 is multiplayer mode
	static char turn = '\0';
	static MyAlg2 comp = new MyAlg2();
	static boolean playAgain = true;
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
//		char win = '\0';
		Renju obj = new Renju();
		
		while(Renju.playAgain == true){
		// ask, create, initialize, and print the board
		char win = '\0';
		gameInit(in);
/**/	char board[][] = new char[Renju.SIZE+1][Renju.SIZE+1];
		boardInit(board);
		Renju.turn = turnInit();
		printBoard(board);
		System.out.print("   Drop:   ");

		while(win == '\0'){
			oneDrop(in,board);
			printBoard(board);
			System.out.print("   Drop:   ");
			win = checkWin(win,board);
		}
		printWinner(win);

		askWhetherPlayAgain(in);
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

		while(Renju.SIZE < Renju.WINCOND || Renju.SIZE > 16){
			System.out.println("Choose the size(length of size) of board: " + Renju.WINCOND + "-16");
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
		for(int i = 0; i < 1; i++){
			System.out.println("");
		}
		printTurn();
		System.out.println("");
		for(int i = 0; i < board.length; i++){
			System.out.print("        ");
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] > '9' && board[i][j] <= '@'){
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
		String input = "";
		int row = -1;
		int col = -1;
		while(input.length() <= 1 || input.length() > 3
				|| row < 0 || row >= board.length
				|| col < 0 || col >= board[1].length
				|| board[row][col] != '-'){
			if(Renju.MODE == 1 && Renju.turn == 'B'){
				//copy the board to a new array boardIn[][]
				char[][] boardIn = new char[board.length][board.length];
				for(int i = 0; i < board.length; i++){
					for(int j = 0; j < board.length; j++){
						boardIn[i][j] = board[i][j];
					}
				}

				input = Renju.comp.autoDrop(boardIn);
				System.out.println("Computer input: " + input);
			}else{
				input = in.nextLine();
				System.out.println("Your input: " + input);
			}
			if(input.length() <= 1 || input.equals(null)){
				continue;
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

	//check if anyone wins
	static char checkWin(char win, char[][] board){
		char symbol[] = {Renju.o, Renju.x};
		//first check O then check X
		for(int k = 0; k < symbol.length; k++){
			//check horizontally and vertically
			for(int i = 1; i < board.length; i++){
				int countH = 0;
				int countV = 0;
				for(int j = 1; j < board.length; j++){
					if(board[i][j] == symbol[k]){
						countH++;
					}else{
						countH = 0;
					}
					if(board[j][i] == symbol[k]){
						countV++;
					}else{
						countV = 0;
					}
					if(countH >= Renju.WINCOND || countV >= Renju.WINCOND){
						return symbol[k];
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
					if(board[1+j][i+j] == symbol[k]){
						countUR++;
					}else{
						countUR = 0;
					}
					//from up right to bottom left(upper triangle)
					if(board[1+j][board.length-i-j] == symbol[k]){
						countUL++;
					}else{
						countUL = 0;
					}
					//from bottom left to up right(lower triangle)
					if(board[board.length-1-j][i+j] == symbol[k]){
						countBR++;
					}else{
						countBR = 0;
					}
					//from bottom right to up left(lower triangle)
					if(board[board.length-1-j][board.length-i-j] == symbol[k]){
						countBL++;
					}else{
						countBL = 0;
					}
					if(countUR >= Renju.WINCOND 
						|| countUL >= Renju.WINCOND
						|| countBR >= Renju.WINCOND
						|| countBL >= Renju.WINCOND){
						return symbol[k];
					}
				}
			}


		}

		//check if the board is full
		//if yes, return 'D' (draw)
		int emptyCount = 0;
		for(int i = 1; i < board.length; i++){
			for(int j = 1; j < board.length; j++){
				if(board[i][j] == '-'){
					emptyCount++;
				}
			}
		}
		if(emptyCount == 0){
			return 'D';
		}			
		
		return win;
	}

	static void printWinner(char win){
		if(win == 'D'){
			System.out.println("It's a draw! Good Game!");
		}else if(Renju.MODE == 1 && win == 'X'){
			System.out.println("The computer wins. Good luck next time!");
		}else{
			System.out.println("Player " + win + " wins! Congratulations!");
		}
	}

	static void askWhetherPlayAgain(Scanner in){
		String input = "";
		while(true){
			System.out.println("Play again? Y/N");
			input = in.nextLine().toLowerCase();
			if(input.equals("yes") || input.equals("y")){
			   	Renju.playAgain = true;
				return;
			}else if(input.equals("no") || input.equals("n")){
				Renju.playAgain = false;
				return;
			}
		}
	}
}

