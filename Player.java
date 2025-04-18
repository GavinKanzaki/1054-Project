import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.io.*;

import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;

public class Player extends DrawableObject 
{
   //instance variables
   private float velX = 0, velY = 0;
   private int currentScore = 0, bestScore = 0;
   private boolean isDead = false;
   
   private int cellX, cellY;
   private int lastCellX, lastCellY;
   
   //high score instance variables
   Scanner fileScan = null;
   FileOutputStream outputFile = null;
   PrintWriter outputWrite = null;
   
   //constructor
   public Player(float startX, float startY) 
   {
      super(startX, startY, 25);
      cellX = (int)(startX / 100);
      cellY = (int)(startY / 100);
      lastCellX = cellX;
      lastCellY = cellY;
   
      try 
      {
         fileScan = new Scanner(new File("highScore.txt"));
         bestScore = fileScan.nextInt();
      } 
      catch (FileNotFoundException e) 
      {}
   }

   public void update(boolean moveLeft, boolean moveRight, boolean moveUp, boolean moveDown) 
   {  
       //making the player move
       applyMovement();
       if (moveUp) 
       {
         speedUp(0, -0.1f);
       }
       if (moveDown) 
       {
         speedUp(0, 0.1f);
       }
       if (moveLeft) 
       {
         speedUp(-0.1f, 0);
       }
       if (moveRight) 
       {
         speedUp(0.1f, 0);
       }
       //when no key is pressed this makes it slow down
       if (!moveUp && !moveDown) 
       {
         slowDown(false, true, 0.025f);
       }
       if (!moveLeft && !moveRight) 
       {
         slowDown(true, false, 0.025f);
       }
       //calculating score
       currentScore = (int)Math.sqrt(Math.pow(300 - getX(), 2) + Math.pow(300 - getY(), 2));
       updateBestScore();
   }

   private void applyMovement() 
   {
       //adding the veolity/force 
       setX(getX() + velX);
       setY(getY() + velY);
       //upadting the grid position
       cellX = (int)(getX() / 100);
       cellY = (int)(getY() / 100);
   }
   //method to speed up the player
   private void speedUp(float xAccel, float yAccel) 
   {
      //takes in a pos or neg float by which to increase speed variables (forces)
      velX += xAccel;
      velY += yAccel;
      //clamps the speed variables within 5 and negative 5
      velX = Math.max(-5,Math.min(velX,5));
      velY = Math.max(-5,Math.min(velY,5));
   }
   //method to slow down the player
   private void slowDown(boolean reduceX, boolean reduceY, float rate) 
   {
       if (reduceX) 
       {
         velX = adjust(velX, rate);
       }
       if (reduceY) 
       {
         velY = adjust(velY, rate);
       }
   }
   //method that is called in slowdown to make it easier
   private float adjust(float value, float rate) 
   {
       if (value < -0.25f) 
       {
       return value + rate;
       }
       if (value > 0.25f) 
       {
       return value - rate;
       }
       else
       {
       return 0;
       }
   }

   public void drawMe(float xDraw, float yDraw, int size, GraphicsContext gc) 
   {
       if (!isDead) 
       {   //drawing the player
           int radius = size / 2+1;
           gc.setFill(Color.BLACK);
           gc.fillOval(xDraw - radius, yDraw - radius, size + 2, size + 2);
           gc.setFill(Color.GREEN);
           gc.fillOval(xDraw - size/2, yDraw - size/2, size, size);
      }
       
      //drawing the score
      gc.setFill(Color.WHITE);
      gc.fillText("Score: " + currentScore, 10, 20);
      gc.fillText("High Score: " + bestScore, 10, 40);
   }
   
   public void checkCollision(ArrayList<Mine> bombs) 
   {
       for (Mine m : bombs) 
       {
           if (distance(m) <= getSize()/2 + m.getSize()/2) 
           {
               isDead = true;
               m.setBlown(true);
           }
       }
   }
   
   public boolean hasMovedGrid() 
   {
       if (lastCellX != cellX || lastCellY != cellY) 
       {
           lastCellX = cellX;
           lastCellY = cellY;
           return true;
       }
       else
       {
          lastCellX = cellX;
          lastCellY = cellY;
          return false;
       }
   }
   //changing the high score to be the newest 
   private void updateBestScore() 
   {
       if (currentScore > bestScore) 
       {
           bestScore = currentScore;
           try 
           {
               outputFile = new FileOutputStream("highScore.txt", false);
               outputWrite = new PrintWriter(outputFile);
               outputWrite.println(currentScore);
               outputWrite.close();
           } 
           catch (IOException e) 
           {}
       }
   }
   
   public int getScore() 
   { 
      return currentScore; 
   }
   public int getGridX() 
   { 
      return cellX; 
   }
   public int getGridY() 
   { 
      return cellY; 
   }
   public boolean hasDied() 
   { 
      return isDead; 
   }
}
   