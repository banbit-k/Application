package GUI_Menu;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// หน้า Admin: ดูออเดอร์ที่ส่งเข้ามาทั้งหมด และกด Confirm เพื่อเปลี่ยนสถานะเป็น Ready
public class AdminPage extends JFrame {

    private static AdminPage instance; // เปิดได้ทีละหน้าต่างเดียว

    private static final Color GREEN = new Color(30, 140, 60);

    private final JPanel listPanel = new JPanel();
    private final JLabel summary = new JLabel(" ");
    private String lastSnapshot = null;

    // เรียกจากปุ่ม Admin ในหน้าเมนู
    public static void open() {
        if (instance != null && instance.isDisplayable()) {
            instance.setState(JFrame.NORMAL);
            instance.toFront();
            instance.refresh(true);
            return;
        }
        instance = new AdminPage();
    }

    private AdminPage() {
        super("Admin - Confirm Orders");
        setSize(400, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ปิดแค่หน้านี้ ไม่ปิดทั้งโปรแกรม
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ส่วนหัว
        JPanel header = new JPanel(null);
        header.setBackground(new Color(210, 180, 140));
        header.setPreferredSize(new Dimension(400, 80));

        JLabel title = new JLabel("Admin");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBounds(20, 8, 200, 35);
        header.add(title);

        summary.setFont(new Font("Tahoma", Font.PLAIN, 14));
        summary.setBounds(20, 44, 250, 25);
        header.add(summary);

        RoundButton refreshBtn = new RoundButton("Refresh");
        refreshBtn.setBounds(280, 25, 95, 35);
        refreshBtn.setBackground(RoundButton.ORANGE);
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        refreshBtn.addActionListener(e -> refresh(true));
        header.add(refreshBtn);

        add(header, BorderLayout.NORTH);

        // รายการออเดอร์
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refresh(true);

        // รีเฟรชอัตโนมัติทุก 3 วินาที เพื่อให้เห็นออเดอร์ใหม่ที่เข้ามา
        Timer timer = new Timer(3000, e -> refresh(false));
        timer.start();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                timer.stop();
            }
        });

        setVisible(true);
    }

    // force = true -> วาดใหม่เสมอ, false -> วาดใหม่เฉพาะตอนข้อมูลเปลี่ยน
    private void refresh(boolean force) {
        List<String[]> orders;
        try {
            orders = OrderManager.readOrders();
        } catch (IOException ex) {
            orders = new ArrayList<>();
        }

        StringBuilder sb = new StringBuilder();
        for (String[] d : orders) sb.append(String.join(",", d)).append('\n');
        String snapshot = sb.toString();
        if (!force && snapshot.equals(lastSnapshot)) return;
        lastSnapshot = snapshot;

        listPanel.removeAll();

        int pending = 0;
        // ออเดอร์ที่ยังไม่ Ready ขึ้นก่อน (เก่าสุดก่อน เพื่อทำตามลำดับคิว)
        for (String[] d : orders) {
            if (!isReady(d)) {
                listPanel.add(createCard(d));
                listPanel.add(Box.createVerticalStrut(10));
                pending++;
            }
        }
        // ออเดอร์ที่ Ready แล้วไว้ด้านล่าง (ใหม่สุดก่อน)
        for (int i = orders.size() - 1; i >= 0; i--) {
            String[] d = orders.get(i);
            if (isReady(d)) {
                listPanel.add(createCard(d));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        if (orders.isEmpty()) {
            JLabel empty = new JLabel("No orders yet");
            empty.setFont(new Font("Tahoma", Font.PLAIN, 16));
            empty.setForeground(Color.GRAY);
            listPanel.add(empty);
        }

        summary.setText("Pending: " + pending + "  |  Total: " + orders.size());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private boolean isReady(String[] d) {
        return d[6].trim().equalsIgnoreCase(OrderManager.STATUS_READY);
    }

    // การ์ดของออเดอร์ 1 รายการ
    // d = {OrderNo, DateTime, Menu, Sweetness, Temperature, Price, Status}
    private JPanel createCard(String[] d) {
        boolean ready = isReady(d);
        int orderNo = 0;
        try {
            orderNo = Integer.parseInt(d[0].trim());
        } catch (NumberFormatException ignored) { }
        final int no = orderNo;

        JPanel card = new JPanel(null);
        Dimension size = new Dimension(350, 115);
        card.setPreferredSize(size);
        card.setMaximumSize(size);
        card.setMinimumSize(size);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBackground(ready ? new Color(245, 250, 245) : Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel noLabel = new JLabel("Order #" + String.format("%03d", no));
        noLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        noLabel.setBounds(12, 8, 200, 25);
        card.add(noLabel);

        JLabel timeLabel = new JLabel(d[1].trim());
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setBounds(12, 31, 200, 18);
        card.add(timeLabel);

        String sweet = d[3].trim().equals("-") ? "" : " | Sweet " + d[3].trim() + "%";
        JLabel menuLabel = new JLabel(d[2].trim() + " | " + d[4].trim() + sweet);
        menuLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        menuLabel.setBounds(12, 55, 330, 22);
        card.add(menuLabel);

        JLabel priceLabel = new JLabel(d[5].trim() + " Baht");
        priceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        priceLabel.setForeground(RoundButton.ORANGE);
        priceLabel.setBounds(12, 80, 120, 25);
        card.add(priceLabel);

        if (ready) {
            JLabel done = new JLabel("Ready \u2713", SwingConstants.RIGHT);
            done.setFont(new Font("Tahoma", Font.BOLD, 16));
            done.setForeground(GREEN);
            done.setBounds(200, 75, 135, 30);
            card.add(done);
        } else {
            RoundButton confirm = new RoundButton("Confirm");
            confirm.setBounds(215, 70, 120, 35);
            confirm.setBackground(RoundButton.ORANGE);
            confirm.setForeground(Color.WHITE);
            confirm.setFont(new Font("Tahoma", Font.BOLD, 15));
            confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            confirm.addActionListener(e -> {
                try {
                    OrderManager.updateStatus(no, OrderManager.STATUS_READY);
                    refresh(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to update order: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            card.add(confirm);
        }

        return card;
    }
}
