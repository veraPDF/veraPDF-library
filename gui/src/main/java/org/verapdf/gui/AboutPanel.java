package org.verapdf.gui;

import org.verapdf.gui.tools.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * About Panel
 *
 * @author Maksim Bezrukov
 */
class AboutPanel extends JPanel {

    private static Logger logger = Logger.getLogger(AboutPanel.class.getName());

    private JButton okButton;
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

        LogoPanel logo = new LogoPanel(GUIConstants.LOGO_NAME, GUIConstants.LOGO_BACKGROUND, 10);

        mainPanel.add(logo);

        PartnersPanel partners = new PartnersPanel(GUIConstants.PARTNERS_NAME, GUIConstants.PARTNERS_BACKGROUND);

        mainPanel.add(partners);

        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(false);
            }
        });


        JButton urlLabel = new JButton(GUIConstants.LOGO_LINK_TEXT);
        urlLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    Desktop.getDesktop().browse(new URI(GUIConstants.LOGO_LINK_URL));
                } catch (IOException | URISyntaxException e1) {
                    JOptionPane.showMessageDialog(AboutPanel.this, GUIConstants.ERROR, GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.SEVERE, "Exception in opening " + GUIConstants.LOGO_LINK_URL + " link: ", e);
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
