package otan.franema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import otan.franema.view.SceneEntity;
import otan.franema.view.StageManager;

import java.io.IOException;

public class FranemaApplication extends Application {
    public static StageManager stageManager;
    @Override
    public void start(Stage stage) throws Exception {
        this.stageManager = new StageManager(stage);

        stageManager.showScene(SceneEntity.LOGIN);
    }

    public static void main(String[] args) {
        launch();
    }
}