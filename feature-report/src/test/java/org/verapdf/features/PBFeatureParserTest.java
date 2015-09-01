package org.verapdf.features;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.pb.objects.*;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maksim Bezrukov
 */
public class PBFeatureParserTest {

    private static final byte[] METADATA_BYTES = {60, 63, 120, 112, 97, 99, 107, 101, 116, 32, 98, 101, 103, 105, 110,
            61, 34, -17, -69, -65, 34, 32, 105, 100, 61, 34, 87, 53, 77, 48, 77, 112, 67, 101, 104, 105, 72, 122,
            114, 101, 83, 122, 78, 84, 99, 122, 107, 99, 57, 100, 34, 63, 62, 10, 60, 120, 58, 120, 109, 112, 109, 101,
            116, 97, 32, 120, 109, 108, 110, 115, 58, 120, 61, 34, 97, 100, 111, 98, 101, 58, 110, 115, 58, 109, 101, 116,
            97, 47, 34, 32, 120, 58, 120, 109, 112, 116, 107, 61, 34, 65, 100, 111, 98, 101, 32, 88, 77, 80, 32, 67,
            111, 114, 101, 32, 53, 46, 50, 45, 99, 48, 48, 49, 32, 54, 51, 46, 49, 51, 57, 52, 51, 57, 44, 32,
            50, 48, 49, 48, 47, 48, 57, 47, 50, 55, 45, 49, 51, 58, 51, 55, 58, 50, 54, 32, 32, 32, 32, 32,
            32, 32, 32, 34, 62, 10, 32, 32, 32, 60, 114, 100, 102, 58, 82, 68, 70, 32, 120, 109, 108, 110, 115, 58,
            114, 100, 102, 61, 34, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 119, 51, 46, 111, 114, 103, 47, 49,
            57, 57, 57, 47, 48, 50, 47, 50, 50, 45, 114, 100, 102, 45, 115, 121, 110, 116, 97, 120, 45, 110, 115, 35,
            34, 62, 10, 32, 32, 32, 32, 32, 32, 60, 114, 100, 102, 58, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111,
            110, 32, 114, 100, 102, 58, 97, 98, 111, 117, 116, 61, 34, 34, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 120, 109, 108, 110, 115, 58, 120, 109, 112, 61, 34, 104, 116, 116, 112, 58, 47, 47, 110, 115, 46,
            97, 100, 111, 98, 101, 46, 99, 111, 109, 47, 120, 97, 112, 47, 49, 46, 48, 47, 34, 62, 10, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 60, 120, 109, 112, 58, 77, 111, 100, 105, 102, 121, 68, 97, 116, 101, 62, 50, 48,
            49, 53, 45, 48, 56, 45, 51, 49, 84, 49, 52, 58, 48, 53, 58, 51, 49, 43, 48, 51, 58, 48, 48, 60,
            47, 120, 109, 112, 58, 77, 111, 100, 105, 102, 121, 68, 97, 116, 101, 62, 10, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 60, 120, 109, 112, 58, 67, 114, 101, 97, 116, 101, 68, 97, 116, 101, 62, 50, 48, 49, 53, 45, 48,
            56, 45, 50, 50, 84, 49, 52, 58, 48, 52, 58, 52, 53, 43, 48, 51, 58, 48, 48, 60, 47, 120, 109, 112,
            58, 67, 114, 101, 97, 116, 101, 68, 97, 116, 101, 62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 60, 120,
            109, 112, 58, 77, 101, 116, 97, 100, 97, 116, 97, 68, 97, 116, 101, 62, 50, 48, 49, 53, 45, 48, 56, 45,
            51, 49, 84, 49, 52, 58, 48, 53, 58, 51, 49, 43, 48, 51, 58, 48, 48, 60, 47, 120, 109, 112, 58, 77,
            101, 116, 97, 100, 97, 116, 97, 68, 97, 116, 101, 62, 10, 32, 32, 32, 32, 32, 32, 60, 47, 114, 100, 102,
            58, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 62, 10, 32, 32, 32, 32, 32, 32, 60, 114, 100, 102,
            58, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 32, 114, 100, 102, 58, 97, 98, 111, 117, 116, 61, 34,
            34, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 120, 109, 108, 110, 115, 58, 100, 99, 61, 34,
            104, 116, 116, 112, 58, 47, 47, 112, 117, 114, 108, 46, 111, 114, 103, 47, 100, 99, 47, 101, 108, 101, 109, 101,
            110, 116, 115, 47, 49, 46, 49, 47, 34, 62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 60, 100, 99, 58,
            102, 111, 114, 109, 97, 116, 62, 97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 112, 100, 102, 60, 47,
            100, 99, 58, 102, 111, 114, 109, 97, 116, 62, 10, 32, 32, 32, 32, 32, 32, 60, 47, 114, 100, 102, 58, 68,
            101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 62, 10, 32, 32, 32, 32, 32, 32, 60, 114, 100, 102, 58, 68,
            101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 32, 114, 100, 102, 58, 97, 98, 111, 117, 116, 61, 34, 34, 10,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 120, 109, 108, 110, 115, 58, 120, 109, 112, 77, 77, 61,
            34, 104, 116, 116, 112, 58, 47, 47, 110, 115, 46, 97, 100, 111, 98, 101, 46, 99, 111, 109, 47, 120, 97, 112,
            47, 49, 46, 48, 47, 109, 109, 47, 34, 62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 60, 120, 109, 112,
            77, 77, 58, 68, 111, 99, 117, 109, 101, 110, 116, 73, 68, 62, 117, 117, 105, 100, 58, 57, 97, 55, 50, 54,
            54, 53, 100, 45, 102, 102, 51, 53, 45, 52, 97, 49, 100, 45, 57, 55, 100, 102, 45, 51, 98, 49, 97, 56,
            49, 97, 98, 97, 97, 51, 54, 60, 47, 120, 109, 112, 77, 77, 58, 68, 111, 99, 117, 109, 101, 110, 116, 73,
            68, 62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 60, 120, 109, 112, 77, 77, 58, 73, 110, 115, 116, 97,
            110, 99, 101, 73, 68, 62, 117, 117, 105, 100, 58, 102, 50, 100, 48, 51, 48, 100, 98, 45, 54, 53, 97, 98,
            45, 52, 57, 50, 55, 45, 97, 101, 48, 97, 45, 100, 56, 55, 56, 57, 49, 56, 52, 99, 49, 48, 55, 60,
            47, 120, 109, 112, 77, 77, 58, 73, 110, 115, 116, 97, 110, 99, 101, 73, 68, 62, 10, 32, 32, 32, 32, 32,
            32, 60, 47, 114, 100, 102, 58, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 62, 10, 32, 32, 32, 60,
            47, 114, 100, 102, 58, 82, 68, 70, 62, 10, 60, 47, 120, 58, 120, 109, 112, 109, 101, 116, 97, 62, 10, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 10, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 10, 60, 63, 120, 112, 97, 99, 107, 101, 116, 32, 101, 110, 100, 61, 34, 119, 34,
            63, 62};

