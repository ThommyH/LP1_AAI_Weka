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
		return this.minmax(startdeep, board, Integer.MIN_VALUE + 10, Integer.MAX_VALUE - 10);
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
		// abbruch board.willAnyWin() == true
		ArrayList<Integer> moves = board.possibleMovesPresorted();
		if (deep == 0 || moves.size() == 0){ 
			int eval = board.evaluate();
			return eval;
		}
		int maxValue = alpha; 
		for (int move : moves){
			Board board_new = new Board(board);
			boolean hasAnotherMove = board_new.performMove(move);
			int value = 0;
			// if we can do another move we will try to move it right away
			if (hasAnotherMove){
				value = minmax(deep-1, board_new, maxValue, beta);
			} else {
				value = -minmax(deep-1, board_new, -beta, -maxValue);
			}
			if (deep == getStartdeep()){
				System.err.println(move +" -->" + value);
			}
			if (value > maxValue) {
				maxValue = value;
				if (maxValue < beta){
					if (deep == startdeep){ 
						storedMove = move;
					}
				}
			}
			
		}
		return maxValue;
	}
	
	public String getMoveMappedOnServer() {
		System.err.println("premapped: "+storedMove);
		return (storedMove < 6)? new String(""+(storedMove + 1)) : new String(""+ (storedMove - 6)); 
	}
	
	public int getStartdeep() {
		return startdeep;
	}

}
