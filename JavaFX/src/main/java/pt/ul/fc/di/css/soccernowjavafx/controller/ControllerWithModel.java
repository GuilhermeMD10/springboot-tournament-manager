package pt.ul.fc.di.css.soccernowjavafx.controller;

import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;



public interface ControllerWithModel {
    void initModel(Stage stage, DataModel model);

    Stage getStage();

    void setStage(Stage stage);
}
