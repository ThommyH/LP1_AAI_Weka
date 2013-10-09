package test_ai;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import AI.Board;

public class TestBoard {

	@Test
	public void testCloning() {
		Board board = new Board();
		byte[] boardArray = {1,0,0,0,0,0,0,
							 0,0,0,0,0,0,0};
		board.setField(boardArray);
		board.setPlayer(2);
		Board clonedBoard = new Board(board);
		assertTrue(board != clonedBoard);
		assertTrue(clonedBoard.getField() != board.getField());
		assertTrue(clonedBoard.getField()[0] == 1);
	}
	
	@Test
	public void stringToBoard(){
		String boardString = "0;0;0;8;8;8;8;2;7;7;6;6;6;6;2";
		Board board = new Board(boardString);
		byte[] boardArray = {0,0,8,8,8,8,2,
							7,7,6,6,6,6,0};
		assertTrue(Arrays.equals(board.getField(), boardArray));
		assertTrue(board.getPlayer() == 2);
		
		String boardString2 = "14;1;2;3;4;5;6;7;8;9;10;11;12;13;1";
		Board board2 = new Board(boardString2);
		byte[] boardArray2 = {1,2,3,4,5,6,7,
							8,9,10,11,12,13,14};
		assertTrue(Arrays.equals(board2.getField(), boardArray2));
		assertTrue(board2.getPlayer() == 1);
	}
	
	@Test
	public void testPossibleMoves(){
		Board board = new Board();
		byte[] boardArray = {2,0,3,0,1,0,10,
							 0,1,0,1,3,1,3};
		board.setField(boardArray);
		board.setPlayer(1);
		ArrayList<Integer> possibleMovesP1 = new ArrayList<Integer>(){{
			add(0);
			add(2);
			add(4);
		}};
		ArrayList<Integer> possibleMovesP2 = new ArrayList<Integer>(){{
			add(8);
			add(10);
			add(11);
			add(12);
		}};
		assertTrue(board.possibleMoves().equals(possibleMovesP1));
		board.setPlayer(2);
		assertTrue(board.possibleMoves().equals(possibleMovesP2));
	}
	
	@Test
	public void testPutAllBeansOfAmboosToHouses(){
		Board board = new Board();
		// 1st board test
		byte[] boardArray = {2,3,3,1,1,4,10,
							 0,0,0,0,0,0,3};
		board.setField(boardArray);
		board.putAllBeansOfAmboosToHouses();
		byte[] swiftedBeans =  {0,0,0,0,0,0,24,
				 				0,0,0,0,0,0,3};
		assertTrue(Arrays.equals(board.getField(), swiftedBeans));
		// 2nd board test
		byte[] boardArray2 = {0,0,0,0,0,0,10,
				 	 		1,3,11,3,1,6,3};
		board.setField(boardArray2);
		board.putAllBeansOfAmboosToHouses();
		byte[] swiftedBeans2 =  {0,0,0,0,0,0,10,
								0,0,0,0,0,0,28};
		assertTrue(Arrays.equals(board.getField(), swiftedBeans2));
	}
	
