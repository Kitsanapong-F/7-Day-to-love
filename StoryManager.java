import javax.swing.*;

public class StoryManager {
    private static String currentRoute = "";

    public static void runStory(playmain ui, String girlName, int day) {
        if (ui == null) return;
        currentRoute = girlName;
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);
        ui.playDayBGM(day);

        if (day >= 7) {
            handleEnding(ui, girlName);
            return;
        }

        String dayTitle = "Daily Life with " + girlName;
        ui.showDayTransition(day, dayTitle, () -> {
            if (girlName.equalsIgnoreCase("Akari")) {
                ui.runDayLogic(
                    storyData.getAkariDayBackground(day), 
                    storyData.getAkariDayStory(day), 
                    storyData.getAkariDayChoice(day), 
                    15, -10,
                    storyData.getAkariDayResponseA(day), 
                    storyData.getAkariDayResponseB(day)
                );
            } 
            else if (girlName.equalsIgnoreCase("Reina")) {
                ui.runDayLogic(
                    storyDataReina.getRaynaDayBackground(day), 
                    storyDataReina.getRaynaDayStory(day), 
                    storyDataReina.getRaynaDayChoice(day), 
                    20, -5, 
                    storyDataReina.getRaynaDayResponseA(day), 
                    storyDataReina.getRaynaDayResponseB(day)
                );
            } 
            else if (girlName.equalsIgnoreCase("Shiori")) {
                ui.runDayLogic(
                    storyDataShiori.getShioriDayBackground(day), 
                    storyDataShiori.getShioriDayStory(day), 
                    storyDataShiori.getShioriDayChoice(day), 
                    15, 0, 
                    storyDataShiori.getShioriDayResponseA(day), 
                    storyDataShiori.getShioriDayResponseB(day)
                );
            }
        });
    }

    public static void onChoiceSelected(playmain ui, int points) {
        ui.getCurrentGirl().addScore(ui.getCurrentPlayer(), points);
        if (points > 0) {
            ui.earnAP(); 
            System.out.println("[Log] Player " + (ui.getCurrentPlayer() + 1) + " +1 AP bonus!");
        }
    }

    public static void handleEnding(playmain ui, String girlName) {
        // ดึงคะแนนของผู้เล่นทุกคนออกมาหาผู้ชนะ
        int[] scores = ui.getCurrentGirl().getScores();
        int winner = 0;
        int maxScore = -100;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                winner = i;
            }
        }

        final int finalWinner = winner;
        final int finalScore = maxScore;

        ui.showDayTransition(7, "THE FINAL DAY", () -> {
            if (girlName.equalsIgnoreCase("Akari")) {
                if (finalScore >= 80) {
                    BGMManager.playBGM("Blue_Archive_Connected_Sky.wav");
                    ui.setBackgroundImage("image\\ending\\akari_happy.png");
                    ui.setDialogueQueue(endingData.getAkariGoodEnding()); // แก้ให้ตรงกับไฟล์ endingData.java
                } else {
                    BGMManager.playBGM("Skyfall.wav");
                    ui.setBackgroundImage("image\\bad ending\\bad_end.png");
                    ui.setDialogueQueue(endingData.getAkariBadEnding());
                }
            } 
            else if (girlName.equalsIgnoreCase("Reina")) {
                if (finalScore >= 90) {
                    BGMManager.playBGM("Blue_Archive_Connected_Sky.wav");
                    ui.setBackgroundImage("image\\ending\\reina_happy.png");
                    ui.setDialogueQueue(endingData.getReinaGoodEnding()); // แก้ให้ตรงกับไฟล์ endingData.java
                } else {
                    BGMManager.playBGM("Skyfall.wav");
                    ui.setBackgroundImage("image\\bad ending\\bad_end.png");
                    ui.setDialogueQueue(endingData.getReinaBadEnding());
                }
            }
            else if (girlName.equalsIgnoreCase("Shiori")) {
                if (finalScore >= 75) {
                    BGMManager.playBGM("Blue_Archive_Connected_Sky.wav");
                    ui.setBackgroundImage("image\\ending\\shiori_happy.png");
                    ui.setDialogueQueue(endingData.getShioriGoodEnding());
                } else {
                    BGMManager.playBGM("Skyfall.wav");
                    ui.setBackgroundImage("image\\bad ending\\bad_end.png");
                    ui.setDialogueQueue(endingData.getShioriGoodEnding());
                }
            }
            
            ui.setNextDayTarget(99); 
            JOptionPane.showMessageDialog(ui, "Player " + (finalWinner + 1) + " is the winner with " + finalScore + " points!");
        });
    }

    public static void finishGame(playmain ui) {
        JOptionPane.showMessageDialog(ui, "ขอบคุณที่เล่นเกม 7 Days to Love!\nคะแนนสุดท้ายของคุณจะถูกบันทึกไว้");
        ui.dispose();
        SceneManager.switchScene(new StartGame());
    }
}