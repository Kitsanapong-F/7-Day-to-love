public class Character {
    private String name;
    private int score = 0;

    public Character(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public int getScore() { return score; }

    // ปรับปรุง: ดักไม่ให้คะแนนติดลบ และแสดงผลใน Console เพื่อ Debug ง่ายขึ้น
    public void addScore(int delta) { 
        this.score += delta; 
        if (this.score < 0) this.score = 0; 
        System.out.println("[System] " + name + " current score: " + this.score);
    }

    public void setScore(int s) { 
        this.score = Math.max(0, s); 
    }

    // สำหรับเริ่มเกมใหม่
    public void reset() {
        this.score = 0;
    }
}