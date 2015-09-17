<?xml version="1.0"?>
<!--
    Transform an XML Validation Report into presentable HTML.
    Author: Maksim Bezrukov
    Version: 1.0
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:profilens="http://www.verapdf.org/ValidationProfile">

    <xsl:output method="html"/>

    <!-- Adding validation profile -->
    <xsl:param name="profilePath"/>
    <xsl:variable name="profile" select="document($profilePath)"/>

    <!-- HTML header and body wrapper -->
    <xsl:template match="/">
        <html>
            <head>
                <style>
                    <xsl:text>body{font-family: sans-serif;}</xsl:text>
                </style>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
                <script type="text/javascript">
                    if(typeof $ === 'function'){
                    $(document).ready(function(){
                    $(".hideable").hide();
                    $(".hide-tr").show();
                    $(".hide-tr").click(function(){
                    $("." + $(this).attr("data-target")).toggle();
                    var prevText = $(this).text();
                    $(this).text($(this).attr('data-translation-toggle'));
                    $(this).attr('data-translation-toggle', prevText)
                    return false;
                    });
                    });
                    }
                </script>
                <title>Validation Report</title>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <!-- Validation Report header -->
    <xsl:template match="report">

        <!-- Header image and overall title -->
        <p>
            <img alt="veraPDF Logo" src="data:image/png;base64,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"/>
        </p>
        <h1 align="left">
            <strong>
                <b>Validation Report</b>
            </strong>
        </h1>

        <!-- General information -->
        <table border="0" id="table1">
            <tr>
                <td width="200">
                    <b>Validation Profile:</b>
                </td>
                <td>
                    <xsl:value-of select="/report/validationInfo/profile/name"/>
                </td>
            </tr>
            <!--<tr>
                <td width="200">
                    <b>Validation Profile checksum:</b>
                </td>
                <td>
                    <xsl:value-of select="/report/validationInfo/profile/hash"/>
                </td>
            </tr>-->
            <tr>
                <td width="200">
                    <xsl:if test="/report/validationInfo/result/compliant = 'true'">
                        <font color="green">
                            <b>PDF is compliant:</b>
                        </font>
                    </xsl:if>
                    <xsl:if test="/report/validationInfo/result/compliant = 'false'">
                        <font color="red">
                            <b>PDF is compliant:</b>
                        </font>
                    </xsl:if>
                </td>
                <td>
                    <xsl:if test="/report/validationInfo/result/compliant = 'true'">
                        <font color="green">
                            <b>Yes</b>
                        </font>
                    </xsl:if>
                    <xsl:if test="/report/validationInfo/result/compliant = 'false'">
                        <font color="red">
                            <b>No</b>
                        </font>
                    </xsl:if>
                </td>
            </tr>
            <tr>
                <td width="200">
                    <b>Statement:</b>
                </td>
                <td>
                    <xsl:value-of select="/report/validationInfo/result/statement"/>
                </td>
            </tr>
        </table>

        <xsl:apply-templates select="/report/validationInfo/result/summary"/>

        <h2>Detailed information</h2>

        <table border="0" id="table3">

            <xsl:apply-templates select="/report/validationInfo/result/details/rules/rule"/>

        </table>


    </xsl:template>

    <!-- Summary information -->
    <xsl:template match="/report/validationInfo/result/summary">
        <h2>Summary</h2>
        <table border="0" id="table2">
            <tr>
                <td width="200">
                    <b>Passed rules:</b>
                </td>
                <td>
                    <xsl:value-of select="@passedRules"/>
                </td>
            </tr>
            <tr>
                <td width="200">
                    <b>Passed Checks:</b>
                </td>
                <td>
                    <xsl:value-of select="@passedChecks"/>
                </td>
            </tr>
            <tr>
                <td width="200">
                    <b>Failed rules:</b>
                </td>
                <td>
                    <xsl:value-of select="@failedRules"/>
                </td>
            </tr>
            <tr>
                <td width="200">
                    <b>Failed Checks:</b>
                </td>
                <td>
                    <xsl:value-of select="@failedChecks"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <!-- Detailed Information -->
    <xsl:template match="/report/validationInfo/result/details/rules/rule">

        <xsl:param name="id" select="@id"/>

        <tr style="BACKGROUND: #dcdaf6">
            <td width="500">
                <b>
                    Rule ID:
                    <xsl:value-of select="$id"/>
                </b>
            </td>
            <td/>
        </tr>
        <tr style="BACKGROUND: #dcdaf6">
            <td width="800">
                <b>
                    <xsl:value-of
                            select="$profile/profilens:profile/profilens:rules/profilens:rule[@id = $id]/profilens:description"/>
                </b>
                <br/>
                <xsl:value-of select="check[@status = 'failed']/error/message"/>
            </td>
            <td>
                <b>
                    <xsl:if test="@status = 'passed'">
                        <font color="green">
                            <b>Passed</b>
                        </font>
                    </xsl:if>
                    <xsl:if test="@status = 'failed'">
                        <font color="red">
                            <b>Failed</b>
                        </font>
                    </xsl:if>
                </b>
            </td>
        </tr>
        <tr style="BACKGROUND: #dcdaf6">
            <xsl:variable name="failedChecksCount" select="count(check[@status = 'failed'])"/>
            <xsl:if test="$failedChecksCount > 0">
                <td width="800">
                    <xsl:value-of select="@failedChecks"/> occurrences
                </td>
            </xsl:if>
            <td>
                <xsl:if test="@status = 'failed'">
                    <a id="lable{$id}" href="#" style="display: none;"
                       class="hide-tr"
                       data-target="hide{$id}"
                       data-translation-toggle="Hide">Show
                    </a>
                </xsl:if>
            </td>
        </tr>
        <xsl:for-each select="check[@status = 'failed']">
            <tr class="hideable hide{$id}">
                <td width="800" style="word-break: break-all">
                    <xsl:value-of select="location/context"/>
                </td>
            </tr>
        </xsl:for-each>

        <tr>
            <td>
                <br/>
            </td>
            <td>
                <br/>
            </td>
        </tr>

    </xsl:template>

</xsl:stylesheet>