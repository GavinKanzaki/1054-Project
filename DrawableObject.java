import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public abstract class DrawableObject
{
   public DrawableObject(float x, float y, int size)
   {
      this.x = x;
      this.y = y;
      this.size = size;
   }

   //positions
   private float x;
   private float y;
   
   //size (I added this so i could vary the sizes)
   private int size;
   
   //takes the position of the player and calls draw me with appropriate positions
   public void draw(float playerx, float playery, GraphicsContext gc, boolean isPlayer)
   {
      //the 300,300 places the player at 300,300, if you want to change it you will have to modify it here
      
      if(isPlayer)
         drawMe(playerx,playery,size,gc);
      else
         drawMe(-playerx+300+x,-playery+300+y,size,gc);
   }
   
   //this method you implement for each object you want to draw. Act as if the thing you want to draw is at x,y.
   //NOTE: DO NOT CALL DRAWME YOURSELF. Let the the "draw" method do it for you. I take care of the math in that method for a reason.
   public abstract void drawMe(float x, float y, int size, GraphicsContext gc);
   public void act()
   {
   }
   
   public float getX(){return x;}
   public float getY(){return y;}
   public int getSize(){return size;}
   public void setX(float x_){x = x_;}
   public void setY(float y_){y = y_;}
   public void setSize(int size_){size = size_;}

   
   public double distance(DrawableObject other)
   {
      return (Math.sqrt((other.x-x)*(other.x-x) +  (other.y-y)*(other.y-y)   ));
   }
}