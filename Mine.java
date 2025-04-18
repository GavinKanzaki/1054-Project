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

public class Mine extends DrawableObject 
{   
    //instance variables
    private float pulseColor, phase;
    private boolean exploded = false;
    private Random randomizer = new Random();

    //constructor, that takes in position
    public Mine(float posX, float posY) 
    {
        super(posX, posY, 9);
        phase = randomizer.nextFloat(0,(float)Math.PI * 2f);
        updatePulse();
    }
    //drawing its self
    public void drawMe(float xDraw, float yDraw, int diameter, GraphicsContext context) 
    {
        int half = diameter / 2;
        context.setFill(Color.BLACK);
        context.fillOval(xDraw - half - 1, yDraw - half - 1, diameter + 2, diameter + 2);

        updatePulse();
        context.setFill(new Color(1, pulseColor, pulseColor, 1));
        context.fillOval(xDraw - half, yDraw - half, diameter, diameter);
    }

    public void setBlown(boolean status) 
    {
        exploded = status;
    }

    public boolean getBlown() 
    {
        return exploded;
    }

    private void updatePulse() 
    {
        phase += 0.02f;
        pulseColor = (float)Math.pow(Math.sin(phase), 2);
    }
}
