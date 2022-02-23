package bll;

import bll.exceptions.NegativeQuantityException;
import dao.OrderDAO;
import model.Client;
import model.Order;
import model.Product;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Reprezinta layer-ul care realizeaza legatura dintre Order si OrderDAO
 */
public class OrderBLL {
    private final OrderDAO orderDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    /**
     * Cauta in baza de date o comanda dupa id.
     *
     * @param id Integer, id-ul comenzii cautate
     * @return Order, comanda daca a fost gasita.
     */
    public Order findOrderById(int id) {
        Order or = orderDAO.findById(id);
        if (or == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return or;
    }

    /**
     * Cauta in baza de date toate comenzile.
     *
     * @return lista de Order, o lista cu comenzile din baza de date.
     */
    public List<Order> findAll() {
        List<Order> or = orderDAO.findAll();
        if (or == null) {
            throw new NoSuchElementException("Could find any orders!");
        }
        return or;
    }

    /**
     * Adauga o comanda noua in baza de date.
     *
     * @param o Order, comanda care trebuie adaugata
     * @return Order, comanda care a fost adaugata
     * @throws NegativeQuantityException arunca exceptia daca cantitatea din comanda este mai mare decat cantitatea ramasa in depozit
     */
    public Order insert(Order o) throws NegativeQuantityException {
        ProductBLL productBLL = new ProductBLL();
        Product product = productBLL.findProductByName(o.getProductName());

        if (product.getQuantity() < o.getProductQuantity())
            throw new NegativeQuantityException("Stockul este prea mic.");
        product.setQuantity(product.getQuantity() - o.getProductQuantity());
        productBLL.update(product);
        Order or = orderDAO.insert(o);
        generateBill(or);

        return o;
    }

    /**
     * Genereaza o factura pentru fiecare comanda nou adaugata in baza de date.
     *
     * @param o Order, comanda careia i se face factura.
     */
    private void generateBill(Order o) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        ClientBLL cbll = new ClientBLL();
        Client c = cbll.findClientByName(o.getClientName());
        ProductBLL pbll = new ProductBLL();
        Product p = pbll.findProductByName(o.getProductName());
        int orderId = orderDAO.getOrderId(o);
        String billName = "OrderBill_no_" + orderId + ".txt";
        File fileOutput = new File(billName);
        FileWriter write = null;
        try {
            write = new FileWriter(fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert write != null;
        PrintWriter pw = new PrintWriter(write);
        String mesaj = "\tFactura nr. " + orderId + "\n\n" +
                "\tDestinatar: " + c.getName() + "\n\t\tAdresa: " + c.getAddress() + "\n\t\tEmail: " + c.getEmail() + "\n\t\tTelefon: " + c.getPhone() +
                "\n\tProducator: " + p.getBrand() + "\n" +
                "\tProdus:\n" + "\t\t" + p.getDenomination() + "\n\t\tPret/buc: " + formatter.format(p.getPrice()) + "\n\t\t" + "Cantitate: " +
                o.getProductQuantity() + "\n\n\t\tPret total: " + formatter.format(o.getTotalPrice());
        pw.println(mesaj);
        pw.close();
    }

    /**
     * Sterge din baza de date o comanda in functie de id.
     *
     * @param id Integer, id-ul comenzii care urmeaza a fi stearsa
     */
    public void deleteOrderById(int id) {
        orderDAO.deleteById(id);
    }

    /**
     * Actualizeaza valorile unei comenzi in functie de id.
     *
     * @param o Order, comanda care trebuie actualizata
     * @return Order, comanda respectiva daca actualizarea a reusit
     */
    public Order update(Order o) {
        return orderDAO.update(o);
    }
}
