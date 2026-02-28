public class DatingEvent {
    
    public static void startDate(playmain ui, String girlName, int currentDay) {
        // 1. เปลี่ยนพื้นหลังและดึงเนื้อเรื่องตามตัวละคร
        if (girlName.equalsIgnoreCase("Akari")) {
            ui.setBackgroundImage("image\\Akari\\Gemini_Generated_Image_jjep3zjjep3zjjep.png");
            ui.setDialogueQueue(storyData.getAkariDateStory()); 
        }
        else if (girlName.equalsIgnoreCase("Reina")) {
            ui.setBackgroundImage("image\\Reina\\Gemini_Generated_Image_t012sat012sat012.png"); 
            ui.setDialogueQueue(storyDataReina.getReinaDateStory()); 
        }
        else if (girlName.equalsIgnoreCase("Shiori")) {
            ui.setBackgroundImage("image\\Shiori\\Gemini_Generated_Image_qvcgu4qvcgu4qvcg.png"); 
            ui.setDialogueQueue(storyDataShiori.getShioriDateStory()); 
        }

        // 2. ตั้งค่าสถานะหลังจบ Event เดท (ข้ามวัน หรือ เข้าโหมดตอบสนอง)
        ui.setNextDayTarget(currentDay + 1); 
        ui.setResponseMode(true);
        ui.setEventMenuVisible(false);
        
        System.out.println("[Event] Dating started with " + girlName + " on Day " + currentDay);
    }
}