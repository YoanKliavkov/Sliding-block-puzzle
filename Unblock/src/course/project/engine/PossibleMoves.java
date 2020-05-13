package course.project.engine;

public class PossibleMoves {
	public boolean up, down, right, left;

	public PossibleMoves() {
		up = down = right = left = true;
	}

	@Override
	public String toString() {
		return "PossibleMoves [up=" + up + ", down=" + down + ", right=" + right + ", left=" + left + "]";
	}
}