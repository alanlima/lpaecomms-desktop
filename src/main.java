import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;


@SuppressWarnings("all")
public class main extends JFrame {

    /**
     * Constants variables
     */
    private static final Color buttonForegroundColour = new Color(0x61B3CF);

    private static final ConnectionManager connManager = ConnectionManager.instance();

    private static final String USER_STATUS_DISABLED = "0";
    private static final String USER_GROUP_ADMIN = "administrator";

    /**
     * Declaring global variables
     */
    private String displayName = "";
    public static JDesktopPane contentPane;
    private ImageIcon mainIcon, stockIcon, salesIcon, invoiceIcon, clientIcon,
            adminIcon, exitIcon, usersIcon, helpIcon, aboutIcon, secIcon,
            keysIcon, loginBGIcon, searchIcon;
    private JMenu lpa_mnSystemAdmin;
    private JSeparator mnMenuSep_1, mnMenuSep_2;
    private Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
    private JMenuBar lpa_menuBar;
    private JInternalFrame ifLogin, ifSearchStock, ifStock, ifSales, ifUserManagement;
    private JLayeredPane layeredPaneBG, layeredPaneFG;
    private JScrollPane searchScrollPaneStock;
    private JTable tblSearchStock;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblDisplayName;
    private JTextField txtStockSearch, txtStockID, txtStockName, txtStockDes,
            txtStockOnHand, txtStockPrice;
    private ButtonGroup bgpStockStatus;
    private String saveMode;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    main frame = new main();
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    private main() {
        setTitle("LPA - Administration System v1.0");
        connManager.connect();
        mainIcon = new ImageIcon("ext-lib/LPALogo.png");
        setIconImage(mainIcon.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 971, 614);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        lpa_menuBar = new JMenuBar();
        setJMenuBar(lpa_menuBar);

        JMenu lpa_mnMenu = new JMenu("Menu");
        lpa_menuBar.add(lpa_mnMenu);

        JMenuItem lpa_mntmStockControl = new JMenuItem("Stock Management");
        stockIcon = new ImageIcon("ext-lib/stockIcon.png");
        lpa_mntmStockControl.setIcon(stockIcon);
        lpa_mntmStockControl.addActionListener(arg0 -> {
            //centerJIF(ifSearchStock,"app");
            ifSearchStock.setVisible(true);
        });
        lpa_mnMenu.add(lpa_mntmStockControl);

        JMenu lpa_mnSalesInvoicing = new JMenu("Sales and Invoicing");
        salesIcon = new ImageIcon("ext-lib/salesIcon.png");
        lpa_mnSalesInvoicing.setIcon(salesIcon);
        lpa_mnMenu.add(lpa_mnSalesInvoicing);

        JMenuItem lpa_mntmInvoices = new JMenuItem("Invoices");
        lpa_mntmInvoices.addActionListener(arg0 -> ifSales.setVisible(true));
        invoiceIcon = new ImageIcon("ext-lib/invoiceIcon.png");
        lpa_mntmInvoices.setIcon(invoiceIcon);
        lpa_mnSalesInvoicing.add(lpa_mntmInvoices);

        JMenuItem lpa_mntmClients = new JMenuItem("Clients");
        clientIcon = new ImageIcon("ext-lib/clientIcon.png");
        lpa_mntmClients.setIcon(clientIcon);
        lpa_mnSalesInvoicing.add(lpa_mntmClients);

        mnMenuSep_1 = new JSeparator();
        lpa_mnMenu.add(mnMenuSep_1);

        lpa_mnSystemAdmin = new JMenu("System Administration");
        adminIcon = new ImageIcon("ext-lib/adminIcon.png");
        lpa_mnSystemAdmin.setIcon(adminIcon);
        lpa_mnMenu.add(lpa_mnSystemAdmin);

        JMenuItem lpa_mntmUserMan = new JMenuItem("User Management");
        lpa_mntmUserMan.addActionListener(e -> ifUserManagement.setVisible(true));
        usersIcon = new ImageIcon("ext-lib/usersIcon.png");
        lpa_mntmUserMan.setIcon(usersIcon);
        lpa_mnSystemAdmin.add(lpa_mntmUserMan);

        mnMenuSep_2 = new JSeparator();
        lpa_mnMenu.add(mnMenuSep_2);

        JMenu mnExit = new JMenu("Exit");
        exitIcon = new ImageIcon("ext-lib/exitIcon.png");
        mnExit.setIcon(exitIcon);
        lpa_mnMenu.add(mnExit);

        JMenuItem mntmLogout = new JMenuItem("Logout");
        mntmLogout.addActionListener(e -> do_logout());
        ImageIcon logoutIcon = new ImageIcon("ext-lib/logoutIcon.png");
        mntmLogout.setIcon(logoutIcon);

        mnExit.add(mntmLogout);

        JSeparator separator = new JSeparator();
        mnExit.add(separator);

        JMenuItem mntmShutdown = new JMenuItem("Shutdown");
        mntmShutdown.addActionListener(e -> System.exit(0));
        ImageIcon shutdownIcon = new ImageIcon("ext-lib/shutdownIcon.png");
        mntmShutdown.setIcon(shutdownIcon);
        mnExit.add(mntmShutdown);


        JMenu lpa_mnHelp = new JMenu("Help");
        lpa_menuBar.add(lpa_mnHelp);

        JMenuItem mntmHelpGuide = new JMenuItem("Help Guide");
        helpIcon = new ImageIcon("ext-lib/helpIcon.png");
        mntmHelpGuide.setIcon(helpIcon);
        lpa_mnHelp.add(mntmHelpGuide);
        mntmHelpGuide.addActionListener(e -> {
            UserGuideDialog dialog = new UserGuideDialog();
            dialog.setVisible(true);
        });

        JMenuItem mntmAboutLpa = new JMenuItem("About LPA");
        aboutIcon = new ImageIcon("ext-lib/aboutIcon.png");
        mntmAboutLpa.setIcon(aboutIcon);
        lpa_mnHelp.add(mntmAboutLpa);
        mntmAboutLpa.addActionListener(e -> {
            AboutDialog dialog = new AboutDialog();
            dialog.setVisible(true);
        });

        contentPane = new JDesktopPane() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(0, 0, 40);
                Color color2 = new Color(80, 80, 100);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        ifLogin = new JInternalFrame("LPA - LOGIN");
        ifLogin.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ifLogin.setVisible(false);
        ifLogin.getContentPane().setBackground(new Color(204, 204, 255));
        ifLogin.setBounds(117, 44, 390, 211);
        keysIcon = new ImageIcon("ext-lib/iconKey.png");
        ifLogin.setFrameIcon(keysIcon);
        centerJIF(ifLogin, "screen");
        contentPane.add(ifLogin);
        ifLogin.getContentPane().setLayout(null);

        layeredPaneBG = new JLayeredPane();
        layeredPaneBG.setBounds(0, 0, 388, 178);
        secIcon = new ImageIcon("ext-lib/securityIcon.png");
        loginBGIcon = new ImageIcon("ext-lib/lpaUserLoginBG.png");
        ifLogin.getContentPane().add(layeredPaneBG);
        layeredPaneFG = new JLayeredPane();
        layeredPaneFG.setBounds(0, 0, 388, 178);
        layeredPaneBG.add(layeredPaneFG);

        JLabel lblUserName = new JLabel("User name:");
        lblUserName.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblUserName.setBounds(10, 100, 81, 14);
        layeredPaneFG.add(lblUserName);

        txtUsername = new JTextField();
        txtUsername.addActionListener(e -> do_login());
        txtUsername.setBounds(84, 97, 288, 20);
        layeredPaneFG.add(txtUsername);
        txtUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPassword.setBounds(10, 125, 64, 14);
        layeredPaneFG.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.addActionListener(e -> do_login());
        txtPassword.setBounds(85, 122, 287, 20);
        layeredPaneFG.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> do_login());
        btnLogin.setBounds(283, 148, 89, 23);
        layeredPaneFG.add(btnLogin);
        JLabel loginBGLabel = new JLabel();
        loginBGLabel.setVerticalAlignment(SwingConstants.TOP);
        loginBGLabel.setBounds(0, 0, 378, 89);
        layeredPaneFG.add(loginBGLabel);
        loginBGLabel.setIcon(loginBGIcon);
        searchIcon = new ImageIcon("ext-lib/searchIcon.png");

