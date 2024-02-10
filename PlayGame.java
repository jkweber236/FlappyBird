import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

public class PlayGame extends JFrame {
    // Initializing the JPanel to use for drawing elements.
    private JPanel canvas;

    // Initializing the score (subtracting 1 so the game starts at 0).
    private int score = -1;

    // Initializing the coordinates of the ball.
    private int dotX;
    private int dotY;
    // Initalizing the jump strength.
    private int jumpVelocity;
    // Indicates whether or not the user is jumping.
    private boolean jumping;

    // Initializing the coordinates of the obstacles.
    private int obstacle1X;
    private int obstacle1Y;
    private int obstacle2X;
    private int obstacle2Y;
    private int obstacle3X;
    private int obstacle3Y;
    private int obstacle4X;
    private int obstacle4Y;

    // Initializing the height of the obstacles.
    private int obstacleHeight;

    // Indicates whether the game is currently active or over.
    private boolean gameActive = true;

    // JLabel to display game over message.
    private JLabel gameOverLabel;

    // JLabel to display the score.
    private JLabel scoreLabel;

    // Indicates whether or not an obstacle has been jumped over.
    private boolean obstacle1Passed;
    private boolean obstacle2Passed;
    private boolean obstacle3Passed;
    private boolean obstacle4Passed;

    public PlayGame(int width, int height) {

        // Set up the main game window
        setTitle("Jump Master");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Create a JPanel to serve as the game canvas
        canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // Renders the visual content
                super.paintComponent(g);

                // Draw the circle/character
                g.setColor(Color.BLUE);
                g.fillOval(dotX, dotY, 50, 50);

                // Fill the top and bottom of the screen (non-playable area)
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 800, 180);
                g.fillRect(0, 380, 800, 220);

