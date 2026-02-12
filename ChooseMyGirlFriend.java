import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseMyGirlFriend {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JButton button1 = new JButton();
        JButton button2 = new JButton();
        JButton button3 = new JButton();
        JLabel label = new JLabel();

  
        frame.setTitle("Choose My Girlfriend");
        frame.setSize(600,400);
        frame.setLayout(null);
        label.setText("Choose Your Girlfriend");
        label.setBounds(240,300,200,40);

        button1.setText("alice");
        button1.setBounds(100, 200, 100, 40);

        button2.setText("wipawadee");
        button2.setBounds(250, 200, 100, 40);

        button3.setText("amanda");
        button3.setBounds(400, 200, 100, 40);

        frame.add(label);
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    
}
}