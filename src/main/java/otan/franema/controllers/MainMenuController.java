package otan.franema.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import otan.franema.FranemaApplication;
import otan.franema.view.SceneEntity;

public class MainMenuController {
    public BorderPane mainMenuPane;
    public Pane logoPane = new Pane();
    public Button logoutButton = new Button();
    public Button exitButton = new Button();
    public Button ticketsButton = new Button();
    public Button adminButton = new Button();
    public VBox menu = new VBox();
    public Label label = new Label();
    public HBox buttons = new HBox();

    @FXML
    public void initialize() {
        System.out.println("MainMenuController initialized!");
        logoPane.setBackground(new Background(new BackgroundImage(new Image("file:src/main/resources/otan/franema/images/logo.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(0.5, 1.0, true, true, false, false))));
        label.setText("Welcome to Franema, " + FranemaApplication.appProvider.getCurrentUser().getUsername() + "!");

        logoutButton.setText("Log out");
        logoutButton.setOnMouseClicked(this::logoutButtonOnMouseClick);

        exitButton.setText("Exit");
        exitButton.setOnMouseClicked(this::exitButtonOnMouseClick);
        
        ticketsButton.setText("View tickets");
        ticketsButton.setOnMouseClicked(this::ticketsButtonOnMouseClick);


        adminButton.setText("Admin panel");
        adminButton.setOnMouseClicked(this::adminButtonOnMouseClick);

        buttons.getChildren().addAll(logoutButton, exitButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        menu.getChildren().addAll(label);
        if (FranemaApplication.appProvider.getCurrentUser().isAdmin()) {
            menu.getChildren().add(adminButton);
        } else {
            menu.getChildren().add(ticketsButton);
        }
        menu.getChildren().add(buttons);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        mainMenuPane.setCenter(menu);
        mainMenuPane.setTop(logoPane);


        mainMenuPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            logoPane.setPrefWidth(mainMenuPane.getWidth());
            menu.setPrefWidth(mainMenuPane.getWidth());
        });
        mainMenuPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            logoPane.setPrefHeight(mainMenuPane.getHeight() * 0.2);
            menu.setPrefHeight(mainMenuPane.getHeight() * 0.8);
        });
    }

    private void adminButtonOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.stageManager.showScene(SceneEntity.ADMIN_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ticketsButtonOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.stageManager.showScene(SceneEntity.TICKET_LIST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void exitButtonOnMouseClick(MouseEvent mouseEvent) {
        System.out.println("Goodbye!");
        FranemaApplication.stageManager.close();
    }

    private void logoutButtonOnMouseClick(MouseEvent mouseEvent) {
        System.out.println("Log out button clicked!");
        try {
            System.out.println("Logging out!");
            FranemaApplication.appProvider.logOut();
            FranemaApplication.stageManager.showScene(SceneEntity.LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}