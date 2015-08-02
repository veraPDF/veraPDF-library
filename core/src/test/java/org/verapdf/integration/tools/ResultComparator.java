package org.verapdf.integration.tools;

import org.verapdf.integration.model.TestEntity;
import org.verapdf.integration.model.comparing.StatsStrategyResource;

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
            case STATS: return compareStatsStrategy(entity);
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
        return !entity.getInfo().getResult().isCompliant();
    }

    public static boolean comparePassStrategy(TestEntity entity) {
        return entity.getInfo().getResult().isCompliant();
    }

    public static boolean compareStatsStrategy(TestEntity entity) {
        StatsStrategyResource resource = (StatsStrategyResource) entity.getStrategyResource();

        int actualPassedChecks = entity.getInfo().getResult().getSummary().getAttrPassedChecks();
        int actualFailedChecks = entity.getInfo().getResult().getSummary().getAttrFailedChecks();
        return actualPassedChecks == resource.getPassedChecks() && actualFailedChecks == resource.getFailedChecks();
    }

}
