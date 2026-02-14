import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame{
    public static void main(String[] args){
        showBackground();


    }

public static void showBackground(){
    JFrame frame = new JFrame("7-Day-to-love");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    
   BackgroundPanel backgroundPanel = new BackgroundPanel("img" + java.io.File.separator + "img login.png");
   backgroundPanel.setLayout(new GridBagLayout());

    
    JPanel freePanel = new JPanel();
    freePanel.setPreferredSize(new Dimension(200, 200)); 
    freePanel.setLayout(null); 
    freePanel.setBackground(new Color(0, 0, 0, 0)); 
    freePanel.setOpaque(false);

    

  
    JButton button = new JButton("New Game");
    button.setBounds(25, 100, 150, 40);

    JButton exitButton = new JButton("Exit");
    exitButton.setBounds(25, 150, 150, 40);


    button.setBorderPainted(false);
    button.setOpaque(true);


    exitButton.setBorderPainted(false);
    exitButton.setOpaque(true);

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
                     name =JOptionPane.showInputDialog("Enter you name :");
                    if(name == null){
                         return;
                    }
                    if(!name.trim().isEmpty() ){
                        name playerName = new name(name);
                        playerName.Welcome();
                       

                        break;
              
                    }
                    JOptionPane.showMessageDialog(frame, "Please enter your name to start the game.", "Input Required", JOptionPane.WARNING_MESSAGE);
                     return;
                }              
    }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

       
    Action toggleFullscreen = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          
            if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                frame.setExtendedState(JFrame.NORMAL);
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    };


        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke("F11"), "toggleFullscreen");
        frame.getRootPane().getActionMap().put("toggleFullscreen", toggleFullscreen);

        backgroundPanel.add(freePanel, gbc); 
        frame.add(backgroundPanel);          
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    
}
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String fileName) {
        backgroundImage = new ImageIcon(fileName).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}


class name{
    String Name;

    public name(String name){
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

   

   
    public void Welcome() {
        System.out.println("Player Name: " + getName());

    }

    
   
    
}