package org.verapdf.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

class AboutPanel extends JPanel {

    private final static String LOGO_NAME = "veraPDF-logo-600.png";
    private final static Color LOGO_BACKGROUND = Color.LIGHT_GRAY;
    private final static String PARTNERS_NAME = "partners.png";
    private final static Color PARTNERS_BACKGROUND = Color.WHITE;
    private final static String LOGO_LINK_TEXT = "www.verapdf.org";
    private final static String LOGO_LINK_URL = "http://www.verapdf.org";

    private JButton okButton;
    private JDialog dialog;

    public AboutPanel() throws IOException {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        add(mainPanel, BorderLayout.CENTER);

        LogoPanel logo = new LogoPanel(LOGO_NAME, LOGO_BACKGROUND, LOGO_LINK_URL, LOGO_LINK_TEXT);

        mainPanel.add(logo);

        PartnersPanel partners = new PartnersPanel(PARTNERS_NAME, PARTNERS_BACKGROUND);

        mainPanel.add(partners);

        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(false);
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showDialog(Component parent, String title) {

        Frame owner = null;
        if (parent instanceof Frame)
            owner = (Frame) parent;
        else
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
                    parent);

        if (dialog == null || dialog.getOwner() != owner) {
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
        }

        dialog.setTitle(title);
        dialog.setVisible(true);
        dialog.setResizable(false);
    }

}
