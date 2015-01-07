// ==UserScript==
// @name       Importar siniestros directamente desde pagina de AXA
// @namespace  http://paqo.scripts.reyma/
// @version    101
// @description  Incluye un boton debajo de los datos del encargo para importar los datos relevantes directamente a Collabtive
// @require    http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js
// @require    http://tinymce.cachefly.net/4.0/tinymce.min.js
// @match      https://redes.axa.es/AssignmentsList*
// @match      file://localhost/*
// @copyright  Paqo
// ==/UserScript==

// definicion de constantes
//const URL_COLLABTIVE = "http://192.168.1.36/collabtive";
const URL_COLLABTIVE = "http://clientes.reymacj.es";

// funcion para convertir los acentos, dieresis, enyes, etc
var escaparHTML = (function() {
  var translate_re = /[áéíóúÁÉÍÓÚÑñ€ºªüÜ]/g;
  var translate = {
      "á": "&aacute;", "é": "&eacute;", "í": "&iacute;", "ó" : "&oacute;", "ú" : "&uacute;",
      "Á": "&Aacute;", "É": "&Eacute;", "Í": "&Iacute;", "Ó" : "&Oacute;", "Ú" : "&Uacute;",
      "Ñ": "&Ntilde;", "ñ": "&ntilde;", "€" : "&euro;", "º" : "&ordm;", "ª" : "&ordf;", "ü" : "&uuml;", "Ü" : "&Uuml;"
  };
  return function(s) {
    return ( s.replace(translate_re, function(match) { 
      return translate[match]; 
    }) );
  }
})();

// añadir fila a tabla principal (".container") que contendra todos los elementos de la personalizacion
$( "<tr><td colspan='2' id='reyma-custom-principal'></td></tr>" ).insertAfter( $(".container").find("tr:first").siblings().get(0) );
// div contenedor
var divContenedor = $("<div id='reyma-custom-contenedor'></div>");
var estilosDivContenedor = {
        height: "5em",
        border: "5px solid #4B78B1",
        float: "left",
        width: "99%"
};
$( divContenedor ).css( estilosDivContenedor );
// añadir div contenedor
divContenedor.appendTo("#reyma-custom-principal");

//div con el boton que envia los datos a reyma gestion
$("<div style='float:left; width: 100%; margin-top: 1.7em; padding-left: 2em;'>" +
  		"<button style='height: 2em; margin-right: 1em;' id='btnImportar'>Importar en REYMA</button>" + 
  		"<span id='span-mensaje'></span>" +
 "</div>")
	.appendTo("#reyma-custom-contenedor");

// 1.- nombre:
var nombreStr = "AXA-" + $("#HeaderAssignment1_lblSiniestro").text();

// 2.- datos generales:
//var infoGeneralStr = window.btoa( $(".HeaderAssignments").prop("outerHTML") );
var infoGeneralStr = window.btoa( escaparHTML($(".HeaderAssignments").prop("outerHTML")) );

// si es urgente, incluimos etiqueta debajo de datos generales: 
var esUrgente = "";
if ( $("#chk_UrgenteValue").is(':checked') ){
    esUrgente = "<br/>" + "<div style='color: #FF0000; font-weight: bold;'>URGENTE</div>" + "<br/><br/>";  
}

// 3.- datos "ENCARGO":
$("<tr id='tmp_cabEncargo'><td><span style='font-size: 15px; font-weight: bold;'>Lugar Intervenci&oacute;n:</span></td></tr>").insertBefore( $("#LugarIntervencion").find("tr:first") );
// ponemos el dni del tomador despues de los telefonos
$('<tr id="tmp_DNI"><td><label id="dni_asegurado">NIF/CIF:</label></td><th><span id="Label16">' + $('#l_NIFCIFValue').text() + '</span></th></tr>').insertBefore($("#LugarIntervencion").find("table tr:nth-child(3)"))
$("<tr id='fecSolInter'></tr>").insertAfter($("#LugarIntervencion").find("tr:last"));
$("#fecSolInter").html( $("#l_Fecha_intervencion").closest("tr").html() );
//var datosEncargoStr = window.btoa( esUrgente + $("#LugarIntervencion").prop("outerHTML") );
var datosEncargoStr = window.btoa( esUrgente + escaparHTML($("#LugarIntervencion").prop("outerHTML")) );
// deshacer cambios temporales para conservar aspecto visual de la pagina
$("#tmp_cabEncargo").remove();
$("#tmp_DNI").remove();
$("#fecSolInter").remove();

// 4.- datos "SINIESTRO"
var customTablaSiniestro = $('<table cellspacing="0" cellpadding="0" width="98%" border="0"><tbody>' + 
                             	'<tr id="cabDatosSiniestro"><td colspan="8" style="text-align: left; font-size: 15px"><span style="font-weight: bold;">Siniestro:</span></td></tr>' +
                             '</tbody></table>');
var tablaSntroOriginal = $("#div_2 table:eq(1)").html();

// filas de version, descripcion y oficio
$("#div_2 table:eq(1)").find("tr").filter(function(index) {
  return index > 0 && index <=4;
}).insertAfter( $(customTablaSiniestro).find("#cabDatosSiniestro") );

