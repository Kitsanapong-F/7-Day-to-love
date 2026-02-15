import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VisualNovelUI extends BaseFrame {
    private Character currentGirl;
    private String heroName;
    private boolean isFullscreen;
    private int currentDay = 1;

    // --- แก้ไขประเภทตัวแปรให้เป็น DialogueLine[] เพื่อรองรับระบบภาพ ---
    private DialogueLine[] dialogueQueue;
    private int dialoguePointer = 0;
    private boolean isResponseMode = false; 
    private DialogueLine[] choiceResponses1, choiceResponses2; 

    private JTextArea dialogueArea;
    private JButton choice1, choice2;
    private JPanel dialoguePanel;
    private JLabel characterLabel;

    public VisualNovelUI(String name, boolean fullscreen) {
        super("7 Days to Love - " + name + "'s Route");
        this.heroName = name;
        this.isFullscreen = fullscreen;
        
        if (name.equals("Akari")) {
            currentGirl = new Character("Akari", "image/akari_normal.png");
        }
        
        setupUI(); 
        applyFullscreenState(); 
        renderDay(1); 
        
        display();
    }

    private void setupUI() {
        characterLabel = new JLabel();
        characterLabel.setHorizontalAlignment(SwingConstants.CENTER);

        dialoguePanel = new JPanel(new BorderLayout());
        dialoguePanel.setBackground(new Color(0, 0, 0, 180));
        
        dialogueArea = new JTextArea();
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 24);
        dialogueArea.setFont(thaiFont);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setEditable(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setOpaque(false);
        dialogueArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialoguePanel.add(dialogueArea, BorderLayout.CENTER);

        choice1 = new JButton();
        choice1.setFont(thaiFont);
        styleButton(choice1);

        choice2 = new JButton();
        choice2.setFont(thaiFont);
        styleButton(choice2);

        // คลิกที่หน้าจอ (mainPanel จาก BaseFrame) เพื่อเลื่อนบทสนทนา
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                advanceDialogue();
            }
        });

        // ใช้ระบบพิกัด Scaling จาก BaseFrame
        addComponent(characterLabel, 400, 50, 480, 670);
        addComponent(dialoguePanel, 50, 500, 1180, 180);
        addComponent(choice1, 950, 300, 300, 50);
        addComponent(choice2, 950, 370, 300, 50);
    }

    private void advanceDialogue() {
        if (dialogueQueue != null && dialoguePointer < dialogueQueue.length) {
            DialogueLine currentLine = dialogueQueue[dialoguePointer];
            dialogueArea.setText(currentLine.text);
            
            // อัปเดตรูปภาพตัวละครตามที่ระบุใน DialogueLine
            if (currentLine.spritePath != null && !currentLine.spritePath.isEmpty()) {
                characterLabel.setIcon(getScaledIcon(currentLine.spritePath, 480, 670));
            }
            
            dialoguePointer++;
            
            if (dialoguePointer == dialogueQueue.length) {
                if (isResponseMode) {
                    isResponseMode = false; 
                    Timer timer = new Timer(1500, e -> {
                        currentDay++;
                        renderDay(currentDay);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    choice1.setVisible(true);
                    choice2.setVisible(true);
                }
            }
        }
    }

    private void akariStory(int day) {
        choice1.setVisible(false);
        choice2.setVisible(false);
        dialoguePointer = 0;

        switch (day) {
            case 1:
                setBackgroundImage("image/Naohiro.jpg");
                
                choice1.setText("รับทราบครับกัปตัน! แต่อย่ามาร้องไห้โยเยตอนเห็นฉันยกของหนักกว่าเธอก็แล้วกัน");
                choice2.setText("โหย ฟังดูเหนื่อยชะมัด แอบไปงีบที่ห้องสมุดไม่ได้เหรอ?");

                // บทโต้ตอบหลังเลือก (ใช้ DialogueLine)
                DialogueLine[] responseA = {
                    new DialogueLine("อาคาริ: (หน้าแดงวูบหนึ่งแล้วยิ้มกว้าง) \"เหะ! ฝันไปเถอะย่ะ! ใครแพ้ต้องเลี้ยงไอศกรีมหลังซ้อมพรุ่งนี้นะ ตกลงมั้ย?\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก: \"ได้เลย เตรียมกระเป๋าฉีกไว้ได้เลยนะกัปตัน เพราะผมไม่มีทางแพ้หรอก\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("อาคาริ: \"มั่นใจจังนะ! แต่ก็... ขอบใจนะที่มาช่วย ความจริงฉันเริ่มจะปวดแขนพอดีเลยล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };

                DialogueLine[] responseB = {
                    new DialogueLine("อาคาริ: \"นายนี่มันทำตัวเป็นตาแก่ไปได้! งั้นก็เชิญขี้เกียจไปคนเดียวเลยนะ...\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก: \"เอาน่า ผมแค่ล้อเล่นเอง เดี๋ยวผมช่วยยกกล่องนี้ให้ก็ได้\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("อาคาริ: \"ไม่ต้องเลย! ฉันไม่อยากพึ่งพาคนไม่มีใจทำงานหรอก ไปนอนให้สบายเลยไป!\" (อาคาริเดินกระแทกส้นเท้าหนีไป)", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA, responseB);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("อาคาริ: \"ฮึ่มมม...! อีกนิดเดียว... อีกแค่ 7 วันก็จะถึงงานเทศกาลแล้วแท้ๆ ทำไมกล่องพวกนี้มันหนักเหมือนใส่หินไว้ข้างในเลยเนี่ย!\"", "image\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
                    new DialogueLine("พระเอก: \"บ่นเป็นคนแก่เลยนะอาคาริ ให้คนแรงเยอะแบบผมจัดการเองดีกว่า เธอไปพักดื่มน้ำก่อนเถอะ\"", "image\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
                    new DialogueLine("อาคาริ: \"อย่าบอกนะว่านายกะจะอู้งานน่ะ! ฉันลงชื่อพวกเราช่วยแต่งเวทีหลักไปแล้วนะ งานนี้ต้องใช้แรงเยอะแน่!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                advanceDialogue();
                break;
        }
    }

    private void setupChoiceButtons(int s1, int s2, DialogueLine[] r1, DialogueLine[] r2) {
        for(ActionListener al : choice1.getActionListeners()) choice1.removeActionListener(al);
        for(ActionListener al : choice2.getActionListeners()) choice2.removeActionListener(al);
        
        this.choiceResponses1 = r1;
        this.choiceResponses2 = r2;

        choice1.addActionListener(e -> handleChoice(s1, choiceResponses1));
        choice2.addActionListener(e -> handleChoice(s2, choiceResponses2));
    }

    private void handleChoice(int scoreChange, DialogueLine[] responses) {
        currentGirl.addScore(scoreChange);
        choice1.setVisible(false);
        choice2.setVisible(false);

        this.dialogueQueue = responses;
        this.dialoguePointer = 0;
        this.isResponseMode = true; 
        advanceDialogue(); 
    }

    private void renderDay(int day) {
        this.currentDay = day;
        if (heroName.equals("Akari")) akariStory(day);
    }

    private void applyFullscreenState() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        dispose(); 
        if (isFullscreen) {
            setUndecorated(true); 
            gd.setFullScreenWindow(this);
        } else {
            setUndecorated(false); 
            gd.setFullScreenWindow(null);
            setSize(1280, 720); 
            setLocationRelativeTo(null);
        }
        setVisible(true);
    }

    private ImageIcon getScaledIcon(String path, int w, int h) {
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}

// --- คลาสสำหรับเก็บข้อมูลบทสนทนาและภาพ ---
class DialogueLine {
    String text;
    String spritePath;

    public DialogueLine(String text, String spritePath) {
        this.text = text;
        this.spritePath = spritePath;
    }
}