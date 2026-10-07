package GUI_Menu;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import javax.swing.*;

public class Menu extends JPanel {

    private Runnable onClick;

    public Menu(String name, String price, String imageName, int y) {
        // กำหนด Layout และขนาดของการ์ด
        setLayout(null);
        setBounds(0, y, 386, 150);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setBackground(Color.WHITE);

        // รูปภาพ
        JLabel imgLabel = new JLabel(loadImage(imageName, 125));
        imgLabel.setBounds(15, 15, 125, 125);
        add(imgLabel);

        // ชื่อเมนู
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        nameLabel.setBounds(150, 20, 220, 25);
        add(nameLabel);

        // ราคา
        JLabel priceLabel = new JLabel("ราคา " + price + " บาท");
        priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        priceLabel.setBounds(150, 50, 200, 25);
        add(priceLabel);

        // ปุ่มไปหน้ารายละเอียด (มุมขวาล่างของการ์ด)
        RoundButton goButton = new RoundButton("สั่งซื้อ");
        goButton.setBounds(250, 95, 115, 40);
        goButton.setBackground(RoundButton.ORANGE);
        goButton.setForeground(Color.WHITE);
        goButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        goButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        goButton.addActionListener(e -> {
            if (onClick != null) onClick.run();
        });
        add(goButton);
    }

    // กำหนดสิ่งที่จะทำเมื่อกดปุ่ม
    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    // โหลดรูปจากโฟลเดอร์ image/ และปรับขนาด (ใช้ร่วมกันทุกหน้า)
    public static ImageIcon loadImage(String fileName, int size) {
        try {
            File imageFile = new File("image/" + fileName);
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            ImageIcon icon = new ImageIcon(imageBytes);
            Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
}