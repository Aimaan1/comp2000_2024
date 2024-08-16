import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.Queue;

public class GridDrawing extends JPanel {

    private static final int TRAIL_LENGTH = 100;
    private static final int CIRCLE_SIZE = 15; 
    private static final int GAP_THRESHOLD = 15;
    private final Queue<Point> mouseTrail = new LinkedList<>();
    private Timer retractionTimer;
    private Point lastPoint;
    private Point highlightedCell;

    public GridDrawing() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (lastPoint == null || e.getPoint().distance(lastPoint) > GAP_THRESHOLD) {
                    if (mouseTrail.size() >= TRAIL_LENGTH) {
                        mouseTrail.poll();
                    }
                    mouseTrail.add(e.getPoint());
                    lastPoint = e.getPoint();
                }
                highlightedCell = calculateCell(e.getPoint());

                repaint();
                if (retractionTimer != null) {
                    retractionTimer.stop();
                }
                startRetractionTimer();
            }
        });
    }

    private Point calculateCell(Point p) {
        int gridSize = 20;
        int cellSize = 35;
        int offset = 10;

        int col = (p.x - offset) / cellSize;
        int row = (p.y - offset) / cellSize;

        if (col >= 0 && col < gridSize && row >= 0 && row < gridSize) {
            return new Point(col, row);
        } else {
            return null;
        }
    }

    private void startRetractionTimer() {
        retractionTimer = new Timer(10, e -> {
            int pointsToRemove = 5; 
            for (int i = 0; i < pointsToRemove && !mouseTrail.isEmpty(); i++) {
                mouseTrail.poll();
            }
            repaint();

            if (mouseTrail.isEmpty()) {
                retractionTimer.stop();
            }
        });
        retractionTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int gridSize = 20;
        int cellSize = 35;
        int offset = 10;

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = offset + col * cellSize;
                int y = offset + row * cellSize;
                if (highlightedCell != null && highlightedCell.x == col && highlightedCell.y == row) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                    g.setColor(Color.BLACK); 
                }

                g.drawRect(x, y, cellSize, cellSize);
            }
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        for (Point p : mouseTrail) {
            g2d.fillOval(p.x - CIRCLE_SIZE / 2, p.y - CIRCLE_SIZE / 2, CIRCLE_SIZE, CIRCLE_SIZE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GridDrawing panel = new GridDrawing();

        frame.setSize(720, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
}
