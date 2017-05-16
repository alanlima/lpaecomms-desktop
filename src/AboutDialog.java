import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by alima on 11/5/17.
 */
public class AboutDialog extends JDialog {
    public AboutDialog() {
        setTitle("About");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel name = new JLabel("<html><div style=\"text-align:center\"><h3>LPA - eComms - Desktop Application</h3><br />" +
                "Version 1.0<br />" +
                "Developed by Alan Lima</div> <br />" +
                "</html>");
        name.setAlignmentX(0.5f);
        add(name);

        add(Box.createRigidArea(new Dimension(0, 100)));

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());

        close.setAlignmentX(0.5f);
        add(close);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
    }
}