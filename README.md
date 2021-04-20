# Renju
Terminal-based Renju(Gomoku) game software and its Algorithm program written in Java.  

Renju is a board game in which two players take turns to place stones on board in order to have five stones in a row, either horizontal, vertical or diagonal. 

## Renju.java
Renju.java is the main file for the game software, display, judge, and interface. It uses 2D char array as board, and has single or double player option. Player can customize the size of board. 
Renju.java is originally using MyAlg2.java as 'computer' Algorithm. Changing the global variable comp from MyAlg2() to MyAlg3() will change the algorithm. 

## MyAlg.java (deleted)
MyAlg.java(deleted) is v1 of Renju algorithm. It randomly chooses an empty spot to place the stone. 

## MyAlg2.java
MyAlg2.java is v2 of Renju AI algorithm. It uses depth-2 alpha-beta algorithm and manual(magical) parameters for score calculation. 

## MyAlg3.java
MyAlg3.java is v3 of Renju AI algorithm. It uses depth-3 alpha-beta algorithm and incomplete manual(magical) parameters for score calculation. It is incomplete because depth-3 15x15 board is slow in operation time. 

## Run Program
To run the Renju program, compile ```Renju.java``` then run ```Renju.class```. 
