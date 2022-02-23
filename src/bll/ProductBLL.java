package bll;

import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.NoSuchElementException;
/**
 * Reprezinta layer-ul care realizeaza legatura dintre Product si ProductDAO
 */
public class ProductBLL {
    private final ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    /**
     * Cauta in baza de date un produs dupa id.
     *
     * @param id Integer, id-ul produsului cautat
     * @return Product, produsul cu id-ul cautat
     */
    public Product findProductById(int id) {
        Product pr = productDAO.findById(id);
        if (pr == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return pr;
    }

    /**
     * Cauta in baza de date toate produsele.
     *
     * @return o lista cu produsele din baza de date.
     */
    public List<Product> findAll() {
        List<Product> pr = productDAO.findAll();
        if (pr == null) {
            throw new NoSuchElementException("Could find any product!");
        }
        return pr;
    }

    /**
     * Adauga un produs nou in baza de date.
     *
     * @param p Product, produsul care trebuie adaugat
     * @return Product, produsul daca adaugarea a reusit.
     */
    public Product insert(Product p) {
        return productDAO.insert(p);
    }

    /**
     * Sterge un produs din baza de date in functie de id.
     *
     * @param id Integer, id-ul produsului care trebuie sters
     */
    public void deleteProductById(int id) {
        productDAO.deleteById(id);
    }

    public Product findProductByName(String name) {
        Product pr = productDAO.findByName(name);
        return pr;
    }

    /**
     * Actualizeaza valorile unui produs in functie de id.
     *
     * @param p Product, produsul care trebuie actualizat
     * @return Product, produsul respectiv daca actualizarea a reusit
     */
    public Product update(Product p) {
        return productDAO.update(p);
    }
}
