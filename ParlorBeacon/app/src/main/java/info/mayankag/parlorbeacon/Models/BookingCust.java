package info.mayankag.parlorbeacon.Models;

public class BookingCust {

    private String shopEmail;
    private String serviceName;
    private String serviceTime;
    private String serviceDate;
    private String custPhone;
    private String custEmail;
    private String custName;

    public BookingCust(String shopEmail, String serviceName, String serviceTime, String serviceDate, String custPhone, String custEmail, String custName) {
        this.shopEmail = shopEmail;
        this.serviceName = serviceName;
        this.serviceTime = serviceTime;
        this.serviceDate = serviceDate;
        this.custPhone = custPhone;
        this.custEmail = custEmail;
        this.custName = custName;
    }

    /**
     * @return Gets the value of shopEmail and returns shopEmail
     */
    public String getShopEmail() {
        return shopEmail;
    }

    /**
     * Sets the shopEmail
     * You can use getShopEmail() to get the value of shopEmail
     */
    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    /**
     * @return Gets the value of serviceName and returns serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the serviceName
     * You can use getServiceName() to get the value of serviceName
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return Gets the value of serviceTime and returns serviceTime
     */
    public String getServiceTime() {
        return serviceTime;
    }

    /**
     * Sets the serviceTime
     * You can use getServiceTime() to get the value of serviceTime
     */
    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     * @return Gets the value of serviceDate and returns serviceDate
     */
    public String getServiceDate() {
        return serviceDate;
    }

    /**
     * Sets the serviceDate
     * You can use getServiceDate() to get the value of serviceDate
     */
    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * @return Gets the value of custPhone and returns custPhone
     */
    public String getCustPhone() {
        return custPhone;
    }

    /**
     * Sets the custPhone
     * You can use getCustPhone() to get the value of custPhone
     */
    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    /**
     * @return Gets the value of custEmail and returns custEmail
     */
    public String getCustEmail() {
        return custEmail;
    }

    /**
     * Sets the custEmail
     * You can use getCustEmail() to get the value of custEmail
     */
    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    /**
     * @return Gets the value of custName and returns custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * Sets the custName
     * You can use getCustName() to get the value of custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }
}
