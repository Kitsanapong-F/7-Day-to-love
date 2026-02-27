import javax.sound.sampled.*;

public class BGMManager {
    private static Clip bgmClip;
    private static float currentVolume = 0.7f; // ค่าเริ่มต้น 70%

    public static void playBGM(String fileName) {
        try {
            if (bgmClip != null && bgmClip.isRunning()) {
                bgmClip.stop();
                bgmClip.close();
            }

            var url = BGMManager.class.getResource("/bgm/" + fileName);
            if (url == null) {
                System.out.println("BGM file not found: " + fileName);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audio);
            
            applyVolume(); // ใช้ระดับเสียงปัจจุบัน
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVolume(float volume) {
        currentVolume = volume;
        applyVolume();
    }

    private static void applyVolume() {
        if (bgmClip != null && bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            // แปลง Linear (0.0 - 1.0) เป็น Decibels
            float dB = (float) (Math.log(currentVolume <= 0 ? 0.0001 : currentVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public static void stopBGM() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}