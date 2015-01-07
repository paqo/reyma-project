// ==UserScript==
// @name       Importar siniestros directamente desde pagina de AXA
// @namespace  http://paqo.scripts.reyma/
// @version    101
// @description  Incluye un boton debajo de los datos del encargo para importarlos en ReymaSur Gestion
// @require    http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js
// @match 	   http://localhost:8080/GDriveAPIJS/*
// @copyright  Paqo
// ==/UserScript==

// definicion de constantes
const URL_REYMA = "http://localhost:8080/gestion";


// funciones

function obtenerParametros() {
    var params = "";
    
    //SINIESTRO
    params += "sinNumero=" + $("#HeaderAssignment1_lblSiniestro").text();
    params += "&sinPoliza=" + $("#l_NumPolizaValue").text();
    params += "&sinFechaComunicacion=" + $("#l_FechDeclaracionValue").text() + " 00:00";
    params += "&sinFechaOcurrencia=" + $("#l_FechOcurrValue").text() + " 00:00";
    params += "&sinDescripcion=" + $("#l_VersionValue").text();
    params += "&obs=" + $("#Label1").text(); // observaciones se concatena a descripcion
    params += "&sinComId=1";
    params += "&sinEstId=3"; // recibido
    if ($("#chk_UrgenteValue").is(":checked")) {
        params += "&sinUrgente=1";
    } else {
        params += "&sinUrgente=0";
    }   
    params += "&tipoSiniestro=" + $("#l_DescripPlanosValue").text();    
    
    console.log("-> params: " + params);
    
    return params;
}

console.log("################################################");
console.log("#####SCRIPT DE IMPORTAR EN REYMASUR ACTIVADO####");
console.log("################################################");

var customDiv = $("<tr><td>" + 
                  "<div style='float: left; border: 5px solid green; width:99%'>" + 
                  	"<div style='float: left;'>Traspasar siniestro a Sistema ReymaSur</div>" + 
                  	"<div style='float: left; padding-left: 1em;'><input id='import-reymasur' type='button' style='width: 5em;' value='OK' /></div>" + 
                  "</div>" + 
                  "</td></tr>");

customDiv.appendTo( ".container > tbody" );

$("#import-reymasur").on( "click", function() {
    
    console.log("enviando peticion...");
    
    var params = obtenerParametros();
    
	GM_xmlhttpRequest({
      method: "POST",
      url: URL_REYMA + "/integracion",
      data: params,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      onload: function(response) {
        console.log("entra en callback...");
        if (response.responseText != null) {           
            var respuesta = JSON.parse(response.responseText);                         
            console.log("result: " + respuesta.resultado);
            if ( respuesta.resultado == "existe" ){
                alert(respuesta.descripcion);
            } else {
               	alert("El siniestro se ha importado con éxito");
                $( "<div style='float: left; margin-left:2em;'><a target='_blank' href='" + URL_REYMA + "/" + respuesta.descripcion + "'>ver en ReymaSur</a></div>" ).appendTo($("#import-reymasur").parent());
            }           
        }
      }
    });			
});

console.log("################################################");
console.log("fin del script");

