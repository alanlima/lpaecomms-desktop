import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * Created by alima on 6/5/17.
 */
public class UserManagementEditFrame extends CustomInternalFrame {
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String USER_STATUS_ENABLED_KEY = "1";
    public static final String USER_STATUS_ENABLED_VALUE = "Enabled";
    public static final String USER_STATUS_DISABLED_KEY = "0";
    public static final String USER_STATUS_DISABLED_VALUE = "Disabled";

    public static final String USER_GROUP_USER_KEY = "user";
    public static final String USER_GROUP_USER_VALUE = "User";
    public static final String USER_GROUP_ADMIN_KEY = "administrator";
    public static final String USER_GROUP_ADMIN_VALUE = "Administrator";

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

    private boolean isEditMode = false;
    private UserEditFrameListener listener;

    public UserManagementEditFrame(){
        setTitle("User Form");
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
        isEditMode = false;
    }

    private boolean isFormValid(){
        StringBuilder sb = new StringBuilder();

        if(txtFirstName.getText().length() == 0){
            sb.append(" - First Name is required.\n");
        }

        if(txtLogin.getText().length() == 0){
            sb.append(" - Login is required.\n");
        }

        if(!isEditMode && txtPassword.getPassword().length == 0){
            sb.append(" - Password is required.\n");
        }

        if(!Arrays.equals(txtPassword.getPassword(), txtPasswordConfirm.getPassword())){
            sb.append(" - The passwords do not match.\n");
        }

        if(sb.length() == 0) return true;
        else {
            showMessage(sb.toString());
            return false;
        }
    }

    private void save(){
        if(!isFormValid()) return;

        String group = null;
        String status = null;

        if(USER_GROUP_ADMIN_VALUE.equals(cbxGroup.getSelectedItem())){
            group = USER_GROUP_ADMIN_KEY;
        } else if (USER_GROUP_USER_VALUE.equals(cbxGroup.getSelectedItem())){
            group = USER_GROUP_USER_KEY;
        }

        if(USER_STATUS_ENABLED_VALUE.equals(cbxStatus.getSelectedItem())){
            status = USER_STATUS_ENABLED_KEY;
        } else if (USER_STATUS_DISABLED_VALUE.equals(cbxStatus.getSelectedItem())) {
            status = USER_STATUS_DISABLED_KEY;
        }

        if(isEditMode) { // UPDATE USER
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE lpa_users SET ");

            sb.append(" lpa_user_username = '")
                    .append(txtLogin.getText())
                    .append("'");

            sb.append(", lpa_user_firstname = '")
                    .append(txtFirstName.getText())
                    .append("'");

            sb.append(", lpa_user_lastname = '")
                    .append(txtLastName.getText())
                    .append("'");

            if(txtPassword.getPassword().length > 0){
                sb.append(", lpa_user_password = '")
                        .append(String.valueOf(txtPassword.getPassword()))
                        .append("'");
            }

            sb.append(", lpa_user_group = '")
                    .append(group).append("'");

            sb.append(", lpa_user_status = '")
                    .append(status).append("'");

            sb.append(" WHERE lpa_user_ID = '").append(txtId.getText()).append("'");

            try {
                connManager.executeUpdate(sb.toString());
                showMessage("User updated.");
                if(listener != null) listener.onSaved();
                setVisible(false);
            } catch (Exception e) {
                showMessage("Error on update the user.");
                e.printStackTrace();
            }

        } else { // NEW USER
            String sql = "INSERT INTO lpa_users (lpa_user_username, lpa_user_password, " +
                    "lpa_user_firstname, lpa_user_lastname, lpa_user_group, lpa_user_status)" +
                    " VALUES (" +
                    " '" + txtLogin.getText() + "'" +
                    ",'" + String.valueOf(txtPassword.getPassword()) + "'" +
                    ",'" + txtFirstName.getText() + "'" +
                    ",'" + txtLastName.getText() + "'" +
                    ",'" + group + "'" +
                    ",'" + status + "')";

            try {
                connManager.executeUpdate(sql);
                showMessage("User created.");
                if(listener != null) listener.onSaved();
                setVisible(false);
            } catch (Exception e) {
                showMessage("Error on create the user.");
                e.printStackTrace();
            }
        }
    }

    private void loadUser(String id){
        try{
            ResultSet rs = connManager.executeQuery("SELECT * FROM lpa_users WHERE " +
                    "lpa_user_ID = '" + id + "' LIMIT 1");
            if(rs.next()){
                txtId.setText(rs.getString("lpa_user_ID"));
                txtFirstName.setText(rs.getString("lpa_user_firstname"));
                txtLastName.setText(rs.getString("lpa_user_lastname"));
                txtLogin.setText(rs.getString("lpa_user_username"));

                String group = rs.getString("lpa_user_group");
                String status = rs.getString("lpa_user_status");

                if(USER_GROUP_USER_KEY.equals(group)){
                    cbxGroup.setSelectedItem(USER_GROUP_USER_VALUE);
                } else if (USER_GROUP_ADMIN_KEY.equals(group)) {
                    cbxGroup.setSelectedItem(USER_GROUP_ADMIN_VALUE);
                }

                if(USER_STATUS_ENABLED_KEY.equals(status)){
                    cbxStatus.setSelectedItem(USER_STATUS_ENABLED_VALUE);
                } else if (USER_STATUS_DISABLED_KEY.equals(status)) {
                    cbxStatus.setSelectedItem(USER_STATUS_DISABLED_VALUE);
                }

                isEditMode = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        cbxGroup = new JComboBox<>(new String[] {USER_GROUP_ADMIN_VALUE, USER_GROUP_USER_VALUE});

        addComponent(cbxGroup, 4, 1);

        cbxStatus = new JComboBox<>(new String[] {USER_STATUS_ENABLED_VALUE, USER_STATUS_DISABLED_VALUE});

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

    public void addUserEditFrameListener(UserEditFrameListener listener){
        this.listener = listener;
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e){
        reset();
        String userId = (String) getExtra(EXTRA_USER_ID);
        if(userId != null){
            loadUser(userId);
        }
    }

    public interface UserEditFrameListener{
        void onSaved();
    }
}
