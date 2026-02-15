public class Character {
    private String name;
    private int relationshipScore;
    private String currentSprite; // เก็บ Path ของภาพตัวละครปัจจุบัน

    public Character(String name, String initialSprite) {
        this.name = name;
        this.currentSprite = initialSprite;
        this.relationshipScore = 0; // เริ่มต้นที่ 0%
    }

    public void addScore(int points) { 
        this.relationshipScore += points; 
        if (this.relationshipScore > 100) this.relationshipScore = 100;
        if (this.relationshipScore < 0) this.relationshipScore = 0;
    }

    public int getScore() { return relationshipScore; }
    public String getName() { return name; }
    public String getSprite() { return currentSprite; }
    public void setSprite(String path) { this.currentSprite = path; }
}