package course.project.engine;

/**
 * Represents the board as an integer matrix.
 * 
 * @author Yoan
 */
public class Board implements Comparable<Board> {

	public static final int BLOCKED_CELL = -1;

	private final int[][] matrix;
	private final int height;
	private final int width;
	private final Board prev;
	private final int moves;
	private final int heuristic;

	/**
	 * 
	 * @param matrix
	 * @param prev      reference to the last board
	 * @param moves     moves it took to reach this board
	 * @param heuristic moves it takes to solve if all pieces except the red are
	 *                  removed
	 */
	public Board(final int[][] matrix, final Board prev, final int moves, final int heuristic) {
		this.matrix = matrix;
		this.height = matrix.length;
		this.width = matrix[0].length;
		this.prev = prev;
		this.moves = moves;
		this.heuristic = heuristic;
	}

	/**
	 * 
	 * @return all possible moves from this position
	 */
	public PossibleMoves[] getPossibleMoves() {
		final PossibleMoves[] blockMoves = new PossibleMoves[10];

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				final int current = matrix[x][y];

				if (current == BLOCKED_CELL || current == 0) {
					continue;
				}

				if (blockMoves[current] == null) {
					blockMoves[current] = new PossibleMoves();
				}

				if (x - 1 < 0 || (matrix[x - 1][y] != 0 && matrix[x - 1][y] != current)) {
					blockMoves[current].up = false;
				}

				if (x + 1 >= height || (matrix[x + 1][y] != 0 && matrix[x + 1][y] != current)) {
					blockMoves[current].down = false;
				}

				if (y - 1 < 0 || (matrix[x][y - 1] != 0 && matrix[x][y - 1] != current)) {
					blockMoves[current].left = false;
				}

				if (y + 1 >= width || (matrix[x][y + 1] != 0 && matrix[x][y + 1] != current)) {
					blockMoves[current].right = false;
				}
			}
		}

		return blockMoves;
	}

	/**
	 * 
	 * @param shape
	 * @param dir
	 * @return new board after the shape is moved in the given direction
	 */
	public Board getBoardAfterMove(final int shape, final Direction dir) {
		final int[][] newMatrix = new int[height][width];

		Point lastOne = null;

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				if (matrix[x][y] == shape) {
					newMatrix[x + dir.x_offset][y + dir.y_offset] = shape;
					if (shape == 1) {
						lastOne = new Point(x + dir.x_offset, y + dir.y_offset);
					}
				} else if (matrix[x][y] != 0) {
					newMatrix[x][y] = matrix[x][y];
					if (matrix[x][y] == 1) {
						lastOne = new Point(x, y);
					}
				}
			}
		}

		final Board newBoard = new Board(newMatrix, this, moves + 1,
				Math.abs(height / 2 - 1 - lastOne.getX()) + Math.abs(width - 1 - lastOne.getY()));
		return newBoard;
	}

	/**
	 * 
	 * @return true if the board is solved
	 */
	public boolean isSolved() {
		return matrix[height / 2 - 1][width - 1] == 1;
	}

	/**
	 * 
	 * @return true if the red piece is only on one row and is connected
	 */
	public boolean isValid() {

		Point lastOne = null;

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				if (matrix[x][y] == 1) {
					if (lastOne == null) {
						lastOne = new Point(x, y);
					} else {
						if (x != lastOne.getX() || y - 1 != lastOne.getY()) {
							return false;
						}
						lastOne = new Point(x, y);
					}
				}
			}
		}

		return lastOne != null;
	}

	/**
	 * return true if both boards have the same matrices
	 */
	public boolean equals(final Object obj) {
		if (obj instanceof Board == false) {
			return false;
		}

		final Board board = (Board) obj;

		if (board == null || board.height != this.height || board.width != this.width) {
			return false;
		}

		final int[][] matrix_ = board.matrix;

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				if (matrix[x][y] != matrix_[x][y])
					return false;
			}
		}

		return true;
	}

	public int hashCode() {
		return java.util.Arrays.deepHashCode(matrix);
	}

	/**
	 * Used by the PriorityQueue in the A* to compare the boards
	 */
	public int compareTo(final Board b) {
		return Integer.compare(this.moves + this.heuristic, b.moves + b.heuristic);
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Board getPrev() {
		return prev;
	}

	public void print() {
		System.out.println("heuristic " + heuristic);
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				System.out.print("[" + matrix[x][y] + "]");
			}
			System.out.println();
		}

		System.out.println();
	}
}
