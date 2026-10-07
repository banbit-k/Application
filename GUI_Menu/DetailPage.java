package GUI_Menu;

import java.awt.*;
import javax.swing.*;

// หน้ารายละเอียดเมนู (คลาสกลางของ Latte / Espresso / Americano)
public class DetailPage extends JFrame {

    // ตั้งฟอนต์ของ dialog (JOptionPane) เป็น Tahoma เพื่อให้แสดงภาษาไทยได้
    static {
        Font thai = new Font("Tahoma", Font.PLAIN, 14);
        UIManager.put("OptionPane.messageFont", thai);
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.BOLD, 14));
    }

    private String sweetness = "50";
    private String temperature = "Iced";

    // subtitle = null  -> ไม่แสดงบรรทัดใต้ชื่อ
    // hasSweetness = false -> ไม่แสดงปุ่มความหวาน
    public DetailPage(String name, String subtitle, int price, String imageName, boolean hasSweetness) {
        super(name);
        setSize(400, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ปิดแค่หน้านี้ ไม่ปิดทั้งโปรแกรม
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // รูปกาแฟ
        JLabel coffeeImage = new JLabel(Menu.loadImage(imageName, 280));
        coffeeImage.setBounds(60, 45, 280, 280);
        panel.add(coffeeImage);

        // ชื่อเมนู
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
        nameLabel.setBounds(40, 350, 300, 30);
        panel.add(nameLabel);

        if (subtitle != null) {
            JLabel sub = new JLabel(subtitle);
            sub.setFont(new Font("Tahoma", Font.PLAIN, 14));
            sub.setBounds(40, 380, 200, 25);
            panel.add(sub);
        }

        // Sweetness
        int tempY = 400;
        if (hasSweetness) {
            JLabel sweetLabel = new JLabel("Sweetness");
            sweetLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
            sweetLabel.setBounds(40, 425, 150, 25);
            panel.add(sweetLabel);

            String[] levels = {"25", "50", "75", "100"};
            RoundButton[] btns = new RoundButton[levels.length];
            for (int i = 0; i < levels.length; i++) {
                final String level = levels[i];
                btns[i] = new RoundButton(level);
                btns[i].setBounds(40 + i * 80, 460, 75, 45);
                panel.add(btns[i]);
            }
            RoundButton.select(btns[1], btns); // ค่าเริ่มต้น 50
            for (RoundButton b : btns) {
                b.addActionListener(e -> {
                    sweetness = b.getText();
                    RoundButton.select(b, btns);
                });
            }
            tempY = 520;
        }

        // Temperature
        JLabel tempLabel = new JLabel("Temperature");
        tempLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        tempLabel.setBounds(40, tempY, 150, 25);
        panel.add(tempLabel);

        RoundButton hot = new RoundButton("Hot");
        hot.setBounds(40, tempY + 35, 120, 45);
        panel.add(hot);

        RoundButton iced = new RoundButton("Iced");
        iced.setBounds(235, tempY + 35, 125, 45);
        panel.add(iced);

        RoundButton.select(iced, hot, iced); // ค่าเริ่มต้น Iced
        hot.addActionListener(e -> {
            temperature = "Hot";
            RoundButton.select(hot, hot, iced);
        });
        iced.addActionListener(e -> {
            temperature = "Iced";
            RoundButton.select(iced, hot, iced);
        });

        // ราคา
        JLabel priceText = new JLabel("Price");
        priceText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        priceText.setForeground(Color.GRAY);
        priceText.setBounds(40, 620, 100, 25);
        panel.add(priceText);

        JLabel priceLabel = new JLabel(price + " Baht");
        priceLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        priceLabel.setForeground(RoundButton.ORANGE);
        priceLabel.setBounds(40, 640, 120, 30);
        panel.add(priceLabel);

        // Buy Now
        RoundButton buy = new RoundButton("Buy Now");
        buy.setBounds(168, 625, 192, 50);
        buy.setBackground(RoundButton.ORANGE);
        buy.setForeground(Color.WHITE);
        buy.setFont(new Font("Tahoma", Font.BOLD, 16));
        panel.add(buy);

        buy.addActionListener(e -> {
            try {
                int orderNo = OrderManager.saveOrder(
                        name, hasSweetness ? sweetness : null, temperature, price);

                String no = String.format("%03d", orderNo);
                String detail = "Order No. #" + no
                        + "\nMenu: " + name
                        + "\nTemperature: " + temperature;
                if (hasSweetness) detail += "\nSweetness: " + sweetness + "%";
                detail += "\nPrice: " + price + " Baht";
                JOptionPane.showMessageDialog(this, detail, "Order #" + no,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save order: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
        setVisible(true);
    }
}