var currentEvent;

  PrimeFaces.widget.ContextMenu.prototype.show = function(e, boleano) {
     $(document.body).children('.ui-contextmenu:visible').hide();
	     if(e) {currentEvent = e;} 
	     var win = $(window),
	     left = e.pageX,
	     top = e.pageY,
	     width = this.jq.outerWidth(),
	     height = this.jq.outerHeight();
	     
	     if((left + width) > (win.width())+ win.scrollLeft()) {
	        left = left - width;
	     }
	     if((top + height ) > (win.height() + win.scrollTop())) {
	        top = top - height;
	     }
	
		if(boleano==true){
		     this.jq.css({
		        'left': left,
		        'top': top,
		        'z-index': ++PrimeFaces.zindex
		     }).show();
		}
     e.preventDefault(); 
  };
