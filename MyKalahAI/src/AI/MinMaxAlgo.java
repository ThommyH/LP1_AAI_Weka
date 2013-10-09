package AI;

import java.util.ArrayList;

/**
 * This class implements the Min-Max-Search-Algorithm
 * @author julia
 *
 */
public class MinMaxAlgo {
	public int startdeep;
	public int storedMove;
	
	/**
	 * Constructor
	 * @param startdeep
	 */
	public MinMaxAlgo() {
	}
	
	public int startMinMax(int deep, Board board) {
		this.startdeep = deep;
		return this.minmax(deep, board);
	}
	
	
	/**
	 * Implementation of the algorithm
	 * 
	 * @param deep		current deep of the tree
	 * @param board		representation of the current gamepostion
	 * @param alpha		best value for max
	 * @param beta		best value for min
	 * @return
	 */
	private int minmax(int deep, Board board) {
		//System.out.println("start algo!");
		if (deep == 0 || board.willAnyWin() == true) {
			//System.out.println("deep = " + deep + " and board.winner = " + board.winner());
			return board.evaluate();
		}
		int maxValue = Integer.MIN_VALUE; 
		ArrayList<Integer> moves = board.possibleMoves();
		java.util.Iterator<Integer> iter = moves.iterator();
	
		while (iter.hasNext()) {
			Board board_new = new Board(board);
			int move = iter.next();
			//System.out.println("next move is " + move);
			//System.out.println("player is " + board.getPlayer());
			board_new.performMove(move);
			int value = minmax(deep-1, board_new);
			//System.out.println("value is " + value + " and maxValue is " + maxValue);
			if (value > maxValue) {
				maxValue = value;
				if (deep == startdeep) {
					this.storedMove = move;
				}
			}
			
		}
		return maxValue;
	}

	public void printMove() {
		System.out.println("Best next Move is: " + this.storedMove);
	}
	

}
