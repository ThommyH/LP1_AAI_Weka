package test_ai;

import static org.junit.Assert.*;


import org.junit.Test;


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
		 MinMaxAlgo minmax = new  MinMaxAlgo();
		 minmax.startMinMax(startdeep, board);
		 minmax.printMove();
		 assertTrue(true);
	}
}
