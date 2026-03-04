import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import audio.AudioManager;
import audio.BGMManager;

/**
 * playmain: คลาสหลักสำหรับหน้าจอเกมเพลย์
 * ปรับปรุงระบบตัดเข้ามินิเกมเมื่อจบวันที่ 7 และรันฉากจบต่อเนื่องจนครบทุกคน
 */
public class playmain extends BaseFrame {

    private final String[] GOOD_ENDING_IMAGES = {
    "4dd1e007-648f-448d-8ccb-2a0d4d2885d0.png", // ภาพของ Akari (ใส่ชื่อเต็ม)
    "Gemini_Generated_Image_bgkyu3bgkyu3bgky.png", // ภาพของ Reina (ใส่ชื่อเต็ม)
    "Gemini_Generated_Image_yhiutiyhiutiyhiu.png" // ภาพของ Shiori (ใส่ชื่อเต็ม)
};

    private final String BAD_ENDING_IMAGE = "Gemini_Generated_Image_f8nd7jf8nd7jf8nd.png";
    
    // --- ระบบผู้เล่น (Multiplayer) ---
    private int currentPlayer = 0;
    private int totalPlayers;
    private int[] playerAP; 
    private final int MAX_AP = 3;
    private boolean hasDoneActionThisTurn = false;

    // --- UI Components ---
    private JPanel transitionPanel, textWindow, choicePanel;
    private JLabel transitionLabel, nameLabel, pTurnLabel, apLabel;
    private JTextArea dialogueArea;
    private CharacterPanel spritePanel;
    private JButton giftBtn, datingBtn, nextBtn;

    // --- สถานะเกม ---
    private int pointer = 0; 
    private Character currentGirl; 
    private int currentDay = 1; 

    private DialogueLine[] currentQueue;
    private boolean isWaitingForResponse = false;
    private Timer typewriterTimer;
    private boolean isResponseMode = false;
    private int nextDayTarget = 0;

    // ระบบจัดการคิวฉากจบ
    private List<Integer> endingPlayerQueue;
    private String endingGirlName;
    
    public playmain(Character selectedGirl, int players) {
        super("7 Days to Love - " + (selectedGirl != null ? selectedGirl.getName() : "Story"));
        this.currentGirl = selectedGirl;
        this.totalPlayers = players;
        this.playerAP = new int[players];
        for(int i = 0; i < players; i++) playerAP[i] = 0; 

        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg");
        initGameUI();
        setupZOrder(); 
        startTurn();
    }

    public int getTotalPlayers() { return this.totalPlayers; }
    public int getCurrentDay() { return this.currentDay; }
    public void setNextDayTarget(int t) { this.nextDayTarget = t; }

    // --- ระบบแสดงฉากจบต่อเนื่อง (เรียกจาก StoryManager) ---
    public void startEndingSequence(List<Integer> queue, String girlName) {
        this.endingPlayerQueue = queue;
        this.endingGirlName = girlName;
        showNextPlayerEnding();
    }

