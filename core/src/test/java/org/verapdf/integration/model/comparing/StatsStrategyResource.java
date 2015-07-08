package org.verapdf.integration.model.comparing;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Timur Kamalov
 */
public class StatsStrategyResource extends StrategyResource {

    private Long passedChecks;
    private Long failedChecks;

    @JsonProperty("passedChecks")
    public Long getPassedChecks() {
        return passedChecks;
    }

    public void setPassedChecks(Long passedChecks) {
        this.passedChecks = passedChecks;
    }

    @JsonProperty("failedChecks")
    public Long getFailedChecks() {
        return failedChecks;
    }

    public void setFailedChecks(Long failedChecks) {
        this.failedChecks = failedChecks;
    }

}
