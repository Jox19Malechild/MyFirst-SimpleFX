package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * Customer Manager classroom lab (ICT261 Lecture 3, slide 29).
 *
 * WHERE EACH RESPONSIBILITY BELONGS (slide 28):
 *   View       = the controls built in start() (fields, ComboBox, buttons, table).
 *                A bigger app would describe these in FXML + CSS.
 *   Controller = the setOnAction handlers (validate input, react to clicks).
 *   Service    = application rules (create / delete a customer). Here the rules
 *                are small, so they sit inside the handlers.
 *   DAO        = reads/writes stored data. Not needed: this lab keeps data in
 *                memory only, so closing the app loses it (slide 4).
 *   Model      = Customer.java
 */
public class CustomerManagerApp extends Application {

    // Step 2 (slide 16): a list that tells the table when it changes.
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // ---------- Step 1: the form (slides 7, 9, 10) ----------
        Label nameLabel = new Label("Customer name");
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        nameLabel.setLabelFor(nameField);

        Label provinceLabel = new Label("Province");
        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll(
                "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western");
        provinceBox.setPromptText("Choose a province");
        provinceLabel.setLabelFor(provinceBox);

        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true);          // Enter key presses Save (slide 26)
        Button deleteButton = new Button("Delete selected");
        Label status = new Label();                 // feedback messages (slide 21)

        // ---------- Step 3: the table (slides 17, 18, 19) ----------
        TableView<Customer> table = new TableView<>();
        table.setItems(customers);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        provinceCol.setPrefWidth(180);

        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);

        // ---------- Step 4: validate, then add (slides 12, 20, 27) ----------
        saveButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                status.setText("Enter the customer name.");
                nameField.requestFocus();
                return;                              // stop here, keep what the user typed
            }

            String province = provinceBox.getValue();
            if (province == null) {
                status.setText("Choose a province.");
                provinceBox.requestFocus();
                return;                              // the name stays in the field
            }

            customers.add(new Customer(name, province));
            status.setText("Customer saved.");
            nameField.clear();                       // clear only after a successful save
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        // ---------- Step 5: confirm deletion (slide 23) ----------
        deleteButton.setOnAction(event -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                status.setText("Select a customer first.");
                return;
            }

            ButtonType delete = new ButtonType("Delete");
            Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete the selected customer?",
                    delete, ButtonType.CANCEL);
            ask.setHeaderText("Confirm deletion");

            if (ask.showAndWait().orElse(ButtonType.CANCEL) == delete) {
                customers.remove(selected);
                status.setText("Customer deleted.");
            } else {
                status.setText("Deletion cancelled.");   // record stays untouched
            }
        });

        // ---------- Layout (Stage > Scene > root pane > nodes) ----------
        Label title = new Label("Customer Manager");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(provinceLabel, 0, 1);
        form.add(provinceBox, 1, 1);

        HBox buttons = new HBox(10, saveButton, deleteButton);

        VBox root = new VBox(12, title, form, buttons, status, table);
        root.setPadding(new Insets(15));
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 520, 480);
        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
        nameField.requestFocus();                    // start with the cursor in the name box
    }

    public static void main(String[] args) {
        launch(args);
    }
}
