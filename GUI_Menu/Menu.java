package GUI_Menu;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import javax.swing.*;

public class Menu extends JPanel {

    public Menu(String name, String price, String imageName, int y) {
        // กำหนด Layout และขนาดของการ์ด
        setLayout(null);
        setBounds(0, y, 386, 150);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setBackground(Color.WHITE);

        // รูปภาพ
        JLabel imgLabel = new JLabel(loadImage(imageName));
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
    }

    // โหลดรูปและปรับขนาด
    protected ImageIcon loadImage(String fileName) {
        try {
            File imageFile = new File("image/" + fileName);
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            ImageIcon icon = new ImageIcon(imageBytes);

            Image scaled = icon.getImage().getScaledInstance(
                    125, 125, Image.SCALE_SMOOTH
            );

            return new ImageIcon(scaled);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
}
    

