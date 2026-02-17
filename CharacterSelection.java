import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CharacterSelection extends JFrame {
    private boolean isFullscreen;
    private JLabel bioLabel;
    private JButton selectBtn;
    private String selectedName = "";
    private ArrayList<CharacterPanel> characterPanels = new ArrayList<>();

    public CharacterSelection(boolean startFullscreen) {
        this.isFullscreen = startFullscreen;
        setTitle("7 Days to Love - Character Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        applyFullscreenState();

        BackgroundPanel bg = new BackgroundPanel("image\\Bgscene\\_front_of_classroom_1.jpg");
        bg.setLayout(new BorderLayout());

        // 1. TOP: Navigation
        JButton backBtn = new JButton("<- BACK TO MENU");
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> {

           new StartGame(); //แก้เป็นหน้าเกมที่ นายต้นทำไว้
            this.dispose();
        });
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(backBtn);
        bg.add(top, BorderLayout.NORTH);

        // 2. CENTER: Character Grid
        JPanel centerGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        centerGrid.setOpaque(false);
        centerGrid.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        // Using your specific file names from the screenshot
        centerGrid.add(createGirlCard("Akari", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", "Your energetic childhood friend."));
        centerGrid.add(createGirlCard("Shiori", "image\\Shiori\\fa52fd12-fbd0-4119-8341-014c0b18c47a.png", "The quiet, mysterious librarian."));
        centerGrid.add(createGirlCard("Reina", "image\\Reina\\a1fd8ce0-99fb-4881-bbee-4677d1b32676.png", "The proud school heiress."));

        bg.add(centerGrid, BorderLayout.CENTER);

        // 3. SOUTH: Bio and Select (Below the characters)
        JPanel bottomUI = new JPanel();
        bottomUI.setLayout(new BoxLayout(bottomUI, BoxLayout.Y_AXIS));
        bottomUI.setOpaque(false);

        bioLabel = new JLabel("Click a heroine to learn her story...", SwingConstants.CENTER);
        bioLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        selectBtn = new JButton("SELECT");
        selectBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        selectBtn.setEnabled(false);
        selectBtn.setFocusable(false);
        selectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomUI.add(bioLabel);
        bottomUI.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomUI.add(selectBtn);
        bottomUI.add(Box.createRigidArea(new Dimension(0, 40)));
        bg.add(bottomUI, BorderLayout.SOUTH);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) toggleFullscreen();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
            }
        });

        add(bg);
        setVisible(true);
        this.requestFocus();
    }

    private JPanel createGirlCard(String name, String path, String bio) {
        CharacterPanel portrait = new CharacterPanel(path);
        characterPanels.add(portrait);

        portrait.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // RULE: Only one blue border at a time
                for (CharacterPanel p : characterPanels) p.setBorder(null);
                
                selectedName = name;
                portrait.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
                
                // Update Bio directly below
                bioLabel.setText("<html><center>" + bio + "</center></html>");
                selectBtn.setText("SPEND 7 DAYS WITH " + name.toUpperCase());
                selectBtn.setEnabled(true);
            }
        });
        return portrait;
    }

    private void applyFullscreenState() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isFullscreen) {
            dispose(); setUndecorated(true); gd.setFullScreenWindow(this);
        } else {
            dispose(); setUndecorated(false); gd.setFullScreenWindow(null);
            setSize(1280, 720); setLocationRelativeTo(null);
        }
    }

    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        applyFullscreenState();
        setVisible(true);
        this.requestFocus();
    }
}

// THE FIX FOR THE RATIO PROBLEM: A custom panel that handles image scaling
class CharacterPanel extends JPanel {
    private Image img;
    public CharacterPanel(String path) {
        this.img = new ImageIcon(path).getImage();
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            // FIX: Dynamic Scaling based on current panel height
            int h = (int)(getHeight() * 0.95); // Take 95% of the panel height
            double ratio = (double) h / img.getHeight(null);
            int w = (int) (img.getWidth(null) * ratio);
            int x = (getWidth() - w) / 2; // Center horizontally
            int y = getHeight() - h;      // Stick to bottom
            g.drawImage(img, x, y, w, h, this);
        }
    }
}