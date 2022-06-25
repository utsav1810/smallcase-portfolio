package com.smallcase.portfolio.dao;

import com.smallcase.portfolio.models.Stock;
import com.smallcase.portfolio.postgres.PostgreSQLJDBC;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PortfolioDao implements Dao<Stock, Integer> {

    private static final Logger LOGGER =
            Logger.getLogger(PortfolioDao.class.getName());
    public static final String PORTFOLIO = "portfolio";
    private final Optional<Connection> connection;

    public PortfolioDao() {
        this.connection = Optional.ofNullable(PostgreSQLJDBC.getConnection());
    }

    @Override
    public Optional<Stock> get(int id) {
        return Optional.empty();
    }

    public Optional<Stock> getByTicker(String ticker) {

        return Optional.empty();
    }

    @Override
    public List<Stock> getAll() {
        List<Stock> customers = new ArrayList<>();
        String sql = "SELECT * FROM " + PORTFOLIO;

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String ticker = resultSet.getString("ticker");
                    double averagePrice = resultSet.getDouble("average_price");
                    int qty = resultSet.getInt("qty");

                    Stock customer = new Stock(id, ticker, averagePrice, qty);

                    customers.add(customer);

                    LOGGER.log(Level.INFO, "Found {0} in database", customer);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return customers;
    }

    @Override
    public Optional<Integer> save(Stock stock) {
        String message = "The customer to be added should not be null";
        Stock nonNullCustomer = Objects.requireNonNull(stock, message);
        String sql = "INSERT INTO " + PORTFOLIO + " (ticket, average_price, qty) " + "VALUES(?, ?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullCustomer.getTicker());
                statement.setDouble(2, nonNullCustomer.getAveragePrice());
                statement.setInt(3, nonNullCustomer.getQty());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(Level.INFO, "{0} created successfully? {1}", new Object[]{nonNullCustomer, (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error occurred while creating record", ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(Stock stock) {

    }

    @Override
    public void delete(Stock stock) {

    }

    // Other methods of the interface which currently aren't implemented yet
}
