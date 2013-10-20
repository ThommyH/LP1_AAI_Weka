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
	public long endTime;
	public static long MAXIMAL_COMPUTATION_TIME = 3000000000l; 
	
	/**
	 * Constructor
	 * @param startdeep
	 */
	public AlphaBetaAlgo(int startdeep) {
		this.startdeep = startdeep;
		this.storedMove = -1;
		this.endTime = System.nanoTime() + MAXIMAL_COMPUTATION_TIME;
	}
	
	/**
	 * runs alpha beta algorithms with an increasing depth as long as there is some more time left
	 * maxmum is 3 seconds
	 * @return
	 */
	public int startMinMaxInterativeDeepening(Board board){
		endTime = System.nanoTime() + MAXIMAL_COMPUTATION_TIME;
		int bestMove;
		int eval = 0;
		int oldEval = 0;
		while (timeLeft() > 0){
			bestMove = storedMove;
			oldEval = eval;
			try {
				System.out.println("START ALPHA BETA WITH DEPTH " + startdeep + ". time left (ms): " + timeLeft()/1000000l);
				System.out.println("best move so far: " + storedMove);
				eval = startMinMax(board);
			// no time was left, reset best move from the previous interation because new algorithm did not finished
			} catch (NoTimeLeftError e) {
				System.out.println("break");
				eval = oldEval;
				storedMove = bestMove;
				break;
			}
			startdeep += 1;
		}
		return eval;
	}
	
	private long timeLeft(){
		return endTime - System.nanoTime();
	}
	
	public int startMinMax(Board board) throws NoTimeLeftError {
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
	 * @throws NoTimeLeftError 
	 */
	private int minmax(int deep, Board board, int alpha, int beta) throws NoTimeLeftError {
		if (timeLeft() <= 0){
			throw new NoTimeLeftError("3 seconds are over");
		}
		ArrayList<Integer> moves = board.possibleMovesPresorted();
		if (deep == 0 || moves.size() == 0 || board.willAnyWin()){
			if (storedMove == -1 && board.willAnyWin() && moves.size()!=0) {
				storedMove = moves.get(0);
			}
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
		//System.err.println("premapped: "+storedMove);
		return (storedMove < 6)? new String(""+(storedMove + 1)) : new String(""+ (storedMove - 6)); 
	}
	
	public int getStartdeep() {
		return startdeep;
	}

}
