import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VisualNovelUI extends BaseFrame {
    private Character currentGirl;
    private String heroName;
    private boolean isFullscreen;
    private int currentDay = 1;

    private DialogueLine[] dialogueQueue;
    private int dialoguePointer = 0;
    private boolean isResponseMode = false; 
    private DialogueLine[] choiceResponses1, choiceResponses2; 

    private JTextArea dialogueArea;
    private JButton choice1, choice2;
    private JPanel dialoguePanel;
    private JLabel characterLabel;

    private JPanel transitionPanel;
    private JLabel transitionLabel;

    private JLabel nameLabel;

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

    private void toggleFullscreen() {
    isFullscreen = !isFullscreen;
    applyFullscreenState();
    }

    private void setupUI() {
        characterLabel = new JLabel();
        characterLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 1. แก้ไขปัญหา Glitch สีดำ: ใช้การวาด Panel แบบกำหนดเอง
        dialoguePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        dialoguePanel.setBackground(new Color(0, 0, 0, 180));
        dialoguePanel.setOpaque(false);

        // 2. ตั้งค่า JTextArea ให้โปร่งใสและไม่มี Glitch เคอร์เซอร์
        dialogueArea = new JTextArea(); 
        dialogueArea.setFocusable(false);
        dialogueArea.setOpaque(false);
        dialogueArea.setHighlighter(null); 
        dialogueArea.setEditable(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 24));
        dialogueArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialoguePanel.add(dialogueArea, BorderLayout.CENTER);

        choice1 = new JButton();
        styleButton(choice1);
        choice2 = new JButton();
        styleButton(choice2);

        // 3. ย้าย MouseListener มาที่ layeredPane เพื่อให้คลิกได้ทั้งจอเพื่ออ่านต่อ
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                advanceDialogue();
            }
        });

        nameLabel = new JLabel("", SwingConstants.CENTER) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
            // ล้างพื้นที่ด้วยสีพื้นหลังก่อนวาดใหม่ทุกครั้ง เพื่อแก้ปัญหาเงาซ้อน
            g2d.setColor(getBackground()); 
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
            g2d.dispose();
            super.paintComponent(g);
        }
    };

        nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 28)); // ชื่อตัวหนาและใหญ่ขึ้น
        nameLabel.setForeground(new Color(255, 255, 255)); // สีขาว หรือสีฟ้าตามสไตล์ Blue Archive
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(100, 100, 100, 220));
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    
        // วางตำแหน่งไว้เหนือ dialoguePanel (dialoguePanel เริ่มที่ y=500)
        // เราจะวางชื่อไว้ที่ x=80, y=450 เพื่อให้อยู่เยื้องบนซ้ายของกล่อง
        addComponent(nameLabel, 60, 445, 200, 40, JLayeredPane.MODAL_LAYER);

        // 4. ใส่คอมโพเนนต์ลง Layer (แก้ปัญหาตัวแดงใน image_e26eaf.png)
        addComponent(characterLabel, 300, 0, 700, 1000, JLayeredPane.PALETTE_LAYER);
        addComponent(dialoguePanel, 50, 500, 1180, 180, JLayeredPane.MODAL_LAYER);
        addComponent(choice1, 950, 300, 300, 50, JLayeredPane.POPUP_LAYER);
        addComponent(choice2, 950, 370, 300, 50, JLayeredPane.POPUP_LAYER);

        InputMap inputMap = layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = layeredPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullscreen");
        actionMap.put("toggleFullscreen", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleFullscreen();
            }
        });

        transitionPanel = new JPanel(new GridBagLayout());
        transitionPanel.setBackground(Color.BLACK);

        transitionLabel = new JLabel("", SwingConstants.CENTER);
        transitionLabel.setFont(new Font("Tahoma", Font.BOLD, 48));
        transitionLabel.setForeground(Color.WHITE);
        transitionPanel.add(transitionLabel);

        addComponent(transitionPanel, 0, 0, 1280, 720, JLayeredPane.DRAG_LAYER);
        transitionPanel.setVisible(false); // ซ่อนไว้ก่อนใช้งาน
        
    }

    private void advanceDialogue() {
        if (dialogueQueue != null && dialoguePointer < dialogueQueue.length) {
            DialogueLine currentLine = dialogueQueue[dialoguePointer];

            nameLabel.setText(currentLine.characterName);
            dialogueArea.setText(currentLine.text);
            
            if (currentLine.spritePath != null && !currentLine.spritePath.isEmpty()) {
                // แก้ไข Ratio: ส่งแค่ความสูง 670 เพื่อให้เมธอดคำนวณสัดส่วนความกว้างเอง
                characterLabel.setIcon(getScaledIcon(currentLine.spritePath, 1000));
            }
            
            dialoguePointer++;
            
            if (dialoguePointer == dialogueQueue.length) {

                if (currentDay == 7) { 
                // --- แก้ไข: ถ้าเป็นวันสุดท้าย ไม่ต้องแสดงปุ่ม Choice ---
                choice1.setVisible(false);
                choice2.setVisible(false);
                
                // รอ 3 วินาทีเพื่อให้คนอ่านดื่มด่ำกับฉากจบ แล้วค่อยขึ้นหน้าจอสรุป
                Timer endTimer = new Timer(3000, e -> {
                    showGameEndSummary(); 
                });
                endTimer.setRepeats(false);
                endTimer.start();
            } 
                else if (isResponseMode) {
                    isResponseMode = false; 
                    Timer timer = new Timer(2000, e -> {
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

    private String getDayTitle(int day) {
    switch(day) {
        case 1: return "พลังงานล้นเหลือกับภาระที่แบกไว้";
        case 2: return "รางวัลแด่คนช่างฝัน"; // ชื่อตอนสำหรับวันที่ 2
        case 3: return "ความกดดันใต้รอยยิ้ม";
        case 4: return "ทางเดินหน้าโรงเรียนยามโพล้เพล้";
        case 5: return "";
        case 6: return "คำสัญญาใต้แสงดาว";
        default: return "";
    }
}

    // เมธอดสำหรับแสดงหน้าจอ Transition
    private void showDayTransition(int day, String title, Runnable onComplete) {
    transitionLabel.setText("<html><div style='text-align: center;'>Day " + day + "<br><span style='font-size: 24px;'>" + title + "</span></div></html>");
    transitionPanel.setVisible(true);
    
    // แสดงหน้าจอสีดำทิ้งไว้ 2.5 วินาทีแล้วค่อยหายไป
    Timer timer = new Timer(2500, e -> {
        transitionPanel.setVisible(false);
        onComplete.run(); // เมื่อ Transition จบ ให้เริ่มเนื้อเรื่องวันนั้น
    });
    timer.setRepeats(false);
    timer.start();
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

                DialogueLine[] responseA = {
                    new DialogueLine("อาคาริ", "\"(หน้าแดงวูบหนึ่งแล้วยิ้มกว้าง) \"เหะ! ฝันไปเถอะย่ะ! ใครแพ้ต้องเลี้ยงไอศกรีมหลังซ้อมพรุ่งนี้นะ ตกลงมั้ย?\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"ได้เลย เตรียมกระเป๋าฉีกไว้ได้เลยนะกัปตัน เพราะผมไม่มีทางแพ้หรอก\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("อาคาริ", "\"มั่นใจจังนะ! แต่ก็... ขอบใจนะที่มาช่วย ความจริงฉันเริ่มจะปวดแขนอยู่พอดีเลยล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
                DialogueLine[] responseB = {
                    new DialogueLine("อาคาริ", "\"นายนี่มันทำตัวเป็นตาแก่ไปได้! งั้นก็เชิญขี้เกียจไปคนเดียวเลยนะ แต่อย่ามาบ่นทีหลังนะถ้าพลาดเรื่องสนุกๆ น่ะ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"เอาน่า ผมแค่ล้อเล่นเอง เดี๋ยวผมช่วยยกกล่องนี้ให้ก็ได้\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("อาคาริ", "\"ไม่ต้องเลย! ฉันไม่อยากพึ่งพาคนไม่มีใจทำงานหรอก ไปนอนให้สบายเลยไป!\" (อาคาริเดินกระแทกส้นเท้าหนีไป)", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA, responseB);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("อาคาริ", "\"ฮึ่มมม...! อีกนิดเดียว... อีกแค่ 7 วันก็จะถึงงานเทศกาลแล้วแท้ๆ ทำไมกล่องพวกนี้มันหนักเหมือนใส่หินไว้ข้างในเลยเนี่ย!\"", "image\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
                    new DialogueLine("พระเอก", "\"บ่นเป็นคนแก่เลยนะอาคาริ ให้คนแรงเยอะแบบผมจัดการเองดีกว่า เธอไปพักดื่มน้ำก่อนเถอะ\"", "image\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
                    new DialogueLine("อาคาริ", "\"อย่าบอกนะว่านายกะจะอู้งานน่ะ! ฉันลงชื่อพวกเราช่วยแต่งเวทีหลักไปแล้วนะ งานนี้ต้องใช้แรงเยอะแน่!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };
                advanceDialogue();
                break;

            case 2:
                setBackgroundImage("image\\_convenience_store_2.jpg");
                choice1.setText("งั้นผมจะยอมเป็นคนแก้เหงาให้เธอไปตลอดเลยดีมั้ย?");
                choice2.setText("กินเลอะหมดแล้วนะอาคาริ มานี่เดี๋ยวเช็ดให้");

                DialogueLine[] responseA2 = {
                    new DialogueLine("อาคาริ", "\"(สำลักไอศกรีม หน้าแดงก่ำ) \"พะ-พูดอะไรน่ะ! ตลอดเลยเหรอ? ตาลุงนี่... ชอบทำตัวเป็นพระเอกมังงะอยู่เรื่อยเลยนะ!\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"ก็ถ้ามันทำให้เธอยิ้มได้ ผมเป็นให้ได้ทุกอย่างแหละ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("อาคาริ", "\"นั่นแหละที่น่ารำคาญ! นายทำเหมือนฉันเป็นเด็กตลอดเลย... วันนี้ฉันกลับเองละกัน เจอกันพรุ่งนี้\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
                DialogueLine[] responseB2 = {
                    new DialogueLine("อาคาริ", "\"(ปัดมือคุณออกด้วยความอาย) \"หยุดเลยนะ! ฉันไม่ใช่เด็กๆ ซะหน่อย ทำแบบนี้คนอื่นมองมันดูแปลกๆ นะ!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"ก็ผมเห็นมันเลอะจริงๆ นี่นา ไม่ได้คิดอะไรไม่ดีเลย\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("อาคาริ", "\"อาคาริ: \"นั่นแหละที่น่ารำคาญ! นายทำเหมือนฉันเป็นเด็กตลอดเลย... วันนี้ฉันกลับเองละกัน เจอกันพรุ่งนี้\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA2, responseB2);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("อาคาริ", "\"อื้มมม! รสช็อกโกแลตมินต์นี่มันที่สุดจริงๆ! ขอบใจนะที่มาช่วยงานเมื่อวาน นายเนี่ย... บางทีก็พึ่งพาได้เหมือนกันนะ\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                    new DialogueLine("พระเอก", "\"แค่บางทีเหรอ? ผมนึกว่าผมเป็นฮีโร่ในสายตาเธอไปแล้วซะอีก\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                    new DialogueLine("อาคาริ", "\"อย่าหลงตัวเองไปหน่อยเลยน่า! ฉันแค่... ไม่อยากนั่งกินคนเดียวเฉยๆ หรอก! มันเหงานะรู้ไหม\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
                };
                advanceDialogue();
                break;
            case 3:
                setBackgroundImage("image\\_school_rooftop_2.jpg");
                choice1.setText("ไม่ต้องสมบูรณ์แบบก็ได้ ขอแค่เป็นอาคาริที่วิ่งอย่างสนุกก็พอแล้ว");
                choice2.setText("ถ้าเธอล้ม ฉันจะยอมแต่งชุดมาสคอตไปวิ่งเป็นเพื่อนเธอเอง");

                DialogueLine[] responseA3 = {
                    new DialogueLine("อาคาริ", "\"(นิ่งไปครู่หนึ่งก่อนจะเงยหน้าสบตาคุณ) \"ขอบคุณนะที่บอกแบบนั้น... ปกติมีแต่คนบอกว่าต้องที่หนึ่งเท่านั้น จนฉันลืมไปเลยว่าความสนุกมันเป็นยังไง\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"งั้นพรุ่งนี้เรามาซ้อมแบบไม่เน้นความเร็ว แต่เน้นความสนุกกันดีกว่าไหม?\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("อาคาริ", "\"อาคาริ: \"อื้ม! ตกลง! ฉันจะวิ่งให้เต็มที่ในแบบของฉันเลยล่ะ!\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
                DialogueLine[] responseB3 = {
                    new DialogueLine("อาคาริ", "\"(หลุดขำแต่ดูเหมือนฝืนยิ้ม) \"นายเนี่ยนะ? ชุดมาสคอต? มันจะช่วยให้ฉันหายเครียดหรือจะทำให้ฉันอายคนทั้งโรงเรียนมากกว่ากันนะ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"อ้าว ผมกะจะสร้างสีสันให้เธอเลยนะเนี่ย\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("อาคาริ", "\"บางครั้งฉันก็ต้องการความจริงจังนะ... ไม่ใช่แค่มุกตลกไปวันๆ น่ะ แต่ก็ขอบใจนะที่พยายาม\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA3, responseB3);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("อาคาริ", "\"นายรู้ไหม... ทุกคนมองว่าฉันร่าเริง แต่ความจริงฉันกลัวมากเลย... ฉันเป็นความหวังของชมรม ถ้าวันวิ่งจริงฉันเกิดล้มขึ้นมาล่ะ?\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                    new DialogueLine("พระเอก", "\"อาคาริที่มั่นใจหายไปไหนแล้วเนี่ย? ต่อให้เธอจะล้ม ผมก็จะรอรับเธอที่เส้นชัยเอง เชื่อใจผมสิ\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
                };
                advanceDialogue();
                break;
            
            case 4:
                setBackgroundImage("image\\_school_in_spring_2.jpg");
                choice1.setText("ถ้างั้นขอจองตำแหน่งถังออกซิเจนนี้ยาวๆ เลยได้มั้ย?");
                choice2.setText("งั้นวันงานต้องจ่ายค่าออกซิเจนเป็นรอยยิ้มสวยๆ ด้วยนะ");

                DialogueLine[] responseA4 = {
                    new DialogueLine("อาคาริ", "\"(กอดแขนคุณเบาๆ) \"พูดแล้วนะ! ห้ามไปเป็นถังออกซิเจนให้คนอื่นล่ะ เพราะฉันต้องการนายคนเดียว... เข้าใจมั้ย?\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"เข้าใจครับกัปตัน ผมเป็นถังส่วนตัวของเธอคนเดียวแน่นอน\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("อาคาริ", "\"ดีมาก! งั้นพรุ่งนี้เดินไปส่งฉันที่บ้านด้วยนะ ฉันมีอะไรจะบอก... แต่ขอเก็บไว้ก่อน\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
                DialogueLine[] responseB4 = {
                    new DialogueLine("อาคาริ", "\"(ยิ้มฝืนๆ) \"ได้อยู่แล้วล่ะ เรื่องยิ้มฉันถนัดที่สุดนี่นา นายต้องการแค่นั้นจริงๆ เหรอ?\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"ก็นั่นคือสิ่งที่ดีที่สุดในตัวเธอนี่นา ผมอยากเห็นมันตลอดไปเลย\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("อาคาริ", "\"อื้ม... นั่นสินะ ฉันก็คงมีดีแค่รอยยิ้มนี่แหละ กลับบ้านเถอะ พรุ่งนี้เจอกัน\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA4, responseB4);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("อาคาริ", "\"ขอบคุณนะที่อยู่เป็นเพื่อนจนป่านนี้... เวลาที่มีนายอยู่ด้วย ฉันรู้สึกว่าฉันวิ่งได้เร็วกว่าปกติอีกแฮะ นาย... เป็นเหมือน 'ถังออกซิเจน' ของฉันเลยล่ะมั้ง?\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                    new DialogueLine("พระเอก", "\"เปรียบเทียบซะเสียภาพลักษณ์หมดเลยนะ แต่ก็นะ ผมยินดีเป็นให้ทุกอย่างที่เธอต้องการนั่นแหละ\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
                };
                advanceDialogue();
                break;

            case 5:
                setBackgroundImage("image\\_school_ground_1.jpg");
                choice1.setText("ถอยไปเถอะรุ่นพี่ อาคาริเขาไม่ได้เลือกพี่ และพี่ก็ไม่มีสิทธิ์มาสั่งเธอ!");
                choice2.setText("เราไปจากตรงนี้กันเถอะอาคาริ อย่าไปแลกกับคนแบบนี้เลย");

                DialogueLine[] responseA5 = {
                    new DialogueLine("เคนจิ", "\"หึ! ฝากไว้ก่อนเถอะไอ้หน้าอ่อน พรุ่งนี้ฉันจะทำให้แกต้องเสียใจ!\"", "image\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
                    new DialogueLine("พระเอก", "\"เป็นอะไรไหมอาคาริ? ผมขอโทษที่มาช้านะ\"", "ads"),
                    new DialogueLine("อาคาริ", "\"ฮึก... ขอบคุณนะ... ฉันกลัวมากเลย แต่พอนายก้าวออกมาปกป้องฉัน ฉันก็รู้สึกว่าฉันไม่กลัวอะไรอีกแล้ว\"", "image\\c4a1d014-6e7f-4c79-b295-13a803c1a711.png")
                };
                DialogueLine[] responseB5 = {
                    new DialogueLine("เคนจิ", "\"ดูสิอาคาริ! ไอ้ขี้ขลาดนี่มันพาเธอหนีหางจุกตูดเลยว่ะ! ฮ่าๆๆ!\"", "image\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
                    new DialogueLine("พระเอก", "\"อาคาริ วิ่งเร็วเข้า เราต้องหนีจากเขา\"", "ads"),
                    new DialogueLine("อาคาริ", "\"พอที! นายเอาแต่พาฉันหนี! เขาดูถูกฉันขนาดนั้นแต่นายกลับไม่กล้าแม้แต่จะพูดอะไรเลยเหรอ? ฉันผิดหวังในตัวนายจริงๆ!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA5, responseB5);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("เคนจิ", "\"ไงจ๊ะ อาคาริ? ซ้อมไปก็เหนื่อยเปล่า มาเป็น 'ควีน' ของฉันในงานเทศกาลดีกว่าน่า พ่อฉันเป็นสปอนเซอร์รายใหญ่ของที่นี่นะ!\"", "image\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
                    new DialogueLine("อาคาริ", "\"ฉันบอกแล้วไงว่าไม่! เลิกใช้เงินของแกมาขู่ฉันสักที!\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
                };
                advanceDialogue();
                break;

            case 6:
                setBackgroundImage("image\\_cultural_club_room_3.jpg");
                choice1.setText("ถ้าชนะ ฉันขอรางวัลเป็น 'คนข้างๆ' ตลอดไปได้มั้ย?");
                choice2.setText("ฉันจะรอเธอตรงนี้ ไม่ว่าจะเกิดอะไรขึ้นก็ตาม");

                DialogueLine[] responseA6 = {
                    new DialogueLine("อาคาริ", "\"นั่นมัน... คำขอที่เอาแต่ใจที่สุดเลยนะ แต่ก็นะ... ฉันเตรียมคำตอบรอไว้ตั้งนานแล้วล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"แสดงว่าผมมีลุ้นใช่ไหมครับกัปตัน?\"", "ads"),
                    new DialogueLine("อาคาริ", "\"ลุ้นอะไรกันล่ะ นายจองตำแหน่งนั้นไว้ตั้งแต่วันแรกที่มาช่วยฉันยกกล่องแล้วล่ะ ตาบ้า!\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
                DialogueLine[] responseB6 = {
                    new DialogueLine("อาคาริ", "\"แค่นั้นเหรอ? นายจะรอแค่เพราะมันเป็นสัญญาเฉยๆ เหรอ?\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"ก็ผมอยากให้เธอสบายใจที่สุดไง ไม่ต้องกดดันเรื่องความสัมพันธ์ตอนนี้ก็ได้\"", "ads"),
                    new DialogueLine("อาคาริ", "\"นายเนี่ย... สุภาพบุรุษเกินไปจนน่าโมโหจริงๆ เลยนะ แต่เอาเถอะ ไว้คุยกันพรุ่งนี้หลังแข่งจบนะ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
                };

                setupChoiceButtons(15, -10, responseA6, responseB6);

                dialogueQueue = new DialogueLine[] {
                    new DialogueLine("อาคาริ", "\"ขอบคุณที่อยู่ข้างๆ ฉันมาตลอดนะ... พรุ่งนี้คือวันสุดท้ายแล้วนะ... ไม่ว่าผลจะเป็นยังไง... สัญญาได้ไหมว่าเราจะมาอยู่ตรงนี้ด้วยกันสองคน?\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
                advanceDialogue();
                break;
            
            case 7:
                setBackgroundImage("image\\Gemini_Generated_Image_oq0tuvoq0tuvoq0t.png"); // ฉากงานเทศกาลตอนกลางคืน
    
                // ตรวจสอบคะแนน (ถ้าคะแนนตั้งแต่ 80 ขึ้นไปให้เข้า Good Ending)
                if (currentGirl.getScore() >= 80) {
                dialogueQueue = endingData.getAkariGoodEnding();
                } else {
                // dialogueQueue = endingData.getAkariBadEnding();
                }
    
                // ซ่อนปุ่มเลือก เพราะตอนจบคือการอ่านบทสรุป
                choice1.setVisible(false);
                choice2.setVisible(false);
    
                advanceDialogue();
                break;

                }
            }

    private void applyFullscreenState() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isDisplayable()) dispose(); 
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
        // บังคับให้ระบบ Scale คำนวณใหม่ทันที
        updatePositions(); 
    }

    private ImageIcon getScaledIcon(String path, int targetHeight) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        double ratio = (double) img.getWidth(null) / img.getHeight(null);
        int targetWidth = (int) (targetHeight * ratio);
        return new ImageIcon(img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH));
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
    String title = getDayTitle(day);
    
    // เรียกใช้ Transition ก่อน แล้วค่อยรันเนื้อเรื่องใน Callback
    showDayTransition(day, title, () -> {
        if (heroName.equals("Akari")) akariStory(day);
    });
    }

    private void showGameEndSummary() {
    String message = "ขอบคุณที่ใช้เวลาร่วมกับ " + currentGirl.getName() + "\n" +
                     "คะแนนความสัมพันธ์สุดท้าย: " + currentGirl.getScore() + "%\n" +
                     "บทสรุป: " + (currentGirl.getScore() >= 80 ? "Good Ending" : "Normal Ending");
    
    JOptionPane.showMessageDialog(this, message, "จบการเดินทาง", JOptionPane.INFORMATION_MESSAGE);
    
    // กลับสู่หน้าจอเมนูหลัก
    new mainMenu(isFullscreen);
    this.dispose();
    }
}