import javax.swing.*;
import java.awt.*;

class CharacterPanel extends JPanel {
    private Image img;
    public CharacterPanel(String path) {
        img = new ImageIcon(path).getImage();
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void updateImage(String path) {
    if (path != null && !path.isEmpty() && !path.equals("ads")) {
        this.img = new ImageIcon(path).getImage();
        repaint();
    }
}
}