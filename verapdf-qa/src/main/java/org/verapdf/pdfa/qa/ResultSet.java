/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.util.Set;

import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface ResultSet {
    /**
     * @return the {@link ResultSetDetails} for the instance
     */
    public ResultSetDetails getDetails();

    /**
     * @return the {@link CorpusDetails} for the instance
     */
    public CorpusDetails getCorpusDetails();

    /**
     * @return the {@link ValidationProfile} used for the result set
     */
    public ValidationProfile getValidationProfile();

    /**
     * @return the {@code Set} of {@link Result}s
     */
    public Set<Result> getResults();
    
    /**
     * @return
     */
    public Set<Incomplete> getExceptions();

    /**
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     *
     */
    public static final class Result {
        private final CorpusItem corpusItem;
        private final ValidationResult result;

        Result(final CorpusItem corpusItem, final ValidationResult result) {
            this.corpusItem = corpusItem;
            this.result = result;
        }

        /**
         * @return the corpusItem
         */
        public CorpusItem getCorpusItem() {
            return this.corpusItem;
        }

        /**
         * @return the result
         */
        public ValidationResult getResult() {
            return this.result;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int hashResult = 1;
            hashResult = prime
                    * hashResult
                    + ((this.corpusItem == null) ? 0 : this.corpusItem
                            .hashCode());
            hashResult = prime * hashResult
                    + ((this.result == null) ? 0 : this.result.hashCode());
            return hashResult;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Result other = (Result) obj;
            if (this.corpusItem == null) {
                if (other.corpusItem != null)
                    return false;
            } else if (!this.corpusItem.equals(other.corpusItem))
                return false;
            if (this.result == null) {
                if (other.result != null)
                    return false;
            } else if (!this.result.equals(other.result))
                return false;
            return true;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public String toString() {
            return "Result [corpusItem=" + this.corpusItem + ", result="
                    + this.result + "]";
        }

    }
    
    /**
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     *
     */
    public static class Incomplete {
        private final CorpusItem corpusItem;
        private final String cause;
        
        /**
         * @param corpusItem
         * @param cause
         */
        public Incomplete(final CorpusItem corpusItem, final Exception cause) {
            this.corpusItem = corpusItem;
            this.cause = cause.getMessage();
        }

        /**
         * @return the corpusItem
         */
        public CorpusItem getCorpusItem() {
            return this.corpusItem;
        }

        /**
         * @return the cause
         */
        public String getCause() {
            return this.cause;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.cause == null) ? 0 : this.cause.hashCode());
            result = prime * result
                    + ((this.corpusItem == null) ? 0 : this.corpusItem.hashCode());
            return result;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Incomplete other = (Incomplete) obj;
            if (this.cause == null) {
                if (other.cause != null)
                    return false;
            } else if (!this.cause.equals(other.cause))
                return false;
            if (this.corpusItem == null) {
                if (other.corpusItem != null)
                    return false;
            } else if (!this.corpusItem.equals(other.corpusItem))
                return false;
            return true;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public String toString() {
            return "Incomplete [corpusItem=" + this.corpusItem + ", cause=" + this.cause
                    + "]";
        }
        
        
    }
}
