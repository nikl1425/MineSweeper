package sample;
import javafx.scene.control.Button;


public class Field {
    boolean isBomb = false;
    private boolean hasBeenPressed = false;
    private int x;
    private int y;
    private Button button;

    private int bombsAround;


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;

    }

    public boolean isHasBeenPressed() {
        return hasBeenPressed;
    }

    public void setHasBeenPressed(boolean hasBeenPressed) {
        this.hasBeenPressed = hasBeenPressed;
    }

    public int getX() {
        return x;
    }

    public void setX (int _x){

        this.x= _x;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }
}