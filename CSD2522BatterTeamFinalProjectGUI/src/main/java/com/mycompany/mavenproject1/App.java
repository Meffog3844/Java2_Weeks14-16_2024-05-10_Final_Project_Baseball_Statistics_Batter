/*
Authors: Luke Dawson, Terry Pescosolido, Cedrick Charles, Gavin Mefford-Gibbins, Wesley Morah
Date created: 4/24/24
Purpose: create a GUI to accept input from a user and write it into a Sqlite database
Updated by:
Luke Dawson - 4/24/24 - created the skeleton for the GUI
Luke Dawson - 4/25/24 - added button functionality to add the data to the database
    and added starter check box. Changed default value of each text box to 0 and
    made reset button return them all to 0 rather than empty them
Luke Dawson - 4/26/24 - added integer parsing for the text fields before adding
    data to the database
Luke Dawson - 4/28/24 - added formatting to clean up the GUI and make it more manageable
Terry Pescosolido - 4/28/24 - changed calls to database for integration
Luke Dawson - 4/29/24 - added "Add new Batter" menu
Terry Pescosolido - 4/29/24 - fixed interaction for new player name combo box with db
Luke Dawson - 4/30/24 - added a menu option to enter a new game
Terry Pescosolido - 4/30/24 - fixed interaction for new game combo box with db
Gavin Mefford-Gibbins - 4/30/2024 - Added functionality to the View Game Report button
Cedrick Charles - 4/30/2024 - Made modification to the display of the View Game Report button added by Gavin
Luke Dawson - 5/3/24 - added error handling to the submit functions
Terry Pescosolido - 5/3/24 - small change to database call on add player stats
Terry Pescosolido - 5/4/24 - small change to reset combo boxes before loading in add player stats
Luke Dawson 5/4/24 - fixed resetButtonClicked to work with the enterGame Button and fixed
    issue regarding manually entering dates into the datePicker field
Terry Pescosolido - 5/5/24 - add crude game report
Terry Pescosolido - 5/5/24 - tweaks to game report
Terry Pescosolido - 5/5/24 - tweaks to game report to increase gridpane size and font
Gavin Mefford-Gibbins - 5/6/2024 - Polished the "rough" game report a little bit to line it up
Gavin Mefford-Gibbins - 5/6/2024 - Added Functionality to the Create file button on single game report page
*/

package com.mycompany.mavenproject1;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.sql.*;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import java.io.FileWriter;
import java.io.IOException;



/**
 * JavaFX App
 */
public class App extends Application {
    // create labels
    private Label gameLabel = new Label("Game");
    private Label playerLabel = new Label("Player:");
    private Label starterLabel = new Label("Starter");
    private Label battingOrderLabel = new Label("Batting Order #");
    private Label atBatLabel = new Label("At-Bats:");
    private Label runLabel = new Label("Runs:");
    private Label singleLabel = new Label("Singles:");
    private Label doubleLabel = new Label("Doubles:");
    private Label tripleLabel = new Label("Triples:");
    private Label homeRunLabel = new Label("Home Runs:");
    private Label basesOnBallLabel = new Label("Bases-on-Balls:");
    private Label hitsByPitchLabel = new Label("Hits-by-Pitch:");
    private Label runsBattedInLabel = new Label("Runs Batted In:");
    private Label strikeOutLabel = new Label("Strike Outs:");
    private Label groundedDoublePlayLabel = new Label("Grounded Double Plays:");
    private Label stolenBaseAttemptLabel = new Label("Stolen Base Attempts:");
    private Label stolenBaseSuccessLabel = new Label("Stolen Base Successes:");
    private Label sacrificeFliesLabel = new Label("Sacrifice Flies:");
    private Label sacrificeHitsLabel = new Label("Sacrifice Hits:");
    private Label leftOnBaseLabel = new Label("Left on Base:");
    private Label firstNameLabel = new Label("First Name:");
    private Label lastNameLabel = new Label("Last Name:");
    private Label enterPlayerNumberLabel = new Label("Player Number:");
    private Label activeLabel = new Label("Active");
    private Label gameNumberLabel = new Label("Game #");
    private Label opponentLabel = new Label("Opponent");
    private Label gameDateLabel = new Label("Game Date (M/D/YYYY)");
    
