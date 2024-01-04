package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.yearup.data.mysql.MySqlProductDao.mapRow;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

     @Override
        public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories";

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {

                categories.add(mapRow(resultSet));
            }
          } catch (SQLException e){
        e.printStackTrace();
        }
        return categories;
        }

        @Override
        public Category getById(int categoryId){
        // get category by id
        String sql = "SELECT * FROM categories WHERE category_id = ?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, categoryId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return category;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    category.setCategoryId(generatedId);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
    }

    @Override
    public void delete(int categoryId) {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }


    @Override
    public Category create(Category category) {
        //New Cat
        String sql = "INSERT INTO categories(name, description) VALUES (?, ?)";

        try (var connection = getConnection();
             var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (var generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedID = generatedKeys.getInt(1);
                        category.setCategoryId(generatedId);
                        return category;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        //the update category
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try (var connection = getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setString(3, categoryId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        //the delete category
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (var connection = getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("There's and Error deleting this category: " + categoryId, e);

        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
                return category;
    }
} catch (SQLException e) {
            throw new RuntimeException(e);
        }