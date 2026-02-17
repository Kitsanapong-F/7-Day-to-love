import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame{
    public static void main(String[] args){
        showBackground();


    }


public static void showBackground(){
  
    BaseFrame baseframe = new BaseFrame("7-Day-to-love");
    baseframe.setLayout(new GridBagLayout());
    baseframe.setBackgroundImage("img" + java.io.File.separator + "img login.png");
    
    
    JPanel freePanel = new JPanel();
    freePanel.setPreferredSize(new Dimension(1000, 400)); 
    freePanel.setBackground(new Color(0, 0, 0, 0)); 
    freePanel.setLayout(null); 
    freePanel.setOpaque(false);

    

  
    JButton button = new JButton("New Game");
    JButton exitButton = new JButton("Exit");

    BaseFrame.styleButton(button);
    BaseFrame.styleButton(exitButton);

    button.setBounds(800, 200, 150, 40);
    exitButton.setBounds(800, 250, 150, 40);

    button.setBorderPainted(false);
    exitButton.setBorderPainted(false);

    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
   
    freePanel.add(button);
    freePanel.add(exitButton);

   
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = null;
                boolean loop = true;
                
                while (loop) {
                     name =JOptionPane.showInputDialog(baseframe,"Enter you name :");

                    if(name == null){
                         return;
                    }
                    if(!name.trim().isEmpty() ){
                       
                        PlayerName playerName = new PlayerName(name);
                        new CharacterSelection(false);
                        // baseframe.dispose();
                        loop = false;
                        break;
              
                    }
                    else {
                    JOptionPane.showMessageDialog(baseframe, "Please enter your name to start the game.", "Input Required", JOptionPane.WARNING_MESSAGE);
                }  
            }            
    }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

       
   

        baseframe.add(freePanel, gbc);
        baseframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseframe.pack();
        baseframe.setLocationRelativeTo(null);
        baseframe.setVisible(true);
    

}


}


class PlayerName{
    String Name;

    public PlayerName(String name){
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    
    public void Welcome() {
        System.out.println("Player Name: " + getName());

    }

}