# Version 1.2 (March 2, 2017)
PDFBox version downloadable from http://downloads.verapdf.org/rel/verapdf-installer.zip. Greenfield version downloadable from: http://downloads.verapdf.org/gf/verapdf-gf-installer.zip.

This is a maintenance release focused on bug fixing and improvements of the test infrastructure.

## Conformance checker:
- fixed cache issues in parsing embedded CMaps
- fixed multiple issues with glyph widths checks for embedded CID fonts
- fixed CIDSet entry validation
- fixed delimiter handling in parsing content streams
- ignore None colorants when checking DeviceN color spaces
- fixed validation of Order arrays in optional content groups
- fixed parsing of /ToUnicode map

## Policy checker:
- fixed plug-in infrastructure
- fixed handling of unknown feature types
- added error info into HTML reports in case of broken PDFs

## Documentation:
- updated developer samples
- updated GUI documentation

# Version 1.0 (January 9, 2017)
PDFBox version downloadable from http://downloads.verapdf.org/rel/verapdf-installer.zip. Greenfield version downloadable from: http://downloads.verapdf.org/gf/verapdf-gf-installer.zip.

## Application enhancements:
- fixed default values for extracted PDF features; and
- fixed removal of temporary files.

## Conformance checker:
- fixed cmap table parsing in TrueType fonts.

## Test corpus:
- changed Metadata File provenance test files from fail to pass (as discussed at the validation technical working group); and
- fixed xref table in test case 6-3-3-t01-pass-a.

# Version 0.28 (December 20, 2016)
Last pre-version 1.0 release. PDFBox version downloadable from http://downloads.verapdf.org/rel/verapdf-installer.zip. Greenfield version downloadable from: http://downloads.verapdf.org/gf/verapdf-gf-installer.zip.

## Application Enhancements
- schematron based policy checker implementation:
  * Example policy schemas on GitHub https://github.com/veraPDF/veraPDF-policy-docs/tree/master/Schemas; and
  * policy functionality available in CLI and GUI;
- greenfield implementation of feature extraction;
- greenfield implementation of metadata fixer;
- GUI now supports checking multiple files or a directory;
- HTML summary report for multiple file results;
- single file detailed report containing policy and feature information; and
- stability improvements and performance optimization of the Greenfield parser.

## Conformance checker
- fixed glyph width checks in case of exactly 1/1000 point difference;
- fixed default color space processing for Indexed color spaces;
- fixed Order array support for OCG checks in PDF/A-2; and
- fixed Unicode character maps support for PDF/A-1 Level A.

# Version 0.26 (November 16, 2016)
We've made two downloads available for out 0.26 release. There's the ususal version, based on Apache PDFBox and downloadable from: http://downloads.verapdf.org/rel/verapdf-installer.zip. For 0.26 we've also prepared the first beta release of our purpose built PDF parser and validation model, also known as the greenfield validator. This is downloadable from: http://downloads.verapdf.org/gf/verapdf-gf-installer.zip. It's not functionally complete yet as it only supports PDF/A validation. Full details of the release features are listed below.

## Conformance checker
- added the new rule for embedded files to be associated with the document or its parts (PDF/A-3 only).

## Application enhancements
- first beta release of greenfield PDF/A validation available as a limited functionality app;
- refactoring of sub-component and application configuration for reproducible execution;
- new BatchProcessor producing multi-item reports;
- batch processing is stream/event driven with event handlers for processing results; and
- report structures altered to accommodate batch processing.

## Code Quality
- publication of integration tests for Greenfield components;
- memory usage and execution times in test reports;
- example test report available here at time of writing: http://tests.verapdf.org/0.26.8/

## Test corpus
- added 7 new test files to cover the new rule in PDF/A-3 validation profile

## Disabled functionality
In order to accommodate batch reporting for this release we've had to sacrifice
redirecting output to user files. This isn't permanent and will be re-instated for the next release. The following functionality has been temporarily disabled:

### Standard release
- HTML report from CLI, HTML reporting will be a function of the dedicated reporter in the next release. HTML reports are still available from the GUI;
- the -pw option that allows the user to override the profiles wiki, this was only used to generate the HTML report so is not required;
- the -c load config option, config is automatically loaded from the app area and we're adding user config to the next release;
- the --reportfile, --reportfolder, and --overwriteReportFile as these need a rethink to accommodate batch processing.

### Greenfield release
The Greenfield release is missing all of the above plus:
- metadata fixing in GUI and CLI; and
- feature extraction in GUI and CLI.

# Version 0.24 (October 11, 2016)

## Conformance checker
- added extraction of the AFRelationship key for embedded files as a part of veraPDF feature extraction.

## Application enhancements
- implemented prototype of batch validation from CLI and GUI;
- implemented robust handling of run-time exceptions during batch processing; and
- added error info on the run-time exceptions to the validation report.

## Code Quality
- moved feature extraction and metadata fixing code to library; and
- tidied various compiler warnings.

# Version 0.22 (September 7, 2016)

