package GUI_Menu;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// บันทึกออร์เดอร์ลง Order.csv และออกเลขออร์เดอร์ให้อัตโนมัติ
public class OrderManager {

    private static final File FILE = new File("Order.csv");
    private static final String HEADER = "OrderNo,DateTime,Menu,Sweetness,Temperature,Price,Status";

    // สถานะของออร์เดอร์
    public static final String STATUS_NOT_READY = "Not Ready";
    public static final String STATUS_READY = "Ready";

    // บันทึกออร์เดอร์ แล้วคืนเลขออร์เดอร์ (เริ่มที่ 1)
    // sweetness ส่ง null ถ้าเมนูนั้นไม่มีความหวาน
    public static synchronized int saveOrder(String menu, String sweetness,
                                             String temperature, int price) throws IOException {
        prepareFile();
        int orderNo = nextOrderNo();

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = orderNo + "," + time + "," + menu + ","
           + (sweetness == null ? "-" : sweetness) + "," + temperature + "," + price
           + "," + STATUS_NOT_READY
           + System.lineSeparator();

        Files.write(FILE.toPath(), line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        return orderNo;
    }

    // อ่านออร์เดอร์ทั้งหมดจาก Order.csv (ไม่รวมบรรทัดหัวคอลัมน์)
    // แต่ละรายการ = {OrderNo, DateTime, Menu, Sweetness, Temperature, Price, Status}
    public static synchronized List<String[]> readOrders() throws IOException {
        List<String[]> result = new ArrayList<>();
        if (!FILE.exists()) return result;

        List<String> lines = Files.readAllLines(FILE.toPath(), StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) continue;
            String[] d = lines.get(i).split(",", -1);
            if (d.length < 7) continue;
            result.add(d);
        }
        return result;
    }

    // เปลี่ยนสถานะของออร์เดอร์ตามเลขที่ให้ (เจอและแก้สำเร็จ = true)
    public static synchronized boolean updateStatus(int orderNo, String newStatus) throws IOException {
        if (!FILE.exists()) return false;

        List<String> lines = Files.readAllLines(FILE.toPath(), StandardCharsets.UTF_8);
        boolean changed = false;
        for (int i = 1; i < lines.size(); i++) {
            String[] d = lines.get(i).split(",", -1);
            if (d.length < 7) continue;
            try {
                if (Integer.parseInt(d[0].trim()) == orderNo) {
                    d[6] = newStatus;
                    lines.set(i, String.join(",", d));
                    changed = true;
                    break;
                }
            } catch (NumberFormatException ignored) { }
        }
        if (changed) {
            Files.write(FILE.toPath(), lines, StandardCharsets.UTF_8);
        }
        return changed;
    }

    // สร้างไฟล์ใหม่ถ้ายังไม่มี ถ้าเป็นไฟล์เก่าที่หัวคอลัมน์ไม่ตรง จะสำรองเป็น Order_old.csv ก่อน
    private static void prepareFile() throws IOException {
        if (FILE.exists()) {
            List<String> lines = Files.readAllLines(FILE.toPath(), StandardCharsets.UTF_8);
            if (!lines.isEmpty() && lines.get(0).replace("\uFEFF", "").trim().equals(HEADER)) {
                return;
            }
            if (!lines.isEmpty()) {
                Files.move(FILE.toPath(), new File("Order_old.csv").toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        Files.write(FILE.toPath(), (HEADER + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
    }

    // เลขออร์เดอร์ถัดไป = เลขสูงสุดในไฟล์ + 1
    private static int nextOrderNo() throws IOException {
        int max = 0;
        List<String> lines = Files.readAllLines(FILE.toPath(), StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            try {
                max = Math.max(max, Integer.parseInt(lines.get(i).split(",")[0].trim()));
            } catch (NumberFormatException ignored) { }
        }
        return max + 1;
    }
}
