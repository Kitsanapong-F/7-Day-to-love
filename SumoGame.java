import javax.swing.*;

import audio.AudioManager;
import audio.BGMManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class SumoGame extends JPanel implements KeyListener {
    enum GameState {
        WAITING, COUNTDOWN, PLAYING, FINISHED
    }

    private GameState currentState = GameState.WAITING;

    // ── ข้อมูลเกมหลัก (คงเดิม 100%) ──────────────────────────────────
    private Character targetGirl;
    private String targetName;
    private int totalPlayers;

    // สัดส่วนเวทีซูโม่ (ใช้ Ratio เพื่อให้ขยายตาม MiniGameSelector)
    private final double ARENA_X_RATIO = 640.0 / 1280.0;
    private final double ARENA_Y_RATIO = 360.0 / 720.0;
    private final double ARENA_RADIUS_RATIO = 300.0 / 720.0;

    // จัดเก็บลำดับคนที่ตกเวทีเพื่อคำนวณอันดับตอนจบ
    private ArrayList<Integer> fallenOrder = new ArrayList<>();
    private ArrayList<Integer> finisherOrder = new ArrayList<>();

    private int countdownValue = 3;
    private Timer countdownTimer, gameLoopTimer;

    // ตัวแปรสำหรับจัดการการหน่วงเวลาก่อนจบเกม
    private boolean isEnding = false;
    private Timer endingTimer;

    // ================= โหลดรูปภาพ =================
    private Image heartImage   = new ImageIcon("image\\miniGame\\HP.png").getImage();
    private Image cheerImage   = new ImageIcon("image\\miniGame\\Sp\\Sp_1.png").getImage();
    private Image player1Image = new ImageIcon("image\\miniGame\\H_p1.png").getImage();
    private Image player2Image = new ImageIcon("image\\miniGame\\H_p2.png").getImage();
    private Image player3Image = new ImageIcon("image\\miniGame\\H_p3.png").getImage();
    private Image deadImage    = new ImageIcon("image\\miniGame\\Die.png").getImage();

    // ระบบกองเชียร์
    private int cheerYOffset = 0;
    private int cheerVelocity = 0;

    // ─────────────────────────────────────────────
    class Player {
        int id;
        double x, y;
        double startX_Ratio, startY_Ratio, startAngle;
        double vx = 0, vy = 0;
        double angle;
        double spinSpeed = 0.08;

        boolean alive = false;
        boolean ready = false;
        boolean active = false;

        int lives = 3;
        int invincibilityTicks = 0;

        boolean charging = false;
        int chargePower = 0;
        final int MAX_CHARGE = 40;

        Color color;
        Image img;
        int radius = 30;
        double mass = 1.0;

        Player(int id, Color color, Image img) {
            this.id = id;
            this.color = color;
            this.img = img;
        }

        // อัปเดตตำแหน่งและขนาดให้ Responsive
        void updateResponsivePos(int w, int h) {
            if (currentState == GameState.WAITING) {
                this.x = w * startX_Ratio;
                this.y = h * startY_Ratio;
            }
            this.radius = (int)(h * 0.0416); // สัดส่วนรัศมีตัวละคร
        }

        void resetPosition(double startX_R, double startY_R, double startAngle) {
            this.startX_Ratio = startX_R;
            this.startY_Ratio = startY_R;
            this.startAngle = startAngle;

            this.x = getWidth() * startX_Ratio;
            this.y = getHeight() * startY_Ratio;
            this.angle = startAngle;
            this.vx = 0;
            this.vy = 0;

            this.alive = true;
            this.lives = 3;
            this.invincibilityTicks = 0;
            this.charging = false;
            this.chargePower = 0;
            this.spinSpeed = 0.08;
        }

        void respawn() {
            this.x = getWidth() * startX_Ratio;
            this.y = getHeight() * startY_Ratio;
            this.vx = 0;
            this.vy = 0;
            this.angle = startAngle;
            this.charging = false;
            this.chargePower = 0;
            this.invincibilityTicks = 60;
        }

        void update() {
            if (!alive) return;
            if (invincibilityTicks > 0) invincibilityTicks--;

            if (charging) {
                chargePower++;
                if (chargePower > MAX_CHARGE) chargePower = MAX_CHARGE;
            } else {
                angle += spinSpeed;
            }

            x += vx;
            y += vy;
            vx *= 0.94;
            vy *= 0.94;
        }

        void dash() {
            charging = false;
            double force = 8 + ((double) chargePower / MAX_CHARGE) * 15;
            chargePower = 0;

            vx += Math.cos(angle) * force;
            vy += Math.sin(angle) * force;

            spinSpeed = -spinSpeed;
            AudioManager.playSound("jump.wav");
            triggerCheerJump();
        }
    }

    private Player p1, p2, p3;
    private ArrayList<Player> players = new ArrayList<>();

    // ─────────────────────────────────────────────
    public SumoGame() {
        this(null, null, 3);
    }

    public SumoGame(Character girl, String name, int playerCount) {
        this.targetGirl   = girl;
        this.targetName   = name;
        this.totalPlayers = playerCount;

        setPreferredSize(new Dimension(1280, 720));
        setFocusable(true);
        addKeyListener(this);

        p1 = new Player(1, Color.RED,                player1Image);
        p2 = new Player(2, Color.GREEN,              player2Image);
        p3 = new Player(3, new Color(50, 150, 255),  player3Image);

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
                currentState = GameState.PLAYING;
                countdownTimer.stop();
            }
            repaint();
        });

        endingTimer = new Timer(2000, e -> {
            endGame();
            endingTimer.stop();
        });

        gameLoopTimer = new Timer(16, e -> {
            if (currentState == GameState.PLAYING) updatePhysics();
            updateEffects();
            repaint();
        });
        gameLoopTimer.start();
    }

    // ─────────────────────────────────────────────
    private void initGame() {
        if (p1.ready && totalPlayers >= 1) {
            p1.active = true;
            p1.resetPosition((640.0 - 150.0)/1280.0, (360.0 + 80.0)/720.0, -Math.PI / 4);
        }
        if (p2.ready && totalPlayers >= 2) {
            p2.active = true;
            p2.resetPosition((640.0 + 150.0)/1280.0, (360.0 + 80.0)/720.0, -Math.PI * 3 / 4);
        }
        if (p3.ready && totalPlayers >= 3) {
            p3.active = true;
            p3.resetPosition(640.0/1280.0, (360.0 - 150.0)/720.0, Math.PI / 2);
        }

        fallenOrder.clear();
        isEnding = false;
    }

    // ─────────────────────────────────────────────
    private void updateEffects() {
        if (cheerVelocity != 0 || cheerYOffset < 0) {
            cheerYOffset += cheerVelocity;
            cheerVelocity += 2;
            if (cheerYOffset >= 0) {
                cheerYOffset = 0;
                cheerVelocity = 0;
            }
        }
    }

    private void triggerCheerJump() {
        if (cheerYOffset >= 0) cheerVelocity = -15;
    }

    // ─────────────────────────────────────────────
    private void updatePhysics() {
        int w = getWidth(), h = getHeight();
        double arenaCenterX = w * ARENA_X_RATIO;
        double arenaCenterY = h * ARENA_Y_RATIO;
        double arenaRadius = h * ARENA_RADIUS_RATIO;

        for (Player p : players) {
            if (p.active) p.update();
        }

        // ตรวจสอบการชนกัน (Physics 100%)
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player a = players.get(i);
                Player b = players.get(j);

                if (!a.active || !a.alive || !b.active || !b.alive) continue;
                if (a.invincibilityTicks > 0 || b.invincibilityTicks > 0) continue;

                double dx   = b.x - a.x;
                double dy   = b.y - a.y;
                double dist = Math.hypot(dx, dy);
                double minDist = a.radius + b.radius;

                if (dist < minDist) {
                    double overlap = minDist - dist;
                    double nx = dx / dist;
                    double ny = dy / dist;

                    a.x -= nx * overlap / 2;
                    a.y -= ny * overlap / 2;
                    b.x += nx * overlap / 2;
                    b.y += ny * overlap / 2;

                    double kx = (a.vx - b.vx);
                    double ky = (a.vy - b.vy);
                    double p  = 2.0 * (nx * kx + ny * ky) / (a.mass + b.mass);

                    if (p > 0) {
                        a.vx -= p * b.mass * nx;
                        a.vy -= p * b.mass * ny;
                        b.vx += p * a.mass * nx;
                        b.vy += p * a.mass * ny;

                        AudioManager.playSound("hit.wav");
                        triggerCheerJump();
                    }
                }
            }
        }

        // ตรวจสอบการตกเวที
        int aliveCount = 0;
        for (Player p : players) {
            if (p.active) {
                if (p.alive) {
                    double distFromCenter = Math.hypot(p.x - arenaCenterX, p.y - arenaCenterY);
                    if (distFromCenter > arenaRadius) {
                        p.lives--;
                        AudioManager.playSound("fall.wav");

                        if (p.lives > 0) {
                            p.respawn();
                            aliveCount++;
                        } else {
                            p.alive = false;
                            fallenOrder.add(p.id);
                        }
                    } else {
                        aliveCount++;
                    }
                }
            }
        }

        // ตรวจสอบจบเกม
        int activeCount = (p1.active ? 1 : 0) + (p2.active ? 1 : 0) + (p3.active ? 1 : 0);
        if (!isEnding && ((activeCount > 1 && aliveCount <= 1) || (activeCount == 1 && aliveCount == 0))) {
            isEnding = true;
            endingTimer.start();
        }
    }

    // ─────────────────────────────────────────────
    private void endGame() {
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
            int pIdx = finisherOrder.get(i) - 1;
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
    }

    // ─────────────────────────────────────────────
    private void drawHearts(Graphics2D g2d, int lives, int startX, int startY, int w) {
        int hSize = (int)(w * 0.03125);
        for (int i = 0; i < lives; i++) {
            int x = startX + (i * (int)(w * 0.035));
            if (heartImage != null && heartImage.getWidth(null) > 0) {
                g2d.drawImage(heartImage, x, startY, hSize, hSize, this);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillArc(x, startY, 20, 20, 0, 180);
                g2d.fillArc(x + 20, startY, 20, 20, 0, 180);
                int[] px = { x, x + 40, x + 20 };
                int[] py = { startY + 10, startY + 10, startY + 35 };
                g2d.fillPolygon(px, py, 3);
            }
        }
    }

    // ─────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double arenaCenterX = w * ARENA_X_RATIO;
        double arenaCenterY = h * ARENA_Y_RATIO;
        double arenaRadius = h * ARENA_RADIUS_RATIO;

        // ── พื้นหลัง + เวทีซูโม่ ──────────────────
        g2d.setColor(new Color(30, 144, 255));
        g2d.fillRect(0, 0, w, h);

        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)(arenaCenterX - arenaRadius - 5), (int)(arenaCenterY - arenaRadius - 5),
                     (int)(arenaRadius * 2) + 10, (int)(arenaRadius * 2) + 10);
        g2d.setColor(new Color(205, 133, 63));
        g2d.fillOval((int)(arenaCenterX - arenaRadius), (int)(arenaCenterY - arenaRadius),
                     (int)(arenaRadius * 2), (int)(arenaRadius * 2));

        // ── กองเชียร์ ─────────────────
        if (currentState == GameState.PLAYING) {
            int cheerX = (int)(w * 0.765), cheerY = (int)(h * 0.72) + cheerYOffset;
            int cheerW = (int)(w * 0.195), cheerH = (int)(h * 0.194);
            if (cheerImage != null && cheerImage.getWidth(null) > 0) {
                g2d.drawImage(cheerImage, cheerX, cheerY, cheerW, cheerH, this);
            }
        }

        // ── วาดผู้เล่น ────────────────────────────
        for (Player p : players) {
            p.updateResponsivePos(w, h);
            if (p.active && p.alive) {
                if (p.invincibilityTicks > 0 && (p.invincibilityTicks / 5) % 2 == 0) continue;

                if (p.img != null && p.img.getWidth(null) > 0) {
                    g2d.drawImage(p.img, (int) p.x - p.radius, (int) p.y - p.radius,
                                  p.radius * 2, p.radius * 2, this);
                } else {
                    g2d.setColor(p.color);
                    g2d.fillOval((int) p.x - p.radius, (int) p.y - p.radius, p.radius * 2, p.radius * 2);
                }

                java.awt.geom.AffineTransform oldTx = g2d.getTransform();
                g2d.translate(p.x, p.y);
                g2d.rotate(p.angle);
                int arrowSize = (int)(h * 0.0347);
                int[] arrowX = { arrowSize, 5, 5 };
                int[] arrowY = { 0, -10, 10 };
                g2d.setColor(Color.WHITE);
                g2d.fillPolygon(arrowX, arrowY, 3);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawPolygon(arrowX, arrowY, 3);
                g2d.setTransform(oldTx);

                if (p.charging) {
                    g2d.setColor(new Color(255, 255, 0, 150));
                    int chargeRadius = p.radius + 10 + (p.chargePower / 2);
                    g2d.drawOval((int) p.x - chargeRadius, (int) p.y - chargeRadius,
                                 chargeRadius * 2, chargeRadius * 2);
                }

            } else if (p.active && !p.alive && currentState != GameState.FINISHED) {
                int deadSize = (int)(h * 0.111);
                if (deadImage != null && deadImage.getWidth(null) > 0) {
                    g2d.drawImage(deadImage, (int) p.x - deadSize/2, (int) p.y - deadSize/2, deadSize, deadSize, this);
                }
            }
        }

        // ── UI ─────────────────────────────────────
        if (currentState == GameState.WAITING) {
            String keys = "P1(A)";
            if (totalPlayers >= 2) keys += "  |  P2(J)";
            if (totalPlayers >= 3) keys += "  |  P3(^)";
            drawOverlay(g2d, keys, "รอผู้เล่นพร้อม...", p1.ready, p2.ready, p3.ready, w, h);

        } else if (currentState == GameState.COUNTDOWN
                || currentState == GameState.PLAYING
                || currentState == GameState.FINISHED) {
            g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.01875)));
            if (p1.active) {
                g2d.setColor(Color.WHITE);
                g2d.drawString("P1: กดค้าง A เพื่อพุ่ง", (int)(w*0.015), (int)(h*0.07));
                drawHearts(g2d, p1.lives, (int)(w*0.015), (int)(h*0.08), w);
            }
            if (p2.active) {
                g2d.setColor(Color.WHITE);
                g2d.drawString("P2: กดค้าง J เพื่อพุ่ง", (int)(w*0.8), (int)(h*0.07));
                drawHearts(g2d, p2.lives, (int)(w*0.8), (int)(h*0.08), w);
            }
            if (p3.active) {
                g2d.setColor(Color.WHITE);
                g2d.drawString("P3: กดค้าง ^ เพื่อพุ่ง", (int)(w*0.015), (int)(h*0.89));
                drawHearts(g2d, p3.lives, (int)(w*0.015), (int)(h*0.9), w);
            }
        }

        if (currentState == GameState.COUNTDOWN) {
            drawCountdown(g2d, w, h);
        } else if (currentState == GameState.FINISHED) {
            drawFinishScreen(g2d, w, h);
        }
    }

    // ─────────────────────────────────────────────
    private void drawOverlay(Graphics2D g2d, String keys, String title, boolean r1, boolean r2, boolean r3, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.031)));
        g2d.drawString(title, (int)(w * 0.375), (int)(h * 0.27));
        g2d.setFont(new Font("Tahoma", Font.PLAIN, (int)(w * 0.023)));
        g2d.drawString(keys, (int)(w * 0.35), (int)(h * 0.416));

        int c = (r1 ? 1 : 0) + (r2 ? 1 : 0) + (r3 ? 1 : 0);
        if (c >= 2) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("กด ENTER เพื่อเริ่ม (" + c + " คน)", (int)(w * 0.35), (int)(h * 0.55));
        }

        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.018)));
        if (totalPlayers >= 1) { g2d.setColor(r1 ? Color.GREEN : Color.RED); g2d.drawString("P1: " + (r1 ? "Ready" : "Waiting"), (int)(w * 0.23), (int)(h * 0.69)); }
        if (totalPlayers >= 2) { g2d.setColor(r2 ? Color.GREEN : Color.RED); g2d.drawString("P2: " + (r2 ? "Ready" : "Waiting"), (int)(w * 0.46), (int)(h * 0.69)); }
        if (totalPlayers >= 3) { g2d.setColor(r3 ? Color.GREEN : Color.RED); g2d.drawString("P3: " + (r3 ? "Ready" : "Waiting"), (int)(w * 0.7), (int)(h * 0.69)); }
    }

    private void drawCountdown(Graphics2D g2d, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.117)));
        String t = countdownValue == 0 ? "FIGHT!" : String.valueOf(countdownValue);
        g2d.drawString(t, (int)(w * 0.31), (int)(h * 0.55));
    }

    private void drawFinishScreen(Graphics2D g2d, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, w, h);

        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.046)));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("ผู้ชนะคือ!", (int)(w * 0.39), (int)(h * 0.208));

        g2d.setFont(new Font("Tahoma", Font.BOLD, (int)(w * 0.031)));
        Color[] textColors = { Color.WHITE, Color.RED, Color.GREEN, new Color(100, 150, 255) };

        for (int i = 0; i < finisherOrder.size(); i++) {
            int playerNum = finisherOrder.get(i);
            g2d.setColor(textColors[playerNum]);
            g2d.drawString("อันดับที่ " + (i + 1) + " : Player " + playerNum, (int)(w * 0.375), (int)(h * 0.36 + (i * h * 0.11)));
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.PLAIN, (int)(w * 0.018)));
        g2d.drawString("กำลังเตรียมหน้าสรุปคะแนนรวม...", (int)(w * 0.375), (int)(h * 0.83));
    }

    // ─────────────────────────────────────────────
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (currentState == GameState.WAITING) {
            if (key == KeyEvent.VK_A  && totalPlayers >= 1 && !p1.ready) {
                p1.ready = true; AudioManager.playSound("umamusume_con.wav");
            }
            if (key == KeyEvent.VK_J  && totalPlayers >= 2 && !p2.ready) {
                p2.ready = true; AudioManager.playSound("umamusume_con.wav");
            }
            if (key == KeyEvent.VK_UP && totalPlayers >= 3 && !p3.ready) {
                p3.ready = true; AudioManager.playSound("umamusume_con.wav");
            }

            int readyCount = (p1.ready ? 1 : 0) + (p2.ready ? 1 : 0) + (p3.ready ? 1 : 0);
            if (readyCount == 3 || (key == KeyEvent.VK_ENTER && readyCount >= 2)) {
                initGame();
                currentState = GameState.COUNTDOWN;
                AudioManager.playSound("umamusume_back.wav");
                countdownTimer.start();
            }
            repaint();
            return;
        }

        if (currentState == GameState.PLAYING) {
            if (key == KeyEvent.VK_A  && p1.active && p1.alive && !p1.charging) p1.charging = true;
            if (key == KeyEvent.VK_J  && p2.active && p2.alive && !p2.charging) p2.charging = true;
            if (key == KeyEvent.VK_UP && p3.active && p3.alive && !p3.charging) p3.charging = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (currentState == GameState.PLAYING) {
            if (key == KeyEvent.VK_A  && p1.charging) p1.dash();
            if (key == KeyEvent.VK_J  && p2.charging) p2.dash();
            if (key == KeyEvent.VK_UP && p3.charging) p3.dash();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}