import javax.sound.sampled.*;

public class BGMManager {

    private static Clip bgmClip;

    // เล่น BGM แบบวนลูป
    public static void playBGM(String fileName) {
        try {

            // ถ้ามี BGM เก่าอยู่ → หยุดก่อน
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

            bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // วนลูปตลอด
            bgmClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // หยุด BGM
    public static void stopBGM() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}

// เวลาเรียกใช้ให้ใช้ BGMManager.playBGM("ชื่อไฟล์.wav ในโฟลเดอร์ bgm"); เช่น BGMManager.playBGM("Blue_Archive_Aira.wav");