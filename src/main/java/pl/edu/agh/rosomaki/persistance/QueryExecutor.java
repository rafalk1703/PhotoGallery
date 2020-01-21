package pl.edu.agh.rosomaki.persistance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public final class QueryExecutor {

    private static final Logger LOGGER = Logger.getGlobal();

    private QueryExecutor() {
        throw new UnsupportedOperationException();
    }

    static {
        try {
            LOGGER.info("Creating table Album");
            create("CREATE TABLE IF NOT EXISTS album (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50) NOT NULL " +
                    ");");
            LOGGER.info("Creating table Photo");
            create("CREATE TABLE IF NOT EXISTS photo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50), " +
                    "height INT, " +
                    "width INT, " +
                    "date DATE, " +
                    "location VARCHAR(50), " +
                    "path VARCHAR(50), " +
                    "album_id INT NOT NULL, " +
                    "FOREIGN KEY(album_id) REFERENCES album (id)" +
                    ");");
            LOGGER.info("Creating table Tag");
            create("CREATE TABLE IF NOT EXISTS tag (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "photo_id INT NOT NULL, " +
                    "FOREIGN KEY(photo_id) REFERENCES photo (id)" +
                    ");");
        } catch (SQLException e) {
            LOGGER.info("Error during create tables: " + e.getMessage());
            throw new RuntimeException("Cannot create tables");
        }
    }

    public static int createAndObtainId(final PreparedStatement statement) throws SQLException {
        statement.execute();
        try (final ResultSet resultSet = statement.getGeneratedKeys()) {
            return readIdFromResultSet(resultSet);
        }
    }

    private static int readIdFromResultSet(final ResultSet resultSet) throws SQLException {
        return resultSet.next() ? resultSet.getInt(1) : -1;
    }

    public static void create(final String insertSql) throws SQLException {
        try (final PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(insertSql)) {
            statement.execute();
        }
    }

    public static ResultSet read(final String sql) throws SQLException {
        final Statement statement = ConnectionProvider.getConnection().createStatement();
        final ResultSet resultSet = statement.executeQuery(sql);
        LOGGER.info(String.format("Query: %s executed.", sql));
        return resultSet;
    }

    public static void delete(final String sql) throws SQLException {
        executeUpdate(sql);
    }

    private static void executeUpdate(final String... sql) throws SQLException {
        try (final Statement statement = ConnectionProvider.getConnection().createStatement()) {
            ConnectionProvider.getConnection().setAutoCommit(false);
            for (String s : sql) {
                statement.executeUpdate(s);
                LOGGER.info(String.format("Query: %s executed.", s));
            }
            ConnectionProvider.getConnection().commit();
            ConnectionProvider.getConnection().setAutoCommit(true);
        }
    }
}
