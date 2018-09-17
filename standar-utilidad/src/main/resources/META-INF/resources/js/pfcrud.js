var safiClientX = undefined;
var safiClientY = undefined;

var resetToggled = function () {
    jQuery('th.ui-helper-hidden[role="columnheader"]').each(function () {
        jQuery(this).removeClass('ui-helper-hidden');
    });
    jQuery('li.ui-columntoggler-item  div.ui-chkbox-box').each(function () {
        var $this = jQuery(this);
        if (!$this.hasClass('ui-state-active')) {
            $this.click();
            $this.click();
        }
    });
};

var documentsShow = function (panelid, buttonid) {
    try {
        PF(panelid).show();
        jQuery('#' + buttonid).addClass('BrownButton');
        jQuery('#' + buttonid).removeClass('RaisedButton');
        jQuery('#' + buttonid).attr('title', 'Ocultar Documentos');
        jQuery('#' + buttonid).find('span').first().removeClass('fa-folder');
        jQuery('#' + buttonid).find('span').first().addClass('fa-folder-open');
    } catch (e) {
        console.log(e);
    }
    PrimeFaces.scrollTo('message');
};

var documentsClose = function (panelid, buttonid) {
    try {
        PF(panelid).close();
        jQuery('#' + buttonid).addClass('OrangeButton');
        jQuery('#' + buttonid).addClass('RaisedButton');
        jQuery('#' + buttonid).removeClass('BrownButton');
        jQuery('#' + buttonid).attr('title', 'Mostrar Documentos');
        jQuery('#' + buttonid).find('span').first().removeClass('fa-folder-open');
        jQuery('#' + buttonid).find('span').first().addClass('fa-folder');
    } catch (e) {
        console.log(e);
    }
    PrimeFaces.scrollTo('message');
};

var documentsButtonClick = function (panelid, buttonid) {
    if (PF(panelid).visibleStateHolder.val() === 'true') {
        documentsClose(panelid, buttonid);
    } else {
        documentsShow(panelid, buttonid);
    }
};

var rememberMousePosition = function (x, y) {
    safiClientX = x;
    safiClientY = y;
};
var doclick = function (btnId) {
    jQuery('#' + btnId).click();
};

document.onmousemove = function (e) {
    rememberMousePosition(e.clientX, e.clientY);
};

function checkUrlStatus(url) {
    var status = 0;
    var script = document.body.appendChild(document.createElement("script"));
    script.onload = function () {

    };
    script.onerror = function () {
        alert(url + " is offline");
    };
    script.src = url;
}
function handleComplete(xhr, status, args) {
    console.log('complete', xhr, status, args);
}
function handleSubmit(xhr, status, args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    console.log('args:', args);
    try {
        PF('statusDialog').hide();
    } catch (e) {
        console.log(e);
    }

    if (args.validationFailed) {
        if (jqDialog !== undefined && jqDialog == null) {
            jqDialog.effect('shake', {
                times: 5
            }, 500);
        }
    } else {
        var pfDialog = PF(dialog);
        if (pfDialog !== undefined && pfDialog !== null) {
            pfDialog.hide();
        }
    }
}



function handleSubmitWithFilter(xhr, status, args, dialog, list) {
    var jqDialog = jQuery('#' + PF(dialog).id);
    if (args.validationFailed) {
        jqDialog.effect('shake', {
            times: 5
        }, 100);
    } else {
        list.clearFilters();
        PF(dialog).hide();
    }
}

function handleSubmitWithSubList(xhr, status, args, dialog, list, sublist) {
    var jqDialog = jQuery('#' + PF(dialog).id);
    if (args.validationFailed) {
        jqDialog.effect('shake', {
            times: 5
        }, 100);
    } else {
        list.clearFilters();
        sublist.clearFilters();
        PF(dialog).hide();
    }
}

function handleShow(args, dialog) {
    if (!args.validationFailed) {
        dialog.show();
    }
}

function handleShowWithFilter(args, dialog, list) {
    var jqDialog = jQuery('#' + PF(dialog).id);
    if (!args.validationFailed) {
        list.clearFilters();
        PF(dialog).show();
    }
}

function handleSubmitRequest(xhr, status, args, dialog) {
    var valid = true;
    if (args.validationFailed) {
        PF(dialog).jq.effect("shake", {times: 5}, 100);
    } else {
        if ((args.fechasValidas === undefined || args.fechasValidas === null) &&
                (args.codigoValido === undefined || args.codigoValido === null)) {
            PF(dialog).hide();
        }
        if ((args.fechasValidas !== undefined && args.fechasValidas !== null)
                && !args.fechasValidas) {
            valid = false;
        }
        if ((args.codigoValido !== undefined && args.codigoValido !== null)
                && !args.codigoValido) {
            valid = false;
        }
        if (!valid) {
            PF(dialog).jq.effect("shake", {times: 5}, 100);
        } else {
            PF(dialog).hide();
        }
    }
}

function handleValidacionDocumento(xhr, status, args, dialog) {
    if (args.validacionDocumentoFailed === undefined || args.validacionDocumentoFailed === null) {
        PF(dialog).show();
    } else if (args.validacionDocumentoFailed) {
        PF(dialog).hide();

    }

}

function disabledButton(idButton) {
    var idButton = PrimeFaces.escapeClientId(idButton);
    $(idButton).addClass("ui-state-disabled");
    $(idButton).attr("disabled", true);
}