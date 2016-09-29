<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:vera="http://www.verapdf.org/MachineReadableReport"
                xmlns:pp="http://www.verapdf.org/PolicyProfile">

    <xsl:output method="text"/>

    <xsl:param name="policyProfilePath"/>
    <xsl:variable name="policyProfile" select="document($policyProfilePath)"/>

    <xsl:template match="/">
        <xsl:call-template name="sum">
            <xsl:with-param name="pList" select="vera:report/vera:validationReport/vera:details/vera:rule"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="sum">
        <xsl:param name="pList"/>
        <xsl:param name="pAccum" select="0"/>

        <xsl:choose>
            <xsl:when test="$pList">
                <xsl:variable name="vHead" select="$pList[1]"/>
                <xsl:param name="id" select="concat($vHead/@clause,'t',$vHead/@testNumber)"/>

                <xsl:variable name="weight">
                    <xsl:choose>
                        <xsl:when test="$policyProfile/pp:policyProfile/pp:rules/pp:rule[@id=$id]">
                            <xsl:value-of
                                    select="$policyProfile/pp:policyProfile/pp:rules/pp:rule[@id=$id]/@weight"/>
                        </xsl:when>
                        <xsl:when test="$policyProfile/pp:policyProfile/pp:rules/@defaultWeight">
                            <xsl:value-of
                                    select="$policyProfile/pp:policyProfile/pp:rules/@defaultWeight"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="1"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>

                <xsl:variable name="count">
                    <xsl:choose>
                        <xsl:when test="$policyProfile/pp:policyProfile/pp:rules/pp:rule[@id=$id]">
                            <xsl:value-of
                                    select="$policyProfile/pp:policyProfile/pp:rules/pp:rule[@id=$id]/@count"/>
                        </xsl:when>
                        <xsl:when test="$policyProfile/pp:policyProfile/pp:rules/@defaultCount">
                            <xsl:value-of
                                    select="$policyProfile/pp:policyProfile/pp:rules/@defaultCount"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'all'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:variable name="countValue">
                    <xsl:choose>
                        <xsl:when test="$count='ignore'">
                            <xsl:value-of select="0"/>
                        </xsl:when>
                        <xsl:when test="$count='one' and $vHead/@failedChecks&gt;0">
                            <xsl:value-of select="1"/>
                        </xsl:when>
                        <xsl:when test="$count='all'">
                            <xsl:value-of select="$vHead/@failedChecks"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$vHead/@failedChecks"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>

                <xsl:call-template name="sum">
                    <xsl:with-param name="pList" select="$pList[position() > 1]"/>
                    <xsl:with-param name="pAccum"
                                    select="$pAccum + $weight * $countValue"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="result">
                    <xsl:with-param name="pAccum" select="$pAccum"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="result">
        <xsl:param name="pAccum"/>
        <xsl:variable name="message">
            <xsl:choose>
                <xsl:when
                        test="$policyProfile/pp:policyProfile/pp:results/pp:result[(not(@rangeStart) or @rangeStart&lt;=$pAccum) and (not(@rangeEnd) or @rangeEnd&gt;=$pAccum)]">
                    <xsl:value-of
                            select="$policyProfile/pp:policyProfile/pp:results/pp:result[(not(@rangeStart) or @rangeStart&lt;=$pAccum) and (not(@rangeEnd) or @rangeEnd&gt;=$pAccum)]/@value"/>
                </xsl:when>
                <xsl:when
                        test="$policyProfile/pp:policyProfile/pp:results/@default">
                    <xsl:value-of
                            select="$policyProfile/pp:policyProfile/pp:results/@default"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of
                            select="concat('There is no message for value ', $pAccum, ' and default message is missing')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:value-of select="$message"/>
    </xsl:template>

</xsl:stylesheet>