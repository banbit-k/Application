package GUI_Menu;

import java.awt.*;
import javax.swing.*;

// ปุ่มแบบขอบมน (ใช้ร่วมกันทุกหน้า)
public class RoundButton extends JButton {

    public static final Color ORANGE = new Color(235, 125, 20);
    public static final Color LIGHT_ORANGE = new Color(255, 245, 235);

    public RoundButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFont(new Font("Tahoma", Font.PLAIN, 15));
        setBorderPainted(false);
        setBackground(Color.WHITE);
        setForeground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(getModel().isPressed() ? new Color(220, 110, 10) : getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2.drawString(getText(), x, y);
        g2.dispose();
    }

    // เลือกปุ่มหนึ่งในกลุ่ม ที่เหลือกลับเป็นสีปกติ
    public static void select(RoundButton chosen, RoundButton... group) {
        for (RoundButton b : group) {
            boolean on = (b == chosen);
            b.setBackground(on ? LIGHT_ORANGE : Color.WHITE);
            b.setForeground(on ? ORANGE : Color.DARK_GRAY);
        }
    }
}
