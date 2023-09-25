<?xml version="1.0"?>
<!--
    Transform an XML Validation Report into presentable HTML.
    Author: Maksim Bezrukov
    Version: 1.0
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output indent="yes" method="html"/>

    <!-- Parameter to select a full HTML document (true) or a fragment within div (false) -->
    <xsl:param name="isFullHTML" select="'true'"/>
    <!-- Parameter to show parser type -->
    <xsl:param name="parserType"/>
    <!-- Parameter to show release details -->
    <xsl:param name="appName"/>
    <!-- Prameter to set the base path to the Wiki instance -->
    <xsl:param name="wikiPath"
               select="'https://github.com/veraPDF/veraPDF-validation-profiles/wiki/'"/>
    <xsl:strip-space elements="*"/>

    <!-- HTML header and body wrapper -->
    <xsl:template match="/">
        <xsl:if test="$isFullHTML='true'">
            <html>
                <head>
                    <title>Validation Report</title>
                </head>
                <body>
                    <!-- Header image and overall title -->
                    <p>
                        <img alt="veraPDF Logo"
                             src="data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAZABkAAD/7AARRHVja3kAAQAEAAAAPAAA/+4ADkFkb2JlAGTAAAAAAf/bAIQABgQEBAUEBgUFBgkGBQYJCwgGBggLDAoKCwoKDBAMDAwMDAwQDA4PEA8ODBMTFBQTExwbGxscHx8fHx8fHx8fHwEHBwcNDA0YEBAYGhURFRofHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8f/8AAEQgA0AGQAwERAAIRAQMRAf/EAMEAAQACAwADAQAAAAAAAAAAAAAHCAQFBgIDCQEBAQABBQEAAAAAAAAAAAAAAAAEAQIDBQYHEAAABQEDBAoKDQoFBAMAAAAAAQIDBAURBhYhElIHMVFxoZLSEzRUdEFhgdEisnOzNgiRsTJyI5MUlLQVNVU3QmKCM1PTdVYXGMGio8PjwmNEpCRkJREBAAECAgQMBQUBAAMBAAAAAAECAxEEMVETBSFBobHB0RIyUnIVBnGBkSIz8GHhFDRCYiMWJP/aAAwDAQACEQMRAD8AtQpRJK0wGC/Vo7J2KUQD0YhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAGIYmmQBiGJpkAYhiaZAPexVo7x2JUQDOSolFaQDHnrNDCjLaAQJrEvPWmK9yEWWtlnkiM0JsszjUeXKQjXq5ieB1G5MhZvWZqrp7U9rohymJ7wdOc3u8MO2q1tx6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1mJ7wdOc3u8G2q1no+V8EcvWYnvB05ze7wbarWej5XwRy9Zie8HTnN7vBtqtZ6PlfBHL1smm3jrjtShtOTXFNuPtIWnJlSpZEZbG0Kxdqx0rLu6ctFEzFEaJ19ax+ErvdDLhucYTnBGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7oZcNzjAGErvdDLhucYAwld7ohcNzjAMGtwYFKYjOQ2uSU4+lBmSlHkNKj7JntAN5AWa2Eme0A8apzVW4ArfrG9I1eTT4yhDzHedp7d/BPm6IcuMDfgAAAAAAAAAAAAAAAAAAAAAAAAAAAMukfa8DrLPnEisaWK93KvLPMtwNk8xAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHPX05nC60nxFgNtS+ap3ACqc1VuAK36xvSNXk0+MoQ8x3nae3fwT5uiHLjA34AAAAAAAAAAAAAAAAAAAAAAAAAAADLpH2vA6yz5xIrGlivdyryzzLcDZPMQAAAAAAAAAAAAAAAAAAAAAAAAAAAABz19OZwutJ8RYDbUvmqdwAqnNVbgCt+sb0jV5NPjKEPMd52nt38E+bohy4wN+AAAAAAAAAAAAAAAAAAAAAAAAAAAAy6R9rwOss+cSKxpYr3cq8s8y3A2TzEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAc9fTmcLrSfEWA21L5qncAKpzVW4ArfrG9I1eTT4yhDzHedp7d/BPm6IcuMDfgAAAAAAAAAAAAAAAAAAAAAAAAAAAMukfa8DrLPnEisaWK93KvLPMtwNk8xAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHPX05nC60nxFgNtS+ap3ACqc1VuAK36xvSNXk0+MoQ8x3nae3fwT5uiHLjA34AAAAAAAAAAAAAAAAAAAAAAAAAAADLpH2vA6yz5xIrGlivdyryzzLcDZPMQAAAAAAAAAAAAAAAAAAAAAAAAAAAABz19OZwutJ8RYDbUvmqdwAqnNVbgCt+sb0jV5NPjKEPMd52nt38E+bohy4wN+AAAAAAAAAAAAAAAAAAAAAAAAAAAAy6R9rwOss+cSKxpYr3cq8s8y3A2TzEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAc9fTmcLrSfEWA21L5qncAKpzVW4ArfrG9I1eTT4yhDzHedp7d/BPm6IcuMDfgAAAAAAAAAAAAAAAAAAAAAAAAAAAMukfa8DrLPnEisaWK93KvLPMtwNk8xAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHPX05nC60nxFgNtS+ap3ACqc1VuAK36xvSNXk0+MoQ8x3nae3fwT5uiHLjA34AAAAAAAAZkWyA/M5O2QGD9IyPYAAAAAAAAzk22WlaAEZHsHaAABmRbOQB+ZydsgMH6SiPYO0AAZdI+14HWWfOJFY0sV7uVeWeZam8ClJoNSUkzSpMV80qI7DIybVlIyGyeYvns3ei8/Jp//AGZ+wX/lv7XvwXPLFF5/vmf87f44Bii8/wB8z/nb/HAMUXn++Z/zt/jgM6m6w7+014nYN46kysv/ALTq0nuoWpST7pAJZuF6115oEluNfFhNWp6lES50dCWpTadvMTmtu7ngn2wUwWhodcpNdpUerUiUiZT5ac9iQ2dpGXtkZHkMjykYKK2+ttV6vBvVQEQZ0mIhcF1S0R3nGiUZPWWmSFJtMFYQTii8/wB8z/nb/HBUxRef75n/ADt/jgGJ7z/fM/52/wAcBkRb731iOE5GvBUmXE5SUmY/xwHd3X9ZjWjRXWymTG65DRkUxNQklmXafbJK7e2rOAwWO1Ya8ro39IojKjptdSnOcpchRZyrPdKYWWR1JdxW2QKYJFBRH2v+TJi6orwvxnnI76G2Mx5pSm1ptktFkUkyMsgEKVYovP8AfM/52/xwXGKLz/fM/wCdv8cAxRef75n/ADt/jgGKLz/fM/52/wAcAxRef75n/O3+OA6TVpeK8T2sW7DT1WmutOVSIlxtcl5SVJN5JGSkmoyMjAXRvpzOF1pPiLBa21L5qncAKpzVW4A46jXKuvXEypdVp6JUhD5tJcUpZGSCSkyLwVEWyoxZVbidKZl8/es09mirCHF64riUehwqfUaNEKKwpxTEtKDUpOcos5tR5xq0VEI963EcMOk3HvG5eqqouTjOGMdKLBHdGAOi1f3fRXr2wIDzfKRM43paTtIjZbK1RGZZfCOxPdF9unGrBA3lmdjYqqjvaI+Mp2/pVq++5muG7xxM2NOpxvrGa8c8nUhzWxAu7TLyJpdEhoiIispOWaDUec654RF4Rq9yiz2RFvREThDqtzXLtyz27lXaxng+EPZcPVZVLzJTOkrODR7cj5la49Zs8kk+x+ceTatC3amrh4lN473oy/2x91fN8epMlF1bXLpCEExTGnnU/wDkSSJ5wz27V2kXcIhKptUxxOUv71zF2eGqYjVHA331dT8zM+StZmxm8mmyzcsF+EIW1q04y5+t6s7l1dtZPU1uO8rYkRSJlwj2/BLNPukYsqtUzxJ2X3rmLU8FUzGqeFDF+9WNWuuZy21HNpBnYUpJWKbtPIl1JbHviybgi3LU0/B1m7t7UZj7Z+2vVr+DixibZN2rG4V0Kvc2HPqVNbky3FvEt5SnCMyS6pJe5URZCIS7VumacZhyG9t437WYmmirCng1ajWdcK6FHubMn06mtxpba2SQ8lThmRKdSlXulGWwYXbdMU4xBuneN+7mIprqxp4dWpqdXupxM+O1VrxktEdwiXGpxGaFLSeUlOnskR6JZdsWW7GPDKVvPfnYmaLWnjq6utLcC71Cp7JMwafHjtl2G20lbunZafdEmKYjQ5i5mblc41VTPzQjr0YYYvbFJltDSThIUokJJNp8q5lOzsiLmO86729VM2Jxn/rohl3I1LSqnHbqNfcXDiOFnNQ27CfWk9hSzMjzCPast3BW3Yx4ZY94b+ptzNFqO1Vr4v5SlTNX1y6alJRqRHNSSs5R1BPLPtmpzOMSIt0xxOcu7yzFzTXPNzN0mnwEpzUxmkp2iQki9oXYQiTdqnjlr6hc+61RTZNpMV638o2kkrhJIlb4pNETxM9rO3qO7XVHzRvfHUcwTDky7C1JdQRqOnPKziV2mnDykfaVbukMFdjU32R9wTj2b2jxR0wiimNuN1qE24k0OIlMpWhRGSkqJ1JGRkewZCNGl0t2Ym3VMeGeZaa8Xo/U+qP+bUNk8yfORr9WjcL2gXPIB2UPU3rSmxGJkS7Ut6LJbS8w8nk7FtuJJSVFavYMjtAxex3UprZabNarqzjSnKealCz7iUqNR9wgMXHy4cyFJcizGHIspo7HWHkKbcSf5yFERkA9ICdfVTv5Kpt7HboyHDVTaylb0Vsz8FqWyjPM02/tW0mR9siBSWT64Xpbd7qDvngIQCCrIh0+oTnFNwor0txJZykMNrdUSdi0yQSjIgGWd2LzEVp0aeRbfyV/iANc624y4bTyFNOlstuEaVF+iqwwHiA9sWVJiSWZUV1ceVHWl1h9tRpWhaTtSpKiykZGAuvqH1r49uypuoKSm8VKzWqilOQnUqL4OQlPYJdhkoi2FEfYsBSXv9Yf8G7yeTY+ktApCjgLnsjsPSJDUdhBuPvrS202WypazJKUlumdgDtP6Ia2/wCVpn+lxwMT+iGtv+Vpn+lxwMT+iGtv+Vpn+lxwMXQavtT2s+n38u7Pm3blsQ4tRivSX18nmobQ6lSlHYszsIgMVsL6czhdaT4iwWttS+ap3ACqc1VuANTcvmc3rSvEQA8r+UP67ulUoBFa6po3GMlp8q18Ij2TTYLLlONMwm7uzGyv01cWPD8J4FWyO0hr3owAmPUDRCzanW3EnaZphx1HsWFY47Z3c0ScvTplyvuS/wB23HxnmjpS3MlMxIj8t881mO2p1xW0lBGo94hJmcHMUUTVVFMaZV6uVd96/d9ZU2cSjg8qqZPPLlJavg2bS0tj3pGIVFPbq4Xc5/Mxk8vFNPew7MdM/rjWIaababS00gm20ESUISRElKSKwiIi2CITnCzMzOMouvzrpbpst2m3fablSGTND81y02ULLIaUJSZZ5keydtm6I9y/hwQ6Ld+4ZuUxXdnCJ4uP+HCFri1g5+d9YNmWh8nZzfFt3xh21Tc+h5XDu8spG1fa3mK9KbpVXaREqbmSO62Z8i8ejYrKhe0Vp2jPbvY8EtFvLcs2Y7due1Rx64/hIkiOxJYcjvtpdYdSaHG1FalSVFYZGQzzDRU1TTOMcEwrPrDugq694nIjdp0+QXLQFnlPkzPKgz20Hk3LDEC5R2Zegbszv9i12p70cE/r90zam/w/geUf88oSrHdcpvz/AFVfLmdNXKNGq8RuJJIlRyfZecbMrSWTKycJJ9ozSL6qcWvy9+bVXajThMfXgeq8d6KLd2D8sqsgmUHaTTZeE44ovyUILKZhVXFOldlcpcv1dmiMehG8v1go6XjKHRluMEeRbzxIUZe9SlZF7IwTmf2b+j21OH3V8P7QxqFMiax9YkSqOwjjw6TESp+O4onCW4lxRtZSIsmcu2wy/JFKZ7dWOpfmKKshlZoirGqurgn9sOH9fumYSnKIv1g64VUaoO0iiMofmseDJlO2m22vQSkrM5RdnLYQj3L2E4Q6Ldu5NrTFy5OFM6IjjcArXDrBNWd9YoL80mGbPFGHbVN36JlfDyy6e52u+pKnsQrxNtux31k38uaTya0Go7EmtBeCabdmyywZKL848LXZ7cFHZmq1M4xxSmgSnJoK1sUZiBrEpkxhBITUlMOukWwbqHiQs+6WaId6nCqHZbmvzXlaqZ/4xj5YJlvF6P1Pqj/m1CY4185Gv1aNwvaBc/Ve5PcAfQvV76BXb/hcL6OgFrfgIL9bC6FNmXKavOlpKKpSpDTSnyIiU5HfVyZtqPZPNWpKk7WXbBWFSgVdRqtkuRdZV1n2zsWmqRU9xbpIUXdSowEreuF6W3e6g754FIQCCqePVA9Oa3/DC+kIBSVsQUai8N0bsXjiLiVymR6gytOafLNpUsi/MX7tB9tJkYCoevXUuq4FQZqFMWt+7VQWaGFOWqcjvWGrkVq/KI0lahR5TsMj2LTLolFICQNRN7nLs6zqRIUs0wqg4VOmpI7EmiSZIQaveO5igJWj9Yf8G7yeTY+ktAthRwFzZ3X9J6N1+J59AD6LAtAAAAc9fTmcLrSfEWA21L5qncAKpzVW4A4K5le+T37nUVwyJudHS+xb+1ZMyURe+Qdv6Ix9r78P2bH+t2sptI/5rmPlMR086SBka5V2/wDRPqW99TgpKxrlTfY8m98Iku5nZvcGvuU4VS9F3bmNrYpq48MJ+XA54zsIz2hYnLRavqJ9TXPpkJRfC8kTz/Y+Ee+EUXczrBsLdOFMPOt5Zja36quLHD6cDQa7K99X3ROChRk/VXCYKz9kjw3T7pESe6LL9WFOGtN3Dl+3f7U6KOH58Tz1K0duDctqXm2P1Jxb7iuzmpUbbZbmam3uhYpwpU39fmvMTTxURh0y2usyuvUW5dQlsLzJC0pjsr2M1TyiRnF2ySZmQuu1YUo26svF3MU0zo0/RWMlIIvdF7IgPQsDPRpF7IGDybfNtxLjbmY4gyUhZHYaVJO0jLcMFJpxjCVrLpVlVau1TaouzlJTCFu5uxn2WLs/SIxsaKsYiXm+dsbK9VRqlx+vOjol3SbqCUWvU59Cs/sk078GsuEaT7gxZinGnFtPb9/s3+xxVRyxw9bYam/w/geUf88oXWO6wb8/1VfLmdq4tDaFOLPNQgjUpR7BEWUzGVqYjGcFWr53olXlr8ipOqPkLTbhNHsNsJPwS3T90fbGvrr7U4vRsjlIy9qKI08f7y0QsTHQXRvtV7quyXaahha5aUJdN9KlWEgzMs3NUnSF9FyadCDnchbzMRFeP26nS/11vp+xhfFOfvBk/sVIH/z2X11fWOpH8qS7KkvSXlZzz7inXFbalqNR75jBMt3RTFMREaIenOTsWlaC7Bv7r3Nr14KgwxEiOlHUtJvS1JNLSEW+ErPMiIzs2CLKYvoomqULN561YpmapjHVxrSJIkpJJbBFYNg85lD+ur0ruv77/fbEW/3odTuH8F39cUpRvF6P1Pqj/m1CU5Z85Gv1aNwvaBc/Ve5PcAX3uFei7LNxrutO1eE263TIaXG1SGiUlRMIIyMjVkMgWt4u+N0UJNS65T0pLZUcpki31AK6esnrnu9X6S1dK7UpM9nl0v1Oc1abPwNpoabXsL8PwlKLJkLKCsK8Aq7nUfR3KtrYu1HQVpMSimOdpEVJve2giAlIvrhelt3uoO+eBSEAgqnj1QPTmt/wwvpCAUlbEFABw2u+hN1vVXeKKpJG4xFVMZPsk5E+HKzdzLAIUPI7StBc9jEhyM+1JbPNcjrS6gy7Cm1Eoj9kgF2NfMkpWo6tyi2H40V0v05DKv8AEFIUjBVlUub8hqkKdmcp8kkNSOTtszuScJebblstzbAFiv7yD/lP/wB7/gBTA/vIP+U//e/4AMD+8g/5T/8Ae/4AMEx6qNYePrqfX/yD6u/+Q7H+T8ry36qzws7Nb2bdoFGxvpzOF1pPiLAbal81TuAFU5qrcAV+vRVnaPf2HVGsqoZtumnbSSjz091NpCJenCuJdbuWzF3KV0T/ANTPNCxMd9qQw2+yolsupSttZbBpUVpGW6QluUqpmmZidMIh1/UM/wD82uNpyFnQ5B7trjVvsLIRcxTol0/tzMd63PmjmnoRzciinWr10yn2WtuPE4/b+ya+EX7KU2DDbpxqiG+z9/ZWKq/25Z4FpxsHnCvGuevfWV8HIjajOPS2yjkXY5U/DdPfJPcEK/VjU7ncWW2djtTpr4flxJsuO0hq51EQgrElCYPum2Rn7YlW+7Dkt4Tjfr8087bSYsWU3yUllD7VpHmOJJabS2DsURkL5hFprmmcYnBi/UFB+7YvxLfFFOzGpk/s3PFV9ZPqCg/dsX4lvih2Y1H9m54qvrJ9QUH7ti/Et8UOzGo/s3PFV9ZZjDDDDSWWG0tNI9y2giSkuzkIsgqxVVTVOMzjLm9ZqUquFWyVsfJzMt0lEZb4x3e7LYbpn/8ATR8WBqb/AA/geUf88oUsd1m35/qq+XM3F/JDke5daebOxaYbxJPazkmn/EXXJ+2UXd1MVZiiJ8UKtbA170YAZNOps+pTWoMBhUiW+ea20grTPsmfaIuyZisRM8EMd27TbpmqqcKYS5d7UIwTaHrwTlKcPKqJEsSku0bqiMz7hEJNOX1uYzPuOccLVPznqdlD1VXBi2GmktuqL8p9S3d5ajLeGWLNMcTVV74zNX/eHw4G8YoNAhkRsU+KwSdg0Mtos7pEQu7MQh1Zi7Vpqqn5y9FQvddmnOtsSqiwh91aW2o6Vktw1LMkpIkJtVsmE1xC+1kr1yJmmmcI4+Jtxcioe11eld2Pff77Yi3+9Dqdw/gu/rilKN4vR+p9Uf8ANqEpyz5yNfq0bhe0C55APHk2zymkvYIA5NvRL2CAeQAZkRWmAtd6r+qqbQoT9760wbFQqbRNU2M4mxxqKZko3FEeVKnjIrC0S7YKS4/1wvS273UHfPAQgEFU8eqB6c1v+GF9IQCkrYgoAOb1ly2omru80h07EIpczZ21MKSW+YD58pKxJFtEC4WRmhRFsmRkXdAXW11MKY1AVNhXumoMJCt1LzJGCilQKgAAAAC43qpfhSX8Rlf9IKSkK+nM4XWk+IsFG2pfNU7gBVOaq3AFb9Y3pGryafGUIeY7ztPbv4J83RCYNTNe+s7nNRXFEcilrOKouzyZFnNGf6J5vcGaxVjS0m/cvs781Ror4etur/0P67uhUoKUEt/kjdjF2eVa8NFm6ZWC+5TjTKJu3MbK/TVxY4T8JRvqBovKTKjW3EeCyhMWOo9JfhuWbiST7IwZenjb/wBx38KabccfDPQlyuVVmk0eZU3v1cRlbplt5pWkXdPIJNU4Ri5jL2Zu3KaI/wCpwVNkyX5Uh2TIUa331qddWeya1nnKP2TGtmXpdNMUxERohZjVlOKbcSju22qbY5BW6yo2/wDpE+1ONMPP97W+xma4/fH68LE1ufLkXHlyITzrD0Zxp03GVqQrMJwkqypMjssVlFL2PZZNy9mczEVRExOOn4K/4kvH96zfnDvGEPtTrdt/VteCn6QYkvH96zfnDvGDtTrP6trwU/SDEl4/vWb84d4wdqdZ/VteCn6Q8yr16FFampTzI9gyffMvGDtTrU/rWfDR9IeuTV7wPMKalTZjjC8i23XXVIV2jJR2GKTVK6ixaicaaacf2iE+6m/w/geUf88oTbHdcVvz/VV8uZs9Y3oLXOqOe0LrvdlH3X/po80KvjXvRABOOoi78dmiyK4tFsuY4phpZ/kstGRHm++XbbuEJeXp4MXH+4szM3Itx3aYx+c/wkmp1GLTadJqEpWbGitqddUWU81BWnZ2xnmcIxaC1am5XFNOmZwV6vHrbvfV5KzjSlUuHafJR4x5qiT2M9z3SlblhCFVeql3GV3LYtRwx26tc9TkpVQqEtRrlSnpCj2TdcWs/wDMZjHMy2dFumnuxEfJ77vkRV6mWF/5cfzqQp0wszP4qvLPMtoNk8zQ9rq9K7se+/32xFv96HU7h/Bd/XFKUbxej9T6o/5tQlOWfORr9WjcL2gXPIzsIz2gE10X1VL51ajQKqxWKc2zUI7UpptZP5yUvIJZEqxFlpErKCmLN/tAvz990z2JHEAxZcL1PLyKWn5beOGyi3wuQYddOztZymgMUpXD9XDV/dWS1PeQ5WqozYpuROzTbbWR2kpthJEgj2jVnGW2BilQFFVPXC9LbvdQd88CsIBBVPHqgenNb/hhfSEApK2IKACDvWnv9Cplzjumw6S6rWzQbzST8JqI2slqWsuxyikkhO3l2gVhUcFW8uPQ3q7fKiUdpGecyawhaf8AtkslOGfaJtKjMBcf1h/wbvH5Jj6S0C2FHAXNhd5ll+8FKYeQTjL02M262rKSkLeSlST7RkdgC8n9FdU/8q074kgW4n9FdU/8q074kgMT+iuqf+Vad8SQGLpKBdyhXegfV9Egs0+Dnqc+TsJzEZ6/dKsLsnYA199OZwutJ8RYDbUvmqdwAqnNVbgCt+sb0jV5NPjKEPMd52nt38E+bohuNSle+rr3fIXFETFVbNk7TsInW7Vt+z4Se6KWKsKsNa/f2X7djtRponH5cawYmuIam7V24N34DsKF+qdkPSTt7BvLNRJ/RTYnuC2mmKYSc3mqr9UVVacIj6OG17175NQotGbUZO1B3lHiL9izYdh7qzT7Aw5irgwbn27l+1cm5OimOWf4QWIjsUxahrzIIpd3H12LMzlQSPYMrCJ1BbyrN0ScvVxOV9xZSeC7HwnoS3Nhx5sN+HJQS48hCmnUH2UrKwy9gxJmMXM265oqiqNMKz31uJV7rT3EPNqdpqlH8lnpIzQpJ7BLMvcrItkj7ggV25pl6BkN428xTjE4V8cfric1aWzaLGwbu6l0KxeaoIiwGlchnF8omGR8k0jsmatgz2k7Ji6iiap4EPOZ23l6e1VPDxRxys7SqZEpdNjU6InNjRW0tNEeU7ElZafbPsjYRGEYPPb12q5XNdWmZRTr6vE0aIV3mVEpZK+VyyLZTYRpaSe7nGr2BGzFXE6T27lZ+67PwjpdXqb/AA/geUf88oZbHda3fn+qr5czZ6xvQWudUc9oXXe7KPuv/TR5oVfGveiACxupqQ07cCChB+Ew4+24W0rlVK9pRCdYn7XB78pmM1Vjx4czfXwpEisXXqdMjGSZEphSGjUdhZ+yRGfbMrBfXTjEwhZG/Fq9TXOiJVbnQZtPlLiTmFxZLZ2LZdSaVEfd2e4NdMYPRrdymuO1TOMPRaWx2T2AXpJ1V6uapPq8WtVKOqPS4iifZJ0jSp9xOVGak8uYSspn2dgZ7NqZnGdDQb43pRRbm3RONdXB8P5T0Jji0P660KK9F112eAa80ldi0n2zsEa/ph1O4Z/9N39cUpQvF6P1Pqj/AJtQkuWfORr9WjcL2gXP1XuT3AH0L1e+gV2/4XC+joBa34AAAACqnrhelt3uoO+eBWEAgq7vVDrQVq6rk2qJpxVM5kX5LyRu8jm/CJczs7Mct9zZZYAlVfrkSM08y6ic7sWzTs3mAUwc9eH1s7/T45sUmDDoxq2ZBZ0l0ve8oSWy7qDAwQzUqlUanOeqFRkuTJ0hWe/JeUa3Fq7ajBVjALF+qfq7fdnyL8zmjTGYSuJR860s9xXgvukWilPgEe2atoFJSx6w/wCDd5PJsfSWgUhRwFzZ3X9J6N1+J59AD6LAtAAAAc9fTmcLrSfEWA21L5qncAKpzVW4ArfrG9I1eTT4yhDzHedp7d/BPm6Ic7ElPQ5bEtg7H4ziHmj/ADkKJRb5DBE4N5XRFVM0zongWxo1TYqlJiVFgyNqW0h1Nh22ZxWmXcPINlTOMYvNL9qbdc0TppnBmCrErVrTr31zfSattZqjQjKHH2rGsizLdcNQgXasanf7ny2yy9OOmrhn5/w5EY20e+DOlwJrE2G4bMqOsnGXU7JKIVicFly3TXTNNUYxKxFw9ZtIvLHbjyFoh1lJETkVR2JcPSZM/dEejslvibbuxV8XC7x3Tcy84x91vX1uzW2hxBocSS0KKxSVFaRl2yMZWqiZjhhp1XLugp7llUWEbltudyDez7As7FOpKjP38MO3V9ZbZlhhhpLTDaWmk5EtoIkpIu0RZBei1VTM4zwy5G/OsujXZjrYbWmZWFEZNQ0HbmHsZzxl7ki2tkxiuXYp+Labv3VczE4z9tGvqV2qNRm1Kc/Pmum9KkrNbrh9kz2toiLIRbQhTOM4u6tWqbdMU0xhTCwmpv8AD+B5R/zyhNsd1w+/P9VXy5mz1jegtc6o57Quu92Ufdf+mjzQq+Ne9EAEhapb/sXdmu02pLzKVOUSidPYZesszj/NWVhK2rCMZrNzs8E6Gj31u2b9MV0d+nlhP7TrTraXWlpcbWRKQtJkpJkewZGWyJriZiYnCXonUum1Bvk50RmU2Wwl5tLhFuZxGKTETpX271dE40zMfBiQrq3Zguk7DpURh0spONsoJRbh2WikURHEy3M5erjCquqY+L0XpvlQrtQ1SKjIInbDNmIgyN5w+wSU/wCJ5BSuuKdK/KZG5mKsKI4NfFDYUipxqpS4tRjKJTEppLqLDtszitMrS7JHkMXROMYsF61NuuaJ0xL9n0qmVFLSZ8VqUTKycZ5VCV5iy2FJtLIYTETpUt3q6MezMxjqem8Xo/U+qP8Am1CrG+cjX6tG4XtAufqvcnuAPoXq99Art/wuF9HQC1vwAAAAFVPXC9LbvdQd88CsIBBUAAAA7JF2TOwi7JmfYIBMmqb1c7x3pkMVO8bLtIu4Rks0OEaJUlJfktoPwm0npqL3pHsgpit7TabApkCPT6ewiLCitpajx2yzUIQgrCSRAo4H1h/wbvJ5Nj6S0BCjgLmzuv6T0br8Tz6AH0WBaAAAA56+nM4XWk+IsBtqXzVO4AVTmqtwBW/WN6Rq8mnxlCHmO87T27+CfN0Q5cYG/biBe+9VPioiQarJjRWreTZbcMkptM1HYW6YuiuY40W5krNdXaqopmXvx/ff78mfGGK7SrWx+m5fwU/RoVKUpRqUZqUozNSjymZnlMzFibEPwFQB+kZkZGWQyO0jLZIy7IKOnpOs2/FLaJmPU1usp9y3JSl+wtolLI1b4yRdqjja+9unL3JxmnCf24G6LXlffMzc2HnafIqt8ewXf2KkT/5/L/8Al9f4aeraz78VRo2X6mpllXukRkpYtLazkFn/AOYWzdqnjSrO6ctbnGKcZ/fhcsZmZmozM1KO1RnlMzPsmYxti/AVben3uvRToqYkCqyYsVBmaGWlmlJGo7TsLtmYuiuY0Si3MlZuVdqqiJl5y76XtmRnIsqryn4zyTQ6ytwzSpJ7JGQTXVPGpRkLFMxVTRTEw0otSwAAbqg3zvPQfBpdQcZZ6Oqxxr4tZKSXcF1NcxoRMxkbN7v0xM6+P6unb1434S3mqKG4rTUyoj/yrIhk29TXT7fy8z/19f4a6oa27+zW1NnUSjIVslGbQ2fcXYay9kUm9VLPa3LlqJx7OPxnFyb778h5Tz7i3nl+7dcUa1HuqVaYxNnTTFMYRGEN7dq/16buNmzTJdkVSs44rqScbtPZNJHlTb2c0yF9NyadCHmt3Wb841xw644JdIxrnvvMnxGOUjMNuvtIc5JnKaVLIjLw1L7BjJt6plr6txZemmZ+6eCeNPE2K3LhvxHTMm5Da2lmnIeatJpOzt5RMcUhdPqjatSSRFOq1hFZ+vZ/cgriH6o+rUys+XVXL/32f3IGKY6PS49JpEKlxjUqPAYajMqcMjWaGUEhJqMiIrbE5cgKMsAAAABwGsjUrdTWBUYc+tSJjL0JlTDRRXG0JNKlZ55xLbcy2gYuR/tH1a9Oqvx7P7kFcT+0fVr06q/Hs/uQMT+0fVr06q/Hs/uQMWVE9VLVUwslPfWMsi/IdlZqT+KQ2e+Bi7e7GqjV3dhROUahRmJBGRlKcSbz5GXZJ101rLuGCjrAABpr5XUp17btTbvVJbrcKelCXlsKJLhEhaXCzTUSy2UF2AEV/wBo+rXp1V+PZ/cgri98D1U9XUKdGmtTaobsV5t9slPsmk1NLJabbGSyWpAxTMCgAAADnr6czhdaT4iwG2pfNU7gBVOaq3AFb9Y3pGryafGUIeY7ztPbv4J83RDlxgb8AAAAAAAAAAAAAAAAAAAAAAAAAAABl0j7XgdZZ84kVjSxXu5V5Z5luBsnmIAAAAAAAAAAAAAAAAAAAAAAAAAAAAA56+nM4XWk+IsBtqXzVO4AVTmqtwBW/WN6Rq8mnxlCHmO87T27+CfN0Q5cYG/AAAAAAAAAAAAAAAAAAAAAAAAAAAAZdI+14HWWfOJFY0sV7uVeWeZbgbJ5iAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOevpzOF1pPiLAbal81TuAFU5qrcAVv1jekavJp8ZQh5jvO09u/gnzdEOXGBvwAAAAAAAAAAAAAAAAAAAAAAAAAAAGXSPteB1lnziRWNLFe7lXlnmW4GyeYgAAAAAAAAAAAAAAAAAAAAAAAAAAAADnr6czhdaT4iwG2pfNU7gBVOaq3AFb9Y3pGryafGUIeY7ztPbv4J83RDlxgb8AAAAAAAAAAAAAAAAAAAAAAAAAAABl0j7XgdZZ84kVjSxXu5V5Z5luBsnmIAAAAAAAAAAAAAAAAAAAAAAAAAAAAA56+nM4XWk+IsBtqXzVO4AVTmqtwBW/WN6Rq8mnxlCHmO87T27+CfN0Q5cYG/AAAAAAAAAAAAAAAAAAAAAAAAAAAAZdI+14HWWfOJFY0sV7uVeWeZbgbJ5iAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOevpzOF1pPiLAbal81TuAFU5qrcAVv1jekavJp8ZQh5jvO09u/gnzdEOXGBvwAAAAAAAAAAAAAAAAAAAAAAAAAAAGXSPteB1lnziRWNLFe7lXlnmW4GyeYgAAAAAAAAAAAAAAAAAAAAAAAAAAAADnr6czhdaT4iwG2pfNU7gDynoNbCiLaAQJrEuxWn69y8WIt5nkiI1pssziUeTKYjXqJmeB1G5M/Zs2Zprq7M9rohymGLwdBc3u+MOxq1Nx6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1GGLwdBc3u+Gxq1HrGV8ccvUYYvB0Fze74bGrUesZXxxy9Rhi8HQXN7vhsatR6xlfHHL1Mmm3crjVShuuQnEttvtLWrJkSlZGZ7O0Kxaqx0LLu9stNExFcaJ19Sx+LbvdMLgOcUTnBGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7phcBzigGLbvdMLgOcUAxbd7pZcBzigMGtzoFVYjNw3eVU2+lZkSVFkJKi7JFtgN5AQaGEke0AyFJJRWGAwX6THeO1SSAejD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIAw9E0CAMPRNAgDD0TQIB72KTHZO1KSAZyUkkrCAf/Z"/>
                    </p>
                    <style>
                        <xsl:text>body{font-family: sans-serif;}</xsl:text>
                    </style>
                    <h1 align="left">
                        <strong>
                            <b>Validation Report</b>
                        </strong>
                    </h1>
                    <xsl:apply-templates/>
                </body>
            </html>
        </xsl:if>
        <xsl:if test="$isFullHTML='false'">
            <xsl:apply-templates/>
        </xsl:if>
    </xsl:template>

    <!-- Validation Report header -->
    <xsl:template match="report">
        <div>
            <style>
                div {
                font-family: sans-serif;
                }
                .invalid {
                color: red;
                font-weight: bold;
                }
                .valid {
                color: green;
                font-weight: bold;
                }
                th {
                text-align: left;
                }
                .policy th, .policy td {
                padding: 5px;
                }
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

            <xsl:variable name="validClass">
                <xsl:choose>
                    <xsl:when
                            test="/report/jobs/job/validationReport/@isCompliant = 'true'">
                        <xsl:value-of select="'valid'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'invalid'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <!-- General information -->
            <table border="0" id="table1">
                <tr>
                    <td width="200">
                        <b>File:</b>
                    </td>
                    <td>
                        <xsl:value-of select="/report/jobs/job/item/name"/>
                    </td>
                </tr>
                <xsl:if test="/report/jobs/job/validationReport/@profileName">
                    <tr>
                        <td width="200">
                            <b>Validation Profile:</b>
                        </td>
                        <td>
                            <xsl:value-of
                                    select="/report/jobs/job/validationReport/@profileName"/>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="/report/jobs/job/validationReport/@isCompliant">
                    <tr>
                        <td width="200" class="{$validClass}">
                            Compliance:
                        </td>
                        <td class="{$validClass}">
                            <xsl:if test="/report/jobs/job/validationReport/@isCompliant = 'true'">
                                Passed
                            </xsl:if>
                            <xsl:if test="/report/jobs/job/validationReport/@isCompliant = 'false'">
                                Failed
                            </xsl:if>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="/report/jobs/job/taskException/exceptionMessage">
                    <tr>
                        <td width="200" class="invalid">
                            Error:
                        </td>
                        <td class="invalid">
                            <xsl:value-of
                                    select="/report/jobs/job/taskException/exceptionMessage"/>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="/report/jobs/job/policyReport">
                    <tr>
                        <xsl:choose>
                            <xsl:when
                                    test="/report/jobs/job/policyReport/@failedChecks > 0">
                                <td class="invalid">Policy compliance:</td>
                                <td class="invalid">Failed</td>
                            </xsl:when>
                            <xsl:otherwise>
                                <td class="valid">Policy compliance:</td>
                                <td class="valid">Passed</td>
                            </xsl:otherwise>
                        </xsl:choose>
                    </tr>
                </xsl:if>
            </table>

            <h2>Statistics</h2>
            <table border="0" id="table2">
                <tr>
                    <td width="250">
                        <b>Version:</b>
                    </td>
                    <td>
                        <xsl:value-of select="/report/buildInformation/releaseDetails[@id=$appName]/@version"/>
                    </td>
                </tr>
                <xsl:if test="$parserType">
                    <tr>
                        <td width="250">
                            <b>Parser:</b>
                        </td>
                        <td>
                            <xsl:value-of
                                    select="$parserType"/>
                        </td>
                    </tr>
                </xsl:if>
                <tr>
                    <td width="250">
                        <b>Build Date:</b>
                    </td>
                    <td>
                        <xsl:value-of select="/report/buildInformation/releaseDetails[@id=$appName]/@buildDate"/>
                    </td>
                </tr>
                <tr>
                    <td width="250">
                        <b>Processing time:</b>
                    </td>
                    <td>
                        <xsl:value-of select="/report/jobs/job/duration"/>
                    </td>
                </tr>
                <xsl:if test="/report/jobs/job/validationReport/details/@passedRules or /report/jobs/job/validationReport/details/@failedRules">
                    <tr>
                        <td width="250">
                            <b>Total rules in Profile:</b>
                        </td>
                        <td>
                            <xsl:value-of
                                    select="/report/jobs/job/validationReport/details/@passedRules + /report/jobs/job/validationReport/details/@failedRules"/>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="/report/jobs/job/validationReport/details/@passedChecks">
                    <tr>
                        <td width="250">
                            <b>Passed Checks:</b>
                        </td>
                        <td>
                            <xsl:value-of
                                    select="/report/jobs/job/validationReport/details/@passedChecks"/>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="/report/jobs/job/validationReport/details/@failedChecks">
                    <tr>
                        <td width="250">
                            <b>Failed Checks:</b>
                        </td>
                        <td>
                            <xsl:value-of
                                    select="/report/jobs/job/validationReport/details/@failedChecks"/>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="/report/jobs/job/policyReport">
                    <tr>
                        <td width="250">
                            <b>Failed Policy Checks:</b>
                        </td>
                        <td>
                            <xsl:value-of
                                    select="/report/jobs/job/policyReport/@failedChecks"/>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:apply-templates
                        select="/report/jobs/job/metadataRepairReport/@status"/>
                <xsl:apply-templates
                        select="/report/jobs/job/metadataRepairReport/@fixCount"/>
                <xsl:apply-templates
                        select="/report/jobs/job/logs/@logsCount"/>
            </table>
            <xsl:if test="/report/jobs/job/metadataRepairReport/fixes/fix">
                <h2>Metadata fixes information</h2>

                <table border="0" id="table4">
                    <tr style="BACKGROUND: #bcbad6">
                        <td width="800">
                            <b>Fixes</b>
                        </td>
                        <td width="50">
                            <a id="lable{fixesId}" href="#"
                               style="display: none;"
                               class="hide-tr"
                               data-target="hide{fixesId}"
                               data-translation-toggle="Hide">Show
                            </a>
                        </td>
                    </tr>

                    <xsl:apply-templates
                            select="/report/jobs/job/metadataRepairReport/fixes/fix"/>

                </table>
            </xsl:if>

            <xsl:if test="/report/jobs/job/metadataRepairReport/errors/error">
                <h2>Metadata fixer errors information</h2>

                <table border="0" id="table5">
                    <tr style="BACKGROUND: #bcbad6">
                        <td width="800">
                            <b>Fixes</b>
                        </td>
                        <td width="50">
                            <a id="lable{fixererrorsId}" href="#"
                               style="display: none;"
                               class="hide-tr"
                               data-target="hide{fixererrorsId}"
                               data-translation-toggle="Hide">Show
                            </a>
                        </td>
                    </tr>

                    <xsl:apply-templates
                            select="/report/jobs/job/metadataFixesReport/error"/>

                </table>
            </xsl:if>

            <xsl:if test="/report/jobs/job/validationReport/details/rule">
                <h2>Validation information</h2>

                <table border="0" id="table3">
                    <tr style="BACKGROUND: #bcbad6">
                        <td width="800">
                            <b>Rule</b>
                        </td>
                        <td width="50">
                            <b>Status</b>
                        </td>
                    </tr>
                    <xsl:apply-templates
                            select="/report/jobs/job/validationReport/details/rule"/>
                </table>
            </xsl:if>

            <xsl:if test="/report/jobs/job/policyReport">
                <xsl:apply-templates select="/report/jobs/job/policyReport"/>
            </xsl:if>

        </div>

        <xsl:if test="/report/jobs/job/logs">
            <h2>Logs information</h2>

            <table border="0" id="table5">
                <tr style="BACKGROUND: #bcbad6">
                    <td width="100">
                        <b>Type</b>
                    </td>
                    <td width="650">
                        <b>Log message</b>
                    </td>
                    <td width="100">
                        <b>Occurrences</b>
                    </td>
                </tr>

                <xsl:apply-templates
                        select="/report/jobs/job/logs/*"/>
            </table>
        </xsl:if>

        <xsl:if test="/report/jobs/job/featuresReport">
            <h2>Features information</h2>

            <table border="0" id="table4">
                <tr style="BACKGROUND: #bcbad6">
                    <td width="800">
                        <b>Feature</b>
                    </td>
                </tr>
                <xsl:apply-templates
                        select="/report/jobs/job/featuresReport/*"/>
            </table>
        </xsl:if>
    </xsl:template>


    <xsl:template match="/report/jobs/job/metadataRepairReport/@status">
        <tr>
            <td width="250">
                <b>Metadata Fixes Status:</b>
            </td>
            <td>
                <xsl:value-of
                        select="/report/jobs/job/metadataRepairReport/@status"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="/report/jobs/job/metadataRepairReport/@fixCount">
        <tr>
            <td width="250">
                <b>Completed Metadata Fixes:</b>
            </td>
            <td>
                <xsl:value-of
                        select="/report/jobs/job/metadataRepairReport/@fixCount"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="/report/jobs/job/logs/@logsCount">
        <tr>
            <td width="250">
                <b>Number of logs:</b>
            </td>
            <td>
                <xsl:value-of
                        select="/report/jobs/job/logs/@logsCount"/>
            </td>
        </tr>
    </xsl:template>

    <!-- Validation Information -->
    <xsl:template match="/report/jobs/job/validationReport/details/rule">

        <xsl:param name="idWithDots" select="concat(@clause, 't', @testNumber)"/>
        <xsl:param name="id" select="translate($idWithDots, '.', '_')"/>

        <xsl:variable name="wikiLinkEnd">
            <xsl:choose>
                <xsl:when test="starts-with(@specification, 'ISO 19005-1')">
                    <xsl:value-of select="'PDFA-Part-1-rules'"/>
                </xsl:when>
                <xsl:when test="starts-with(@specification, 'ISO 19005-2')">
                    <xsl:value-of select="'PDFA-Parts-2-and-3-rules'"/>
                </xsl:when>
                <xsl:when test="starts-with(@specification, 'ISO 19005-3')">
                    <xsl:value-of select="'PDFA-Parts-2-and-3-rules'"/>
                </xsl:when>
                <xsl:when test="starts-with(@specification, 'ISO 19005-4')">
                    <xsl:value-of select="'PDFA-Part-4-rules'"/>
                </xsl:when>
                <xsl:when test="starts-with(@specification, 'ISO 14289-1')">
                    <xsl:value-of select="'PDFUA-Part-1-rules'"/>
                </xsl:when>
                <xsl:when test="starts-with(@specification, 'ISO 14289-2')">
                    <xsl:value-of select="'PDFUA-Part-2-rules'"/>
                </xsl:when>                
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="tempWikiLink">
            <xsl:choose>
                <xsl:when
                        test="'/' = substring($wikiPath, string-length($wikiPath))">
                    <xsl:value-of select="concat($wikiPath, $wikiLinkEnd)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($wikiPath, '/', $wikiLinkEnd)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="wikiLink">
            <xsl:choose>
                <xsl:when test="not(starts-with($tempWikiLink, 'http'))">
                    <xsl:value-of select="concat($tempWikiLink, '.html')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$tempWikiLink"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="ruleLink"
                      select="concat($wikiLink, '#rule-', translate(@clause, '.', ''), '-', @testNumber)"/>
        <xsl:variable name="hasLink"
                      select="starts-with(@specification, 'ISO 19005-1') or starts-with(@specification, 'ISO 19005-2') or 
                      starts-with(@specification, 'ISO 19005-3') or starts-with(@specification, 'ISO 19005-4') or 
                      starts-with(@specification, 'ISO 14289-1') or starts-with(@specification, 'ISO 14289-2')"/>
        <xsl:variable name="ruleInformation">
            Specification:
            <xsl:value-of select="@specification"/>,
            Clause:
            <xsl:value-of select="@clause"/>,
            Test number:
            <xsl:value-of select="@testNumber"/>
        </xsl:variable>

        <tr style="BACKGROUND: #dcdaf6">
            <td width="800">
                <xsl:choose>
                    <xsl:when test="$hasLink">
                        <a href="{$ruleLink}">
                            <xsl:copy-of select="$ruleInformation" />
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <b>
                            <xsl:copy-of select="$ruleInformation" />
                        </b>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
            <td/>
        </tr>
        <tr style="BACKGROUND: #dcdaf6">
            <td width="800">
                <xsl:value-of
                        select="description"/>
            </td>
            <td width="50">
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
        <xsl:if test="@failedChecks > 0">
            <tr style="BACKGROUND: #dcdaf6">
                <td width="800">
                    <xsl:value-of select="@failedChecks"/> occurrences
                </td>
                <td width="50">
                    <xsl:if test="@status = 'failed'">
                        <a id="lable{$id}" href="#" style="display: none;"
                           class="hide-tr"
                           data-target="hide{$id}"
                           data-translation-toggle="Hide">Show
                        </a>
                    </xsl:if>
                </td>
            </tr>
            <tr style="BACKGROUND: #eceaf6" class="hideable hide{$id}">
                <td width="800" style="word-break: break-all">
                    <xsl:value-of select="object"/>
                </td>
                <td/>
            </tr>
            <tr style="BACKGROUND: #eceaf6" class="hideable hide{$id}">
                <td width="800" style="word-break: break-all">
                    <xsl:value-of select="test"/>
                </td>
                <td/>
            </tr>
            <xsl:for-each select="check[@status = 'failed']">
                <tr class="hideable hide{$id}">
                    <td width="800" style="word-break: break-all">
                        <xsl:value-of select="context"/>
                    </td>
                </tr>
                <tr class="hideable hide{$id}">
                    <td width="800" style="word-break: break-all">
                        <xsl:value-of select="errorMessage"/>
                    </td>
                </tr>
            </xsl:for-each>
        </xsl:if>

        <tr>
            <td>
                <br/>
            </td>
            <td>
                <br/>
            </td>
        </tr>

    </xsl:template>
    <!-- Features Information -->
    <xsl:template match="/report/jobs/job/featuresReport/*">

        <tr style="BACKGROUND: #dcdaf6">
            <td width="800">
                <a>
                    <xsl:choose>
                        <xsl:when test="local-name()='informationDict'">
                            Information dictionary
                        </xsl:when>
                        <xsl:when test="local-name()='documentResources'">
                            Document resources
                        </xsl:when>
                        <xsl:when test="local-name()='outlines'">
                            Outlines
                        </xsl:when>
                        <xsl:when test="local-name()='lowLevelInfo'">
                            Low level info
                        </xsl:when>
                        <xsl:when test="local-name()='metadata'">
                            Metadata
                        </xsl:when>
                        <xsl:when test="local-name()='signatures'">
                            Signatures
                        </xsl:when>
                        <xsl:when test="local-name()='embeddedFiles'">
                            Embedded files
                        </xsl:when>
                        <xsl:when test="local-name()='iccProfiles'">
                            ICC profiles
                        </xsl:when>
                        <xsl:when test="local-name()='outputIntents'">
                            Output intents
                        </xsl:when>
                        <xsl:when test="local-name()='annotations'">
                            Annotations
                        </xsl:when>
                        <xsl:when test="local-name()='pages'">
                            Pages
                        </xsl:when>
                        <xsl:when test="local-name()='errors'">
                            Errors
                        </xsl:when>
                    </xsl:choose>
                </a>
            </td>
        </tr>

    </xsl:template>

    <xsl:template match="/report/jobs/job/logs/*">
        <tr style="BACKGROUND: #dcdaf6">
            <td width="100" style="word-break: break-all">
            <b>
                <xsl:choose>
                    <xsl:when test="@level = 'SEVERE'">
                        <font color="red">
                            <b>SEVERE</b>
                        </font>
                    </xsl:when>
                    <xsl:when test="@level = 'WARNING'">
                        <font color="orange">
                            <b>WARNING</b>
                        </font>
                    </xsl:when>
                    <xsl:otherwise>
                        <font color="green">
                            <b><xsl:value-of select="@level"/></b>
                        </font>
                    </xsl:otherwise>
                </xsl:choose>
            </b>
            </td>
            <td width="650" style="word-break: break-all">
                <xsl:value-of select="."/>
            </td>
            <td width="100" style="word-break: break-all">
                <xsl:value-of select="@occurrences"/>
            </td>
        </tr>
    </xsl:template>

    <!-- Metadata fixes information -->
    <xsl:template match="/report/jobs/job/metadataRepairReport/fixes/fix">
        <tr class="hideable hide{fixesId}">
            <td width="800" style="word-break: break-all">
                <xsl:value-of select="."/>
            </td>
        </tr>
    </xsl:template>

    <!-- Metadata fixer errors information -->
    <xsl:template match="/report/jobs/job/metadataRepairReport/errors/error">
        <tr class="hideable hide{fixererrorsId}">
            <td width="800" style="word-break: break-all">
                <xsl:value-of select="."/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="/report/jobs/job/policyReport">
        <h2>Policy information</h2>
        <table class="policy">
            <tr style="background: #bcbad6">
                <th class="le">Message</th>
                <th>Status</th>
                <th>Location</th>
            </tr>
            <xsl:for-each select="*/check">
                <xsl:variable name="validClass">
                    <xsl:choose>
                        <xsl:when test="@status = 'passed'">
                            <xsl:value-of select="'valid'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'invalid'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <tr style="background: #dcdaf6">
                    <td>
                        <xsl:value-of select="message"/>
                    </td>
                    <td class="{$validClass}">
                        <xsl:value-of select="@status"/>
                    </td>
                    <td>
                        <xsl:value-of select="@location"/>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

</xsl:stylesheet>
