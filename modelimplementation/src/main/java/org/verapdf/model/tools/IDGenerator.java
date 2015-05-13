package org.verapdf.model.tools;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 *
 * This class specified for creating unique ID`s for every object
 * from model
 */
public final class IDGenerator {

    private static Integer id = 0;

    private IDGenerator(){}

    public static Integer generateID() {
        return id++;
    }
}
