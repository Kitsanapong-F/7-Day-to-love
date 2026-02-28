public class Character {
    private String name;
    // เปลี่ยนจาก score ตัวเดียว เป็น Array สำหรับ 3 ผู้เล่น 
    private int[] playerScores = new int[3]; 

    public Character(String name) {
        this.name = name;
        reset(); // รีเซ็ตคะแนนทุกคนเป็น 0 เมื่อสร้างตัวละคร
    }

    public String getName() { return name; }

    // ดึงคะแนนตามลำดับผู้เล่น (0, 1, 2)
    public int getScore(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < 3) {
            return playerScores[playerIndex];
        }
        return 0;
    }

    /**
     * แก้ไข: รับค่า playerIndex เพื่อระบุว่าใครได้คะแนน 
     * และ delta คือคะแนนที่เพิ่มหรือลด
     */
    public void addScore(int playerIndex, int delta) { 
        if (playerIndex >= 0 && playerIndex < 3) {
            playerScores[playerIndex] += delta;
            
            // ป้องกันคะแนนติดลบ
            if (playerScores[playerIndex] < 0) {
                playerScores[playerIndex] = 0;
            }

            System.out.println("[System] " + name + " -> Player " + (playerIndex + 1) + 
                               " current score: " + playerScores[playerIndex]);
        }
    }

    // แก้ไข: ตั้งค่าคะแนนเจาะจงรายคน
    public void setScore(int playerIndex, int s) { 
        if (playerIndex >= 0 && playerIndex < 3) {
            playerScores[playerIndex] = Math.max(0, s); 
        }
    }

    /**
     * แก้ไข: รีเซ็ตคะแนนของผู้เล่นทุกคนเป็น 0
     */
    public void reset() {
        for (int i = 0; i < playerScores.length; i++) {
            playerScores[i] = 0;
        }
    }
}