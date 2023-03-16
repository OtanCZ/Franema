package otan.franema.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import otan.franema.FranemaApplication;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class LoginController {
    public BorderPane loginPane;
    public Pane logoPane = new Pane();
    public Button loginButton = new Button();
    public TextField usernameField = new TextField();
    public PasswordField passwordField = new PasswordField();
    public Button exitButton = new Button();
    public VBox menu = new VBox();
    public VBox usernameBox = new VBox();
    public Label usernameLabel = new Label();
    public VBox passwordBox = new VBox();
    public Label passwordLabel = new Label();
    public HBox buttons = new HBox();

    @FXML
    public void initialize() {
        System.out.println("LoginController initialized!");
        logoPane.setBackground(new Background(new BackgroundImage(new Image("file:src/main/resources/otan/franema/images/logo.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(0.5, 1.0, true, true, false, false))));
        usernameField.setPromptText("Average Franta Uživatel");
        usernameLabel.setText("Username:");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER);

        passwordField.setPromptText("Asi 1234, nebo 12345, známe Frantu");
        passwordLabel.setText("Password:");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER);

        loginButton.setText("Login/Register");
        loginButton.setOnMouseClicked(this::loginButtonOnMouseClick);

        exitButton.setText("Exit");
        exitButton.setOnMouseClicked(this::exitButtonOnMouseClick);

        buttons.getChildren().addAll(loginButton, exitButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        menu.getChildren().addAll(usernameBox, passwordBox, buttons);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        loginPane.setCenter(menu);
        loginPane.setTop(logoPane);


        loginPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            logoPane.setPrefWidth(loginPane.getWidth());
            menu.setPrefWidth(loginPane.getWidth());
            if (loginPane.getWidth() > 500) {
                passwordField.setMaxWidth(loginPane.getWidth() / 2);
                usernameField.setMaxWidth(loginPane.getWidth() / 2);
            } else {
                passwordField.setMaxWidth(loginPane.getWidth());
                usernameField.setMaxWidth(loginPane.getWidth());
            }
        });
        loginPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            logoPane.setPrefHeight(loginPane.getHeight() * 0.2);
            menu.setPrefHeight(loginPane.getHeight() * 0.8);
        });
    }


    private void exitButtonOnMouseClick(MouseEvent mouseEvent) {
        System.out.println("Goodbye!");
        FranemaApplication.stageManager.close();
    }

    private void loginButtonOnMouseClick(MouseEvent mouseEvent) {
        System.out.println("Login button clicked!");
    }
}