## Application enhancements:
- changed default feature generation to document level features
- added a new GUI dialog for managing feature generation options
- added a user-friendly Java OutOfMemoryError with suggestions for reconfiguration
- CLI can now overwrite report files
- added help message when CLI processes STDIN stream
- synchronized the Web demo validation report with the CLI and GUI report styles

## Conformance checker fixes:
- removed the rules for validating file provenance information (based on veraPDF TWG discussion)
- fixed an issue with structure type mapping in Level A validation
- implemented resource caching for memory optimization

## Test corpus:
- converted all 'fail' test cases on file provenance information to 'pass' tests

# Version 0.20 (August 1, 2016)

## Application enhancements:
- added signature types to features report;
- depth of feature reporting now configurable; and
- altered log level of some validation methods.

## Conformance checker fixes:
- fix for validation of character encoding requirements of invisible fonts; and
- fix for ICC Profile mluc tag.

## Test corpus:
- 34 new test files for PDF/A-2b.

# Version 0.18 (July 5, 2016)

This beta release provides fixes for PDF/A Validation, enhanced functionality & usability fixes for the application, and additions to the test corpus. It also marks the launch of our beta documentation site.

## Application enhancements:
- suppress all PDFBox warnings in the CLI interface when parsing PDF documents
- generate error report instead of the exception in case of broken PDF documents
- added a new CLI option to save XML report to a separate file in recursive PDF processing

## veraPDF characterisation plugins
  - enhancements to example pure java plugins available
  - plugins now configurable through dedicated config file

## Conformance checker fixes:
- ignore DeviceGray color space in soft mask images
- treat glyph with GID 0 as “.notdef” in case of Type0 fonts
- fixed validation of role map for non-standard structure elements (Level A)
- fixed validation of page size implementation limits in case of negative width or height
- fixed validation of non-standard embedded CMaps referenced from other CMaps

## Test corpus:
- added 180 new test files for parts 2 and 3

## Infrastructure
- test coverage now monitered by Codecov online service
- integration tests for 2u and 3b validation profiles added
- using codacy and covertiy online code QA services

# Version 0.16 (June 3, 2016)

This beta release features the full support of all PDF/A-2 and PDF/A-3 requirements (all levels). Together with earlier support of PDF/A-1 validation, it represents the first full support for all PDF/A parts and conformance levels.

## Features
- Conformance checker
  - added validation of digital signature requirements
  - added extraction of color space info from JPEG2000 images
  - added validation of permissions dictionary (Parts 2 and 3)
  - PDF/A-2B fix: correct implementation of CIDSystemInfo entry requrements
  - command line support for plugin execution to extend feature extraction

## veraPDF characterisation plugins
  - first set of example pure java plugins available
  - optional sample plugin pack available through installer

## Test corpus:
  - Added further 112 atomic test files for parts 2 and 3

## Infrastructure:
  - added support for all parts and conformance levels at http://demo.verapdf.org


# Version 0.14 (May 5, 2016)

This beta release features Transparency and Unicode character map validation in
PDF/A-2 levels B and U.

## Features
- Conformance checker:
  - added all transparency-related validation rules in PDF/A-2 and PDF/A-3
  - added full Level U support in PDF/A-2 and PDF/A-3
  - code refactoring to synchronize GUI, API and CLI interfaces
  - PDF/A-1B fix: check both Tiling patterns used as different fill and stroke
    colour spaces in the same drawing operations
  - added initial versions of PDF/A-2U, PDF/A-2A, PDF/A-3U, PDF/A-3A validation
    profiles. We now have initial validation for all PDF/A flavours.

## Test corpus:
  - added a further 65 atomic test files for PDF/A-2 specific requirements

## Infrastructure:
  - a demo of the REST interface is available at http://demo.verapdf.org

# Version 0.12 (March 31, 2016)

This beta release features improved PDF/A-2b and PDF/A-3b validation and the fully featured REST API.

## Features
- Conformance checker:
  - PDF/A-2 and PDF-A/3 improvements: implement checks for optional content, JPEG2000 requirements
  - full compliance with BFO test suite (PDF/A-2b)
  - PDF/A-1b fix: check for form field appearance
  - code refactoring to enable PDF model implementation via different PDF parsers
  - performance and memory optimization

- Test corpus:
  - full coverage of all predefined XMP properties

- Documentation
  - first version of wiki on all validation rules availabe at: https://github.com/veraPDF/veraPDF-validation-profiles/wiki

- Command line:
  - CLI now supports metadata fixing

- Infrastructure
  - veraPDF-library project refactored into multiple projects.
  - PDF Box validator implementation in separate project.
  - Automated source packaging with dependencies.

# Version 0.10 (February 29, 2016)

This beta release features command line interface enhancements

## Features
- Conformance checker:
 - new implementation of the XMP validation
 - proper CharSet / CIDSet validation

- Command line:
 - processes stdin if no file paths are supplied for use in *nix pipes;
 - directory and recursive sub-directory processing; and
 - text mode output with summarised output

- Test corpus:
 - initial set of PDF/A-2 test files

