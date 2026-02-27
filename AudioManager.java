import java.net.URL;
import javax.sound.sampled.*;

public class AudioManager {

    public static void playSound(String path) {
        try {
            URL soundURL = AudioManager.class.getClassLoader().getResource(path);
            if (soundURL == null) {
                System.out.println("Sound not found: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// เวลาเรียกใช้ให้ใช้ AudioManager.playSound("ชื่อไฟล์.wav ในโฟลเดอร์ sound"); เช่น AudioManager.playSound("click.wav");