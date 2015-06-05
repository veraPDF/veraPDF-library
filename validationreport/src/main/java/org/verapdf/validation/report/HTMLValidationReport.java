package org.verapdf.validation.report;

import com.google.common.io.Files;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Generating HTML validation report
 * Created by bezrukov on 6/2/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class HTMLValidationReport {

    private final static String LOGO_NAME = "veraPDF-logo-200.png";

    /**
     * Creates html validation report
     * @param htmlReportPath - path with name of the resulting html report
     * @param xmlReport - xml validation report file
     * @param validationProfile - validation profile file
     * @throws TransformerException - if an unrecoverable error occurs during the course of the transformation or
     * @throws IOException - file system exceptions
     */
    public static void wrightHTMLValidationReport(String htmlReportPath, File xmlReport, File validationProfile) throws TransformerException, IOException {

        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer(new StreamSource(HTMLValidationReport.class.getClassLoader().getResourceAsStream("HTMLValidationReportStylesheet.xsl")));
        transformer.setParameter("profilePath", validationProfile.getAbsolutePath());

        transformer.transform(new StreamSource(xmlReport), new StreamResult(new FileOutputStream(htmlReportPath)));

        File dir = new File(htmlReportPath).getParentFile();

        InputStream imageResource = HTMLValidationReport.class.getClassLoader().getResourceAsStream(LOGO_NAME);

        File image = new File(dir, LOGO_NAME);
        OutputStream outStream = new FileOutputStream(image);

        int read;
        byte[] bytes = new byte[1024];

        while ((read = imageResource.read(bytes)) != -1) {
            outStream.write(bytes, 0, read);
        }

    }

    /**
     * @return name of the generated image
     */
    public static String getLogoImageName(){
        return LOGO_NAME;
    }

}
