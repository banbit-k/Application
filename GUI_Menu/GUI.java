package GUI_Menu;

import java.awt.*;
import javax.swing.*;

public class GUI {

    public static void main(String[] args) {

        JFrame f = new JFrame("ร้านกาแฟแฟนแก");

        Container cp = f.getContentPane();
        cp.setLayout(null);
        cp.setBackground(new Color(210, 180, 140));

        // ชื่อร้าน
        JLabel SN = new JLabel("ร้านกาแฟแฟนแก", SwingConstants.CENTER);
        SN.setFont(new Font("Tahoma", Font.BOLD, 24));
        SN.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        SN.setBounds(0, 0, 386, 60);
        cp.add(SN);

        // MENU
        JLabel Coffee = new JLabel("MENU");
        Coffee.setFont(new Font("Tahoma", Font.BOLD, 20));
        Coffee.setBounds(10, 70, 386, 30);
        cp.add(Coffee);

        // เมนูกาแฟ (ราคาตรงกับหน้ารายละเอียด)
        CoffeeMenuCard card1 = new CoffeeMenuCard("ลาเต้ (Latte)", "35", "latte.jpg", 110);
        CoffeeMenuCard card2 = new CoffeeMenuCard("เอสเปรสโซ่ (Espresso)", "40", "espresso.jpg", 270);
        CoffeeMenuCard card3 = new CoffeeMenuCard("อเมริกาโน่ (Americano)", "30", "americano.jpg", 430);

        // คลิกการ์ดแล้วเปิดหน้ารายละเอียด
        card1.setOnClick(() -> new GUILatte());
        card2.setOnClick(() -> new GUIEspresso());
        card3.setOnClick(() -> new GUIAmericano());

        cp.add(card1);
        cp.add(card2);
        cp.add(card3);

        // ตั้งค่าหน้าต่าง
        f.setSize(400, 650);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
