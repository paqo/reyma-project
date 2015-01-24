function generaNuevoAfectado(asegurado, perjudicado,idContador) {
	// encabezado
	var h3 = "AFECTADO";
	if ( asegurado ){
		h3 = "ASEGURADO";
		if ( perjudicado ){
			h3 += " Y PERJUDICADO";
		}
	} else {
		h3 = "PERJUDICADO";
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
			  filaRaw += "<div style='float: left; width: 30%; color:#000000;'>Fecha de entrada: " + data[index].fechaOcurrencia + "</div>";
			  filaRaw += "<div style='float: left; width: 25%; color:#000000; text-align: center;'>(" + data[index].tipo + ")</div>";
			  filaRaw += "<div style='float: left; width: 20%;'>";							  
			  filaRaw += "<div style='float: left; width: 20%;'><a href='/reymasur/siniestroes/" + data[index].id + "'><img style='float:left;' title='ver siniestro' src='/reymasur/resources/images/show.png' class='image'/></a></div>";							  
			  filaRaw += "<div style='float: left; width: 20%;'><a href='/reymasur/siniestroes/" + data[index].id + "?form'><img style='float:left;' title='ir al siniestro' src='/reymasur/resources/images/update.png' class='image'/></a></div>";
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

function modificarH3(){
	$( "[id^='chkAsegurado-'], [id^='chkPerjudicado-']" ).on( "click", function() {				
		var id = $(this).attr("id").split("-")[1];
		var h3 = $(this).closest("div.cont-afectados").prev();
		var txtAsegurado = !$( "#chkAsegurado-" + id ).is(":checked")? "ASEGURADO" : "";
		var txtPerjudicado = !$( "#chkPerjudicado-" + id ).is(":checked")? "PERJUDICADO" : "";
		var txtY = txtAsegurado != "" && txtPerjudicado != ""? " Y " : "";
		var res = txtAsegurado + txtY + txtPerjudicado;
		h3.contents().filter(function() {
		      return this.nodeType === 3;
		}).get(0).data = res == ""? 
				"ASEGURADO Y PERJUDICADO" : res;
	});
}

function imprimirDatosSiniestro(elem){
	console.log("=> " + elem.id);
	var contenido = $(elem).closest("#contenido-sin");
	abrirPopUpImprmir($(contenido).html());	
}

function abrirPopUpImprmir(data) {
    var mywindow = window.open('', 'Encargo', 'height=400,width=600');
    var rutaCss = $("head").find("link[media='screen']").attr("href");
    mywindow.document.write('<html><head><title>Encargo</title>');    
    mywindow.document.write('<link rel="stylesheet" href="' + rutaCss + '" type="text/css" />');
    mywindow.document.write('</head><body>');
    mywindow.document.write(data);
    mywindow.document.write('</body></html>');

    mywindow.document.close(); // necessary for IE >= 10
    mywindow.focus(); // necessary for IE >= 10

    $(mywindow.document).find(".quicklinks").empty();
    
    mywindow.print();
    mywindow.close();

    return true;
}
