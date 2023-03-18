package otan.franema.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import otan.franema.FranemaApplication;
import otan.franema.view.SceneEntity;

public class AdminPanelController {
    public BorderPane adminPanelPane;
    public Button ticketsButton = new Button();
    public Button cinemasButton = new Button();
    public Button backButton = new Button();
    public VBox menu = new VBox();

    @FXML
    public void initialize() {
        System.out.println("AdminPanelController initialized!");
        ticketsButton.setText("Manage tickets");
        ticketsButton.setOnMouseClicked(this::ticketsButtonOnMouseClick);

        cinemasButton.setText("Manage cinemas");
        cinemasButton.setOnMouseClicked(this::cinemasButtonOnMouseClick);

        backButton.setText("Back to Main Menu");
        backButton.setOnMouseClicked(this::backButtonOnMouseClick);
        
        menu.getChildren().addAll(ticketsButton, cinemasButton, backButton);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        adminPanelPane.setCenter(menu);

        adminPanelPane.widthProperty().addListener((observable, oldValue, newValue) -> menu.setPrefWidth(adminPanelPane.getWidth()));

        adminPanelPane.heightProperty().addListener((observable, oldValue, newValue) -> menu.setPrefHeight(adminPanelPane.getHeight()));
    }

    private void backButtonOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.stageManager.showScene(SceneEntity.MAIN_MENU);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cinemasButtonOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.stageManager.showScene(SceneEntity.CINEMA_LIST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void ticketsButtonOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.stageManager.showScene(SceneEntity.TICKET_LIST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
