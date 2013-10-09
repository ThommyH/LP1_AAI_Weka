package AI;

import java.util.ArrayList;

/**
 * This class implements the Min-Max-Search-Algorithmus 
 * with Alpha-Beta-Prunning
 * @author julia
 *
 */
public class AlphaBetaAlgo {

	// how deep the search tree should be
	public int startdeep;
	public int storedMove;
	
	/**
	 * Constructor
	 * @param startdeep
	 */
	public AlphaBetaAlgo(int startdeep) {
		this.startdeep = startdeep;
	}
	
	public int startMinMax(Board board) {
		return this.minmax(startdeep, board, Integer.MAX_VALUE, Integer.MIN_VALUE);
	}

	/**
	 * Implementation of the algorithm
	 * 
	 * @param deep		current deep of the tree
	 * @param board		representation of the current gameposition
	 * @param alpha		best value for max
	 * @param beta		best value for min
	 * @return
	 */
	private int minmax(int deep, Board board, int alpha, int beta) {
		if (deep == 0 || board.willAnyWin() == true) 
			return board.evaluate();
		int maxValue = alpha; 
		
		ArrayList<Integer> moves = board.possibleMoves();
		java.util.Iterator<Integer> iter = moves.iterator();
		
		while (iter.hasNext()) {
			Board board_new = new Board(board);
			int move = iter.next();
			board_new.performMove(move);
			// if ich wieder dran: int value = minmax(deep, board_new, alpha, beta)
			int value = minmax(deep-1, board_new, -beta, -maxValue);
			if (value > maxValue) {
				maxValue = value;
				if (maxValue >= beta)
					break;
				if (deep == startdeep) 
					storedMove = move;
			}
			
		}
		return maxValue;
	}
	
	public String getMoveMappedOnServer() {
		System.err.println("premapped: "+storedMove);
		return (storedMove < 6)? new String(""+(storedMove + 1)) : new String(""+ (storedMove - 6)); 
	}

}
