import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HospitalInfoSystem {

    private static final String DB_URL = "jdbc:mysql://localhost/hospital";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password"; // Change to your password

    private Connection connection;

    private JFrame frame;
    private JTextArea textArea;
    private JTextField patientNameField;
    private JTextField appointmentDateField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalInfoSystem().createAndShowGUI());
    }

    public HospitalInfoSystem() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Hospital Information System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Patient Name:");
        patientNameField = new JTextField();

        JLabel appointmentLabel = new JLabel("Appointment Date:");
        appointmentDateField = new JTextField();

        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Patients");
        JButton addAppointmentButton = new JButton("Add Appointment");
        JButton viewAppointmentButton = new JButton("View Appointments");

        textArea = new JTextArea();
        textArea.setEditable(false);

        panel.add(nameLabel);
        panel.add(patientNameField);
        panel.add(appointmentLabel);
        panel.add(appointmentDateField);
        panel.add(addButton);
        panel.add(viewButton);
        panel.add(addAppointmentButton);
        panel.add(viewAppointmentButton);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        addButton.addActionListener(e -> addPatient());
        viewButton.addActionListener(e -> viewPatients());
        addAppointmentButton.addActionListener(e -> addAppointment());
        viewAppointmentButton.addActionListener(e -> viewAppointments());

        frame.setVisible(true);
    }

    private void addPatient() {
        String name = patientNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Patient name cannot be empty.");
            return;
        }
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO patients (name) VALUES (?)");
            stmt.setString(1, name);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Patient added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewPatients() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM patients");
            textArea.setText("Patients:\n");
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAppointment() {
        String name = patientNameField.getText();
        String date = appointmentDateField.getText();
        if (name.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Patient name and appointment date cannot be empty.");
            return;
        }
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointments (patient_name, appointment_date) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, date);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Appointment added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewAppointments() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM appointments");
            textArea.setText("Appointments:\n");
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("id") + ", Patient Name: " + rs.getString("patient_name") + ", Date: " + rs.getString("appointment_date") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
