import javax.swing.*;

/**
 * StoryManager: ควบคุมลำดับเหตุการณ์ในเกม (Main Logic Controller)
 * จัดการการเปลี่ยนวัน, การเรียกใช้เนื้อเรื่อง และการตัดสินฉากจบ
 */
public class StoryManager {
    private static String currentRoute = "Akari";

    public static void resetGame(playmain ui, String route) {
        currentRoute = route;
        // เริ่มต้นวันที่ 1
        runStory(ui, route, 1);
    }

    public static void processNextDay(playmain ui) {
        // ปรับเข้าสู่ช่วง Free Action
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

        if (day >= 1 && day <= 6) {
            // ดึงข้อมูลจาก storyData มาใช้งาน (คะแนนบวก 15, ลบ 10)
            ui.runDayLogic(
                storyData.getAkariDayBackground(day), 
                storyData.getAkariDayStory(day), 
                storyData.getAkariDayChoice(day), 
                15, -10, 
                storyData.getAkariDayResponseA(day), 
                storyData.getAkariDayResponseB(day)
            );
        } else if (day == 7) {
            handleEnding(ui, "Akari");
        } else {
            finishGame(ui);
        }
    }

    private static void handleEnding(playmain ui, String girlName) {
        ui.hideChoices();
        ui.setEventMenuVisible(false);
        
        int finalScore = ui.getCurrentGirlScore();
        System.out.println("[Ending Check] Final Score for " + girlName + ": " + finalScore);

        if (girlName.equals("Akari")) {
            if (finalScore >= 80) {
                ui.setBackgroundImage("image\\Akari\\good_end_bg.png");
                ui.setDialogueQueue(endingData.getAkariGoodEnding());
            } else {
                ui.setBackgroundImage("image\\bad_ending\\bad_end_bg.png");
                ui.setDialogueQueue(endingData.getAkariBadEnding());
            }
        }
        // สามารถเพิ่มเงื่อนไข Shiori และ Reina ได้ที่นี่
    }

    private static void finishGame(playmain ui) {
        JOptionPane.showMessageDialog(ui, "Your 7-day story has come to an end. Thank you for playing!");
        System.exit(0);
    }

    // --- รูทที่กำลังพัฒนา ---
    public static void runReina(playmain ui, int day) {
        JOptionPane.showMessageDialog(ui, "Reina's route is coming soon in the next update!");
        ui.dispose();
    }

    public static void runShiori(playmain ui, int day) {
        JOptionPane.showMessageDialog(ui, "Shiori's route is coming soon in the next update!");
        ui.dispose();
    }
}