import java.awt.*;

abstract class GameMode {
    Snake s1, s2;
    private DuoSnake ds;
    GameMode(DuoSnake ds, Snake s1, Snake s2){ this.ds = ds; this.s1 = s1; this.s2 = s2; }
    abstract int checkDeath();
    abstract void update(boolean[] s1keys, boolean[] s2keys);
    abstract void render(Graphics g);
    void kill(int k) {
        ds.stop();
        win(3-k); // the other player wins
    }
    void win(int w){
        int points = 0;
        if (w == DuoSnake.PLAYER1) points = s1.length;
        else points = s2.length;
        System.out.println("Player " + w + " wins with " + points + " points!");
    }
}

class GameMode0 extends GameMode{
    private static final int NUM_FRUIT = 3;
    private Fruit[] fruits;

    GameMode0(DuoSnake ds, Snake s1, Snake s2){
        super(ds, s1, s2);
        fruits = new Fruit[NUM_FRUIT];
        for(int i = 0;i  < NUM_FRUIT; i++) fruits[i] = new Fruit(s1, s2);
    }

    int checkDeath(){
        int x1 = s1.hx, x2 = s2.hx, y1 = s1.hy, y2 = s2.hy;
        if(x1 < 0 || x1 >= DuoSnake.FIELD_W || y1 < 0 || y1 >= DuoSnake.FIELD_H) return DuoSnake.PLAYER1;
        if(x2 < 0 || x2 >= DuoSnake.FIELD_W || y2 < 0 || y2 >= DuoSnake.FIELD_H) return DuoSnake.PLAYER2;
        for(int i = Snake.HEAD; i <= s1.length; i++){
            Piece p = s1.pieces[i];
            if(p.moving) {
                if (p.x == x1 && p.y == y1 && i != Snake.HEAD) return DuoSnake.PLAYER1;
                if (p.x == x2 && p.y == y2) return DuoSnake.PLAYER2;
            }
        }
        for(int i = Snake.HEAD; i <= s2.length; i++){
            Piece p = s2.pieces[i];
            if(p.moving) {
                if (p.x == x1 && p.y == y1) return DuoSnake.PLAYER1;
                if (p.x == x2 && p.y == y2 && i != Snake.HEAD) return DuoSnake.PLAYER2;
            }
        }
        return -1;
    }

    void update(boolean[] s1keys, boolean[] s2keys){
        s1.update(s1keys);
        s2.update(s2keys);
        for(Fruit f: fruits) {
            switch (f.eaten()) {
                case DuoSnake.PLAYER1:
                    s1.grow();
                    f.newFruit();
                    break;
                case DuoSnake.PLAYER2:
                    s2.grow();
                    f.newFruit();
                    break;
                default:
                    break;
            }
        }
        int d = checkDeath();
        if(d != -1) kill(d);
    }

    void render(Graphics g){
        for(Fruit f: fruits) f.render(g);
        s1.render(g);
        s2.render(g);
    }
}

class GameMode1 extends GameMode{
    private Fruit f1, f2;
    private static final int BARRIER = DuoSnake.FIELD_X_OFF + DuoSnake.PIXEL_WIDTH*DuoSnake.FIELD_W/2;

    GameMode1(DuoSnake ds, Snake s1, Snake s2){
        super(ds, s1, s2);
        f1 = new Fruit(s1, 0, 0, DuoSnake.FIELD_W/2, DuoSnake.FIELD_H);
        f2 = new Fruit(s2, DuoSnake.FIELD_W/2+1, 0, DuoSnake.FIELD_W, DuoSnake.FIELD_H);
    }

    int checkDeath(){
        int x1 = s1.hx, x2 = s2.hx, y1 = s1.hy, y2 = s2.hy;
        if(x1 < 0 || x1 >= DuoSnake.FIELD_W/2 || y1 < 0 || y1 >= DuoSnake.FIELD_H) return DuoSnake.PLAYER1;
        if(x2 < DuoSnake.FIELD_W/2+1 || x2 >= DuoSnake.FIELD_W || y2 < 0 || y2 >= DuoSnake.FIELD_H) return DuoSnake.PLAYER2;
        for(int i = 1; i <= s1.length; i++){
            Piece p = s1.pieces[i];
            if (p.moving && p.x == x1 && p.y == y1) return DuoSnake.PLAYER1;
        }
        for(int i = 1; i <= s2.length; i++){
            Piece p = s2.pieces[i];
            if (p.moving && p.x == x2 && p.y == y2) return DuoSnake.PLAYER2;
        }
        return -1;
    }

    void update(boolean[] s1keys, boolean[] s2keys){
        s1.update(s1keys);
        s2.update(s2keys);
        if(f1.eaten() != -1) {
            s1.grow();
            f1.newFruit();
        }
        if(f2.eaten() != -1) {
            s2.grow();
            f2.newFruit();
        }
        int d = checkDeath();
        if(d != -1) kill(d);
    }

    void render(Graphics g){
        g.setColor(DuoSnake.BG_COLOR);
        g.fillRect(BARRIER, DuoSnake.FIELD_Y_OFF, DuoSnake.PIXEL_WIDTH, DuoSnake.FIELD_H*DuoSnake.FIELD_W);
        f1.render(g);
        f2.render(g);
        s1.render(g);
        s2.render(g);
    }
}

