package test_ai;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;


import AI.Board;
import AI.MinMaxAlgo;

public class TestAlgo {

	@Test
	public void testMinMax() {
		Board board = new Board();
		byte[] boardArray = {0,0,0,4,0,1,0,
							 0,0,0,0,2,1,0};
		board.setField(boardArray);
		board.setPlayer(1);
		
		 MinMaxAlgo minmax = new  MinMaxAlgo(2);
		 minmax.minmax(4, board);
		 assertTrue(true);
	}
}
