package presentation;

import javax.swing.JFrame;
import java.awt.SystemColor;
import java.awt.Window.Type;
import java.awt.Font;
import javax.swing.JButton;

/**
 * Creeaza un meniu din care se poate alege una dintre cele 3 ferestre de gestiune a bazei de date.
 */
public class GUI {

    private JFrame selectionWindow;

    public GUI() {
        initialize();
        this.selectionWindow.setVisible(true);
    }

    /**
     * Creeaza interfata
     */
    private void initialize() {
        selectionWindow = new JFrame();
        selectionWindow.setType(Type.POPUP);
        selectionWindow.setTitle("Main Window");
        selectionWindow.getContentPane().setBackground(SystemColor.activeCaption);
        selectionWindow.getContentPane().setLayout(null);

        JButton clientWindowBtn = new JButton("Client Window");
        clientWindowBtn.addActionListener(e -> {
            View view = new View("Client");
            new Controller(view);
        });
        clientWindowBtn.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        clientWindowBtn.setBounds(31, 55, 195, 55);
        selectionWindow.getContentPane().add(clientWindowBtn);

        JButton productWindowBtn = new JButton("Product Window");
        productWindowBtn.addActionListener(e -> {
            View view = new View("Product");
            new Controller(view);
        });
        productWindowBtn.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        productWindowBtn.setBounds(31, 141, 195, 55);
        selectionWindow.getContentPane().add(productWindowBtn);
        selectionWindow.setBounds(400, 100, 634, 456);

        JButton orderWindowBtn = new JButton("Order Window");
        orderWindowBtn.addActionListener(e -> {
            View view = new View("Order");
            new Controller(view);
        });
        orderWindowBtn.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        orderWindowBtn.setBounds(31, 227, 195, 55);
        selectionWindow.getContentPane().add(orderWindowBtn);
        selectionWindow.setBounds(400, 100, 634, 456);
        selectionWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
