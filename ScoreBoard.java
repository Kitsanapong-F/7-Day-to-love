import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import audio.AudioManager;

/**
 * ScoreBoard: หน้าจอสรุปคะแนนรวมหลังจากจบมินิเกม
 * แสดงผลคะแนนเนื้อเรื่อง โบนัสมินิเกม และประกาศผู้ชนะก่อนเข้าสู่ฉากจบ
 */
public class ScoreBoard extends BaseFrame {

    private Character currentGirl;
    private String girlName;
    private int[] miniBonus;
    private int winnerIndex = -1;

    private JPanel cardPanel;
    private JScrollPane scrollPane;
    private JTable table;
    private JLabel winLabel;
    private JButton endBtn; 

    public ScoreBoard(Character girl, String name, int[] bonus) {
        super("7 Days to Love - ScoreBoard");
        this.currentGirl = girl;
        this.girlName = name;
        this.miniBonus = bonus;

        // 1. อัปเดตคะแนนโบนัสจากมินิเกมลงในออบเจกต์ Character
        if (currentGirl != null) {
            for (int i = 0; i < 3; i++) {
                // ตรวจสอบเพื่อป้องกัน ArrayIndexOutOfBounds หากจำนวนผู้เล่นน้อยกว่า 3
                if (i < bonus.length) {
                    currentGirl.addScore(i, bonus[i]);
                }
            }
        }

        // 2. คำนวณหาผู้ชนะที่มีคะแนนรวมสูงสุด
        calculateWinner();
        
        // 3. ตั้งค่าพื้นหลังและสร้าง UI
        setBackgroundImage("image\\cover\\Gemini_Generated_Image_tvx0fotvx0fotvx0.png");
        initUI();
    }

    private void calculateWinner() {
        int[] scores = (currentGirl != null) ? currentGirl.getScores() : new int[]{0, 0, 0};
        winnerIndex = 0;
        // เปรียบเทียบคะแนนเพื่อหา Index ของผู้เล่นที่ได้คะแนนสูงสุด
        if (scores[1] > scores[winnerIndex]) winnerIndex = 1;
        if (scores[2] > scores[winnerIndex]) winnerIndex = 2;
    }

    private void initUI() {
        int[] finalScores = (currentGirl != null) ? currentGirl.getScores() : new int[]{0, 0, 0};

        // แสดง Card สรุปคะแนนด้านบน 3 ใบ
        cardPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) {
            cardPanel.add(createMiniCard(i, finalScores[i]));
        }

        // สร้างตารางสรุปรายละเอียดคะแนน
        String[] columns = {"PLAYER", "STORY SCORE", "MINI GAME", "TOTAL SCORE"};
        Object[][] data = new Object[3][4];
        for (int p = 0; p < 3; p++) {
            data[p][0] = "Player " + (p + 1);
            // คำนวณคะแนนเนื้อเรื่องเดิมโดยการลบโบนัสออก
            data[p][1] = finalScores[p] - (p < miniBonus.length ? miniBonus[p] : 0); 
            data[p][2] = "+" + (p < miniBonus.length ? miniBonus[p] : 0);            
            data[p][3] = finalScores[p];                                
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        styleTable(table);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        winLabel = new JLabel("VICTORY BELONGS TO PLAYER " + (winnerIndex + 1), SwingConstants.CENTER);
        winLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        winLabel.setForeground(Color.YELLOW);

        // --- จุดสำคัญ: ปุ่มนี้จะเริ่มระบบฉากจบต่อเนื่อง ---
        endBtn = new JButton("SEE YOUR ENDING");
        styleButton(endBtn);
       endBtn.addActionListener(e -> {
        AudioManager.playSound("umamusume_click.wav");
        
        // 1. ดึงจำนวนผู้เล่นจริงจากความยาวของ array miniBonus ที่ได้รับมา
        // ถ้าเล่น 1 คน actualPlayers จะมีค่าเท่ากับ 1
        int actualPlayers = miniBonus.length; 
        
        // 2. ส่งค่า actualPlayers เข้าไปแทนเลข 3 เดิม
        // เพื่อให้ playmain รู้ว่าต้องรันฉากจบกี่คน
        playmain finalScene = new playmain(currentGirl, actualPlayers);
        
        // 3. สลับหน้าจอและสั่งเริ่มคิวฉากจบตามปกติ
        SceneManager.switchScene(finalScene);
        StoryManager.handleEnding(finalScene, girlName);
        
        this.dispose();
    });

        // จัดวาง Component ลงบนหน้าจอ
        addComponent(cardPanel, 140, 100, 1000, 180);
        addComponent(scrollPane, 50, 320, 1180, 180); 
        addComponent(winLabel, 0, 520, 1280, 40);
        addComponent(endBtn, 515, 600, 250, 45);
    }

    private void styleTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        table.setRowHeight(45);
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));
        table.setShowGrid(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFocusable(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, false, false, row, column);
                this.setOpaque(true);
                // ตั้งค่าสีพื้นหลังแถวให้โปร่งแสง
                this.setBackground(new Color(0, 0, 0, 160)); 
                this.setHorizontalAlignment(JLabel.CENTER);
                // ไฮไลท์ตัวหนังสือสีเหลืองสำหรับผู้ชนะ
                this.setForeground(row == winnerIndex ? Color.YELLOW : Color.WHITE);
                this.setFont(new Font("Tahoma", Font.BOLD, 16));
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        if (table != null) {
            table.revalidate();
            table.doLayout();
        }
    }

    private JPanel createMiniCard(int idx, int scoreValue) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // วาดพื้นหลัง Card สี่เหลี่ยมมุมมน
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // วาดเส้นขอบสีเหลืองถ้าเป็นผู้ชนะ
                if (idx == winnerIndex) {
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 20, 20);
                }
            }
        };
        card.setOpaque(false);
        JLabel name = new JLabel("PLAYER " + (idx + 1), SwingConstants.CENTER);
        name.setForeground(idx == winnerIndex ? Color.YELLOW : Color.WHITE);
        name.setFont(new Font("Tahoma", Font.BOLD, 18));
        
        JLabel scoreLabel = new JLabel(String.valueOf(scoreValue), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Impact", Font.PLAIN, 55));
        scoreLabel.setForeground(Color.WHITE);
        
        card.add(name, BorderLayout.NORTH);
        card.add(scoreLabel, BorderLayout.CENTER);
        return card;
    }
}