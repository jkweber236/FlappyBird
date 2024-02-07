import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class PlayGame extends JFrame {

    private JPanel canvas;
    private int dotX;
    private int dotY;
    private int jumpVelocity;
    private boolean jumping;

    public PlayGame(int width, int height) {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(Color.BLUE);
                g.fillOval(dotX, dotY, 50, 50);
            }
        };

        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.lightGray);

        InputMap inputMap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = canvas.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "jumpAction");
        actionMap.put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jump();
            }
        });

        add(canvas);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        dotX = 50;
        dotY = 100;
        jumpVelocity = 0;
        jumping = false;

        // Set up a Timer to continually update the dot's position
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDot();
                repaint();
            }
        });
        timer.start();
    }

    private void moveDot() {
        if (jumping) {
            dotY -= jumpVelocity;
            jumpVelocity -= 1;

            if (dotY >= getHeight() - 50) {
                dotY = getHeight() - 50;
                jumping = false;
            }
        } else {
            dotY += 2; // Adjust the value for the desired falling speed
            if (dotY > getHeight() - 50) {
                dotY = getHeight() - 50;
            }
        }
    }

    private void jump() {
        if (!jumping) {
            jumpVelocity = 15; // Adjust the value for the desired jump strength
            jumping = true;
        }
    }
}
