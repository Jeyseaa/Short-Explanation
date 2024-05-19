package POS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends JFrame implements ActionListener {
    JButton submit, back, eye, eye1, eye5, eye6;
    JTextField textName, textEmail;
    JPasswordField textPassword, textConfirmPassword;
    JRadioButton adminRadioButton, staffRadioButton;

    Registration() {
        JLabel labelName = new JLabel("Name:");
        labelName.setFont(new Font("Raleway", Font.BOLD, 14));
        labelName.setBounds(400, 160, 100, 30);
        add(labelName);

        textName = new JTextField();
        textName.setFont(new Font("Raleway", Font.PLAIN, 14));
        textName.setBounds(500, 160, 500, 30);
        add(textName);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Raleway", Font.BOLD, 14));
        labelEmail.setBounds(400, 210, 100, 30);
        add(labelEmail);

        textEmail = new JTextField();
        textEmail.setFont(new Font("Raleway", Font.PLAIN, 14));
        textEmail.setBounds(500, 210, 400, 30);
        add(textEmail);

        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setFont(new Font("Raleway", Font.BOLD, 14));
        labelPassword.setBounds(400, 260, 100, 30);
        add(labelPassword);

        textPassword = new JPasswordField();
        textPassword.setFont(new Font("Raleway", Font.PLAIN, 14));
        textPassword.setBounds(500, 260, 400, 30);
        add(textPassword);

        // Radio buttons for role selection
        JLabel labelRole = new JLabel("Role:");
        labelRole.setFont(new Font("Raleway", Font.BOLD, 14));
        labelRole.setBounds(400, 360, 100, 30);
        add(labelRole);

        adminRadioButton = new JRadioButton("Admin");
        adminRadioButton.setFont(new Font("Raleway", Font.PLAIN, 14));
        adminRadioButton.setBounds(500, 360, 100, 30);
        add(adminRadioButton);

        staffRadioButton = new JRadioButton("Staff");
        staffRadioButton.setFont(new Font("Raleway", Font.PLAIN, 14));
        staffRadioButton.setBounds(600, 360, 100, 30);
        add(staffRadioButton);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminRadioButton);
        roleGroup.add(staffRadioButton);

        ImageIcon buttonIcon = new ImageIcon(ClassLoader.getSystemResource("images/itachi.png"));
        eye = new JButton(buttonIcon);
        eye.setText(null);
        eye.setBackground(Color.lightGray);
        eye.setForeground(Color.black);
        eye.setBounds(865, 255, 113, 50);
        eye.addActionListener(this);
        eye.setOpaque(false);
        eye.setContentAreaFilled(false);
        eye.setBorderPainted(false);
        add(eye);

        ImageIcon buttonIcon1 = new ImageIcon(ClassLoader.getSystemResource("images/itachi1.png"));
        eye1 = new JButton(buttonIcon1);
        eye1.setText(null);
        eye1.setBackground(Color.lightGray);
        eye1.setForeground(Color.black);
        eye1.setBounds(865, 260, 113, 50);
        eye1.addActionListener(this);
        eye1.setOpaque(false);
        eye1.setContentAreaFilled(false);
        eye1.setBorderPainted(false);
        add(eye1);
        eye1.setVisible(false);

        JLabel labelConfirmPassword = new JLabel("Confirm Password:");
        labelConfirmPassword.setFont(new Font("Raleway", Font.BOLD, 14));
        labelConfirmPassword.setBounds(400, 310, 150, 30);
        add(labelConfirmPassword);

        textConfirmPassword = new JPasswordField();
        textConfirmPassword.setFont(new Font("Raleway", Font.PLAIN, 14));
        textConfirmPassword.setBounds(550, 310, 350, 30);
        add(textConfirmPassword);

        ImageIcon buttonIcon5 = new ImageIcon(ClassLoader.getSystemResource("images/itachi.png"));
        eye5 = new JButton(buttonIcon5);
        eye5.setText(null);
        eye5.setBackground(Color.lightGray);
        eye5.setForeground(Color.black);
        eye5.setBounds(870, 300, 113, 50);
        eye5.addActionListener(this);
        eye5.setOpaque(false);
        eye5.setContentAreaFilled(false);
        eye5.setBorderPainted(false);
        add(eye5);

        ImageIcon buttonIcon6 = new ImageIcon(ClassLoader.getSystemResource("images/itachi1.png"));
        eye6 = new JButton(buttonIcon6);
        eye6.setText(null);
        eye6.setBackground(Color.lightGray);
        eye6.setForeground(Color.black);
        eye6.setBounds(870, 300, 113, 50);
        eye6.addActionListener(this);
        eye6.setOpaque(false);
        eye6.setContentAreaFilled(false);
        eye6.setBorderPainted(false);
        add(eye6);
        eye6.setVisible(false);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/typing.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1400, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1400, 700);
        add(l3);

        submit = new JButton("SUBMIT");
        submit.setFont(new Font("Raleway", Font.BOLD, 14));
        submit.setBackground(Color.white);
        submit.setForeground(Color.black);
        submit.setBounds(800, 355, 100, 40);
        submit.addActionListener(this);
        l3.add(submit);

        back = new JButton("BACK");
        back.setFont(new Font("Raleway", Font.BOLD, 14));
        back.setBackground(Color.white);
        back.setForeground(Color.black);
        back.setBounds(905, 355, 100, 40);
        back.addActionListener(this);
        l3.add(back);

        getContentPane().setBackground(new Color(135, 206, 250));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            if (validateInput()) {
                saveToDatabase();
                JOptionPane.showMessageDialog(this, "REGISTRATION SUCCESSFUL", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == back) {
            new Homepage();
            this.dispose();
        } else if (e.getSource() == eye || e.getSource() == eye1) {
            togglePasswordVisibility(textPassword, eye, eye1);
        } else if (e.getSource() == eye5 || e.getSource() == eye6) {
            togglePasswordVisibility(textConfirmPassword, eye5, eye6);
        }
    }

    private void togglePasswordVisibility(JPasswordField passwordField, JButton eyeButton, JButton eye1Button) {
        if (passwordField.getEchoChar() == '•') {
            passwordField.setEchoChar((char) 0);
            eyeButton.setVisible(false);
            eye1Button.setVisible(true);
        } else {
            passwordField.setEchoChar('•');
            eyeButton.setVisible(true);
            eye1Button.setVisible(false);
        }
    }

    private boolean validateInput() {
        String name = textName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isAlphabetic(name)) {
            JOptionPane.showMessageDialog(this, "Name should contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String email = textEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        char[] password = textPassword.getPassword();
        char[] confirmPassword = textConfirmPassword.getPassword();
        if (password.length == 0 || confirmPassword.length == 0) {
            JOptionPane.showMessageDialog(this, "Password fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isStrongPassword(password)) {
            JOptionPane.showMessageDialog(this, "Weak password. Make it stronger.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!Arrays.equals(password, confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if a role is selected
        if (!adminRadioButton.isSelected() && !staffRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a role.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isStrongPassword(char[] password) {
        String passwordString = new String(password);

        if (passwordString.replaceAll("[^A-Z]", "").length() != 1) {
            JOptionPane.showMessageDialog(this, "Password should contain exactly 1 capital letter.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (passwordString.replaceAll("[^a-z]", "").length() < 5) {
            JOptionPane.showMessageDialog(this, "Password should contain at least 5 small letters.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!passwordString.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            JOptionPane.showMessageDialog(this, "Password should contain at least 1 special character.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (passwordString.replaceAll("[^0-9]", "").length() < 1) {
            JOptionPane.showMessageDialog(this, "Password should contain at least 1 number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.length < 9) {
            JOptionPane.showMessageDialog(this, "Password should be at least 9 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isAlphabetic(String input) {
        return input.matches("[a-zA-Z ]+");
    }

    private void saveToDatabase() {
        String databaseUrl = "jdbc:mysql://localhost:3306/Authentication";
        String login = "root";
        String password = "qwerty123";

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, textName.getText().trim());
            preparedStatement.setString(2, textEmail.getText().trim());
            preparedStatement.setString(3, new String(textPassword.getPassword()));

            // Save role
            String role = adminRadioButton.isSelected() ? "admin" : "staff";
            preparedStatement.setString(4, role);

            preparedStatement.executeUpdate();

            openHomepage();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openHomepage() {
        new Homepage();
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registration::new);
    }
}

