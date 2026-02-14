import javax.swing.*;
import java.awt.*;
public class BaseFrame extends JFrame{

    protected JLabel backgroundLabel;
    
    public BaseFrame(String title){
        
        setTitle(title);
        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1280,720));
        setContentPane(layeredPane);
        setLayout(null);
    }
    
    public void setBackgroundImage(String imagePath){
        if(backgroundLabel != null){
            getLayeredPane().remove(backgroundLabel);
        }
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
        
        backgroundLabel = new JLabel(new ImageIcon(img));
        backgroundLabel.setBounds(0, 0, 1280, 720);

        // เพิ่มลงใน Layer ต่ำที่สุด (DEFAULT_LAYER)
        getLayeredPane().add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        
        // สั่งให้วาดหน้าจอใหม่
        revalidate(); 
        repaint();
    }

    public void display(){
        setVisible(true);
    }
}


//DEFAULT_LAYER = พื้นหลัง

//PALETTE_LAYER = ปุ่มและตัวละคร

//MODAL_LAYER = กล่องข้อความ (Dialogue Box)