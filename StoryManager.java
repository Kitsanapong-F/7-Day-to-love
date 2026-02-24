import javax.swing.*;

/**
 * StoryManager: ควบคุมลำดับเหตุการณ์ในเกม (Main Logic Controller)
 * ทำหน้าที่จัดการการเปลี่ยนวัน, เรียกใช้เนื้อเรื่องแต่ละตัวละคร และตัดสินฉากจบ
 */
public class StoryManager {
    private static String currentRoute = "Akari";

    public static void resetGame(playmain ui, String route) {
        currentRoute = route;
        // เริ่มต้นวันที่ 1
        runStory(ui, route, 1);
    }

    public static void processNextDay(playmain ui) {
        // ปรับเข้าสู่ช่วง Free Action ให้ผู้เล่นเลือกกิจกรรม
        ui.setEventMenuVisible(true);
        ui.earnAP();
        System.out.println("[System] Free Action Mode - Route: " + currentRoute);
    }

    public static void onChoiceSelected(playmain ui, int scoreChange) {
        if (ui != null && scoreChange > 0) {
            ui.earnAP(); // ให้รางวัลผู้เล่นที่เลือกคำตอบถูกด้วยการเพิ่ม Action Point
            System.out.println("[System] Correct Choice! AP Refilled.");
        }
    }

    public static void runStory(playmain ui, String girlName, int day) {
        if (ui == null) return;
        
        currentRoute = girlName;
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);

        // หากเป็นวันที่ 8 (หลังจากจบกิจกรรมคืนวันที่ 7) ให้รัน Logic สำหรับฉากจบโดยตรง
        if (day >= 8) {
            if (girlName.equals("Akari")) runAkari(ui, day);
            // สามารถเพิ่มรูทตัวละครอื่นๆ ได้ที่นี่
            return;
        }

        // แสดงแอนิเมชันเปลี่ยนวันก่อนเริ่มเนื้อเรื่อง
        String dayTitle = (day == 7) ? "The Final Day" : "Daily Life";
        ui.showDayTransition(day, dayTitle, () -> {
            switch (girlName) {
                case "Akari":  runAkari(ui, day);  break;
                case "Reina":  runReina(ui, day);  break;
                case "Shiori": runShiori(ui, day); break;
                default: 
                    System.err.println("Error: Route '" + girlName + "' not found!");
                    break;
            }
        });
    }

    public static void runAkari(playmain ui, int day) {
        if (ui == null) return;
        ui.setDialoguePointer(0);

        // เนื้อเรื่องปกติวันที่ 1-6
        if (day >= 1 && day <= 6) {
            ui.runDayLogic(
                storyData.getAkariDayBackground(day), 
                storyData.getAkariDayStory(day), 
                storyData.getAkariDayChoice(day), 
                15, -10, 
                storyData.getAkariDayResponseA(day), 
                storyData.getAkariDayResponseB(day)
            );
        } 
        // เมื่อจบวันที่ 6 แล้วเข้าสู่วันที่ 7 (Ending State)
        else if (day >= 7) {
            handleEnding(ui, "Akari");
        }
    }

    private static void handleEnding(playmain ui, String girlName) {
        ui.hideChoices();
        ui.setEventMenuVisible(false);
        
        int finalScore = ui.getCurrentGirlScore();
        ui.setResponseMode(true);
        ui.setNextDayTarget(99); // Flag พิเศษบอก playmain ว่าเมื่อจบไดอะล็อกนี้ให้เรียก finishGame
        
        System.out.println("[Ending Check] Final Score for " + girlName + ": " + finalScore);

        if (girlName.equals("Akari")) {
            if (finalScore >= 20) { // เงื่อนไข Good Ending
                ui.showDayTransition(7, "GOOD ENDING", () -> {
                    ui.setBackgroundImage("image\\Akari\\4dd1e007-648f-448d-8ccb-2a0d4d2885d0.png");
                    ui.setDialogueQueue(endingData.getAkariGoodEnding());
                });
            } else { // เงื่อนไข Bad Ending
                ui.showDayTransition(7, "BAD ENDING", () -> {
                    ui.setBackgroundImage("image\\bad ending\\Gemini_Generated_Image_f8nd7jf8nd7jf8nd.png");
                    ui.setDialogueQueue(endingData.getAkariBadEnding());
                });
            }
        }
    }

    /**
     * เมธอดต้องเป็น public เพื่อให้ playmain สามารถเรียกใช้ได้จากภายนอกคลาส
     */
    public static void finishGame(playmain ui) {
        JOptionPane.showMessageDialog(ui, "Your 7-day story has come to an end. Thank you for playing!");
        ui.dispose(); // ปิดหน้าต่างเกมปัจจุบัน
        
        // สลับไปยังหน้าเลือกตัวละคร
        SceneManager.switchScene(new CharacterSelection());
    }

    // --- รูทที่กำลังพัฒนา ---
    public static void runReina(playmain ui, int day) {
        JOptionPane.showMessageDialog(ui, "Reina's route is coming soon!");
        ui.dispose();
    }

    public static void runShiori(playmain ui, int day) {
        JOptionPane.showMessageDialog(ui, "Shiori's route is coming soon!");
        ui.dispose();
    }
}