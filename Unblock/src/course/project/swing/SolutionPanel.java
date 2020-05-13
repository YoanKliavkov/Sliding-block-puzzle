package course.project.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import course.project.engine.Board;
import course.project.engine.Point;

@SuppressWarnings("serial")
public class SolutionPanel extends JPanel {
	private BufferedImage image;
	private final Color[] colors = { Color.WHITE, Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE,
			new Color(165, 42, 42), new Color(128, 0, 128), new Color(128, 128, 128), Color.BLACK };

	SolutionPanel() {
		setLayout(new FlowLayout());
	}

	public void refreshSize() {
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}

	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	public void showBoard(final Board board, final Point offset, final int lineLen) {
		final int[][] matrix = board.getMatrix();
		final int height = board.getHeight();
		final int width = board.getWidth();

		image = new BufferedImage(width * lineLen + 2 * offset.getY(), height * lineLen + 2 * offset.getX(),
				BufferedImage.TYPE_INT_RGB);

		final Graphics2D g2 = image.createGraphics();

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, image.getWidth(), image.getHeight());

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(10));
		g2.drawRect(offset.getY(), offset.getX(), width * lineLen, height * lineLen);

		g2.setColor(Color.RED);

		g2.drawLine(offset.getY() + 6 * lineLen, offset.getX() + 2 * lineLen, offset.getY() + 6 * lineLen,
				offset.getX() + 3 * lineLen);

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {

				if (matrix[x][y] == -1) {
					g2.setColor(Color.BLACK);
				} else
					g2.setColor(colors[matrix[x][y]]);
				g2.fillRect(offset.getY() + y * lineLen, offset.getX() + x * lineLen, lineLen, lineLen);
			}

		}

		g2.setColor(Color.RED);

		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		revalidate();
		repaint();
	}

}
