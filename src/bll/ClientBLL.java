package bll;

import dao.ClientDAO;
import model.Client;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Reprezinta layer-ul dintre Client si ClientDAO
 */
public class ClientBLL {

    private final ClientDAO clientDAO;

    public ClientBLL() {
        clientDAO = new ClientDAO();
    }

    /**
     * Cauta in baza de date un client dupa id.
     *
     * @param id id-ul clientului cautat
     * @return Client daca a fost gasit.
     */
    public Client findClientById(int id) {
        Client cl = clientDAO.findById(id);
        if (cl == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return cl;
    }

    /**
     * Cauta in baza de date toti clientii.
     *
     * @return lista de Client, lista cu clientii din baza de date.
     */
    public List<Client> findAll() {
        List<Client> cl = clientDAO.findAll();
        if (cl == null) {
            throw new NoSuchElementException("Could find any client!");
        }
        return cl;
    }

    /**
     * Adauga un client nou in baza de date.
     *
     * @param c Client, clientul care trebuie adaugat
     * @return Client, clientul daca adaugarea a reusit.
     */
    public Client insert(Client c) {
        Client cl = clientDAO.insert(c);
        return cl;
    }

    /**
     * Actualizeaza valorile unui client in functie de id.
     *
     * @param c Client, clientul care trebuie actualizat
     * @return Client, clientul respectiv daca actualizarea a reusit
     */
    public Client update(Client c) {
        return clientDAO.update(c);
    }

    /**
     * Sterge un client din baza de date in functie de id.
     *
     * @param id Integer, id-ul clinetului care trebuie sters
     */
    public void deleteClientById(int id) {
        clientDAO.deleteById(id);
    }

    /**
     * Cauta un client in baza de date dupa nume.
     *
     * @param name String, numele clientului care trebuie cautat
     * @return Client, prima aparitie a clientul cu numele respectiv, daca exista in baza de date.
     */
    public Client findClientByName(String name) {
        Client cl = clientDAO.findByName(name);
        return cl;
    }
}
