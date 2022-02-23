package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

/**
 * Reprezinta legatura dintre baza de date si aplicatie
 *
 * @param <T> poate fi Client, Order, Product
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * Se creeaza o interogare care afiseaza toate interogarile din baza de date care contin o anumita valoare in campul "field"
     *
     * @param field campul din conditia interogarii
     * @return returneaza interogarea creata
     */
    private String createSelectQuery(String field) {
        return "SELECT " +
                " * " +
                " FROM " +
                "`warehouse`.`" + type.getSimpleName().toLowerCase() + "`" +
                " WHERE `" + field + "` =?";
    }

    /**
     * Cauta toate datele introduse intr-un anumit tabel, in functie de clasa T
     *
     * @return o lista de obiecte, reprezinta datele dintr-un tabel din baza de date
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from `warehouse`.`" + type.getSimpleName().toLowerCase() + "`;";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Cauta un obiect in baza de date in functie de id
     *
     * @param id id-ul obiectului cautat
     * @return T, un obiect de clasa T
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("`id`");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creeaza o lista de obiecte de clasa T in functie de rezultatul unei interogari
     *
     * @param resultSet ResultSet, rezultatul interogarii
     * @return o lista cu toate obiectele create cu datele din interogare
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (Constructor constructor : ctors) {
            ctor = constructor;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cauta un obiect in tabelul cu acelasi nume cu tipul obiectului cautat
     *
     * @param field String, campul care trebuie sa indeplineasca o conditie
     * @param value String, valoare campului
     * @return un obieci care contine datele din interogare
     */
    public T findByName(String field, String value) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(field);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, value);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creeaza o interogare pentru a adauga un obiect nou in baza de date
     *
     * @param t T, obiectul ce va fi introdus
     * @return String, interogarea
     */
    private String createInsertQuery(T t) {
        try {
            StringBuilder s = new StringBuilder("insert into " + "`warehouse`.`" + type.getSimpleName().toLowerCase() + "` (");
            Field[] fields = t.getClass().getDeclaredFields();
            fields[0].setAccessible(true);
            Object valueId = fields[0].get(t);
            if ((int) valueId != 0) {
                s.append("`").append(fields[0].getName()).append("`, ");
            }
            for (int i = 1; i < fields.length - 1; i++)
                s.append("`").append(fields[i].getName()).append("`, ");
            s.append("`").append(fields[fields.length - 1].getName()).append("`) ");
            s.append("values (");
            if ((int) valueId != 0) {
                fields[0].setAccessible(true);

                s.append("'").append(fields[0].get(t)).append("', ");

            }
            for (int i = 1; i < fields.length - 1; i++) {
                fields[i].setAccessible(true);
                s.append("'").append(fields[i].get(t)).append("', ");
            }
            fields[fields.length - 1].setAccessible(true);
            s.append("'").append(fields[fields.length - 1].get(t)).append("') ;");
            return s.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adauga un obiect nou in baza de date
     *
     * @param t T, obiectul ce va fi introdus
     * @return T, obiectul respectiv daca adaugarea a reusit
     */
    public T insert(T t) {
        try {
            String s = createInsertQuery(t);
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(s);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Creeaza interogarea pentru actualizarea unui obiect din baza de date
     *
     * @param t T, obiectul care trebuie actualizat
     * @return String, interogarea creata pentru obiectul transmis ca parametru
     */
    private String createUpdateQuery(T t) {
        try {
            StringBuilder query = new StringBuilder("update `warehouse`.`" + type.getSimpleName().toLowerCase() + "` set ");
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                query.append("`").append(fields[i].getName()).append("`='").append(fields[i].get(t)).append("'");
                if (i != fields.length - 1) {
                    query.append(", ");
                } else query.append(" ");
            }
            query.append(" where `id`='");
            Method method = null;

            method = t.getClass().getDeclaredMethod("getId");

            Object result = method.invoke(t);
            query.append(String.valueOf(result)).append("';");
            return query.toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualizeaza un obiect din baza de date
     *
     * @param t T, obiectul ce trebuie actualizat
     * @return T, obiectul actualizat
     */
    public T update(T t) {
        try {
            String s = createUpdateQuery(t);
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(s);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Sterge un obiect din baza de date in functie de id
     *
     * @param id Integer, id-ul obiectului care va fi sters
     */
    public void deleteById(int id) {
        try {
            String s = createDeleteQuery();
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(s);
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creeaza interogarea pentru stergerea unui obiect din baza de date
     *
     * @return String, intergarea creata
     */
    private String createDeleteQuery() {
        return "delete from `warehouse`.`" + type.getSimpleName().toLowerCase() + "` where " + "id" + " =?;";
    }
}
