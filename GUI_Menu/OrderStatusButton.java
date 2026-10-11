package GUI_Menu;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.time.*;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class OrderStatusButton extends JButton {

    private LocalDateTime targetTime;
    private String currentMenu = "";
    private String currentSweetness = "";
    private String currentTemp = "";
    private String currentPrice = "";
    private String currentStatus = "";
    private String currentOrderNo = "";
    private String currentDateTimeStr = "";

    public OrderStatusButton(JFrame parentFrame) {
        // ใช้ชื่อปุ่มเริ่มต้นตามต้องการ เช่น "Check Order" หรือ "ตรวจสอบสถานะคิว"
        super("Check Order");
        setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
        setBackground(new java.awt.Color(235, 125, 20));
        setForeground(java.awt.Color.WHITE);
        setFocusPainted(false);

        // โหลดข้อมูลและเริ่มนับเวลา
        updateOrderStatusData();

        // เมื่อกดที่ปุ่ม จะเด้ง Pop-up แสดง Detail ทั้งหมด
        addActionListener(e -> showOrderDetailPopup(parentFrame));
    }

    // ฟังก์ชันอ่านข้อมูลล่าสุดจาก Order.csv มาเก็บไว้
    private void updateOrderStatusData() {
        try {
            File file = new File("Order.csv");
            if (!file.exists()) return;

            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() > 1) {
                String lastLine = lines.get(lines.size() - 1);
                String[] data = lastLine.split(",");
                
                currentOrderNo = data[0];
                currentDateTimeStr = data[1];
                currentMenu = data[2];
                currentSweetness = data[3];
                currentTemp = data[4];
                currentPrice = data[5];
                currentStatus = data[6];

                // แปลงเวลาและบวกเพิ่ม 5 นาทีเป็นเวลาเป้าหมาย
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime orderTime = LocalDateTime.parse(currentDateTimeStr.trim(), formatter);
                targetTime = orderTime.plusMinutes(5);
            }
        } catch (Exception ex) {
            // จัดการ Error เงียบๆ หรือพิมพ์ debug
        }
    }

    // ฟังก์ชันแสดง Pop-up เมื่อกดคลิกที่ปุ่ม
    private void showOrderDetailPopup(JFrame parent) {
        // อัปเดตข้อมูลล่าสุดก่อนแสดง
        updateOrderStatusData();

        if (targetTime == null) {
            JOptionPane.showMessageDialog(parent, "No order data available", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, targetTime);
        long remainingSeconds = duration.getSeconds();
        
        String timeInfo = "";
        if (remainingSeconds > 0) {
            long minutes = remainingSeconds / 60;
            long seconds = remainingSeconds % 60;
            timeInfo = String.format("⏳ Remaining time: %d:%02d minutes\n(Expected completion: %s)", 
                                     minutes, seconds, targetTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } else {
            timeInfo = "✅ Order is ready for service!";
        }

        String message = "📋 Latest Order (Order #" + currentOrderNo + ")\n" +
                         "🕒 Order Time: " + currentDateTimeStr + "\n" +
                         "----------------------------------------\n" +
                         "• Menu: " + currentMenu + "\n" +
                         "• Sweetness: " + currentSweetness + "%\n" +
                         "• Temperature: " + currentTemp + "\n" +
                         "• Price: " + currentPrice + " Baht\n" +
                         "• Status: " + currentStatus + "\n" +
                         "----------------------------------------\n" +
                         timeInfo;
        
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Order Status & Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}