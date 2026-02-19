public class Character {
    private String name;
    private int score = 0;

    public Character(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public void addScore(int delta) { this.score += delta; }
    public void setScore(int s) { this.score = s; }
}
