import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import javax.swing.*;

public class GUILatte {

    // สีหลัก
    static Color orange = new Color(235, 125, 20);
    static Color lightOrange = new Color(255, 245, 235);
    static Color borderColor = new Color(225, 225, 225);

    // ปุ่มแบบขอบมน
    static class RoundButton extends JButton {
        public RoundButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setFont(new Font("Tahoma", Font.PLAIN, 15));
            setBorderPainted(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (getModel().isPressed()) {
                g2.setColor(new Color(220, 110, 10));
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(
                    0, 0,
                    getWidth() - 1,
                    getHeight() - 1,
                    18, 18
            );

            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();

            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;

            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    public static ImageIcon loadImage(String fileName) {
        try {
            String folder = System.getProperty("user.dir");
            File file = new File(folder, fileName);

            byte[] data = Files.readAllBytes(file.toPath());
            ImageIcon icon = new ImageIcon(data);

            Image image = icon.getImage().getScaledInstance(
                    280, 280, Image.SCALE_SMOOTH
            );

            return new ImageIcon(image);

        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {

        JFrame f = new JFrame("Latte");
        f.setSize(400, 800);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);


        // =========================
        // รูปกาแฟ
        // =========================

        JLabel coffeeImage = new JLabel(loadImage("latte.jpg"));
        coffeeImage.setBounds(60, 45, 280, 280);
        panel.add(coffeeImage);

        // =========================
        // ชื่อ Latte
        // =========================

        JLabel name = new JLabel("Latte");
        name.setFont(new Font("Tahoma", Font.BOLD, 21));
        name.setBounds(40, 350, 200, 30);
        panel.add(name);

        // =========================
        // With Milk
        // =========================

        JLabel milk = new JLabel("with Milk");
        milk.setFont(new Font("Tahoma", Font.PLAIN, 14));
        milk.setBounds(40, 380, 200, 25);
        panel.add(milk);

        // =========================
        // Sweetness
        // =========================

        JLabel sweetness = new JLabel("Sweetness");
        sweetness.setFont(new Font("Tahoma", Font.BOLD, 17));
        sweetness.setBounds(40, 425, 150, 25);
        panel.add(sweetness);

        // ปุ่ม 25
        RoundButton b25 = new RoundButton("25");
        b25.setBounds(40, 460, 75, 45);
        b25.setBackground(Color.WHITE);
        b25.setForeground(Color.DARK_GRAY);
        panel.add(b25);

        // ปุ่ม 50
        RoundButton b50 = new RoundButton("50");
        b50.setBounds(120, 460, 75, 45);
        b50.setBackground(lightOrange);
        b50.setForeground(orange);
        panel.add(b50);

        // ปุ่ม 75
        RoundButton b75 = new RoundButton("75");
        b75.setBounds(200, 460, 75, 45);
        b75.setBackground(Color.WHITE);
        b75.setForeground(Color.DARK_GRAY);
        panel.add(b75);

        // ปุ่ม 100
        RoundButton b100 = new RoundButton("100");
        b100.setBounds(280, 460, 75, 45);
        b100.setBackground(Color.WHITE);
        b100.setForeground(Color.DARK_GRAY);
        panel.add(b100);
        // =========================
        // Temperature
        // =========================

        JLabel temperature = new JLabel("Temperature");
        temperature.setFont(new Font("Tahoma", Font.BOLD, 17));
        temperature.setBounds(40, 520, 150, 25);
        panel.add(temperature);

        // Hot
        RoundButton hot = new RoundButton("Hot");
        hot.setBounds(40, 555, 120, 45);
        hot.setBackground(Color.WHITE);
        hot.setForeground(Color.DARK_GRAY);
        panel.add(hot);

        // Iced
        RoundButton iced = new RoundButton("Iced");
        iced.setBounds(235, 555, 125, 45);
        iced.setBackground(lightOrange);
        iced.setForeground(orange);
        panel.add(iced);

        // =========================
        // ราคา
        // =========================

        JLabel priceText = new JLabel("Price");
        priceText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        priceText.setForeground(Color.GRAY);
        priceText.setBounds(40, 620, 100, 25);
        panel.add(priceText);

        JLabel price = new JLabel("35 Bath");
        price.setFont(new Font("Tahoma", Font.BOLD, 20));
        price.setForeground(orange);
        price.setBounds(40, 640, 120, 30);
        panel.add(price);

        // =========================
        // Buy Now
        // =========================

        RoundButton buy = new RoundButton("Buy Now");
        buy.setBounds(168, 625, 192, 50);
        buy.setBackground(orange);
        buy.setForeground(Color.WHITE);
        buy.setFont(new Font("Tahoma", Font.BOLD, 16));
        panel.add(buy);

        // =========================
        // การทำงานของปุ่ม
        // =========================
// =========================
// การเลือก Sweetness
// =========================

        b25.addActionListener(e -> {
            b25.setBackground(lightOrange);
            b25.setForeground(orange);

            b50.setBackground(Color.WHITE);
            b50.setForeground(Color.DARK_GRAY);

            b75.setBackground(Color.WHITE);
            b75.setForeground(Color.DARK_GRAY);

            b100.setBackground(Color.WHITE);
            b100.setForeground(Color.DARK_GRAY);
        });

        b50.addActionListener(e -> {
            b25.setBackground(Color.WHITE);
            b25.setForeground(Color.DARK_GRAY);

            b50.setBackground(lightOrange);
            b50.setForeground(orange);

            b75.setBackground(Color.WHITE);
            b75.setForeground(Color.DARK_GRAY);

            b100.setBackground(Color.WHITE);
            b100.setForeground(Color.DARK_GRAY);
        });

        b75.addActionListener(e -> {
            b25.setBackground(Color.WHITE);
            b25.setForeground(Color.DARK_GRAY);

            b50.setBackground(Color.WHITE);
            b50.setForeground(Color.DARK_GRAY);

            b75.setBackground(lightOrange);
            b75.setForeground(orange);

            b100.setBackground(Color.WHITE);
            b100.setForeground(Color.DARK_GRAY);
        });

        b100.addActionListener(e -> {
            b25.setBackground(Color.WHITE);
            b25.setForeground(Color.DARK_GRAY);

            b50.setBackground(Color.WHITE);
            b50.setForeground(Color.DARK_GRAY);

            b75.setBackground(Color.WHITE);
            b75.setForeground(Color.DARK_GRAY);

            b100.setBackground(lightOrange);
            b100.setForeground(orange);
        });

        hot.addActionListener(e -> {
            hot.setBackground(lightOrange);
            hot.setForeground(orange);

            iced.setBackground(Color.WHITE);
            iced.setForeground(Color.DARK_GRAY);
        });

        iced.addActionListener(e -> {
            hot.setBackground(Color.WHITE);
            hot.setForeground(Color.DARK_GRAY);

            iced.setBackground(lightOrange);
            iced.setForeground(orange);
        });

        buy.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    f,
                    "สั่ง Latte ราคา 35 Bath",
                    "Order",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        f.add(panel);
        f.setVisible(true);
    }
}