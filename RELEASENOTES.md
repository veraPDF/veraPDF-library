Version 1.14-RC (June 10, 2019)
==============================

### Applications
- fixed installer CLI args not forwarded [[#apps-253][]], [[#iss-1021][]]
- fixed Java 11 XML bind dependencies [[#apps-254][]], [[#iss-986][]]
- command line application terminsation codes [[#apps-255][]], [[#apps-256][]], [[#iss-841][]]

## PDF Model:
- added processColor link for PDDeviceN [[#mod-168][]], [[#iss-902][]]

## PDF Parser:
- fixed XRefStream handling for trailers [[#par-359][]]
- fixed space skipping afer RD for type1 [[#par-360][]], [[#par-361][]]
- fixed various issues with AES decryption [[#par-362][]], [[#par-364][]], [[#par-365][]]
- fixed font encoding issue [[#par-363][]]
- fixed buffered in filter and COSStream problems [[#par-366][]]
- fixed SOF exception for colorspaces [[#par-368][]]
- fixed date parser issue [[#par-368][]]
- fixed creation of PD resources from dictionary when cos object empty  [[#par-370][]]
- logging improvements [[#par-367][]], [[#par-371][]]
- fixed toUnicode for PDF/A-1 glyph name check [[#par-372][]], [[#iss-1012][]]
- add underlying color space for PDPattern [[#par-373][]], [[#iss-984][]]

## Core Library:
- fixed xmp-core byte buffer [[#lib-974][]]
- logging improvements [[#lib-980][]], [[#lib-1023][]]
- input stream and file processing XML results now consistent [[#lib-1020][]], [[#iss-1014][]]
- fixed Java 11 XML bind dependencies  [[#lib-1022][]], [[#iss-986][]]
- performance tweaks  [[#lib-1028][]]

## PDF Validation:
- fix metadata creation [[#val-270][]]
- fix null pointer exception when processing glyphs [[#val-271][]]
- fix null pointer exception when validating embedded files [[#val-273][]], [[#iss-976][]]
- fxed graphic state initial colorspace creation and font inheritance  [[#val-274][]], [[#iss-975][]], [[#iss-978][]]
- deny operstors q, Q, cm inside Text object [[#val-276][]], [[#iss-985][]]
- preflight passes, veraPDF shows clause="6.2.11.4" error [[#val-277][]], [[#iss-1019][]]
- fix Ignore trailing zero for info dictionary values during metadata info match xmp check [[#val-278][]], [[#iss-1017][]]
- fixed Java 11 XML bind dependencies  [[#val-279][]], [[#iss-986][]]
- fix processColorspace model link implementation for PDDeviceN [[#val-280][]], [[#iss-902][]]
- added warning for invalid color space objects [[#val-281][]], [[#iss-797][]]
- fix process color operator flag logic, add underlying color space processing for PDPattern [[#val-282][]], [[#iss-984][]]

## PDF Box Validation:
- fix metadata creation [[#pdf-195][]]
- fixed Java 11 XML bind dependencies  [[#pdf-197][]], [[#iss-986][]]
- fix processColorspace model link implementation for PDDeviceN [[#pdf-198][]], [[#iss-902][]]

[#iss-797]: https://github.com/veraPDF/veraPDF-library/issues/797
[#iss-841]: https://github.com/veraPDF/veraPDF-library/issues/841
[#iss-902]: https://github.com/veraPDF/veraPDF-library/issues/902
[#iss-975]: https://github.com/veraPDF/veraPDF-library/issues/975
[#iss-976]: https://github.com/veraPDF/veraPDF-library/issues/976
[#iss-978]: https://github.com/veraPDF/veraPDF-library/issues/978
[#iss-984]: https://github.com/veraPDF/veraPDF-library/issues/984
[#iss-985]: https://github.com/veraPDF/veraPDF-library/issues/985
[#iss-986]: https://github.com/veraPDF/veraPDF-library/issues/986
[#iss-1012]: https://github.com/veraPDF/veraPDF-library/issues/1012
[#iss-1014]: https://github.com/veraPDF/veraPDF-library/issues/1014
[#iss-1017]: https://github.com/veraPDF/veraPDF-library/issues/1017
[#iss-1019]: https://github.com/veraPDF/veraPDF-library/issues/1019
[#iss-1021]: https://github.com/veraPDF/veraPDF-library/issues/1021

[#apps-253]: https://github.com/veraPDF/veraPDF-apps/pull/253
[#apps-254]: https://github.com/veraPDF/veraPDF-apps/pull/254
[#apps-255]: https://github.com/veraPDF/veraPDF-apps/pull/255
[#apps-256]: https://github.com/veraPDF/veraPDF-apps/pull/256

[#lib-974]: https://github.com/veraPDF/veraPDF-library/pull/974
[#lib-980]: https://github.com/veraPDF/veraPDF-library/pull/980
[#lib-1020]: https://github.com/veraPDF/veraPDF-library/pull/1020
[#lib-1022]: https://github.com/veraPDF/veraPDF-library/pull/1022
[#lib-1023]: https://github.com/veraPDF/veraPDF-library/pull/1023
[#lib-1028]: https://github.com/veraPDF/veraPDF-library/pull/1028

[#mod-168]: https://github.com/veraPDF/veraPDF-model/pull/168

[#par-359]: https://github.com/veraPDF/veraPDF-parser/pull/359
[#par-360]: https://github.com/veraPDF/veraPDF-parser/pull/360
[#par-361]: https://github.com/veraPDF/veraPDF-parser/pull/361
[#par-362]: https://github.com/veraPDF/veraPDF-parser/pull/362
[#par-363]: https://github.com/veraPDF/veraPDF-parser/pull/363
[#par-364]: https://github.com/veraPDF/veraPDF-parser/pull/364
[#par-365]: https://github.com/veraPDF/veraPDF-parser/pull/365
[#par-366]: https://github.com/veraPDF/veraPDF-parser/pull/366
[#par-367]: https://github.com/veraPDF/veraPDF-parser/pull/367
[#par-368]: https://github.com/veraPDF/veraPDF-parser/pull/368
[#par-370]: https://github.com/veraPDF/veraPDF-parser/pull/370
[#par-371]: https://github.com/veraPDF/veraPDF-parser/pull/371
[#par-372]: https://github.com/veraPDF/veraPDF-parser/pull/372

[#pdf-195]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/195
[#pdf-197]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/197
[#pdf-198]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/198

[#val-270]: https://github.com/veraPDF/veraPDF-validation/pull/270
[#val-271]: https://github.com/veraPDF/veraPDF-validation/pull/271
[#val-273]: https://github.com/veraPDF/veraPDF-validation/pull/273
[#val-274]: https://github.com/veraPDF/veraPDF-validation/pull/274
[#val-276]: https://github.com/veraPDF/veraPDF-validation/pull/276
[#val-277]: https://github.com/veraPDF/veraPDF-validation/pull/277
[#val-277]: https://github.com/veraPDF/veraPDF-validation/pull/278
[#val-277]: https://github.com/veraPDF/veraPDF-validation/pull/279
[#val-280]: https://github.com/veraPDF/veraPDF-validation/pull/280
[#val-281]: https://github.com/veraPDF/veraPDF-validation/pull/281
[#val-282]: https://github.com/veraPDF/veraPDF-validation/pull/282

Version 1.12 (May 9, 2018)
==========================

## PDF Parser:
- added support for PDF files over 2Gb [[#par-334][]]
- fixed date parsing issues with trailing apostrophe [[#par-335][]]
- fixed bug with Standard Encoding in PostScript font programs [[#par-340][]]
- fixed issue parsing digital signatures for files below 1024 bytes [[#par-343][]], [[#par-351][]], [[#par-353][]]
- fixed issue with recognition of standard font with differences entry and parsing empty differences array [[#par-345][]][[#par-350][]]
- close streams properly during parsing and ensure tempfile deletion [[#par-346][]], [[#par-352][]], [[#par-353][]]
- fixed signature EOF stream logic causing ByteRange issues[[#par-348][]]
- Fixed detection of the CFF font charset in case of a predefined charset with incomplete glyph plus issues with Adobe Type 3 Font, Adobe Type 1 Font and ASCII85 parsing [[#par-348][]], [[#par-355][]]
- added check to disallow and report Type 1 PFB fonts [[#par-349][]]

## Conformance Checker:
- add Identifier to validator type for reporting, added details to HTML report [[#val-261][]], [[#lib-940][]]
- fixed issue parsing digital signatures for files below 1024 bytes [[#val-265][]]
- exclude process colors from spot color validation in DeviceN / NChannel color spaces for PDF/A-2 and 3 [[#val-267][]] [[#pdf-191][]]
- fixed metadata extensions support across different PDF/A levels [[#lib-947]]
- fixed bug with automatic selection and processing of PDF/A flavour  [[#pdf-190][]]
- fixed validation of smooth shading color spaces and inline images in presence of default color space [[#val-263][]]
- fixed bug in embedded file features data extraction [[#lib-951][]]
- fixed bug with validation rule caching [[#lib-963][]]

## Application enhancements:
- added fixes for thread saftey and multithreading support [[#apps-246][]], [[#apps-248][]], [[#par-342][]], [[#lib-941][]], [[#lib-960][]], [[#val-262][]],  [[#pdf-189][]], [[#lib-950][]] [[#apps-244][]]
- added multi-process parallelization in the CLI [[#apps-242][]]
- improved formatting of XML reports [[#apps-243][]]
- fixed plug-ins loading mechanism [[#apps-244][]]
- updated copyright statement for GUI  [[#apps-245][]]

## Project infrastructure
- merged veraPDF-xmp project with library [[#lib-964][]]

[#lib-940]: https://github.com/veraPDF/veraPDF-library/pull/940/
[#lib-941]: https://github.com/veraPDF/veraPDF-library/pull/941/
[#lib-947]: https://github.com/veraPDF/veraPDF-library/pull/947/
[#lib-950]: https://github.com/veraPDF/veraPDF-library/pull/950/
[#lib-951]: https://github.com/veraPDF/veraPDF-library/pull/951/
[#lib-960]: https://github.com/veraPDF/veraPDF-library/pull/960/
[#lib-963]: https://github.com/veraPDF/veraPDF-library/pull/963/
[#lib-964]: https://github.com/veraPDF/veraPDF-library/pull/964/

[#par-334]: https://github.com/veraPDF/veraPDF-parser/pull/334/
[#par-335]: https://github.com/veraPDF/veraPDF-parser/pull/335/
[#par-340]: https://github.com/veraPDF/veraPDF-parser/pull/340/
[#par-342]: https://github.com/veraPDF/veraPDF-parser/pull/342/
[#par-343]: https://github.com/veraPDF/veraPDF-parser/pull/343/
[#par-345]: https://github.com/veraPDF/veraPDF-parser/pull/345/
[#par-346]: https://github.com/veraPDF/veraPDF-parser/pull/346/
[#par-347]: https://github.com/veraPDF/veraPDF-parser/pull/347/
[#par-348]: https://github.com/veraPDF/veraPDF-parser/pull/348/
[#par-349]: https://github.com/veraPDF/veraPDF-parser/pull/349/
[#par-350]: https://github.com/veraPDF/veraPDF-parser/pull/350/
[#par-351]: https://github.com/veraPDF/veraPDF-parser/pull/351/
[#par-352]: https://github.com/veraPDF/veraPDF-parser/pull/352/
[#par-353]: https://github.com/veraPDF/veraPDF-parser/pull/353/
[#par-355]: https://github.com/veraPDF/veraPDF-parser/pull/355/

[#val-261]: https://github.com/veraPDF/veraPDF-validation/pull/261/
[#val-262]: https://github.com/veraPDF/veraPDF-validation/pull/262/
[#val-263]: https://github.com/veraPDF/veraPDF-validation/pull/263/
[#val-265]: https://github.com/veraPDF/veraPDF-validation/pull/265/
[#val-267]: https://github.com/veraPDF/veraPDF-validation/pull/267/

[#pdf-188]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/189/
[#pdf-189]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/189/
[#pdf-190]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/190/
[#pdf-191]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/191/
[#pdf-192]: https://github.com/veraPDF/veraPDF-pdfbox-validation/pull/192/

[#apps-242]: https://github.com/veraPDF/veraPDF-apps/pull/242/
[#apps-243]: https://github.com/veraPDF/veraPDF-apps/pull/243/
[#apps-244]: https://github.com/veraPDF/veraPDF-apps/pull/244/
[#apps-245]: https://github.com/veraPDF/veraPDF-apps/pull/245/
[#apps-246]: https://github.com/veraPDF/veraPDF-apps/pull/246/
[#apps-248]: https://github.com/veraPDF/veraPDF-apps/pull/248/

Version 1.10 (November 30, 2017)
==========================

## PDF Parser:
- fixed retrieval of glyph widths from PS and CFF font programs (multiple issues);
- optimized creation and cleanup of temporary files; and
- optimized parsing of text-related data in PDF documents (up to 3 times faster for PDF documents with primarily text content).

## Conformance Checker:
- fixed checks on the presence of SMask, NeedApperane keys in case of invalid value types;
- fixed ByteRange check of digital signatures in case of incrementally updated files;
- fixed Unicode checks in PDF/A-1A validation for Type1 and Type3 fonts;
- fixed inheritance of /FT entry in Widget annotations; and
- fixed role map retrieval in case of remapping standard structure types.

## Policy Checker:
- fixed Schematron warnings in Policy checks; and
- fixed issue with access to temp reports from Schematron stylesheets on some systems and in case of Java 9.

## Application enhancements:
- fixed various issues caused by Java 9, particularly a problem in the start up scripts; and
- fixed access to PDF resources in case of veraPDF integration into web applications.


Version 1.8 (August 9, 2017)
==========================

## PDF parser:
- fixed PS-specific issues in parsing embedded CMaps, ToUnicode maps and PS Type1 fonts;
- implemented the protection against (invalid) loops in PDF tree structures;
- fixed parsing of CIDSet and CharSet and their comparison with the glyph collection in the embedded font subset (PDF/A-2 and PDF/A-3);
- implemented support for CalCMYK colour space as specified in ISO 32000-1; and
- fixed initialization and inheritance of graphics state for tiling patterns, Type3 fonts and form XObjects.

## Conformance checker:
- implemented check for glyph width consistence in case of Type3 fonts;
- implemented check for the Private Unicode Area use in Level A conformance;
- implemented validation of transfer functions in Halftone dictionaries (PDF/A-2 and PDF/A-3);
- added validation of MIME type value for embedded files (PDF/A-3);
- refactored the validation model to check for presence of certain keys, even if they refer to empty arrays/collections;
- fixed misspelled predefined CMap names for GBK2K-H and GBK2K-V;
- fixed validation of UTF8 encoding for role map names (PDF/A-2 and PDF/A-3); and
- fixed detection of references to Associated files from marked content sequences (PDF/A-3)

## Feature report generation and policy checker:
- added new features to the report:
  * PDF Version;
  * form field names and values; and
  * page labels.

## Application enhancements:
- implemented automatic configuration of veraPDF feature report by a custom Policy profile;
- implemented workaround for the veraPDF GUI appearance on high resolution screens;
- fixed problems with spaces if full JRE path used on Windows; and
- fixed problems handling spaces in installer path.

## Infrastructure:
- implemented automatic generation of PREFORMA test reports; and
- both greenfield anb PDF Box versions now built and packaged from a single branch

## Test corpus:
- fixed veraPDF test files to comply with PDFA TechNote 0010; and
- fixed outlines Count value to comply with ISO 32000-1

# Version 1.6 (June 6, 2017)

## Desktop Applications:
- GUI and CLI now capable of checking for updated version of the software.

## Conformance Checker:
Updated validation logic to comply with Technical Working Group resolutions:
- color spaces are validated now when specified in the content stream;
- a CMap may refer only to predefined CMaps in the ‘usecmap’ operator; and
- OpenType causes an ‘unsupported font type’ error in PDF/A-1 validation.

Other conformance checker fixes and improvements:
- fixed calculation of glyph widths for embedded CFF fonts in some special cases;
- fixed validation of digital signature ByteRange array;
- fixed recursive links of color spaces;
- fixed validation of spaces in the indirect object header;
- fixed Crypt filter handling;
- fixed misprint in ‘Lbl’ structure tag; and
- added support for glyphs with CIDs > 65535 (with an appropriate validation. error)

## Policy checker:
- fixed feature extraction from encrypted documents.

## Test corpus:
- added new test corpus for the Technical Working Group resolutions;
- fixed test file for digital signature validation; and
- fixed test files for Extension Schema definitions in XMP.

# Version 1.4 (April 20, 2017)

## Conformance Checker:
- significant optimization of performance in the greenfield PDF parser
- fixed parsing of embedded PS Type1 fonts
- fixed default value of WMode entry for embedded CMaps
- fixed inline image data parsing
- fixed digital signature parsing

## Reporting:
- refactored feature extraction
- pretty formatted XML reports
- clearer XML structure in veraPDF reports
- improvements to the HTML reports

## Policy Checker:
- added GUI wizard for creating custom policy files

## Infrastructure
- release artifacts now deployed to Maven Central
- started transfer to external static code QA service

## Test corpus:
- aligned the existing veraPDF corpus and added 80 new test files to cover Technical Working Group resolutions

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
