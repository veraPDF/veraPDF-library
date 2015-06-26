package org.verapdf.integration.tools;

import org.verapdf.integration.model.StatsStrategyResource;
import org.verapdf.integration.model.TestEntity;

/**
 * @author Timur Kamalov
 */
public class ResultComparator {

    public static Boolean compare(TestEntity entity) {
        switch (entity.getComparingStrategy()) {
            case IGNORE: return compareIgnoreStrategy(entity);
            case DRAFT: return compareDraftStrategy(entity);
            case FAIL: return compareFailStrategy(entity);
            case PASS: return comparePassStrategy(entity);
            case STATS: return compareStatsStrategy(entity);
            default: return Boolean.FALSE;
        }
    }

    public static Boolean compareIgnoreStrategy(TestEntity entity) {
        return Boolean.TRUE;
    }

    public static Boolean compareDraftStrategy(TestEntity entity) {
        return Boolean.valueOf(entity.getInfo() != null);
    }

    public static Boolean compareFailStrategy(TestEntity entity) {
        return Boolean.valueOf(!entity.getInfo().getResult().isCompliant());
    }

    public static Boolean comparePassStrategy(TestEntity entity) {
        return Boolean.valueOf(entity.getInfo().getResult().isCompliant());
    }

    public static Boolean compareStatsStrategy(TestEntity entity) {
        StatsStrategyResource resource = (StatsStrategyResource) entity.getStrategyResource();

        int actualPassedChecks = entity.getInfo().getResult().getSummary().getAttrPassedChecks();
        int actualFailedChecks = entity.getInfo().getResult().getSummary().getAttrFailedChecks();
        return Boolean.valueOf(actualPassedChecks == resource.getPassedChecks() && actualFailedChecks == resource.getFailedChecks());
    }

}
