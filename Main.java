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


public class Main extends Application
{
   FlowPane fp;
   Canvas theCanvas = new Canvas(600,600);
   GraphicsContext gc;
   
   Player thePlayer = new Player(300,300);
   ArrayList<Mine> mines = new ArrayList<Mine>();

   public void start(Stage stage)
   {
      fp = new FlowPane();
      fp.getChildren().add(theCanvas);
      gc = theCanvas.getGraphicsContext2D();
      drawBackground(300,300,gc);
      thePlayer.draw(300,300,gc,true);
      
      //implement animationHandler
      AnimationHandler ta = new AnimationHandler();
      ta.start();
      
      //set keyhandlers
      theCanvas.setOnKeyPressed(new KeyPressedHandler());
      theCanvas.setOnKeyReleased(new KeyReleasedHandler());
      
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      spawn(83,3);
      
      theCanvas.requestFocus();
      
   }
      
   //random for probabilities
   Random rand = new Random();
   public void spawn(int cellX, int cellY)
   {
      //calculating the distance from the origin
      int distFromOrigin = (int)Math.sqrt(Math.pow((double)cellX*100 - 300.0,2)+Math.pow((double)cellY*100 - 300.0,2));
      int n = distFromOrigin/1000;
      
      //loop n number of times and try and spawn a mine
      for(int i = 0; i < n; i++)
      {
         //get a random numnber from 0 to 9
         int randFrom1to10 = rand.nextInt(1,11);
         //if the number is greater than 2 (30% chance);
         if(randFrom1to10 <= 3)
         {
            //spawn a mine within the 100x100 grid starting at gridX*100 and gridY*100
            int posX = cellX*100 + (int)(rand.nextFloat()*100);
            int posY = cellY*100 + (int)(rand.nextFloat()*100);
            mines.add(new Mine(posX, posY));
         }
      }
      
   }
   
   public void newGrid()
   {
      //called every time the player reaches a new grid
      if(thePlayer.hasMovedGrid())
      {
         //spawning mines
         //min = grid coordinates up and to the left of player, 4 away
         //max = grid coordinates down and to the right of player, 3 away
         int gridLeft = thePlayer.getGridX()-5;
         int gridTop = thePlayer.getGridY()-5;
         int gridRight = thePlayer.getGridX()+4;
         int gridBottom = thePlayer.getGridY()+4;
         /*loops through the grid of grids from minX,minY to maxX,maxY, and will only 
            attempt to spawn if its one of the border grids*/
         for(int i = gridLeft; i <= gridRight; i++)
         {
            for(int j = gridTop; j <= gridBottom; j++)
            {
               if(i == gridLeft || i == gridRight || j == gridTop || j == gridBottom)
                  spawn(i,j);
            }
         }
         
         //removing mines
         for(int i = 0; i < mines.size(); i++)
         {
            if(thePlayer.distance(mines.get(i)) > 800)
               mines.remove(i);
         }
      }
   }
   
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);
         
         //check for a new grid
         newGrid();
         
         //draw background
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 
         
         //draw mines
         for(int i = 0; i < mines.size(); i++)
         {
            if(!mines.get(i).getBlown()) // draw mine if it is not blown up
               mines.get(i).draw(thePlayer.getX(), thePlayer.getY(), gc, false);
         }
         

            //draw player and update functions if player is alive
            thePlayer.draw(300,300,gc,true); 
            if(!thePlayer.hasDied()) 
               thePlayer.update(left,right,up,down);//act takes in the key boolean values and uses them for movement
            thePlayer.checkCollision(mines);
      }
   }
   
   //Key Handling --
   //up down left right booleans to handle the movemet and acceleration and decleration when unpressed
   boolean up = false, down = false, left = false, right = false;
   public class KeyPressedHandler implements EventHandler<KeyEvent>
   {
      public void handle(KeyEvent event)
      {
         if (event.getCode() == KeyCode.W) 
         {
            up = true;
         }
         if (event.getCode() == KeyCode.A) 
         {
            left = true;
         }
         if (event.getCode() == KeyCode.S) 
         {
            down = true;
         }
         if (event.getCode() == KeyCode.D)  
         {
            right = true;
         }
      }
   } //setting the booleans to that the player slows down when the keys are released
   public class KeyReleasedHandler implements EventHandler<KeyEvent>
   {
      public void handle(KeyEvent event)
      {
         if (event.getCode() == KeyCode.W) 
         {
            up = false;
         }
         if (event.getCode() == KeyCode.A) 
         {
            left = false;
         }
         if (event.getCode() == KeyCode.S) 
         {
            down = false;
         }
         if (event.getCode() == KeyCode.D)  
         {
            right = false;
         }
      }
   }

   
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	   //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	   //figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   
   /*public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);
         
         //USE THIS CALL ONCE YOU HAVE A PLAYER
         //drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 


	      //example calls of draw - this should be the player's call for draw
         //thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter.

         //example call of a draw where m is a non-player object. Note that you are passing the player's position in and not m's position.
         //m.draw(thePlayer.getX(),thePlayer.getY(),gc,false);
         
      }
   }*/

   public static void main(String[] args)
   {
      launch(args);
   }
}

