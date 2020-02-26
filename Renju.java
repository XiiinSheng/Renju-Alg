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
	static String displayA = "  -> Player 1                 Player 2    ";
	static String displayB = "     Player 1                 Player 2 <- ";
	static char a = 'O';           // player 1
	static char b = 'X';           // player 2
	static int SIZE = 0;       	// size of board: 1-9
	static int WINCOND = 0;        // number of pieces to win: 1-9
	static int MODE = 0;           // 1 is single player mode, 2 is multiplayer mode
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		char win = '\0';
		char round = '\0';
		Renju obj = new Renju();

		//prompt the player to choose mode
		while(Renju.MODE != 1 && Renju.MODE != 2){
			System.out.println("Choose mode:\n1: single player mode\n2: multiplayer mode");
			Renju.MODE = in.nextInt();
			in.nextLine();
		}

		while(Renju.SIZE < 1 || Renju.SIZE > 9){
			System.out.println("Choose the size(length of size) of board: 1-9");
			Renju.SIZE = in.nextInt();
			in.nextLine();
		}

		while(Renju.WINCOND < 1 || Renju.WINCOND > 9){
			System.out.println("Choose the condition(number of pieces) to win: 1-9");
			Renju.WINCOND = in.nextInt();
			in.nextLine();
		}

		// create, initialize, and print the board
		char board[][] = new char[Renju.SIZE+1][Renju.SIZE+1];
		boardInit(board);
		round = roundInit();

		printBoard(board,round);


	}

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

	static void printBoard(char[][] board, char round){
		for(int i = 0; i < 3; i++){
			System.out.println("");
		}
		printRound(round);
		System.out.println("");
		for(int i = 0; i < board.length; i++){
			System.out.print("           ");
			for(int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println("");
		}
	}

	static char roundInit(){
		Random rand = new Random();
		int i = rand.nextInt(1);
		if(i == 0){
			return 'A';
		}else{
			return 'B';
		}
	}

	static void printRound(char round){
		System.out.println("");
		if(round == 'A'){
			System.out.println(Renju.displayA);
		}else if(round == 'B'){
			System.out.println(Renju.displayB);
		}
		System.out.println("");
	}
		
}