        String[] columnNames = {
                "Stock ID",
                "Stock Name",
                "Description",
                "On-Hand",
                "Price"
        };

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		/*
        tblSearchStock.getTableHeader().getColumnModel().getColumn(2).setHeaderRenderer(centerRenderer);
		tblSearchStock.getTableHeader().getColumnModel().getColumn(3).setHeaderRenderer(rightRenderer);
		*/


        ifStock = new JInternalFrame("LPA - Stock Record");
        ifStock.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ifStock.setClosable(true);
        ifStock.setBackground(new Color(35, 44, 49));
        ifStock.setBounds(288, 438, 618, 244);
        contentPane.add(ifStock);
        ifStock.getContentPane().setLayout(null);

        JLabel lblStockId = new JLabel("Stock ID:");
        lblStockId.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStockId.setForeground(Color.WHITE);
        lblStockId.setBounds(10, 11, 80, 14);
        ifStock.getContentPane().add(lblStockId);

        txtStockID = new JTextField();
        txtStockID.setBackground(Color.DARK_GRAY);
        txtStockID.setForeground(Color.WHITE);
        txtStockID.setBounds(100, 9, 203, 20);
        ifStock.getContentPane().add(txtStockID);
        txtStockID.setColumns(10);
//        txtStockID.setEditable(false);

