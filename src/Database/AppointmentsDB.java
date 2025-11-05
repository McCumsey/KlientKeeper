package Database;

import Controller.AppointmentsController;
import Controller.ReportsController;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.StringJoiner;


public class AppointmentsDB {

    /**
     * Retrieves a list of all appointments from the database.
     *
     * @return an ObservableList of all Appointment objects.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<Appointment> getAppointments() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String queryStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID INNER JOIN customers ON a.Customer_ID=customers.Customer_ID;"; //

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }


    /**
     * Used to filter appointments within the next month from the database.
     *
     * @return an ObservableList of Appointment objects within the next month.
     * @throws SQLException if a database access error occurs.
     */
     public static ObservableList<Appointment> getAppointmentsMonth() throws SQLException {
         ObservableList<Appointment> appointments = FXCollections.observableArrayList();

         LocalDateTime todaysDate = LocalDateTime.now();
         LocalDateTime nextMonth = todaysDate.plusDays(30);

         String queryStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID INNER JOIN customers ON a.Customer_ID=customers.Customer_ID WHERE Start > ? AND End < ?;";

         Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
         PreparedStatement preparedStatement = Helper.getPreparedStatement();

         preparedStatement.setDate(1, Date.valueOf(todaysDate.toLocalDate()));
         preparedStatement.setDate(2, Date.valueOf(nextMonth.toLocalDate()));

         try {
             preparedStatement.execute();
             ResultSet rs = preparedStatement.getResultSet();

             while (rs.next()) {
                 int appointmentID = rs.getInt("Appointment_ID");
                 int custID = rs.getInt("Customer_ID");
                 String cusName = rs.getString("Customer_Name");
                 String title = rs.getString("Title");
                 String location = rs.getString("Location");
                 String type = rs.getString("Type");
                 String description = rs.getString("Description");
                 LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                 LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                 int userID = rs.getInt("User_ID");
                 String conName = rs.getString("Contact_Name");

                 appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                         description, start, end, conName, userID));
             }
             return appointments;
         } catch (Exception e) {
             System.out.println("Error: " + e.getMessage());
             return null;
         }
     }

    /**
     * Used to filter appointments within the next week from the database.
     *
     * @return an ObservableList of Appointment objects within the next week.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<Appointment> getAppointmentsWeek() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        LocalDateTime todaysDate = LocalDateTime.now();
        LocalDateTime nextWeek = todaysDate.plusDays(7);

        java.lang.String queryStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID INNER JOIN customers ON a.Customer_ID=customers.Customer_ID WHERE Start > ? AND End < ?;";

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setDate(1, Date.valueOf(todaysDate.toLocalDate()));
        preparedStatement.setDate(2, Date.valueOf(nextWeek.toLocalDate()));

        try {
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID));
            }
            return appointments;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a new appointment in the database.
     *
     * @param contactName   the name of the contact associated with the appointment.
     * @param title         the title of the appointment.
     * @param description   the description of the appointment.
     * @param location      the location of the appointment.
     * @param type          the type of the appointment.
     * @param start         the start date and time of the appointment.
     * @param end           the end date and time of the appointment.
     * @param customerName  the name of the customer associated with the appointment.
     * @param userID        the ID of the user creating the appointment.
     * @return true if the appointment is created successfully, false otherwise.
     */
    public static boolean createAppointment(String contactName, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, String customerName, Integer userID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Contact contact = ContactsDB.getContactId(contactName);
            Customer customer = CustomersDB.getCustomerId(customerName);

            String insertStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, Contact_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            connection = JDBC.getConnection();
            preparedStatement = connection.prepareStatement(insertStatement);

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
            preparedStatement.setInt(7, customer.getCustomerId());
            preparedStatement.setInt(8, contact.getContactId());
            preparedStatement.setInt(9, userID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rows affected: " + rowsAffected);
            } else {
                System.out.println("No change");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointmentId the ID of the appointment to be deleted.
     * @return true if the appointment is deleted successfully, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public static boolean deleteAppointment(int appointmentId) throws SQLException {
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID=?";
        Connection connection = JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);

        try {
            preparedStatement.setInt(1, appointmentId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rows affected: " + rowsAffected);
            } else {
                System.out.println("No change");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Modifies an existing appointment in the database.
     *
     * @param contactName   the name of the contact associated with the appointment.
     * @param title         the title of the appointment.
     * @param description   the description of the appointment.
     * @param location      the location of the appointment.
     * @param type          the type of the appointment.
     * @param start         the start date and time of the appointment.
     * @param end           the end date and time of the appointment.
     * @param customerName  the name of the customer associated with the appointment.
     * @param userID        the ID of the user modifying the appointment.
     * @param appointmentID the ID of the appointment to be modified.
     * @return true if the appointment is modified successfully, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public static boolean modifyAppointment(String contactName, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, String customerName, Integer userID, Integer appointmentID) throws SQLException {
        Contact contact = ContactsDB.getContactId(contactName);
        Customer customer = CustomersDB.getCustomerId(customerName);

        String updateStatement = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Customer_ID=?, Contact_ID=?, User_ID=? WHERE Appointment_ID=?";

        Connection connection = JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);

        try {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
            preparedStatement.setInt(7, customer.getCustomerId());
            preparedStatement.setInt(8, contact.getContactId());
            preparedStatement.setInt(9, userID);
            preparedStatement.setInt(10, appointmentID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rows affected: " + rowsAffected);
            } else {
                System.out.println("No change");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of appointments associated with a specific customer from the database.
     *
     * @param customerName the name of the customer.
     * @return an ObservableList of Appointment objects associated with the customer.
     * @throws SQLException if a database access error occurs.
     */
      public static ObservableList<Appointment> getAppointmentsByCustomerName(String customerName) throws SQLException {
      String sql = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID INNER JOIN customers AS z ON a.Customer_ID=z.Customer_ID WHERE Customer_Name=?;";
      StringBuilder reportString = new StringBuilder("APPOINTMENTS BY CUSTOMER + \n");
      ObservableList<Appointment> appointments = FXCollections.observableArrayList();
      ObservableList<Customer> customers = CustomersDB.getCustomers();

      try {
      PreparedStatement ps = JDBC.connection.prepareStatement(sql);
          ps.setString(1, customerName);
      ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              int appointmentID = rs.getInt("Appointment_ID");
              int custID = rs.getInt("Customer_ID");
              String cusName = rs.getString("Customer_Name");
              String title = rs.getString("Title");
              String location = rs.getString("Location");
              String type = rs.getString("Type");
              String description = rs.getString("Description");
              LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
              LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
              int userID = rs.getInt("User_ID");
              String conName = rs.getString("Contact_Name");

              appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                      description, start, end, conName, userID));
          }

      } catch (SQLException e) {
      System.out.println("ERROR: " + e.getMessage());
      e.printStackTrace();
      }
      return appointments;
      }


}


