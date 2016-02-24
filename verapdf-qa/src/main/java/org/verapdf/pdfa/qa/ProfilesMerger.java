package org.verapdf.pdfa.qa;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author Maksim Bezrukov
 */
public class ProfilesMerger {

    public static void main(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("There must be at least four arguments");
        }

        File[] directories = new File[args.length - 3];
        for (int i = 3; i < args.length; ++i) {
            File dir = new File(args[i]);
            if (!dir.isDirectory()) {
                throw new IllegalArgumentException("All entered arguments after the third one " +
                        "should point to the folder with the profiles to merge");
            }
            directories[i - 3] = dir;
        }

        try {
            mergeAtomicProfiles(new FileOutputStream(new File("/Users/bezrukov/Documents/Duallab/veraPDF-validation-profiles/PDF_A/new.xml")), directories, args[0], args[1], args[2]);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void mergeAtomicProfiles(OutputStream out,
                                           final File[] root,
                                           final String name,
                                           final String description,
                                           final String creator) throws IOException, JAXBException {
        SortedSet<Rule> rules = new TreeSet<>(new RuleComparatorById());
        Set<Variable> variables = new HashSet<>();
        PDFAFlavour flavour = null;

        for (File dir : root) {
            RuleDirectory ruleDir = RuleDirectory.loadFromDir(dir);
            rules.addAll(ruleDir.getItems());
            RuleDirectory.checkAndAddAllVariables(variables, ruleDir.getVariables());
            if (flavour == null) {
                flavour = ruleDir.getFlavour();
            }
        }

        ProfileDetails det = Profiles.profileDetailsFromValues(name, description, creator, new Date());
        ValidationProfile mergedProfile = Profiles.profileFromSortedValues(flavour, det, "", rules, variables);
        Profiles.profileToXml(mergedProfile, out, true);
    }

    public static class RuleComparatorById implements Comparator<Rule> {

        @Override
        public int compare(Rule o1, Rule o2) {
            RuleId o1RuleId = o1.getRuleId();
            RuleId o2RuleId = o2.getRuleId();
            String o1Clause = o1RuleId.getClause();
            String o2Clause = o2RuleId.getClause();

            if (o1Clause.equals(o2Clause)) {
                return o1RuleId.getTestNumber() - o2RuleId.getTestNumber();
            } else {
                String[] o1StrArr = o1Clause.split("\\.");
                String[] o2StrArr = o2Clause.split("\\.");
                int min = Math.min(o1StrArr.length, o2StrArr.length);

                for (int i = 0; i < min; ++i) {
                    if (!o1StrArr[i].equals(o2StrArr[i])) {
                        return Integer.parseInt(o1StrArr[i]) - Integer.parseInt(o2StrArr[i]);
                    }
                }

                return o1StrArr.length - o2StrArr.length;
            }
        }
    }
}
