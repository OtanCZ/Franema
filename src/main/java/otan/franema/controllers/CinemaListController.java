package otan.franema.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import otan.franema.FranemaApplication;
import otan.franema.entities.CinemaEntity;
import otan.franema.view.SceneEntity;

public class CinemaListController {
    public BorderPane adminCinemaListPanelPane;
    public VBox cinemaList = new VBox();
    public HBox bottom = new HBox();
    public Button backButton = new Button();
    public Button createCinema = new Button();


    @FXML
    public void initialize() {
        System.out.println("cinemaListController initialized!");
        backButton.setText("Back to Admin Panel");
        backButton.setOnMouseClicked(this::backButtonOnMouseClick);

        createCinema.setText("Create Cinema");
        createCinema.setOnMouseClicked(this::createCinemaOnMouseClick);

        bottom.getChildren().add(backButton);
        if (FranemaApplication.appProvider.getCurrentUser().isAdmin()) {
            bottom.getChildren().add(createCinema);
        }
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);

        adminCinemaListPanelPane.setBottom(bottom);
        adminCinemaListPanelPane.setCenter(cinemaList);

        FranemaApplication.appProvider.getAllCinemas().forEach(cinema -> {
            Button button = new Button();
            button.setText(cinema.getName());
            button.setTooltip(new Tooltip(cinema.getId() + ""));
            button.setPrefWidth(adminCinemaListPanelPane.getWidth());
            button.setPrefHeight(adminCinemaListPanelPane.getPrefHeight() * 0.9 * 0.1);
            button.setTextFill(Color.BLACK);
            button.setOnMouseClicked(this::cinemaButtonOnMouseClick);
            cinemaList.getChildren().add(button);
        });

        adminCinemaListPanelPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            cinemaList.setPrefWidth(adminCinemaListPanelPane.getWidth());
            bottom.setPrefWidth(adminCinemaListPanelPane.getWidth());
            cinemaList.getChildren().forEach(node -> {
                Button button = (Button) node;
                button.setPrefWidth(adminCinemaListPanelPane.getWidth());
            });
        });

        adminCinemaListPanelPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            bottom.setPrefHeight(adminCinemaListPanelPane.getHeight() * 0.1);
            cinemaList.setPrefHeight(adminCinemaListPanelPane.getHeight() * 0.9);
        });
    }

    private void cinemaButtonOnMouseClick(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        FranemaApplication.appProvider.setCurrentCinema(FranemaApplication.appProvider.findCinemaById(Integer.parseInt(button.tooltipProperty().get().getText())));
        try {
            FranemaApplication.stageManager.showPopup(SceneEntity.CINEMA_INSPECT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createCinemaOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.appProvider.setCurrentCinema(new CinemaEntity(0, null, null));
            FranemaApplication.stageManager.showPopup(SceneEntity.CINEMA_INSPECT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void backButtonOnMouseClick(MouseEvent mouseEvent) {
        try {
            if (FranemaApplication.appProvider.getCurrentUser().isAdmin()) {
                FranemaApplication.stageManager.showScene(SceneEntity.ADMIN_PANEL);
            } else {
                FranemaApplication.stageManager.showScene(SceneEntity.MAIN_MENU);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
