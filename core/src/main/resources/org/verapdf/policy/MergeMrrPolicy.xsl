<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:vera="http://www.verapdf.org/MachineReadableReport"
                 xmlns:svrl="http://purl.oclc.org/dsdl/svrl" exclude-result-prefixes="svrl">
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>

  <xsl:param name="policyResultPath" select="'policyResult.xml'" />

  <xsl:template match="@*|node()" name="identity">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="report/jobs">
    <xsl:copy>
      <xsl:variable name="jobCount" select="count(/report/jobs/job)" />
      <xsl:for-each select="job">
        <xsl:variable name="jobPos">
          <xsl:choose>
              <xsl:when test="$jobCount > 1">
                  <xsl:value-of select="concat('job[', position(), ']')" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>job</xsl:text>
              </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="failedChecks" select="count(document($policyResultPath)//svrl:failed-assert[contains(@location, $jobPos)])"/>
        <xsl:variable name="passedChecks" select="count(document($policyResultPath)//svrl:successful-report[contains(@location, $jobPos)])"/>
        <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
          <policyReport>
            <xsl:attribute name="passedChecks">
              <xsl:value-of select="$passedChecks" />
            </xsl:attribute>
            <xsl:attribute name="failedChecks">
              <xsl:value-of select="$failedChecks" />
            </xsl:attribute>
            <xsl:element name="passedChecks">
              <xsl:for-each select="document($policyResultPath)//svrl:successful-report[contains(@location, $jobPos)]">
                <xsl:element name="check">
                  <xsl:attribute name="status">passed</xsl:attribute>
                  <xsl:attribute name="test">
                    <xsl:value-of select="@test" />
                  </xsl:attribute>
                  <xsl:attribute name="location">
                    <xsl:value-of select="@location" />
                  </xsl:attribute>
                  <xsl:element name="message">
                    <xsl:value-of select="./svrl:text" />
                  </xsl:element>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
            <xsl:element name="failedChecks">
              <xsl:for-each select="document($policyResultPath)//svrl:failed-assert[contains(@location, $jobPos)]">
                <xsl:element name="check">
                  <xsl:attribute name="status">failed</xsl:attribute>
                  <xsl:attribute name="test">
                    <xsl:value-of select="@test" />
                  </xsl:attribute>
                  <xsl:attribute name="location">
                    <xsl:value-of select="@location" />
                  </xsl:attribute>
                  <xsl:element name="message">
                    <xsl:value-of select="./svrl:text" />
                  </xsl:element>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
          </policyReport>
        </xsl:copy>
      </xsl:for-each>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="report">
    <xsl:copy>
      <xsl:copy-of select="buildInformation"/>
      <xsl:apply-templates select="jobs"/>
      <xsl:copy-of select="batchSummary"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
