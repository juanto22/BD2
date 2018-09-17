$(document).ready(function () {
    $('.estiloGrid table').css({"width": "auto"});
    $('.estiloGrid table th').css({"white-space": "nowrap", "width": "auto"});

    //$('.tablaRrhh table').css({"width":"auto"});
    //$('.tablaRrhh div table th').css({"white-space": "nowrap", "width":"auto"});

    $('.estiloBandeja table:eq(1) tbody tr td').css({"white-space": "pre-line"});
    $('.estiloBandeja table:eq(0) thead tr th').css({"white-space": "pre-wrap"});
    //$('.estiloBandeja table').css({"width":"auto"});

    getWidthFromStyleAttribute = function (jq) {
        try {
            if (typeof jq.attr != "undefined") {
                var a = /width:[0-9]+px/.exec(jq.attr("style"));
                if (a != null) {
                    if (a.length > 0) {
                        var b = /[0-9]+/.exec(a[0]);
                        return (b.length > 0 ? parseFloat(b[0]) : -1)
                    }
                }
            }
            return null;
        } catch (e) {
            return null;
        }
    }

    resizeTreeTables = function () {
        $('.estiloBandeja').each(
                function (i, o) {
                    var o = $(o);
                    $(o).find('table:eq(1) tbody tr td').css({"white-space": "no-wrap"});
                    $(o).find('table:eq(1) tbody tr td').css({"width": "auto"});
                    o.find("table:eq(1) tbody tr:first td").each(
                            function (index, td) {
                                o.find("table:eq(0) thead tr:first th:eq(" + index + ")").css({"width": getWidthFromStyleAttribute($(td)) + "px"});
                            }
                    );
                }
        );

    }

    //resizeTreeTables();
    $(document).on("pfAjaxComplete", function (event, xhr, options) {
        //setTimeout(function(){resizeTreeTables();},100);
        // for(i = 0; i=17; i++){
        //var i = 0;
        /*   if (document.getElementById('listavotado:ancho:0:filter') != null) {
         for (var i = 0; i < 18; i++) {
         document.getElementById('listavotado:ancho:' + i + ':filter').style.width = "40px";
         }
         }*/

    });

    /* if (document.getElementById('listavotado:ancho:0:filter') != null) {
     for (var i = 0; i < 18; i++) {
     document.getElementById('listavotado:ancho:' + i + ':filter').style.width = "40px";
     }
     }*/

//    var elements = document.getElementsByClassName('estilo');
//    if (elements.length > 0) {
//        for (var i = 0, l = elements.length; i < l; i++) {
//            elements[i].style.width = "40px";
//        }
//
//    }
});