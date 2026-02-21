import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class playmain extends BaseFrame {
    private JPanel transitionPanel;
    private JLabel transitionLabel;
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
        addComponent(spritePanel, 440, 50, 400, 600); 

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
        nextBtn = new JButton("END DAY / NEXT");
        
        JButton[] actionButtons = {giftBtn, datingBtn, nextBtn};
        int yPos = 280;
        for (JButton btn : actionButtons) {
            styleButton(btn);
            addComponent(btn, 980, yPos, 260, 50);
            yPos += 70;
        }

        giftBtn.addActionListener(e -> { if(canPerformAction(1, "gift")) { currentGirl.addScore(10); updateUI(); } });
        datingBtn.addActionListener(e -> { if(canPerformAction(2, "date")) { DatingEvent.startDate(this, currentGirl.getName(), currentDay); } });
        nextBtn.addActionListener(e -> handleDayTransition());

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

    /**
     * ระบบจัดการลำดับเลเยอร์ (Z-Order)
     * ยิ่งเลขน้อย (เช่น 0) จะยิ่งอยู่หน้าสุดของจอ
     */
    private void setupZOrder() {
        if (transitionPanel != null) mainPanel.setComponentZOrder(transitionPanel, 0);
        
        // กลุ่มปุ่มกดและทางเลือก (อยู่หน้ากล่องข้อความ)
        if (choicePanel != null) mainPanel.setComponentZOrder(choicePanel, 1);
        if (giftBtn != null) mainPanel.setComponentZOrder(giftBtn, 1);
        if (datingBtn != null) mainPanel.setComponentZOrder(datingBtn, 1);
        if (nextBtn != null) mainPanel.setComponentZOrder(nextBtn, 1);

        // กล่องข้อความและสเตตัส (Layer กลาง)
        if (textWindow != null) mainPanel.setComponentZOrder(textWindow, 2);
        if (apLabel != null) mainPanel.setComponentZOrder(apLabel, 2);

        // ตัวละคร (Layer ลึกสุด แต่อยู่บนฉากหลัง)
        if (spritePanel != null) mainPanel.setComponentZOrder(spritePanel, 3);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setEventMenuVisible(boolean visible) {
        giftBtn.setVisible(visible); 
        datingBtn.setVisible(visible); 
        nextBtn.setVisible(visible);
        textWindow.setVisible(!visible);
        
        // เมื่อเปลี่ยนโหมดหน้าจอ ให้จัดเลเยอร์ใหม่เพื่อให้ปุ่มอยู่หน้าสุดเสมอ
        if (visible) setupZOrder();
    }

    public void advanceDialogue() {
        if (currentQueue == null) return;
        if (pointer < currentQueue.length) {
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
                setupZOrder(); // จัดเลเยอร์ให้ choicePanel อยู่หน้าสุด
                isWaitingForResponse = true;
            } else {
                StoryManager.processNextDay(this);
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

        btnA.addActionListener(e -> { score += scoreA; handleSelection(resA); StoryManager.onChoiceSelected(this, scoreA); });
        btnB.addActionListener(e -> { score += scoreB; handleSelection(resB); StoryManager.onChoiceSelected(this, scoreB); });

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

    private void handleDayTransition() {
        if (nextDayTarget != -1) { 
            currentDay = nextDayTarget; 
            nextDayTarget = -1; 
        } else { 
            currentDay++; 
        }
        giftCount = 0; dateCount = 0;
        StoryManager.runStory(this, currentGirl.getName(), currentDay);
    }

    private void startTypewriter(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText("");
        final int[] i = {0};
        typewriterTimer = new Timer(30, e -> {
            if (i[0] < text.length()) {
                dialogueArea.append(String.valueOf(text.charAt(i[0])));
                i[0]++;
            } else { typewriterTimer.stop(); }
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
}