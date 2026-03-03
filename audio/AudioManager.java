package audio;
import javax.sound.sampled.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

public class AudioManager {
    private static float sfxVolume = 0.7f; // ค่าเริ่มต้น 70%
    private static final String SAVE_FILE = "settings.properties"; // ชื่อไฟล์ที่จะบันทึก

    // --- ส่วนที่เพิ่มใหม่: บันทึกค่าลงไฟล์ ---
    public static void saveSettings() {
        Properties props = new Properties();
        props.setProperty("sfxVolume", String.valueOf(sfxVolume));
        props.setProperty("bgmVolume", String.valueOf(BGMManager.getVolume())); // ดึงค่าจาก BGMManager

        try (OutputStream out = new FileOutputStream(SAVE_FILE)) {
            props.store(out, "7 Days to Love Settings");
            System.out.println("Settings Saved!");
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- ส่วนที่เพิ่มใหม่: โหลดค่าจากไฟล์ ---
    public static void loadSettings() {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(SAVE_FILE)) {
            props.load(in);
            sfxVolume = Float.parseFloat(props.getProperty("sfxVolume", "0.7"));
            BGMManager.setVolume(Float.parseFloat(props.getProperty("bgmVolume", "0.7")));
        } catch (IOException e) {
            System.out.println("No save file found, using defaults.");
        }
    }
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

public static float getSFXVolume() { return sfxVolume; }
}