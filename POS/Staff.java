package POS;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Staff extends JFrame {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 150;
    private static final int BUTTON_MARGIN = 50;
    private int rowCount = 0;
    private int colCount = 0;
    private JPanel backgroundPanel;
    private List<String> addedItems;
    private JTextPane textArea;
    private DecimalFormat df;
    private double subtotal;
    private JLabel subtotalLabel;
    private int selectedItemStart;
    private int selectedItemEnd;
    private JButton removeButton;
    private JTextField itemNameTextField;
    private JTextField itemPriceTextField;
    private List<JButton> uploadButtons = new ArrayList<>();
    private Map<String, JButton> itemButtons = new HashMap<>();

    public Staff(boolean isAdmin) {
        setTitle("Cashier Point of Sale");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addedItems = new ArrayList<>();
        df = new DecimalFormat("0.00");

        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bgImage = new ImageIcon("images/restobg.jpg").getImage();
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        textArea = new JTextPane();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1),
                new EmptyBorder(5, 5, 5, 5)));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(1025, 30, 320, 350);
        backgroundPanel.add(scrollPane);

        removeButton = createStyledButton("Remove Item", 1025, 400);
        backgroundPanel.add(removeButton);

        removeButton.addActionListener(e -> removeSelectedItem());
        removeButton.setEnabled(false);

        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                highlightSelectedItem(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (textArea.getSelectedText() == null) {
                    removeButton.setEnabled(false);
                }
            }
        });

        JButton clearButton = createStyledButton("Collect All", 1200, 400);
        clearButton.addActionListener(e -> clearAllItems());
        backgroundPanel.add(clearButton);

        subtotalLabel = new JLabel("Subtotal: ₱0.00");
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subtotalLabel.setForeground(Color.BLACK);
        subtotalLabel.setBounds(1125, 470, 200, 30);
        backgroundPanel.add(subtotalLabel);

        JButton printButton = createStyledButton("Print Receipt", 1115, 510);
        printButton.addActionListener(e -> printReceipt());
        backgroundPanel.add(printButton);

        JLabel itemNameLabel = new JLabel("Enter the Item to add:");
        itemNameLabel.setBounds(30, 650, 150, 30);
        backgroundPanel.add(itemNameLabel);

        itemNameTextField = new JTextField();
        itemNameTextField.setBounds(180, 650, 200, 30);
        backgroundPanel.add(itemNameTextField);
        itemNameTextField.setFont(new Font("Arial", Font.BOLD, 14));
        itemNameTextField.setForeground(Color.WHITE);
        itemNameTextField.setBackground(Color.WHITE);
       

        itemNameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemNameTextField.setBackground(Color.white);
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                itemNameTextField.setBackground(Color.white);
            }
        });
        
        if (!isAdmin) {
            itemNameTextField.setEnabled(false); // Disable deleteButton for staff login
        }

        itemNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume(); // Ignore input
                    JOptionPane.showMessageDialog(null, "Please enter letters only", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel itemPriceLabel = new JLabel("Enter the item price:");
        itemPriceLabel.setBounds(400, 650, 150, 30);
        backgroundPanel.add(itemPriceLabel);

        itemPriceTextField = new JTextField();
        itemPriceTextField.setBounds(550, 650, 100, 30);
        backgroundPanel.add(itemPriceTextField);
        itemPriceTextField.setFont(new Font("Arial", Font.BOLD, 14));
        itemPriceTextField.setForeground(Color.WHITE);
        itemPriceTextField.setBackground(Color.WHITE);
       

        itemPriceTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPriceTextField.setBackground(Color.white);
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                itemPriceTextField.setBackground(Color.white);
            }
        });
        
        if (!isAdmin) {
            itemPriceTextField.setEnabled(false); // Disable deleteButton for staff login
        }


        itemPriceTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume(); // Ignore input
                    JOptionPane.showMessageDialog(null, "Please enter numbers only", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton uploadButton = new JButton("Upload");
        uploadButton.setBounds(680, 650, 100, 30);
        backgroundPanel.add(uploadButton);
        backgroundPanel.add(uploadButton);
        uploadButton.setFont(new Font("Arial", Font.BOLD, 14));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setBackground(Color.BLACK);
        uploadButton.setFocusPainted(false);
        uploadButton.setBorderPainted(false);

        uploadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                uploadButton.setBackground(Color.DARK_GRAY);
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                uploadButton.setBackground(Color.BLACK);
            }
        });
        
        if (!isAdmin) {
            uploadButton.setEnabled(false); // Disable deleteButton for staff login
        }

        uploadButton.addActionListener(e -> {
            String itemName = itemNameTextField.getText().toLowerCase(); // Normalize to lowercase
            String itemPrice = itemPriceTextField.getText();

            if (itemName.isEmpty() || itemPrice.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill up all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (addedItems.contains(itemName)) {
                JOptionPane.showMessageDialog(null, "Item already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addedItems.add(itemName);

            if (rowCount * 4 + colCount >= 12) {
                JOptionPane.showMessageDialog(null, "Maximum photos limit reached", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String imagePath = "images/" + itemName + ".png";

            int x = 30 + (BUTTON_WIDTH + BUTTON_MARGIN) * colCount;
            int y = 30 + (BUTTON_HEIGHT + BUTTON_MARGIN) * rowCount;

            if (colCount >= 4) {
                colCount = 0;
                rowCount++;
                x = 30;
                y += BUTTON_HEIGHT + BUTTON_MARGIN;
            }

            JButton newItemButton = createButton(imagePath, itemName + " - ₱" + itemPrice, x, y);
            backgroundPanel.add(newItemButton);

            colCount++;

            validate();
            repaint();

            JOptionPane.showMessageDialog(null, "Item Successfully Added", "Success", JOptionPane.INFORMATION_MESSAGE);

            itemNameTextField.setText("");
            itemPriceTextField.setText("");

            // Add item to database
            Database.addItem(itemName, Double.parseDouble(itemPrice));
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(800, 650, 100, 30);
        backgroundPanel.add(deleteButton);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.BLACK);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        backgroundPanel.add(deleteButton);
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteButton.setBackground(Color.DARK_GRAY);
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                deleteButton.setBackground(Color.BLACK);
            }
        });
        
        if (!isAdmin) {
            deleteButton.setEnabled(false); // Disable deleteButton for staff login
        }

        deleteButton.addActionListener(e -> {
            String itemName = itemNameTextField.getText().toLowerCase(); // Normalize to lowercase
            if (itemName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter the item name to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!addedItems.contains(itemName)) {
                JOptionPane.showMessageDialog(null, "Item does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addedItems.remove(itemName);
            Database.deleteItem(itemName);

            JButton itemButton = itemButtons.remove(itemName);
            if (itemButton != null) {
                backgroundPanel.remove(itemButton);
                backgroundPanel.revalidate();
                backgroundPanel.repaint();
            }

            JOptionPane.showMessageDialog(null, "Item Successfully Deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(920, 650, 100, 30);
        backgroundPanel.add(updateButton);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(Color.BLACK);
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        backgroundPanel.add(updateButton);
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateButton.setBackground(Color.DARK_GRAY);
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                updateButton.setBackground(Color.BLACK);
            }
        });
        
        if (!isAdmin) {
            updateButton.setEnabled(false); // Disable deleteButton for staff login
        }
        updateButton.addActionListener(e -> {
            String itemName = itemNameTextField.getText().toLowerCase(); // Normalize to lowercase
            String itemPrice = itemPriceTextField.getText();

            if (itemName.isEmpty() || itemPrice.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill up all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!addedItems.contains(itemName)) {
                JOptionPane.showMessageDialog(null, "Item does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double newItemPrice = Double.parseDouble(itemPrice);
            Database.updateItem(itemName, newItemPrice);

            JButton itemButton = itemButtons.get(itemName);
            if (itemButton != null) {
                String imagePath = "images/" + itemName + ".png";
                String newLabelText = itemName + " - ₱" + itemPrice;
                updateButtonImage(itemButton, imagePath, newLabelText);
            }

            JOptionPane.showMessageDialog(null, "Item Successfully Updated", "Success", JOptionPane.INFORMATION_MESSAGE);

            itemNameTextField.setText("");
            itemPriceTextField.setText("");
        });

        loadItemsFromDatabase(isAdmin);
        setVisible(true);
        JButton logoutButton = createStyledButton("Logout", 1200, 640);
        backgroundPanel.add(logoutButton);
    
        logoutButton.addActionListener(e -> {
            dispose(); // Close the current window
            // Open the login window
            SwingUtilities.invokeLater(Login::new);
        });
    }

    private void loadItemsFromDatabase(boolean isAdmin) {
        List<Item> items = null;
        try {
            items = Database.getAllItems();
            for (Item item : items) {
                String imagePath = "images/" + item.getName().toLowerCase() + ".png"; // Normalize to lowercase
                int x = 30 + (BUTTON_WIDTH + BUTTON_MARGIN) * colCount;
                int y = 30 + (BUTTON_HEIGHT + BUTTON_MARGIN) * rowCount;

                JButton newItemButton = createButton(imagePath, item.getName() + " - ₱" + item.getPrice(), x, y);
                backgroundPanel.add(newItemButton);

                colCount++;

                if (colCount >= 4) {
                    colCount = 0;
                    rowCount++;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading items from the database", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void reloadItemsFromDatabase(boolean isAdmin) {
        backgroundPanel.removeAll();
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
        rowCount = 0;
        colCount = 0;
        addedItems.clear();
        loadItemsFromDatabase(isAdmin);
    }

    private JButton createButton(String imagePath, String labelText, int x, int y) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setBounds(0, BUTTON_HEIGHT, BUTTON_WIDTH, 30);

        JButton button = new JButton();
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setLayout(null);
        button.add(imageLabel);
        button.add(textLabel);
        button.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT + 30);

        button.addActionListener(e -> {
            String[] parts = labelText.split(" - ");
            if (parts.length >= 2) {
                String itemName = parts[0];
                String priceStr = parts[1].replaceAll("[^0-9.]", ""); // Remove non-numeric characters
                double itemPrice = Double.parseDouble(priceStr);
                addedItems.add(itemName.toLowerCase()); // Normalize to lowercase
                updateTextArea(itemName, itemPrice);
                updateSubtotal(itemPrice);
                JOptionPane.showMessageDialog(null, "Item Successfully Added", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println("Invalid format for item button's text: " + labelText);
            }
        });

        uploadButtons.add(button);
        itemButtons.put(labelText.split(" - ")[0].toLowerCase(), button); // Map item name to button
        return button;
    }

    private void updateButtonImage(JButton button, String imagePath, String labelText) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setBounds(0, BUTTON_HEIGHT, BUTTON_WIDTH, 30);

        button.removeAll();
        button.add(imageLabel);
        button.add(textLabel);
        button.revalidate();
        button.repaint();
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBounds(x, y, 150, 50);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }

    private void removeSelectedItem() {
        if (selectedItemStart >= 0 && selectedItemEnd >= 0) {
            try {
                int start = Math.min(selectedItemStart, selectedItemEnd);
                int end = Math.max(selectedItemStart, selectedItemEnd);
                int documentLength = textArea.getDocument().getLength();
                end = Math.min(end, documentLength); // Ensure end is within valid range

                String clickedItem = textArea.getDocument().getText(start, end - start);
                String[] parts = clickedItem.split(" - ");
                if (parts.length >= 2) {
                    double price = Double.parseDouble(parts[1].replace("₱", "").trim());
                    subtotal -= price;
                    subtotalLabel.setText("Subtotal: ₱" + df.format(subtotal));

                    // Remove the clicked item from the text area
                    textArea.getDocument().remove(start, end - start);

                    selectedItemStart = -1;
                    selectedItemEnd = -1;

                    removeButton.setEnabled(false); // Disable remove button
                } else {
                    System.err.println("Invalid format for clicked item: " + clickedItem);
                }
            } catch (BadLocationException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void highlightSelectedItem(MouseEvent e) {
        int offset = textArea.viewToModel(e.getPoint());
        Element root = textArea.getDocument().getDefaultRootElement();
        int line = root.getElementIndex(offset);
        Element lineElement = root.getElement(line);
        selectedItemStart = lineElement.getStartOffset();
        selectedItemEnd = lineElement.getEndOffset();
        textArea.select(selectedItemStart, selectedItemEnd);
        removeButton.setEnabled(selectedItemStart >= 0 && selectedItemEnd >= 0);
    }

    private void clearAllItems() {
        textArea.setText("");
        subtotal = 0.0;
        subtotalLabel.setText("Subtotal: ₱0.00");
        selectedItemStart = -1;
        selectedItemEnd = -1;
        removeButton.setEnabled(false);
    }

    private void printReceipt() {
        String[] items = textArea.getText().split("\n");
        double total = subtotal;
        StringBuilder receiptContent = new StringBuilder("Receipt:\n\n");
        for (String item : items) {
            receiptContent.append("<hr>\n");
            receiptContent.append(item).append("\n");
        }
        receiptContent.append("<hr>\n");
        receiptContent.append("\nSubtotal: ₱").append(df.format(subtotal)).append("\n");
        double tax = subtotal * 0.12;
        receiptContent.append("<hr>\n");
        receiptContent.append("Tax (12%): ₱").append(df.format(tax)).append("\n");
        total += tax;
        receiptContent.append("<hr>\n");
        receiptContent.append("Total: ₱").append(df.format(total));
        JOptionPane.showMessageDialog(this, formatReceipt(receiptContent.toString()));
    }

    private String formatReceipt(String items) {
        StringBuilder formattedReceipt = new StringBuilder("<html><body style='width: 250px;'>");
        formattedReceipt.append("<h2 style='text-align:center;'>Receipt</h2>");
        formattedReceipt.append("<hr>");
        formattedReceipt.append("<p>").append(items.replace("\n", "<br>")).append("</p>");
        formattedReceipt.append("</body></html>");
        return formattedReceipt.toString();
    }

    private void updateTextArea(String itemName, double itemPrice) {
        String itemInfo = itemName + " - ₱" + df.format(itemPrice);
        String currentText = textArea.getText();
        if (!currentText.isEmpty()) {
            currentText += "\n";
        }
        currentText += itemInfo;
        textArea.setText(currentText);
    }

    private void updateSubtotal(double itemPrice) {
        subtotal += itemPrice;
        subtotalLabel.setText("Subtotal: ₱" + df.format(subtotal));
    }

    public static void main(String[] args) {
        // Example usage for staff login
        boolean isAdmin = false;
        SwingUtilities.invokeLater(() -> new Staff(isAdmin));
    }
}
