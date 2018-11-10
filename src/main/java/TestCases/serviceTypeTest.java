package TestCases;

import core.serviceStation.dao.ServiceJDBCDAO;
import org.json.JSONArray;

import java.sql.SQLException;

public class serviceTypeTest {
    public static void main(String[] args) throws SQLException {
        JSONArray jsonArray = ServiceJDBCDAO.getInstance().getServiceTypes();
        System.out.println(jsonArray);
    }
}
