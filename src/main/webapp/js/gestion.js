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


//#################### mindmup editable table plugin

/*global $, window*/
$.fn.editableTableWidget = function (options) {
	'use strict';
	return $(this).each(function () {
		var buildDefaultOptions = function () {
				var opts = $.extend({}, $.fn.editableTableWidget.defaultOptions);
				opts.editor = opts.editor.clone();
				return opts;
			},
			activeOptions = $.extend(buildDefaultOptions(), options),
			ARROW_LEFT = 37, ARROW_UP = 38, ARROW_RIGHT = 39, ARROW_DOWN = 40, ENTER = 13, ESC = 27, TAB = 9,
			element = $(this),
			editor = activeOptions.editor.css('position', 'absolute').hide().appendTo(element.parent()),
			active,
			showEditor = function (select) {
				active = element.find('td:focus');
				if (active.length && !active.children().is('select')) {
					editor.val(active.text())
						.removeClass('error')
						.show()
						.offset(active.offset())
						.css(active.css(activeOptions.cloneProperties))
						.width(active.width())
						.height(active.height())
						.focus();
					if (select) {
						editor.select();
					}
				}
			},
			setActiveText = function () {
				var text = editor.val(),
					evt = $.Event('change'),
					originalContent;
				if (active.text() === text || editor.hasClass('error')) {
					return true;
				}
				originalContent = active.html();
				active.text(text).trigger(evt, text);
				if (evt.result === false) {
					active.html(originalContent);
				}
			},
			movement = function (element, keycode) {
				if (keycode === ARROW_RIGHT) {
					return element.next('td');
				} else if (keycode === ARROW_LEFT) {
					return element.prev('td');
				} else if (keycode === ARROW_UP) {
					return element.parent().prev().children().eq(element.index());
				} else if (keycode === ARROW_DOWN) {
					return element.parent().next().children().eq(element.index());
				}
				return [];
			};
		editor.blur(function () {
			setActiveText();
			editor.hide();
		}).keydown(function (e) {
			if (e.which === ENTER) {
				setActiveText();
				editor.hide();
				active.focus();
				e.preventDefault();
				e.stopPropagation();
			} else if (e.which === ESC) {
				editor.val(active.text());
				e.preventDefault();
				e.stopPropagation();
				editor.hide();
				active.focus();
			} else if (e.which === TAB) {
				active.focus();
			} else if (this.selectionEnd - this.selectionStart === this.value.length) {
				var possibleMove = movement(active, e.which);
				if (possibleMove.length > 0) {
					possibleMove.focus();
					e.preventDefault();
					e.stopPropagation();
				}
			}
		})
		.on('input paste', function () {
			var evt = $.Event('validate');
			active.trigger(evt, editor.val());
			if (evt.result === false) {
				editor.addClass('error');
			} else {
				editor.removeClass('error');
			}
		});
		element.on('click keypress dblclick', showEditor)
		.css('cursor', 'pointer')
		.keydown(function (e) {
			var prevent = true,
				possibleMove = movement($(e.target), e.which);
			if (possibleMove.length > 0) {
				possibleMove.focus();
			} else if (e.which === ENTER) {
				showEditor(false);
			} else if (e.which === 17 || e.which === 91 || e.which === 93) {
				showEditor(true);
				prevent = false;
			} else {
				prevent = false;
			}
			if (prevent) {
				e.stopPropagation();
				e.preventDefault();
			}
		});

		element.find('td').prop('tabindex', 1);
		
		$(".eliminarFactura" )
 		.button({
 			icons: {
 				primary: "ui-icon-closethick"
 		      },
 		    text: false
 		})
 		.click(function() {	 							 	
 			event.preventDefault();
 		});

		$(window).on('resize', function () {
			if (editor.is(':visible')) {
				editor.offset(active.offset())
				.width(active.width())
				.height(active.height());
			}
		});
	});

};

$.fn.editableTableWidget.defaultOptions = {
	cloneProperties: ['padding', 'padding-top', 'padding-bottom', 'padding-left', 'padding-right',
					  'text-align', 'font', 'font-size', 'font-family', 'font-weight',
					  'border', 'border-top', 'border-bottom', 'border-left', 'border-right'],
	editor: $('<input>')
};

$.fn.formularioFactura = function () {
'use strict';
	var element = $(this),		
		footer = element.find('tfoot tr'),
		dataRows = element.find('tbody tr'),
		initialTotal = function () {
			var column, total;			
			/*for (column = 1; column < footer.children().size() - 1; column++) {
				total = 0;
				if ( column == 1 ){					
					calcularTotal(element, footer, column);		
				} else {
					calcularTotalIVA(element, footer, column);		
				}				
			};*/
			for (column = 2; column < footer.children().size() - 1; column++) {
				total = 0;
				if ( column == 2 ){					
					calcularTotal(element, footer, column);		
				} else {
					calcularTotalIVA(element, footer, column);		
				}				
			};
		};
	element.find('td, select').on('change', function (evt) {
		var cell = $(this),
			column = cell.index();
		if (column === 0 || column === 1) { // oficio y concepto
			return;
		} else  if (column == 2) { // coste
			calcularTotal(element, footer);		
			// (recalcular IVA siempre)
			calcularTotalIVA(element, footer);
		} else if (column == 3) { //iva
			calcularTotalIVA(element, footer);		
		}
	}).on('validate', function (evt, value) {
		var cell = $(this),
			column = cell.index();
		if (column === 0 || column === 1) {
			return !!value && value.trim().length > 0;
		} else {
			return !isNaN(parseFloat(value)) && isFinite(value);
		}
	});
	initialTotal();
	return this;
};

