import java.awt.*;

class Fruit{
    private int xMin, xMax, yMin, yMax;
    private Snake[] snakes;
    int x, y;

    Fruit(Snake s1, Snake s2){
        this.snakes = new Snake[]{s1, s2};
        xMin = 0; xMax = DuoSnake.FIELD_W;
        yMin = 0; yMax = DuoSnake.FIELD_H;
        newFruit();
    }

    Fruit(Snake s1, int xMin, int yMin, int xMax, int yMax){
        this.snakes = new Snake[]{s1};
        this.xMin = xMin; this.xMax = xMax;
        this.yMin = yMin; this.yMax = yMax;
        newFruit();
    }

    void newFruit(){
        boolean intersect;
        do{
            intersect = false;
            x = (int)(Math.random()*(xMax-xMin)) + xMin;
            y = (int)(Math.random()*(yMax-yMin)) + yMin;
            for(Snake s: snakes){
                for (Piece p : s.pieces) {
                    if (p == null) break;
                    if (p.x == x && p.y == y) {
                        intersect = true;
                        break;
                    }
                }
                if (intersect) break;
            }
        }while(intersect);
    }

    int eaten(){
        for(int i = 0; i < snakes.length; i++)
            if(snakes[i].hx == x && snakes[i].hy == y) return i+1;
        return -1;
    }

    void render(Graphics g){
        g.setColor(DuoSnake.FRUIT_COLOR);
        g.fillRect(Piece.BUFFER+DuoSnake.scale(x, true),
                Piece.BUFFER+DuoSnake.scale(y, false),
                Piece.DIM, Piece.DIM);
    }
}