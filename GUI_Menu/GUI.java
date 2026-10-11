package GUI_Menu;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import javax.swing.*;

public class GUI {

    public static boolean hasOrders() {
        try {
            File file = new File("Order.csv");
            if (!file.exists()) return false;
            
            List<String> lines = Files.readAllLines(file.toPath());
            // ถ้ามีมากกว่า 1 บรรทัด และบรรทัดถัดไปมีข้อมูล (ไม่ใช่บรรทัดว่าง)
            if (lines.size() > 1) {
                for (int i = 1; i < lines.size(); i++) {
                    if (!lines.get(i).trim().isEmpty()) {
                        return true; // พบออเดอร์
                    }
                }
            }
        } catch (Exception e) {
            // if Error
        }
        return false; // ยังไม่มีออเดอร์
    }

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

        final OrderStatusButton[] checkStatusBtn = {null};

        // ตรวจสอบตอนเปิดโปรแกรมครั้งแรก
        if (hasOrders()) {
            checkStatusBtn[0] = new OrderStatusButton(f);
            checkStatusBtn[0].setBounds(200, 70, 170, 30);
            cp.add(checkStatusBtn[0]);
        }

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                // ถ้ามีออเดอร์แล้ว แต่ยังไม่มีปุ่มแสดงอยู่บนหน้าจอ
                if (hasOrders() && checkStatusBtn[0] == null) {
                    checkStatusBtn[0] = new OrderStatusButton(f);
                    checkStatusBtn[0].setBounds(200, 70, 170, 30);
                    cp.add(checkStatusBtn[0]);
                    
                    // สั่งให้หน้าจอทำการรีเฟรช (Revalidate & Repaint) เพื่อแสดงปุ่มทันที
                    cp.revalidate();
                    cp.repaint();
                }
            }
        });

        f.setSize(400, 650);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
