package dao;

import connection.ConnectionFactory;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class OrderDAO extends AbstractDAO<Order> {
    public OrderDAO() {
        super();
    }

    /**
     * Cauta o comanda in baza de date si returneaza id-ul comenzii
     *
     * @param o Order, comanda cautata
     * @return int, id-ul comenzii cautate
     */
    public int getOrderId(Order o) {
        int id = 0;
        String query = "select * from `warehouse`.`order` where `clientName`='" + o.getClientName() + "' and `productName`='" +
                o.getProductName() + "' and `productQuantity`='" + String.valueOf(o.getProductQuantity()) + "';";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            resultSet.next();
            id = resultSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "orderDAO:getOrderId " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return id;
    }
}
