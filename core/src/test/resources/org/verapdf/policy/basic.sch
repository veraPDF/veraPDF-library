<schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process"
    xmlns="http://purl.oclc.org/dsdl/schematron">
    <pattern>
        <rule context="item">
            <assert test="@size &lt; 1">Size should be zero.</assert>
        </rule>
    </pattern>
</schema>
