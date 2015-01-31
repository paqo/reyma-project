// ==UserScript==
// @name       Importar siniestros directamente desde pagina de AXA
// @namespace  http://paqo.scripts.reyma/
// @version    110
// @description  Incluye un boton debajo de los datos del encargo para importarlos en ReymaSur Gestion
// @require    http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js
// @match 	   http://localhost:8080/GDriveAPIJS/*
// @match      https://redes.axa.es/AssignmentsList*
// @copyright  Paqo
// ==/UserScript==

// definicion de constantes
const URL_REYMA = "http://localhost:8080/reymasur";


// funciones

function obtenerParametros() {
    var params = "";
    
    //SINIESTRO
    params += "sinNumero=" + $("#HeaderAssignment1_lblSiniestro").text();
    params += "&sinPoliza=" + $("#l_NumPolizaValue").text();
    params += "&sinFechaEncargo=" + $("#l_FechDeclaracionValue").text() + " 00:00";
    params += "&sinFechaOcurrencia=" + $("#l_FechOcurrValue").text() + " 00:00";
    params += "&sinDescripcion=" + $("#l_VersionValue").text();
    params += "&sinMediador=" + $("#l_NombreMediadorValue").text() + " " + $("#l_TelMediadorValue").text();
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
    
    var prov_mun_cp_aseg = obtenerProvMunicipioCP( $("#l_DirecTomadorValue").text().trim(), "asegurado" );
    params += "&provId=" + prov_mun_cp_aseg[0];
    params += "&munId=" + prov_mun_cp_aseg[1];
    params += "&domCp=" + prov_mun_cp_aseg[2];
    
    if ($("#l_TipoLugarValue").text().toUpperCase() === "DOMICILIO ASEGURADO") {
        params += "&mismolugar=true";
        // si el domicilio del asegurado es el del perjudicado, 
        // damos siempre preferencia a la dirección que aparece
        // en el encabezamiento:
        params += "&domDireccion=" + $("#HeaderAssignment1_lblDir_Riesgo").text().trim();        
    } else {
     	params += "&domDireccion=" + $("#l_DirecTomadorValue").text().replace(',SEVILLA','').replace(', SEVILLA','').trim();
        // DATOS DEL PERJUDICADO:
        params += "&perjNombre="  +$("#l_NombreLugarValue").text();
        params += "&perjDireccion=" + $("#HeaderAssignment1_lblDir_Riesgo").text().trim(); 
        params += "&perjTlf1=" + $("#l_TelefLugarValue").text().replace('(FIJO)','').trim();
        params += "&perjTlf2=" + $("#l_TelefLugarValue2").text().replace('(MOVIL)','').trim();
        //var prov_mun_cp_perj = obtenerProvMunicipioCP( $("#HeaderAssignment1_lblDir_Riesgo").text().trim() );
        var prov_mun_cp_perj = obtenerProvMunicipioCP( $("#l_DireccionLugarVaue").text().trim(), "perjudicado" );        
        params += "&perjProvId=" + prov_mun_cp_perj[0];
        params += "&perjMunId=" + prov_mun_cp_perj[1];
        params += "&perjCp=" + prov_mun_cp_perj[2];
    }
    console.log("-> params: " + params);
    
    return params;
}

function obtenerProvMunicipioCP(direccion, tipo) {
	// sincrono...
	$.ajaxSetup({
	    async: false
	});
	
	var resultado = [];
	var googleApi = "https://maps.googleapis.com/maps/api/geocode/json";
	$.getJSON( googleApi, {
		address : direccion,
		region: "es",
		sensor: "false"
	})
	.error(function( data ) {
		console.log("error recibiendo respuesta de google" );
	}).success(function( data ) {
		if ( data && data.status == 'OK' ){
			var direccionFormateada = data.results[0].formatted_address;
			console.log("=> direccionFormateada de " + tipo + ": " + direccionFormateada);			
			var precision = data.results[0].geometry.location_type;
            console.log("precision: " + precision);
            var result = direccionFormateada.split(",");
			//console.log("=> " + result[result.length-3]);
			var provincia = result[result.length-2];
			var mun_CP = result[result.length-3];
			var cp = mun_CP.match(/4\d{4}/);
			resultado.push(provincia.trim());
			resultado.push(mun_CP.replace(cp,"").trim());
			resultado.push(cp);
            if (precision == "APPROXIMATE" || precision == "GEOMETRIC_CENTER"){ 
            	// alta probabilidad de error en 
				// CP y municipio, avisar
            	if (cp == null){
            		resultado[2]="";
            		alert("no se ha podido obtener el cp del domicilio del " + tipo + "\n(comprobar manualmente CP y municipio del " + tipo + ")");
            	}else {
            		alert("comprobar municipio y cp en domicilio de " + tipo + ":\n" + mun_CP + "?");
            	}
            	
			}            
		} 
	});
	// asincrono de nuevo
	$.ajaxSetup({
	    async: true
	});
	console.log("=> resultado: " + resultado);
	return resultado;
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
        	console.log("recibiendo respuesta del servidor...");
          	if ( response.responseText.indexOf("j_username") != -1){
            	alert("debe logarse primero en ReymaSur Gestion");
                return;
          	}
        	if (response.responseText != null) {           
                try {
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
                } catch (e) {
                    alert("Se ha producido un error en la comunicación con el servidor.");
                    console.log("error: " + e);
                }
            } else {
            	alert("No se ha podido obtener repuesta del servidor. Inténtelo de nuevo más tarde");
            }
      }
    });			
});

console.log("################################################");
console.log("preparado.");
