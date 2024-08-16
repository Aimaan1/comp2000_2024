import javax.swing.*;
import java.awt.*;

public class GridDrawing extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int gridSize = 20;
        int cellSize = 35;
        int offset = 10;

        // Draw the grid
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = offset + col * cellSize;
                int y = offset + row * cellSize;
                g.drawRect(x, y, cellSize, cellSize);
            }
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
