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
	
	/**
	 * Instance variables
	 */
//	private GRect paddle;
	private GOval ball;
	private double vX, vY;
//	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int numOfBricks;
	private GLabel label;
//	private int numOfHits;
	private GLabel scoreLabel;
//	private int score;
	private boolean startGame;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */

		for (int i = 0; i < NTURNS; i++) {
			displayMessage("Click to start");
			startGame = false;
			while (!startGame) {
				pause(DELAY * 5);
			}
//			removeGameMessage();
			setTheBall();
			while ((ball.getY() < HEIGHT) && (numOfBricks > 0)){
				moveTheBall();
				
			}
		}
		displayMessage("Game Over");
	}
	
	/**
	 * Set The Ball
	 */
	
	private void setTheBall() {
		double b = 2 * BALL_RADIUS;
		ball = new GOval(b, b);
		ball.setLocation(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
		add(ball);
	}
	
	/**
	 * Move The Ball
	 */
	
	private void moveTheBall() {
		ball.move(vX, vY);
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

//	private void removeGameMessage() {
//		remove(label);
//	}
}
