/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)
			* BRICK_SEP)
			/ NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Animation delay or pause time between ball moves */
	private static final int DELAY = 15;
	
	/** Number of times the paddle has to hit before doubling speed */
	private static final int SUCCESSFULL_PADDLEHITS_BEFORE_KICKER = 7;
	
	/** Start value for vY */
	private static final double VY_START = 5.0;
	
	/**
	 * Instance variables
	 */
	private GRect paddle;
	private GOval ball;
	private double vX, vY;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int numOfBricks;
	private GLabel label;
	private int numOfHits;
	private GLabel scoreLabel;
	private int score;
	private boolean GameStart;
	
	/* Method: init() */
	/** Initialize the game*/
	
	public void init() {
		setTheBricks();
		setThePaddle();
		setScore();
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */

		for (int i = 0; i < NTURNS; i++) {
			displayMessage("Click to start");
			GameStart = false;
			while (!GameStart) {
				pause(DELAY * 5);
			}
			removeGameMessage();
			setTheBall();
			while ((ball.getY() < HEIGHT) && (numOfBricks > 0)) {
				moveTheBall();
				checkForCollision();
			}
			if (numOfBricks == 0) {
				displayMessage("You Win!");
				return;
			}
		}
		displayMessage("Game Over");
	}
	
	/**
	 * Set the Bricks
	 */
	private void setTheBricks() {
		int Y = BRICK_Y_OFFSET;
		int firstX = (WIDTH - BRICK_WIDTH * NBRICKS_PER_ROW - BRICK_SEP * (NBRICKS_PER_ROW - 1)) / 2;
		Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};

		for (int k = 0; k < NBRICK_ROWS; k ++) {
			int X = firstX;
			Color c = colors[k / 2];
			for (int i = 0; i < NBRICKS_PER_ROW; i++) {
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick.setLocation(X, Y);
				brick.setFilled(true);
				brick.setColor(c);
				add(brick);
				X += BRICK_WIDTH + BRICK_SEP;
			}
			Y += BRICK_HEIGHT + BRICK_SEP;
		}
		numOfBricks = NBRICK_ROWS * NBRICKS_PER_ROW;
	}
	
	/**
	 * Set The Paddle
	 */
	
	private void setThePaddle(){
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setLocation(WIDTH - PADDLE_WIDTH /2 , HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}
	
	/**
	 * Set The Ball
	 */

	private void setTheBall() {
		double b = 2 * BALL_RADIUS;
		ball = new GOval(b, b);
		ball.setLocation(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
		add(ball);
		
		vY = VY_START;
		vX = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vX = -vX;
		
		numOfHits = 0;
	}
	
	/**
	 * set the score
	 */
	
	private void setScore() {
		score = 0;
		scoreLabel = new GLabel("Score: " + score + " " );
		scoreLabel.setFont("Verdana-30");
		double X = getWidth() - scoreLabel.getWidth();
		double Y = getWidth() - scoreLabel.getWidth();
		scoreLabel.setLocation(X,Y);
		add(scoreLabel);
	}

	/**
	 * Mouse Function
	 */
	
	public void mouseMoved(MouseEvent e) {
		double x = e.getX();
		double minX = PADDLE_WIDTH / 2;
		double maxX = WIDTH - PADDLE_WIDTH / 2;
		if (x < minX) {
			x = minX;
		} else if (x > maxX) {
			x = maxX;
		}
		paddle.setLocation(
				x - minX, 
				HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT
		);
		
	}
	
	/**
	 * Start The Game!
	 */

	public void mouseClicked(MouseEvent e) {
		GameStart = true;
	}

	/**
	 * Move The Ball
	 */

	private void moveTheBall() {
		ball.move(vX, vY);
	}
	
	/**
	 * Collision and direction
	 */

	private void checkForCollision() {
		double x = ball.getX();
		double y = ball.getY();
		double b = 2 * BALL_RADIUS;
		if (y < 0) {
			vY = -vY;
			ball.move(0, -2 * y);
		}
		if (x > WIDTH - b) {
			vX = -vX;
			ball.move(-2 * (x - WIDTH + b), 0);
		}
		if(x < 0) {
			vX = -vX;
			ball.move(-2 * x, 0);
		}
		
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			double hitPosition = (2 * (x - paddle.getX()) + b - PADDLE_WIDTH) / (b + PADDLE_WIDTH);
			if (hitPosition < 0) {
				vX = -Math.abs(vX);
			}else {
				vX = Math.abs(vX);
			}
			numberOfPaddleHits++;
			if (numberOfPaddleHits % SUCCESSFULL_PADDLEHITS_BEFORE_KICKER == 0) {
				
			}
		}
	}
	
	/**
	 * Message for game
	 * 
	 * @param message
	 */

	private void displayMessage(String message) {
		label = new GLabel(message);
		label.setFont("Verdana-30");
		double X = (getWidth() - label.getWidth()) / 2;
		double Y = (getHeight() - label.getHeight()) / 2;
		label.setLocation(X, Y);
		add(label);
	}

	/**
	 * delete message
	 */

	 private void removeGameMessage() {
	 remove(label);
	 }
}
