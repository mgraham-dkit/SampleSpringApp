package web_patterns.persistence;

import org.junit.jupiter.api.Test;
import web_patterns.business.Employee;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoImplTest {
    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    @Test
    void addEmployee_SuccessfulAdd() throws SQLException {
        Employee tester = Employee.builder()
                .employeeNumber(-1)
                .email("michelle@michelle.ie")
                .extension("1830")
                .firstName("Michelle")
                .lastName("Graham")
                .reportsTo(1076)
                .jobTitle("Sleeper")
                .officeCode("2040")
                .build();

        int incorrectResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        EmployeeDao empDao = new EmployeeDaoImpl(conn);

        int result = empDao.addEmployee(tester);
        assertNotEquals(incorrectResult, result);
        tester.setEmployeeNumber(result);

        Employee inserted = empDao.getById(result);
        assertNotNull(inserted);

        assertEmployeesEqual(tester, inserted);
        conn.rollback();
    }

    private void assertEmployeesEqual(Employee e1, Employee e2){
        assertEquals(e1.getEmployeeNumber(), e2.getEmployeeNumber());
        assertEquals(e1.getFirstName(), e2.getFirstName());
        assertEquals(e1.getLastName(), e2.getLastName());
        assertEquals(e1.getExtension(), e2.getExtension());
        assertEquals(e1.getJobTitle(), e2.getJobTitle());
        assertEquals(e1.getOfficeCode(), e2.getOfficeCode());
        assertEquals(e1.getReportsTo(), e2.getReportsTo());
        assertEquals(e1.getEmail(), e2.getEmail());
    }
}