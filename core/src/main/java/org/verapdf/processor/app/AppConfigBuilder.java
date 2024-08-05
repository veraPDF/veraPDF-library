/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor.app;

import java.nio.file.FileSystems;
import org.verapdf.processor.FormatOption;

public class AppConfigBuilder {

    private ProcessType _type = ProcessType.VALIDATE;
    private String _fixerFolder = FileSystems.getDefault().getPath("").toString(); //$NON-NLS-1$
    private FormatOption _format = FormatOption.XML;
    private String _wikiPath = "https://github.com/veraPDF/veraPDF-validation-profiles/wiki/"; //$NON-NLS-1$
    private String _policyFile = FileSystems.getDefault().getPath("").toString(); //$NON-NLS-1$
    private boolean _isVerbose = false;

    private AppConfigBuilder() {
        super();
    }

    private AppConfigBuilder(VeraAppConfig config) {
        super();
        this._type = config.getProcessType();
        this._fixerFolder = config.getFixesFolder();
        this._format = config.getFormat();
        this._isVerbose = config.isVerbose();
        this._wikiPath = config.getWikiPath();
        this._policyFile = config.getPolicyFile();
    }

    public AppConfigBuilder type(ProcessType type) {
        this._type = type;
        return this;
    }

    public AppConfigBuilder fixerFolder(String fixerFold) {
        this._fixerFolder = fixerFold;
        return this;
    }

    public AppConfigBuilder format(FormatOption format) {
        this._format = format;
        return this;
    }

    public AppConfigBuilder isVerbose(boolean isVerbose) {
        this._isVerbose = isVerbose;
        return this;
    }

    public AppConfigBuilder wikiPath(String path) {
        this._wikiPath = path;
        return this;
    }

    public AppConfigBuilder policyFile(String policy) {
        this._policyFile = policy;
        return this;
    }

    public static AppConfigBuilder fromConfig(VeraAppConfig config) {
        return new AppConfigBuilder(config);
    }

    public static AppConfigBuilder defaultBuilder() {
        return new AppConfigBuilder();
    }

    public VeraAppConfig build() {
        return new VeraAppConfigImpl(this._type, this._fixerFolder, this._format,
                this._isVerbose, this._wikiPath, this._policyFile);
    }
}
