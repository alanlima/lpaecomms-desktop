import javax.swing.*;
import java.awt.*;

/**
 * Created by alima on 6/5/17.
 */
public class UserManagementEditFrame extends JInternalFrame {
    private GridBagConstraints layoutConstraints;
    private GridBagLayout layout;
    private ConnectionManager connManager = ConnectionManager.instance();

    private JTextField txtId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordConfirm;
    private JComboBox<String> cbxGroup;
    private JComboBox<String> cbxStatus;

    public UserManagementEditFrame(){
        super("User Form");
        this.layout = new GridBagLayout();
        this.layoutConstraints = new GridBagConstraints();
        this.layoutConstraints.insets = new Insets(10, 15, 6, 15);

        setLayout(this.layout);
        setClosable(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBackground(new Color(35, 44, 49));

        initComponents();
    }

    private void reset(){
        txtId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLogin.setText("");
        txtPassword.setText("");
        txtPasswordConfirm.setText("");
        cbxGroup.setSelectedIndex(0);
        cbxStatus.setSelectedIndex(0);
    }

    private void save(){

    }

    private void initComponents(){
        layoutConstraints.fill = GridBagConstraints.BOTH;

        // Column 0 :: Begin
        layoutConstraints.weightx = 40;

        // Id:
        JLabel label = new JLabel("Id:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 0, 0);

        // First Name:
        label = new JLabel("First Name:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 1, 0);

        // Login
        label = new JLabel("Login:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 2, 0);

        // Password
        label = new JLabel("Password:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 3, 0);

        // Group
        label = new JLabel("Group:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 4, 0);

        // Status
        label = new JLabel("Status:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 5, 0);

        // Column 0 :: End

        // Column 1 :: Begin
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        layoutConstraints.weightx = 100;

        txtId = new JTextField(15);
        txtId.setForeground(Color.WHITE);
        txtId.setBackground(Color.DARK_GRAY);
        txtId.setEditable(false);

        addComponent(txtId, 0, 1);

        txtFirstName = new JTextField(30);
        txtFirstName.setForeground(Color.WHITE);
        txtFirstName.setBackground(Color.DARK_GRAY);

        addComponent(txtFirstName, 1, 1);

        txtLogin = new JTextField(30);
        txtLogin.setForeground(Color.WHITE);
        txtLogin.setBackground(Color.DARK_GRAY);

        addComponent(txtLogin, 2, 1);

        txtPassword = new JPasswordField(30);
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBackground(Color.DARK_GRAY);

        addComponent(txtPassword, 3, 1);

        cbxGroup = new JComboBox<>(new String[] {"Administrator", "User"});
        cbxGroup.setForeground(Color.WHITE);
        cbxGroup.setBackground(Color.DARK_GRAY);

        addComponent(cbxGroup, 4, 1);

        cbxStatus = new JComboBox<>(new String[] {"Active", "Inactive"});
        cbxStatus.setForeground(Color.WHITE);
        cbxStatus.setBackground(Color.DARK_GRAY);

        addComponent(cbxStatus, 5, 1);

        // Column 1 :: End

        // Column 2 :: Begin
        layoutConstraints.weightx = 20;

        label = new JLabel("Last Name:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 1, 2);

        label = new JLabel("Confirm:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(102, 147, 182));
        addComponent(label, 3, 2);

        // Column 2 :: End

        // Column 3 :: Begin
        layoutConstraints.weightx = 100;

        txtLastName = new JTextField(30);
        txtLastName.setForeground(Color.WHITE);
        txtLastName.setBackground(Color.DARK_GRAY);

        addComponent(txtLastName, 1, 3);

        txtPasswordConfirm = new JPasswordField(30);
        txtPasswordConfirm.setForeground(Color.WHITE);
        txtPasswordConfirm.setBackground(Color.DARK_GRAY);

        addComponent(txtPasswordConfirm, 3, 3);

        // Column 3 :: End

        layoutConstraints.fill = GridBagConstraints.NONE;
        //layoutConstraints.anchor = GridBagConstraints.EAST;
        layoutConstraints.weightx = 20;
//        layoutConstraints.weighty = 1;

        JButton button = new JButton("Save");
        button.addActionListener(e -> save());

        //layoutConstraints.gridwidth = GridBagConstraints.RELATIVE;
        addComponent(button, 6, 3);

        button = new JButton("Cancel");
        button.addActionListener(e -> UserManagementEditFrame.this.setVisible(false));

        //layoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
        addComponent(button, 6, 2);
    }

    private void addComponent(Component component,
                              int row, int column) {
        addComponent(component, row, column, 1, 1);
    }

    private void addComponent(Component component,
                              int row, int column, int width, int height) {
        layoutConstraints.gridx = column; // set gridx
        layoutConstraints.gridy = row; // set gridy
        layoutConstraints.gridwidth = width; // set gridwidth
        layoutConstraints.gridheight = height; // set gridheight
        layout.setConstraints(component, layoutConstraints); // set constraints
        add(component); // add component
    }
}
