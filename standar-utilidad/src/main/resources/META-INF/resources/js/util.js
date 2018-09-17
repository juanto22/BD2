function upDialog(bool, dialog){
	if(bool){
		PF(dialog).show();
	}
}
		
var indices = new Array();

function validatePorcentaje(row, val, tableId, buttonId){

	var idButton = PrimeFaces.escapeClientId(buttonId);
	var idTable = PrimeFaces.escapeClientId(tableId);
	
	if(val > 100){
		$(idTable).find("tbody").find("tr").eq(row).find("td").eq(7).find("div").addClass("ui-state-error");
		$(idButton).css("display","none");

		indices.push(row);
	}else{
		$(idTable).find("tbody").find("tr").eq(row).find("td").eq(7).find("div").removeClass("ui-state-error");

		for(var i = indices.length - 1; i >= 0; i--) {
		    if(indices[i] === row) {
			    var index = indices.indexOf(row);
		    	indices.splice(index, 1);
		    }
		}
			
		if(indices.length === 0){
			$(idButton).css("display","block");
		}
	}
}