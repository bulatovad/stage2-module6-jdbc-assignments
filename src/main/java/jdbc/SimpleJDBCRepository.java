package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers(firstname, lastname, age) VALUES (?,?,?)";
    private static final String updateUserSQL = "";
    private static final String deleteUser = "DELETE FROM myysers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE (firstname||' '||lastname) like '%?%'";
    private static final String findAllUserSQL = "SELECT * FROM myusers";


    public Long createUser() {

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL);
            ps.setString(1, "Firstname");
            ps.setString(2, "LastName");
            ps.setInt(3, 1);
            if(ps.execute()){
                User newUser = findUserByName("Firstname Lastname");
                return newUser.getId();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return -1L;
    }

    public User findUserById(Long userId) {
        initializeConnection();
        try {

            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User findUserByName(String userName) {
        initializeConnection();
        try {
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        initializeConnection();
        try {
            st = connection.createStatement();

            ResultSet rs = st.executeQuery(findUserByNameSQL);
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUser() {
        initializeConnection();
        try {
            ps = connection.prepareStatement(updateUserSQL);
            if(ps.execute()){
                return findUserById(123L);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteUser(Long userId) {
        initializeConnection();
        try {
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeConnection() {
        if(connection == null) {
            try {
                connection = CustomDataSource.getInstance().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
