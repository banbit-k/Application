import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import javax.swing.*;

public class GUI {

    // โหลดรูปและปรับขนาด
    public static ImageIcon loadImage(String fileName) {
        try {
            //String folder = System.getProperty("user.dir");
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

    // สร้างการ์ดเมนู
    public static JPanel createCard(
            String name,
            String price,
            String imageName,
            int y
    ) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(0, y, 386, 150);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        card.setBackground(Color.WHITE);

        // รูป
        JLabel imgLabel = new JLabel(loadImage(imageName));
        imgLabel.setBounds(15, 15, 125, 125);
        card.add(imgLabel);

        // ชื่อเมนู
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        nameLabel.setBounds(150, 20, 220, 25);
        card.add(nameLabel);

        // ราคา
        JLabel priceLabel = new JLabel("ราคา " + price + " บาท");
        priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        priceLabel.setBounds(150, 50, 200, 25);
        card.add(priceLabel);

        return card;
    }

    public static void main(String[] args) {

        JFrame f = new JFrame("ร้านกาแฟแฟนแก");

        Container cp = f.getContentPane();
        cp.setLayout(null);
        cp.setBackground(new Color(210, 180, 140));

        // =========================
        // ชื่อร้าน
        // =========================
        JLabel SN = new JLabel(
                "ร้านกาแฟแฟนแก",
                SwingConstants.CENTER
        );

        SN.setFont(new Font("Tahoma", Font.BOLD, 24));
        SN.setBorder(
                BorderFactory.createLineBorder(Color.BLACK, 4)
        );
        SN.setBounds(0, 0, 386, 60);

        cp.add(SN);

        // =========================
        // MENU
        // =========================
        JLabel Coffee = new JLabel("MENU");

        Coffee.setFont(
                new Font("Tahoma", Font.BOLD, 20)
        );
        Coffee.setBounds(10, 70, 386, 30);

        cp.add(Coffee);

        // =========================
        // เมนู 1 ลาเต้
        // =========================
        JPanel card1 = createCard(
                "ลาเต้ (Latte)",
                "35",
                "latte.jpg",
                110
        );

        // =========================
        // เมนู 2 เอสเปรสโซ่
        // =========================
        JPanel card2 = createCard(
                "เอสเปรสโซ่ (Espresso)",
                "40",
                "espresso.jpg",
                270
        );

        // =========================
        // เมนู 3 อเมริกาโน่
        // =========================
        JPanel card3 = createCard(
                "อเมริกาโน่ (Americano)",
                "30",
                "americano.jpg",
                430
        );

        cp.add(card1);
        cp.add(card2);
        cp.add(card3);

        // =========================
        // ตั้งค่าหน้าต่าง
        // =========================
        f.setSize(400, 650);
        f.setResizable(false);
        f.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}