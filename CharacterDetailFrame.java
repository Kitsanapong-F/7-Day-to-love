import java.awt.*;
import javax.swing.*;

/**
 * คลาสสำหรับแสดงรายละเอียดโปรไฟล์ของตัวละครแต่ละคน
 * ออกแบบมาให้เปิดเป็นหน้าต่างแยก (Pop-up) โดยไม่ปิดโปรแกรมหลัก
 */
public class CharacterDetailFrame extends JFrame {

    public CharacterDetailFrame(String name) {
        // 1. ตั้งค่าหน้าต่างพื้นฐาน
        setTitle("Character Profile: " + name);
        setSize(550, 650);
        setLocationRelativeTo(null);
        // ใช้ DISPOSE_ON_CLOSE เพื่อให้ปิดเฉพาะหน้าต่างนี้ หน้าต่างหลักยังอยู่
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 2. JPanel หลัก (Main Container)
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(new Color(30, 30, 35)); // พื้นหลังโทนมืด
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 3. ส่วนหัว (Header) - ชื่อตัวละคร
        JLabel titleLabel = new JLabel(name.toUpperCase(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0)); // สีทองคำ
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 4. ส่วนเนื้อหา (Content) - ใช้ JTextArea แสดงข้อมูล
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Tahoma", Font.PLAIN, 18));
        infoArea.setBackground(new Color(45, 45, 50)); // สีกล่องข้อความ
        infoArea.setForeground(new Color(230, 230, 230)); // สีตัวอักษรเกือบขาว
        infoArea.setMargin(new Insets(20, 20, 20, 20));

        // ดึงข้อมูลคำอธิบายตัวละครตามชื่อที่ได้รับมา
        infoArea.setText(getCharacterDescription(name));

        // 5. ScrollPane - เพื่อรองรับกรณีเนื้อหายาวเกินหน้าต่าง
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12); // ปรับการเลื่อนให้ลื่นขึ้น
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 6. ปุ่มปิด (Footer)
        JButton closeBtn = new JButton("CLOSE PROFILE");
        // เรียกใช้ Static Method จาก BaseFrame เพื่อคุมธีมปุ่มให้เหมือนกันทั้งเกม
        BaseFrame.styleButton(closeBtn); 
        closeBtn.setPreferredSize(new Dimension(0, 50));
        closeBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_back.wav");
            dispose(); // ปิดเฉพาะหน้าต่างนี้
        });
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        // เพิ่ม Main Panel เข้าหน้าต่าง
        add(mainPanel);
        
        // แสดงผล
        setVisible(true);
    }

    /**
     * เมธอดสำหรับดึงข้อมูลเนื้อเรื่องและสเปกของตัวละคร
     */
    private String getCharacterDescription(String name) {
        switch (name) {
            case "Akari":
                return "ชื่อ: อาคาริ (Akari)\n" +
                       "ฉายา: ยัยตัวแสบข้างบ้าน\n" +
                       "--------------------------------------------------\n" +
                       "อาคาริเป็นเพื่อนสมัยเด็กของคุณที่โตมาด้วยกัน " +
                       "เธอมักจะเป็นคนลากคุณออกไปทำกิจกรรมกลางแจ้งเสมอ " +
                       "แม้ภายนอกจะดูร่าเริงและซุ่มซ่าม แต่จริงๆ แล้วเธอเป็นคน " +
                       "ที่คิดถึงความรู้สึกของคนรอบข้างมากกว่าใครๆ\n\n" +
                       "[สเปกที่ชอบ]: คนที่ตามใจเธอและชอบกินไอศกรีมเหมือนกัน";

            case "Shiori":
                return "ชื่อ: ชิโอริ (Shiori)\n" +
                       "ฉายา: บรรณารักษ์ผู้เงียบขรึม\n" +
                       "--------------------------------------------------\n" +
                       "รุ่นพี่ปี 2 ที่ใช้ชีวิตส่วนใหญ่อยู่ในห้องสมุด " +
                       "เธอเป็นคนพูดน้อยและเข้าถึงยาก แต่ถ้าได้ลองคุยเรื่องหนังสือที่เธอชอบ " +
                       "เธอจะยอมเปิดใจให้คุณมากกว่าใคร " +
                       "เบื้องหลังแว่นตานั้นมีความลับบางอย่างซ่อนอยู่...\n\n" +
                       "[สเปกที่ชอบ]: คนที่เงียบสงบและชอบอ่านหนังสือ";

            case "Reina":
                return "ชื่อ: เรอินะ (Reina)\n" +
                       "ฉายา: คุณหนูผู้เอาแต่ใจ\n" +
                       "--------------------------------------------------\n" +
                       "ทายาทตระกูลดังที่เพียบพร้อมไปทุกด้าน ทั้งการเรียนและหน้าตา " +
                       "เธอมักจะมองทุกคนด้วยสายตาเย็นชา แต่ลึกๆ แล้วเธอกำลังตามหาใครบางคน " +
                       "ที่มองเห็นตัวตนจริงๆ ของเธอที่อยู่ภายใต้หน้ากากคุณหนูผู้สมบูรณ์แบบ\n\n" +
                       "[สเปกที่ชอบ]: คนที่ซื่อสัตย์และไม่กลัวอำนาจของเธอ";

            default:
                return "ไม่มีข้อมูลของตัวละครนี้ในระบบ";
        }
    }
}