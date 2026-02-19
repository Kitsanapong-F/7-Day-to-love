public class StoryManager {

    private static int currentDay = 1;
    private static String currentRoute = "Akari";

    public static void processNextDay(playAkari ui) {
        if (currentDay < 7) {
            currentDay++; // เพิ่มวัน
            System.out.println("ระบบ: กำลังเข้าสู่ความสัมพันธ์วันที่ " + currentDay);
            
            // ตรวจสอบว่าต้องรันรูทของใคร
            if (currentRoute.equals("Akari")) {
                runAkari(ui, currentDay);
            } else if (currentRoute.equals("Rayna")) {
                runRayna(ui, currentDay);
            } else if (currentRoute.equals("Shiori")) {
                runShiori(ui, currentDay);
            }
        }
    }


    // Method สำหรับ Reset วัน (เช่น เริ่มเกมใหม่)
    public static void resetGame(playAkari ui, String route) {
        currentDay = 1;
        currentRoute = route;
        if (route.equals("Akari")) runAkari(ui, 1);
        else if (route.equals("Rayna")) runRayna(ui, 1);
        else if (route.equals("Shiori")) runShiori(ui, 1);
    }

    public static void  runRayna(playAkari ui, int day) {}
    public static void  runShiori(playAkari ui, int day) {}

    public static void runAkari(playAkari ui, int day) {

        ui.setDialoguePointer(0);

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
                    ui.setBackgroundImage("image\\Place\\Naohiro.jpg");
                    ui.setDialogueQueue(endingData.getAkariBadEnding());
                }
                ui.hideChoices();
                ui.advanceDialogue();
                break;
        }   
    }
}