    // create textfields
    private ComboBox<String> gameComboBox = new ComboBox<>();
    ComboBox<String> playerComboBox = new ComboBox<>();
    private CheckBox starterCheckBox = new CheckBox("");
    private TextField battingOrderField = new TextField("0");
    private TextField atBatField = new TextField("0");
    private TextField runField = new TextField("0");
    private TextField singleField = new TextField("0");
    private TextField doubleField = new TextField("0");
    private TextField tripleField = new TextField("0");
    private TextField homeRunField = new TextField("0");
    private TextField basesOnBallField= new TextField("0");
    private TextField hitsByPitchField = new TextField("0");
    private TextField runsBattedInField = new TextField("0");
    private TextField strikeOutField = new TextField("0");
    private TextField groundedDoublePlayField = new TextField("0");
    private TextField stolenBaseAttemptField = new TextField("0");
    private TextField stolenBaseSuccessField = new TextField("0");
    private TextField sacrificeFliesField = new TextField("0");
    private TextField sacrificeHitsField = new TextField("0");
    private TextField leftOnBaseField = new TextField("0");
    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();
    private TextField enterPlayerNumberField = new TextField();
    private CheckBox activeCheckBox = new CheckBox("");
    private TextField gameNumberField = new TextField();
    private TextField opponentField = new TextField();
    private DatePicker gameDatePicker = new DatePicker();
    
    BaseballStatsDB baseball_stats_db = new BaseballStatsDB();
    //List<Batter> batters = null; 
    //private List<Player> players = new ArrayList<>();
    //private List<Game> games = new ArrayList<>();
    
    // create the primary stage for the main menu
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        // set title
        stage.setTitle("Baseball Stats");
        
        // create a new grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);
    
        // create the scene
        Scene scene = new Scene(grid, 250, 260);

        // create buttons for different actions
        Button enterDataButton = new Button("Enter Batter Stats");
        Button enterPlayerButton = new Button("Enter New Batter");
        Button enterGameButton = new Button("Enter New Game");
        Button viewGameReportButton = new Button("View Game Report");
        Button viewMultiGameReportButton = new Button("View Multi-Game Report");
        
        // VBox to hold the different options the user can choose from
        VBox optionBox = new VBox(10);
        optionBox.getChildren().add(enterDataButton);
        optionBox.getChildren().add(enterPlayerButton);
        optionBox.getChildren().add(enterGameButton);
        optionBox.getChildren().add(viewGameReportButton);
        optionBox.getChildren().add(viewMultiGameReportButton); 
        grid.add(optionBox, 0, 0, 3, 1);
        
        enterDataButton.setOnAction(event -> enterDataButtonClicked());
        enterPlayerButton.setOnAction(event -> enterBatterButtonClicked());
        enterGameButton.setOnAction(event -> enterGameButtonClicked());
        viewGameReportButton.setOnAction(event -> viewGameReportButtonClicked());
        
        // exit button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> exitButtonClicked());
        
        // HBox to hold the utility commands
        HBox utilityBox = new HBox(10);
        utilityBox.getChildren().add(exitButton);
        grid.add(utilityBox, 0, 2, 1, 1);

        stage.setScene(scene);
        stage.show();
        
    }
    
