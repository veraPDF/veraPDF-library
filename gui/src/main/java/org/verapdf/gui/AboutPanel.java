package org.verapdf.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * About Panel
 *
 * @author Maksim Bezrukov
 */
class AboutPanel extends JPanel {

    private static final String LOGO_NAME = "veraPDF-logo-600.png";
    private static final Color LOGO_BACKGROUND = Color.LIGHT_GRAY;
    private static final String PARTNERS_NAME = "partners.png";
    private static final Color PARTNERS_BACKGROUND = Color.WHITE;
    private static final String LOGO_LINK_TEXT = "Visit veraPDF.org";
    private static final String LOGO_LINK_URL = "http://www.verapdf.org";

    private static final String ERROR = "Error";

    private JButton okButton;
    private JButton urlLabel;
    private JDialog dialog;

    /**
     * About panel
     *
     * @throws IOException - throws when there is a problem with reading image from the input stream
     */
    public AboutPanel() throws IOException {
        setLayout(new BorderLayout());


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        add(mainPanel, BorderLayout.CENTER);

        LogoPanel logo = new LogoPanel(LOGO_NAME, LOGO_BACKGROUND, 10);

        mainPanel.add(logo);

        PartnersPanel partners = new PartnersPanel(PARTNERS_NAME, PARTNERS_BACKGROUND);

        mainPanel.add(partners);

        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(false);
            }
        });


        urlLabel = new JButton(LOGO_LINK_TEXT);
        urlLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    Desktop.getDesktop().browse(new URI(LOGO_LINK_URL));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(AboutPanel.this, ERROR, ERROR, JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException e1) {
                    JOptionPane.showMessageDialog(AboutPanel.this, ERROR, ERROR, JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(urlLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);

        bottomPanel.add(labelPanel);
        bottomPanel.add(buttonPanel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Shows about dialog
     *
     * @param parent - parent component of the dialog
     * @param title  - title of the dialog
     */
    public void showDialog(Component parent, String title) {

        Frame owner;
        if (parent instanceof Frame) {
            owner = (Frame) parent;
        } else {
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
        }

        if (dialog == null || dialog.getOwner() != owner) {
            dialog = new JDialog(owner, true);
            dialog.setResizable(false);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
            dialog.setTitle(title);
        }

        dialog.setLocation(150, 150);
        dialog.setVisible(true);
    }

}
