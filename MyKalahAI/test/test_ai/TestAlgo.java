package test_ai;

import static org.junit.Assert.*;

import java.util.ArrayList;


import org.junit.Test;


import AI.AlphaBetaAlgo;
import AI.Board;
import AI.EvaluationType;
import AI.NoTimeLeftError;

public class TestAlgo {

	@Test
	public void testAlphaBeta() throws NoTimeLeftError {
		System.out.println("testAlphaBeta started");
		Board board = new Board();
		byte[] boardArray = {0,6,5,6,6,1,0,
							 4,6,4,7,6,6,0};
		board.setField(boardArray);
		board.setPlayer(1);
		
		int startdeep = 3;
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(startdeep, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		assertTrue(true);
	}
	
	@Test
	public void debugTestcase() throws NoTimeLeftError{
		System.out.println("debugTestcase started");
		// ich: 0,1 ai: 7 ich: 0 ai: 0 --> error
		Board board = new Board();
		byte[] boardArray = {0,0,8,8,8,8,10,
							 0,8,7,7,0,7,1};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		assertTrue(alphaBeta.storedMove != -1);
	}
	
	@Test
	public void debugTestcase2() throws NoTimeLeftError{
		System.out.println("debugTestcase2 started");
		// too long history
		Board board = new Board();
		byte[] boardArray = {0,0,4,0,1,0,50,
							 0,0,0,0,3,1,13};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		//System.out.println(alphaBeta.storedMove);
		assertTrue(alphaBeta.storedMove != -1);
	}
	
	@Test
	public void debugTestcase3() throws NoTimeLeftError{
		System.out.println("debugTestcase3 started");
		// last move
		Board board = new Board();
		byte[] boardArray = {0,0,0,0,1,0,40,
							 0,1,0,0,0,0,30};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		//System.out.println(alphaBeta.storedMove);
		assertTrue(alphaBeta.storedMove != -1);
	}
	
	@Test
	public void testPresorting(){
		System.out.println("testPresorting started");
		// just for trying out, not a real test
		Board board = new Board();
		byte[] boardArray = {1,3,0,1,2,1,11,
				  			1,0,1,1,0,1,5};
		board.setField(boardArray);
		board.setPlayer(2);
		ArrayList<Integer> moves = board.possibleMovesPresorted();
		//System.out.println(moves);
	}
	
	@Test
	public void willAnyWinTest() throws NoTimeLeftError{
		System.out.println("willAnyWinTest started");
		// just for trying out, not a real test
		Board board = new Board();
		byte[] boardArray = {1,3,0,1,2,1,40,
				  			1,0,1,1,0,1,5};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		assertTrue(alphaBeta.storedMove != -1);
	}

	
	@Test
	public void testTime() throws InterruptedException{
		System.out.println("testTime started");
		long start = System.nanoTime();
		System.out.println();
		Thread.currentThread().sleep(3000);
		System.out.println(System.nanoTime() - start);
	}

	@Test
	public void willAnyWinTest1() throws NoTimeLeftError{
		System.out.println("willAnyWinTest1 started");
		// just for trying out, not a real test
		Board board = new Board();
		byte[] boardArray = {1,0,7,0,5,0,19,
				  			3,14,0,0,1,13,9};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		assertTrue(alphaBeta.storedMove != -1);
	}
	
	@Test
	public void testPossibleMovesPresorted() throws NoTimeLeftError{
		System.out.println("testPossibleMovesPresorted started");
		// just for trying out, not a real test
		Board board = new Board();
		byte[] boardArray = {1,0,7,0,5,0,19,
				  			2,2,3,0,1,12,9};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6, EvaluationType.WILLWIN_HOUSECOMPARE);
		alphaBeta.startMinMax(board);
		assertTrue(alphaBeta.storedMove != -1);
	}
}