//    // method to connect to the sql database
//    private Connection getConnection() throws SQLException {
//        String dbUrl = "jdbc:sqlite:baseball_batter_stats.sqlite"; // enter file name here
//        Connection connection = DriverManager.getConnection(dbUrl);
//        return connection;
//    }
    
    // method for when the user selects the "Enter Data" button
    private void enterDataButtonClicked() {
        // create a new scene for entering data
        GridPane enterDataGrid = new GridPane();
        enterDataGrid.setAlignment(Pos.TOP_LEFT);
        enterDataGrid.setPadding(new Insets(25, 25, 25, 25));
        enterDataGrid.setHgap(10);
        enterDataGrid.setVgap(10); 
        
        gameComboBox.getItems().clear();
        for (Game game : baseball_stats_db.getGames()) {
            gameComboBox.getItems().add("Game " + game.getGameNumber() + " - " + game.getGameDate() + " - " + game.getGameOpponentName()); // Add game names to ComboBox
        }
        enterDataGrid.add(gameComboBox, 1, 1, 1, 1);
        
        playerComboBox.getItems().clear();
        for (Player player : baseball_stats_db.getPlayers()) {
            playerComboBox.getItems().add(player.getPlayerName() + " #" + player.getPlayerNumber()); // Add player names to ComboBox
        }
        enterDataGrid.add(playerComboBox, 1, 1, 1, 1);
        
        VBox labelBox1 = new VBox(18);
        labelBox1.getChildren().add(gameLabel);
        labelBox1.getChildren().add(playerLabel);
        labelBox1.getChildren().add(starterLabel);
        labelBox1.getChildren().add(battingOrderLabel);
        labelBox1.getChildren().add(atBatLabel);
        labelBox1.getChildren().add(runLabel);
        labelBox1.getChildren().add(singleLabel);
        labelBox1.getChildren().add(doubleLabel);
        labelBox1.getChildren().add(tripleLabel);
        labelBox1.getChildren().add(homeRunLabel);
        enterDataGrid.add(labelBox1, 0, 0);
        
        VBox labelBox2 = new VBox(18);
        labelBox2.getChildren().add(basesOnBallLabel);
        labelBox2.getChildren().add(hitsByPitchLabel);
        labelBox2.getChildren().add(runsBattedInLabel);
        labelBox2.getChildren().add(strikeOutLabel);
        labelBox2.getChildren().add(groundedDoublePlayLabel);
        labelBox2.getChildren().add(stolenBaseAttemptLabel);
        labelBox2.getChildren().add(stolenBaseSuccessLabel);
        labelBox2.getChildren().add(sacrificeFliesLabel);
        labelBox2.getChildren().add(sacrificeHitsLabel);
        labelBox2.getChildren().add(leftOnBaseLabel);
        enterDataGrid.add(labelBox2, 2, 0);
        
        VBox textFieldBox1 = new VBox(10);
        textFieldBox1.getChildren().add(gameComboBox);
        textFieldBox1.getChildren().add(playerComboBox);
        textFieldBox1.getChildren().add(starterCheckBox);
        textFieldBox1.getChildren().add(battingOrderField);
        textFieldBox1.getChildren().add(atBatField);
        textFieldBox1.getChildren().add(runField);
        textFieldBox1.getChildren().add(singleField);
        textFieldBox1.getChildren().add(doubleField);
        textFieldBox1.getChildren().add(tripleField);
        textFieldBox1.getChildren().add(homeRunField);
        enterDataGrid.add(textFieldBox1, 1, 0);
        
        VBox textFieldBox2 = new VBox(10);
        textFieldBox2.getChildren().add(basesOnBallField);
        textFieldBox2.getChildren().add(hitsByPitchField);
        textFieldBox2.getChildren().add(runsBattedInField);
        textFieldBox2.getChildren().add(strikeOutField);
        textFieldBox2.getChildren().add(groundedDoublePlayField);
        textFieldBox2.getChildren().add(stolenBaseAttemptField);
        textFieldBox2.getChildren().add(stolenBaseSuccessField);
        textFieldBox2.getChildren().add(sacrificeFliesField);
        textFieldBox2.getChildren().add(sacrificeHitsField);
        textFieldBox2.getChildren().add(leftOnBaseField);
        enterDataGrid.add(textFieldBox2, 3, 0);

        // add a submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> submitButtonClicked());
        // add a reset button
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> resetButtonClicked());
        // add a return button
        Button returnButton = new Button("Return");
        returnButton.setOnAction(event -> returnButtonClicked());
        
        // HBox to hold the submit and return buttons
        HBox submitReturnBox = new HBox(10);
        submitReturnBox.getChildren().add(submitButton);
        submitReturnBox.getChildren().add(resetButton);
        submitReturnBox.getChildren().add(returnButton);
        enterDataGrid.add(submitReturnBox, 0, 1, 2, 1);

        // set the new scene
        Scene enterDataScene = new Scene(enterDataGrid);
        primaryStage.setScene(enterDataScene);
    }
    
    private void enterBatterButtonClicked() {
        // create a new scene for entering data
        GridPane enterDataGrid = new GridPane();
        enterDataGrid.setAlignment(Pos.TOP_LEFT);
        enterDataGrid.setPadding(new Insets(25, 25, 25, 25));
        enterDataGrid.setHgap(10);
        enterDataGrid.setVgap(10);
        
        // Define font
        Font font = Font.font("Courier New", FontWeight.NORMAL, 14);

        // Create labels with the defined font
        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setFont(font);

        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setFont(font);

        Label enterPlayerNumberLabel = new Label("Enter Player Number:");
        enterPlayerNumberLabel.setFont(font);

        // Create text fields with the defined font
        TextField firstNameField = new TextField();
        firstNameField.setFont(font);

        TextField lastNameField = new TextField();
        lastNameField.setFont(font);

        TextField enterPlayerNumberField = new TextField();
        enterPlayerNumberField.setFont(font);

        // Create VBoxes for labels and text fields
        VBox labelBox = new VBox(20);
        labelBox.getChildren().addAll(firstNameLabel, lastNameLabel, enterPlayerNumberLabel);
        enterDataGrid.add(labelBox, 0, 0);

        VBox textBox = new VBox(10);
        textBox.getChildren().addAll(firstNameField, lastNameField, enterPlayerNumberField);
        enterDataGrid.add(textBox, 1, 0);
        
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> submitPlayerButtonClicked());
        // add a reset button
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> resetButtonClicked());
        // add a return button
        Button returnButton = new Button("Return");
        returnButton.setOnAction(event -> returnButtonClicked());
        
        // HBox to hold the submit and return buttons
        HBox submitReturnBox = new HBox(10);
        submitReturnBox.getChildren().add(submitButton);
        submitReturnBox.getChildren().add(resetButton);
        submitReturnBox.getChildren().add(returnButton);
        enterDataGrid.add(submitReturnBox, 0, 1, 2, 1);
        
        // set the new scene
        Scene enterDataScene = new Scene(enterDataGrid);
        primaryStage.setScene(enterDataScene);
    }
    
    private void enterGameButtonClicked() {
        // create a new scene for entering data
        GridPane enterGameGrid = new GridPane();
        enterGameGrid.setAlignment(Pos.TOP_LEFT);
        enterGameGrid.setPadding(new Insets(25, 25, 25, 25));
        enterGameGrid.setHgap(10);
        enterGameGrid.setVgap(10);
        
        VBox labelBox = new VBox(20);
        labelBox.getChildren().add(gameNumberLabel);
        labelBox.getChildren().add(opponentLabel);
        labelBox.getChildren().add(gameDateLabel);
        enterGameGrid.add(labelBox, 0, 0);
        
        VBox fieldBox = new VBox(10);
        fieldBox.getChildren().add(gameNumberField);
        fieldBox.getChildren().add(opponentField);
        fieldBox.getChildren().add(gameDatePicker);
        enterGameGrid.add(fieldBox, 1, 0);
        
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> submitGameButtonClicked());
        // add a return button
        Button returnButton = new Button("Return");
        returnButton.setOnAction(event -> returnButtonClicked());
        
        HBox submitReturnBox = new HBox(10);
        submitReturnBox.getChildren().add(submitButton);
        submitReturnBox.getChildren().add(returnButton);
        enterGameGrid.add(submitReturnBox, 0, 1, 2, 1);
        
        Scene enterGameScene = new Scene(enterGameGrid, 400, 180);
        primaryStage.setScene(enterGameScene);
    }
    
    private void viewGameReportButtonClicked() {
    // Create a new GridPane for layout management
    GridPane viewGameReportGrid = new GridPane();
    viewGameReportGrid.setAlignment(Pos.TOP_LEFT); 
    viewGameReportGrid.setPadding(new Insets(25, 25, 25, 25));
    viewGameReportGrid.setHgap(0);
    viewGameReportGrid.setVgap(5);

    VBox topVBox = new VBox(20);
    HBox gameReportTopBox = new HBox(20);

    // Combo box setup
    ComboBox<String> gameComboBox = new ComboBox<>();
    Label gameComboBoxLabel = new Label("Game: ");
    for (Game game : baseball_stats_db.getGames()) {
        gameComboBox.getItems().add("Game " + game.getGameNumber() + " - " + game.getGameDate() + " - " + game.getGameOpponentName());
    }
    gameReportTopBox.getChildren().addAll(gameComboBoxLabel, gameComboBox);

    // Toggle buttons setup
    ToggleGroup shortLongReportToggle = new ToggleGroup();
    RadioButton shortReport = new RadioButton("Short Report");
    shortReport.setToggleGroup(shortLongReportToggle);
    shortReport.setSelected(true);
    RadioButton longReport = new RadioButton("Detailed Report");
    longReport.setToggleGroup(shortLongReportToggle);
    gameReportTopBox.getChildren().addAll(shortReport, longReport);

    topVBox.getChildren().add(gameReportTopBox);
    viewGameReportGrid.add(topVBox, 0, 0);

    // Return and create file buttons
    HBox bottomHBox = new HBox(5);
    Button returnButton = new Button("Return");
    returnButton.setOnAction(event -> returnButtonClicked());
    bottomHBox.getChildren().add(returnButton);

    Button createFileButton = new Button("Create File");
    createFileButton.setOnAction(event -> returnButtonClicked());
    createFileButton.setOnAction(event -> {
    int selectedGameNumber = Integer.parseInt(gameComboBox.getValue().split(" ")[1]);
    try {
        writeDetailedReportToFile(selectedGameNumber);
        // Optionally, notify the user that the file was successfully created
        System.out.println("Report file created successfully!");
    } catch (IOException e) {
        // Handle any exceptions while writing to the file
        System.err.println("Error while creating report file: " + e.getMessage());
    }
});

    bottomHBox.getChildren().add(createFileButton);

    viewGameReportGrid.add(bottomHBox, 0, 2);

    VBox statsVBox = new VBox(5);
    shortReport.setOnAction(e -> {
        if (!createFileButton.isDisabled()) {
            gameComboBox.fireEvent(e);
        }
    });
    longReport.setOnAction(e -> {
        if (!createFileButton.isDisabled()) {
            gameComboBox.fireEvent(e);
        }
    });

    gameComboBox.setOnAction(e -> {
        createFileButton.setDisable(false);
        int selectedGameNumber = Integer.parseInt(gameComboBox.getValue().split(" ")[1]);
        statsVBox.getChildren().clear();

        GridPane playerStatsGrid = new GridPane();
        playerStatsGrid.setVgap(5);
        playerStatsGrid.setHgap(10);

        Font font = Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 14);
        Label headerLabel;

        if (longReport.isSelected()) {
            String[] headers = {
                "p#", "Player", "Avg", "AB", "R", "H", "2B", "3B", "HR", "RBI", "TB", "SLG%", "BB", "HP", "SO", "GDP", "OB%", "SF", "SH", "SB-Att", "LOB"
            };

            for (int i = 0; i < headers.length; i++) {
                headerLabel = new Label(headers[i]);
                headerLabel.setFont(font);
                playerStatsGrid.add(headerLabel, i, 0);
            }

            int rowIndex = 1;
            for (Batter batter : baseball_stats_db.getGamePlayerStats(selectedGameNumber)) {
                String[] batterStats = {
                    String.valueOf(batter.getPlayerNumber()),
                    batter.getPlayerName(),
                    batter.getBatterAVGFormatted(),
                    String.valueOf(batter.getBatterAB()),
                    String.valueOf(batter.getBatterRuns()),
                    String.valueOf(batter.getBatterHits()),
                    String.valueOf(batter.getBatter2B()),
                    String.valueOf(batter.getBatter3B()),
                    String.valueOf(batter.getBatterHR()),
                    String.valueOf(batter.getBatterRBI()),
                    String.valueOf(batter.getBatterTB()),
                    batter.getBatterSLGFormatted(),
                    String.valueOf(batter.getBatterBB()),
                    String.valueOf(batter.getBatterHP()),
                    String.valueOf(batter.getBatterSO()),
                    String.valueOf(batter.getBatterGDP()),
                    batter.getBatterOBFormatted(),
                    String.valueOf(batter.getBatterSF()),
                    String.valueOf(batter.getBatterSH()),
                    batter.getBatterSBSBAFormatted(),
                    String.valueOf(batter.getBatterLOB())
                };

                for (int colIndex = 0; colIndex < batterStats.length; colIndex++) {
                    Label statLabel = new Label(batterStats[colIndex]);
                    statLabel.setFont(font);
                    playerStatsGrid.add(statLabel, colIndex, rowIndex);
                }
                rowIndex++;
            }
        }else if (shortReport.isSelected()) {
        // Short report
        String[] headers = {"Player", "AB", "R", "H", "RBI", "BB", "SO", "LOB"};

        for (int i = 0; i < headers.length; i++) {
            headerLabel = new Label(headers[i]);
            headerLabel.setFont(font);
            playerStatsGrid.add(headerLabel, i, 0);
        }

        int rowIndex = 1;
        for (Batter batter : baseball_stats_db.getGamePlayerStats(selectedGameNumber)) {
            String[] batterStats = {
                batter.getPlayerName(),
                String.valueOf(batter.getBatterAB()),
                String.valueOf(batter.getBatterRuns()),
                String.valueOf(batter.getBatterHits()),
                String.valueOf(batter.getBatterRBI()),
                String.valueOf(batter.getBatterBB()),
                String.valueOf(batter.getBatterSO()),
                String.valueOf(batter.getBatterLOB())
            };

            for (int colIndex = 0; colIndex < batterStats.length; colIndex++) {
                Label statLabel = new Label(batterStats[colIndex]);
                statLabel.setFont(font);
                playerStatsGrid.add(statLabel, colIndex, rowIndex);
            }
            rowIndex++;
        }
    }

    statsVBox.getChildren().add(playerStatsGrid);
});

    viewGameReportGrid.add(statsVBox, 0, 3);

    // Create a scene and set it to the primary stage
    Scene enterGameScene = new Scene(viewGameReportGrid, 1000, 800);
    primaryStage.setScene(enterGameScene);
}
    
