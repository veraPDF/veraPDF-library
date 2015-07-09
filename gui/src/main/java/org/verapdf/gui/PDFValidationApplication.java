package org.verapdf.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Main frame of the PDFA Conformance Checker
 *
 * @author Maksim Bezrukov
 */
public class PDFValidationApplication extends JFrame {

    private static final String TITLE = "PDF/A Conformance Checker";
    private static final String LOGO_NAME = "veraPDF-logo-600.png";

    private AboutPanel aboutPanel;

    /**
     * Starting point of the gui
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                try {
                    PDFValidationApplication frame = new PDFValidationApplication();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Creates the frame.
     */
    public PDFValidationApplication() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 350);
        setResizable(false);

        setTitle(TITLE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        aboutPanel = null;
        try {
            aboutPanel = new AboutPanel();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in reading logo image.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (aboutPanel != null) {
                    aboutPanel.showDialog(PDFValidationApplication.this, "About veraPDF");
                }
            }
        });

        menuBar.add(about);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        MiniLogoPanel logoPanel = null;
        try {
            logoPanel = new MiniLogoPanel(LOGO_NAME);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(PDFValidationApplication.this, "Error in creating mini logo.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        contentPane.add(logoPanel);

        CheckerPanel checkerPanel = null;
        try {
            checkerPanel = new CheckerPanel();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(PDFValidationApplication.this, "Error in loading xml and html images.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        contentPane.add(checkerPanel);
    }

}
