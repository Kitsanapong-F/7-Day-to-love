import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame{
    public static void main(String[] args){
        showBackground();

    }



public static void showBackground(){
    
        JFrame frame = new JFrame();
        frame.setTitle("7-Day-to-love");
        frame.setSize(600,400);
        frame.setLayout(null);

        JButton button = new JButton("New Game");
        button.setBounds(250, 100, 100, 40);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                boolean loop = true;
                String name =JOptionPane.showInputDialog("Enter you name :");
                while (loop) {
                
               if(name == null || name.isEmpty() ){
                  JOptionPane.showMessageDialog(null, "Please enter your name","Warning",JOptionPane.ERROR_MESSAGE);
                  name =JOptionPane.showInputDialog("Enter you name :");
              
               }
                else {
                     loop = false;
                     
            }
        }

                int age = JOptionPane.showConfirmDialog(null, "Are you over 18 years old?", "Age Verification", JOptionPane.YES_NO_OPTION);
             
                if(age == JOptionPane.NO_OPTION){
                         JOptionPane.showMessageDialog(null, "You must be over 18 to play this game.","Access Denied",JOptionPane.ERROR_MESSAGE);
                         frame.dispose();

              }
                else {
                             JOptionPane.showMessageDialog(null, "Welcome " + name + "! Let's start the game.");
                            
                }
               

        }
        
        });

        frame.add(button);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    
}
}