package org.verapdf.metadata.fixer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.verapdf.pdfa.MetadataFixerResult;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataFixerResultImpl implements MetadataFixerResult {

    private final RepairStatus status;
    private final List<String> appliedFixes;

    private MetadataFixerResultImpl() {
        this(RepairStatus.NO_ACTION, new ArrayList<String>());
    }

    private MetadataFixerResultImpl(final RepairStatus status,
            final List<String> fixes) {
        super();
        this.status = status;
        this.appliedFixes = new ArrayList<>(fixes);
    }

    @Override
    public RepairStatus getRepairStatus() {
        return this.status;
    }

    @Override
    public List<String> getAppliedFixes() {
        return Collections.unmodifiableList(this.appliedFixes);
    }

    @Override
    public Iterator<String> iterator() {
        return this.appliedFixes.iterator();
    }

    /**
     * @param status
     * @param fixes
     * @return
     */
    public static MetadataFixerResult fromValues(final RepairStatus status,
            final List<String> fixes) {
        return new MetadataFixerResultImpl(status, fixes);
    }

    /**
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     *
     */
    @SuppressWarnings("hiding")
    public static class Builder {
        private RepairStatus status = RepairStatus.NO_ACTION;
        private final List<String> fixes = new ArrayList<>();

        /**
         * @param status
         * @return
         */
        public Builder status(final RepairStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return
         */
        public RepairStatus getStatus() {
            return this.status;
        }

        /**
         * @param fix
         * @return
         */
        public Builder addFix(final String fix) {
            this.fixes.add(fix);
            return this;
        }

        /**
         * @return
         */
        public MetadataFixerResult build() {
            return MetadataFixerResultImpl.fromValues(this.status, this.fixes);
        }
    }
}
