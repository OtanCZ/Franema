package otan.franema.view;

import javafx.stage.Stage;

public class StageManager {
    private Stage stage;
    private SceneEntity currentScene;

    public StageManager(Stage stage) {
        this.stage = stage;
        stage.setMaximized(true);
    }

    public void showScene(SceneEntity sceneEntity) throws Exception {
        stage.setScene(sceneEntity.getScene());
        this.currentScene = sceneEntity;
        stage.show();
        stage.setWidth(stage.getWidth());
        System.out.println("Switched scene to " + sceneEntity.name());
    }

    public void showPopup(SceneEntity sceneEntity) throws Exception {
        System.out.println("Showing popup of " + sceneEntity.name());
        Stage popup = new Stage();
        popup.initOwner(stage);
        popup.setScene(sceneEntity.getScene());
        popup.setWidth(stage.getWidth()/2);
        popup.setHeight(stage.getHeight()/2);
        popup.show();
    }

    public void closePopup(Stage popup) {
        popup.close();
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