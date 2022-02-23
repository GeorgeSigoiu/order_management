package presentation;

import bll.ProductBLL;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final View view;

    public Controller(View view) {
        this.view = view;
        view.addAddActionListener(new AddActionListener());
        view.addDeleteActionListener(new DeleteActionListener());
        view.addCancelActionListener(new CancelActionListener());
        view.addEditActionListener(new EditActionListener());
        view.addSaveActionListener(new SaveActionListener());
        view.addRefreshActionListener(new RefreshOrderActionListener());
        view.addAddMouseListener(new AddMouseListener());
    }

    /**
     * Clasa care descrie functionarea butonului de adaugare in baza de date.
     * In functie de valorile din tabelul pentru adaugare in baza de date se creeaza un nou obiect care
     * apoi este introdus in baza de date. La apsarea butonului (daca interfata pentru comenzi este selectata)
     * va aparea un mesaj de eroare daca nu sunt suficiente produse in stock. Dupa apasarea butonului, valorile
     * respective din tabel se vor reseta.
     */
    class AddActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            DefaultTableModel model2 = view.getAddTableModel();
            Object[] values = new Object[model2.getColumnCount()];
            if (view.getObject().equals("Order")) {
                values[0] = view.getValue0TxtField().getText();
                String nameClient = (String) view.getValue1ComboBox().getSelectedItem();
                values[1] = nameClient;
                String nameProduct = (String) view.getValue2ComboBox().getSelectedItem();
                values[2] = nameProduct;
                double pricePerItem = Double.parseDouble(view.getValue3TxtField().getText());
                values[3] = pricePerItem;
                int quantity = Integer.parseInt(view.getValue4TxtField().getText());
                values[4] = quantity;
                double totalPrice = Double.parseDouble(view.getValue5TxtField().getText());
                values[5] = totalPrice;

                view.getValue0TxtField().setText("");
                view.getValue1ComboBox().setSelectedIndex(0);
                view.getValue2ComboBox().setSelectedIndex(0);
                view.getValue3TxtField().setText("");
                view.getValue4TxtField().setText("");
                view.getValue5TxtField().setText("");
            } else {

                for (int i = 0; i < model2.getColumnCount(); i++)
                    values[i] = model2.getValueAt(0, i);
                for (int i = 0; i < model2.getColumnCount(); i++)
                    model2.setValueAt("", 0, i);
            }
            try {
                Object object = createObject1(values);
                Object clsBLL = Class.forName("bll." + view.getObject() + "BLL").newInstance();
                Class cls = Class.forName("model." + view.getObject());
                Method m = clsBLL.getClass().getDeclaredMethod("insert", cls);
                m.invoke(clsBLL, object);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException instantiationException) {
                instantiationException.printStackTrace();
            } catch (InvocationTargetException invocationTargetException) {
                JOptionPane.showMessageDialog(view.getAddBtn(), "Nu exista suficiente produse in stock!");
            }
            view.updateTable();
        }

        /**
         * Metoda care creeaza un obiect de tip Client, Product sau Order cu valorile din tabelul pentru adaugare in baza de date.
         *
         * @param values valorile din tabel.
         * @return Object, creeaza un nou obiect in functie de tipul acestuia din tabel
         */
        private Object createObject1(Object[] values) {
            try {
                Class cls = Class.forName("model." + view.getObject());
                Object instance = cls.newInstance();
                Field[] fields = cls.getDeclaredFields();
                int indx = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (indx == 0 && values[0] != null && !values[0].equals(""))
                        try {
                            field.set(instance, values[0]);
                        } catch (IllegalArgumentException e) {
                            try {
                                field.set(instance, Double.parseDouble((String) values[indx]));
                            } catch (IllegalArgumentException ex) {
                                field.set(instance, Integer.parseInt((String) values[indx]));
                            }
                        }
                    else if (indx > 0) {
                        try {
                            field.set(instance, values[indx]);
                        } catch (IllegalArgumentException e) {
                            try {
                                field.set(instance, Double.parseDouble((String) values[indx]));
                            } catch (IllegalArgumentException ex) {
                                field.set(instance, Integer.parseInt((String) values[indx]));
                            }
                        }
                    }
                    indx++;
                }
                return instance;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Clasa care descrie functionarea butonului de delete a interfetei.
     */
    class DeleteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = view.getMainTable();
            int[] rows = table.getSelectedRows();
            for (int row : rows) {
                int id = (int) table.getValueAt(row, 0);
                try {
                    Object clsBLL = Class.forName("bll." + view.getObject() + "BLL").newInstance();
                    Method method = clsBLL.getClass().getDeclaredMethod("delete" + view.getObject() + "ById", int.class);
                    method.setAccessible(true);
                    method.invoke(clsBLL, id);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
            view.updateTable();
        }
    }

    /**
     * Clasa care descrie functionarea butonului de edit a interfetei.
     * La apasararea butonului, butoanele de save, delete si cancel vor deveni vizibile.
     */
    class EditActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.setVisible(true);
            view.setEditable(true);
        }
    }

    /**
     * Clasa care descrie functionarea butonului de save a interfetei.
     * La apasararea butonului, butoanele de save, delete si cancel vor deveni invizibile, iar
     * datele se vor modifica in baza de date.
     */
    class SaveActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                List<Object> objectsValues = getValuesFromTable(view.getMainTable());
                Object clsBLL = Class.forName("bll." + view.getObject() + "BLL").newInstance();
                Class cls = Class.forName("model." + view.getObject());
                Method m = clsBLL.getClass().getDeclaredMethod("update", cls);
                assert objectsValues != null;
                for (Object object : objectsValues) {
                   m.invoke(clsBLL, object);
                }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException instantiationException) {
                instantiationException.printStackTrace();
            }
            view.setVisible(false);
            view.setEditable(false);
        }

        private List<Object> getValuesFromTable(JTable table) {
            try {
                List<Object> list = new ArrayList<>();
                int columns = table.getColumnCount();
                int rows = table.getRowCount();
                TableModel model = table.getModel();
                Class cls = Class.forName("model." + view.getObject());
                Field[] fields = cls.getDeclaredFields();

                for (int i = 0; i < rows; i++) {
                    Object[] values = new Object[columns];
                    for (int j = 0; j < columns; j++) {
                        values[j] = model.getValueAt(i, j);
                    }
                    int indx = 0;
                    Object instance = cls.newInstance();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        try {
                            field.set(instance, values[indx]);
                        } catch (IllegalArgumentException e) {
                            try {
                                field.set(instance, Double.parseDouble((String) values[indx]));
                            } catch (IllegalArgumentException ex) {
                                field.set(instance, Integer.parseInt((String) values[indx]));
                            }
                        }
                        indx++;
                    }
                    list.add(instance);
                }
                return list;
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Clasa care descrie functionarea butonului de save a interfetei.
     * La apasararea butonului, butoanele de save, delete si cancel vor deveni invizibile.
     */
    class CancelActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.setVisible(false);
            view.setEditable(false);
            view.updateTable();
        }
    }

    /**
     * Clasa care descrie functionarea butonului de refresh a interfetei.
     * Butonul este disponibil doar in fereastra pentru comenzi. La apasarea butonul se vor actualiza
     * datele comenzii, pretul pe bucata si pretul total, in functie de produsul ales si de cantitatea introdusa.
     */
    class RefreshOrderActionListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            NumberFormat formatter = new DecimalFormat("#0.00");
            String nameProduct = (String) view.getValue1ComboBox().getSelectedItem();
            Product p;
            ProductBLL productBLL = new ProductBLL();
            p = productBLL.findProductByName(nameProduct);
            double price = p.getPrice();
            view.getValue3TxtField().setText(String.valueOf(formatter.format(price)));
            if (!view.getValue4TxtField().getText().equals("")) {
                int quantity = Integer.parseInt(view.getValue4TxtField().getText());
                double pricePerItem = Double.parseDouble(view.getValue3TxtField().getText());
                double priceTotal = quantity * pricePerItem;
                view.getValue5TxtField().setText(String.valueOf(formatter.format(priceTotal)));
            }
        }
    }

    /**
     * Clasa adauga un mouse event pe butonul de adaugare in baza de date.
     * Daca nu sunt completate toate campurile, cu exceptia campului id, cand cursorului
     * va aparea peste buton va fi afisat un avertisment.
     */
    class AddMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

            try {
                if (view.getObject().equals("Order")) {
                    if (view.getValue1ComboBox().getSelectedIndex() == 0 ||
                            view.getValue2ComboBox().getSelectedIndex() == 0 ||
                            view.getValue5TxtField().getText().equals("") ||
                            view.getValue3TxtField().getText().equals("") ||
                            view.getValue3TxtField().getText().equals("")) throw new IllegalCallerException();
                } else {
                    JTable table = view.getAddTable();
                    for (int i = 1; i < table.getColumnCount(); i++) {
                        if (table.getValueAt(0, i) == null || table.getValueAt(0, i).equals(""))
                            throw new IllegalCallerException();
                    }
                }
            } catch (IllegalCallerException exception) {
                view.getAddBtn().setEnabled(false);
                JOptionPane.showMessageDialog(view.getAddBtn(), "Complete all fields! (ID field is optional)");
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            view.getAddBtn().setEnabled(true);
        }
    }
}