//var datosSiniestroStr = window.btoa( customTablaSiniestro.prop("outerHTML") );
var datosSiniestroStr = window.btoa( escaparHTML(customTablaSiniestro.prop("outerHTML")) );
// deshacer cambios temporales para conservar aspecto visual de la pagina
$("#div_2 table:eq(1)").html( tablaSntroOriginal );

// 5.- datos "POLIZA"
var tmp_mediador = $("table#Mediador").html();
var tmp_poliza = $("table#Poliza").html();

var customTablaPoliza = $('<table cellspacing="0" cellpadding="0" width="98%" border="0"><tbody>' + 
                          '<tr style="height: 2em;"><td style="text-align: left; font-size: 15px"><span style="font-weight: bold;">P&oacute;liza y mediador:</span></td></tr>' +
                          		'<tr id="cabDatosPoliza"></tr>' +
                          		'<tr id="cabDatosMediador"></tr>' +
                          '</tbody></table>');

// datos poliza y ramo
var tablaDatosPoliza = $("table#Poliza").find("table:first");
// eliminamos todas las filas menos la primera
tablaDatosPoliza.find("tr").filter(function(index) {
  return index > 0;
}).remove();
// eliminar datos no necesarios
tablaDatosPoliza.find("tr:first").children("th:last").remove();
tablaDatosPoliza.find("tr:first").children("td:last").remove();
tablaDatosPoliza.find("tr:first").children("th:last").remove();
tablaDatosPoliza.find("tr:first").children("td:last").remove();
// insertamos la tabla resultante en customTablaPoliza
$("<td>" + tablaDatosPoliza.prop("outerHTML") + "</td>").appendTo( $(customTablaPoliza).find("#cabDatosPoliza") );

// mediador
var tablaDatosMediador = $("table#Mediador").find("table:first");
// eliminamos todas las filas menos la primera
tablaDatosMediador.find("tr").filter(function(index) {
  return index > 0;
}).remove();
// poner bien label del nombre del medidor y eliminar datos no necesarios
tablaDatosMediador.find("tr:first").children("td:eq(0)").text("Nombre mediador:");
tablaDatosMediador.find("tr:first").children("th:last").remove();
tablaDatosMediador.find("tr:first").children("td:last").remove();
// insertamos tabla resultante en customTablaPoliza
$("<td>" + tablaDatosMediador.prop("outerHTML") + "</td>").appendTo( $(customTablaPoliza).find("#cabDatosMediador") );

var datosPolizaStr = window.btoa( customTablaPoliza.prop("outerHTML") );

// deshacer cambios temporales para conservar aspecto visual de la pagina
$("table#Mediador").html( tmp_mediador );
$("table#Poliza").html( tmp_poliza );


// funcion para comprobar que el siniestro no ha sido ya importado
comprobarSiniestro = function(data){
    if (data.existe){
		$("#btnImportar").attr("disabled", "disabled");
        $("#span-mensaje").text('El siniestro ' + nombreStr + ' ya existe en Reyma Gestión').css('color', '#FF8000');    
    }
};


finalizarImportacion = function(data){
    if (data.resultado){
        // proceso total completado con exito, informar de resultados
        $("#span-mensaje").text(data.mensaje).css({'color':'#105C25', 'padding-right':'4em'});
        // mostrar enlace en collabtive
        $("<a style='font-size: 14px;' target='_blank' href='" + URL_COLLABTIVE + "/manageproject.php?action=showproject&id=" + data.idSiniestro + "'>(ver en Reyma Gesti&oacute;n)</a>").insertAfter("#span-mensaje");
    	// deshabilitar boton para no volver a crear
        $("#btnImportar").attr("disabled", "disabled");        
    } else {
        $("#span-mensaje").text('Error importando siniestro: ' + data.mensaje).css('color', 'red'); 
    }
};

resultadoImportacion = function(data){
    if (data.resultado){
        // ha habido exito, actualizamos el resto de datos
        $.ajax({
            url: URL_COLLABTIVE + '/manageimport.php?callback=finalizarImportacion',
            dataType: 'jsonp',
            jsonp: false, 
            jsonpCallback: "",
            type: "GET",
            data: { datosSiniestro: datosSiniestroStr, datosPoliza: datosPolizaStr, idSiniestro: data.idSiniestro },
            success: finalizarImportacion
        });
    } else {
        $("#span-mensaje").text('Error importando siniestro: ' + data.mensaje).css('color', 'red'); 
    }
};

function crearScriptComprobacion() {
    var script = document.createElement ("script");
    script.src = URL_COLLABTIVE + "/manageimport.php?callback=comprobarSiniestro&nombre=" + nombreStr;
    document.body.appendChild(script);
}

crearScriptComprobacion();

// asignar funcion de importacion al boton
$("#btnImportar").on("click", function(event){
    event.preventDefault();
    $.ajax({
		url: URL_COLLABTIVE + '/manageimport.php?callback=resultadoImportacion',
        dataType: 'jsonp',
        jsonp: false, 
        jsonpCallback: "",
        type: "GET",
        data: { infoGeneral: infoGeneralStr, datosEncargo: datosEncargoStr, nombre: nombreStr },
        success: resultadoImportacion
	});     
}); 

console.log("opciones de importacion cargadas");