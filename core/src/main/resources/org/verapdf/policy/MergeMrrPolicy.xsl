<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:vera="http://www.verapdf.org/MachineReadableReport">
  <xsl:output indent="yes" method="xml"/>

  <xsl:template match="@*|node()">
      <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
  </xsl:template>

  <xsl:template match="report">
      <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
          <xsl:for-each select="document('policyResult.xml')/*">
              <xsl:copy>
                  <xsl:apply-templates select="@*|node()"/>
              </xsl:copy>
          </xsl:for-each>
      </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
