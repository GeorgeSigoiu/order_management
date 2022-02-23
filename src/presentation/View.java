package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Client;
import model.Order;
import model.Product;

import javax.swing.*;
import java.awt.SystemColor;
import java.awt.Window.Type;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa care creeaza interfata de gestiune a bazei de date.
 */
public class View {

    private JFrame framWindow;
    private JTable mainTable;
    private JTable addTable;

    private final JButton editBtn = new JButton("Edit");
    private final JButton saveBtn = new JButton("Save");
    private final JButton cancelBtn = new JButton("Cancel");
    private final JButton deleteBtn = new JButton("Delete");
    private final JButton addBtn = new JButton("Add");
    private final JButton refreshBtn = new JButton("Refresh");
    private final JTextField value0TxtField = new JTextField();

    private final JTextField value3TxtField = new JTextField();

    private final JTextField value4TxtField = new JTextField();

    private final JTextField value5TxtField = new JTextField();

    private final JComboBox value1ComboBox = new JComboBox();
    private final JComboBox value2ComboBox = new JComboBox();

    private DefaultTableModel mainTableModel;
    private DefaultTableModel addTableModel;

    private final String object;

    public View(String obj) {
        object = obj;
        initialize(obj + " Window");
        this.framWindow.setVisible(true);
    }

    /**
     * Crearea ferestrei cu tabelul principal.
     *
     * @param title String, reprezinta numele ferestrei.
     */
    private void initialize(String title) {
        framWindow = new JFrame();
        framWindow.setType(Type.POPUP);
        framWindow.setTitle(title);
        framWindow.getContentPane().setBackground(SystemColor.activeCaption);
        framWindow.getContentPane().setLayout(null);

        JScrollPane mainTablePane = new JScrollPane();
        mainTablePane.setBounds(146, 10, 681, 316);
        framWindow.getContentPane().add(mainTablePane);

        mainTable = new JTable();
        mainTable.setEnabled(false);
        createModel();
        mainTable.setModel(mainTableModel);

        mainTablePane.setViewportView(mainTable);

        editBtn.setBounds(33, 43, 85, 21);
        framWindow.getContentPane().add(editBtn);

        saveBtn.setBounds(33, 74, 85, 21);
        saveBtn.setVisible(false);
        framWindow.getContentPane().add(saveBtn);

        cancelBtn.setBounds(33, 105, 85, 21);
        cancelBtn.setVisible(false);
        framWindow.getContentPane().add(cancelBtn);

        deleteBtn.setBounds(33, 136, 85, 21);
        deleteBtn.setVisible(false);
        framWindow.getContentPane().add(deleteBtn);

        addBtn.setBounds(33, 348, 85, 21);
        framWindow.getContentPane().add(addBtn);


        JScrollPane addTablePane = new JScrollPane();
        if (this.object.equals("Order")) {
            addTablePane.setBounds(146, 345, 681, 19);
            value0TxtField.setBounds(146, 364, 110, 17);
            framWindow.getContentPane().add(value0TxtField);
            value0TxtField.setColumns(10);

            value1ComboBox.setBounds(256, 362, 115, 20);
            value1ComboBox.setModel(createProductComboBox());
            framWindow.getContentPane().add(value1ComboBox);

            value2ComboBox.setBounds(372, 362, 115, 20);
            value2ComboBox.setModel(createClientComboBox());
            framWindow.getContentPane().add(value2ComboBox);

            value3TxtField.setColumns(10);
            value3TxtField.setBounds(488, 363, 110, 17);
            value3TxtField.setEditable(false);
            framWindow.getContentPane().add(value3TxtField);

            value4TxtField.setColumns(10);
            value4TxtField.setBounds(598, 363, 115, 17);
            framWindow.getContentPane().add(value4TxtField);

            value5TxtField.setColumns(10);
            value5TxtField.setBounds(714, 363, 112, 17);
            value5TxtField.setEditable(false);
            framWindow.getContentPane().add(value5TxtField);

            refreshBtn.setBounds(33, 375, 85, 21);
            framWindow.getContentPane().add(refreshBtn);

        } else
            addTablePane.setBounds(146, 345, 681, 38);
        framWindow.getContentPane().add(addTablePane);

        addTable = new JTable();
        createModelAddClientTable();
        addTable.setModel(addTableModel);
        addTablePane.setViewportView(addTable);
        framWindow.setBounds(400, 100, 870, 456);
        framWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    }

