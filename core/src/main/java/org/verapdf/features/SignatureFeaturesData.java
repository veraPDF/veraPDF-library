package org.verapdf.features;

import java.io.InputStream;
import java.util.Calendar;

/**
 * @author Maksim Bezrukov
 */
public class SignatureFeaturesData extends FeaturesData {

    private String filter;
    private String subFilter;
    private String name;
    private Calendar signDate;
    private String location;
    private String reason;
    private String contactInfo;

    public SignatureFeaturesData(InputStream stream, String filter, String subFilter, String name, Calendar signDate, String location, String reason, String contactInfo) {
        super(stream);
        this.filter = filter;
        this.subFilter = subFilter;
        this.name = name;
        this.signDate = signDate;
        this.location = location;
        this.reason = reason;
        this.contactInfo = contactInfo;
    }

    public static SignatureFeaturesData newInstance(InputStream stream, String filter, String subFilter, String name, Calendar signDate, String location, String reason, String contactInfo) {
        return new SignatureFeaturesData(stream, filter, subFilter, name, signDate, location, reason, contactInfo);
    }

    public String getFilter() {
        return filter;
    }

    public String getSubFilter() {
        return subFilter;
    }

    public String getName() {
        return name;
    }

    public Calendar getSignDate() {
        return signDate;
    }

    public String getLocation() {
        return location;
    }

    public String getReason() {
        return reason;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
