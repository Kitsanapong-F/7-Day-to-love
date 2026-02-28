public class Character {
    private String name;
    // เก็บคะแนนแยกสำหรับผู้เล่นสูงสุด 3 คน
    private int[] playerScores = new int[3]; 

    public Character(String name) {
        this.name = name;
        reset(); // รีเซ็ตคะแนนทุกคนเป็น 0 เมื่อเริ่มต้น
    }

    public String getName() { return name; }

    /**
     * ดึงคะแนนของผู้เล่นคนใดคนหนึ่ง
     * @param playerIndex ลำดับผู้เล่น (0, 1, 2)
     */
    public int getScore(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < playerScores.length) {
            return playerScores[playerIndex];
        }
        return 0;
    }

    /**
     * ดึงอาเรย์คะแนนทั้งหมด (ใช้สำหรับหาผู้ชนะใน StoryManager)
     */
    public int[] getScores() {
        return playerScores;
    }

    /**
     * เพิ่มหรือลดคะแนนให้ผู้เล่นเจาะจงรายคน
     */
    public void addScore(int playerIndex, int delta) { 
        if (playerIndex >= 0 && playerIndex < playerScores.length) {
            playerScores[playerIndex] += delta;
            
            // ป้องกันคะแนนติดลบ
            if (playerScores[playerIndex] < 0) {
                playerScores[playerIndex] = 0;
            }

            System.out.println("[System] " + name + " -> Player " + (playerIndex + 1) + 
                               " current score: " + playerScores[playerIndex]);
        }
    }

    public void setScore(int playerIndex, int s) { 
        if (playerIndex >= 0 && playerIndex < playerScores.length) {
            playerScores[playerIndex] = Math.max(0, s); 
        }
    }

    /**
     * รีเซ็ตคะแนนของผู้เล่นทุกคนเป็น 0
     */
    public void reset() {
        for (int i = 0; i < playerScores.length; i++) {
            playerScores[i] = 0;
        }
    }
}