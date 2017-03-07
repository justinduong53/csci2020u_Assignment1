import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.io.File;


/**
 * This is the Main Class to the Spam Filter Assignment. The program is designed to take a directory of data and locate
 * emails files within. Calculations are done to the files based on the words in the files and find out the probability
 * that a file is a spam email. Display all these results on a JavaFx UI
 *
 * @author  Justin Duong(100588398)
 * @version 1.0
 * @since   1/24/2017
 *
 */
public class Main extends Application {

    private TableView<TestFile> table;

    /**
     *
     * Start method is meant create the JavaFX UI and process for the data to be displayed. It is composed of a Stage
     * with a Table and a Grid. The table displays the data for the fileName, actualClass, guessClass, and the spam
     * probability. The grid provides the accuracy and percision display via Text Labels.
     *
     * @param primaryStage
     * @return Nothing.
     * @throws Exception
     */

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Spam Filter");

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);
        BorderPane layout = new BorderPane();

        table = new TableView<>();
        //TODO: Add some data to our table.
        table.setItems(DataSource.getData(mainDirectory));

        TableColumn<TestFile, String> fileColumn = new TableColumn<>("File");
        fileColumn.setMinWidth(350);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<TestFile, Integer> classColumn = new TableColumn<>("Actual Class");
        classColumn.setMinWidth(100);
        classColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        TableColumn<TestFile, Integer> guessColumn = new TableColumn<>("Guess Class");
        guessColumn.setMinWidth(100);
        guessColumn.setCellValueFactory(new PropertyValueFactory<>("guessClass"));

        TableColumn<TestFile, String> probColumn = new TableColumn<>("Spam Probability");
        probColumn.setMinWidth(250);
        probColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSpamProbRounded()));

        table.getColumns().add(fileColumn);
        table.getColumns().add(classColumn);
        table.getColumns().add(guessColumn);
        table.getColumns().add(probColumn);

        layout.setCenter(table);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setMinWidth(10);

        Label a = new Label("Accuracy:");
        grid.add(a, 0, 1);

        TextField accuracyField = new TextField();
        accuracyField.setText(""+DataSource.accur);
        accuracyField.setDisable(true);
        grid.add(accuracyField, 1, 1);

        Label p = new Label("Precision:");
        grid.add(p, 0, 2);

        TextField percisionField =  new TextField();
        percisionField.setText(""+DataSource.perci);
        percisionField.setDisable(true);
        grid.add(percisionField, 1, 2);

        layout.setBottom(grid);
        Scene scene = new Scene(layout, 800,600);
        primaryStage .setScene(scene);
        primaryStage.show();
    }

    /**
     * This is the main method
     * @param args Unused
     * @return Nothing.
     */
    public static void main(String[] args){
        launch(args);
    }
}
