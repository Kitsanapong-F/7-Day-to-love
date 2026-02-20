public class DatingEvent {
    public static void startDate(playmain ui, String girlName, int currentDay) {
        // 1. ตรวจสอบว่ามีเนื้อเรื่องการเดทสำหรับวันนั้นๆ หรือไม่
        DialogueLine[] dateStory = null;

        if (girlName.equals("Akari")) {
            // เรียกใช้ Method ที่เราควรจะเพิ่มใน storyData (ตัวอย่างด้านล่าง)
            dateStory = getAkariDateContent(currentDay);
        }

        if (dateStory != null) {
            // 2. ปิดเมนูคำสั่งเพื่อเข้าสู่โหมดเนื้อเรื่อง
            ui.setEventMenuVisible(false);
            
            // 3. ตั้งค่าคิวบทสนทนา (จะเริ่มแสดงผลทันที)
            ui.setDialogueQueue(dateStory);

            // 4. ระบบ Dynamic Skip: ข้ามไป 2 วัน
            // ต้องดักไม่ให้เกินวันที่ 7 (ฉากจบ)
            int skipTo = Math.min(currentDay + 2, 7); 
            ui.setNextDayTarget(skipTo); 

            // 5. โบนัสคะแนนพิเศษสำหรับการไปเดท
            // สมมติว่าได้คะแนนพื้นฐาน 20 แต้ม
            if (ui instanceof playmain) {
                ((playmain)ui).earnAP(); // อาจจะคืน AP ให้ 1 หน่วย หรือเพิ่ม Score โดยตรง
            }
        } else {
            // กรณีไม่มีเนื้อเรื่องเดทในวันนั้น (เช่น วันที่ 6-7 อาจจะติด Event ใหญ่)
            javax.swing.JOptionPane.showMessageDialog(ui, 
                girlName + " is too busy for a date today!", 
                "Busy Schedule", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // เมธอดจำลองเนื้อหาการเดท (สามารถย้ายไปไว้ใน storyData ได้)
    private static DialogueLine[] getAkariDateContent(int day) {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"เอ๊ะ? จะพาฉันไปเดทเหรอ? ไปสิ! ฉันรู้จักร้านเครปอร่อยๆ แถวนี้ด้วยนะ!\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
            new DialogueLine("System", "คุณใช้เวลาทั้งวันเดินเที่ยวกับอาคาริอย่างมีความสุข (เวลาผ่านไป 2 วัน)", ""),
            new DialogueLine("อาคาริ", "\"ขอบคุณนะสำหรับวันนี้... ไว้เรามาด้วยกันอีกนะ!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
}