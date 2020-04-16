import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Teren extends JPanel implements ActionListener{
    private final int T_SIRINA = 700;
    private final int T_VISINA = 700;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int hrana_x;
    private int hrana_y;

    private boolean lijevo = false;
    private boolean desno = true;
    private boolean gore = false;
    private boolean dole = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image hrana;
    private Image glava;

    public Teren() {

        initTeren();
    }

    private void initTeren() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(T_SIRINA, T_VISINA));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/green_dot.png");
        ball = iid.getImage();

        ImageIcon iih = new ImageIcon("src/resources/orange_dot.png");
        hrana = iih.getImage();

        ImageIcon iig = new ImageIcon("src/resources/red_dot.png");
        glava = iig.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateHrana();

        timer = new Timer(DELAY, this);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(hrana, hrana_x, hrana_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(glava, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (T_SIRINA - metr.stringWidth(msg)) / 2, T_VISINA / 2);
    }

    private void checkHrana() {

        if ((x[0] == hrana_x) && (y[0] == hrana_y)) {

            dots++;
            locateHrana();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (lijevo) {
            x[0] -= DOT_SIZE;
        }

        if (desno) {
            x[0] += DOT_SIZE;
        }

        if (gore) {
            y[0] -= DOT_SIZE;
        }

        if (dole) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= T_VISINA) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= T_SIRINA) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateHrana() {

        int r = (int) (Math.random() * RAND_POS);
        hrana_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        hrana_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkHrana();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!desno)) {
                lijevo = true;
                gore = false;
                dole = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!lijevo)) {
                desno = true;
                gore = false;
                dole = false;
            }

            if ((key == KeyEvent.VK_UP) && (!dole)) {
                gore = true;
                desno = false;
                lijevo = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!gore)) {
                dole = true;
                desno = false;
                lijevo = false;
            }
        }
    }


}
