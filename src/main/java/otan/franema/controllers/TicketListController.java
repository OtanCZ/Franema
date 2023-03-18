package otan.franema.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import otan.franema.FranemaApplication;
import otan.franema.entities.TicketEntity;
import otan.franema.view.SceneEntity;

public class TicketListController {
    public BorderPane ticketListPanelPane;
    public VBox ticketList = new VBox();
    public HBox bottom = new HBox();
    public HBox top = new HBox();
    public ToggleGroup toggleGroup = new ToggleGroup();
    public ToggleButton ticketsAvailable = new ToggleButton();
    public ToggleButton userTickets = new ToggleButton();
    public Button backButton = new Button();
    public Button createTicket = new Button();


    @FXML
    public void initialize() {
        System.out.println("ticketListController initialized!");
        backButton.setText("Back");
        backButton.setOnMouseClicked(this::backButtonOnMouseClick);

        createTicket.setText("Create Ticket");
        createTicket.setOnMouseClicked(this::createTicketOnMouseClick);

        ticketsAvailable.setText("Tickets Available");
        ticketsAvailable.setOnMouseClicked(this::ticketsAvailableOnMouseClick);
        ticketsAvailable.selectedProperty().setValue(true);

        userTickets.setText("Bought Tickets");
        userTickets.setOnMouseClicked(this::userTicketsOnMouseClick);

        bottom.getChildren().add(backButton);
        if (FranemaApplication.appProvider.getCurrentUser().isAdmin()) {
            bottom.getChildren().add(createTicket);
        }
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);

        toggleGroup.getToggles().addAll(ticketsAvailable, userTickets);
        top.getChildren().addAll(ticketsAvailable, userTickets);
        top.setAlignment(Pos.CENTER);
        top.setSpacing(10);

        ticketListPanelPane.setTop(top);
        ticketListPanelPane.setBottom(bottom);
        ticketListPanelPane.setCenter(ticketList);

        ticketsAvailableOnMouseClick(null);
        ticketListPanelPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            ticketList.setPrefWidth(ticketListPanelPane.getWidth());
            bottom.setPrefWidth(ticketListPanelPane.getWidth());
            ticketList.getChildren().forEach(node -> {
                Button button = (Button) node;
                button.setPrefWidth(ticketListPanelPane.getWidth());
            });
        });

        ticketListPanelPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            bottom.setPrefHeight(ticketListPanelPane.getHeight() * 0.1);
            top.setPrefHeight(ticketListPanelPane.getHeight() * 0.1);
            ticketList.setPrefHeight(ticketListPanelPane.getHeight() * 0.8);
        });
    }

    private void userTicketsOnMouseClick(MouseEvent mouseEvent) {
        ticketList.getChildren().clear();
        FranemaApplication.appProvider.getUserTickets().forEach(ticket -> {
            Button button = new Button();
            button.setText(ticket.getMovie() + " - " + ticket.getCinema().getName());
            button.setTooltip(new Tooltip(ticket.getId() + ""));
            button.setPrefWidth(ticketListPanelPane.getWidth());
            button.setPrefHeight(ticketListPanelPane.getPrefHeight() * 0.8 * 0.1);
            button.setTextFill(Color.BLACK);
            button.setOnMouseClicked(this::ticketButtonOnMouseClick);
            ticketList.getChildren().add(button);
        });
    }

    private void ticketsAvailableOnMouseClick(MouseEvent mouseEvent) {
        ticketList.getChildren().clear();

        FranemaApplication.appProvider.getAllTickets().forEach(ticket -> {
            Button button = new Button();
            button.setText(ticket.getMovie() + " - " + ticket.getCinema().getName());
            button.setTooltip(new Tooltip(ticket.getId() + ""));
            button.setPrefWidth(ticketListPanelPane.getWidth());
            button.setPrefHeight(ticketListPanelPane.getPrefHeight() * 0.8 * 0.1);
            button.setTextFill(Color.BLACK);
            button.setOnMouseClicked(this::ticketButtonOnMouseClick);
            ticketList.getChildren().add(button);
        });
    }

    private void ticketButtonOnMouseClick(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        FranemaApplication.appProvider.setCurrentTicket(FranemaApplication.appProvider.findTicketById(Integer.parseInt(button.tooltipProperty().get().getText())));
        try {
            FranemaApplication.stageManager.showPopup(SceneEntity.TICKET_INSPECT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createTicketOnMouseClick(MouseEvent mouseEvent) {
        try {
            FranemaApplication.appProvider.setCurrentTicket(new TicketEntity(0, null, null, null));
            FranemaApplication.stageManager.showPopup(SceneEntity.TICKET_INSPECT);
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