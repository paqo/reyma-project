//#####################################
//cuidado con las diferencias entre get y post (campo "data", 
//el campo "content-type" de las cabeceras a "application/x-www-form-urlencoded" (POST), 
//params en la URL (GET)
//#####################################

###### CORS con POST:

// ==UserScript==
// @name       Cross Domain Test
// @namespace  http://paco.scripts.agencia/
// @version    0.5
// @description  prueba POST
// @match      http://localhost:8080/PruebasWeb/*
// @copyright  2013, Yo
// ==/UserScript==

GM_xmlhttpRequest({
  method: "POST",
  url: "http://soporte.agenciaandaluzadelaenergia.es/SolicitudActuacionesAhorro/AjaxRequests",
  data: "op=0",
  headers: {
    "Content-Type": "application/x-www-form-urlencoded"
  },
  onload: function(response) {
    if (response.responseText != null) {
        //console.log("respuesta: " + response.responseText); // [{"con_id":1,"con_descripcion":"PRESIDENCIA E IGUALDAD"},{"con_id":2,"con_descripcion":"ADMINISTRACIÓN LOCAL Y RELACIONES INSTITUCIONALES"}...]
        var resp = JSON.parse(response.responseText);
        for (var i= 0;i<resp.length;i++){
            console.log( "=> " + resp[i].con_id + ": " + resp[i].con_descripcion);
        }        
    }
  }
});


###### CORS con GET:
	
	// ==UserScript==
	// @name       Cross Domain Test
	// @namespace  http://paco.scripts.agencia/
	// @version    0.6
	// @description  prueba GET
	// @match      http://localhost:8080/PruebasWeb/*
	// @copyright  2013, Yo
	// ==/UserScript==

	GM_xmlhttpRequest({
	  method: "GET",
	  //url: "http://soporte.agenciaandaluzadelaenergia.es/SolicitudActuacionesAhorro/AjaxRequests",
	  url: "http://pre-apolo:8083/GeneracionElectricaAndalucia/generacion/TecnologiasPorTipo.do?tipoTecnologia=ree",
	  /*data: "tipoTecnologia=ree",
	  headers: {
	    "Content-Type": "application/x-www-form-urlencoded"
	  },*/
	  onload: function(response) {
	    if (response.responseText != null) {
	        //console.log("respuesta: " + response.responseText); // [{"con_id":1,"con_descripcion":"PRESIDENCIA E IGUALDAD"},{"con_id":2,"con_descripcion":"ADMINISTRACIÓN LOCAL Y RELACIONES INSTITUCIONALES"}...]
	        var resp = JSON.parse(response.responseText);
	        for (var i= 0;i<resp.length;i++){
	            //console.log( "=> " + resp[i].con_id + ": " + resp[i].con_descripcion);
	            console.log( "=> " + resp[i].teclId + ": " + resp[i].tecDesc);
	        }        
	    }
	  }
	});