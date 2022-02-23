package dao;

import model.Client;

public class ClientDAO extends AbstractDAO<Client> {

    public ClientDAO() {
        super();
    }

    /**
     * Cauta in baza de date un client cu numele dat ca parametru
     *
     * @param name String, numele clientului care va fi cautat
     * @return Client, clientul cu numele respectiv daca exista in baza de date
     */
    public Client findByName(String name) {
        String field = "name";
        Client cl = super.findByName(field, name);
        return cl;
    }
}
