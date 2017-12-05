/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rational;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author james
 */
public class Rational extends Application {
    
    private boolean hasSession = false;
    boolean loginSuccess = false;
    boolean registerSuccess = false;
    
    private static final int DEFAULT_RAT_NUM = 20;
    
    private final ObservableList<ListItem> data = FXCollections.observableArrayList();
    private final ObservableList<String> boroughs = FXCollections.observableArrayList("Manhattan", "Queens", "Brooklyn", "Bronx", "Staten Island");
    private final ObservableList<String> types = FXCollections.observableArrayList("1–2 Family Dwelling", "1–2 Family Mixed Use Building", "3+ Family Apt. Building", "3+ Family Mixed Use Building", "Catch Basin/Sewer", "Commercial Building", "Construction Lot", "Day Care/Nursery", "Government Building", "Hospital", "Office Building", "Parking Lot/Garage", "Public Garden", "Public Stairs", "School/Pre-School", "Single Room Occupancy (SRO)", "Summer Camp", "Vacant Building", "Vacant Lot", "Other (Explain Below)");
    
    public class ListItem {
        private final SimpleStringProperty timestamp;
        private final SimpleStringProperty id;
        private final SimpleStringProperty borough;
        
        public ListItem(String timestamp, String id, String borough) {
            this.timestamp = new SimpleStringProperty(timestamp);
            this.id = new SimpleStringProperty(id);
            this.borough = new SimpleStringProperty(borough);
        }
        
        public String getTimestamp() {
            return timestamp.get();
        }
        
        public String getId() {
            return id.get();
        }
        
        public String getBorough() {
            return borough.get();
        }
    }
    
