import javax.swing.*;
import audio.BGMManager;
import java.util.ArrayList;
import java.util.List;

public class StoryManager {
    private static String currentRoute = "";

    public static void runStory(playmain ui, String girlName, int day) {
        if (ui == null) return;
        currentRoute = girlName;
        ui.setDialoguePointer(0);
        ui.setEventMenuVisible(false);
        ui.playDayBGM(day);

        // แก้ไข: เปลี่ยนจาก 7 เป็น 8 เพื่อให้วันที่ 7 เล่นเนื้อเรื่องได้จนจบ
        if (day >= 8) {
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
        List<Integer> playerQueue = new ArrayList<>();
        for (int i = 0; i < ui.getTotalPlayers(); i++) {
            playerQueue.add(i);
        }
        ui.startEndingSequence(playerQueue, girlName);
    }

    public static void finishGame(playmain ui) {
        ui.dispose();
        SceneManager.switchScene(new StartGame());
    }
}