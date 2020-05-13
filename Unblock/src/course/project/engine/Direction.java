package course.project.engine;

public enum Direction {
	UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

	public final int x_offset;
	public final int y_offset;

	Direction(int x, int y) {
		x_offset = x;
		y_offset = y;
	}
}
