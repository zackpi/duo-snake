import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class DuoSnake extends Canvas implements KeyListener{
    private BufferedImage img;
    private boolean[] keys;
    private boolean running, gamemode = false;
    private GameMode gm;
    private Client client = null;
    private Server server = null;

    private static final int FPS = 16;
    static final int PIXEL_WIDTH = 10, FIELD_W = 64, FIELD_H = 32, WIDTH = 720, HEIGHT = 480,
                    FIELD_X_OFF = 32, FIELD_Y_OFF = 32, PLAYER1 = 1, PLAYER2 = 2;
    static final Color P1_COLOR = new Color(100, 100, 200),
                        P2_COLOR = new Color(100,200, 100),
                        FRUIT_COLOR = new Color(200, 100, 100),
                        FIELD_COLOR = new Color(200, 200, 200),
                        BG_COLOR = new Color(100, 100, 100);

    private DuoSnake(){ new DuoSnakeWindow(this); }

    void start(){
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        keys = new boolean[256];
        addKeyListener(this);
        createBufferStrategy(3);

        Snake p1Snake = new Snake(true);
        Snake p2Snake = new Snake(false);
        if(gamemode) gm = new GameMode1(this, p1Snake, p2Snake);
        else gm = new GameMode0(this, p1Snake, p2Snake);

        running = true;
        run();
    }

    private void update() {
        gm.update(keys, keys);
    }

    private void render(){
        Graphics g = img.getGraphics();
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(FIELD_COLOR);
        g.fillRect(FIELD_X_OFF, FIELD_Y_OFF, FIELD_W*PIXEL_WIDTH, FIELD_H*PIXEL_WIDTH);

        gm.render(g);

        this.getGraphics().drawImage(img, 0, 0, null);
        g.dispose();
    }

    private void run(){
        long targetTime = System.nanoTime();
        while(running){
            targetTime += 1000000000 / FPS;
            update();
            render();
            while(System.nanoTime() < targetTime){/* NOP */}
        }
        System.exit(0);
    }

    void stop(){ running = false; }

    static int scale(int val, boolean isX){
        if(isX) return FIELD_X_OFF + PIXEL_WIDTH*val;
        else return FIELD_Y_OFF + PIXEL_WIDTH*val;
    }

    public void keyPressed(KeyEvent ke){
        int k = ke.getKeyCode();
        if(k < 256) keys[k] = true;
    }
    public void keyReleased(KeyEvent ke){
        int k = ke.getKeyCode();
        if(k < 256) keys[k] = false;
    }
    public void keyTyped(KeyEvent ke){}

    public static void main(String[] args){
        new DuoSnake();
    }
}

class DuoSnakeWindow{
    DuoSnakeWindow(DuoSnake ds){
        JFrame f = new JFrame("DuoSnake");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(DuoSnake.WIDTH, DuoSnake.HEIGHT);
        f.setLocation(DuoSnake.WIDTH, DuoSnake.HEIGHT);
        f.setResizable(false);
        f.add(ds);
        f.setVisible(true);
        ds.start();
    }
}