    private void showNextPlayerEnding() {
    if (endingPlayerQueue == null || endingPlayerQueue.isEmpty()) {
        BGMManager.stopBGM();
        SceneManager.switchScene(new StartGame());
        this.dispose();
        return;
    }

    int pIdx = endingPlayerQueue.remove(0);
    int score = currentGirl.getScores()[pIdx];
    int playerNum = pIdx + 1;

    if (pTurnLabel != null) {
        pTurnLabel.setText("PLAYER " + playerNum);
        pTurnLabel.setForeground(Color.CYAN); // ตั้งสีฟ้าตามรูป
    }
    
    // 1. หาว่าใครคือคนที่ได้คะแนนสูงสุดในกลุ่ม
    int[] allScores = currentGirl.getScores();
    int highestScore = -1;
    int topPlayerIdx = -1;
    for (int i = 0; i < totalPlayers; i++) {
        if (allScores[i] > highestScore) {
            highestScore = allScores[i];
            topPlayerIdx = i;
        }
    }

    // 2. กำหนดเกณฑ์คะแนน (Threshold)
    int threshold = (endingGirlName.equalsIgnoreCase("Akari")) ? 80 : 
                    (endingGirlName.equalsIgnoreCase("Reina")) ? 80 : 
                    (endingGirlName.equalsIgnoreCase("Shiori")) ? 80 : 75;

    // 3. ตัดสินฉากจบ: ต้องเป็นคนที่คะแนนสูงสุด (topPlayerIdx) และต้องถึงเกณฑ์ด้วย
    boolean isWinnerAndPassed = (pIdx == topPlayerIdx) && (score >= threshold);

   showDayTransition(playerNum, "ENDING: PLAYER " + playerNum, () -> {
    // ซ่อนตัวละคร Sprite ปกติเพื่อให้เห็นภาพ CG ฉากหลังได้ชัดเจน
    if (spritePanel != null) spritePanel.setVisible(false);

    if (isWinnerAndPassed) {
        // --- 🟢 กรณีได้ Good Ending ---
        BGMManager.playBGM("Blue_Archive_Aira.wav");
        
        // ใช้การต่อชื่อไฟล์อัตโนมัติ (เช่น image\goodending\akari_happy.png) 
        // มั่นใจว่าได้เปลี่ยนชื่อไฟล์ในโฟลเดอร์ให้เป็นชื่อตัวละครตามด้วย _happy.png แล้ว
        String happyImgPath = "image\\goodending\\" + endingGirlName.toLowerCase() + "_happy.png";
        setBackgroundImage(happyImgPath);
        
        if (endingGirlName.equalsIgnoreCase("Akari")) setDialogueQueue(endingData.getAkariGoodEnding(playerNum));
        else if (endingGirlName.equalsIgnoreCase("Reina")) setDialogueQueue(endingData.getReinaGoodEnding(playerNum));
        else if (endingGirlName.equalsIgnoreCase("Shiori")) setDialogueQueue(endingData.getShioriGoodEnding(playerNum));
        
    } else {
        // --- 🔴 กรณีได้ Bad Ending ---
        BGMManager.playBGM("Skyfall.wav");
        
        // ใช้รูปภาพที่เจนจาก Gemini สำหรับฉากจบที่ผิดหวัง
        setBackgroundImage("image\\bad ending\\Gemini_Generated_Image_f8nd7jf8nd7jf8nd.png");
        
        if (endingGirlName.equalsIgnoreCase("Akari")) setDialogueQueue(endingData.getAkariBadEnding());
        else if (endingGirlName.equalsIgnoreCase("Reina")) setDialogueQueue(endingData.getReinaBadEnding());
        else if (endingGirlName.equalsIgnoreCase("Shiori")) setDialogueQueue(endingData.getShioriBadEnding());
    }

    // บังคับให้ UI อัปเดตการแสดงผลภาพใหม่
    mainPanel.revalidate();
    mainPanel.repaint();
    });
}

    private void initGameUI() {
        spritePanel = new CharacterPanel("");
        addComponent(spritePanel, 540, 100, 200, 600); 

       JPanel apContainer = new JPanel(null) {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // วาดพื้นหลังสีดำโปร่งแสง
        g2d.setColor(new Color(0, 0, 0, 160)); 
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        // วาดเส้นขอบสีชมพูแดงจางๆ ให้เข้ากับสีตัวอักษร
        g2d.setColor(new Color(255, 80, 80, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
        
        g2d.dispose();
    }
};
    apContainer.setOpaque(false);
    // ปรับตำแหน่งและขนาดให้ครอบคลุมตัวอักษร
    addComponent(apContainer, 25, 15, 180, 55); 

    // ปรับปรุง apLabel เดิม
    apLabel = new JLabel("AP: 0", SwingConstants.CENTER); // ตั้งกึ่งกลาง
    apLabel.setFont(new Font("Tahoma", Font.BOLD, 32)); // ขยายขนาดให้ชัดขึ้น
    apLabel.setForeground(new Color(255, 100, 100)); // สีชมพูแดงสว่างขึ้น
    apLabel.setBounds(0, 0, 180, 55);

    // นำ Label ใส่เข้าไปใน Container
    apContainer.add(apLabel);
        pTurnLabel = new JLabel("PLAYER 1");
        pTurnLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        pTurnLabel.setForeground(Color.CYAN);
        addComponent(pTurnLabel, 1080, 20, 150, 40);
        JPanel pTurnContainer = new JPanel(null) {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // วาดพื้นหลังสีดำโปร่งแสง
        g2d.setColor(new Color(0, 0, 0, 160)); 
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        // วาดเส้นขอบสี Cyan จางๆ ให้ดูเหมือนแสง Neon
        g2d.setColor(new Color(0, 255, 255, 100)); 
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
        
        g2d.dispose();
    }
};
pTurnContainer.setOpaque(false);
// ปรับตำแหน่งให้สมดุลกับมุมขวาบน (ขยายความกว้างเผื่อ Player เลข 2 หลัก)
addComponent(pTurnContainer, 1050, 15, 200, 55); 
// 3. นำ Label ใส่เข้าไปใน Container
pTurnContainer.add(pTurnLabel);
        textWindow = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        textWindow.setOpaque(false);
        
        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(255, 215, 0));
        nameLabel.setBounds(25, 10, 300, 40);
        textWindow.add(nameLabel);

