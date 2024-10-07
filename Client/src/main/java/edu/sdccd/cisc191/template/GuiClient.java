package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Presents the user with the game graphical user interface
 */
public class GuiClient extends Application
{
    /**
     * launches the JavaFX application
     * @param args command line input parameters
     */
    public static void main(String[] args)
    {
        // launch the app
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // https://stackoverflow.com/a/36873768
        MainModel model = new MainModel();
        MainController controller = new MainController(model);
        MainView view = new MainView(controller, model);

        // create scene, stage, set title, and show
        Scene scene = new Scene(view.asParent());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Table GUI Client");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
