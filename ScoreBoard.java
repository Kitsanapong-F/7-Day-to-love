import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ScoreBoard extends BaseFrame {

    private int[] totalScores = new int[3];
    private int[][] dailyScores = new int[3][7];
    private int winnerIndex = -1;

    private JPanel cardPanel;
    private JScrollPane scrollPane;
    private JTable table;
    private JLabel winLabel;
    private JButton homeBtn;

    public ScoreBoard() {
        super("7 Days to Love - ScoreBoard");
        setupTestData();
        setBackgroundImage("image\\cover\\Gemini_Generated_Image_tvx0fotvx0fotvx0.png");
        initUI();
    }

    private void setupTestData() {
        java.util.Random rand = new java.util.Random();
        for (int p = 0; p < 3; p++) {
            int sum = 0;
            for (int d = 0; d < 7; d++) {
                dailyScores[p][d] = rand.nextInt(20) + 10;
                sum += dailyScores[p][d];
            }
            totalScores[p] = sum;
        }
        winnerIndex = (totalScores[0] > totalScores[1] && totalScores[0] > totalScores[2]) ? 0 :
                      (totalScores[1] > totalScores[2] ? 1 : 2);
    }

    private void initUI() {
        cardPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) cardPanel.add(createMiniCard(i));

        String[] columns = {"PLAYER", "DAY 1", "DAY 2", "DAY 3", "DAY 4", "DAY 5", "DAY 6", "DAY 7", "TOTAL"};
        Object[][] data = new Object[3][9];
        for (int p = 0; p < 3; p++) {
            data[p][0] = "Player " + (p + 1);
            for (int d = 0; d < 7; d++) data[p][d+1] = dailyScores[p][d];
            data[p][8] = totalScores[p];
        }

        // 1. ตั้งค่า Model ให้แก้ไขข้อมูลไม่ได้
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(model);
        styleTable(table);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // ปิด Scrollbar แนวนอนเพื่อบังคับให้ตารางยืดตามขนาด ScrollPane
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        winLabel = new JLabel("VICTORY BELONGS TO PLAYER " + (winnerIndex + 1), SwingConstants.CENTER);
        winLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        winLabel.setForeground(Color.YELLOW);

        homeBtn = new JButton("RETURN TO TITLE");
        styleButton(homeBtn);

        // เพิ่มคอมโพเนนต์ผ่านระบบพิกัดของ BaseFrame (สัดส่วน 1280x720)
        addComponent(cardPanel, 140, 130, 1000, 180);
        addComponent(scrollPane, 140, 350, 1000, 165);
        addComponent(winLabel, 0, 535, 1280, 40);
        addComponent(homeBtn, 515, 610, 250, 45);
    }

    private void styleTable(JTable table) {
        // 2. ตั้งค่าให้ขยายคอลัมน์อัตโนมัติเมื่อขนาดตารางเปลี่ยน
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        table.setRowHeight(40);
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));
        table.setShowGrid(false);

        // 3. ปิดการโต้ตอบทุกอย่าง (ห้ามเลือก, ห้ามลากสลับ, ห้ามรับโฟกัส)
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                // บังคับสถานะการวาดให้เป็นปกติเสมอ (ไม่มีสีไฮไลต์)
                super.getTableCellRendererComponent(table, value, false, false, row, column);
                this.setOpaque(true);
                this.setBackground(new Color(0, 0, 0, 160)); 
                this.setHorizontalAlignment(JLabel.CENTER);
                this.setForeground(row == winnerIndex ? Color.YELLOW : Color.WHITE);
                this.setBorder(null); 
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        // 4. จุดสำคัญ: เมื่อ BaseFrame สั่ง Update พิกัด ให้ตารางวาดขนาดใหม่ทันที
        if (table != null) {
            table.revalidate();
            table.doLayout();
        }
    }

    private JPanel createMiniCard(int idx) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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
        
        JLabel score = new JLabel(String.valueOf(totalScores[idx]), SwingConstants.CENTER);
        score.setFont(new Font("Impact", Font.PLAIN, 55));
        score.setForeground(Color.WHITE);
        
        card.add(name, BorderLayout.NORTH);
        card.add(score, BorderLayout.CENTER);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScoreBoard().setVisible(true));
    }
}