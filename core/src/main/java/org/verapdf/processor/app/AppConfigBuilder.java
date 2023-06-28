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
