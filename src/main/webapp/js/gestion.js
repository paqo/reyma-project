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
				if (active.length) {
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
			for (column = 1; column < footer.children().size(); column++) {
				total = 0;
				dataRows.each(function () {
					var row = $(this);
					total += parseFloat(row.children().eq(column).text());
				});
				footer.children().eq(column).text(total);
			};
		};
	element.find('td').on('change', function (evt) {
		var cell = $(this),
			column = cell.index(),
			total = 0;
		if (column === 0) {
			return;
		}
		element.find('tbody tr').each(function () {
			var row = $(this);
			total += parseFloat(row.children().eq(column).text());
		});
		if (column === 1 && total > 5000) {
			alert("temporal: restricción de < 5000");
			return false; // changes can be rejected
		} else {
			$('.alert').hide();
			footer.children().eq(column).text(total);
		}
	}).on('validate', function (evt, value) {
		var cell = $(this),
			column = cell.index();
		if (column === 0) {
			return !!value && value.trim().length > 0;
		} else {
			return !isNaN(parseFloat(value)) && isFinite(value);
		}
	});
	initialTotal();
	return this;
};