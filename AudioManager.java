import javax.sound.sampled.*;

public class AudioManager {
    private static float sfxVolume = 0.7f; // ค่าเริ่มต้น 70%

    public static void playSound(String fileName) {
        try {
            var url = AudioManager.class.getResource("/sound/" + fileName);
            if (url == null) return;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // ปรับระดับเสียง Decibel ก่อนเล่น
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(sfxVolume <= 0 ? 0.0001 : sfxVolume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }

            clip.start();
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