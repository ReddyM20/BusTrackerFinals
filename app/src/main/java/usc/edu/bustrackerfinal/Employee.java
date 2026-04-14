package usc.edu.bustrackerfinal;

public class Employee {
    private String id, name, role, unit, email, address, license, contact, password, status;

    // Empty constructor required for Firebase
    public Employee() {}

    // Constructor for all fields
    public Employee(String id, String name, String role, String unit, String email,
                    String address, String license, String contact, String password, String status) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.unit = unit;
        this.email = email;
        this.address = address;
        this.license = license;
        this.contact = contact;
        this.password = password;
        this.status = status;
    }

    // Getters for all fields (Firebase uses these to write data)
    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getUnit() { return unit; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getLicense() { return license; }
    public String getContact() { return contact; }
    public String getPassword() { return password; }
    public String getStatus() { return status; }
}