        JLabel lblStockName = new JLabel("Stock Name:");
        lblStockName.setForeground(Color.WHITE);
        lblStockName.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStockName.setBounds(10, 42, 80, 14);
        ifStock.getContentPane().add(lblStockName);

        txtStockName = new JTextField();
        txtStockName.setForeground(Color.WHITE);
        txtStockName.setColumns(10);
        txtStockName.setBackground(Color.DARK_GRAY);
        txtStockName.setBounds(100, 40, 492, 20);
        ifStock.getContentPane().add(txtStockName);

        txtStockDes = new JTextField();
        txtStockDes.setForeground(Color.WHITE);
        txtStockDes.setColumns(10);
        txtStockDes.setBackground(Color.DARK_GRAY);
        txtStockDes.setBounds(100, 71, 492, 20);
        ifStock.getContentPane().add(txtStockDes);

        JLabel lblStockDes = new JLabel("Stock Desc.:");
        lblStockDes.setForeground(Color.WHITE);
        lblStockDes.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStockDes.setBounds(10, 73, 80, 14);
        ifStock.getContentPane().add(lblStockDes);

        txtStockOnHand = new JTextField();
        txtStockOnHand.setForeground(Color.WHITE);
        txtStockOnHand.setColumns(10);
        txtStockOnHand.setBackground(Color.DARK_GRAY);
        txtStockOnHand.setBounds(100, 102, 80, 20);
        ifStock.getContentPane().add(txtStockOnHand);

        JLabel lblStockOnHand = new JLabel("On-Hand:");
        lblStockOnHand.setForeground(Color.WHITE);
        lblStockOnHand.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStockOnHand.setBounds(10, 104, 80, 14);
        ifStock.getContentPane().add(lblStockOnHand);

        txtStockPrice = new JTextField();
        txtStockPrice.setForeground(Color.WHITE);
        txtStockPrice.setColumns(10);
        txtStockPrice.setBackground(Color.DARK_GRAY);
        txtStockPrice.setBounds(100, 133, 80, 20);
        ifStock.getContentPane().add(txtStockPrice);

        JLabel lblStockPrice = new JLabel("Price:");
        lblStockPrice.setForeground(Color.WHITE);
        lblStockPrice.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStockPrice.setBounds(10, 135, 80, 14);
        ifStock.getContentPane().add(lblStockPrice);

        JLabel lblStockStatus = new JLabel("Status:");
        lblStockStatus.setForeground(Color.WHITE);
        lblStockStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStockStatus.setBounds(10, 155, 80, 14);
        ifStock.getContentPane().add(lblStockStatus);

        bgpStockStatus = FormControls.addStockStatusGroup(ifStock.getContentPane());

