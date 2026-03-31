package fr.cpe;

import javafx.scene.paint.Color;

/**
 * Simple model class representing a ball with position, velocity, and color.
 */
public class Ball {
    public double x;
    public double y;
    public double dx;
    public double dy;
    private Color color;

    public Ball(double x, double y, double dx, double dy, Color color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
