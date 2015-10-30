# Version 0.1 (June 5, 2015)

This is a inimal viable product release of the veraPDF Conformance Checker

## Features

- Implementation of the generic validation model
- Parser for validation profiles
- Initial version of machine-readable reports
- Proof of concept for human-readable reports in HTML format
- Initial version of the CLI interface for the Conformance Checker
- Initial version of the GUI application 
- Partial implementation of the COS layer for the PDF/A validation model
- PDF/A-1 validation profiles for ISO 19005-1:2005, 6.1 File structure

## Test corpus

- Draft set of test files for ISO 19005-1:2005, 6.1 File structure

***

# Version 0.2 (July 15, 2015)

The releases includes a fully functional prototype for the PDF/A-1b valitation and the PDF Feature Report generation

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
- More restrictive quality criteria (50% unit test coverage, 80% documentation of public API, zero-tolerance to critial Sonar issues)
- Integration tests framework with 150 atomic validation test

## Test corpus

- Draft set of new test files for ISO 19005-1: 
  - 6.1 File structure
  - 6.2 Graphics (under review of veraPDF Technical Workgroup of the PDF Association)

***

# Version 0.4 (September 16, 2015)

The releases includes a fully functional implementation (alpha version) for the complete PDF/A-1b valitation and the PDF Feature Report generation

## Features

- A number of bug fixes in the implementation of the formal PDF model for PDF/A Level B validation
- Added missing validation rules for the full coverage of ISO 19005-1:2005, 19005-1:2005/Cor.1:2011, 19005-1:2005/Cor.1:2007, 19005-1:2005/Cor.2:2011, Level B
- Complete implementation of the PDF Feature Report generation
- Minor imporvements in the GUI and the Human-readable Report in HTML format
- Added extra parameters to limit the number of rule failtures and the number of reported errors
- Optimized performance

## Infrastructure

- Increased unit test coverage to 70%
- Increased the number of atomic validation tests to 226, including the full coverage of tests against Isartor test corpus

## Test corpus

- Total 176 atomic test files for PDF/A-1B extending Isartor test suite

***

# Version 0.6 (October 30, 2015)

The releases includes a fully functional and internally tested implementation (beta version) for the complete PDF/A-1b valitation, the PDF Feature Report generation and the Metadata Fixer.

## Features

- Bug fixes in the implementation of the formal PDF model for PDF/A Level B validation
- Minor refactoring and stricter naming conventions in validation rules for ISO 19005-1:2005, 19005-1:2005/Cor.1:2011, 19005-1:2005/Cor.1:2007, 19005-1:2005/Cor.2:2011, Level B
- Initial set of valudation rules for ISO 19005-1:2005 Level A, ISO 19005-2:2011 Level B, ISO 19005-3:2012 Level B
- Initial implementation of the Metadata Fixer
- Initial implementation of the plug-in architecture for PDF Feature Report generation
- Optimized performance for fonts validation (glyphs presence, widths consistency)

## Infrastructure

- Added wizard-based installation
- Full coverage of Bavaria, Isartor, veraPDF test suites for PDF/A-1B

## Test corpus

- Full coverage of
- Total 176 atomic test files for PDF/A-1B extending Isartor test suite

***

<img src="http://verapdf.openpreservation.org/wp-content/uploads/sites/3/2015/06/veraPDF-logo-200.png" width="88" alt="veraPDF Consortium"/>
© 2015 [veraPDF Consortium](http://www.verapdf.org)