    /**
     * Creeaza un combo box care contine toate produsele din baza de date.
     *
     * @return ComboBoxModel, contine produsele din baza de date.
     */
    private ComboBoxModel createProductComboBox() {
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();
        int count = products.size();
        Object[] objs = new Object[count + 1];
        objs[0] = "Select product";
        int indx = 1;
        for (Product product : products) {
            objs[indx] = product.getDenomination();
            indx++;
        }
        return new DefaultComboBoxModel(objs);
    }

    /**
     * Creeaza un combo box care contine toti clientii din baza de date.
     *
     * @return ComboBoxModel, contine clientii din baza de date.
     */
    private ComboBoxModel createClientComboBox() {
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();
        int count = clients.size();
        Object[] objs = new Object[count + 1];
        objs[0] = "Select client";
        int indx = 1;
        for (Client client : clients) {
            objs[indx] = client.getName();
            indx++;
        }
        return new DefaultComboBoxModel(objs);
    }

    /**
     * Actualizeaza datele din tabelul din interfata grafica cu valorile actuale ale valorilor din baza de date.
     */
    public void updateTable() {
        createModel();
        mainTable.setModel(mainTableModel);
    }

    /**
     * Creeaza capul tabelului folosit pentru inserarea in baza de date.
     */
    private void createModelAddClientTable() {
        addTableModel = new DefaultTableModel();
        addTableModel.setColumnIdentifiers(createColumnIdentifiers());
        if (this.object.equals("Order")) {

        } else {
            Class cls = Client.class;
            Field[] fields = cls.getDeclaredFields();
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                row[i] = null;
            }
            addTableModel.addRow(row);
        }
    }

    /**
     * Creeaza si populeaza tabelul principal cu datele din baza de date.
     */
    private void createModel() {
        mainTableModel = new DefaultTableModel();
        Object[] columnIdentifiers = createColumnIdentifiers();
        mainTableModel.setColumnIdentifiers(columnIdentifiers);

        if (object.equals("Client")) {
            createModelForClient();
        } else if (object.equals("Product")) {
            createModelForProduct();
        } else {
            createModelForOrder();
        }
    }

    /**
     * Populeaza tabelul principal cu comenzile din baza de date.
     */
    private void createModelForOrder() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        OrderBLL orderBLL = new OrderBLL();
        List<Order> orders = orderBLL.findAll();
        Class cls = Order.class;
        Field[] fields = cls.getDeclaredFields();
        Object[] row = new Object[fields.length];
        for (Order order : orders) {
            int indx = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(order);
                    try {
                        value = Integer.parseInt(String.valueOf(value));
                    } catch (NumberFormatException e) {
                        try {
                            double d = Double.parseDouble(String.valueOf(value));
                            value = formatter.format(d);
                        } catch (NumberFormatException ex) {

                        }
                    }
                    row[indx] = value;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                indx++;
            }
            mainTableModel.addRow(row);
        }
    }

    /**
     * Populeaza tabelul principal cu produsele din baza de date.
     */
    private void createModelForProduct() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();
        Class cls = Product.class;
        Field[] fields = cls.getDeclaredFields();
        Object[] row = new Object[fields.length];
        for (Product product : products) {
            int indx = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(product);
                    try {
                        value = Integer.parseInt(String.valueOf(value));
                    } catch (NumberFormatException e) {
                        try {
                            double d = Double.parseDouble(String.valueOf(value));
                            value = formatter.format(d);
                        } catch (NumberFormatException ex) {
                        }
                    }
                    row[indx] = value;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                indx++;
            }
            mainTableModel.addRow(row);
        }
    }

    /**
     * Populeaza tabelul principal cu clientii din baza de date.
     */
    private void createModelForClient() {
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();
        Class cls = Client.class;
        Field[] fields = cls.getDeclaredFields();
        Object[] row = new Object[fields.length];
        for (Client client : clients) {
            int indx = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(client);
                    row[indx] = value;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                indx++;
            }
            mainTableModel.addRow(row);
        }
    }

    /**
     * Creeaza capul tabelului principal.
     *
     * @return un vector de obiecte care reprezinta fiecare coloana din tabel.
     */
    private Object[] createColumnIdentifiers() {
        List<String> columnsName = new ArrayList<>();
        try {
            Class cls = Class.forName("model." + object);

            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                columnsName.add(field.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object[] columnsIdentifiers = new Object[columnsName.size()];
        int indx = 0;
        for (String s : columnsName)
            columnsIdentifiers[indx++] = s;
        return columnsIdentifiers;
    }

    /**
     * Seteaza vizibilitatea butoanelor cancelBtn, saveBtn, delteBtn la valoarea "val".
     *
     * @param val parametru de tip boolean care reprezinta vizibilitatea butoanelor.
     */
    public void setVisible(boolean val) {
        cancelBtn.setVisible(val);
        saveBtn.setVisible(val);
        deleteBtn.setVisible(val);
    }

    /**
     * Seteaza tabelul principal sa fie editabil sau nu, in functie de valoarea parametrului "val" si sterge posibilele selectari ale unor randuri.
     *
     * @param val parametru de tip boolean in functie de care tabelul poate fi sau nu editat
     */
    public void setEditable(boolean val) {
        mainTable.setEnabled(val);
        mainTable.clearSelection();
    }

    /**
     * Adauga ActonListener pe butonul add
     *
     * @param actionListener ActionListener, descrie functionalitatea butonului add
     */
    public void addAddActionListener(ActionListener actionListener) {
        addBtn.addActionListener(actionListener);
    }

    /**
     * Adauga ActonListener pe butonul delete
     *
     * @param actionListener ActionListener, descrie functionalitatea butonului delete
     */
    public void addDeleteActionListener(ActionListener actionListener) {
        deleteBtn.addActionListener(actionListener);
    }

    /**
     * Adauga ActonListener pe butonul save
     *
     * @param actionListener ActionListener, descrie functionalitatea butonului save
     */
    public void addSaveActionListener(ActionListener actionListener) {
        saveBtn.addActionListener(actionListener);
    }

    /**
     * Adauga ActonListener pe butonul cancel
     *
     * @param actionListener ActionListener, descrie functionalitatea butonului cancel
     */
    public void addCancelActionListener(ActionListener actionListener) {
        cancelBtn.addActionListener(actionListener);
    }

    /**
     * Adauga ActonListener pe butonul edit
     *
     * @param actionListener ActionListener, descrie functionalitatea butonului edit
     */
    public void addEditActionListener(ActionListener actionListener) {
        editBtn.addActionListener(actionListener);
    }

    /**
     * Adauga ActonListener pe butonul refresh
     *
     * @param actionListener ActionListener, descrie functionalitatea butonului refresh
     */
    public void addRefreshActionListener(ActionListener actionListener) {
        refreshBtn.addActionListener(actionListener);
    }

    public void addAddMouseListener(MouseListener mouseListener) {
        addBtn.addMouseListener(mouseListener);
    }


    public JTable getMainTable() {
        return mainTable;
    }


    public JTable getAddTable() {
        return addTable;
    }


    public DefaultTableModel getAddTableModel() {
        return addTableModel;
    }


    public String getObject() {
        return object;
    }


    public JTextField getValue0TxtField() {
        return value0TxtField;
    }

    public JTextField getValue3TxtField() {
        return value3TxtField;
    }


    public JTextField getValue4TxtField() {
        return value4TxtField;
    }


    public JTextField getValue5TxtField() {
        return value5TxtField;
    }


    public JComboBox getValue1ComboBox() {
        return value1ComboBox;
    }


    public JComboBox getValue2ComboBox() {
        return value2ComboBox;
    }


    public JButton getAddBtn() {
        return addBtn;
    }
}
