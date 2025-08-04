package pt.ul.fc.di.css.soccernowjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.controller.InitController;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;



//import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String prefix = "/pt/ul/fc/di/css/soccernowjavafx/view/";

        BorderPane root = new BorderPane();
        FXMLLoader initLoader = new FXMLLoader(getClass().getResource(prefix + "init.fxml"));
        root.setCenter(initLoader.load());
        //InitController initController = initLoader.getController();
        InitController controller = initLoader.getController();

        DataModel model = new DataModel();
        controller.initModel(primaryStage, model);

        Scene scene = new Scene(root, 230, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SoccerNow");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

