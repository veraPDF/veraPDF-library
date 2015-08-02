package org.verapdf.integration.model.comparing;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Timur Kamalov
 */
public class StatsStrategyResource extends StrategyResource {

    private long passedChecks;
    private long failedChecks;

    @JsonProperty("passedChecks")
    public long getPassedChecks() {
        return passedChecks;
    }

    public void setPassedChecks(long passedChecks) {
        this.passedChecks = passedChecks;
    }

    @JsonProperty("failedChecks")
    public long getFailedChecks() {
        return failedChecks;
    }

    public void setFailedChecks(long failedChecks) {
        this.failedChecks = failedChecks;
    }

}
