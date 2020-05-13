package course.project.engine;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * This class contains only one static method. The method is used for solving
 * the game "Unblock me"
 * 
 * 
 * @author Yoan
 */
public class UnblockSolver {

	public final static int MAX_POSITIONS = 1000000; // maximum number of positions to be analyzed

	/**
	 * 
	 * @param myBoard to solve
	 * @return the final board if solution is found or null if no solution is found.
	 *         Every board has a reference to its parent.
	 */
	public static Board solve(final Board myBoard) {
		if (!myBoard.isValid()) {
			return null;
		}

		if (myBoard.isSolved()) {
			return myBoard;
		}

		final PriorityQueue<Board> myQueue = new PriorityQueue<Board>();
		final HashSet<Board> visited = new HashSet<Board>();

		myQueue.add(myBoard);
		visited.add(myBoard);

		while (!myQueue.isEmpty() && visited.size() <= MAX_POSITIONS) {
			final Board current = myQueue.poll(); // save a reference to the board with the lowest cost and remove from
													// the queue
			final PossibleMoves[] moves = current.getPossibleMoves(); // get all possible moves that could be made from
																		// the given position

			for (int i = 1; i < moves.length; i++) {
				if (moves[i] == null) // piece i can not be moved, continue to the next piece
				{
					continue;
				}

				if (moves[i].up) // piece i can be moved up
				{
					final Board up = current.getBoardAfterMove(i, Direction.UP); // get the board after moving i in
																					// direction up

					if (!visited.contains(up)) // if up is visited do nothing, else check for solved and add to visited
												// and queue
					{
						if (up.isSolved()) {
							return up;
						}
						visited.add(up);
						myQueue.add(up);
					}
				}

				// the rest is the same...

				if (moves[i].down) {
					final Board down = current.getBoardAfterMove(i, Direction.DOWN);

					if (!visited.contains(down)) {
						if (down.isSolved()) {
							return down;
						}
						visited.add(down);
						myQueue.add(down);
					}
				}

				if (moves[i].left) {
					final Board left = current.getBoardAfterMove(i, Direction.LEFT);

					if (!visited.contains(left)) {
						if (left.isSolved()) {
							return left;
						}
						visited.add(left);
						myQueue.add(left);
					}
				}

				if (moves[i].right) {
					final Board right = current.getBoardAfterMove(i, Direction.RIGHT);

					if (!visited.contains(right)) {
						if (right.isSolved()) {
							return right;
						}
						visited.add(right);
						myQueue.add(right);
					}
				}
			}
		}

		return null; // if the code reaches here just return null, no solution was found
	}
}
