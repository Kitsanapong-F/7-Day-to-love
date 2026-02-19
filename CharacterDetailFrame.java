import javax.swing.*;
import java.awt.*;

public class CharacterDetailFrame extends JFrame {

    public CharacterDetailFrame(String name) {
        // 1. ตั้งค่าหน้าต่าง
        setTitle("Character Profile: " + name);
        setSize(550, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 2. ใช้ JPanel หลักสำหรับจัดวาง
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(30, 30, 35)); // สีพื้นหลังมืดๆ เท่ๆ
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 3. หัวข้อชื่อตัวละคร
        JLabel titleLabel = new JLabel(name, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 215, 0)); // สีทอง
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 4. พื้นที่แสดงเนื้อหา (JTextArea)
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Tahoma", Font.PLAIN, 18));
        infoArea.setBackground(new Color(45, 45, 50));
        infoArea.setForeground(Color.WHITE);
        infoArea.setMargin(new Insets(15, 15, 15, 15));

        // 5. ใส่ข้อมูลตามชื่อตัวละครที่รับมา
        infoArea.setText(getCharacterDescription(name));

        // 6. เพิ่ม ScrollPane เผื่อเนื้อหายาว
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 7. ปุ่มปิดหน้าต่าง
        JButton closeBtn = new JButton("CLOSE");
        BaseFrame.styleButton(closeBtn); // ใช้สไตล์ปุ่มจากคลาสแม่
        closeBtn.addActionListener(e -> {
        AudioManager.playSound("umamusume_back.wav"); // เสียงปิด
        dispose();
        });
        
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // เมธอดแยกสำหรับเก็บเนื้อเรื่อง/ข้อมูล (แก้ไขข้อมูลตรงนี้ได้เลย)
    private String getCharacterDescription(String name) {
        switch (name) {
            case "Akari":
                return "ชื่อ: อาคาริ (Akari)\n" +
                       "ฉายา: ยัยตัวแสบข้างบ้าน\n" +
                       "-----------------------------------\n" +
                       "อาคาริเป็นเพื่อนสมัยเด็กของคุณที่โตมาด้วยกัน " +
                       "เธอมักจะเป็นคนลากคุณออกไปทำกิจกรรมกลางแจ้งเสมอ " +
                       "แม้ภายนอกจะดูร่าเริงและซุ่มซ่าม แต่จริงๆ แล้วเธอ..." +
                       "\n\n[สเปกที่ชอบ]: คนที่ตามใจเธอและชอบกินไอศกรีมเหมือนกัน";

            case "Shiori":
                return "ชื่อ: ชิโอริ (Shiori)\n" +
                       "ฉายา: บรรณารักษ์ผู้เงียบขรึม\n" +
                       "-----------------------------------\n" +
                       "รุ่นพี่ปี 2 ที่ใช้ชีวิตส่วนใหญ่อยู่ในห้องสมุด " +
                       "เธอเป็นคนพูดน้อยและเข้าถึงยาก แต่ถ้าได้ลองคุยเรื่องหนังสือที่เธอชอบ " +
                       "เธอจะยอมเปิดใจให้คุณมากกว่าใคร..." +
                       "\n\n[สเปกที่ชอบ]: คนที่เงียบสงบและชอบอ่านหนังสือ";

            case "Reina":
                return "ชื่อ: เรอินะ (Reina)\n" +
                       "ฉายา: คุณหนูผู้เอาแต่ใจ\n" +
                       "-----------------------------------\n" +
                       "ทายาทตระกูลดังที่เพียบพร้อมไปทุกด้าน ทั้งการเรียนและหน้าตา " +
                       "เธอมักจะมองทุกคนด้วยสายตาเย็นชา แต่ลึกๆ แล้วเธอกำลังตามหาใครบางคน " +
                       "ที่มองเห็นตัวตนจริงๆ ของเธอที่อยู่ภายใต้หน้ากากคุณหนู..." +
                       "\n\n[สเปกที่ชอบ]: คนที่ซื่อสัตย์และไม่กลัวอำนาจของเธอ";

            default:
                return "ไม่มีข้อมูลของตัวละครนี้";
            }
        }
}