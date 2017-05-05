import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by alima on 4git/5/17.
 */
@SuppressWarnings("WeakerAccess")
public class InvoicesFrame extends JInternalFrame
implements ActionListener{

    final ImageIcon searchIcon = new ImageIcon("ext-lib/searchIcon.png");
    JTable tblInvoices;
    private final ConnectionManager connManager = ConnectionManager.instance();
    JTextField txtSearch;
    JLabel lblTotalValue;

    public InvoicesFrame(){
        title = "Invoices";
        setBounds(586, 198, 725, 512);

        setFrameIcon(searchIcon);
        setClosable(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBackground(new Color(35, 44, 49));

        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(null);

        JPanel searchPanel = new JPanel();
        searchPanel.setSize(725, 45);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
        lblSearch.setForeground(new Color(102, 147, 182));

        txtSearch = new JTextField();
        txtSearch.addActionListener(this);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setBackground(Color.DARK_GRAY);
        txtSearch.setColumns(30);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(this);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        tblInvoices = new JTable();

        String[] columnNames = {
                "Number",
                "Date",
                "Customer Name",
                "Amount"
        };

        tblInvoices.setForeground(Color.WHITE);
        tblInvoices.setBackground(Color.DARK_GRAY);
        tblInvoices.setModel(new DefaultTableModel(null, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblInvoices);
        scrollPane.setBounds(10, 50, 689, 191);
        tblInvoices.setFillsViewportHeight(true);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setForeground(new Color(102, 147, 182));

        lblTotalValue = new JLabel("0.00");
        lblTotal.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTotal.setForeground(new Color(102, 147, 182));

        footerPanel.add(lblTotal);
        footerPanel.add(lblTotalValue);

        footerPanel.setBounds(0, 260, 700, 45);

        this.add(scrollPane);
        this.add(searchPanel);
        this.add(footerPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResultSet rs = null;
        try{
            String search = txtSearch.getText();
            rs = connManager.executeQuery("SELECT * FROM lpa_invoices WHERE " +
                                                    "lpa_inv_client_name LIKE '%" + search + "%'");

            if(rs.next()){
                double totalAmount = 0;
                DefaultTableModel model = (DefaultTableModel)tblInvoices.getModel();
                model.getDataVector().removeAllElements();
                model.fireTableDataChanged();
                rs.beforeFirst();
                while (rs.next()){
                    Object[] row = {
                            rs.getString("lpa_inv_no"),
                            rs.getString("lpa_inv_date"),
                            rs.getString("lpa_inv_client_name"),
                            rs.getString("lpa_inv_amount")
                    };
                    model.addRow(row);

                    totalAmount += Double.parseDouble(rs.getString("lpa_inv_amount"));
                }
                lblTotalValue.setText("$ " + totalAmount);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }finally {
            connManager.closeResultSet(rs);
        }
    }
}
