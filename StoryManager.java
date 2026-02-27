import javax.swing.*;

public class StoryManager {
    private static String currentRoute = "";

    // --- 1. เมธอดเริ่มต้นเกม (เรียกจาก CharacterSelection) ---
    public static void resetGame(JFrame ui, String route) {
        currentRoute = route;
        if (ui instanceof playmain) {
            runStory((playmain) ui, route, 1);
        } else if (ui instanceof playmainReina) {
            runReina((playmainReina) ui, 1);
        } else if (ui instanceof playmainShiori) {
            runShiori((playmainShiori) ui, 1);
        }
    }

    // --- 2. ระบบรันเนื้อเรื่องแยกตามประเภท UI (Overloading) ---

    // [ROUTE: AKARI] ใช้ playmain UI
    public static void runStory(playmain ui, String girlName, int day) {
        if (ui == null) return;
        currentRoute = girlName;
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);

        if (day >= 7) {
            handleEnding(ui, "Akari");
            return;
        }

        String dayTitle = "School Life";
        ui.showDayTransition(day, dayTitle, () -> {
            ui.runDayLogic(
                storyData.getAkariDayBackground(day), 
                storyData.getAkariDayStory(day), 
                storyData.getAkariDayChoice(day), 
                15, -10, 
                storyData.getAkariDayResponseA(day), 
                storyData.getAkariDayResponseB(day)
            );
        });
    }

    // [ROUTE: REINA] ใช้ playmainReina UI
    public static void runReina(playmainReina ui, int day) {
        if (ui == null) return;
        currentRoute = "Reina";
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);

        if (day >= 7) {
            handleEnding(ui, "Reina");
            return;
        }

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

    // [ROUTE: SHIORI] ใช้ playmainShiori UI
    public static void runShiori(playmainShiori ui, int day) {
        if (ui == null) return;
        currentRoute = "Shiori";
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);

        if (day >= 7) {
            handleEnding(ui, "Shiori");
            return;
        }

        String dayTitle = "Library Quiet Time"; 
        ui.showDayTransition(day, dayTitle, () -> {
            ui.runDayLogic(
                storyDataShiori.getShioriDayBackground(day), 
                storyDataShiori.getShioriDayStory(day), 
                storyDataShiori.getShioriDayChoice(day), 
                15, 0, 
                storyDataShiori.getShioriDayResponseA(day), 
                storyDataShiori.getShioriDayResponseB(day)
            );
        });
    }

    // --- 3. ระบบจัดการฉากจบ (Ending Logic) ---

    // Ending สำหรับ Akari
    private static void handleEnding(playmain ui, String girlName) {
        ui.hideChoices();
        ui.setEventMenuVisible(false);
        int finalScore = ui.getCurrentGirlScore();
        ui.setResponseMode(true);
        ui.setNextDayTarget(99); 

        if (finalScore >= 20) {
            ui.showDayTransition(7, "GOOD ENDING", () -> {
                ui.setBackgroundImage("image\\Akari\\good_end.png");
                ui.setDialogueQueue(endingData.getAkariGoodEnding());
            });
        } else {
            ui.showDayTransition(7, "BAD ENDING", () -> {
                ui.setBackgroundImage("image\\bad ending\\bad_end.png");
                ui.setDialogueQueue(endingData.getAkariBadEnding());
            });
        }
    }

    // Ending สำหรับ Reina
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
                ui.setBackgroundImage("image\\bad ending\\bad_end.png");
                ui.setDialogueQueue(endingData.getReinaBadEnding());
            });
        }
    }

    // Ending สำหรับ Shiori
    private static void handleEnding(playmainShiori ui, String girlName) {
        ui.hideChoices();
        ui.setEventMenuVisible(false);
        int finalScore = ui.getCurrentGirlScore();
        ui.setResponseMode(true);
        ui.setNextDayTarget(99); 

        if (finalScore >= 20) {
            ui.showDayTransition(7, "GOOD ENDING", () -> {
                ui.setBackgroundImage("image\\Shiori\\library_sunset.png");
                ui.setDialogueQueue(endingData.getShioriGoodEnding());
            });
        } else {
            ui.showDayTransition(7, "BAD ENDING", () -> {
                ui.setBackgroundImage("image\\bad ending\\bad_end.png");
                ui.setDialogueQueue(endingData.getShioriBadEnding());
            });
        }
    }

    // --- 4. เมธอดจบเกมและกลับหน้าหลัก ---
    public static void finishGame(JFrame ui) {
        JOptionPane.showMessageDialog(ui, "Your 7-day story has come to an end. Thank you for playing!");
        ui.dispose();
        SceneManager.switchScene(new CharacterSelection());
    }

    // ระบบเพิ่ม AP เมื่อเลือกตอบถูก
    public static void onChoiceSelected(playmain ui, int scoreChange) {
        if (ui != null && scoreChange > 0) ui.earnAP();
    }
}