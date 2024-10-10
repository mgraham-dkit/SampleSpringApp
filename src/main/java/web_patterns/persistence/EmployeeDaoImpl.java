package web_patterns.persistence;

import web_patterns.business.Employee;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeDaoImpl extends MySQLDao implements EmployeeDao {
    public EmployeeDaoImpl(String propertiesFile){
        super(propertiesFile);
    }
    
    public EmployeeDaoImpl(){
        super();
    }

    public static void main(String[] args) {
        EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
        System.out.println(empDao.getAllEmployees());
        System.out.println("---------------------------------------");
        System.out.println("No such employee: " + empDao.getById(1));
        System.out.println("Employee exists: " + empDao.getById(1002));
        System.out.println("---------------------------------------");
        empDao.updateEmployeeEmail(1002, "hello@name.me");
        System.out.println("Employee exists: " + empDao.getById(1002));
        System.out.println("---------------------------------------");
        Employee employee = EmployeeDaoImpl.createEmployee();
        System.out.println("Employee details: " + employee);
        int newId = empDao.addEmployee(employee);
        System.out.println("The id of the inserted employee is: " + newId);
        System.out.println("When retrieved from the database, this employee looks like this: " + empDao.getById(newId));
        System.out.println();
    }

    private static Employee createEmployee(){
        Scanner input = new Scanner(System.in);
        // Read in the employee's information
        System.out.println("Please enter the new employee's first name:");
        String firstName = input.nextLine();

        System.out.println("Please enter the new employee's last name:");
        String lastName = input.nextLine();

        System.out.println("Please enter the new employee's extension number:");
        String extension = input.nextLine();

        System.out.println("Please enter the new employee's email address:");
        String email = input.nextLine();

        System.out.println("Please enter the new employee's office code:");
        String officeCode = input.nextLine();

        System.out.println("Please enter the id of the new employee's manager");
        int reportsTo = input.nextInt();
        // Remove the left-over newline character from the buffer
        input.nextLine();

        System.out.println("Please enter the new employee's job title:");
        String jobTitle = input.nextLine();

        // Put all the information about the employee into an Employee object
        return new Employee(lastName, firstName, extension, email, officeCode, reportsTo, jobTitle);
    }

    public int addEmployee(Employee emp){
        int newId = -1;

        Connection conn = super.getConnection();
        String sql = "INSERT INTO Employees(lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, emp.getLastName());
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getExtension());
            ps.setString(4, emp.getEmail());
            ps.setString(5, emp.getOfficeCode());
            ps.setInt(6, emp.getReportsTo());
            ps.setString(7, emp.getJobTitle());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    newId = rs.getInt(1);
                }
            }catch (SQLException e){
                System.out.println(LocalDateTime.now() + ": An SQLException occurred while retrieving the generated " +
                        "primary key information." +
                        ".");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }catch(SQLIntegrityConstraintViolationException e){
            System.out.println(LocalDateTime.now() + ": An integrity constraint failed while inserting the new " +
                    "Employee." +
                    ".");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException occurred while inserting the new " +
                    "Employee." +
                    ".");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return newId;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * from Employees")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee e = mapRow(rs);
                    employees.add(e);
                }
            } catch (SQLException e) {
                System.out.println(LocalDateTime.now() + ": An SQLException  occurred while running the query" +
                        " or processing the result.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getById(int id) {
        Employee emp = null;

        Connection conn = super.getConnection();
        try (PreparedStatement ps =
                     conn.prepareStatement("SELECT * from Employees where employeeNumber = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    emp = mapRow(rs);
                }
            } catch (SQLException e) {
                System.out.println(LocalDateTime.now() + ": An SQLException  occurred while running the query" +
                        " or processing the result.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return emp;
    }

    public boolean updateEmployeeEmail(int id, String email) throws RuntimeException {
        int rowsAffected = 0;

        Connection conn = super.getConnection();
        try (PreparedStatement ps =
                     conn.prepareStatement("UPDATE Employees SET email = ? WHERE employeeNumber = ?")) {
            ps.setString(1, email);
            ps.setInt(2, id);

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        if(rowsAffected > 1){
            throw new RuntimeException(LocalDateTime.now() + " ERROR: Multiple rows affected on primary key selection" +
                    ".");
        }
        else if(rowsAffected == 0){
            return false;
        }else{
            return true;
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException{
        Employee e = new Employee(
                rs.getInt("employeeNumber"),
                rs.getString("lastName"),
                rs.getString("firstName"),
                rs.getString("extension"),
                rs.getString("email"),
                rs.getString("officeCode"),
                rs.getInt("reportsTo"),
                rs.getString("jobTitle")
        );
        return e;
    }

}
