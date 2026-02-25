public class DatingEvent {
    
    // สำหรับ Akari (ใช้ playmain ดั้งเดิม)
    public static void startDate(playmain ui, String girlName, int currentDay) {
        if (girlName.equals("Akari")) {
            ui.setBackgroundImage("image\\Akari\\Gemini_Generated_Image_jjep3zjjep3zjjep.png");
            ui.setDialogueQueue(storyData.getAkariDateStory()); 
        }
        
        setupDateState(ui, currentDay);
    }

    // --- เพิ่มส่วนนี้: สำหรับ Reina (ใช้ playmainReina) ---
    public static void startDate(playmainReina ui, String girlName, int currentDay) {
        if (girlName.equals("Reina")) {
            // เปลี่ยนพื้นหลังเป็นสถานที่เดทของเรย์นะ (เช่น สวนสาธารณะหรือร้านกาแฟ)
            ui.setBackgroundImage("image\\Bgscene\\park_date.jpg"); 
            // ดึงเนื้อเรื่องเดทจาก storyDataReina
            ui.setDialogueQueue(storyDataReina.getReinaDateStory()); 
        }
        
        setupDateStateReina(ui, currentDay);
    }

    // Helper เพื่อลดโค้ดซ้ำซ้อนสำหรับ Akari
    private static void setupDateState(playmain ui, int currentDay) {
        ui.setNextDayTarget(currentDay + 1); 
        ui.setResponseMode(true);
        ui.setEventMenuVisible(false);
    }

    // Helper เพื่อลดโค้ดซ้ำซ้อนสำหรับ Reina
    private static void setupDateStateReina(playmainReina ui, int currentDay) {
        ui.setNextDayTarget(currentDay + 1); 
        ui.setResponseMode(true);
        ui.setEventMenuVisible(false);
    }
}