package my.app.bookrepository.it;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class AbstractIntegrationTest {

    private  static final Logger LOG = Logger.getLogger(AbstractIntegrationTest.class.getName());

    private Connection connection;

    @Before
    public void setup() throws SQLException {
        connection = getConnection();
        connection.setAutoCommit(false);
    }

    @After
    public void after() throws SQLException {
        if ( connection != null ) {
            connection.close();
        }
    }

    public void clearTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + tableName);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceData(String tableName, String[] params, String[][] values) {
        clearTable(tableName);
        insertData(tableName, params, values);
    }

    public void insertData(String tableName, String[] params, String[][] values) {
        String param = createParam(params);
        String value = createValue(values);
        String query = "INSERT INTO " + tableName + "(" + param + ") VALUES" + value;

        LOG.info("query=" + query);

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String createParam(String[] params) {
        StringBuilder paramBuilder = new StringBuilder();
        for ( int i = 0 ; i < params.length ; i ++ ) {
            paramBuilder.append(params[i] + ",");
        }
        boolean hasValue = paramBuilder.charAt(paramBuilder.length() - 1) == ',';
        if( hasValue ) {
            paramBuilder.deleteCharAt(paramBuilder.length() - 1);
        }

        return paramBuilder.toString();
    }

    private String createValue(String[][] values) {
        StringBuilder valueBuilder = new StringBuilder();
        for ( int outer = 0 ; outer < values.length ; outer ++ ) {
            valueBuilder.append("(");
            for ( int inner = 0 ; inner < values[outer].length ; inner ++ ) {
                valueBuilder.append(values[outer][inner] + ", ");
            }
            boolean hasValue = valueBuilder.charAt(valueBuilder.length() - 1) == ',';
            if( hasValue ) {
                valueBuilder.deleteCharAt(valueBuilder.length() - 1);
            }

            valueBuilder.append("),");
        }

        boolean hasValue = valueBuilder.charAt(valueBuilder.length() - 1) == ',';
        if( hasValue ) {
            valueBuilder.deleteCharAt(valueBuilder.length() - 1);
        }
        return valueBuilder.toString();
    }

        private Connection getConnection() {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setURL("jdbc:mysql://192.168.1.109:3306/BookRepository");
            dataSource.setUser("root");
            dataSource.setPassword("n1k1jkai");
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
