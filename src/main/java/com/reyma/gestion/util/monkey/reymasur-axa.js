// ==UserScript==
// @name       Importar siniestros directamente desde pagina de AXA
// @namespace  http://paqo.scripts.reyma/
// @version    101
// @description  Incluye un boton debajo de los datos del encargo para importarlos en ReymaSur Gestion
// @require    http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js
// @match 	   http://localhost:8080/GDriveAPIJS/*
// @match      https://redes.axa.es/AssignmentsList*
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
    //ASEGURADO
    params += "&perNombre=" + $("#l_NombreTomadorValue").text();
    params += "&perNif=" + $("#l_NIFCIFValue").text();
    var telefonos = $("#l_TelTomadorValue").text().match(/(\d[\d\.]*)/g);
    if ( telefonos ){
         params += "&perTlf1=" + telefonos[0];
        if (telefonos.length == 2 ){           
            params += "&perTlf2=" + telefonos[1];
        }
    } else { // no se ha podido extraer ninguno
        params += "&perTlf1=" + $("#l_TelTomadorValue").text();
    }
    var cpRaw = $("#l_DirecTomadorValue").text().match(/\d{5}/);
    var cp = "";
    if (cpRaw && cpRaw.length == 1){
        cp = cpRaw[0];    
    	params += "&domCp=" + cp;       
    } 
    params += "&domDireccion=" + $("#l_DirecTomadorValue").text().replace(',SEVILLA','').replace(', SEVILLA','').trim();
    var provinciaRaw = $("#l_DirecTomadorValue").text().split(" ");
    var provincia = provinciaRaw[provinciaRaw.length - 1];
    if ( provincia ){        
    	params += "&provId=" + provincia.replace(',','').trim();    
    }    
    /*var municipio = $("#l_DirecTomadorValue").text().replace(provincia,'').trim().replace(',','').trim().replace(cp,'').trim().split(" ");    
    if ( municipio && municipio.length > 1 ) {
         params += "&munId=" + municipio[municipio.length - 1];
         console.log("municipio2: " + municipio[municipio.length - 1]);
    }*/
    var municipio = $("#l_DirecTomadorValue").text().replace(provincia,'').trim().replace(',','').trim().replace(cp,'').trim();    
    params += "&munId=" + municipio;
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
            try {
                
            } catch (e) {
              alert("Se ha producido un error en la comunicación con el servidor.");
            }
            
            var respuesta = JSON.parse(response.responseText);                         
            console.log("result: " + respuesta.resultado);
            if ( respuesta.resultado == "existe" ){
                alert(respuesta.descripcion);
            } else if ( respuesta.resultado == "creado" ) { 
               	alert("El siniestro se ha importado con éxito");
                $( "<div style='float: left; margin-left:2em;'><a target='_blank' href='" + URL_REYMA + "/" + respuesta.descripcion + "'>ver en ReymaSur</a></div>" ).appendTo($("#import-reymasur").parent());
            } else if ( respuesta.resultado == "error" ) { 
                alert("El siniestro no se ha podido crear correctamente, revisar los datos en Reyma Gestion.");
                $( "<div style='float: left; margin-left:2em;'><a target='_blank' href='" + URL_REYMA + "/" + respuesta.descripcion + "'>ver en ReymaSur</a></div>" ).appendTo($("#import-reymasur").parent());
            }
        }
      }
    });			
});

console.log("################################################");
console.log("preparado.");

