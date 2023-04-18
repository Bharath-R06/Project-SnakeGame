
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    //initialising size of panel screen width
    static final int SCREEN_WIDTH = 1200;
    //initialising size of panel screen height
    static final int SCREEN_HEIGHT = 600;
    //initialising size of grid unit size
    static final int UNIT_SIZE = 35;
    //to calculate how many objects to fit on the screen
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 125;
    //create 2 arrays x and y these arrays will hold all co-ordinates for the body parts of snake
    //x array will store all x coordinates
    final int x[] = new int[GAME_UNITS];
    //y array will store all y coordinates
    final int y[] = new int[GAME_UNITS];
   // when game begins inirtialise bodyparts of snake as needed
    int bodyParts = 3;
    //initially whwn game begins applesEaten will assigned to zero
    int applesEaten;
    //x co-ordinate for the place where apple is located
    int appleX;
    //y co-ordinate for the place where apple is located
    int appleY;
    //initialing char varibale called direction 'R' means when game begins snake will move to right direction
    char direction = 'R';

    boolean running = false;
    Timer timer;
    //instance of random class
    Random random;

    GamePanel(){
        //creating instance of random class
        random = new Random();
        //setting PreferredSize for gamepanel
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        //set Background of panel to black
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        //after all finishing game panel constructor we need to call startgame function
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        //if game is running
        if(running) {
		//for loop is to create grid in x and y axis
//			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}
			// to draw the apple
            // to set the colour of apple
            g.setColor(Color.red);
            //to set the shape of apple to oval
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);


            //to draw the snake
            for(int i = 0; i< bodyParts;i++) {
                if(i == 0) {
                    //when game begins 1st body part of snake initialsing colour to green and shape of snaketo rectangle;
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    //if we want all body parts of snake colour to be in same colour
                   // g.setColor(Color.green);
                    //if we want all body parts of snake colour to be in different colour except 1st body part
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //when game starts in top to show score after snake pass on each apple we need to update score
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        //if game is lost we need to call game over method an pass g grahic parameter
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        //storing apple in x axis random poistion within the width of the panel size
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        //storing apple in y axis random poistion within the height of the panel size
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple() {

        //to grap the apple
        if((x[0] == appleX) && (y[0] == appleY)) {
            //if snake body passes on apple then increase the body part
            bodyParts++;
            //if snake body passes on apple then increase the applesEaten variable initially it is 0
            applesEaten++;
            //after snake passes on apple to call new apple in the panel
            newApple();
        }
    }
    public void checkCollisions() {
        //check if head collides with body
        for(int i = bodyParts;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
     //if body collides then we need to stop the game of thimer function
        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        //after game over  we need to show score text
        //Score text colour
        g.setColor(Color.red);
        // score text font
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        // for lining score text in the centre of the screen panel
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        //Game Over texT COLOUR
        g.setColor(Color.red);
        //Game over text font
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        //to lining Gameover text in the centre of the screen panel
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            //if game is sunning move snake
            move();
            //if we passed on apple
            checkApple();
            //if snake hit any wall of panel
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}