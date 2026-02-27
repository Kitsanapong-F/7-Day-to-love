import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class playmain extends BaseFrame {

    private JPanel transitionPanel, textWindow, choicePanel;
    private JLabel transitionLabel, nameLabel;
    private JTextArea dialogueArea;
    private CharacterPanel spritePanel;
    private int pointer = 0; 
    private int nextDayTarget = -1;
    private int ap = 0;           
    private int giftCount = 0;    
    private int dateCount = 0;    

    private JButton giftBtn, datingBtn, nextBtn;
    private JLabel apLabel; 
    private Character currentGirl; 
    private int currentDay = 1; 

    private DialogueLine[] currentQueue;
    private int score = 0; 
    private boolean isWaitingForResponse = false;
    private Timer typewriterTimer;

    private boolean isResponseMode = false;

    public void setResponseMode(boolean mode) { 
    this.isResponseMode = mode; 
}

    public playmain(Character selectedGirl) {
        super("7 Days to Love - " + (selectedGirl != null ? selectedGirl.getName() : "Story"));
        this.currentGirl = selectedGirl;
        
        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg");
        
        initGameUI();
        setupZOrder(); // จัดเลเยอร์ครั้งแรกตอนเริ่มเกม
        
        if (currentGirl != null) {
            StoryManager.runStory(this, currentGirl.getName(), currentDay);
        } else {
            System.err.println("Warning: No character selected!");
        }
    }

    private void initGameUI() {
        // 1. Sprite Panel (Layer 3 - อยู่ด้านหลัง)
        spritePanel = new CharacterPanel("");
        addComponent(spritePanel, 540, 100, 200, 600); 

        // 2. AP Display
        apLabel = new JLabel("AP: 0");
        apLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        apLabel.setForeground(new Color(255, 80, 80));
        addComponent(apLabel, 30, 20, 200, 40);

        // 3. Text Window (Layer 2)
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
        textWindow.add(nameLabel);

        dialogueArea = new JTextArea();
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setOpaque(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setEditable(false);
        dialogueArea.setFocusable(false);
        textWindow.add(dialogueArea);
        
        addComponent(textWindow, 50, 510, 1180, 170);

        // 4. Choice Panel (Layer 1)
        choicePanel = new JPanel(new GridLayout(2, 1, 0, 20));
        choicePanel.setOpaque(false);
        choicePanel.setVisible(false);
        addComponent(choicePanel, 800, 200, 430, 220);

        // 5. Action Menu (Layer 1) - พิกัดเดิม x=980
        giftBtn = new JButton("GIVE GIFT (-1 AP)");
        datingBtn = new JButton("GO ON DATE (-2 AP)");
        nextBtn = new JButton("NEXT DAY");
        
        JButton[] actionButtons = {giftBtn, datingBtn, nextBtn};
        int yPos = 280;
        for (JButton btn : actionButtons) {
            styleButton(btn);
            addComponent(btn, 980, yPos, 260, 50);
            yPos += 70;
        }

        giftBtn.addActionListener(e -> { 
            AudioManager.playSound("umamusume_click.wav");

            if(canPerformAction(1, "gift")) { 
            currentGirl.addScore(10); 
            setEventMenuVisible(false); // ซ่อนปุ่มเลือกกิจกรรมก่อน
        
            showDayTransition(currentDay, "A Small Gift", () -> {
            // สำคัญ: ต้องเข้าโหมด Response เพื่อไม่ให้วนกลับไปหน้าเมนูทันที
            setResponseMode(true); 
            
            // ตรวจสอบชื่อตัวละคร (ใช้ trim เพื่อตัดช่องว่างที่อาจติดมา)
            String name = currentGirl.getName().trim();
            if (name.equalsIgnoreCase("Akari")) {
                setDialogueQueue(storyData.getAkariGiftStory());
            } else if (name.equalsIgnoreCase("Reina")) {

            } else if (name.equalsIgnoreCase("Shiori")) {

            }
        });
        updateUI();
        } 
    });
        datingBtn.addActionListener(e -> { AudioManager.playSound("umamusume_click.wav"); if(canPerformAction(2, "date")) { DatingEvent.startDate(this, currentGirl.getName(), currentDay); } });
        nextBtn.addActionListener(e -> { AudioManager.playSound("umamusume_click.wav");
        setEventMenuVisible(false);
        isResponseMode = false; // รีเซ็ตโหมด
        StoryManager.runStory(this, currentGirl.getName(), currentDay); // เริ่มเนื้อเรื่องของวันที่เพิ่งเปลี่ยนมา
        });

        // 6. Transition Panel (Layer 0 - อยู่หน้าสุดเสมอ)
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
                AudioManager.playSound("umamusume_click.wav");

                if (transitionPanel.isVisible() || choicePanel.isVisible() || giftBtn.isVisible()) return;
                if (typewriterTimer != null && typewriterTimer.isRunning()) {
                    stopTypewriter(currentQueue[pointer - 1].text);
                } else {
                    advanceDialogue();
                }
            }
        });
        
        setEventMenuVisible(false);
        setupZOrder();
    }

    /**
     * ระบบจัดการลำดับเลเยอร์ (Z-Order)
     * ยิ่งเลขน้อย (เช่น 0) จะยิ่งอยู่หน้าสุดของจอ
     */
    private void setupZOrder() {
    if (transitionPanel != null) mainPanel.setComponentZOrder(transitionPanel, 0); // หน้าสุด
    if (choicePanel != null) mainPanel.setComponentZOrder(choicePanel, 1);

    if (giftBtn != null) mainPanel.setComponentZOrder(giftBtn, 1);
    if (datingBtn != null) mainPanel.setComponentZOrder(datingBtn, 1);
    if (nextBtn != null) mainPanel.setComponentZOrder(nextBtn, 1);
    
    // กล่องข้อความ (Layer 2) อยู่หน้าตัวละคร 
    if (textWindow != null) mainPanel.setComponentZOrder(textWindow, 2);
    
    // ตัวละคร (Layer 3) อยู่หลังสุด 
    if (spritePanel != null) mainPanel.setComponentZOrder(spritePanel, 3);

    mainPanel.revalidate();
    mainPanel.repaint();
}

   public void setEventMenuVisible(boolean visible) {
        if (giftBtn != null) giftBtn.setVisible(visible);
        if (datingBtn != null) datingBtn.setVisible(visible);
        if (nextBtn != null) nextBtn.setVisible(visible);
        
        // ถ้าแสดงเมนู ให้ซ่อนกล่องข้อความ และในทางกลับกัน
        if (textWindow != null) textWindow.setVisible(!visible);
    }

    public void advanceDialogue() {
    if (currentQueue == null) return;
    if (pointer < currentQueue.length) {
        // ... (โค้ดแสดงบทสนทนาปกติของคุณ) ...
        DialogueLine line = currentQueue[pointer];
        nameLabel.setText(line.characterName);
        startTypewriter(line.text);
        if (line.spritePath != null && !line.spritePath.isEmpty()) {
            spritePanel.updateImage(line.spritePath);
        }
        pointer++;
    } else {
        if (choicePanel.getComponentCount() > 0 && !isWaitingForResponse) {
            choicePanel.setVisible(true);
            setupZOrder();
            isWaitingForResponse = true;
        } else {
            // --- แก้ไขตรงนี้เพื่อแก้ปัญหาการแสดงเมนูซ้ำซ้อน ---
           if (isResponseMode) {
                if (nextDayTarget != -1) {
                    // กรณี Dating: มีการตั้งข้ามวันไว้ -> เรียก handleDayTransition (มีการข้ามวัน) 
                    handleDayTransition(); 
                } else {
                    // กรณี Gift: ไม่มีการข้ามวัน -> รันเนื้อเรื่องวันปัจจุบันต่อทันที (No Transition 2)
                    isResponseMode = false; // ปิดโหมดตอบโต้
                    StoryManager.runStory(this, currentGirl.getName(), currentDay); 
                }
            } else {
                // จบเนื้อเรื่องหลัก -> ไปสู่หน้าเลือกกิจกรรม (ที่มี Transition)
                triggerEveningChoice(); 
            }
        }
    }
}

    public void runDayLogic(String bg, DialogueLine[] story, String[] choices, int scoreA, int scoreB, DialogueLine[] resA, DialogueLine[] resB) {
        setBackgroundImage(bg);
        hideChoices();
        isWaitingForResponse = false;

        JButton btnA = new JButton("<html><center>" + choices[0] + "</center></html>");
        JButton btnB = new JButton("<html><center>" + choices[1] + "</center></html>");
        styleChoiceButton(btnA); styleChoiceButton(btnB);

        btnA.addActionListener(e -> { AudioManager.playSound("umamusume_click.wav"); score += scoreA; handleSelection(resA); StoryManager.onChoiceSelected(this, scoreA); });
        btnB.addActionListener(e -> { AudioManager.playSound("umamusume_click.wav"); score += scoreB; handleSelection(resB); StoryManager.onChoiceSelected(this, scoreB); });

        choicePanel.add(btnA);
        choicePanel.add(btnB);
        setDialogueQueue(story);
        setupZOrder(); // จัดเลเยอร์เพื่อให้องค์ประกอบหน้าใหม่แสดงผลถูกต้อง
    }

    private void handleSelection(DialogueLine[] response) {
        hideChoices(); 
        isWaitingForResponse = true; 
        setDialogueQueue(response);
    }

    public void showDayTransition(int day, String title, Runnable onComplete) {
        transitionLabel.setText("<html><center>Day " + day + "<br><small>" + title + "</small></center></html>");
        transitionPanel.setVisible(true);
        mainPanel.setComponentZOrder(transitionPanel, 0); // บังคับ Layer 0 หน้าสุด
        
        Timer timer = new Timer(2200, e -> {
            transitionPanel.setVisible(false);
            if (onComplete != null) onComplete.run();
            setupZOrder(); // คืนค่าเลเยอร์ปกติหลังจบการเปลี่ยนวัน
            mainPanel.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

        public void triggerEveningChoice() {
    // แก้ไข: เพิ่มวันก่อน เพื่อให้ Transition แสดงเป็น "Day 3" (ถ้าจบ Day 2)
    this.currentDay++;

    String name = currentGirl.getName().trim();
    String newBG = null;

    if (name.equalsIgnoreCase("Akari")) {
        newBG = storyData.getAkariDayBackground(currentDay); // [cite: 5]
    } else if (name.equalsIgnoreCase("Reina")) {
        //newBG = storyDataReina.getReinaDayBackground(currentDay); // [cite: 6]
    } else if (name.equalsIgnoreCase("Shiori")) {
        //newBG = storyDataShiori.getShioriDayBackground(currentDay); // [cite: 8]
    }

    // 4. อัปเดตพื้นหลังทันทีก่อนขึ้น Transition [cite: 4]
    if (newBG != null) {
        setBackgroundImage(newBG); 
    }
    
    showDayTransition(currentDay, "Evening Activities", () -> {
        setEventMenuVisible(true); // แสดงปุ่มเลือกกิจกรรมหลังจอดำหายไป [cite: 13]
        setupZOrder();
    });
}
    

   private void handleDayTransition() {
    // 1. ตรวจสอบเงื่อนไขจบเกมก่อนเป็นอันดับแรก
    // ถ้า currentDay เป็น 8 หรือ Target เป็น 99 แสดงว่าจบฉากจบแล้ว
    if (this.currentDay >= 8 || nextDayTarget == 99) {
        StoryManager.finishGame(this); 
        return; // หยุดการทำงานเพื่อไม่ให้รัน StoryManager.runStory ซ้ำ
    }

    isResponseMode = false; 
    giftCount = 0; dateCount = 0; 
    
    if (nextDayTarget != -1) { 
        this.currentDay = nextDayTarget; 
        nextDayTarget = -1; 
    } else { 
        this.currentDay++; 
    }
    
    setEventMenuVisible(false);
    
    // 2. ส่งค่าไปให้ StoryManager รันเนื้อเรื่องวันถัดไป หรือรัน Ending
    StoryManager.runStory(this, currentGirl.getName(), currentDay);
    }

    private void startTypewriter(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText("");
        final int[] i = {0};
        typewriterTimer = new Timer(30, e -> {
            if (i[0] < text.length()) {

                dialogueArea.append(String.valueOf(text.charAt(i[0])));

                if (i[0] % 2 == 0) {
                    AudioManager.playSound("undertale_type.wav");
                }

                i[0]++;
            } else {
                typewriterTimer.stop();
            }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter(String fullText) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText(fullText);
    }

    public void hideChoices() { 
        choicePanel.setVisible(false); 
        choicePanel.removeAll(); 
        choicePanel.revalidate();
    }

    public boolean canPerformAction(int cost, String type) {
        if (type.equals("gift") && giftCount >= 2) return false;
        if (type.equals("date") && dateCount >= 1) return false;
        if (ap < cost) {
            JOptionPane.showMessageDialog(this, "Not enough AP!");
            return false;
        }
        ap -= cost;
        if (type.equals("gift")) giftCount++; else dateCount++;
        updateUI(); return true;
    }

    private void updateUI() { if (apLabel != null) apLabel.setText("AP: " + ap); }
    public void earnAP() { this.ap++; updateUI(); }
    public void setDialoguePointer(int p) { this.pointer = p; }
    public void setDialogueQueue(DialogueLine[] queue) { this.currentQueue = queue; this.pointer = 0; advanceDialogue(); }
    public int getCurrentGirlScore() { return this.score; }
    public void setNextDayTarget(int t) { this.nextDayTarget = t; }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        if (transitionPanel != null) transitionPanel.setBounds(0, 0, getWidth(), getHeight());
        if (nameLabel != null) nameLabel.setBounds((int)(30 * scaleX), (int)(15 * scaleY), (int)(400 * scaleX), (int)(40 * scaleY));
        if (dialogueArea != null) dialogueArea.setBounds((int)(30 * scaleX), (int)(60 * scaleY), (int)(1100 * scaleX), (int)(100 * scaleY));
    }

    // ใน playmain.java [cite: 12]
    
}