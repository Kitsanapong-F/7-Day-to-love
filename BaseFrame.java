import javax.swing.*;
import java.awt.*;
public class BaseFrame extends JFrame{
    public BaseFrame(String title){
        
        setTitle(title);
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }

    public void display(){
        setVisible(true);
    }
}
