import java.awt.*;

public class MessageStyle {
    private Color backgroundColor;
    private Color textColor;

    public MessageStyle(Color back, Color text) {
        this.backgroundColor = back;
        this.textColor = text;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getTextColor() {
        return textColor;
    }
}
