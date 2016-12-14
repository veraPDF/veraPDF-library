<schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process"
    xmlns="http://purl.oclc.org/dsdl/schematron">

    <pattern>
        <rule context="fonts/font">
            <assert test="type = 'Type0'">Only Type0 fonts allowed.</assert>
        </rule>
    </pattern>
</schema>
