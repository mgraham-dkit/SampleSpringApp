package web_patterns.persistence;

import web_patterns.business.Employee;

import java.util.List;

public interface EmployeeDao {
    public List<Employee> getAllEmployees();
    public Employee getById(int id);
    public boolean updateEmployeeEmail(int id, String email);
    public int addEmployee(Employee e);
}
