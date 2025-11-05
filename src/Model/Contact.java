package Model;

public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * Constructor for the Contact class.
     *
     * @param contactId The contact ID.
     * @param contactName The contact name.
     * @param contactEmail The contact email.
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Get the contact ID.
     *
     * @return The contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Set the contact ID.
     *
     * @param contactId The contact ID to set.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Get the contact name.
     *
     * @return The contact name.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Set the contact name.
     *
     * @param contactName The contact name to set.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Get the contact email.
     *
     * @return The contact email.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Set the contact email.
     *
     * @param contactEmail The contact email to set.
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}

