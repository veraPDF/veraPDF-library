/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor.reports.multithread.writer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.valueOf;
import static org.verapdf.component.AuditDurationImpl.getStringDuration;

public class ReportParserEventHandler extends DefaultHandler {
    private static final Logger LOGGER = Logger.getLogger(ReportParserEventHandler.class.getCanonicalName());

    private static final Set<String> BATCH_SUMMARY_TAGS =
            new HashSet<>(Arrays.asList("batchSummary", "arlingtonReports", "featureReports", "repairReports"));

    private String element;
    private final Map<String, Map<String, Integer>> batchSummary = new LinkedHashMap<>();
    private final Map<String, Map<String, Integer>> current = new LinkedHashMap<>();

    private boolean isPrinting = false;
    private final long startTime;

    private boolean isAddReportToSummary = false;

    private final XMLStreamWriter writer;
    
    public ReportParserEventHandler(XMLStreamWriter writer) {
        this.writer = writer;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void endDocument() {
        if (!current.isEmpty()) {
            if (!batchSummary.isEmpty()) {
                current.forEach((k, currentAttributesAndValues) -> {
                    Map<String, Integer> summaryAttributesAndValues = batchSummary.get(k);
                    currentAttributesAndValues.forEach((key, v) -> summaryAttributesAndValues.merge(key, v, Integer::sum));
                });
            } else {
                batchSummary.putAll(current);
            }
            current.clear();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (isAddReportToSummary
                && BATCH_SUMMARY_TAGS.contains(qName)
                && !current.containsKey(qName)) {
            addReportToSummary(qName, attributes);
        }

        if (element.equals(qName)) {
            isPrinting = true;
        }

        if (isPrinting) {
            print(qName, attributes);
        }
    }

    private void print(String qName, Attributes attributes) {
        try {
            writer.writeStartElement(qName);
            for (int i = 0; i < attributes.getLength(); i++) {
                writer.writeAttribute(attributes.getQName(i), attributes.getValue(i));
            }
        } catch (XMLStreamException e) {
            LOGGER.log(Level.SEVERE, "Can't write the element", e);
        }
    }

    private void addReportToSummary(String qName, Attributes attributes) {
        Map<String, Integer> attributesAndValues = new LinkedHashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            String attribute = attributes.getQName(i);
            Integer value = Integer.valueOf(attributes.getValue(i));
            attributesAndValues.put(attribute, value);
        }
        current.put(qName, attributesAndValues);
    }

    public void printSummary() {
        try {
            String batchSummaryTag = "batchSummary";

            writeStartBatchSummaryTag(batchSummaryTag);

            batchSummary.remove(batchSummaryTag);

            writeTagsInsideBatchSummary(batchSummary.keySet());

            writer.writeEndElement();
        } catch (XMLStreamException e) {
            LOGGER.log(Level.SEVERE, "Can't write the element", e);
        }
    }

    private void writeTagsInsideBatchSummary(Set<String> batchSummaryTags) throws XMLStreamException {
        batchSummaryTags.forEach(k -> {
            try {
                writer.writeStartElement(k);

                Map<String, Integer> attributesAndValues = this.batchSummary.get(k);
                int sum = attributesAndValues.values().stream().mapToInt(Number::intValue).sum();
                attributesAndValues.forEach((attribute, value) -> {
                    try {
                        writer.writeAttribute(attribute, valueOf(attributesAndValues.get(attribute)));
                    } catch (XMLStreamException e) {
                        LOGGER.log(Level.SEVERE, "Can't write the element", e);
                    }
                });
                writer.writeCharacters(valueOf(sum));
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                LOGGER.log(Level.SEVERE, "Can't write the element", e);
            }
        });

        writeDurationTag();
    }

    private void writeDurationTag() throws XMLStreamException {
        long finishTime = System.currentTimeMillis();
        String stringDuration = getStringDuration(finishTime - this.startTime);

        writer.writeStartElement("duration");
        writer.writeAttribute("start", valueOf(this.startTime));
        writer.writeAttribute("finish", valueOf(finishTime));
        writer.writeCharacters(stringDuration);
        writer.writeEndElement();
    }

    private void writeStartBatchSummaryTag(String batchSummaryTag) throws XMLStreamException {
        writer.writeStartElement(batchSummaryTag);
        Map<String, Integer> batchSummaryAttributesAndValues = this.batchSummary.get(batchSummaryTag);
        batchSummaryAttributesAndValues.forEach((attribute, value) -> {
            try {
                writer.writeAttribute(attribute, valueOf(value));
            } catch (XMLStreamException e) {
                LOGGER.log(Level.SEVERE, "Can't write the element", e);
            }
        });
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (isPrinting) {
            try {
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                LOGGER.log(Level.SEVERE, "Can't write the element", e);
            }
        }
        if (element.equals(qName)) {
            isPrinting = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (isPrinting) {
            try {
                writer.writeCharacters(new String(ch, start, length));
            } catch (XMLStreamException e) {
                LOGGER.log(Level.SEVERE, "Can't write the element", e);
            }
        }
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setIsAddReportToSummary(boolean addReportToSummary) {
        this.isAddReportToSummary = addReportToSummary;
    }
}
