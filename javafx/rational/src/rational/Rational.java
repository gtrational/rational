/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rational;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author james
 */
public class Rational extends Application {
    
    private boolean hasSession = false;
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("rational");
        TableView allSightingsView = new TableView();
        allSightingsView.setEditable(false);
        TableColumn timestampCol = new TableColumn("Timestamp");
        TableColumn idCol = new TableColumn("ID");
        TableColumn boroughCol = new TableColumn("Borough");
        timestampCol.setMinWidth(200);
        idCol.setMinWidth(50);
        boroughCol.setMinWidth(300);
        allSightingsView.getColumns().addAll(timestampCol, idCol, boroughCol);

        TabPane tabPane = new TabPane();

        Tab dashboardTab = new Tab("Dashboard");
        Tab graphTab = new Tab("Graphs");
        Tab mapTab = new Tab("Map");
        Tab allSightingsTab = new Tab("All Sightings", allSightingsView);

        tabPane.getTabs().addAll(dashboardTab, graphTab, mapTab, allSightingsTab);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 800, 500);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
        
        if (!hasSession) {
            startLogin();
        }
    }
    
    private static void startLogin() {
        Stage startPage = new Stage();
        startPage.setTitle("Welcome to rational");
        VBox vbox = new VBox(8);
        Label welcome = new Label("Welcome to rational");
        welcome.setFont(new Font(32));
        welcome.setTextAlignment(TextAlignment.CENTER);
        Label detail = new Label("Welcome to rational. rational is the world's "
                + "premiere rat-tracking app. Just tap and go! Your rat data "
                + "will be logged and analyzed for any potential public " 
                + "health hazards. Just login or register to make the world a "
                + "safer place.");
        detail.setWrapText(true);
        HBox buttonBar = new HBox(8);
        Button loginStartButton = new Button("Login");
        Button registerStartButton = new Button("Register");
        buttonBar.getChildren().addAll(loginStartButton, registerStartButton);
        
        vbox.getChildren().addAll(welcome, detail, buttonBar);
        
        Scene startScene = new Scene(vbox, 400, 300);
        startPage.setScene(startScene);
        
        startPage.show();
        
        
        
//        vbox.getChildren().addAll(usernameLabel, username, passwordLabel, password);
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
