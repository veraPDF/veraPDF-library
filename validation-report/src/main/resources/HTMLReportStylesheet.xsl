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
            <img border="0" src="veraPDF-logo-200.png"/>
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
            <tr>
                <td width="200">
                    <b>Validation Profile checksum:</b>
                </td>
                <td>
                    <xsl:value-of select="/report/validationInfo/profile/hash"/>
                </td>
            </tr>
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
                    <xsl:value-of
                            select="$profile/profilens:profile/profilens:rules/profilens:rule[@id = $id]/profilens:description"/>
                </b>
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
        <xsl:for-each select="check[@status = 'failed']">
            <tr>
                <td width="500">
                    <xsl:value-of select="error/message"/>
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