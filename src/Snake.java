import java.awt.*;
import java.awt.event.*;
class Snake {
    static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3, HEAD = 0;
    private static final int P1_START_X = 0, P1_START_Y = DuoSnake.FIELD_H/2,
                    P2_START_X = DuoSnake.FIELD_W-1, P2_START_Y = DuoSnake.FIELD_H/2,
                    P1_START_DIR = RIGHT, P2_START_DIR = LEFT, MAX_PIECES = 2048;
    private static final KeySet P1_KEYSET = new KeySet(KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S),
            P2_KEYSET = new KeySet(KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN);

    private Color c;
    private KeySet ks;

    int length = 0, hdir, hx, hy;
    Piece[] pieces;

    Snake(boolean isPlayer1){
        pieces = new Piece[MAX_PIECES];
        setup(isPlayer1);
    }

    private void setup(boolean p1){
        if(p1){
            c = DuoSnake.P1_COLOR;
            hdir = P1_START_DIR;
            hx = P1_START_X;
            hy = P1_START_Y;
            ks = P1_KEYSET;
        }else{
            c = DuoSnake.P2_COLOR;
            hdir = P2_START_DIR;
            hx = P2_START_X;
            hy = P2_START_Y;
            ks = P2_KEYSET;
        }
        pieces[HEAD] = new Piece(hx, hy, hdir);
        pieces[HEAD].moving = true;
    }

    void grow(){
        Piece last = pieces[length];
        pieces[++length] = new Piece(last.x, last.y, last.dir);
    }

    void update(boolean[] keys) {
        for(int i = length; i > HEAD; i--) {
            pieces[i].update(pieces[i-1]);
        }

        if(1 == (hdir & 1)){
            //moving vertically
            if(keys[ks.right]){
                hdir = RIGHT;
                keys[ks.right] = false;
            }else if(keys[ks.left]){
                hdir = LEFT;
                keys[ks.left] = false;
            }
        }else{
            //moving horizontally
            if(keys[ks.up]){
                hdir = UP;
                keys[ks.up] = false;
            }else if(keys[ks.down]){
                hdir = DOWN;
                keys[ks.down] = false;
            }
        }
        pieces[HEAD].dir = hdir;

        switch (hdir) {
            case RIGHT:   hx += 1;     break;
            case UP:      hy -= 1;     break;
            case LEFT:    hx -= 1;     break;
            case DOWN:    hy += 1;     break;
            default:            break;
        }
        pieces[HEAD].x = hx;
        pieces[HEAD].y = hy;
    }

    void render(Graphics g){
        g.setColor(c);
        for(Piece p: pieces) {
            if (p == null) break;
            p.render(g);
        }
    }
}

class Piece{
    static final int BUFFER = 1, DIM = DuoSnake.PIXEL_WIDTH - 2*BUFFER;
    boolean moving;
    int x, y, dir;
    Piece(int x, int y, int dir){ this.x = x; this.y = y; this.dir = dir; }
    void render(Graphics g){
        g.fillRect(BUFFER+DuoSnake.scale(x, true), BUFFER+DuoSnake.scale(y, false), DIM, DIM);
    }
    void update(Piece prev){
        dir = prev.dir;
        if(moving) {
            switch (dir) {
                case Snake.RIGHT:   x += 1;     break;
                case Snake.UP:      y -= 1;     break;
                case Snake.LEFT:    x -= 1;     break;
                case Snake.DOWN:    y += 1;     break;
                default:            break;
            }
        }else moving = true;
    }
}

class KeySet{
    int right, up, left, down;
    KeySet(int r, int u, int l, int d){ right = r; up = u; left = l; down = d; }
}


