import java.awt.*;
import javax.swing.*;

public class GUI {
    public static void main(String[] args) {
        JFrame f = new JFrame("ร้านกาแฟแฟนแก");
        Container cp = f.getContentPane();
        cp.setLayout(null);
        cp.setBackground(new Color(255, 235, 238));

        JLabel SN = new JLabel("ร้านกาแฟแฟนแก", SwingConstants.CENTER);
        SN.setFont(new Font("Tahoma", Font.BOLD, 24));
        SN.setBorder(BorderFactory.createLineBorder(Color.pink, 2));
        SN.setPreferredSize(new Dimension(300, 60));

        SN.setBounds(0, 0, 386, 60);
        cp.add(SN);
        f.setSize(400,600);
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);

    }
}
