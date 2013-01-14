package my.app.bookrepository.it.util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcManager {

    private static final String JDBC_URL = "jdbc:mysql://192.168.1.109/BookRepository";

    private static final String APP_USER = "AppUser1";

    private static final String APP_PASS = "n1k1jkai";

    private static final String DRIVER_FQCN = "com.mysql.jdbc.Driver";

    private static final Logger LOG = Logger.getLogger(JdbcManager.class.getName());

    private Connection connection;

    public void open() {
        try {
            Class.forName(DRIVER_FQCN);
            try {
                Connection connection = DriverManager.getConnection(JDBC_URL, APP_USER, APP_PASS);
                connection.setAutoCommit(false);
                this.connection = connection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if ( connection != null ) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void clearTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE " + tableName);
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

        if( LOG.isLoggable(Level.INFO) ) {
            LOG.info("insert query=" + query);
        }

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTableAsCSV(String tableName, String where) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM " + tableName + " WHERE " + where;
            if ( LOG.isLoggable(Level.INFO) ) {
                LOG.info("select query=" + query);
            }

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            StringBuilder csvBuilder = new StringBuilder();
            while ( resultSet.next() ) {
                for ( int i = 0 ; i < metaData.getColumnCount() ; i ++ ) {
                    String colValue = resultSet.getString(i + 1);
                    csvBuilder.append(colValue + ",");
                }
                trimLastComma(csvBuilder);
                csvBuilder.append("\r\n");
            }

            return csvBuilder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if ( resultSet != null ) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String createParam(String[] params) {
        StringBuilder paramBuilder = new StringBuilder();
        for ( int i = 0 ; i < params.length ; i ++ ) {
            paramBuilder.append(params[i] + ",");
        }
        trimLastComma(paramBuilder);

        return paramBuilder.toString();
    }

    private void trimLastComma(StringBuilder paramBuilder) {
        boolean hasValue = paramBuilder.charAt(paramBuilder.length() - 1) == ',';
        if( hasValue ) {
            paramBuilder.deleteCharAt(paramBuilder.length() - 1);
        }
    }

    private String createValue(String[][] values) {
        StringBuilder valueBuilder = new StringBuilder();
        for ( int outer = 0 ; outer < values.length ; outer ++ ) {
            valueBuilder.append("(");
            for ( int inner = 0 ; inner < values[outer].length ; inner ++ ) {
                valueBuilder.append(values[outer][inner] + ",");
            }
            trimLastComma(valueBuilder);

            valueBuilder.append("),");
        }

        trimLastComma(valueBuilder);
        return valueBuilder.toString();
    }
}
