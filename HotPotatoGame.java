import javax.swing.*;
import audio.AudioManager;
import audio.BGMManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HotPotatoGame extends JPanel implements KeyListener {
    enum GameState {
        WAITING, COUNTDOWN, PLAYING, ROUND_OVER, FINISHED
    }

    private GameState currentState = GameState.WAITING;

    // ── ข้อมูลเกมหลัก ──────────────────────────────────────────────────
    private Character targetGirl;
    private String targetName;
    private int totalPlayers;

    // ปรับให้เป็น Ratio (0.0 - 1.0) เพื่อขยายตามหน้าจอ
    private final double[] START_X_RATIO = {0.26, 0.73, 0.50}; // P1 ซ้าย, P2 ขวา, P3 กลาง
    private final double[] START_Y_RATIO = {0.72, 0.72, 0.28}; // ล่าง, ล่าง, บน

    private ArrayList<Integer> fallenOrder = new ArrayList<>();
    private ArrayList<Integer> finisherOrder = new ArrayList<>();

    private int countdownValue = 3;
    private Timer countdownTimer, gameLoopTimer, roundOverTimer;

    // ================= โหลดรูปภาพ =================
    private Image heartImage   = new ImageIcon("image\\miniGame\\HP.png").getImage();
    private Image player1Image = new ImageIcon("image\\miniGame\\H_p1.png").getImage();
    private Image player2Image = new ImageIcon("image\\miniGame\\H_p2.png").getImage();
    private Image player3Image = new ImageIcon("image\\miniGame\\H_p3.png").getImage();
    private Image bombImage    = new ImageIcon("image\\miniGame\\Boom.png").getImage();
    private Image deadImage    = new ImageIcon("image\\miniGame\\Die.png").getImage();

    // ================= ระบบระเบิด =================
    private int bombHolder = -1;
    private int bombTarget = -1;
    private double bombX, bombY;
    private boolean bombTraveling = false;
    private int hiddenBombTimer = 0;
    private int bombCooldown = 0;

    private Random random = new Random();

    // ─────────────────────────────────────────────
    class Player {
        int id;
        double x, y;
        boolean alive = false;
        boolean ready = false;
        boolean active = false;

        int lives = 3;
        boolean charging = false;
        int chargeTicks = 0;
        final int MAX_CHARGE = 15;

        Color color;
        Image img;
        int radius = 60;

        Player(int id, Color color, Image img) {
            this.id = id;
            this.color = color;
            this.img = img;
        }

        // เมธอดสำหรับอัปเดตตำแหน่งตามขนาดหน้าจอจริง
        void updateResponsivePos(int w, int h) {
            this.x = w * START_X_RATIO[id];
            this.y = h * START_Y_RATIO[id];
            this.radius = (int)(w * 0.046); // ปรับรัศมีตัวละครตามความกว้างจอ
        }

        void resetPlayer() {
            this.alive = true;
            this.lives = 3;
            this.charging = false;
            this.chargeTicks = 0;
        }
    }

    private Player p1, p2, p3;
    private ArrayList<Player> players = new ArrayList<>();

    public HotPotatoGame() {
        this(null, null, 3);
    }

    public HotPotatoGame(Character girl, String name, int playerCount) {
        this.targetGirl   = girl;
        this.targetName   = name;
        this.totalPlayers = playerCount;

        setPreferredSize(new Dimension(1280, 720));
        setFocusable(true);
        addKeyListener(this);

        p1 = new Player(0, Color.RED,                player1Image);
        p2 = new Player(1, Color.GREEN,              player2Image);
        p3 = new Player(2, new Color(50, 150, 255),  player3Image);

        players.add(p1);
        players.add(p2);
        players.add(p3);

        BGMManager.playBGM("BG_minigame.wav");

        countdownTimer = new Timer(1000, e -> {
            countdownValue--;
            if (countdownValue > 0) {
                AudioManager.playSound("umamusume_back.wav");
            } else if (countdownValue == 0) {
                AudioManager.playSound("umamusume_con.wav");
            } else {
                startNewRound();
                countdownTimer.stop();
            }
            repaint();
        });

        roundOverTimer = new Timer(2000, e -> {
            checkGameOver();
            if (currentState != GameState.FINISHED) {
                startNewRound();
            }
            roundOverTimer.stop();
        });

        gameLoopTimer = new Timer(16, e -> {
            if (currentState == GameState.PLAYING) updateGameLogic();
            repaint();
        });
        gameLoopTimer.start();
    }

    private void initGame() {
        int w = getWidth(), h = getHeight();
        if (p1.ready && totalPlayers >= 1) { p1.active = true; p1.resetPlayer(); p1.updateResponsivePos(w, h); }
        if (p2.ready && totalPlayers >= 2) { p2.active = true; p2.resetPlayer(); p2.updateResponsivePos(w, h); }
        if (p3.ready && totalPlayers >= 3) { p3.active = true; p3.resetPlayer(); p3.updateResponsivePos(w, h); }

        fallenOrder.clear();
        assignRandomBombHolder();
    }

    private void assignRandomBombHolder() {
        ArrayList<Integer> alivePlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).active && players.get(i).alive) alivePlayers.add(i);
        }
        if (!alivePlayers.isEmpty()) {
            bombHolder = alivePlayers.get(random.nextInt(alivePlayers.size()));
        }
    }

    private void startNewRound() {
        currentState = GameState.PLAYING;
        bombTraveling = false;
        bombTarget = -1;
        bombCooldown = 0;

        Player holder = players.get(bombHolder);
        bombX = holder.x;
        bombY = holder.y;

        hiddenBombTimer = 300 + random.nextInt(600);

        for (Player p : players) {
            p.charging = false;
            p.chargeTicks = 0;
        }
    }

    private void updateGameLogic() {
        int w = getWidth(), h = getHeight();
        if (bombCooldown > 0) bombCooldown--;

        for (Player p : players) {
            p.updateResponsivePos(w, h); // อัปเดตพิกัดตามขนาดจอทุกเฟรม
            if (p.charging) {
                p.chargeTicks++;
                if (p.chargeTicks > p.MAX_CHARGE) p.chargeTicks = p.MAX_CHARGE;
            }
        }

        if (bombTraveling) {
            Player target = players.get(bombTarget);
            double dx   = target.x - bombX;
            double dy   = target.y - bombY;
            double dist = Math.hypot(dx, dy);
            double speed = w * 0.02; // ความเร็วระเบิดปรับตามขนาดจอ

            if (dist < speed) {
                bombX = target.x;
                bombY = target.y;
                bombHolder = bombTarget;
                bombTraveling = false;
                bombCooldown = 5;
                AudioManager.playSound("hit.wav");
            } else {
                bombX += (dx / dist) * speed;
                bombY += (dy / dist) * speed;
            }
        } else {
            Player holder = players.get(bombHolder);
            bombX = holder.x;
            bombY = holder.y;
        }

        if (hiddenBombTimer > 0) hiddenBombTimer--;
        if (hiddenBombTimer <= 0 && !bombTraveling) triggerExplosion();
    }

    private void passBomb(int senderIndex) {
        Player sender = players.get(senderIndex);
        boolean isSkip = sender.chargeTicks >= sender.MAX_CHARGE;
        sender.charging = false;
        sender.chargeTicks = 0;

        int activeAliveCount = 0;
        for (Player p : players) {
            if (p.active && p.alive) activeAliveCount++;
        }
        if (activeAliveCount <= 2) isSkip = false;

        int nextIndex = findNextPlayer(senderIndex, isSkip);
        bombTarget = nextIndex;
        bombHolder = -1;
        bombTraveling = true;
        AudioManager.playSound("jump.wav");
    }

    private int findNextPlayer(int currentIndex, boolean skip) {
        int next = (currentIndex + 1) % players.size();
        while (!players.get(next).active || !players.get(next).alive) {
            next = (next + 1) % players.size();
        }
        if (skip) {
            next = (next + 1) % players.size();
            while (!players.get(next).active || !players.get(next).alive) {
                next = (next + 1) % players.size();
            }
        }
        return next;
    }

    private void triggerExplosion() {
        currentState = GameState.ROUND_OVER;
        AudioManager.playSound("boom.wav");

        int victimIndex = bombHolder;
        Player victim = players.get(victimIndex);

        victim.lives--;
        if (victim.lives <= 0) {
            victim.alive = false;
            fallenOrder.add(victim.id);
        }

        bombX = victim.x;
        bombY = victim.y;
        bombTraveling = false;

        if (victim.alive) {
            bombHolder = victimIndex;
        } else {
            assignRandomBombHolder();
        }

        roundOverTimer.start();
    }

    private void checkGameOver() {
        int aliveCount = 0;
        for (Player p : players) {
            if (p.active && p.alive) aliveCount++;
        }

        int activeCount = (p1.active ? 1 : 0) + (p2.active ? 1 : 0) + (p3.active ? 1 : 0);

        if ((activeCount > 1 && aliveCount <= 1) || (activeCount == 1 && aliveCount == 0)) {
            currentState = GameState.FINISHED;
            gameLoopTimer.stop();
            BGMManager.stopBGM();
            AudioManager.playSound("win.wav");

            for (Player p : players) {
                if (p.active && p.alive) fallenOrder.add(p.id);
            }
            finisherOrder = new ArrayList<>(fallenOrder);
            Collections.reverse(finisherOrder);

            int[] bonus = new int[totalPlayers > 0 ? totalPlayers : 3];

            for (int i = 0; i < finisherOrder.size(); i++) {
                int pIdx = finisherOrder.get(i);
                if (pIdx >= 0 && pIdx < bonus.length) {
                    bonus[pIdx] = (i == 0) ? 30 : (i == 1) ? 20 : 10;
                }
            }

            Timer delay = new Timer(2000, e -> {
                Window win = SwingUtilities.getWindowAncestor(this);
                if (win != null) win.dispose();
                SceneManager.switchScene(new ScoreBoard(targetGirl, targetName, bonus));
            });
            delay.setRepeats(false);
            delay.start();

        } else {
            startNewRound();
        }
    }

    private void drawHearts(Graphics2D g2d, int lives, int startX, int startY) {
        int hSize = (int)(getWidth() * 0.031); // ปรับขนาดหัวใจตามจอ
        for (int i = 0; i < lives; i++) {
            int x = startX + (i * (hSize + 5));
            if (heartImage != null && heartImage.getWidth(null) > 0) {
                g2d.drawImage(heartImage, x, startY, hSize, hSize, this);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillArc(x, startY, 20, 20, 0, 180);
                g2d.fillArc(x + 20, startY, 20, 20, 0, 180);
                int[] px = {x, x + 40, x + 20};
                int[] py = {startY + 10, startY + 10, startY + 35};
                g2d.fillPolygon(px, py, 3);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(40, 40, 60));
        g2d.fillRect(0, 0, w, h);

        // ── วาดผู้เล่น ────────────────────────────
        for (Player p : players) {
            p.updateResponsivePos(w, h); // อัปเดตตำแหน่งตามจอ
            if (p.active && p.alive) {
                if (bombHolder == p.id && currentState == GameState.PLAYING) {
                    g2d.setColor(new Color(255, 0, 0, 100));
                    int auraSize = p.radius + 10 + (int)(Math.sin(System.currentTimeMillis() / 100.0) * 8);
                    g2d.fillOval((int) p.x - auraSize, (int) p.y - auraSize, auraSize * 2, auraSize * 2);
                }

                if (p.img != null && p.img.getWidth(null) > 0) {
                    g2d.drawImage(p.img, (int) p.x - p.radius, (int) p.y - p.radius, p.radius * 2, p.radius * 2, this);
                } else {
                    g2d.setColor(p.color);
                    g2d.fillOval((int) p.x - p.radius, (int) p.y - p.radius, p.radius * 2, p.radius * 2);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawOval((int) p.x - p.radius, (int) p.y - p.radius, p.radius * 2, p.radius * 2);
                }

                g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.017)));
                g2d.setColor(Color.WHITE);
                String pLabel = "P" + (p.id + 1);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(pLabel, (int)p.x - (fm.stringWidth(pLabel) / 2), (int)p.y - p.radius - 10);

                if (p.charging) {
                    int barWidth = (int)(w * 0.06), barHeight = (int)(h * 0.014);
                    int barX = (int) p.x - (barWidth / 2);
                    int barY = (int) p.y + p.radius + 15;
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRect(barX, barY, barWidth, barHeight);
                    g2d.setColor(p.chargeTicks >= p.MAX_CHARGE ? Color.YELLOW : Color.ORANGE);
                    g2d.fillRect(barX, barY, (int)((double) p.chargeTicks / p.MAX_CHARGE * barWidth), barHeight);
                }

            } else if (p.active && !p.alive && currentState != GameState.FINISHED) {
                int deadSize = (int)(w * 0.078);
                if (deadImage != null && deadImage.getWidth(null) > 0) {
                    g2d.drawImage(deadImage, (int) p.x - deadSize/2, (int) p.y - deadSize/2, deadSize, deadSize, this);
                } else {
                    g2d.setColor(new Color(0, 0, 0, 100));
                    g2d.fillOval((int) p.x - 20, (int) p.y - 20, 40, 40);
                }
                g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.012)));
                g2d.setColor(Color.GRAY);
                String pLabel = "P" + (p.id + 1);
                g2d.drawString(pLabel, (int)p.x - (g2d.getFontMetrics().stringWidth(pLabel) / 2), (int)p.y - deadSize/2 - 5);
            }
        }

        // ── วาดระเบิด ─────────────────────────────
        if (currentState == GameState.PLAYING || currentState == GameState.ROUND_OVER) {
            int drawBombX = (int) bombX;
            int drawBombY = (int) bombY;
            int bSize = (int)(w * 0.078); // ขนาดระเบิดปรับตามจอ

            if (currentState == GameState.PLAYING && hiddenBombTimer < 180) {
                drawBombX += random.nextInt(9) - 4;
                drawBombY += random.nextInt(9) - 4;
            }

            if (currentState == GameState.ROUND_OVER) {
                g2d.setColor(Color.ORANGE);
                g2d.fillOval(drawBombX - bSize, drawBombY - bSize, bSize * 2, bSize * 2);
            } else {
                if (bombImage != null && bombImage.getWidth(null) > 0) {
                    g2d.drawImage(bombImage, drawBombX - bSize/2, drawBombY - bSize/2, bSize, bSize, this);
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(drawBombX - 25, drawBombY - 25, 50, 50);
                }
            }
        }

        // ── HUD ──────────────────────────────────
        if (currentState != GameState.WAITING) {
            g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.018)));
            if (p1.active) {
                g2d.setColor(p1.color);
                g2d.drawString("P1: ส่ง (A)", (int)(w * 0.03), (int)(h * 0.87));
                drawHearts(g2d, p1.lives, (int)(w * 0.03), (int)(h * 0.89));
            }
            if (p2.active) {
                g2d.setColor(p2.color);
                g2d.drawString("P2: ส่ง (J)", (int)(w * 0.82), (int)(h * 0.87));
                drawHearts(g2d, p2.lives, (int)(w * 0.82), (int)(h * 0.89));
            }
            if (p3.active) {
                g2d.setColor(p3.color);
                g2d.drawString("P3: ส่ง (^)", (int)(w * 0.44), (int)(h * 0.07));
                drawHearts(g2d, p3.lives, (int)(w * 0.43), (int)(h * 0.08));
            }
        }

        // ── Overlay UI ─────────────────────────────
        if (currentState == GameState.WAITING) {
            String keys = "P1(A)";
            if (totalPlayers >= 2) keys += "  |  P2(J)";
            if (totalPlayers >= 3) keys += "  |  P3(^)";
            drawOverlay(g2d, keys, "HOT POTATO: BOMB DASH", p1.ready, p2.ready, p3.ready, w, h);
        } else if (currentState == GameState.COUNTDOWN) {
            drawCountdown(g2d, w, h);
        } else if (currentState == GameState.FINISHED) {
            drawFinishScreen(g2d, w, h);
        }
    }

    private void drawOverlay(Graphics2D g2d, String keys, String title, boolean r1, boolean r2, boolean r3, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.ORANGE);
        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.039)));
        g2d.drawString(title, (int)(w * 0.25), (int)(h * 0.28));
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.PLAIN, (int)(w * 0.023)));
        g2d.drawString(keys, (int)(w * 0.35), (int)(h * 0.42));

        int c = (r1 ? 1 : 0) + (r2 ? 1 : 0) + (r3 ? 1 : 0);
        if (c >= 2 || (totalPlayers == 1 && c == 1)) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("กด ENTER เพื่อเริ่ม (" + c + " คน)", (int)(w * 0.35), (int)(h * 0.55));
        }

        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.018)));
        if (totalPlayers >= 1) { g2d.setColor(r1 ? Color.GREEN : Color.RED); g2d.drawString("P1: " + (r1 ? "Ready" : "Waiting"), (int)(w * 0.23), (int)(h * 0.69)); }
        if (totalPlayers >= 2) { g2d.setColor(r2 ? Color.GREEN : Color.RED); g2d.drawString("P2: " + (r2 ? "Ready" : "Waiting"), (int)(w * 0.46), (int)(h * 0.69)); }
        if (totalPlayers >= 3) { g2d.setColor(r3 ? Color.GREEN : Color.RED); g2d.drawString("P3: " + (r3 ? "Ready" : "Waiting"), (int)(w * 0.70), (int)(h * 0.69)); }
    }

    private void drawCountdown(Graphics2D g2d, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.11)));
        String t = countdownValue == 0 ? "START!" : String.valueOf(countdownValue);
        g2d.drawString(t, (int)(w * 0.3), (int)(h * 0.55));
    }

    private void drawFinishScreen(Graphics2D g2d, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, w, h);
        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.046)));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("ผู้ชนะคือ!", (int)(w * 0.39), (int)(h * 0.21));

        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.031)));
        Color[] textColors = {Color.RED, Color.GREEN, new Color(50, 150, 255)};

        for (int i = 0; i < finisherOrder.size(); i++) {
            int playerNum = finisherOrder.get(i);
            g2d.setColor(textColors[playerNum]);
            g2d.drawString("อันดับที่ " + (i + 1) + " : Player " + (playerNum + 1), (int)(w * 0.37), (int)(h * 0.36 + (i * h * 0.11)));
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.PLAIN, (int)(w * 0.018)));
        g2d.drawString("กำลังเตรียมหน้าสรุปคะแนนรวม...", (int)(w * 0.37), (int)(h * 0.83));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (currentState == GameState.WAITING) {
            if (key == KeyEvent.VK_A  && totalPlayers >= 1 && !p1.ready) { p1.ready = true; AudioManager.playSound("umamusume_con.wav"); }
            if (key == KeyEvent.VK_J  && totalPlayers >= 2 && !p2.ready) { p2.ready = true; AudioManager.playSound("umamusume_con.wav"); }
            if (key == KeyEvent.VK_UP && totalPlayers >= 3 && !p3.ready) { p3.ready = true; AudioManager.playSound("umamusume_con.wav"); }

            int readyCount = (p1.ready ? 1 : 0) + (p2.ready ? 1 : 0) + (p3.ready ? 1 : 0);
            if (key == KeyEvent.VK_ENTER && readyCount >= totalPlayers) {
                initGame();
                currentState = GameState.COUNTDOWN;
                AudioManager.playSound("umamusume_back.wav");
                countdownTimer.start();
            }
            return;
        }

        if (currentState == GameState.PLAYING && !bombTraveling && bombCooldown <= 0) {
            if (key == KeyEvent.VK_A  && bombHolder == 0 && p1.alive) p1.charging = true;
            if (key == KeyEvent.VK_J  && bombHolder == 1 && p2.alive) p2.charging = true;
            if (key == KeyEvent.VK_UP && bombHolder == 2 && p3.alive) p3.charging = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (currentState == GameState.PLAYING && !bombTraveling) {
            if (key == KeyEvent.VK_A  && p1.charging) passBomb(0);
            if (key == KeyEvent.VK_J  && p2.charging) passBomb(1);
            if (key == KeyEvent.VK_UP && p3.charging) passBomb(2);

            if (key == KeyEvent.VK_A)  p1.charging = false;
            if (key == KeyEvent.VK_J)  p2.charging = false;
            if (key == KeyEvent.VK_UP) p3.charging = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}