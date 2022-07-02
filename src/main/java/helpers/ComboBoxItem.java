package helpers;

public class ComboBoxItem {
    private int value;
    private String text;

    public ComboBoxItem(int value, String text){
        this.value = value;
        this.text = text;
    }

    public int getValue() {return value; }

    public String getText() {return text; }

    @Override
    public String toString() { return text; }
}