function calcularTotal(element, footer) {
	var total = 0,
	COLUMNA_COSTE = 2;
	element.find('tbody tr').each(function () {
		var row = $(this);
		total += parseFloat(row.children().eq(COLUMNA_COSTE).text());
	});	
	footer.children().eq(COLUMNA_COSTE).text(total);	
}

function calcularTotalIVA(element, footer) {
	var total = 0,
	COLUMNA_IVA = 3;
	element.find('tbody tr').each(function () {
		var row = $(this);
		var coste = parseFloat( row.children().eq(COLUMNA_IVA-1).text() );
		//var iva = parseFloat( row.find("option:selected").text().replace('%','') );
		var iva = parseFloat( row.find("select[id^='cbIva-'] > option:selected").text().replace('%','') );		
		total += parseFloat( coste + (coste * iva)/100 );		
	});	
	// dos decimales
	total = Math.floor(total * 100) / 100
	footer.children().eq(COLUMNA_IVA).text(total);
}

function initFecFacDatePicker() {
	$( "#facFecha" ).datepicker({
		dateFormat: 'dd/mm/yy',
		constrainInput: false,
		showOn: "button",
	    buttonImage: "/reymasur/images/calendar.png",
	    buttonImageOnly: true,
	    buttonText: "Seleccionar fecha",
	    onSelect: function(datetext){
	        var d = new Date();
	        datetext = d.getMinutes() >=10? 
	        		datetext+" "+d.getHours()+":"+d.getMinutes() : 
	        		datetext+" "+d.getHours()+":0"+d.getMinutes();
	        $(this).val(datetext);
	    },
	});
}

function obtenerParametroLineaFactura(contenedor, idFactura) {
	var filas = contenedor.find("tbody tr");
	var res = [];
	// TODO: pasar a POO, para hacer news, con sus
	// setters, validaciones y un metodo que haga 
	// el JSON directamente
	var linea = {};	
	var auxIva = {"ivaId" : null};
	var auxOficio = {"oficioId" : null};
	filas.each(function( index ) {
		linea.linConcepto = $( this ).find("td").eq(0).text();
		linea.linImporte = parseFloat($( this ).find("td").eq(1).text());
		auxIva.ivaId = parseInt($( this ).find("select[id^='cbIva-']").val());  
		linea.linIvaId = auxIva;
		auxOficio.oficioId = parseInt($( this ).find("select[id^='cbOficio-']").val());
		linea.linOficioId = auxOficio;
		var idLinea = $( this ).find("input[id^='idLinea-']").val();
		linea.linId = idLinea == ""? null : parseInt(idLinea);
		linea.linFacId = isNaN(parseInt(idFactura))? null : parseInt(idFactura);
		res.push(linea);
		linea = {};
		auxIva = {"ivaId" : null};
		auxOficio = {"oficioId" : null};
	});
	console.log("json: " + JSON.stringify(res));
	return JSON.stringify(res);
}

function limpiarLineasFactura(oficios, iva) {	
	//TODO: el id del tipo de IVA debe tomarse de BD, en facturas.jsp también
	//TODO: igual para oficios

	// limpiar fecha y numero
	$("#facFecha, #facNumero").val('');
	// limpiar posibles lineas de anteriores 
	// facturas
	$("#tablaFactura").find("tbody").empty();
	
	var _oficios = cargarOpcionesCombo(oficios);
	var _iva = cargarOpcionesCombo(iva);
	
	//linea inicial vacia
	$( "<tr>" + 
			"<td>" + 
				"<select name='cbOficio-1' id='cbOficio-1'>" +
					_oficios +
				"</select>" + 
			"</td>" + 
			"<td></td>" + 
			"<td>0</td>" + 
			"<td>" + 
				"<select name='cbIva-1' id='cbIva-1'>" +
					_iva +
				"</select>" + 
			"</td>" + 
			"<td style='text-align: center;'><button class='eliminarLineaFactura'></button>" +
				"<input type='hidden' name='idLinea-1' id='idLinea-1' value='' />" + 
			"</td>" + 
	  "</tr>" )
	  			.appendTo( $("#tablaFactura").find("tbody") );
	// añadir funcionalidad a la tabla			        	
	$("#tablaFactura").editableTableWidget().formularioFactura();
}

function cargarOpcionesCombo(opciones) {
	var res = "";
	$.each(opciones, function(index, opcion) {
	     res += "<option value='" + opcion.valor + "'>" + opcion.label + "</option>";
    });
	return res;
}

