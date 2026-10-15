package GUI_Menu;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class OrderStatusButton extends JButton {

    public OrderStatusButton(JFrame parentFrame) {
        super("Check Order");
        setFont(new Font("Tahoma", Font.BOLD, 12));
        setBackground(new Color(235, 125, 20));
        setForeground(Color.WHITE);
        setFocusPainted(false);

        // กดปุ่ม -> เปิดหน้าต่างให้กรอกเลข Order ก่อน
        addActionListener(e -> openInputDialog(parentFrame));
    }

    // =========================
    // หน้าต่างกรอกเลข Order
    // =========================
    private void openInputDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Check Order", true);
        dialog.setSize(340, 240);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Enter Order No.");
        title.setFont(new Font("Tahoma", Font.BOLD, 17));
        title.setBounds(30, 20, 270, 28);
        panel.add(title);

        JTextField field = new JTextField();
        field.setFont(new Font("Tahoma", Font.PLAIN, 18));
        field.setBounds(30, 58, 265, 40);
        panel.add(field);

        // ข้อความแจ้งเตือน (กรอกผิด / ไม่เจอออร์เดอร์)
        JLabel error = new JLabel(" ");
        error.setFont(new Font("Tahoma", Font.PLAIN, 12));
        error.setForeground(new Color(200, 30, 30));
        error.setBounds(30, 102, 265, 20);
        panel.add(error);

        RoundButton check = new RoundButton("Check");
        check.setBounds(30, 135, 265, 45);
        check.setBackground(RoundButton.ORANGE);
        check.setForeground(Color.WHITE);
        check.setFont(new Font("Tahoma", Font.BOLD, 16));
        panel.add(check);

        Runnable submit = () -> {
            String text = field.getText().trim().replace("#", "");
            if (text.isEmpty()) {
                error.setText("Please enter an order number");
                return;
            }
            int orderNo;
            try {
                orderNo = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                error.setText("Order number must be a number");
                return;
            }
            String[] order = findOrder(orderNo);
            if (order == null) {
                error.setText("Order #" + orderNo + " not found");
                return;
            }
            dialog.dispose();
            showOrderDetailPopup(parent, order);
        };

        check.addActionListener(e -> submit.run());
        field.addActionListener(e -> submit.run()); // กด Enter ก็ค้นหาได้

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // =========================
    // หาออร์เดอร์จากเลขใน Order.csv (ไม่เจอ = null)
    // =========================
    static String[] findOrder(int orderNo) {
        try {
            File file = new File("Order.csv");
            if (!file.exists()) return null;

            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                String[] d = lines.get(i).split(",", -1);
                if (d.length < 7) continue;
                try {
                    if (Integer.parseInt(d[0].trim()) == orderNo) return d;
                } catch (NumberFormatException ignored) { }
            }
        } catch (Exception ex) {
            // อ่านไฟล์ไม่ได้ ถือว่าไม่เจอ
        }
        return null;
    }

    // =========================
    // หน้าต่างแสดงรายละเอียดออร์เดอร์ + เวลานับถอยหลังแบบเรียลไทม์
    // initial = {OrderNo, DateTime, Menu, Sweetness, Temperature, Price, Status}
    // - เวลาลดลงทุก 1 วินาที
    // - อ่านสถานะจาก Order.csv ทุกวินาที ถ้า Admin กด Confirm (Ready) เวลาจะเป็น 0:00 ทันที
    // =========================
    private void showOrderDetailPopup(JFrame parent, String[] initial) {
        final String[] d = initial;
        final String sweetness = d[3].trim().equals("-") ? "-" : d[3].trim() + "%";

        // เวลาที่คาดว่าจะเสร็จ = เวลาสั่ง + 5 นาที
        LocalDateTime parsed = null;
        try {
            parsed = LocalDateTime.parse(d[1].trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).plusMinutes(5);
        } catch (Exception ex) {
            // อ่านเวลาไม่ได้ ก็ข้ามส่วนนับเวลา
        }
        final LocalDateTime target = parsed;

        int no = 0;
        try {
            no = Integer.parseInt(d[0].trim());
        } catch (NumberFormatException ignored) { }
        final int orderNo = no;

        JDialog dialog = new JDialog(parent, "Order Status & Details", true);
        dialog.setSize(340, 470);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Order #" + d[0].trim());
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        title.setBounds(30, 15, 270, 28);
        panel.add(title);

        JLabel timeLabel = new JLabel("Order Time: " + d[1].trim());
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setBounds(30, 45, 280, 22);
        panel.add(timeLabel);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(30, 75, 270, 2);
        panel.add(sep1);

        String[] rows = {
                "Menu: " + d[2].trim(),
                "Sweetness: " + sweetness,
                "Temperature: " + d[4].trim(),
                "Price: " + d[5].trim() + " Baht"
        };
        int y = 85;
        for (String r : rows) {
            JLabel l = new JLabel(r);
            l.setFont(new Font("Tahoma", Font.PLAIN, 15));
            l.setBounds(30, y, 270, 24);
            panel.add(l);
            y += 26;
        }

        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        statusLabel.setBounds(30, y, 270, 24);
        panel.add(statusLabel);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(30, y + 35, 270, 2);
        panel.add(sep2);

        JLabel remainCaption = new JLabel("Remaining time");
        remainCaption.setFont(new Font("Tahoma", Font.PLAIN, 14));
        remainCaption.setForeground(Color.GRAY);
        remainCaption.setBounds(30, y + 45, 270, 22);
        panel.add(remainCaption);

        JLabel remainLabel = new JLabel("-");
        remainLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        remainLabel.setForeground(RoundButton.ORANGE);
        remainLabel.setBounds(30, y + 65, 270, 50);
        panel.add(remainLabel);

        JLabel noteLabel = new JLabel(" ");
        noteLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        noteLabel.setBounds(30, y + 118, 280, 24);
        panel.add(noteLabel);

        RoundButton ok = new RoundButton("OK");
        ok.setBounds(30, y + 155, 270, 45);
        ok.setBackground(RoundButton.ORANGE);
        ok.setForeground(Color.WHITE);
        ok.setFont(new Font("Tahoma", Font.BOLD, 16));
        ok.addActionListener(e -> dialog.dispose());
        panel.add(ok);

        // อัปเดตหน้าจอ: อ่านสถานะล่าสุดจากไฟล์ + คำนวณเวลาที่เหลือ
        Runnable update = () -> {
            String[] cur = findOrder(orderNo);
            if (cur == null) cur = d;
            String status = cur[6].trim();
            boolean ready = status.equalsIgnoreCase(OrderManager.STATUS_READY);

            statusLabel.setText("Status: " + status);
            statusLabel.setForeground(ready ? new Color(30, 140, 60) : Color.BLACK);

            if (ready) {
                // Admin กด Confirm แล้ว -> เวลาเป็น 0 ทันที
                remainLabel.setText("0:00");
                noteLabel.setText("Your order is ready for service!");
                noteLabel.setForeground(new Color(30, 140, 60));
                return;
            }

            if (target == null) {
                remainLabel.setText("-");
                return;
            }

            long remaining = Math.max(0,
                    Duration.between(LocalDateTime.now(), target).getSeconds());
            remainLabel.setText(String.format("%d:%02d", remaining / 60, remaining % 60));

            if (remaining > 0) {
                noteLabel.setText("Expected completion: "
                        + target.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } else {
                noteLabel.setText("Waiting for the shop to confirm");
            }
            noteLabel.setForeground(Color.DARK_GRAY);
        };

        update.run(); // แสดงค่าทันทีตอนเปิด

        // เดินทุก 1 วินาที
        Timer timer = new Timer(1000, e -> update.run());
        timer.start();
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                timer.stop();
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
