public class DialogueLine {
    // ปรับเป็น public หรือเพิ่ม Getter เพื่อให้ playmain และ StoryManager เรียกใช้ได้ง่าย
    public String characterName;
    public String text;
    public String spritePath;

    public DialogueLine(String name, String text, String path) {
        this.characterName = name;
        this.text = text;
        this.spritePath = path;
    }

    // (Optional) เมธอดสำหรับ Debug เพื่อเช็คค่าใน Console
    @Override
    public String toString() {
        return "[" + characterName + "]: " + text + " (Sprite: " + spritePath + ")";
    }
}