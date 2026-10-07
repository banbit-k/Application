package GUI_Menu;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// บันทึกออร์เดอร์ลง Order.csv และออกเลขออร์เดอร์ให้อัตโนมัติ
public class OrderManager {

    private static final File FILE = new File("Order.csv");
    private static final String HEADER = "OrderNo,DateTime,Menu,Sweetness,Temperature,Price";

    // บันทึกออร์เดอร์ แล้วคืนเลขออร์เดอร์ (เริ่มที่ 1)
    // sweetness ส่ง null ถ้าเมนูนั้นไม่มีความหวาน
    public static synchronized int saveOrder(String menu, String sweetness,
                                             String temperature, int price) throws IOException {
        prepareFile();
        int orderNo = nextOrderNo();

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = orderNo + "," + time + "," + menu + ","
                + (sweetness == null ? "-" : sweetness) + "," + temperature + "," + price
                + System.lineSeparator();

        Files.write(FILE.toPath(), line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        return orderNo;
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
