import javax.sound.sampled.*;

public class AudioManager {

    public static void playSound(String fileName) {
        try {
            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(
                            AudioManager.class.getResource("/sound/" + fileName)
                    );

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
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
}