import javax.swing.*;
import java.awt.*;

/**
 * Created by alima on 15/5/17.
 */
public class UserGuideDialog extends JDialog {


    public UserGuideDialog(){
        setTitle("User Guide");
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(920, 800);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

//        add(contentPane);
//
//        JLabel title = new JLabel("User Guide");
//
//        contentPane.add(title);
//
//        JLabel login = new JLabel("Login");
//
//        JLabel loginIcon = new JLabel();
//        loginIcon.setIcon(new ImageIcon(loginUrl));
//
//        contentPane.add(login);
//        contentPane.add(loginIcon);

        JLabel help1 = new JLabel();
        help1.setIcon(new ImageIcon(getClass().getResource("Help Guide-1.png")));

        JLabel help2 = new JLabel();
        help2.setIcon(new ImageIcon(getClass().getResource("Help Guide-2.png")));

        JLabel help3 = new JLabel();
        help3.setIcon(new ImageIcon(getClass().getResource("Help Guide-3.png")));

//        JLabel help4 = new JLabel();
//        help4.setIcon(new ImageIcon("images/Help Guide-4.png"));

        contentPane.add(help1);
        contentPane.add(help2);
        contentPane.add(help3);
//        contentPane.add(help4);

        JScrollPane sp = new JScrollPane(contentPane);

        add(sp);
    }
}
