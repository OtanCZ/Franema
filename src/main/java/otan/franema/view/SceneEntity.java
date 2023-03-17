package otan.franema.view;

import otan.franema.FranemaApplication;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
public enum SceneEntity {
    MAIN_MENU("main-menu-view.fxml"),
    ADMIN_PANEL("admin-panel-view.fxml"),
    CINEMA_LIST("cinema-list-view.fxml"),
    CINEMA_INSPECT("cinema-inspect-view.fxml"),
    LOGIN("login-view.fxml");

    private String fxmlPath;
    SceneEntity(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public Scene getScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FranemaApplication.class.getResource(this.getFxmlPath()));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getRoot().requestFocus();
        return scene;
    }
}