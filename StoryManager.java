import javax.swing.*;

public class StoryManager {
    private static String currentRoute = "Akari";

    // --- เมธอดเริ่มต้นเกม (เรียกจาก CharacterSelection) ---
    public static void resetGame(playmain ui, String route) {
        currentRoute = route;
        runStory(ui, route, 1);
    }

    // --- ระบบรันเนื้อเรื่องแยกตามประเภท UI (Overloading) ---
    
    // สำหรับรูทปกติ (Akari / Shiori)
    public static void runStory(playmain ui, String girlName, int day) {
        if (ui == null) return;
        currentRoute = girlName;
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);

        if (day >= 7) { // เข้าสู่ช่วงจบเกม
            if (girlName.equals("Akari")) handleEnding(ui, "Akari");
            return;
        }

        String dayTitle = "Daily Life";
        ui.showDayTransition(day, dayTitle, () -> {
            if (girlName.equals("Akari")) runAkari(ui, day);
            else if (girlName.equals("Shiori")) runShiori(ui, day);
        });
    }

    // สำหรับรูท Reina (ใช้ playmainReina UI)
   public static void runReina(playmainReina ui, int day) {
    if (ui == null) return;
    ui.setDialoguePointer(0);
    ui.setEventMenuVisible(false);

    if (day >= 7) {
        handleEnding(ui, "Reina");
        return;
    }

    // เพิ่ม Transition เพื่อให้เหมือน playmain
    String dayTitle = "Student Council Work"; 
    ui.showDayTransition(day, dayTitle, () -> {
        ui.runDayLogic(
            storyDataReina.getRaynaDayBackground(day), 
            storyDataReina.getRaynaDayStory(day), 
            storyDataReina.getRaynaDayChoice(day), 
            20, -5, 
            storyDataReina.getRaynaDayResponseA(day), 
            storyDataReina.getRaynaDayResponseB(day)
        );
    });
}
    // --- รูท Akari ---
    public static void runAkari(playmain ui, int day) {
        ui.runDayLogic(
            storyData.getAkariDayBackground(day), 
            storyData.getAkariDayStory(day), 
            storyData.getAkariDayChoice(day), 
            15, -10, 
            storyData.getAkariDayResponseA(day), 
            storyData.getAkariDayResponseB(day)
        );
    }

    public static void runShiori(playmain ui, int day) {
        JOptionPane.showMessageDialog(ui, "Shiori's route is coming soon!");
        ui.dispose();
        SceneManager.switchScene(new CharacterSelection());
    }

    // --- ระบบจัดการฉากจบ (Ending Logic) ---

    // ฉากจบ Akari (playmain)
    private static void handleEnding(playmain ui, String girlName) {
        ui.hideChoices();
        ui.setEventMenuVisible(false);
        int finalScore = ui.getCurrentGirlScore();
        ui.setResponseMode(true);
        ui.setNextDayTarget(99); 

        if (finalScore >= 20) {
            ui.showDayTransition(7, "GOOD ENDING", () -> {
                ui.setBackgroundImage("image\\Akari\\4dd1e007-648f-448d-8ccb-2a0d4d2885d0.png");
                ui.setDialogueQueue(endingData.getAkariGoodEnding());
            });
        } else {
            ui.showDayTransition(7, "BAD ENDING", () -> {
                ui.setBackgroundImage("image\\bad ending\\Gemini_Generated_Image_f8nd7jf8nd7jf8nd.png");
                ui.setDialogueQueue(endingData.getAkariBadEnding());
            });
        }
    }

    // ฉากจบ Reina (playmainReina)
    private static void handleEnding(playmainReina ui, String girlName) {
        ui.hideChoices();
        ui.setEventMenuVisible(false);
        int finalScore = ui.getCurrentGirlScore();
        ui.setResponseMode(true);
        ui.setNextDayTarget(99); 

        if (finalScore >= 25) {
            ui.showDayTransition(7, "GOOD ENDING", () -> {
                ui.setBackgroundImage("image\\Bgscene\\student_council_room.jpg");
                ui.setDialogueQueue(endingData.getReinaGoodEnding());
            });
        } else {
            ui.showDayTransition(7, "BAD ENDING", () -> {
                ui.setBackgroundImage("image\\bad ending\\Gemini_Generated_Image_f8nd7jf8nd7jf8nd.png");
                ui.setDialogueQueue(endingData.getReinaBadEnding());
            });
        }
    }

    // --- จบเกมและกลับหน้าเลือกตัวละคร ---
    public static void finishGame(JFrame ui) {
        JOptionPane.showMessageDialog(ui, "Your 7-day story has come to an end. Thank you for playing!");
        ui.dispose();
        SceneManager.switchScene(new CharacterSelection());
    }

    // ระบบเพิ่ม AP เมื่อเลือกตอบถูก (สำหรับ playmain)
    public static void onChoiceSelected(playmain ui, int scoreChange) {
        if (ui != null && scoreChange > 0) ui.earnAP();
    }
}