        dialogueArea = new JTextArea();
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setOpaque(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setEditable(false);
        dialogueArea.setBounds(25, 55, 1130, 100);
        textWindow.add(dialogueArea);
        addComponent(textWindow, 50, 510, 1180, 170);

        choicePanel = new JPanel(new GridLayout(2, 1, 0, 20));
        choicePanel.setOpaque(false);
        choicePanel.setVisible(false);
        addComponent(choicePanel, 800, 200, 430, 220);

        giftBtn = new JButton("GIVE GIFT (-1 AP)");
        datingBtn = new JButton("GO ON DATE (-2 AP)");
        nextBtn = new JButton("END TURN");
        
        JButton[] actionButtons = {giftBtn, datingBtn, nextBtn};
        int yPos = 280;
        for (JButton btn : actionButtons) {
            styleButton(btn);
            addComponent(btn, 980, yPos, 260, 50);
            yPos += 70;
        }

        giftBtn.addActionListener(e -> { 
            if(canPerformAction(1)) { 
                currentGirl.addScore(currentPlayer, 10);
                setEventMenuVisible(false);
                setResponseMode(true); 
                String name = currentGirl.getName().trim();
                if (name.equalsIgnoreCase("Akari")) setDialogueQueue(storyData.getAkariGiftStory());
                else if (name.equalsIgnoreCase("Reina")) setDialogueQueue(storyDataReina.getReinaGiftStory());
                else if (name.equalsIgnoreCase("Shiori")) setDialogueQueue(storyDataShiori.getShioriGiftStory());
            } 
        });

        datingBtn.addActionListener(e -> { 
            if(canPerformAction(2)) { 
                currentGirl.addScore(currentPlayer, 25);
                DatingEvent.startDate(this, currentGirl.getName(), currentDay);
            } 
        });

        // --- จุดที่บังคับตัดเข้ามินิเกม ---
        nextBtn.addActionListener(e -> { 
            setEventMenuVisible(false);
            isResponseMode = false;
            
            // ตรวจสอบค่า Player และ Day สำหรับการ Debug
            System.out.println("DEBUG: Player " + (currentPlayer + 1) + "/" + totalPlayers + " Day: " + currentDay);
            
            if (currentPlayer >= (totalPlayers - 1) && currentDay >= 6) {
                System.out.println("[System] Match Day Triggered -> Going to Mini-Game Selector");
                BGMManager.stopBGM();
                SceneManager.switchScene(new MiniGameSelector(currentGirl, currentGirl.getName(), totalPlayers));
                this.dispose();
            } else {
                handleTurnTransition(); 
            }
        });

        transitionPanel = new JPanel(new BorderLayout());
        transitionPanel.setBackground(Color.BLACK);
        transitionPanel.setVisible(false);
        transitionLabel = new JLabel("", SwingConstants.CENTER);
        transitionLabel.setForeground(Color.WHITE);
        transitionLabel.setFont(new Font("Serif", Font.ITALIC, 48));
        transitionPanel.add(transitionLabel, BorderLayout.CENTER);
        addComponent(transitionPanel, 0, 0, 1280, 720);

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (transitionPanel.isVisible() || choicePanel.isVisible() || giftBtn.isVisible()) return;
                if (typewriterTimer != null && typewriterTimer.isRunning()) {
                    stopTypewriter(currentQueue[pointer - 1].text);
                } else {
                    advanceDialogue();
                }
            }
        });
        
        setEventMenuVisible(false);
    }

    private void startTurn() {
        hasDoneActionThisTurn = false; 
        updateUI();
        StoryManager.runStory(this, currentGirl.getName(), currentDay);
    }

    private void handleTurnTransition() {
        if (currentPlayer < totalPlayers - 1) {
            currentPlayer++;
            startTurn();
        } else {
            currentPlayer = 0; 
            
            // จบวันสุดท้ายของทุกคน -> ไปมินิเกม (ดักที่ Day 6 ตามลอจิก Console)
            if (currentDay >= 6) {
                System.out.println("[System] Final Day Turn Ended -> Mini-Game Selector");
                BGMManager.stopBGM();
                SceneManager.switchScene(new MiniGameSelector(currentGirl, currentGirl.getName(), totalPlayers));
                this.dispose();
                return;
            }

            currentDay++;
            if (currentDay >= 8) {
                StoryManager.finishGame(this);
            } else {
                for(int i = 0; i < totalPlayers; i++) {
                    playerAP[i] = Math.min(playerAP[i] + 0, MAX_AP);
                }
                startTurn();
            }
        }
    }

    public boolean canPerformAction(int cost) {
        if (hasDoneActionThisTurn) {
            JOptionPane.showMessageDialog(this, "You've finished today's activities!");
            return false;
        }
        if (playerAP[currentPlayer] < cost) {
            JOptionPane.showMessageDialog(this, "Not enough AP points!");
            return false;
        }
        playerAP[currentPlayer] -= cost;
        hasDoneActionThisTurn = true; 
        updateUI();
        return true;
    }

    public void runDayLogic(String bg, DialogueLine[] lines, String[] choiceText, int scoreA, int scoreB, DialogueLine[] resA, DialogueLine[] resB) {
        setBackgroundImage(bg);
        setDialogueQueue(lines);
        choicePanel.removeAll();
        JButton b1 = new JButton(choiceText[0]);
        JButton b2 = new JButton(choiceText[1]);
        styleButton(b1); styleButton(b2);
        b1.addActionListener(e -> { hideChoices(); StoryManager.onChoiceSelected(this, scoreA); setResponseMode(true); setDialogueQueue(resA); });
        b2.addActionListener(e -> { hideChoices(); StoryManager.onChoiceSelected(this, scoreB); setResponseMode(true); setDialogueQueue(resB); });
        choicePanel.add(b1); choicePanel.add(b2);
        isWaitingForResponse = false;
    }

    public void advanceDialogue() {
        if (currentQueue == null) return;
        if (pointer < currentQueue.length) {
            DialogueLine line = currentQueue[pointer++];
            nameLabel.setText(line.characterName);
            startTypewriter(line.text);
            if (line.spritePath != null && !line.spritePath.isEmpty()) spritePanel.updateImage(line.spritePath);
        } else {
            if (endingPlayerQueue != null) {
                showNextPlayerEnding();
                return;
            }

            // ดักจับ: จบเนื้อเรื่องวันสุดท้ายให้ไปมินิเกมทันที
            // if (currentDay >= 6 && !isResponseMode) {
            //     System.out.println("[System] Day 7 Dialogue Done -> Mini-Game Selector");
            //     BGMManager.stopBGM();
            //     SceneManager.switchScene(new MiniGameSelector(currentGirl, currentGirl.getName(), totalPlayers));
            //     this.dispose();
            //     return; 
            // }

            if (choicePanel.getComponentCount() > 0 && !isWaitingForResponse) {
                choicePanel.setVisible(true); 
                setupZOrder(); 
                isWaitingForResponse = true;
            } else {
                if (isResponseMode) {
                    isResponseMode = false; 
                    setEventMenuVisible(true); 
                    setupZOrder();
                } else {
                    triggerEveningActivities(); 
                }
            }
        }
    }

    public void triggerEveningActivities() {
        showDayTransition(currentDay, "Evening Activities", () -> {
            setEventMenuVisible(true);
            setupZOrder();
        });
    }

    private void setupZOrder() {
        if (transitionPanel != null) mainPanel.setComponentZOrder(transitionPanel, 0);
        if (pTurnLabel != null) mainPanel.setComponentZOrder(pTurnLabel, 1);
        if (choicePanel != null) mainPanel.setComponentZOrder(choicePanel, 1);
        if (giftBtn != null) mainPanel.setComponentZOrder(giftBtn, 1);
        if (datingBtn != null) mainPanel.setComponentZOrder(datingBtn, 1);
        if (nextBtn != null) mainPanel.setComponentZOrder(nextBtn, 1);
        if (textWindow != null) mainPanel.setComponentZOrder(textWindow, 2);
        if (spritePanel != null) mainPanel.setComponentZOrder(spritePanel, 3);
        mainPanel.revalidate(); mainPanel.repaint();
    }

    public void updateUI() { 
        if (apLabel != null) apLabel.setText("AP: " + playerAP[currentPlayer]); 
        if (pTurnLabel != null) pTurnLabel.setText("PLAYER " + (currentPlayer + 1));
    }

    public void setEventMenuVisible(boolean visible) {
        giftBtn.setVisible(visible); datingBtn.setVisible(visible); nextBtn.setVisible(visible);
        textWindow.setVisible(!visible); if (visible) updateUI();
    }

    public void earnAP() { if(playerAP[currentPlayer] < MAX_AP) playerAP[currentPlayer]++; updateUI(); }

    private void startTypewriter(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText("");
        final int[] i = {0};
        typewriterTimer = new Timer(30, e -> {
            if (i[0] < text.length()) {
                dialogueArea.append(String.valueOf(text.charAt(i[0]++)));
                if (i[0] % 2 == 0) AudioManager.playSound("undertale_type.wav");
            } else typewriterTimer.stop();
        });
        typewriterTimer.start();
    }

    private void stopTypewriter(String fullText) { if (typewriterTimer != null) typewriterTimer.stop(); dialogueArea.setText(fullText); }

    public void showDayTransition(int day, String title, Runnable onComplete) {
        String labelText = (endingPlayerQueue != null) ? "Player " + day : "Day " + day;
        transitionLabel.setText("<html><center>" + labelText + "<br><small>" + title + "</small></center></html>");
        transitionPanel.setVisible(true);
        mainPanel.setComponentZOrder(transitionPanel, 0);
        Timer timer = new Timer(2000, e -> {
            transitionPanel.setVisible(false); if (onComplete != null) onComplete.run(); setupZOrder();
        });
        timer.setRepeats(false); timer.start();
    }

    public Character getCurrentGirl() { return this.currentGirl; }
    public int getCurrentPlayer() { return this.currentPlayer; }
    public void hideChoices() { choicePanel.setVisible(false); choicePanel.removeAll(); }
    public void setDialoguePointer(int p) { this.pointer = p; }
    public void setDialogueQueue(DialogueLine[] queue) { this.currentQueue = queue; this.pointer = 0; advanceDialogue(); }
    public void setResponseMode(boolean mode) { this.isResponseMode = mode; }

    public void playDayBGM(int day) {
        String bgmName = switch (day) {
            case 1 -> "Blue_Archive_Future_Bossa.wav";
            case 2 -> "Blue_Archive_Mischievous_Step.wav";
            case 3 -> "Blue_Archive_Lovely_Picnic.wav";
            case 4 -> "Blue_Archive_Midsummer_Cat.wav";
            case 5 -> "Blue_Archive_Shooting_Stars.wav";
            case 6 -> "Blue_Archive_Morose_Dreamer.wav";
            default -> "Blue_Archive_Future_Bossa.wav";
        };
        BGMManager.playBGM(bgmName);
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        if (transitionPanel != null) transitionPanel.setBounds(0, 0, getWidth(), getHeight());
    }
}