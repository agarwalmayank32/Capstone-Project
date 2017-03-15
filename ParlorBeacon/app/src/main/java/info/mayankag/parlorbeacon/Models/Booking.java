package info.mayankag.parlorbeacon.Models;

@SuppressWarnings("ALL")
public class Booking {

    private String CustomerName;
    private String CustomerEmail;
    private String ServiceName;
    private String ServiceTime;
    private String ServiceStatus;
    private String ServiceDate;

    public Booking(String customerName, String customerEmail, String serviceName, String serviceTime, String serviceDate, String serviceStatus) {
        CustomerName = customerName;
        CustomerEmail = customerEmail;
        ServiceName = serviceName;
        ServiceTime = serviceTime;
        ServiceDate = serviceDate;
        ServiceStatus = serviceStatus;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceTime() {
        return ServiceTime;
    }

    public void setServiceTime(String serviceTime) {
        ServiceTime = serviceTime;
    }

    public String getServiceStatus() {
        return ServiceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        ServiceStatus = serviceStatus;
    }

    public String getServiceDate() {
        return ServiceDate;
    }

    public void setServiceDate(String serviceDate) {
        ServiceDate = serviceDate;
    }

    public void setEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }
}
