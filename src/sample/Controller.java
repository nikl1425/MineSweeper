package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Optional;
import java.util.Random;

public class Controller {
    private int score = 0;
    private int amountOfBombs = 0; //int vi bruger til at bestemme antallet af bomber.
    private int counterForWin = 0;
    private int width = 9;   // Værdi som antager bredden af kommende grid
    private int height = 9;  // Værdi som antager højden af kommende grid
    private Field[][] fields = new Field[width][height];   // Laver et grid med array (x,y-koordinatsystem) vha. Field class'en

    @FXML
    Pane mainPane;

    // initialize er en metode, der først går igang når alting er klart modsat en konstruktor
    @FXML
    void initialize() {
        generateFields();
        generateButtons();
}

    // fylder vores grid med buttons. altså vores 2d array med knapper...
    private void generateButtons() {
        for (int y = 0; y < width; y++) {  // for-løkke til at generere samtlige knapper i feltet.
            for (int x = 0; x < height; x++) {
                Button myButton = new Button();
                mainPane.getChildren().add(myButton);
                fields[x][y].setButton(myButton);
                int finalX = x;
                int finalY = y;


                myButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        MouseButton button = event.getButton();

                        if(button == MouseButton.SECONDARY) {
                            myButton.setText("⛳");
                        }
                        if(button == MouseButton.SECONDARY && fields[finalX][finalY].isBomb()) {
                            score = score + 1;
                            System.out.println(String.valueOf(score));
                        }
                        if(score >= amountOfBombs && counterForWin == 71) {
                            triggerWinCondition(finalX, finalY);
                        }
                    }
                });


                // ActionEvent bruges til at finde ud af om knappen er blevet trykket på:
                myButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        System.out.println(finalX + "," + finalY);  // Til egen hjælp, for at se koordinatet for den trykkede knap
                        recursiveFill(finalX, finalY);
// viser bombe hvis spilleren trykker på et felt der er en bombe.
                        if(fields[finalX][finalY].isBomb()) {
                            myButton.setText(" \uD83D\uDCA3 ");  // Når der trykkes på en bombe ændre knappen til X
                            triggerLoseCondition(finalX, finalY);

                        }
                    }
                });

                // Sætter knappens højde/bredde
                myButton.setPrefWidth(50);
                myButton.setPrefHeight(50);
                myButton.setLayoutY(y * 50);
                myButton.setLayoutX(x * 50);
                fields[x][y].getButton().setStyle("");
                fields[x][y].getButton().setStyle("-fx-background-color: silver; -fx-border-color: darkgrey");

            }
        }

    }
// dette er metoden, der viser feltet, basically får vores spil til at blive minesweeper.
    private void recursiveFill(int x, int y) {
        if(x < width && x >= 0 && y < height && y >= 0 && !fields[x][y].isBomb() && !fields[x][y].isHasBeenPressed()) {
            fields[x][y].setHasBeenPressed(true);
            fields[x][y].getButton().setStyle("-fx-background-color: grey; -fx-border-color: darkgrey;");
            //Her bruger vi Int der er lavet længere ned til at tjekke for bombs.
            int checkingBombs = checkForBombsAround(x - 1, y + 1);
            fields[x][y].setBombsAround(checkingBombs);
            if(checkingBombs > 0) {
                fields[x][y].getButton().setText(String.valueOf(checkingBombs));
                fields[x][y].getButton().setStyle("-fx-background-color: lightskyblue");
            }
            if (fields[x][y].isHasBeenPressed() == true)
            {
                counterForWin++;
                System.out.println(""+ counterForWin);
            }
            // recursive method, vi kalder inden i vores metode, altså en metode der kalder sig selv.
            if(checkingBombs <= 0) {
                recursiveFill(x, y - 1);
                recursiveFill(x, y + 1);
                recursiveFill(x + 1, y);
                recursiveFill(x - 1, y);
                recursiveFill(x + 1, y + 1);
                recursiveFill(x - 1, y - 1);
                recursiveFill(x - 1, y + 1);
                recursiveFill(x + 1, y - 1);
            }
        }
    }

    // pop-up hvis spiller taber (rammer bombe), den har en OK knap, der kan reset spil "initialize"
    void triggerLoseCondition(int x, int y) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("LOSER!");
        alert.setHeaderText("You Lost THE GAME");
        alert.setContentText("I have a great message for you!");
        Optional<ButtonType> result = alert.showAndWait();
        if(alert.getResult() == ButtonType.OK) {
            score = 0;
            amountOfBombs = 0;
            counterForWin = 0;
            initialize();
        }
    }


    // pop-up hvis spiller vinder, alert med OK knap, som kan reset spil "initialize"
    void triggerWinCondition(int x, int y) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WINNER");
        alert.setHeaderText("You won THE GAME");
        alert.setContentText("I have a great message for you!");
        ButtonType buttonTypeOne = new ButtonType("One");
        Optional<ButtonType> result = alert.showAndWait();
        if(alert.getResult() == ButtonType.OK) {
            score = 0;
            amountOfBombs = 0;
            counterForWin = 0;
            initialize();
        }
    }

    //dette er den int, som vi giver vores felter til, at angive antallet af bomber omkring den.
    private int checkForBombsAround(int x, int y) {
        int amount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(x + i >= 0 && x + i < width && y - j >= 0 && y - j < height && fields[x + i][y - j].isBomb()) {
                    amount++;
                }
            }
        }
        return amount;

    }

    public void generateFields() {
        // Feltet genereres samtidig med der bliver placeret random bomber.
        int bombsPlaced = 0;
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                fields[x][y] = new Field();
                fields[x][y].setX(x);
                fields[x][y].setY(y);
            }
        }
        //4LOOP der sætter 10 bomber, dette er løsningen istedet for math.random
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                while (bombsPlaced < 10) {
                    Random rng = new Random();
                    //nedenstående så jeg ved de er i mit grid
                    int xnrg = rng.nextInt(9);
                    int ynrg = rng.nextInt(9);
                    if(!fields[xnrg][ynrg].isBomb()) {
                        fields[xnrg][ynrg].isBomb = true;
                        bombsPlaced++;
                        amountOfBombs = amountOfBombs + 1;
                        System.out.println("" + amountOfBombs);
                    }
                }
            }

        }

    }
}
