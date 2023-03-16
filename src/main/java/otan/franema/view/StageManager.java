package otan.franema.view;

import javafx.stage.Stage;

public class StageManager {
    private Stage stage;
    private SceneEntity currentScene;

    public StageManager(Stage stage) {
        this.stage = stage;
        stage.setMaximized(true);
    }

    public StageManager() {
        this.stage = new Stage();
    }

    public void showScene(SceneEntity sceneEntity) throws Exception {
        stage.setScene(sceneEntity.getScene());
        this.currentScene = sceneEntity;
        stage.show();
        stage.setWidth(stage.getWidth());
        System.out.println("Switched scene to " + sceneEntity.name());
    }

    public void close() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public SceneEntity getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(SceneEntity currentScene) {
        this.currentScene = currentScene;
    }

    public Stage getStage() {
        return stage;
    }
}