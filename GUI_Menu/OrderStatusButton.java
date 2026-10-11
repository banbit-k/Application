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
    // Pop-up แสดงรายละเอียดออร์เดอร์
    // d = {OrderNo, DateTime, Menu, Sweetness, Temperature, Price, Status}
    // =========================
    private void showOrderDetailPopup(JFrame parent, String[] d) {
        String sweetness = d[3].trim().equals("-") ? "-" : d[3].trim() + "%";

        String timeInfo = "";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime targetTime = LocalDateTime.parse(d[1].trim(), formatter).plusMinutes(5);
            long remainingSeconds = Duration.between(LocalDateTime.now(), targetTime).getSeconds();

            if (remainingSeconds > 0) {
                timeInfo = String.format("⏳ Remaining time: %d:%02d minutes\n(Expected completion: %s)",
                        remainingSeconds / 60, remainingSeconds % 60,
                        targetTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } else {
                timeInfo = "✅ Order is ready for service!";
            }
        } catch (Exception ex) {
            // อ่านเวลาไม่ได้ ก็ข้ามส่วนนับเวลา
        }

        String message = "📋 Order #" + d[0].trim() + "\n" +
                "🕒 Order Time: " + d[1].trim() + "\n" +
                "----------------------------------------\n" +
                "• Menu: " + d[2].trim() + "\n" +
                "• Sweetness: " + sweetness + "\n" +
                "• Temperature: " + d[4].trim() + "\n" +
                "• Price: " + d[5].trim() + " Baht\n" +
                "• Status: " + d[6].trim() + "\n" +
                "----------------------------------------\n" +
                timeInfo;

        JOptionPane.showMessageDialog(parent, message,
                "Order Status & Details", JOptionPane.INFORMATION_MESSAGE);
    }
}