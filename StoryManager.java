public class StoryManager {

    private static int currentDay = 1;
    private static String currentRoute = "Akari";

    public static void processNextDay(playmain ui) {
        if (currentDay < 7) {
            currentDay++; // เพิ่มวัน
            System.out.println("ระบบ: กำลังก้าวเข้าสู่ความสัมพันธ์วันที่ " + currentDay);
            
            // ตรวจสอบว่าต้องรันรูทของใคร
            if (currentRoute.equals("Akari")) {
                runAkari(ui, currentDay);
            } else if (currentRoute.equals("Reina")) {
                runReina(ui, currentDay);
            } else if (currentRoute.equals("Shiori")) {
                runShiori(ui, currentDay);
            }
        }
    }

    public static void onChoiceSelected(Object ui, int scoreChange) {
        if (scoreChange > 0) {
            if (ui instanceof playmain) {
                ((playmain) ui).earnAP(); // ให้ 1 AP เมื่อเลือกถูก
                // แสดงข้อความบอกผู้เล่นเล็กน้อย
                System.out.println("Excellent! You earned 1 AP.");
            }
        }
    }


    // Method สำหรับ Reset วัน (เช่น เริ่มเกมใหม่)
    public static void resetGame(playmain ui, String route) {
        currentDay = 1;
        currentRoute = route;
        if (route.equals("Akari")) runAkari(ui, 1);
        else if (route.equals("Reina")) runReina(ui, 1);
        else if (route.equals("Shiori")) runShiori(ui, 1);
    }
    public static void runStory(Object ui, String girlName, int day) {
        // รีเซ็ตตำแหน่งบทสนทนาเมื่อเป็น UI ที่รองรับ
        if (ui instanceof playmain) ((playmain) ui).setDialoguePointer(0);

        if (girlName.equals("Akari")) {
            if (ui instanceof playmain) runAkari((playmain) ui, day);
            else {
                // ถ้าเรียกจากที่อื่น ให้สร้างหน้าจอ playmain ใหม่
                playmain m = new playmain();
                m.display();
                runAkari(m, day);
            }
        } else if (girlName.equals("Reina")) {
            if (ui instanceof playmain) runReina((playmain) ui, day);
            else {
                playmain m = new playmain();
                m.display();
                runReina(m, day);
            }
        } else if (girlName.equals("Shiori")) {
            if (ui instanceof playmain) runShiori((playmain) ui, day);
            else {
                playmain m = new playmain();
                m.display();
                runShiori(m, day);
            }
        }
    }

    public static void runAkari(playmain ui, int day) {

        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);
        switch (day) {
            case 1:
                ui.runDayLogic(
                    storyData.getAkariDay1Backgroud(),
                    storyData.getAkariDay1story(),
                    storyData.getAkariDay1Choice(),
                    15, -10,
                    storyData.getAkariDay1ResponseA(),
                    storyData.getAkariDay1ResponseB()
                );
                break;
            
            case 2:
                ui.runDayLogic(
                    storyData.getAkariDay2Backgroud(),
                    storyData.getAkariDay2story(),
                    storyData.getAkariDay2Choice(),
                    15, -10,
                    storyData.getAkariDay2ResponseA(),
                    storyData.getAkariDay2ResponseB()
                );
                break;

            case 3:
                ui.runDayLogic(
                    storyData.getAkariDay3Backgroud(),
                    storyData.getAkariDay3story(),
                    storyData.getAkariDay3Choice(),
                    15, -10,
                    storyData.getAkariDay3ResponseA(),
                    storyData.getAkariDay3ResponseB()
                );
                break;
            
            case 4:
                ui.runDayLogic(
                    storyData.getAkariDay4Backgroud(),
                    storyData.getAkariDay4story(),
                    storyData.getAkariDay4Choice(),
                    15, -10,
                    storyData.getAkariDay4ResponseA(),
                    storyData.getAkariDay4ResponseB()
                );
                break;

            case 5:
                ui.runDayLogic(
                    storyData.getAkariDay5Backgroud(),
                    storyData.getAkariDay5story(),
                    storyData.getAkariDay5Choice(),
                    15, -10,
                    storyData.getAkariDay5ResponseA(),
                    storyData.getAkariDay5ResponseB()
                );
                break;

            case 6:
                ui.runDayLogic(
                    storyData.getAkariDay6Backgroud(),
                    storyData.getAkariDay6story(),
                    storyData.getAkariDay6Choice(),
                    15, -10,
                    storyData.getAkariDay6ResponseA(),
                    storyData.getAkariDay6ResponseB()
                );
                break;

            case 7:
                if (ui.getCurrentGirlScore() >= 80) {
                    ui.setBackgroundImage("image\\Akari\\Gemini_Generated_Image_oq0tuvoq0tuvoq0t.png");
                    ui.setDialogueQueue(endingData.getAkariGoodEnding());
                }
                else {
                    ui.setBackgroundImage("image\\bad ending\\Gemini_Generated_Image_f8nd7jf8nd7jf8nd.png");
                    ui.setDialogueQueue(endingData.getAkariBadEnding());
                }
                ui.hideChoices();
                ui.advanceDialogue();
                break;
        }   
    }

    public static void handleStoryChoice(Object ui, int scoreChange) {
        if (scoreChange > 0) {
            if (ui instanceof playmain) ((playmain) ui).earnAP(); // ให้รางวัล 1 AP เมื่อผู้เล่นตอบถูก (Right Choice)
        }
        // ... จัดการคะแนนความสัมพันธ์ต่อไป ...
    }

    public static void  runReina(playmain ui, int day) {
        System.out.println("Reina route not implemented yet (day " + day + ").");
        // TODO: implement Reina route (use playmain.runDayLogic or similar)
    }
    public static void  runShiori(playmain ui, int day) {
        System.out.println("Shiori route not implemented yet (day " + day + ").");
        // TODO: implement Shiori route
    }

}
