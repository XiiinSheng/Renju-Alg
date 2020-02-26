/************************
* Renju game
* Description: two-player board game that one player wins when forms certain number of pieces in a line
* 			   winning condition: one 
*               

*************************/

import java.io.*;
import java.util.*;

public class Renju{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int SIZE = 0;       	// size of board: 1-9
		int WINCOND = 0;        // number of pieces to win: 1-9
		int MODE = 0;           // 1 is single player mode, 2 is multiplayer mode
		char a = 'O';           // player 1
		char b = 'X';           // player 2
		char win = null;
		String displayA = "    -> Player 1                    Player 2    ";
		String displayB = "       Player 1                    Player 2 <- ";
		char round = "";

		//prompt the player to choose mode
		while(MODE != 1 && MODE != 2){
			System.out.println("Choose mode:\n1: single player mode\n2: multiplayer mode");
			MODE = in.nextInt();
			in.nextLine();
		}

		while(SIZE < 1 || SIZE > 9){
			System.out.println("Choose the size(length of size) of board: 1-9");
			SIZE = in.nextInt();
			in.nextLine();
		}

		while(WINCOND < 1 || WINCOND > 9){
			System.out.println("Choose the condition(number of pieces) to win: 1-9");
			WINCOND = in.nextInt();
			in.nextLine();
		}

		char board[][] = new char[SIZE+1][SIZE+1];
		boardInit(board);
		round = roundInit();

		printBoard(board);


	}

	static void boardInit(char[][] board){
		int row = 1;
		int col = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(j == 0){
					board[i][j] = (char)(col+'0');
					col++;
				}else if(i == 0){
					board[i][j] = (char)(row+'0');
					row++;
				}else{
					board[i][j] = '-';
				}
			}
		}
		board[0][0] = ' ';
	}

	static void printBoard(char[][] board){
		System.out.println("");
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println("");
		}
	}

	static int roundInit(){
		int i = rand.nextInt(1);
		if(i == 0){
			return 'A';
		}else{
			return 'B';
		}
	}
		
}