        JButton btnStockSave = new JButton("Save");
        btnStockSave.addActionListener(arg0 -> saveStockData(txtStockID.getText()));
        btnStockSave.setBounds(503, 181, 89, 23);
        ifStock.getContentPane().add(btnStockSave);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 170, 582, 2);
        ifStock.getContentPane().add(separator_1);
        ifStock.setVisible(false);

        //ifSales = new JInternalFrame("Sales");
        ifSales = new InvoicesFrame();
        //ifSales.setClosable(true);
        //ifSales.setBounds(47, 354, 295, 148);
        contentPane.add(ifSales);

        ifUserManagement = new UserManagementFrame();
        ifUserManagement.setBounds(586, 198, 725, 512);
        contentPane.add(ifUserManagement);

        lblDisplayName = new JLabel(displayName);
        lblDisplayName.setBounds(5, 5, 500, 14);
        lblDisplayName.setVerticalAlignment(SwingConstants.TOP);
        lblDisplayName.setHorizontalAlignment(SwingConstants.LEFT);
        lblDisplayName.setForeground(Color.WHITE);
        contentPane.add(lblDisplayName);

        ifSearchStock = new JInternalFrame("LPA - Search");
        ifSearchStock.setBounds(586, 198, 725, 312);
        contentPane.add(ifSearchStock);
        ifSearchStock.setFrameIcon(searchIcon);
        ifSearchStock.setClosable(true);
        ifSearchStock.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ifSearchStock.setBackground(new Color(35, 44, 49));
        ifSearchStock.getContentPane().setLayout(null);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
        lblSearch.setForeground(new Color(102, 147, 182));
        lblSearch.setBounds(10, 14, 46, 14);
        ifSearchStock.getContentPane().add(lblSearch);

        txtStockSearch = new JTextField();
        txtStockSearch.addActionListener(arg0 -> searchStockData(txtStockSearch.getText()));
        txtStockSearch.setForeground(Color.WHITE);
        txtStockSearch.setBackground(Color.DARK_GRAY);
        txtStockSearch.setBounds(66, 11, 534, 20);
        ifSearchStock.getContentPane().add(txtStockSearch);
        txtStockSearch.setColumns(10);
        tblSearchStock = new JTable();
        tblSearchStock.setForeground(Color.WHITE);
        tblSearchStock.setBackground(Color.DARK_GRAY);
        tblSearchStock.setModel(new DefaultTableModel(null, columnNames) {
                                    @Override
                                    public boolean isCellEditable(int row, int column) {
				       /* Set all cells to NON Editable
				        *   - change return value to "true" for Editable
				        * */
                                        return false;
                                    }
                                }
        );
        tblSearchStock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 1) {
                    final JTable target = (JTable) e.getSource();
                    final int row = target.getSelectedRow();

                    String val = target.getValueAt(row, 0).toString();
                    saveMode = "update";
                    getStockData(val);
                }
            }
        });
        tblSearchStock.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblSearchStock.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblSearchStock.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblSearchStock.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblSearchStock.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblSearchStock.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        searchScrollPaneStock = new JScrollPane(tblSearchStock);
        searchScrollPaneStock.setBounds(10, 47, 689, 191);
        tblSearchStock.setFillsViewportHeight(true);
        ifSearchStock.getContentPane().add(searchScrollPaneStock);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(arg0 -> ifSearchStock.setVisible(false));
        btnClose.setBounds(610, 249, 89, 23);
        ifSearchStock.getContentPane().add(btnClose);

        JButton btnNewStock = new JButton("New");
        btnNewStock.addActionListener(arg0 -> {
            txtStockID.setText("");
            txtStockName.setText("");
            txtStockDes.setText("");
            txtStockOnHand.setText("0");
            txtStockPrice.setText("0");
            saveMode = "new";
            centerJIF(ifStock, "app");
        });
        btnNewStock.setBounds(511, 249, 89, 23);

        JButton btnSearchStock = new JButton("Search");
        btnSearchStock.addActionListener(arg0 -> searchStockData(txtStockSearch.getText()));
        tblSearchStock.getTableHeader().setForeground(Color.WHITE);
        tblSearchStock.getTableHeader().setBackground(Color.DARK_GRAY);

        tblSearchStock.getTableHeader().setReorderingAllowed(false);
        btnSearchStock.setBounds(610, 11, 89, 23);
        ifSearchStock.getContentPane().add(btnSearchStock);

        ifSearchStock.getContentPane().add(btnNewStock);
        ifSearchStock.setVisible(false);

        ifSales.setVisible(false);
        lpa_menuBar.setVisible(false);
        ifLogin.setVisible(true);

    }

    private void centerJIF(JInternalFrame jif, String parent) {

        Dimension parentFrame, jInternalFrameSize;
        if (parent.equals("app")) {
            parentFrame = contentPane.getSize();
        } else {
            parentFrame = screenDims;
        }
        jInternalFrameSize = jif.getSize();
        int width = (parentFrame.width - jInternalFrameSize.width) / 2;
        int height = (parentFrame.height - jInternalFrameSize.height) / 2;
        jif.setLocation(width, height);
        jif.setVisible(true);
    }

    private void MSG_POPUP(String MSG) {
        JOptionPane.showMessageDialog(contentPane, MSG);
    }


    private void do_login() {
        try {
            ResultSet rs = connManager.executeQuery(
                    "SELECT * FROM lpa_users WHERE " +
                            "lpa_user_username = '" + txtUsername.getText() + "' AND " +
                            "lpa_user_password = '" + new String(txtPassword.getPassword()) +
                            "' LIMIT 1;"
            );
            if (rs.next()) {
                String group = rs.getString("lpa_user_group");
                String status = rs.getString("lpa_user_status");

                if(USER_STATUS_DISABLED.equals(status)){
                    MSG_POPUP("User disabled.");
                    return;
                }

                if(!USER_GROUP_ADMIN.equals(group)){
                    MSG_POPUP("Access unauthorized.");
                    return;
                }


                lpa_menuBar.setVisible(true);
                txtUsername.setText("");
                txtPassword.setText("");
                txtUsername.requestFocus();
                ifLogin.setVisible(false);
                lblDisplayName.setText(
                        "Welcome " +
                                rs.getString("lpa_user_firstname") + " " +
                                rs.getString("lpa_user_lastname")
                );
            } else {
                MSG_POPUP("Login Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void do_logout() {
        lpa_menuBar.setVisible(false);
        txtUsername.setText("");
        txtPassword.setText("");
        lblDisplayName.setText("");
        centerJIF(ifLogin, "app");
        txtUsername.requestFocus();
    }

    private void searchStockData(final String searchData) {
        try {
            ResultSet rs = connManager.executeQuery(
                    "SELECT * FROM lpa_stock WHERE " +
                            "lpa_stock_ID LIKE '%" + searchData + "%' OR " +
                            "lpa_stock_name LIKE '%" + searchData + "%';"
            );
            if (rs.next()) {
                DefaultTableModel stockModel = (DefaultTableModel) tblSearchStock.getModel();
                stockModel.getDataVector().removeAllElements();
                stockModel.fireTableDataChanged();
                rs.beforeFirst();
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("lpa_stock_ID"),
                            rs.getString("lpa_stock_name"),
                            rs.getString("lpa_stock_desc"),
                            rs.getString("lpa_stock_onhand"),
                            rs.getString("lpa_stock_price")
                    };
                    stockModel.addRow(row);
                }
            } else {
                MSG_POPUP("No records found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStockData(final String StockID) {
        try {
            ResultSet rs = connManager.executeQuery(
                    "SELECT * FROM lpa_stock WHERE " +
                            "lpa_stock_ID = '" + StockID + "' LIMIT 1;"
            );
            if (rs.next()) {
                txtStockID.setText(rs.getString("lpa_stock_ID"));
                txtStockName.setText(rs.getString("lpa_stock_name"));
                txtStockDes.setText(rs.getString("lpa_stock_desc"));
                txtStockOnHand.setText(rs.getString("lpa_stock_onhand"));
                txtStockPrice.setText(rs.getString("lpa_stock_price"));

                FormControls.setButtonGroup(rs.getString("lpa_stock_status"), bgpStockStatus.getElements());

                Dimension ifS = ifSearchStock.getSize();
                Point IFSS = ifSearchStock.getLocation();
                int ifsX = (int) IFSS.getX();
                int ifsY = (int) (IFSS.getY() + ifS.height) + 1;
                ifStock.setLocation(ifsX, ifsY);
                ifStock.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveStockData(final String StockID) {
        try {
            if (Objects.equals(saveMode, "new")) {
                connManager.executeUpdate(
                        "INSERT INTO lpa_stock " +
                                "(lpa_stock_ID,lpa_stock_name,lpa_stock_desc, lpa_stock_onhand, lpa_stock_price, lpa_stock_status) " +
                                "VALUES (" +
                                "'" + txtStockID.getText() + "'," +
                                "'" + txtStockName.getText() + "'," +
                                "'" + txtStockDes.getText() + "'," +
                                "'" + txtStockOnHand.getText() + "'," +
                                "'" + txtStockPrice.getText() + "'," +
                                "'" + bgpStockStatus.getSelection().getActionCommand() + "')"
                );
            } else {
                connManager.executeUpdate(
                        "UPDATE lpa_stock SET " +
                                "lpa_stock_ID = '" + txtStockID.getText() + "'," +
                                "lpa_stock_name = '" + txtStockName.getText() + "'," +
                                "lpa_stock_desc = '" + txtStockDes.getText() + "'," +
                                "lpa_stock_onhand = '" + txtStockOnHand.getText() + "'," +
                                "lpa_stock_price = '" + txtStockPrice.getText() + "', " +
                                "lpa_stock_status = '" + bgpStockStatus.getSelection().getActionCommand() + "' " +
                                "WHERE lpa_stock_ID = '" + StockID + "' LIMIT 1;"
                );
            }
            searchStockData(txtStockSearch.getText());
            JOptionPane.showMessageDialog(null, "Record saved!");
            ifStock.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
