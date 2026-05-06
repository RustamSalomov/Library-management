package org.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.actions.LibraryActionHandler;
import org.example.exceptions.LibraryActionException;
import org.example.models.*;

public class LibraryDashboard {

    private BorderPane root;

    private ObservableList<BookItem> books;

    private TableView<BookItem> table;

    private TextField barcodeField;
    private TextField titleField;
    private TextField isbnField;
    private TextField memberIdField;
    private TextField memberNameField;

    private TextArea outputArea;

    private Member currentMember;

    private LibraryActionHandler actionHandler;

    public LibraryDashboard() {

        actionHandler = new LibraryActionHandler();

        currentMember = new Member("M001", "Zohidjon");

        books = FXCollections.observableArrayList();

        root = new BorderPane();

        root.setStyle("-fx-background-color: #f4f6f9;");

        createTopSection();
        createLeftSection();
        createCenterSection();
        createBottomSection();

        loadSampleBooks();
    }

    public Parent getRoot() {
        return root;
    }

    private void createTopSection() {

        Label title = new Label("LIBRARY MANAGEMENT SYSTEM");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        HBox top = new HBox(title);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(20));
        top.setStyle("-fx-background-color: #1e3c72;");

        root.setTop(top);
    }

    private void createLeftSection() {

        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(300);

        leftPanel.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: lightgray;"
        );

        Label sectionTitle = new Label("Manage Books");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        barcodeField = new TextField();
        barcodeField.setPromptText("Barcode");

        titleField = new TextField();
        titleField.setPromptText("Book Title");

        isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        memberIdField = new TextField();
        memberIdField.setPromptText("Member ID");

        memberNameField = new TextField();
        memberNameField.setPromptText("Member Name");

        Button addBookBtn = createButton("Add Book", "#27ae60");
        Button removeBookBtn = createButton("Remove Book", "#c0392b");
        Button checkoutBtn = createButton("Checkout Book", "#2980b9");
        Button returnBtn = createButton("Return Book", "#8e44ad");
        Button renewBtn = createButton("Renew Book", "#d35400");

        addBookBtn.setOnAction(e -> addBook());
        removeBookBtn.setOnAction(e -> removeBook());
        checkoutBtn.setOnAction(e -> checkoutBook());
        returnBtn.setOnAction(e -> returnBook());
        renewBtn.setOnAction(e -> renewBook());

        leftPanel.getChildren().addAll(
                sectionTitle,
                barcodeField,
                titleField,
                isbnField,
                memberIdField,
                memberNameField,
                addBookBtn,
                removeBookBtn,
                checkoutBtn,
                returnBtn,
                renewBtn
        );

        root.setLeft(leftPanel);
    }

    private void createCenterSection() {

        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(20));

        Label booksLabel = new Label("Books Database");
        booksLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        table = new TableView<>();

        TableColumn<BookItem, String> barcodeColumn = new TableColumn<>("Barcode");
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        barcodeColumn.setPrefWidth(150);

        TableColumn<BookItem, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getBook().getTitle()
                )
        );
        titleColumn.setPrefWidth(300);

        TableColumn<BookItem, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getBook().getISBN()
                )
        );
        isbnColumn.setPrefWidth(200);

        TableColumn<BookItem, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getStatus().toString()
                )
        );
        statusColumn.setPrefWidth(150);

        table.getColumns().addAll(barcodeColumn, titleColumn, isbnColumn, statusColumn);

        table.setItems(books);

        centerBox.getChildren().addAll(booksLabel, table);

        root.setCenter(centerBox);
    }

    private void createBottomSection() {

        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(15));

        Label logLabel = new Label("System Logs");
        logLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(140);

        bottom.getChildren().addAll(logLabel, outputArea);

        root.setBottom(bottom);
    }

    private Button createButton(String text, String color) {

        Button button = new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 10px;"
        );

        return button;
    }

    private void addBook() {

        try {

            String barcode = barcodeField.getText();
            String title = titleField.getText();
            String isbn = isbnField.getText();

            if (barcode.isEmpty() || title.isEmpty() || isbn.isEmpty()) {
                showMessage("Please fill all book fields.");
                return;
            }

            Book book = new Book(isbn, title, "General", "Unknown Publisher");
            BookItem item = new BookItem(barcode, book);

            books.add(item);

            showMessage("Book added successfully: " + title);

            clearFields();

        } catch (Exception e) {
            showMessage("Error while adding book.");
        }
    }

    private void removeBook() {

        BookItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Select a book first.");
            return;
        }

        books.remove(selected);

        showMessage("Book removed successfully.");
    }

    private void checkoutBook() {

        BookItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a book.");
            return;
        }

        try {

            actionHandler.checkOutBook(currentMember, selected);

            table.refresh();

            showMessage("Book checked out successfully.");

        } catch (LibraryActionException e) {
            showMessage(e.getMessage());
        }
    }

    private void returnBook() {

        BookItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a book.");
            return;
        }

        try {

            double fine = actionHandler.returnBook(currentMember, selected);

            table.refresh();

            if (fine > 0) {
                showMessage("Book returned. Fine: $" + fine);
            } else {
                showMessage("Book returned successfully.");
            }

        } catch (LibraryActionException e) {
            showMessage(e.getMessage());
        }
    }

    private void renewBook() {

        BookItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a book.");
            return;
        }

        try {

            actionHandler.renewBook(currentMember, selected);

            showMessage("Book renewed successfully.");

        } catch (LibraryActionException e) {
            showMessage(e.getMessage());
        }
    }

    private void showMessage(String message) {
        outputArea.appendText(message + "\n");
    }

    private void clearFields() {

        barcodeField.clear();
        titleField.clear();
        isbnField.clear();
    }

    private void loadSampleBooks() {

        Book book1 = new Book("978-111", "Java Programming", "Programming", "Oracle");
        Book book2 = new Book("978-222", "Database Systems", "Database", "Pearson");
        Book book3 = new Book("978-333", "Data Structures", "CS", "McGraw Hill");

        books.add(new BookItem("B001", book1));
        books.add(new BookItem("B002", book2));
        books.add(new BookItem("B003", book3));
    }
}
