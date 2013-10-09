package test_ai;

import static org.junit.Assert.*;


import org.junit.Test;


import AI.AlphaBetaAlgo;
import AI.Board;
import AI.MinMaxAlgo;

public class TestAlgo {

	@Test
	public void testMinMax() {
		Board board = new Board();
		byte[] boardArray = {0,6,5,4,0,1,0,
							 4,6,4,7,2,1,0};
		board.setField(boardArray);
		board.setPlayer(1);
		
		int startdeep = 3;
		 MinMaxAlgo minmax = new  MinMaxAlgo();
		 minmax.startMinMax(startdeep, board);
		 minmax.printMove();
		 assertTrue(true);
	}
	
	@Test
	public void testAlphaBeta() {
		Board board = new Board();
		byte[] boardArray = {0,6,5,6,6,1,0,
							 4,6,4,7,6,6,0};
		board.setField(boardArray);
		board.setPlayer(1);
		
		int startdeep = 3;
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(startdeep);
		alphaBeta.startMinMax(board);
		assertTrue(true);
	}
	
	@Test
	public void debugTestcase(){
		// ich: 0,1 ai: 7 ich: 0 ai: 0 --> error
		Board board = new Board();
		byte[] boardArray = {0,0,8,8,8,8,10,
							 0,8,7,7,0,7,1};
		board.setField(boardArray);
		board.setPlayer(2);
		AlphaBetaAlgo alphaBeta = new  AlphaBetaAlgo(6);
		assertTrue(alphaBeta.storedMove != 0);
	}
}
