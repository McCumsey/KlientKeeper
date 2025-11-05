package Model;

public class Country {

    private int countryId;
    private String country;

    /**
     * Constructor for the Country class.
     *
     * @param countryId The country ID.
     * @param country The country name.
     */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     * Get the country ID.
     *
     * @return The country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Set the country ID.
     *
     * @param countryId The country ID to set.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Get the country name.
     *
     * @return The country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country name.
     *
     * @param country The country name to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
