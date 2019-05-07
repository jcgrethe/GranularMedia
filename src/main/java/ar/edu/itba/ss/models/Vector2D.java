package ar.edu.itba.ss.models;

public class Vector2D {
    private Double x;
    private Double y;

    public Vector2D(){
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vector2D(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2D(Vector2D base){
        this.x = base.x;
        this.y = base.y;
    }


    public Vector2D add(Double magnitude){
        return new Vector2D(this.x + magnitude, this.y + magnitude);
    }

    public Vector2D multiply(Double magnitude){
        return new Vector2D(this.x * magnitude, this.y * magnitude);
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Vector2D add(Vector2D vector2D){
        return new Vector2D(this.x + vector2D.x, this.y + vector2D.y);
    }
}