	@Test
	public void testPerformMovementTestgame(){
		Board board = new Board();
		byte[] boardArray = {2,0,3,0,1,0,10,
							 0,1,0,1,3,1,3};
		board.setField(boardArray);
		board.setPlayer(1);
		// 1st move (move: 0)
		board.performMove(0);
		byte[] boardAfter1Move = {0,1,4,0,1,0,10,
				 				  0,1,0,1,3,1,3};
		assertTrue(Arrays.equals(board.getField(), boardAfter1Move));
		// 2nd move (move: 8) -> reaches field 9
		board.performMove(8);
		byte[] boardAfter2Move = {0,1,4,0,1,0,10,
				  				  0,0,1,1,3,1,3};
		assertTrue(Arrays.equals(board.getField(), boardAfter2Move));
		// 3rd move (move: 1) -> reaches field 2)
		board.performMove(1);
		byte[] boardAfter3Move = {0,0,5,0,1,0,10,
								  0,0,1,1,3,1,3};
		assertTrue(Arrays.equals(board.getField(), boardAfter3Move));
		// 4th move (move: 12) -> goes into house and gain another move
		board.performMove(12);
		byte[] boardAfter4Move = {0,0,5,0,1,0,10,
								  0,0,1,1,3,0,4};
		assertTrue(Arrays.equals(board.getField(), boardAfter4Move));
		assertTrue(board.getPlayer() == 2);
		// 5th move (move 11) -> goes into house and continue at field 0
		board.performMove(11);
		byte[] boardAfter5Move = {1,0,5,0,1,0,10,
								  0,0,1,1,0,1,5};
		assertTrue(Arrays.equals(board.getField(), boardAfter5Move));
		// 6th move (move 2), goes into house and continue at field 7
		board.performMove(2);
		byte[] boardAfter6Move = {1,0,0,1,2,1,11,
								  1,0,1,1,0,1,5};
		assertTrue(Arrays.equals(board.getField(), boardAfter6Move));
		// 7th move (move: 7), steals beans from field 4
		board.performMove(7);
		byte[] boardAfter7Move = {1,0,0,1,0,1,11,
								  0,0,1,1,0,1,8};
		assertTrue(Arrays.equals(board.getField(), boardAfter7Move));
		// 8th move (move: 3) want to steal from field 8 but there are no beans, therefore the bean stays
		board.performMove(3);
		byte[] boardAfter8Move = {1,0,0,0,1,1,11,
								  0,0,1,1,0,1,8};
		assertTrue(Arrays.equals(board.getField(), boardAfter8Move));
		// 9th move (move: 12) reach the house -> new move
		board.performMove(12);
		byte[] boardAfter9Move = {1,0,0,0,1,1,11,
								  0,0,1,1,0,0,9};
		assertTrue(Arrays.equals(board.getField(), boardAfter9Move));
		assertTrue(board.getPlayer()==2);
		// 10th move (move: 10) steal from field 2, but no beans there -> player 2 bean stays in the amboo
		board.performMove(10);
		byte[] boardAfter10Move = {1,0,0,0,1,1,11,
								  0,0,1,0,1,0,9};
		assertTrue(Arrays.equals(board.getField(), boardAfter10Move));
		// 11th move (move: 0)
		board.performMove(0); // steal field 11
		byte[] boardAfter11Move = {0,0,0,0,1,1,13,
								  0,0,1,0,0,0,9};
		assertTrue(Arrays.equals(board.getField(), boardAfter11Move));
		// 12th move (move: 9)
		board.performMove(9); // nothing happens
		byte[] boardAfter12Move = {0,0,0,0,1,1,13,
				  				  0,0,0,1,0,0,9};
		assertTrue(Arrays.equals(board.getField(), boardAfter12Move));
		// 13th move (move: 5)
		board.performMove(5); // go in house, is again
		byte[] boardAfter13Move = {0,0,0,0,1,0,14,
				  				  0,0,0,1,0,0,9};
		assertTrue(Arrays.equals(board.getField(), boardAfter13Move));
		assertTrue(board.getPlayer() == 1);
		// 14th move (move: 4)
		board.performMove(4); // nothing
		byte[] boardAfter14Move = {0,0,0,0,0,1,14,
				  				  0,0,0,1,0,0,9};
		assertTrue(Arrays.equals(board.getField(), boardAfter14Move));
		// nothing move
		board.performMove(10);
		// final move (move: 10)
		board.performMove(5);
		byte[] boardAfter15Move = {0,0,0,0,0,0,15,
				  					0,0,0,0,0,0,10};
		assertTrue(Arrays.equals(board.getField(), boardAfter15Move));
	}
	
	@Test
	public void testPerfomMovementHoleBoard(){
		Board board = new Board();
		byte[] boardArray = {20,0,0,0,0,0,0,
							 0,0,0,0,0,0,0};
		board.setField(boardArray);
		board.setPlayer(1);
		// move: 0 surround the board one time and last bean were put in amboo no 0
		board.performMove(0);
		byte[] boardAfter1Move = {1,2,2,2,2,2,2,
				 				  2,1,1,1,1,1,0};
		assertTrue(Arrays.equals(board.getField(), boardAfter1Move));
		assertTrue(board.getPlayer() == 2);
		// second test
		byte[] boardArray2 = {0,0,0,0,0,0,0,
							13,0,0,0,0,0,0};
		board.setField(boardArray2);
		board.setPlayer(2);
		// surround board and will steal one bean from the opponent because the move ends on the beginning field
		board.performMove(7);
		byte[] boardAfter3Move = {1,1,1,1,1,0,0,
				 				  0,1,1,1,1,1,3};
		assertTrue(Arrays.equals(board.getField(), boardAfter3Move));
		assertTrue(board.getPlayer() == 1);
	}
	
	@Test
	public void testWinner(){
		Board board = new Board();
		byte[] boardArray = {20,0,0,0,0,0,20,
							 0,20,0,0,0,0,12};
		board.setField(boardArray);
		assertTrue(board.winner() == -1); // game not over
		
		byte[] boardArray2 = {0,0,0,0,0,0,20,
							  0,20,0,0,0,0,32};
		board.setField(boardArray2);
		assertTrue(board.winner() == -1); // game not over
		
		byte[] boardArray3 = {0,0,0,0,0,0,60,
							  0,0,0,0,0,0,12};
		board.setField(boardArray3);
		assertTrue(board.winner() == 1); // player 1 wins
		
		byte[] boardArray4 = {0,0,0,0,0,0,20,
				 			 0,0,0,0,0,0,52};
		board.setField(boardArray4);
		assertTrue(board.winner() == 2); // player 2 wins
		
		byte[] boardArray5 = {0,0,0,0,0,0,36,
				 			  0,0,0,0,0,0,36};
		board.setField(boardArray5);
		assertTrue(board.winner() == 0); // game is draw
	}
}
