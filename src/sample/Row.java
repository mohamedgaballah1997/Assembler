package sample;

import javafx.beans.property.SimpleStringProperty;

public class Row {
    private final SimpleStringProperty line = new SimpleStringProperty("");
    public Row(String l)
    {
        setLine(l);

    }
    public String getLine() {
        return line.get();
    }

    public void setLine(String l) {
        line.set(l);
    }
}
