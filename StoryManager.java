public class StoryManager {
    public static void runAkari(VisualNovelUI ui, int day) {

        ui.setDialoguePointer(0);

        switch (day) {
            case 1:
                ui.runDayLogic(
                    storyData.getAkariDay1Backgroud(),
                    storyData.getAkariDay1story(),
                    storyData.getAkariDay1Choice(),
                    15, -10,
                    storyData.getAkariDay1ResponseA(),
                    storyData.getAkariDay1ResponseB()
                );
                break;
            
            case 2:
                ui.runDayLogic(
                    storyData.getAkariDay2Backgroud(),
                    storyData.getAkariDay2story(),
                    storyData.getAkariDay2Choice(),
                    15, -10,
                    storyData.getAkariDay2ResponseA(),
                    storyData.getAkariDay2ResponseB()
                );
                break;

            case 3:
                ui.runDayLogic(
                    storyData.getAkariDay3Backgroud(),
                    storyData.getAkariDay3story(),
                    storyData.getAkariDay3Choice(),
                    15, -10,
                    storyData.getAkariDay3ResponseA(),
                    storyData.getAkariDay3ResponseB()
                );
                break;
            
            case 4:
                ui.runDayLogic(
                    storyData.getAkariDay4Backgroud(),
                    storyData.getAkariDay4story(),
                    storyData.getAkariDay4Choice(),
                    15, -10,
                    storyData.getAkariDay4ResponseA(),
                    storyData.getAkariDay4ResponseB()
                );
                break;

            case 5:
                ui.runDayLogic(
                    storyData.getAkariDay5Backgroud(),
                    storyData.getAkariDay5story(),
                    storyData.getAkariDay5Choice(),
                    15, -10,
                    storyData.getAkariDay5ResponseA(),
                    storyData.getAkariDay5ResponseB()
                );
                break;

            case 6:
                ui.runDayLogic(
                    storyData.getAkariDay6Backgroud(),
                    storyData.getAkariDay6story(),
                    storyData.getAkariDay6Choice(),
                    15, -10,
                    storyData.getAkariDay6ResponseA(),
                    storyData.getAkariDay6ResponseB()
                );
                break;

            case 7:
                ui.setBackgroundImage("image\\Gemini_Generated_Image_oq0tuvoq0tuvoq0t.png");
                if (ui.getCurrentGirlScore() >= 80) {
                    ui.setDialogueQueue(endingData.getAkariGoodEnding());
                }
                else {
                    // ui.setDialogueQueue(endingData.getAkariBadEnding());
                }
                ui.hideChoices();
                ui.advanceDialogue();
                break;
        }   
    }
}
