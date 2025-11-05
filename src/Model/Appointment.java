package Model;

import java.time.LocalDateTime;

/**

 Represents an appointment.
 */
public class Appointment {

    // Appointment properties
    private final int appointmentId;
    private int customerId;
    private String customerName;
    private String title;
    private String location;
    private String type;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String contactName;
    private final int userId;

    /**

     Constructs an appointment object with the given parameters.

     @param appointmentId The ID of the appointment.
     @param customerId The ID of the customer associated with the appointment.
     @param customerName The name of the customer associated with the appointment.
     @param title The title of the appointment.
     @param location The location of the appointment.
     @param type The type of the appointment.
     @param description The description of the appointment.
     @param start The start time of the appointment.
     @param end The end time of the appointment.
     @param contactName The name of the contact associated with the appointment.
     @param userId The ID of the user associated with the appointment.
     */
    public Appointment(int appointmentId, int customerId, String customerName, String title, String location,
                       String type, String description, LocalDateTime start, LocalDateTime end, String contactName, int userId) {

        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.contactName = contactName;
        this.userId = userId;
    }

    /**

     Gets the ID of the user associated with the appointment.
     @return The user ID.
     */
    public int getUserId() {
        return userId;
    }
    /**

     Gets the ID of the appointment.
     @return The appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }
    /**

     Gets the name of the contact associated with the appointment.
     @return The contact name.
     */
    public String getContactName() {
        return contactName;
    }
    /**

     Gets the name of the customer associated with the appointment.
     @return The customer name.
     */
    public String getCustomerName() {
        return customerName;
    }
    /**

     Gets the ID of the customer associated with the appointment.
     @return The customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }
    /**

     Gets the title of the appointment.
     @return The appointment title.
     */
    public String getTitle() {
        return title;
    }
    /**

     Gets the location of the appointment.
     @return The appointment location.
     */
    public String getLocation() {
        return location;
    }
    /**

     Gets the type of the appointment.
     @return The appointment type.
     */
    public String getType() {
        return type;
    }
    /**

     Gets the description of the appointment.
     @return The appointment description.
     */
    public String getDescription() {
        return description;
    }
    /**

     Gets the start time of the appointment.
     @return The appointment start time.
     */
    public LocalDateTime getStart() {
        return start;
    }
    /**

     Gets the end time of the appointment.
     @return The appointment end time.
     */
    public LocalDateTime getEnd() {
        return end;
    }
    /**

     Returns a string representation of the appointment.
     @return The string representation of the appointment.
     */
    public String toString(){
        return("ID: " + this.getAppointmentId() + " Title: " + this.title + " Type: " + this.type +
                " Description: " + this.description + " Start: " + this.start + " End: " + this.end +
                " CustomerID: " + this.customerId);
    }
}