package usc.edu.bustrackerfinal;

public class Employee {
    private String id, name, role;
    public Employee(String id, String name, String role) {
        this.id = id; this.name = name; this.role = role;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
