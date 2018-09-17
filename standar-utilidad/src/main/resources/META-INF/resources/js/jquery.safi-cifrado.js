$.fn.filtroC=function(user_options){
    var mapping_default = {
            thClass:'fC',
            inputClass:'ui-column-filter'
	};
	var map = jQuery.extend(mapping_default, user_options);
		this.find("."+map.inputClass).addClass("uiColumnInputFc");
		var this_ = this;
    this.each(function(index, object) {
       var o = $(object);
			 if(o.find("."+map.inputClass).length>0){
					o.click(function(){
						this_.find("."+map.inputClass).each(
							function(i,o1){
								o1_=$(o1);
								if(o1_.val().trim().length==0){
									o1_.parents("."+map.thClass+":first").removeClass("fil");
								}else{
									o1_.parents("."+map.thClass+":first").addClass("fil");
								}
							}
						)
						$(o).addClass("fil");
						$(o).find("."+map.inputClass).focus();
						
					})
			 }


    });

}


$(document).ready(function(){
    try{

        $(document).on("pfAjaxComplete", function(event, xhr, options) {
            $(".fC").filtroC();
        });
    }catch(e){
        console.log(e);
    }

});
