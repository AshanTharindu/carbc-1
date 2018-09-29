package core.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IdentityJDBC {
    Connection connection = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;
}
