public class DatingEvent {
    public static void startDate(playmain ui, String girlName, int currentDay) {
        // เลือกบทพูดตามตัวละคร 
        if (girlName.equals("Akari")) {
            // ui.setDialogueQueue(storyData.getAkariDateStory());
        }

        // ระบบ Dynamic Skip: วันปัจจุบัน + 2 [cite: 24]
        int skipTo = currentDay + 2; 
        ui.setNextDayTarget(skipTo); 
        
        ui.setResponseMode(true);
        ui.advanceDialogue();
    }
}