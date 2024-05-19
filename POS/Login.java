package POS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton eye, eye1, back, exit, enter;
    JPasswordField textPassword;
    JTextField textEmail;
    private int user_id = -1;
    private String userName;
    private String userRole;

    Login() {

        JLabel labelName = new JLabel("Welcome to J & A's");
        labelName.setFont(new Font("COURIER", Font.BOLD, 30));
        labelName.setForeground(Color.BLACK);
        labelName.setBounds(150, 50, 400, 60);
        add(labelName);

        JLabel labelName1 = new JLabel("Bulalo at Tapsihan");
        labelName1.setFont(new Font("Raleway", Font.PLAIN, 15));
        labelName1.setForeground(Color.BLACK);
        labelName1.setBounds(200, 85, 400, 60);
        add(labelName1);


        textEmail = new JTextField("Email");
        textEmail.setFont(new Font("Raleway", Font.PLAIN, 14));
        textEmail.setBounds(150, 250, 340, 30);
        add(textEmail);

        textEmail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (textEmail.getText().equals("Email")) {
                    textEmail.setText("");
                }
            }
        });


        textPassword = new JPasswordField("Password");
        textPassword.setFont(new Font("Raleway", Font.PLAIN, 14));
        textPassword.setBounds(150, 300, 280, 30);
        add(textPassword);


        textPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (String.valueOf(textPassword.getPassword()).equals("Password")) {
                    textPassword.setText("");
                }
            }
        });
        ImageIcon i21 = new ImageIcon(ClassLoader.getSystemResource("images/hacker.png"));
        Image i22 = i21.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon i23 = new ImageIcon(i22);
        JLabel l23 = new JLabel(i23);
        l23.setBounds(40, 30, 150, 150);
        add(l23);

        ImageIcon buttonIcon = new ImageIcon(ClassLoader.getSystemResource("images/itachi.png"));
        eye = new JButton(buttonIcon);
        eye.setText(null);
        eye.setBackground(Color.lightGray);
        eye.setForeground(Color.black);
        eye.setBounds(400, 300, 113, 50);
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
        eye1.setBounds(400, 300, 113, 50);
        eye1.addActionListener(this);
        eye1.setOpaque(false);
        eye1.setContentAreaFilled(false);
        eye1.setBorderPainted(false);
        add(eye1);
        eye1.setVisible(false);

        exit = new JButton("EXIT");
        exit.setBounds(350, 358, 72, 54);
        exit.setBackground(Color.white);
        exit.setForeground(Color.black);
        exit.addActionListener(this);

        add(exit);

        back = new JButton("BACK");
        back.setBounds(150, 358, 72, 54);
        back.setBackground(Color.white);
        back.setForeground(Color.black);
        back.addActionListener(this);
        add(back);

        enter = new JButton("LOGIN");
        enter.setBounds(250, 358, 72, 54);
        enter.setBackground(Color.white);
        enter.setForeground(Color.black);
        enter.addActionListener(this);
        add(enter);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("images/message.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l6 = new JLabel(i6);
        l6.setBounds(100, 240, 50, 50);
        add(l6);

        ImageIcon i41 = new ImageIcon(ClassLoader.getSystemResource("images/reset.png"));
        Image i42 = i41.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i43 = new ImageIcon(i42);
        JLabel l43 = new JLabel(i43);
        l43.setBounds(100, 290, 50, 50);
        add(l43);

      

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/fingerprint.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 720, Image.SCALE_SMOOTH); // Adjusted size
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(620, 0, 800, 720); // Adjusted bounds
        add(l3);
        

        getContentPane().setBackground(new Color(153, 204   , 255));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    private boolean authenticateUser(String email, String password) {
        try {
            String databaseUrl = "jdbc:mysql://localhost:3306/Authentication";
            String login = "root";
            String dbPassword = "qwerty123";
    
            Connection connection = DriverManager.getConnection(databaseUrl, login, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT id, name, role FROM users WHERE email = '" + email + "' AND password = '" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);
    
            if (resultSet.next()) {
                user_id = resultSet.getInt("id"); // Assign the value to user_id, not id
                userName = resultSet.getString("name");
                userRole = resultSet.getString("role");
                resultSet.close();
                statement.close();
                connection.close();
    
                if ("admin".equals(userRole)) {
                    new Admin(true); // Replace AdminDashboard with the actual class for admin interface
                } else if ("staff".equals(userRole)) {
                    new Staff(false); // Replace StaffDashboard with the actual class for staff interface
                }
    
                dispose();
                return true;
            } else {
                resultSet.close();
                statement.close();
                connection.close();
                return false;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
    

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            new Homepage();
            this.dispose();
        } else if (e.getSource() == enter) {
            String email = textEmail.getText();
            String password = new String(textPassword.getPassword());

            if (authenticateUser(email, password)) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password");
            }
        } else if (e.getSource() == exit) {
            dispose();
        } else if (e.getSource() == eye || e.getSource() == eye1) {
            if (textPassword.getEchoChar() == '•') {
                textPassword.setEchoChar((char) 0);
                eye.setVisible(false);
                eye1.setVisible(true);
            } else {
                textPassword.setEchoChar('•');
                eye.setVisible(true);
                eye1.setVisible(false);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Login();
    }

    public int getUserID() {
        return user_id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }
}