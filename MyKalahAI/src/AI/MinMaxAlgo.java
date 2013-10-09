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
	public MinMaxAlgo(int startdeep) {
		this.startdeep = startdeep;
		System.out.println("new MinMaxAlgo started");
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
	public int minmax(int deep, Board board) {
		System.out.println("start algo!");
		if (deep == 0 || board.winner() ==1) {
			System.out.println("deep = " + deep + " and board.winner = " + board.winner());
			return board.evaluate();
		}
		int maxValue = Integer.MIN_VALUE; 
		ArrayList<Integer> moves = board.possibleMoves();
		java.util.Iterator<Integer> iter = moves.iterator();
	
		while (iter.hasNext()) {
			Board board_new = new Board(board);
			int move = iter.next();
			System.out.println("next move is " + move);
			System.out.println("player is " + board.getPlayer());
			board_new.performMove(move);
			int value = minmax(deep-1, board_new);
			if (value > maxValue) {
				maxValue = value;
				if (deep == startdeep) {
					storedMove = move;
				}
			}
			
		}
		System.out.println("move = " + maxValue);
		return maxValue;
	}


	public String getMoveMappedOnServer() {
		return (storedMove < 6)? new String(""+(storedMove + 1)) : new String(""+ (storedMove - 6)); 
	}
	

}
