package org.verapdf.integration.tools;

import org.verapdf.integration.model.TestEntity;

/**
 * @author Timur Kamalov
 */
public class ResultComparator {

    public static boolean compare(TestEntity entity) {
        switch (entity.getComparingStrategy()) {
            case IGNORE: return compareIgnoreStrategy(entity);
            case DRAFT: return compareDraftStrategy(entity);
            case FAIL: return compareFailStrategy(entity);
            case PASS: return comparePassStrategy(entity);
            default: return false;
        }
    }

    public static boolean compareIgnoreStrategy(TestEntity entity) {
        return true;
    }

    public static boolean compareDraftStrategy(TestEntity entity) {
        return entity.getInfo() != null;
    }

    public static boolean compareFailStrategy(TestEntity entity) {
        return !entity.getInfo().isCompliant();
    }

    public static boolean comparePassStrategy(TestEntity entity) {
        return entity.getInfo().isCompliant();
    }
}
