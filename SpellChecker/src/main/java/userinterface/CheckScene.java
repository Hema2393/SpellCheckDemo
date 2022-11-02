package userinterface;

import domain.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * This class creates the scene where user can check the spelling of the text input.
 *
 * @author sallasal
 */
public class CheckScene {

    /**
     * Creates the scene that has spell checking feature
     * @param window is the main window of the graphical user interface
     * @param checkerService is the spell checker service instance
     * @return the scene that contains spell checking feature.
     */
    public Scene getCheckScene(Stage window, CheckerService checkerService) {
        ScrollPane scrollable = new ScrollPane();
        BorderPane checkerLayout = new BorderPane();

        VBox elements = new VBox();
        VBox falseWords = new VBox();
        elements.setPadding(new Insets(10, 10, 10, 10));
        falseWords.setPadding(new Insets(10, 10, 10, 10));

        elements.setSpacing(20);

        TextArea inputText = new TextArea();
        inputText.setWrapText(true);

        Button checkButton = new Button("Check spelling!");

        elements.getChildren().add(new Label("Add the text for spell checking!"));
        elements.getChildren().add(inputText);
        elements.getChildren().add(checkButton);

        checkButton.setOnAction((event) -> {
            falseWords.getChildren().clear();
            checkerService.clearCheckedWords();
            
            long startTime = System.nanoTime();

            String[] words = checkerService.getWords(inputText.getText());

            String[] suggestions = new String[10];
            cleanArray(suggestions);

            int count = 0;
            for (int i = 0; i < words.length; i++) {
                if (!checkerService.checkWordFromDictionary(words[i]) 
                        && checkerService.wordQualityIsGood(words[i]) 
                        && !checkerService.checkIfWordIsDuplicate(words[i])) {
                    suggestions = checkerService.getSuggestions(words[i]);

                    GridPane suggestionsPane = new GridPane();
                    suggestionsPane.setHgap(20);
                    suggestionsPane.setPadding(new Insets(10, 10, 10, 10));
                    ColumnConstraints columnWidth = new ColumnConstraints();
                    columnWidth.setMinWidth(60);
                    suggestionsPane.getColumnConstraints().add(columnWidth);

                    suggestionsPane.add(new Label(words[i]), 0, 0);

                    for (int j = 0; j < 10; j++) {
                        if (!suggestions[j].equals("-")) {
                            suggestionsPane.add(new Label(suggestions[j]), 1, j);
                        }
                    }

                    try {
                        falseWords.getChildren().add(suggestionsPane);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    count++;
                }
            }

            if (count == 0) {
                falseWords.getChildren().add(new Label("All good!"));
            } else {
                falseWords.getChildren().add(new Label("There were " + count + " errors found."));
            }
            
            checkerService.printDuration(startTime);
        });

        elements.getChildren().remove(falseWords);
        elements.getChildren().add(falseWords);

        checkerLayout.setCenter(elements);
        scrollable.setContent(checkerLayout);

        return new Scene(scrollable);

    }

     /**
     * Replace array values with "-" that is empty value in the program.
     * @param array is the array that needs to be cleaned
     * @return String array that is empty (all strings are "-").
     */
    private String[] cleanArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = "-";
        }

        return array;
    }

}
