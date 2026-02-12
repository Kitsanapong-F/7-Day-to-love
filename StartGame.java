import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame{
    public static void main(String[] args){
        showBackground();


    }

public static void showBackground(){
    
       ChooseMyGirlFriend choose = new ChooseMyGirlFriend();
        JFrame frame = new JFrame();
        frame.setTitle("7-Day-to-love");
        frame.setSize(600,400);
        frame.setLayout(null);

        JButton button = new JButton("New Game");
        button.setBounds(250, 100, 100, 40);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = null;
                boolean loop = true;
                
                while (loop) {
                     name =JOptionPane.showInputDialog("Enter you name :");
                    if(name == null){
                         return;
                    }
                    if(!name.trim().isEmpty() ){
                        break;
              
                    }
                    
                       JOptionPane.showMessageDialog(null, "Please enter your name.","Invalid Input",JOptionPane.ERROR_MESSAGE);
                     
                }

                int age = JOptionPane.showConfirmDialog(null, "Are you over 18 years old?", "Age Verification", JOptionPane.YES_NO_OPTION);
                  if(age == JOptionPane.YES_OPTION){
                        JOptionPane.showMessageDialog(null, "Welcome " + name + "! Let's start the game.");
                         choose.main(null);
                   
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry, you must be over 18 to play this game.", "Age Restriction", JOptionPane.WARNING_MESSAGE);
             }
  
    }
        });

        frame.add(button);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    
}
}