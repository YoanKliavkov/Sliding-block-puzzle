package course.project.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import course.project.engine.Board;
import course.project.engine.Point;
import course.project.engine.UnblockSolver;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private JPanel mainPanel = new JPanel(new CardLayout());
	final CardLayout cards = (CardLayout) (mainPanel.getLayout());
	private final JFrame frame = this;

	private final JPanel inputPanel = new JPanel();
	private final JPanel outputPanel = new JPanel();

	private SolutionPanel solutionPanel = new SolutionPanel();

	private ArrayList<Board> solution = new ArrayList<Board>();
	private int index = 0;
	private Color selectedColor = Color.RED;

	public Window() {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		configInputPanel();
		configSolutionPanel();

		mainPanel.add(inputPanel, "inputPanel");
		mainPanel.add(outputPanel, "solutionPanel");

		cards.show(mainPanel, "inputPanel");

		add(mainPanel);

		setSize(650, 450);
		setVisible(true);
	}

	private void configInputPanel() {

		inputPanel.setLayout(new BorderLayout());

		final JPanel colorsPanel = new JPanel(new FlowLayout());
		final Color[] colors = { Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, new Color(165, 42, 42),
				new Color(128, 0, 128), new Color(128, 128, 128), Color.BLACK, Color.WHITE };
		final String[] colorNames = { "red", "yellow", "blue", "green", "orange", "brown", "purple", "grey", "wall",
				"clear tile" };
		final JRadioButton[] radioButtons = new JRadioButton[10];

		final ButtonGroup group = new ButtonGroup();

		for (int x = 0; x < radioButtons.length; x++) {
			final JRadioButton jrb = new JRadioButton(colorNames[x]);
			final int x_ = x;
			jrb.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					selectedColor = colors[x_];
				}
			});

			radioButtons[x] = jrb;
			group.add(jrb);
			colorsPanel.add(jrb);
		}
		group.setSelected(radioButtons[0].getModel(), true);

		final JPanel buttonsGridHolder = new JPanel(new FlowLayout());
		final JPanel buttonsGrid = new JPanel(new GridLayout(6, 6));
		buttonsGridHolder.add(buttonsGrid);

		final JButton[][] buttons = new JButton[6][6];

		for (int x = 0; x < buttons.length; x++) {
			for (int y = 0; y < buttons[x].length; y++) {
				final JButton b = new JButton();
				b.setPreferredSize(new Dimension(50, 50));
				b.setBackground(Color.WHITE);
				b.setOpaque(true);

				b.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {

						if (b.getBackground().equals(selectedColor)) {
							b.setBackground(Color.WHITE);
						} else {
							b.setBackground(selectedColor);
						}

					}
				});

				buttonsGrid.add(b);
				buttons[x][y] = b;
			}
		}

		inputPanel.add(colorsPanel, BorderLayout.NORTH);
		inputPanel.add(buttonsGridHolder, BorderLayout.CENTER);

		final JPanel southButtons = new JPanel(new FlowLayout());
		final JButton solve = new JButton("Solve");
		final JButton clear = new JButton("Clear board");
		southButtons.add(solve);
		southButtons.add(clear);

		inputPanel.add(southButtons, BorderLayout.SOUTH);

		solve.addActionListener(new ActionListener() {

			private final HashMap<Color, Integer> colorMap = new HashMap<Color, Integer>();

			{
				for (int x = 1; x <= 8; x++) {
					colorMap.put(colors[x - 1], x);
				}
				colorMap.put(Color.WHITE, 0);
				colorMap.put(Color.BLACK, -1);
			}

			public void actionPerformed(final ActionEvent e) {
				int[][] matrix = new int[6][6];

				for (int x = 0; x < matrix.length; x++) {
					for (int y = 0; y < matrix[x].length; y++) {
						matrix[x][y] = colorMap.get(buttons[x][y].getBackground());
					}
				}

				final Board toSolve = new Board(matrix, null, 0, 0);

				if (!toSolve.isValid()) {
					JOptionPane.showMessageDialog(frame, "Red piece should be on only one line and connected.");
					return;
				}

				solution.clear();
				index = 0;

				Board board = UnblockSolver.solve(toSolve);

				if (board != null) {
					solution.add(board);

					while (board.getPrev() != null) {
						board = board.getPrev();
						solution.add(board);
					}
					Collections.reverse(solution);
					solutionPanel.showBoard(board, new Point(20, 20), 50);
					cards.show(mainPanel, "solutionPanel");
				} else {
					JOptionPane.showMessageDialog(frame,
							"No solution found after analyzing " + UnblockSolver.MAX_POSITIONS + " positions.");
				}
			}
		});

		clear.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				for (int x = 0; x < buttons.length; x++) {
					for (int y = 0; y < buttons[x].length; y++) {
						buttons[x][y].setBackground(Color.WHITE);
					}
				}
			}
		});
	}

	private void configSolutionPanel() {
		outputPanel.setLayout(new BorderLayout());
		outputPanel.setBackground(Color.white);

		final JPanel buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.setBackground(Color.white);

		final JButton next = new JButton("Next");
		final JButton previous = new JButton("Previous");
		final JButton back = new JButton("Back to input");

		buttonsPanel.add(previous);
		buttonsPanel.add(next);

		final JPanel south = new JPanel(new FlowLayout());
		south.setBackground(Color.white);
		south.add(back);

		final Box box = new Box(BoxLayout.Y_AXIS);

		final JPanel center = new JPanel(new FlowLayout());
		center.setBackground(Color.white);
		center.add(solutionPanel);

		box.add(Box.createVerticalGlue());
		box.add(center);
		box.add(Box.createVerticalGlue());

		outputPanel.add(buttonsPanel, BorderLayout.NORTH);
		outputPanel.add(box, BorderLayout.CENTER);
		outputPanel.add(south, BorderLayout.SOUTH);

		back.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				cards.show(mainPanel, "inputPanel");
			}
		});

		next.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				if (index + 1 < solution.size()) {
					index++;
					solutionPanel.showBoard(solution.get(index), new Point(20, 20), 50);
				} else {
					JOptionPane.showMessageDialog(frame, "You reached the final position.");
				}
			}
		});

		previous.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				if (index - 1 >= 0) {
					index--;
					solutionPanel.showBoard(solution.get(index), new Point(20, 20), 50);
				} else {
					JOptionPane.showMessageDialog(frame, "You reached the starting position.");
				}
			}
		});
	}

	public static void main(final String[] args) {
		final Window window = new Window();
	}

}
