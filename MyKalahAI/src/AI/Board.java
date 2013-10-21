package AI;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	
	private byte[] field;
	static int AMOUNTOFBEANSAMBOO = 6;
	private int player;
	public static final byte[] FIELD_PARTNER = {12,11,10,9,8,7,-1,5,4,3,2,1,0,-1};
	
	//
	//north			12	11	10	9	8	7	
	//houses  	13							6
	//south			0	1	2	3	4	5
	//
	//south = player 1, north = player 2
	
	
	public Board(){
		field = new byte[14];
		player = 1;
	}
	
	public Board(String codedBoard){
//		String h2;p1a1;p1a2;p1a3;p1a4;p1a5;p1a6;h1;p2a1;p2a2;p2a3;p2a4;p2a5;p2a6;P
//		  where
//		       p = player (1,2)
//		       a = ambo (1-6)
//		       h = house (1,2)
//		       P = The player (1 or 2) to make the next move.
//		  Example:
//		       p1a2 means the second ambo for player 1
//		       h2 means the house of player 2.
		this.field = new byte[14];
		String[] boardSplitted = codedBoard.split(";");
		for (int i = 0; i<14; i++){
			this.field[i] = (byte) Integer.parseInt(boardSplitted[(i+1)%14]);
		}
		this.player = Integer.parseInt(boardSplitted[14]); 
	}
	
	public Board(Board another){
		// copy method
		this.field = another.getField().clone();
		this.player = another.getPlayer();
	}
	
	////////////////////
	// getter and setter
	////////////////////
	
	public byte[] getField() {
		return field;
	}

	public void setField(byte[] field) {
		this.field = field;
	}

	public int getPlayer() {
		return player;
	}

	public int getOtherPlayer(){
		return (3 - player);
	}

	public void setPlayer(int i) {
		this.player = i;
	}
	
	private int getPoints(int forPlayer) {
		if (forPlayer == 1) 
			return this.field[6];
		else
			return this.field[13];
	}
	
	/**
	 * 
	 * @param fieldindex
	 * @return number of beans of the opposing field towards
	 */
	private int getNumbersOfBeansInFieldPartner(int fieldindex) {
		int mirroredField = FIELD_PARTNER[fieldindex];
		return this.field[mirroredField]; 
	}

	

	//////////
	// methods
	//////////

	/**
	 * @return the winner (1 or 2), or 0 if the game ended in a draw. -1 means the game is not finished.
	 */
	public int winner(){
		byte pointsP1 = field[6];
		byte pointsP2 = field[13];
		if (pointsP1 + pointsP2 == AMOUNTOFBEANSAMBOO*6*2){
			if (pointsP1 > pointsP2) return 1;
			else if (pointsP1 < pointsP2) return 2;
			else return 0;
		}
		return -1;
	}

	/**
	 * @return the possible moves of the current player
	 */
	public ArrayList<Integer> possibleMoves(){
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		int i = ((player == 1)? 0 : 7);
		int max = i+6;
		for (; i < max; i++){
			if (field[i] != 0) possibleMoves.add(i);
		}
		return possibleMoves;
	}
	
	/**
	 * 
	 * @return the possible presorted moves of the current player 
	 */
	public ArrayList<Integer> possibleMovesPresorted(){
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		ArrayList<Integer> possibleMoves_best = new ArrayList<Integer>();
		int minField = (player == 1)? 0 : 7;
		int maxField = minField+5;
		byte[] field_clone = getField().clone();
		for (int p = maxField; p >= minField; p--) {
			//System.out.println("p ->" + p);
			if (field_clone[p] == 0 && getNumbersOfBeansInFieldPartner(p) != 0) {
				//System.out.println("field partner for:" + p + "->" + getNumbersOfBeansInFieldPartner(p));
				for (int t = p-1; t >= minField; t--) {
					if (field_clone[t] == p - t) {
						possibleMoves_best.add(0, t);
						field_clone[t] = 0;
					}
				}
			}
		}
		for (int i = minField; i <= maxField; i++){
			if (field_clone[i] != 0) {
				if (field_clone[i] == maxField+1-i) 
					possibleMoves.add(0, i);
				else
					possibleMoves.add(i);
			}
		}
		possibleMoves.addAll(0, possibleMoves_best);
		//System.out.println(Arrays.toString(field));
		//System.out.println(possibleMoves);
		//System.out.println();
		return possibleMoves;
	}
	
	/**
	 * actual movement of beans
	 * places all beans to pots when one player have no move left (after the move)
	 * returns true if player can play again, false if not
	 */
	public boolean performMove(int move){
		if (!isMovePossible(move)) throw new Error("move not possible");
		byte beansFromAmbo = field[move];
		field[move] = 0; // empty the amboo
		int offset = 0; // for jumping over the house of the opponent
		// fillup the following amboos/houses with the beans
		int indexHouseOfOpponent = (player==1)? 13 : 6;
		for (int i = 1; i <= beansFromAmbo; i++){
			int fieldindex = (move+i+offset)%14;
			if (fieldindex == indexHouseOfOpponent){
				offset += 1;
				fieldindex = (move+i+offset)%14;
			}
			field[fieldindex] += 1;
			// need to prevent, that the ambo goes into the opponents house
		}
		// if placed last bean into empty amboo you get all beans of enemy
		int indexEndfield = (move+beansFromAmbo+offset)%14;
		if (field[indexEndfield] == 1 && ((player == 1 && indexEndfield < 6) || (player == 2 && indexEndfield > 6 && indexEndfield < 13))){
			int mirroredField = FIELD_PARTNER[indexEndfield]; // field 0 takes beans from 7 and field 8 from 1 (8+7 = 15, 15%14 = 1)
			if ((field[mirroredField] != 0)){
				int indexHouse = (player==1)? 6 : 13;
				field[indexHouse] += field[mirroredField] + 1; //the 1 from the amboo, its later removed
				field[mirroredField] = 0;
				field[indexEndfield] = 0;
			}
		}
		// search if the amboos of at least one player are empty -> should be faster than loop
		if ((field[0]==0 && field[1]==0 && field[2]==0 && field[3]==0 && field[4]==0 && field[5]==0) 
				|| 
			(field[7]==0 && field[8]==0 && field[9]==0 && field[10]==0 && field[11]==0 && field[12]==0)){
			// if one player has no possible move anymore -> put all remaining tokens to the house
			putAllBeansOfAmboosToHouses();
		}
		// if player 1/2 reached with his last move his house, he have a new move
		// else it's the other ones move 
		if ((player == 1 && indexEndfield == 6) || (player == 2 && indexEndfield == 13)){
			return true;
		} else {
			player = getOtherPlayer();
			return false;
		}
	}
	
	/**
	 * places all remaining beans to the houses
	 */
	public void putAllBeansOfAmboosToHouses(){
		for (int i = 0; i < 6; i++){
			field[6] += field[i];
			field[i] = 0;
		}
		for (int i = 7; i < 13; i++){
			field[13] += field[i];
			field[i] = 0;
		}
	}
	
	/**
	 * @return true if the move i possible from the current player
	 */
	public Boolean isMovePossible(int move){
		// todo test
		if ((player == 1 && move > 6) || (player == 2 && (move < 6 || move == 13))) return false;
		else if (field[move] == 0) return false;
		else return true;
	}
	
	/**
	 * If any one has more than the half of the beans,
	 * the winner of the game is decided
	 * 
	 * @return true if it is decided who will win 
	 */
	public Boolean willAnyWin() {
		if (this.willIWin() || this.willOtherWin())
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @return true if current player will win
	 */
	private Boolean willIWin() {
		int pointsToWin = AMOUNTOFBEANSAMBOO*6;
		if (getPoints(getPlayer()) > pointsToWin) {
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * 
	 * @return true if current player will lose
	 */
	private Boolean willOtherWin() {
		int pointsToWin = AMOUNTOFBEANSAMBOO*6;
		if (getPoints(getOtherPlayer()) > pointsToWin) {
			return true;
		}
		else 
			return false;
	}
	
	public int evaluate() {
		return evalWillWin() + evalBeansHouses();
	}

	/**
	 * @return 10mio if win is secure or -10mio if lose is secure, otherwise 0
	 */
	public int evalWillWin(){
		if (willIWin()){
			return 10000000;
		} else if (willOtherWin()){
			return -1000000;
		} else {
			return 0;
		}
	}
	
	/**
	 * @return difference between players beans in houses and those of the opponent 
	 */
	public int evalBeansHouses(){
		return getPoints(getPlayer()) - getPoints(getOtherPlayer());
	}
	
	
}