    public void createNew() {
        Stage stage = new Stage();
        
        VBox vbox = new VBox(8);
        
        final Spinner<String> boroughSpinner = new Spinner<>();
        final Spinner<String> typeSpinner = new Spinner<>();
        
        TextField address1 = new TextField();
        address1.setPromptText("Address (Line 1)");
        TextField address2 = new TextField();
        address2.setPromptText("Address (Line 2)");
        
        TextField city = new TextField();
        city.setPromptText("City");
        TextField state = new TextField();
        state.setPromptText("State");
        TextField zip = new TextField();
        zip.setPromptText("ZIP");
        
        Button create = new Button("Create");
        
        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int zipCode = 0;
                try {
                    zipCode = Integer.parseInt(zip.getText().toString());
                } catch (Exception f) {
                    // TODO Popup saying invalid zip code
                }

                String address = address1.getText();
                String secondAddress = address2.getText();
                if (!secondAddress.isEmpty()) {
                    address += secondAddress;
                }
                
                long time = System.currentTimeMillis();

                WebAPI.RatData newRatData = new WebAPI.RatData(
                        -1,
                        time,
                        typeSpinner.getValueFactory().getValue(),
                        WebAPI.RatData.AddressInfo.of(
                                address,
                                city.getText().toString(),
                                boroughSpinner.getValueFactory().getValue(),
                                zipCode,
                                new WebAPI.RatData.LatLon(0, 0)
                        )
                );

                WebAPI.addRatSighting(newRatData, (WebAPI.RatDataResult result) -> {
                    if (result.success) {
                        fetchNewData();
                    }
                });
                
                try {
                    Thread.sleep(2000);
                } catch (Exception g) {

                }
                
                stage.close();
            }
        });
        
        
        SpinnerValueFactory<String> boroughFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(boroughs);
        SpinnerValueFactory<String> typeFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(types);
        
        boroughFactory.setValue("Manhattan");
        typeFactory.setValue("1–2 Family Dwelling");
        
        boroughSpinner.setValueFactory(boroughFactory);
        typeSpinner.setValueFactory(typeFactory);
        
        vbox.getChildren().addAll(boroughSpinner, address1, address2, city, state, zip, typeSpinner, create);
        
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        
        stage.show();
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        startLogin();
    }
    
    private boolean startLogin()  {
        Stage startPage = new Stage();
        startPage.setTitle("Welcome to rational");
        VBox vbox = new VBox(8);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        Label welcome = new Label("Welcome to rational");
        welcome.setFont(new Font(32));
        welcome.setAlignment(Pos.CENTER);
        Label detail = new Label("Welcome to rational. rational is the world's "
                + "premiere rat-tracking app. Just tap and go! Your rat data "
                + "will be logged and analyzed for any potential public " 
                + "health hazards. Just login or register to make the world a "
                + "safer place.");
        detail.setWrapText(true);
        HBox buttonBar = new HBox(8);
        buttonBar.setAlignment(Pos.CENTER);
        Button loginStartButton = new Button("Login");
        Button registerStartButton = new Button("Register");
        buttonBar.getChildren().addAll(loginStartButton, registerStartButton);
        
        vbox.getChildren().addAll(welcome, detail, buttonBar);
        
        Scene startScene = new Scene(vbox, 400, 300);
        startPage.setScene(startScene);
        
        startPage.show();
        
        loginStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage loginPage = new Stage();
                loginPage.initStyle(StageStyle.UTILITY);
                loginPage.setTitle("Login");
                VBox vbox = new VBox(8);
                vbox.setPadding(new Insets(10, 10, 10, 10));
                vbox.setAlignment(Pos.CENTER);
                
                Label login = new Label("Login");
                login.setFont(new Font(18));
                
                Label error = new Label();
                error.setTextFill(Color.web("#CC0000"));
                
                TextField username = new TextField();
                PasswordField password = new PasswordField();
                
                username.setPromptText("Username");
                password.setPromptText("Password");
                
                Button loginButton = new Button("Login");
                
                loginButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        if (username.getText().equals("") || password.getText().equals("")) {
                            error.setText("ERROR: Username or password empty");
                            return;
                        }
                        
                        WebAPI.login(username.getText(), password.getText(), (WebAPI.LoginResult result) -> {
                            if (result.success) {
                                try {
                                    Model.getInstance().updateUser(
                                            new JSONObject().put("email", username.getText())
                                                    .put("sessionID", result.sessionID)
                                                    .put("permLevel", result.permissionLevel.ordinal())
                                    );
                                } catch (JSONException f) {
                                    f.printStackTrace();
                                }
                                
                                loginSuccess = true;
                            } else {
                                loginSuccess = false;
                            }
                        });
                        
                        try {
                            Thread.sleep(2000);
                        } catch (Exception g) {
                            
                        }
                        
                        if (loginSuccess) {
                            startPage.close();
                            loginPage.close();

                            startMain();
                        } else {
                            error.setText("ERROR: Username or password incorrect");
                        }
                    }
                });
                
                vbox.getChildren().addAll(login, username, password, loginButton, error);
                
                Scene loginScene = new Scene(vbox, 300, 180);
                loginPage.setScene(loginScene);
                loginPage.show();   
            }
        });
        
        registerStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage registerPage = new Stage();
                registerPage.initStyle(StageStyle.UTILITY);
                registerPage.setTitle("Register");
                VBox vbox = new VBox(8);
                vbox.setPadding(new Insets(10, 10, 10, 10));
                vbox.setAlignment(Pos.CENTER);
                
                Label login = new Label("Register");
                login.setFont(new Font(18));
                
                Label error = new Label();
                error.setTextFill(Color.web("#CC0000"));
                
                TextField username = new TextField();
                PasswordField password = new PasswordField();
                PasswordField passwordConfirm = new PasswordField();
                
                username.setPromptText("Username");
                password.setPromptText("Password");
                passwordConfirm.setPromptText("Confirm Password");
                
                Button registerButton = new Button("Register");
                
                registerButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        if (username.getText().equals("") || password.getText().equals("") || passwordConfirm.getText().equals("")) {
                            error.setText("ERROR: Fields are empty");
                            return;
                        }
                        
                        if (!password.getText().equals(passwordConfirm.getText())) {
                            error.setText("ERROR: Passwords do not match");
                            return;
                        }
                        
                        WebAPI.register (
                            username.getText(),
                            password.getText(),
                            User.PermissionLevel.ADMIN,
                            (WebAPI.RegisterResult res) -> {
                                if (res.success) {
                                    registerSuccess = true;
                                    return;
                                } else {
                                    registerSuccess = false;
                                    return;
                                }
                            }
                        );
                        
                        try {
                            Thread.sleep(2000);
                        } catch (Exception g) {
                            
                        }
                        
                        if (registerSuccess) {
                            registerSuccess = false;
                            registerPage.close();
                        } else {
                            error.setText("ERROR: Username Taken");
                        }
                    }
                });
                
                vbox.getChildren().addAll(login, username, password, passwordConfirm, registerButton, error);
                
                Scene registerScene = new Scene(vbox, 300, 250);
                registerPage.setScene(registerScene);
                registerPage.show();
            }
        });
        
        return false;
    }
    
    private void startMain() {
        Stage stage = new Stage();
        stage.setTitle("rational");
        
        TableView allSightingsView = new TableView();
        allSightingsView.setEditable(false);
        allSightingsView.setFixedCellSize(25);
        
        allSightingsView.setPrefHeight(500);
        
        TableColumn timestampCol = new TableColumn("Timestamp");
        TableColumn idCol = new TableColumn("ID");
        TableColumn boroughCol = new TableColumn("Borough");
        
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        boroughCol.setCellValueFactory(new PropertyValueFactory<>("borough"));
        
        timestampCol.setMinWidth(200);
        idCol.setMinWidth(50);
        boroughCol.setMinWidth(300);
        
        fetchOldData();
        
        allSightingsView.setItems(data);
        allSightingsView.getColumns().addAll(timestampCol, idCol, boroughCol);

        TabPane tabPane = new TabPane();

        Tab dashboardTab = new Tab("Dashboard");
        Tab graphTab = new Tab("Graphs");
        Tab mapTab = new Tab("Map");
        Tab allSightingsTab = new Tab("All Sightings", allSightingsView);
        
        tabPane.getTabs().addAll(dashboardTab, graphTab, mapTab, allSightingsTab);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        Button createSighting = new Button("Create New Sighting");
        
        createSighting.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                createNew();
            }
        });
        
        VBox vbox = new VBox(0);
        vbox.getChildren().addAll(createSighting, tabPane);

        Scene scene = new Scene(vbox, 800, 500);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private ListItem buildRatData(WebAPI.RatData data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd KK:mm:ss aa", Locale.US);
        return new ListItem(sdf.format(new Date(data.createdTime)) + "", data.uniqueKey + "", data.borough + ", " + data.city);
    }
    
    private void fetchOldData() {
        Model.getInstance().getRatData(data.size(), DEFAULT_RAT_NUM, (List<WebAPI.RatData> newData) -> {
            if (newData != null) {
                for (int i = 0; i < newData.size(); i++) {
                    data.add(buildRatData(newData.get(i)));
                }
            }
        });
    }
    
    public void fetchNewData() {
        Model.getInstance().getNewestRatData((List<WebAPI.RatData> newData) -> {
            if (newData != null) {
                for (int i = newData.size() - 1; i >= 0; i--) {
                    data.add(0, buildRatData(newData.get(i)));
                }
            }
        });
    }
}