    private static final String ENTRY = "entry";
    private static final String ID = "id";
    private static final String METADATA = "metadata";

    @Test
    public void featuresObjectsTypeTest() {
        assertEquals(FeaturesObjectTypesEnum.ANNOTATION, new PBAnnotationFeaturesObject(null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.COLORSPACE, new PBColorSpaceFeaturesObject(null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.DOCUMENT_SECURITY, new PBDocSecurityFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.EMBEDDED_FILE, new PBEmbeddedFileFeaturesObject(null, 0).getType());
        assertEquals(FeaturesObjectTypesEnum.EXT_G_STATE, new PBExtGStateFeaturesObject(null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.FONT, new PBFontFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.FORM_XOBJECT, new PBFormXObjectFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.ICCPROFILE, new PBICCProfileFeaturesObject(null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.IMAGE_XOBJECT, new PBImageXObjectFeaturesObject(null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY, new PBInfoDictFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, new PBLowLvlInfoFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.METADATA, new PBMetadataFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.OUTLINES, new PBOutlinesFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.OUTPUTINTENT, new PBOutputIntentsFeaturesObject(null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PAGE, new PBPageFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, 0).getType());
        assertEquals(FeaturesObjectTypesEnum.PROCSET, new PBProcSetFeaturesObject(null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PROPERTIES, new PBPropertiesDictFeaturesObject(null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.SHADING, new PBShadingFeaturesObject(null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBShadingPatternFeaturesObject(null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBTilingPatternFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
    }

    @Test
    public void test() throws URISyntaxException, IOException, FeaturesTreeNodeException {
        File pdf = new File(getSystemIndependentPath("/FR.pdf"));
        PDDocument document = PDDocument.load(pdf, false, true);
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document);

        assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).size());
        FeatureTreeNode infDict = getInfDictNode();
        assertEquals(infDict, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).get(0));

        assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).size());
        FeatureTreeNode metadata = getMetadataNode();
        assertEquals(metadata, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).get(0));

        assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).size());
        FeatureTreeNode lli = getLowLvlInfo();
        assertEquals(lli, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).get(0));

        assertEquals(4, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).size());
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file1", "1.txt", "",
                "text/plain", "FlateDecode", "2015-08-31T13:33:43.000+03:00", "2015-08-31T13:20:39.000Z", "Ô˛„Ù‘\u0000²\u0004é•\tŸìøB~", "0")));
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file2", "Arist.jpg", "",
                "image/jpeg", "FlateDecode", "2015-08-31T13:33:33.000+03:00", "2014-08-15T17:17:58.000Z", "ù•8r‚‰$ŠKåŒêlŸm}", "26862")));
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file3", "XMP - 8.xml", "",
                "text/xml", "FlateDecode", "2015-08-31T13:33:38.000+03:00", "2015-08-20T12:24:50.000Z", "\u0006\u0005¼ä\u0017Uw\r⁄©>ñ8\u000EnÔ", "876")));
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file4", "fox_1.jpg", "Some Description for embedded file",
                "image/jpeg", "FlateDecode", "2015-08-22T14:01:19.000+03:00", "2014-09-08T12:01:07.000Z", "ËÓþVf\u0007ç`ºŁåk\u0015?A\r", "67142")));

        assertEquals(5, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).size());
    }

    private static FeatureTreeNode getEmbeddedFileNode(String id, String fileName, String description, String subtype,
                                                       String filter, String creationDate, String modDate, String checkSum,
                                                       String size) throws FeaturesTreeNodeException {
        FeatureTreeNode root = FeatureTreeNode.newRootInstance("embeddedFile");
        root.addAttribute(ID, id);
        FeatureTreeNode.newChildInstanceWithValue("fileName", fileName, root);
        FeatureTreeNode.newChildInstanceWithValue("description", description, root);
        FeatureTreeNode.newChildInstanceWithValue("subtype", subtype, root);
        FeatureTreeNode.newChildInstanceWithValue("filter", filter, root);
        FeatureTreeNode.newChildInstanceWithValue("creationDate", creationDate, root);
        FeatureTreeNode.newChildInstanceWithValue("modDate", modDate, root);
        FeatureTreeNode.newChildInstanceWithValue("checkSum", checkSum, root);
        FeatureTreeNode.newChildInstanceWithValue("size", size, root);
        return root;
    }

    private static FeatureTreeNode getLowLvlInfo() throws FeaturesTreeNodeException {
        FeatureTreeNode lli = FeatureTreeNode.newRootInstance("lowLevelInfo");
        FeatureTreeNode.newChildInstanceWithValue("indirectObjectsNumber", "125", lli);
        FeatureTreeNode docID = FeatureTreeNode.newChildInstance("documentId", lli);
        docID.addAttribute("modificationId", "295EBB0E08D32644B7E5C1825F15AD3A");
        docID.addAttribute("creationId", "85903F3A2C43B1DA24E486CD15B8154E");
        FeatureTreeNode filters = FeatureTreeNode.newChildInstance("filters", lli);
        addFilter("FlateDecode", filters);
        addFilter("ASCIIHexDecode", filters);
        addFilter("CCITTFaxDecode", filters);
        addFilter("DCTDecode", filters);
        return lli;
    }

    private static void addFilter(String name, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        FeatureTreeNode filter = FeatureTreeNode.newChildInstance("filter", parent);
        filter.addAttribute("name", name);
    }

    private static FeatureTreeNode getInfDictNode() throws FeaturesTreeNodeException {
        FeatureTreeNode infDict = FeatureTreeNode.newRootInstance("informationDict");
        addEntry("Title", "SomeTitle", infDict);
        addEntry("Author", "SomeAuthor", infDict);
        addEntry("Subject", "SomeSubject", infDict);
        addEntry("Keywords", "Some Keywords", infDict);
        addEntry("Creator", "SomeCreator", infDict);
        addEntry("Producer", "SomeProducer", infDict);
        addEntry("CreationDate", "2015-08-22T14:04:45.000+03:00", infDict);
        addEntry("ModDate", "2015-08-31T14:05:31.000+03:00", infDict);
        addEntry("Trapped", "False", infDict);
        addEntry("CustomEntry", "CustomValue", infDict);
        addEntry("SecondCustomEntry", "SomeCustomValue", infDict);
        return infDict;
    }

    private static FeatureTreeNode getMetadataNode() throws FeaturesTreeNodeException {
        return FeatureTreeNode.newRootInstanceWIthValue(METADATA, METADATA_BYTES);
    }

    private static void addEntry(String name, String value, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        FeatureTreeNode entry = FeatureTreeNode.newChildInstanceWithValue(ENTRY, value, parent);
        entry.addAttribute("key", name);
    }

    private static String getSystemIndependentPath(String path) throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
