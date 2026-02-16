import javax.swing.*;
import java.awt.*;

public class CharacterDetailFrame extends JFrame {

    public CharacterDetailFrame(String name) {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Character Detail");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel(name, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JTextArea text = new JTextArea();
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // ✅ แยกข้อมูลตามตัวละคร
        switch (name) {

            case "Akari":
                text.setText(
                        "Akari\n\n" +
                        "Your energetic childhood friend.\n\n" +
                        "- Loves outdoor activities\n" +
                        "- Always cheerful\n" +
                        "- Known you since childhood\n" +
                        "\n(Can add more backstory here later)"
                );
                break;

            case "Shiori":
                text.setText(
                        "Shiori\n\n" +
                        "The quiet, mysterious librarian.\n\n" +
                        "- Calm and intelligent\n" +
                        "- Loves books\n" +
                        "- Hard to read emotions\n" +
                        "\n(Can add more backstory here later)"
                );
                break;

            case "Reina":
                text.setText(
                        "Reina\n\n" +
                        "The proud school heiress.\n\n" +
                        "- Confident\n" +
                        "- Elegant\n" +
                        "- Competitive\n" +
                        "\n(Can add more backstory here later)"
                );
                break;

            default:
                text.setText("No data.");
        }

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(text), BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        add(close, BorderLayout.SOUTH);

        setVisible(true);
    }
}