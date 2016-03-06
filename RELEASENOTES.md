# Version 0.10 (February )

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
