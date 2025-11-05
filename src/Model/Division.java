package Model;

public class Division {
    private int divisionId;
    private String division;
    private int countryId;

    /**
     * Constructor for the Division class.
     *
     * @param divisionId The division ID.
     * @param division   The division name.
     * @param countryId  The ID of the country associated with the division.
     */
    public Division(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Get the division ID.
     *
     * @return The division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Set the division ID.
     *
     * @param divisionId The division ID to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Get the division name.
     *
     * @return The division name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Set the division name.
     *
     * @param division The division name to set.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Get the ID of the country associated with the division.
     *
     * @return The ID of the country.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Set the ID of the country associated with the division.
     *
     * @param countryId The ID of the country to set.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}