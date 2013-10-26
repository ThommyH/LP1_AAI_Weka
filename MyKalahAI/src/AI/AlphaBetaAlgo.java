package AI;

import java.util.ArrayList;

/**
 * This class implements the Min-Max-Search-Algorithmus 
 * with Alpha-Beta-Prunning
 *
 */
public class AlphaBetaAlgo {

	
	public int startdeep; // how deep the search tree should be in the beginning
	public int storedMove; // best move found by algorithm
	public long endTime; // deadline for computation
	public static long MAXIMAL_COMPUTATION_TIME = 3000000000l; 
	public EvaluationType evalMethod;
	
	/**
	 * Constructor
	 * @param startdeep
	 */
	public AlphaBetaAlgo(int startdeep, EvaluationType evalMethod) {
		this.startdeep = startdeep;
		this.storedMove = -1;
		this.endTime = System.nanoTime() + MAXIMAL_COMPUTATION_TIME;
		this.evalMethod = evalMethod;
	}
	
	/**
	 * runs alpha beta algorithms with an increasing depth as long as there is some more time left
	 * maxmum is 3 seconds
	 * @return
	 */
	public int startAlphaBetaInterativeDeepening(Board board){
		endTime = System.nanoTime() + MAXIMAL_COMPUTATION_TIME;
		int bestMove;
		int eval = 0;
		int oldEval = 0;
		while (timeLeft() > 0){
			bestMove = storedMove;
			oldEval = eval;
			try {
				eval = startAlphaBeta(board);
			// no time was left, reset best move from the previous interation because new algorithm did not finished
			} catch (NoTimeLeftError e) {
				eval = oldEval;
				storedMove = bestMove;
				break;
			}
			startdeep += 1;
			// dont need to calculate anymore because every move will end in a 100% win or lose
			if (eval >= 10000000 || eval <= -10000000){
				break;
			}
		}
		return eval;
	}
	
	private long timeLeft(){
		return endTime - System.nanoTime();
	}
	
	public int startAlphaBeta(Board board) throws NoTimeLeftError {
		return this.alphaBeta(startdeep, board, Integer.MIN_VALUE + 10, Integer.MAX_VALUE - 10);
	}
	
	/**
	 * Implementation of the algorithm alpha beta in the NegaMax version
	 * every player will maximize (not only the player 'max' but also player 'min')
	 * @param deep		current deep of the tree
	 * @param board		representation of the current gameposition
	 * @param alpha		best value for max
	 * @param beta		best value for min
	 * @return
	 * @throws NoTimeLeftError 
	 */
	private int alphaBeta(int deep, Board board, int alpha, int beta) throws NoTimeLeftError {
		// if time is over, interrupt
		if (timeLeft() <= 0){
			throw new NoTimeLeftError("3 seconds are over");
		}
		ArrayList<Integer> moves = board.possibleMovesPresorted();
		// recursive anchor reached or no moves left or somebody will win
		if (deep == 0 || moves.size() == 0 || board.willAnyWin()){
			// if move doesnt matter anymore because it is already won, get the first possible move (when no other moves was saved before)
			if (storedMove == -1 && board.willAnyWin() && moves.size()!=0) {
				storedMove = moves.get(0);
			}
			return board.evaluate(evalMethod);
		}
		// best found move so far has evaluation alpha
		int maxValue = alpha; 
		for (int move : moves){
			Board board_new = new Board(board);
			boolean hasAnotherMove = board_new.performMove(move);
			int value = 0;
			// if we can do another move we will try to move it right away
			if (hasAnotherMove){
				value = alphaBeta(deep-1, board_new, maxValue, beta);
			} else {
				// nega max implementation, switch and negate alpha and beta.
				value = -alphaBeta(deep-1, board_new, -beta, -maxValue);
			}
			// check if move is better than the previous best
			if (value > maxValue) {
				maxValue = value;
				// check if it is acceptable good for the opponent
				if (maxValue < beta){
					if (deep == startdeep){ 
						// found new best move
						storedMove = move;
					}
				} else {
					break; // cutoff because move was too good ;)
				}
			}
		}
		return maxValue;
	}
	
	/**
	 * 
	 * @return the server version of the calculated move
	 */
	public String getMoveMappedOnServer() {
		return (storedMove < 6)? new String(""+(storedMove + 1)) : new String(""+ (storedMove - 6)); 
	}
	
	public int getStartdeep() {
		return startdeep;
	}

}
