package dao;

import model.Product;

public class ProductDAO extends AbstractDAO<Product> {
    public ProductDAO() {
        super();
    }

    /**
     * Cauta un produs dupa nume.
     *
     * @param name String, numele produsului cautat
     * @return Product, produsul cu numele respectiv daca exista in baza de date.
     */
    public Product findByName(String name) {
        String field = "denomination";
        return super.findByName(field, name);
    }
}
