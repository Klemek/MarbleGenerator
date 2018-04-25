package fr.klemek.marble;

import javax.swing.*;
import java.awt.*;

class MarbleViewer extends JFrame {

    MarbleViewer(Generator generator) {
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new Panel(generator));
        this.pack();
        this.setVisible(true);
    }

    private class Panel extends JPanel {

        private final transient Generator generator;

        Panel(Generator generator) {
            this.generator = generator;
            Dimension size = new Dimension(generator.getWidth(), generator.getHeight());
            this.setMinimumSize(size);
            this.setPreferredSize(size);
            this.setMaximumSize(size);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int size = generator.getSize();

            for (int x = 0; x < generator.getWidth2(); x++) {
                for (int y = 0; y < generator.getHeight2(); y++) {
                    g.setColor(generator.getTable()[x][y].toColor());
                    g.fillRect(x * size, y * size, size, size);
                }
            }
        }
    }

}
