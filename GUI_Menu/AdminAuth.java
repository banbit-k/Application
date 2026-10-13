package GUI_Menu;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

// เก็บและตรวจรหัสผ่าน Admin จากไฟล์ Admin.csv
//
// รูปแบบไฟล์ (ไฟล์ข้อความธรรมดา):
//   Password
//   1234
// บรรทัดแรกคือหัวคอลัมน์ ตั้งแต่บรรทัดที่สองคือรหัสผ่าน (ใส่ได้หลายรหัส บรรทัดละ 1 รหัส)
public class AdminAuth {

    private static final File FILE = new File("Admin.csv");
    private static final String HEADER = "Password";
    private static final String DEFAULT_PASSWORD = "1234"; // ใช้ตอนสร้างไฟล์ใหม่ (แก้ในไฟล์ได้)

    // ตรวจรหัสผ่านที่กรอก กับรหัสในไฟล์ Admin.csv
    public static synchronized boolean checkPassword(String input) {
        if (input == null || input.isEmpty()) return false;
        try {
            prepareFile();
            for (String p : readPasswords()) {
                if (p.equals(input)) return true;
            }
        } catch (IOException e) {
            // อ่านไฟล์ไม่ได้ ถือว่ารหัสไม่ผ่าน
        }
        return false;
    }

    // อ่านไฟล์เป็นข้อความ (ตัด BOM ออก)
    private static String readText() throws IOException {
        return new String(Files.readAllBytes(FILE.toPath()), StandardCharsets.UTF_8).replace("\uFEFF", "");
    }

    // รหัสทั้งหมดในไฟล์ (ตั้งแต่บรรทัดที่ 2 ที่ไม่ว่าง)
    private static List<String> readPasswords() throws IOException {
        List<String> result = new ArrayList<>();
        String[] lines = readText().split("\\r?\\n");
        for (int i = 1; i < lines.length; i++) {
            String p = lines[i].trim();
            if (!p.isEmpty()) result.add(p);
        }
        return result;
    }

    // ไฟล์ใช้ได้ = บรรทัดแรกเป็นหัวคอลัมน์ "Password"
    private static boolean isValidFile() throws IOException {
        String first = readText().split("\\r?\\n", 2)[0].trim();
        return first.equalsIgnoreCase(HEADER);
    }

    // สร้างไฟล์ใหม่ถ้ายังไม่มี
    // ถ้ามีแต่รูปแบบไม่ถูก (เช่น เป็นไฟล์ Excel ที่แค่เปลี่ยนนามสกุลเป็น .csv) จะสำรองเป็น Admin_old.csv ก่อน
    private static void prepareFile() throws IOException {
        if (FILE.exists()) {
            if (isValidFile()) {
                if (readPasswords().isEmpty()) {
                    // มีแต่หัวคอลัมน์ ไม่มีรหัส -> เติมรหัสเริ่มต้นให้
                    Files.write(FILE.toPath(),
                            (DEFAULT_PASSWORD + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                            java.nio.file.StandardOpenOption.APPEND);
                }
                return;
            }
            Files.move(FILE.toPath(), new File("Admin_old.csv").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        String content = HEADER + System.lineSeparator() + DEFAULT_PASSWORD + System.lineSeparator();
        Files.write(FILE.toPath(), content.getBytes(StandardCharsets.UTF_8));
    }
}