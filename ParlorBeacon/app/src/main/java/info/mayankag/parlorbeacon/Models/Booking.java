package info.mayankag.parlorbeacon.Models;

@SuppressWarnings("ALL")
public class Booking {

    private String CustomerName;
    private String CustomerEmail;
    private String ServiceName;
    private String ServiceTime;
    private String ServiceStatus;
    private String ServiceDate;

    public Booking(String customerName, String customerEmail, String serviceName, String serviceTime, String serviceDate, String serviceStatus)
    {
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

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public String getServiceTime() {
        return ServiceTime;
    }

    public String getServiceStatus() {
        return ServiceStatus;
    }

    public String getServiceDate() {
        return ServiceDate;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public void setEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public void setServiceTime(String serviceTime) {
        ServiceTime = serviceTime;
    }

    public void setServiceStatus(String serviceStatus) {
        ServiceStatus = serviceStatus;
    }

    public void setServiceDate(String serviceDate) {
        ServiceDate = serviceDate;
    }
}
