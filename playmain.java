import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import audio.AudioManager;
import audio.BGMManager;

/**
 * playmain: คลาสหลักสำหรับหน้าจอเกมเพลย์
 * รองรับระบบ Multiplayer, AP, และจำกัดกิจกรรม 1 อย่างต่อวันตามเงื่อนไข
 */
public class playmain extends BaseFrame {

    // --- ระบบผู้เล่น (Multiplayer) ---
    private int currentPlayer = 0;
    private int totalPlayers;
    private int[] playerAP; 
    private final int MAX_AP = 5;

    // --- สถานะการทำกิจกรรม (จำกัด 1 อย่างต่อเทิร์น) ---
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
    
    public playmain(Character selectedGirl, int players) {
        super("7 Days to Love - " + (selectedGirl != null ? selectedGirl.getName() : "Story"));
        this.currentGirl = selectedGirl;
        this.totalPlayers = players;
        
        // ค่า AP เริ่มต้น (คนละ 2 แต้มเพื่อให้ทำกิจกรรมวันแรกได้ทันที)
        this.playerAP = new int[players];
        for(int i = 0; i < players; i++) playerAP[i] = 0; 

        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg");
        initGameUI();
        setupZOrder(); 
        
        startTurn();
    }

    private void initGameUI() {
        // 1. Sprite ตัวละคร
        spritePanel = new CharacterPanel("");
        addComponent(spritePanel, 540, 100, 200, 600); 

        // 2. แถบสถานะ (AP และชื่อผู้เล่น)
        apLabel = new JLabel("AP: 2");
        apLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        apLabel.setForeground(new Color(255, 80, 80));
        addComponent(apLabel, 30, 20, 200, 40);

        pTurnLabel = new JLabel("PLAYER 1");
        pTurnLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        pTurnLabel.setForeground(Color.CYAN);
        addComponent(pTurnLabel, 1080, 20, 150, 40);

        // 3. หน้าต่างบทสนทนา
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

        // 4. แผงเลือกคำตอบระหว่างเนื้อเรื่อง
        choicePanel = new JPanel(new GridLayout(2, 1, 0, 20));
        choicePanel.setOpaque(false);
        choicePanel.setVisible(false);
        addComponent(choicePanel, 800, 200, 430, 220);

        // 5. ปุ่มกิจกรรมช่วงเย็น
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

        // --- ตั้งค่าปุ่มกิจกรรม ---
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
                // เรียก DatingEvent (ไม่ต้องแก้คลาสโน้น)
                DatingEvent.startDate(this, currentGirl.getName(), currentDay);
            } 
        });

        nextBtn.addActionListener(e -> { 
            setEventMenuVisible(false);
            isResponseMode = false;
            handleTurnTransition(); 
        });

        // 6. Transition Panel (สำหรับแสดง Day X)
        transitionPanel = new JPanel(new BorderLayout());
        transitionPanel.setBackground(Color.BLACK);
        transitionPanel.setVisible(false);
        transitionLabel = new JLabel("", SwingConstants.CENTER);
        transitionLabel.setForeground(Color.WHITE);
        transitionLabel.setFont(new Font("Serif", Font.ITALIC, 48));
        transitionPanel.add(transitionLabel, BorderLayout.CENTER);
        addComponent(transitionPanel, 0, 0, 1280, 720);

        // ระบบคลิกเพื่อข้ามข้อความ
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
        hasDoneActionThisTurn = false; // รีเซ็ตสิทธิ์การทำกิจกรรมเมื่อเริ่มเทิร์นใหม่
        updateUI();
        // เรียก StoryManager (ไม่ต้องแก้คลาสโน้น)
        StoryManager.runStory(this, currentGirl.getName(), currentDay);
    }

    private void handleTurnTransition() {
        if (currentPlayer < totalPlayers - 1) {
            currentPlayer++;
            startTurn();
        } else {
            currentPlayer = 0;
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

    // แก้ไข: เพิ่มการเช็ค hasDoneActionThisTurn เพื่อจำกัดกิจกรรม 1 อย่าง
    public boolean canPerformAction(int cost) {
        if (hasDoneActionThisTurn) {
            JOptionPane.showMessageDialog(this, "คุณทำกิจกรรมของวันนี้ไปแล้ว!");
            return false;
        }
        if (playerAP[currentPlayer] < cost) {
            JOptionPane.showMessageDialog(this, "แต้ม AP ไม่พอ!");
            return false;
        }
        playerAP[currentPlayer] -= cost;
        hasDoneActionThisTurn = true; // ล็อคสิทธิ์ทันที
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

        b1.addActionListener(e -> { 
            hideChoices(); 
            StoryManager.onChoiceSelected(this, scoreA);
            setResponseMode(true); 
            setDialogueQueue(resA); 
        });
        b2.addActionListener(e -> { 
            hideChoices(); 
            StoryManager.onChoiceSelected(this, scoreB);
            setResponseMode(true); 
            setDialogueQueue(resB); 
        });

        choicePanel.add(b1);
        choicePanel.add(b2);
        isWaitingForResponse = false;
    }

    public void advanceDialogue() {
        if (currentQueue == null) return;
        if (pointer < currentQueue.length) {
            DialogueLine line = currentQueue[pointer++];
            nameLabel.setText(line.characterName);
            startTypewriter(line.text);
            if (line.spritePath != null && !line.spritePath.isEmpty()) {
                spritePanel.updateImage(line.spritePath);
            }
        } else {
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
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void updateUI() { 
        if (apLabel != null) apLabel.setText("AP: " + playerAP[currentPlayer]); 
        if (pTurnLabel != null) pTurnLabel.setText("PLAYER " + (currentPlayer + 1));
    }

    public void setEventMenuVisible(boolean visible) {
        giftBtn.setVisible(visible);
        datingBtn.setVisible(visible);
        nextBtn.setVisible(visible);
        textWindow.setVisible(!visible);
        if (visible) updateUI();
    }

    public void earnAP() { 
        if(playerAP[currentPlayer] < MAX_AP) playerAP[currentPlayer]++; 
        updateUI(); 
    }

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

    private void stopTypewriter(String fullText) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText(fullText);
    }

    public void showDayTransition(int day, String title, Runnable onComplete) {
        transitionLabel.setText("<html><center>Day " + day + "<br><small>" + title + "</small></center></html>");
        transitionPanel.setVisible(true);
        mainPanel.setComponentZOrder(transitionPanel, 0);
        Timer timer = new Timer(2000, e -> {
            transitionPanel.setVisible(false);
            if (onComplete != null) onComplete.run();
            setupZOrder();
        });
        timer.setRepeats(false);
        timer.start();
    }

    // --- Getters & Setters ---
    public Character getCurrentGirl() { return this.currentGirl; }
    public int getCurrentPlayer() { return this.currentPlayer; }
    public void hideChoices() { choicePanel.setVisible(false); choicePanel.removeAll(); }
    public void setDialoguePointer(int p) { this.pointer = p; }
    public void setDialogueQueue(DialogueLine[] queue) { this.currentQueue = queue; this.pointer = 0; advanceDialogue(); }
    public void setResponseMode(boolean mode) { this.isResponseMode = mode; }
    public void setNextDayTarget(int t) { /* คงไว้เพื่อให้ DatingEvent เรียกใช้งานได้ปกติ */ }

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