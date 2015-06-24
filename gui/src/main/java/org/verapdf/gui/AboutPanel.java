package org.verapdf.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;

class AboutPanel extends JPanel {

    private final static String LOGO_NAME = "veraPDF-logo-600.png";
    private final static Color LOGO_BACKGROUND = Color.LIGHT_GRAY;
    private final static String PARTNERS_NAME = "partners.png";
    private final static Color PARTNERS_BACKGROUND = Color.WHITE;
    private final static String LOGO_LINK_TEXT = "Visit veraPDF.org";
    private final static String LOGO_LINK_URL = "http://www.verapdf.org";

    private JButton okButton;
    private JButton urlLabel;
    private JDialog dialog;

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
                    JOptionPane.showMessageDialog(AboutPanel.this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException e1) {
                    JOptionPane.showMessageDialog(AboutPanel.this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
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

    public void showDialog(Component parent, String title) {

        Frame owner;
        if (parent instanceof Frame)
            owner = (Frame) parent;
        else
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
                    parent);

        if (dialog == null || dialog.getOwner() != owner) {
            dialog = new JDialog(owner, true);
            dialog.setResizable(false);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
            dialog.setTitle(title);
        }

        dialog.setLocation(150,150);
        dialog.setVisible(true);
    }

}