## Fixes
- Conformance checker:
 - fixed CMap / WMode validation
 - minor fixes in PDF/A-2b and PDF/A-3b validation profiles
 - based on TWG resolution, fixed validation normal appearance object type (Dict vs Stream) for Button widgets

- Command line fixes:
 - all CLI output for a single file now in one XML document; and
 - error output now all to stderr, keeping stdin clean;

***

# Version 0.8 (December 22 2015)

This beta release features a command line interface for validation and feature extraction, with supporting install scripts.

## Features

- Refactored plug-in architecture.
- Re-designed command line interface for PDF/A validation and feature reporting.
- Updated validation profile syntax.
- Simplified machine-readable report format.
- Bug fixes for:
 - comparison of Info dictionary and XMP metadata (PDF/A-1);
 - support for missing resources and resource inheritance mechanism (PDF/A-1); and
 - parsing TrueType fonts with zero-length tables.
- Synchronization with PDFBox 2.0 RC1 library.

## Infrastructure

- Installer adds command line scripts.
- Refactored integration tests.

## Test corpus

***

# Version 0.6 (October 30, 2015)

This beta release includes a fully functional, internally-tested implementation for complete PDF/A-1b validation, PDF Feature Report generation, and the Metadata Fixer.

## Features

- Stable (beta version) implementation of the formal PDF model for PDF/A-1b
- Prototype the formal PDF model for PDF/A-1a and PDF/A-2b, 3b
- Minor refactoring and stricter naming conventions in validation rules for PDF/A-1b
- Prototype validation rules for PDF/A-1a and PDF/A-2b, 3b
- Prototype implementation of the Metadata Fixer
- Prototype implementation of the plug-in architecture for PDF Feature Report generation
- Optimized performance for PDF/A font rules validation (glyphs presence, widths consistency)

## Infrastructure

- Added cross-platform installer
- Full coverage of Bavaria, Isartor, veraPDF test suites in automated integration tests

## Test corpus

- New test files for the use of Device colour spaces and XMP metadata
- 200 new atomic test files for PDF/A-1b extending Isartor and Bavaria test suites

***

# Version 0.4 (September 16, 2015)

The release includes a fully functional implementation (alpha version) for the complete PDF/A-1B validation and the PDF Feature Report generation

## Features

- A number of bug fixes in the implementation of the formal PDF model for PDF/A Level B validation
- Added missing validation rules for the full coverage of ISO 19005-1:2005, 19005-1:2005/Cor.1:2011, 19005-1:2005/Cor.1:2007, 19005-1:2005/Cor.2:2011, Level B
- Complete implementation of the PDF Feature Report generation
- Minor improvements in the GUI and the Human-readable Report in HTML format
- Added extra parameters to limit the number of rule failures and the number of reported errors
- Optimized performance

## Infrastructure

- Increased unit test coverage to 70%
- Increased the number of atomic validation tests to 226, including the full coverage of tests against Isartor test corpus

## Test corpus

- Total 176 atomic test files for PDF/A-1B extending Isartor test suite

***

# Version 0.2 (July 15, 2015)

The release includes a fully functional prototype for the PDF/A-1b validation and the PDF Feature Report generation

## Features

- The formal PDF model for PDF/A Level B validation
- The set of validation rules covering ISO 19005-1:2005, 19005-1:2005/Cor.1:2011, 19005-1:2005/Cor.1:2007, 19005-1:2005/Cor.2:2011, Level B
- Implementation of the rules covering the following sections of ISO 19005-1:
  - 6.1 File structure
  - 6.2 Graphics
  - 6.4 Transparency
  - 6.5 Annotations
  - 6.6 Actions
  - 6.7 Metadata
  - 6.9 Interactive Forms
- Initial implementation of the PDF Feature Report generation
- Minor imporvements in the GUI and the Human-readable Report in HTML format

## Infrastructure

- Fully automated build and deployment procedures based on quality gateways
- More restrictive quality criteria (50% unit test coverage, 80% documentation of public API, zero-tolerance to critical Sonar issues)
- Integration tests framework with 150 atomic validation test

## Test corpus

- Draft set of new test files for ISO 19005-1:
  - 6.1 File structure
  - 6.2 Graphics (under review of veraPDF Technical Workgroup of the PDF Association)

***

# Version 0.1 (June 5, 2015)

This is a minimal viable product release of the veraPDF Conformance Checker

## Features

- Implementation of the generic validation model
- Parser for validation profile
- Initial version of machine-readable reports
- Proof of concept for human-readable reports in HTML format
- Initial version of the CLI interface for the Conformance Checker
- Initial version of the GUI application
- Partial implementation of the COS layer for the PDF/A validation model
- PDF/A-1 validation profiles for ISO 19005-1:2005, 6.1 File structure

## Test corpus

- Draft set of test files for ISO 19005-1:2005, 6.1 File structure

***

<img src="http://verapdf.openpreservation.org/wp-content/uploads/sites/3/2015/06/veraPDF-logo-200.png" width="88" alt="veraPDF Consortium"/>
© 2015 [veraPDF Consortium](http://www.verapdf.org)