                // Draw the obstacles
                g.fillRect(obstacle1X, obstacle1Y, 10, obstacleHeight);
                g.fillRect(obstacle2X, obstacle2Y, 10, obstacleHeight);
                g.fillRect(obstacle3X, obstacle3Y, 10, obstacleHeight);
                g.fillRect(obstacle4X, obstacle4Y, 10, obstacleHeight);
            }
        };

        // Set the size and color of the game screen.
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.lightGray);

        // Initialize the game over label
        gameOverLabel = new JLabel("");
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
        canvas.add(gameOverLabel);

        // Initialize the score label
        scoreLabel = new JLabel("");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 25));
        canvas.add(scoreLabel);

        // Set layout to null for manual positioning
        canvas.setLayout(null);

        // Set up keyboard input for jumping
        InputMap inputMap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = canvas.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "jumpAction");
        actionMap.put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jump();
            }
        });

        // Add the canvas to the window
        add(canvas);
        pack();

        // Centers the window on the screen
        setLocationRelativeTo(null);
        // Makes the JFrame visible.
        setVisible(true);

        // Set the initial Y-coordinate of the obstacles.
        obstacle1Y = 300;
        obstacle2Y = 300;
        obstacle3Y = 300;
        obstacle4Y = 300;

        // Set the initial X-coordinate of the obstacles
        obstacle1X = 200;
        obstacle2X = 400;
        obstacle3X = 600;
        obstacle4X = 800;

        // Set the height of the obstacles
        obstacleHeight = 80;

        // Set the initial coordinates of the character
        dotX = 250;
        dotY = 330;

        // Set up a Timer to continually update the game state
        Timer timer1 = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDot();
                updateLine();
                passObstacles();
                checkCollision();
                repaint();
            }
        });
        timer1.start();
    }

    // Updates obstacle positions and resets them if they move off-screen
    private void updateLine() {

        if (!gameActive) {
            // Stop moving if the game is not active
            return;
        }

        // Moves obstacles left towards the character
        obstacle1X -= 4;
        obstacle2X -= 4;
        obstacle3X -= 4;
        obstacle4X -= 4;

        // Resets the position of the obstacles when they move off-screen
        // Also resets the state of obstaclePassed to continue counting each time
        // an obstacle has been passed.
        if (obstacle1X <= 0) {
            obstacle1X = getWidth();
            obstacle1Passed = false;
        } else if (obstacle2X <= 0) {
            obstacle2X = getWidth();
            obstacle2Passed = false;
        } else if (obstacle3X <= 0) {
            obstacle3X = getWidth();
            obstacle3Passed = false;
        } else if (obstacle4X <= 0) {
            obstacle4X = getWidth();
            obstacle4Passed = false;
        }
    }

    // Checks if the character has passed an obstacle and updates the score
    private void passObstacles() {
        // Makes sure the ball is passedone obstacle and not the next to show
        // which obstacle was jumped over
        if (dotX > obstacle1X && dotX < obstacle2X) {
            if (!obstacle1Passed) {
                score++;
                obstacle1Passed = true; // Indicates the obstacle is passed
            }
        } else if (dotX > obstacle2X && dotX < obstacle3X) {
            if (!obstacle2Passed) {
                score++;
                obstacle2Passed = true; // Indicates the obstacle is passed
            }
        } else if (dotX > obstacle3X && dotX < obstacle4X) {
            if (!obstacle3Passed) {
                score++;
                obstacle3Passed = true; // Indicates the obstacle is passed
            }
        } else if (dotX > obstacle4X && dotX < obstacle1X) {
            if (!obstacle4Passed) {
                score++;
                obstacle4Passed = true; // Indicates the obstacle is passed
            }
        }
    }

    // Moves the character in response to a jump
    private void moveDot() {
        if (jumping) {
            // Allows the ball to move upward
            dotY -= jumpVelocity;
            // Allows the ball to fall
            jumpVelocity -= 1;

            // Stop jumping (falling) when the character reaches the ground
            if (dotY >= 330) {
                dotY = 330;
                jumping = false;
            }
        }
    }

    private void jump() {
        // Only allows the user to jump again once they have landed
        if (!jumping) {
            jumpVelocity = 16;
            jumping = true;
        }
    }

    private void checkCollision() {
        // Don't check for collision if the game is already over
        if (!gameActive) {
            return;
        }

        // Define (invisible) shapes for character and obstacles
        // These are used to track collisions
        int dotRadius = 25;
        Ellipse2D ballEllipse = new Ellipse2D.Double(dotX - dotRadius, dotY - dotRadius, 2 * dotRadius, 2 * dotRadius);
        Rectangle obstacle1Rect = new Rectangle(obstacle1X, obstacle1Y, 10, obstacleHeight);
        Rectangle obstacle2Rect = new Rectangle(obstacle2X, obstacle2Y, 10, obstacleHeight);
        Rectangle obstacle3Rect = new Rectangle(obstacle3X, obstacle3Y, 10, obstacleHeight);
        Rectangle obstacle4Rect = new Rectangle(obstacle4X, obstacle4Y, 10, obstacleHeight);

        // Check if the ball intersects with any obstacle
        if (ballEllipse.intersects(obstacle1Rect) || ballEllipse.intersects(obstacle2Rect) ||
                ballEllipse.intersects(obstacle3Rect) || ballEllipse.intersects(obstacle4Rect)) {

            gameActive = false; // Stop the game
            displayGameOverMessage("Game over");
            displayScore(score);
            return; // Exit the method to avoid unnecessary repaint
        }

        // If no collision, continue the game
        canvas.repaint();
    }

    private void displayGameOverMessage(String message) {
        // Displays a game over message once the user loses the game
        gameOverLabel.setText(message);
        gameOverLabel.setBounds((canvas.getWidth() / 2) - 135, getHeight() / 2 - 300, 400, 100);
    }

    private void displayScore(int score) {
        // Displays the score once the user loses the game
        String text = String.format("<html>%s%d</html>", "Score: ", score);
        scoreLabel.setText(text);
        scoreLabel.setBounds((canvas.getWidth() / 2) - 60, getHeight() / 2 - 250, 400, 100);
    }
}