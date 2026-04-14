package usc.edu.bustrackerfinal;

public class Route {
    private String routeCode, terminalStart, terminalEnd, distance, status, departureTime, plateNumber, assignedDriver, assignedConductor, contactInfo;

    public Route() {} // Required for Firebase

    public Route(String routeCode, String terminalStart, String terminalEnd, String distance, String status, String departureTime, String plateNumber, String assignedDriver, String assignedConductor, String contactInfo) {
        this.routeCode = routeCode;
        this.terminalStart = terminalStart;
        this.terminalEnd = terminalEnd;
        this.distance = distance;
        this.status = status;
        this.departureTime = departureTime;
        this.plateNumber = plateNumber;
        this.assignedDriver = assignedDriver;
        this.assignedConductor = assignedConductor;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public String getRouteCode() { return routeCode; }
    public void setRouteCode(String routeCode) { this.routeCode = routeCode; }
    public String getTerminalStart() { return terminalStart; }
    public void setTerminalStart(String terminalStart) { this.terminalStart = terminalStart; }
    public String getTerminalEnd() { return terminalEnd; }
    public void setTerminalEnd(String terminalEnd) { this.terminalEnd = terminalEnd; }
    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getAssignedDriver() { return assignedDriver; }
    public void setAssignedDriver(String assignedDriver) { this.assignedDriver = assignedDriver; }
    public String getAssignedConductor() { return assignedConductor; }
    public void setAssignedConductor(String assignedConductor) { this.assignedConductor = assignedConductor; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}