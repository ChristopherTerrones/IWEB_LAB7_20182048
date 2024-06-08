package com.example.lab7_20182048.Controllers;

import com.example.lab7_20182048.Beans.Department;
import com.example.lab7_20182048.Beans.Employee;
import com.example.lab7_20182048.Beans.Job;
import com.example.lab7_20182048.Daos.DepartmentDao;
import com.example.lab7_20182048.Daos.EmployeeDao;
import com.example.lab7_20182048.Daos.JobDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "home", urlPatterns = {"/home"})
public class EmployeeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        RequestDispatcher view;
        EmployeeDao employeeDao = new EmployeeDao();
        JobDao jobDao = new JobDao();
        DepartmentDao departmentDao = new DepartmentDao();

        switch (action) {
            case "lista":
                request.setAttribute("listaEmpleados", employeeDao.listarEmpleados());
                view = request.getRequestDispatcher("employees/lista.jsp");
                view.forward(request, response);
                break;
            case "agregar":
                request.setAttribute("listaTrabajos",jobDao.obtenerListaTrabajos());
                request.setAttribute("listaJefes",employeeDao.listarEmpleados());
                request.setAttribute("listaDepartamentos",departmentDao.lista());
                view = request.getRequestDispatcher("employees/formularioNuevo.jsp");
                view.forward(request, response);
                break;
            case "editar":
                if (request.getParameter("id") != null) {
                    String employeeIdString = request.getParameter("id");
                    int employeeId = 0;
                    try {
                        employeeId = Integer.parseInt(employeeIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("home");

                    }

                    Employee emp = employeeDao.obtenerEmpleado(employeeId);

                    if (emp != null) {
                        request.setAttribute("empleado", emp);
                        request.setAttribute("listaTrabajos",jobDao.obtenerListaTrabajos());
                        request.setAttribute("listaJefes",employeeDao.listarEmpleados());
                        request.setAttribute("listaDepartamentos",departmentDao.lista());
                        view = request.getRequestDispatcher("employees/formularioEditar.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect("home");
                    }

                } else {
                    response.sendRedirect("home");
                }

                break;
            case "borrar":
                if (request.getParameter("id") != null) {
                    String employeeIdString = request.getParameter("id");
                    int employeeId = 0;
                    try {
                        employeeId = Integer.parseInt(employeeIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("home");
                    }

                    Employee emp = employeeDao.obtenerEmpleado(employeeId);

                    if (emp != null) {
                        employeeDao.borrarEmpleado(employeeId);
                    }
                }

                response.sendRedirect("home");
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        EmployeeDao employeeDao = new EmployeeDao();

        Employee employee = new Employee();
        employee.setFirstName(request.getParameter("first_name"));
        employee.setLastName(request.getParameter("last_name"));
        employee.setEmail(request.getParameter("email"));
        employee.setPhoneNumber(request.getParameter("phone"));
        employee.setHireDate(request.getParameter("hire_date"));

        Job job = new Job();
        job.setJobId(request.getParameter("job_id"));
        employee.setJob(job);

        employee.setSalary(new BigDecimal(request.getParameter("salary")));
        employee.setCommissionPct(request.getParameter("commission").equals("") ? null : new BigDecimal(request.getParameter("commission")));

        Employee manager = new Employee();
        manager.setEmployeeId(Integer.parseInt(request.getParameter("manager_id")));
        employee.setManager(manager);

        Department department = new Department();
        department.setDepartmentId(Integer.parseInt(request.getParameter("department_id")));
        employee.setDepartment(department);


        System.out.println("First Name: " + employee.getFirstName());
        System.out.println("Last Name: " + employee.getLastName());
        System.out.println("Email: " + employee.getEmail());
        System.out.println("Phone: " + employee.getPhoneNumber());
        System.out.println("Hire Date: " + employee.getHireDate());
        System.out.println("Job ID: " + employee.getJob().getJobId());
        System.out.println("Salary: " + employee.getSalary());
        System.out.println("Commission: " + employee.getCommissionPct());
        System.out.println("Manager ID: " + employee.getManager().getEmployeeId());
        System.out.println("Department ID: " + employee.getDepartment().getDepartmentId());

        switch (action) {
            case "guardar":
                employeeDao.guardarEmpleado(employee);

                response.sendRedirect("home");
                break;
            case "actualizar":
                employee.setEmployeeId(Integer.parseInt(request.getParameter("employee_id")));

                employeeDao.actualizarEmpleado(employee);

                response.sendRedirect("home");

                break;
        }
    }

}
