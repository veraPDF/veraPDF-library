/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor.reports;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * <p>
 * Created 18 Apr 2017:18:24:01
 */

public interface BatchJobSummary {

    /**
     * Gets number of failed jobs.
     *
     * @return the number of jobs that failed to execute due to an exception.
     */
    int getFailedJobCount();

    /**
     * Gets total number of validation jobs.
     *
     * @return the total number of validation jobs in the batch.
     */
    int getTotalJobCount();

    /**
     * Gets number of successful jobs.
     *
     * @return the number of successful jobs
     */
    int getSuccessfulJobCount();
}