private void writeDetailedReportToFile(int gameNumber) throws IOException {
    // Specify the output file name
    String fileName = "GameReport_" + gameNumber + ".txt";
    try (FileWriter writer = new FileWriter(fileName)) {
        // Write the headers
        String[] headers = {
            "p#", "Player", "Avg", "AB", "R", "H", "2B", "3B", "HR", "RBI", "TB", "SLG%", "BB", "HP", "SO", "GDP", "OB%", "SF", "SH", "SB-Att", "LOB"
        };
        writer.write(String.join("\t", headers) + "\n");

        // Write the data rows
        for (Batter batter : baseball_stats_db.getGamePlayerStats(gameNumber)) {
            String[] batterStats = {
                String.valueOf(batter.getPlayerNumber()),
                batter.getPlayerName(),
                batter.getBatterAVGFormatted(),
                String.valueOf(batter.getBatterAB()),
                String.valueOf(batter.getBatterRuns()),
                String.valueOf(batter.getBatterHits()),
                String.valueOf(batter.getBatter2B()),
                String.valueOf(batter.getBatter3B()),
                String.valueOf(batter.getBatterHR()),
                String.valueOf(batter.getBatterRBI()),
                String.valueOf(batter.getBatterTB()),
                batter.getBatterSLGFormatted(),
                String.valueOf(batter.getBatterBB()),
                String.valueOf(batter.getBatterHP()),
                String.valueOf(batter.getBatterSO()),
                String.valueOf(batter.getBatterGDP()),
                batter.getBatterOBFormatted(),
                String.valueOf(batter.getBatterSF()),
                String.valueOf(batter.getBatterSH()),
                batter.getBatterSBSBAFormatted(),
                String.valueOf(batter.getBatterLOB())
            };

            // Write each batter's stats, separated by tabs
            writer.write(String.join("\t", batterStats) + "\n");
        }
    }
}



    
    // when the user has all data entered, write the data to the database
    private void submitButtonClicked() {
        try {
            // ensure all fields have a valid value
            if (gameComboBox.getSelectionModel().isEmpty() ||
                playerComboBox.getSelectionModel().isEmpty() ||
                battingOrderField.getText().isEmpty() ||
                atBatField.getText().isEmpty() ||
                runField.getText().isEmpty() ||
                singleField.getText().isEmpty() ||
                doubleField.getText().isEmpty() ||
                tripleField.getText().isEmpty() ||
                homeRunField.getText().isEmpty() ||
                basesOnBallField.getText().isEmpty() ||
                hitsByPitchField.getText().isEmpty() ||
                runsBattedInField.getText().isEmpty() ||
                strikeOutField.getText().isEmpty() ||
                groundedDoublePlayField.getText().isEmpty() ||
                stolenBaseAttemptField.getText().isEmpty() ||
                stolenBaseSuccessField.getText().isEmpty() ||
                sacrificeFliesField.getText().isEmpty() ||
                sacrificeHitsField.getText().isEmpty() ||
                leftOnBaseField.getText().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }
            // Retrieve the data entered in each field
            String gameInfo = gameComboBox.getSelectionModel().getSelectedItem();
            int game_number = Integer.parseInt(gameInfo.split(" ")[1]);
            String playerNameNumber = playerComboBox.getSelectionModel().getSelectedItem();
            int batter_pn = Integer.parseInt(playerNameNumber.split("#")[1]);
            int batter_gs = starterCheckBox.isSelected() ? 1 : 0; // convert boolean to int
            int batter_bo = Integer.parseInt(battingOrderField.getText());
            int batter_ab = Integer.parseInt(atBatField.getText());
            int batter_runs = Integer.parseInt(runField.getText());
            int batter_1b = Integer.parseInt(singleField.getText());
            int batter_2b = Integer.parseInt(doubleField.getText()); 
            int batter_3b = Integer.parseInt(tripleField.getText());
            int batter_hr = Integer.parseInt(homeRunField.getText());
            int batter_bb = Integer.parseInt(basesOnBallField.getText());
            int batter_hp = Integer.parseInt(hitsByPitchField.getText());
            int batter_rbi = Integer.parseInt(runsBattedInField.getText());
            int batter_so = Integer.parseInt(strikeOutField.getText());
            int batter_gdp = Integer.parseInt(groundedDoublePlayField.getText());
            int batter_sba = Integer.parseInt(stolenBaseAttemptField.getText());
            int batter_sb = Integer.parseInt(stolenBaseSuccessField.getText());
            int batter_sf = Integer.parseInt(sacrificeFliesField.getText());
            int batter_sh = Integer.parseInt(sacrificeHitsField.getText());
            int batter_lob = Integer.parseInt(leftOnBaseField.getText());
            
            // Insert record into 
            //try (
                baseball_stats_db.addGamePlayerStats(game_number, batter_pn, 
                              batter_bo, batter_gs,
                              batter_ab, batter_runs, batter_1b, batter_2b, 
                              batter_3b, batter_hr, batter_bb, batter_hp, batter_rbi,
                              batter_so, batter_gdp, batter_sba, batter_sb, 
                              batter_sf, batter_sh, batter_lob);

                // show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Commit successful");
                successAlert.setContentText("Data inserted successfully");
                successAlert.showAndWait();

                // clear fields
                resetButtonClicked();
        } catch (NumberFormatException e) {
            // show error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Commit failed");
            errorAlert.setContentText("There was an error in adding the data to the database. Please check your information and try again");
            errorAlert.showAndWait();
        } catch (IllegalArgumentException e) {
            // show error message for empty fields
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Commit failed");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
    
    // method to handle the submit button on the enter batter menu
    private void submitPlayerButtonClicked() {
        try {
            // ensure all fields are filled
            if (firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                enterPlayerNumberField.getText().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }
            
            // Retrieve the data entered in each field
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            int playerNumber = Integer.parseInt(enterPlayerNumberField.getText());
            //boolean isActive = activeCheckBox.isSelected();

            baseball_stats_db.addPlayer(firstName, lastName, playerNumber);
    //        // Create a new Player object with the entered data
    //        Player player = new Player(playerNumber, firstName + " " + lastName, isActive);
    //
    //        // Add the new player to the list
    //        players.add(player);

            // Show success message
            //String activeStatus = isActive ? "active" : "inactive";
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Player added successfully");
            successAlert.setContentText("Player " + firstName + " " + lastName + " added with player number " + playerNumber); // + " and is " + activeStatus);
            successAlert.showAndWait();

            // Clear fields
            resetButtonClicked();
        } catch (NumberFormatException e) {
            // show error message for number format exception
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Player addition failed");
            errorAlert.setContentText("Please enter a valid numeric value for player number.");
            errorAlert.showAndWait();
        }  catch (IllegalArgumentException e) {
            // show error message for empty fields
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Player addition failed");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
    
    private void submitGameButtonClicked() {
        try {
            // ensure all fields have a valid value
            if (gameNumberField.getText().isEmpty() || opponentField.getText().isEmpty() || gameDatePicker.getValue() == null) {
                throw new IllegalArgumentException("All fields must be filled.");
            }
            
            int gameNumber = Integer.parseInt(gameNumberField.getText());
            String opponent = opponentField.getText();
            LocalDate gameDate = gameDatePicker.getValue();
            
            // if the value is entered manually, parse the date
            if (gameDatePicker.getValue() != null) {
                gameDate = gameDatePicker.getValue();
            } else {
                String dateString = gameDatePicker.getEditor().getText();
                gameDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/yyyy"));
            }

            baseball_stats_db.addGame(gameNumber, opponent, String.valueOf(gameDate));

    //        Game game = new Game(gameNumber, opponent, String.valueOf(gameDate));
    //        
    //        // Add the new player to the list
    //        games.add(game);

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Game added successfully");
            successAlert.setContentText("Game " + gameNumber + " added with opponent " + opponent + " and date " + gameDate);
            successAlert.showAndWait();

            // Clear fields
            resetButtonClicked();
        } catch (NumberFormatException e) {
            // show error message for number format exception
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Game addition failed");
            errorAlert.setContentText("Please enter a valid numeric value for game number.");
            errorAlert.showAndWait();
        } catch (IllegalArgumentException e) {
            // show error message for empty fields
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Game addition failed");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        } catch (DateTimeParseException e) {
            // handle invalid date format
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Game addition failed");
            errorAlert.setContentText("Please enter the date in the format M/D/YYYY.");
            errorAlert.showAndWait();
        }
        
        
//        String gameInfo = ("Game " + gameNumber + " - " + gameDate + " vs " + opponent);
//        
//        // Add gameInfo to gameComboBox
//        gameComboBox.getItems().add(gameInfo);
    }
    
    // function to return back to the main menu
    private void returnButtonClicked() {
        // navigate back to the main menu
        start(primaryStage);
    }
    
    // function to reset all data entry boxes
    private void resetButtonClicked() {
        // reset all data input
        gameComboBox.getSelectionModel().clearSelection();
        playerComboBox.getSelectionModel().clearSelection();
        starterCheckBox.setSelected(false);
        battingOrderField.setText("0");
        atBatField.setText("0");
        runField.setText("0");
        singleField.setText("0");
        doubleField.setText("0");
        tripleField.setText("0");
        homeRunField.setText("0");
        basesOnBallField.setText("0");
        hitsByPitchField.setText("0");
        runsBattedInField.setText("0");
        strikeOutField.setText("0");
        groundedDoublePlayField.setText("0");
        stolenBaseAttemptField.setText("0");
        stolenBaseSuccessField.setText("0");
        sacrificeFliesField.setText("0");
        sacrificeHitsField.setText("0");
        leftOnBaseField.setText("0");
        
        // reset data input for enter player menu
        firstNameField.setText("");
        lastNameField.setText("");
        enterPlayerNumberField.setText("");
        activeCheckBox.setSelected(false);
            
        // reset data input for enter game menu
        gameNumberField.setText("");
        opponentField.setText("");
        gameDatePicker.setValue(null);
    }
    
    // function to end the program
    private void exitButtonClicked() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
    
    // pad with " " to the right to the given length (n)
    public static String padRight(String s, int n) {
      return String.format("%1$-" + n + "s", s);
    }

    // pad with " " to the left to the given length (n)
    public static String padLeft(String s, int n) {
      return String.format("%1$" + n + "s", s);
    }

}
