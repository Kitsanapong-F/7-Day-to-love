public class DatingEvent {
    public static void startDate(playmain ui, String girlName, int currentDay) {
        // 1. ดึงเนื้อเรื่องตามชื่อตัวละคร
        if (girlName.equals("Akari")) {
            ui.setBackgroundImage("image\\Akari\\Gemini_Generated_Image_jjep3zjjep3zjjep.png");
            ui.setDialogueQueue(storyData.getAkariDateStory()); 
        }

        
        
        // 2. ตั้งเป้าหมายการข้ามวัน (วันปัจจุบัน + 2)
        int skipTo = currentDay + 1; 
        ui.setNextDayTarget(skipTo); 
        
        ui.setResponseMode(true);
        ui.setEventMenuVisible(false); // ซ่อนปุ่มกิจกรรมขณะเดท [cite: 12]
        ui.advanceDialogue();
    }
}