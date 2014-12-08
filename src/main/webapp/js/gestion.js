function generaNuevoAfectado(asegurado, perjudicado,idContador) {
	// encabezado
	var h3 = "Afectado";
	if ( asegurado ){
		h3 = "Asegurado";
		if ( perjudicado ){
			h3 += " / Perjudicado";
		}
	} else {
		h3 = "Perjudicado";
	}
	h3 = "<h3>" + h3 + "</h3>";
	// cuerpo
	var div = "<div id='" + idContador + "'>" + "</div>";	
	return h3 + div;
}

function formatearResultados(contenedorResultados) {
	$("#" + contenedorResultados + " tr:odd").addClass("odd");
	$("#" + contenedorResultados + " tr:not(.odd)").hide();
	$("#" + contenedorResultados + " tr:first-child").show();    
	$("#" + contenedorResultados +" tr.odd").click(function(){
        $(this).next("tr").toggle();		
        $(this).find(".arrow").toggleClass("up");
    }); 
	return;
}

