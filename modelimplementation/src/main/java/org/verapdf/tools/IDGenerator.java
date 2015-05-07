package org.verapdf.tools;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 *
 * This class specified for creating unique ID`s for every object
 * from model
 */
public final class IDGenerator {

    private static Integer id = 0;

    private IDGenerator(){}

    public static String generateID() {
        return "id" + id++;
    }
}
