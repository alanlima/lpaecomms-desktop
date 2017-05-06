import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

/**
 * Created by alima on 5/5/17.
 */
public class UserManagementFrame extends JInternalFrame {
    private GridBagConstraints layoutConstraints;
    private GridBagLayout layout;
    private ConnectionManager connManager = ConnectionManager.instance();
    private UserManagementEditFrame userEditFrame;


    /**
     * Components Variables
     */
    JTextField txtSearch;
    JTable tblUsers;

    public UserManagementFrame() {
        super("User Management");
        this.layout = new GridBagLayout();
        this.layoutConstraints = new GridBagConstraints();
        this.layoutConstraints.insets = new Insets(10, 15, 6, 15);
        this.userEditFrame = new UserManagementEditFrame();
        this.userEditFrame.setBounds(600, 170, 700, 350);

        setLayout(this.layout);
        setClosable(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBackground(new Color(35, 44, 49));

        initComponents();

        search();

        main.contentPane.add(this.userEditFrame);
    }

    private void search() {
        ResultSet rs = null;
        try{
            String searchText = txtSearch.getText();
            rs = connManager.executeQuery("SELECT * FROM lpa_users WHERE " +
                    "lpa_user_username LIKE '%" + searchText + "%' OR " +
                    "lpa_user_firstname LIKE '%" + searchText + "%' OR " +
                    "lpa_user_lastname LIKE '%" + searchText + "%'");

            if(rs.next()){
                DefaultTableModel model = (DefaultTableModel)tblUsers.getModel();
                model.getDataVector().removeAllElements();
                model.fireTableDataChanged();
                rs.beforeFirst();
                while (rs.next()){
                    Object[] row = {
                            rs.getString("lpa_user_ID"),
                            rs.getString("lpa_user_firstname") + " " + rs.getString("lpa_user_lastname"),
                            rs.getString("lpa_user_username"),
                            rs.getString("lpa_user_group"),
                            "1".equals(rs.getString("lpa_user_status")) ? "Active" : "Inactive"
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void newUser() {
    }

    private void edit(String userId){
        this.userEditFrame.setVisible(true);
    }

    private void initComponents() {
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
        lblSearch.setForeground(new Color(102, 147, 182));
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        addComponent(lblSearch, 0, 0);

        txtSearch = new JTextField();
        txtSearch.addActionListener(e -> search());
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setBackground(Color.DARK_GRAY);
        txtSearch.setColumns(30);

        layoutConstraints.weightx = 800;
        addComponent(txtSearch, 0, 1);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> search());
        layoutConstraints.weightx = 1;
        addComponent(btnSearch, 0, 2);

        String[] columnNames =
        {
            "Id",
            "Name",
            "Login",
            "Group",
            "Status"
        };

        tblUsers = new JTable();
        tblUsers.setForeground(Color.WHITE);
        tblUsers.setBackground(Color.DARK_GRAY);
        tblUsers.setModel(new DefaultTableModel(null, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 1) {
                    final JTable target = (JTable) e.getSource();
                    final int row = target.getSelectedRow();

                    String userId = target.getValueAt(row, 0).toString();
                    edit(userId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblUsers);
        tblUsers.setFillsViewportHeight(true);
        layoutConstraints.weightx = 1000;
        layoutConstraints.weighty = 1000;
        addComponent(scrollPane,1,0,3,1);

        JButton btnNewUser = new JButton("New");
        btnNewUser.addActionListener(e -> newUser());
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 0;
        layoutConstraints.anchor = GridBagConstraints.EAST;
        addComponent(btnNewUser, 2, 2);
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
