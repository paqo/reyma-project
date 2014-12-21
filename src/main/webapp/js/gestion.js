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

function cargarTablaResultados(data, idTabla) {
	 var filaCabecera = $("#" + idTabla).find(".resultados-cabecera").clone();
	$("#" + idTabla).empty();
	$( filaCabecera ).appendTo( "#" + idTabla );
	var filaRaw;
	if ( data.length == 0 ){ // hay resultados?
		filaRaw = "<tr>"; 
		filaRaw += "<td colspan='5'>No se han encontrado resultados</td>";
		filaRaw += "<td><div class='arrow'></div></td>";
		filaRaw += "</tr>";		
		$( filaRaw ).appendTo( "#" + idTabla );
	} else {
		$.each(data, function( index, value ) {
			  filaRaw = "<tr>";
			  filaRaw += "<td>" + data[index].compania + "</td>";
			  filaRaw += "<td>" + data[index].fecha + "</td>";
			  filaRaw += "<td>" + data[index].numeroSiniestro + "</td>";
			  filaRaw += "<td>" + data[index].domicilio + "</td>";
			  filaRaw += "<td>" + data[index].asegurado + "</td>";
			  filaRaw += "<td><div class='arrow'></div></td>";							  
			  filaRaw += "</tr>"
			  $( filaRaw ).appendTo( "#" + idTabla );
			  filaRaw = "<tr>";
			  filaRaw += "<td colspan='6'>";
			  filaRaw += "<div style='float: left; width: 100%;' class='detalle'>";
			  filaRaw += "<div style='float: left; width: 100%;'>";
			  filaRaw += "<div style='float: left; width: 25%; color:#000000;'>" + data[index].estado + "</div>";
			  filaRaw += "<div style='float: left; width: 30%; color:#000000;'>Fecha de entrada: " + data[index].fechaEntrada + "</div>";
			  filaRaw += "<div style='float: left; width: 25%; color:#000000; text-align: center;'>(" + data[index].tipo + ")</div>";
			  filaRaw += "<div style='float: left; width: 20%;'>";							  
			  filaRaw += "<div style='float: left; width: 20%;'><a href='/gestion/siniestroes/" + data[index].id + "'><img style='float:left;' title='ver siniestro' src='/gestion/resources/images/show.png' class='image'/></a></div>";							  
			  filaRaw += "<div style='float: left; width: 20%;'><a href='/gestion/siniestroes/" + data[index].id + "?form'><img style='float:left;' title='ir al siniestro' src='/gestion/resources/images/update.png' class='image'/></a></div>";
			  filaRaw += "</div>";
			  filaRaw += "</div>";
			  filaRaw += "<div style='float: left; width: 100%; margin-top: 1em; color:#000000;'>Descripci&oacute;n: " + data[index].descripcion + "</div>";
			  filaRaw += "</div>";
			  filaRaw += "</td>";							  
			  filaRaw += "</tr>";
			  $( filaRaw ).appendTo( "#" + idTabla );
		});	
	}	
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

