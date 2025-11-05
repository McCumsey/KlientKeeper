package Model;

public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private String country;
    private int divisionId;

    /**
     * Constructor for the Customer class.
     *
     * @param customerId   The customer ID.
     * @param customerName The customer name.
     * @param address      The customer address.
     * @param postalCode   The customer postal code.
     * @param phoneNumber  The customer phone number.
     * @param division     The customer division.
     * @param country      The customer country.
     * @param divisionId   The ID of the customer's division.
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phoneNumber,
                    String division, String country, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.country = country;
        this.divisionId = divisionId;
    }

    /**
     * Get the customer ID.
     *
     * @return The customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Set the customer ID.
     *
     * @param customerId The customer ID to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the customer name.
     *
     * @return The customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Set the customer name.
     *
     * @param customerName The customer name to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Get the customer address.
     *
     * @return The customer address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the customer address.
     *
     * @param address The customer address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the customer postal code.
     *
     * @return The customer postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Set the customer postal code.
     *
     * @param postalCode The customer postal code to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Get the customer phone number.
     *
     * @return The customer phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the customer phone number.
     *
     * @param phoneNumber The customer phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the customer division.
     *
     * @return The customer division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Set the customer division.
     *
     * @param division The customer division to set.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Get the customer country.
     *
     * @return The customer country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the customer country.
     *
     * @param country The customer country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the ID of the customer's division.
     *
     * @return The ID of the customer's division.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Set the ID of the customer's division.
     *
     * @param divisionId The ID of the customer's division to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
