package org.cis120.minesweeper;

public class Square {
    private int value;
    private boolean enabled;
    private boolean visible;
    private boolean isFlagged;


    public Square(int v) {
        value = v;
        enabled = true;
        visible = false;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int v) {
        value = v;
    }

    public boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(boolean b) {
        enabled = b;
    }

    public boolean getVisible() {
        return visible;
    }
    public void setVisible(boolean b) {
        visible = b;
    }
    public boolean isFlagged() {
        return isFlagged;
    }
    public void setFlagged(boolean b) {
        isFlagged = b;
    }

}
