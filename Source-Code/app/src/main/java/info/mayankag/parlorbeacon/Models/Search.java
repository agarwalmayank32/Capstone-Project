package info.mayankag.parlorbeacon.Models;

public class Search {

    private String name;
    private String rating;
    private String address;
    private String email;

    public Search(String name, String rating, String address, String email) {
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.email = email;
    }

    /**
     * @return Gets the value of name and returns name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * You can use getName() to get the value of name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Gets the value of rating and returns rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the rating
     * You can use getRating() to get the value of rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return Gets the value of address and returns address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address
     * You can use getAddress() to get the value of address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Gets the value of email and returns email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     * You can use getEmail() to get the value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
