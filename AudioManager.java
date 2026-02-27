import javax.sound.sampled.*;
import java.net.URL;

public class AudioManager {
    private static float sfxVolume = 0.7f; // ค่าเริ่มต้น 70%

    public static void playSound(String fileName) {
        try {
            // แก้ไข: ใช้ fileName ที่ส่งเข้ามา
            // หาก fileName มี path อยู่แล้ว (เช่น "sound/click.wav") ให้ใช้แค่ fileName
            // หากใส่แค่ชื่อไฟล์ (เช่น "click.wav") ให้ดึงจากโฟลเดอร์ /sound/
            URL url = AudioManager.class.getResource("/sound/" + fileName);
            
            // กรณีถ้าใส่ Path เต็มมาแล้ว url จะเป็น null ให้ลองโหลดโดยตรง
            if (url == null) {
                url = AudioManager.class.getResource(fileName);
            }
            
            if (url == null) {
                System.out.println("Audio file not found: " + fileName);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // ปรับระดับเสียง Decibel
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(sfxVolume <= 0 ? 0.0001 : sfxVolume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }

            clip.start();
            // ปิด clip ทันทีเมื่อเล่นจบเพื่อประหยัด Memory
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSFXVolume(float volume) {
        sfxVolume = volume;
    }
}