function cargarLineaFactura(idLinea, oficio, concepto, coste, iva) {
	var cont = $("#tablaFactura").find("tr").size() - 1;
	var selected1 = iva == 1? " selected='selected' " : "";
	var selected2 = iva == 2? " selected='selected' " : "";
	$( "<tr>" + 
			"<td>" + 
				"<select name='cbOficio-1' id='cbOficio-1'>" +
					"<option value='1'>Fontaneria</option>" + 
					"<option value='2'>Pintura</option>" +
				"</select>" + 
			"</td>" +
			"<td>" + concepto + "</td>" + 
			"<td>" + coste + "</td>" + 
			"<td>" + 
				"<select name='cbIva-" + cont + "' id='cbIva-" + cont + "'>" +
					"<option " + selected1 + " value='1'>10%</option>" + 
					"<option " + selected2 + " value='2'>21%</option>" +
				"</select>" + 
			"</td>" + 
			"<td style='text-align: center;'><button class='eliminarLineaFactura'></button>" +
				"<input type='hidden' name='idLinea-" + cont + "' id='idLinea-" + cont + "' value='" + idLinea + "' />" + 
			"</td>" + 
	  "</tr>" )
		.appendTo( $("#tablaFactura").find("tbody") );
}

function initFormularioFactura(ffac, nfac) {
	$("#facFecha").val(ffac);
	$("#facNumero").val(nfac);	
	initFecFacDatePicker();
	initBotonEliminarLinea();
}

function initBotonEliminarLinea() {
	$( ".eliminarLineaFactura" )
	.button({
		icons: {
			primary: "ui-icon-closethick"
	      },
	    text: false
	})
	.click(function() {	 							 		
		event.preventDefault();		 
		var fila = $(this).closest("tr");
		var tam = $("#tablaFactura").find("tr").size();
		var pos = fila.index();
		if ( pos > 0 || 
			 /* tam > 3: cabecera, pie y la propia fila */
			 (pos == 0 && tam > 3 ) ){		 				
			fila.remove();
		}
	});
}

function obtenerDatosFacturaJSON( idDivFormulario ) {
	var divForm = $("#" + idDivFormulario); 
	// campos generales
	var idFactura = parseInt( divForm.find("#idFacturaAbierta").val() );
	var numFactura = divForm.find("#facNumero").val();
	var fechaFactura = divForm.find("#facFecha").val();
	// lineas de factura	
	var celdas, 
		lineasFactura = [];
	divForm.find("tbody > tr").each(function( index ) {
		celdas = $( this ).children();
		lineasFactura.push({			 
			oficio: parseInt( $(celdas[0].firstChild).val() ),
			concepto: celdas[1].textContent, 
			coste: parseFloat(celdas[2].textContent),
			iva: parseInt( $(celdas[3].firstChild).val() ), 			
			id: parseInt( $(celdas[4]).children("input[type='hidden']").eq(0).val() )
		});
	});	
	
	return JSON.stringify({ 'idFactura': parseInt(idFactura), 
							'numFactura': numFactura, 
							'fechaFactura': fechaFactura,
							'lineasFactura' : lineasFactura });
}


//------------ DATATABLES ------------------

function obtenerI18NParaDT() {
	var labelsListadoSiniestros = {
			  "sProcessing": "Procesando...",
			  "sLoadingRecords": "Cargando...",
			  "sEmptyTable" : "No hay resultados",
			  "sZeroRecords" : "No hay resultados",
			  "sInfo" : "Mostrando siniestros _START_ a _END_  (_TOTAL_ en total)",
			  "sInfoFiltered" : "(Filtrando de un total de _MAX_)",
			  "sInfoEmpty" : "",
			  "sSearch": "Buscar",
			  "sLengthMenu": "Mostrar _MENU_ resultados",
			  "oPaginate": {
					  "sNext": "Siguiente",
					  "sPrevious": "Anterior",
					  "sFirst": "Primera",
					  "sLast": "&Uacute;ltima"
			      },		  
			  };
	return labelsListadoSiniestros;
}

function crearOrdenacionFechasDT() {
		// funcion para detectar una fecha
		// (en realidad no es necesario, basta definir el sType)  
		/*jQuery.fn.dataTableExt.aTypes.push(
		    function ( sData )
		    {
		        var reg = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g;
		        
		        if ( reg.test(sData) )
		        {
		            return 'fecha';
		        }
		        return null;
		    }
		);*/

		jQuery.fn.dataTableExt.oSort['fecha-asc']  = function(x,y) {
			var fec1 = parseInt( x.split('/').reverse().join('') );
			var fec2 = parseInt( y.split('/').reverse().join('') );

		    return ( (fec1 < fec2) ? -1 : ((fec1 > fec2) ?  1 : 0) );
		};
		 
		jQuery.fn.dataTableExt.oSort['fecha-desc'] = function(x,y) {
		    var fec1 = parseInt( x.split('/').reverse().join('') );
			var fec2 = parseInt( y.split('/').reverse().join('') );

		    return ( (fec1 < fec2) ? 1 : ((fec1 > fec2) ?  -1 : 0) );
		};
}