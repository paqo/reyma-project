<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    
    	<div><!-- contenedor general -->
    		<div id="contenedorBusquedas">
    			<div>
    				<label for="numSiniestro">N&uacute;mero de siniestro:&nbsp;</label>
    				<input id="numSiniestro" name="sinNumero" value="111111111111" />
    			</div>
    			<div>    				
    				<button id="btnBuscar">Buscar</button>
    			</div>
    		</div>

	  	</div>	<!-- fin contenedor general -->
	  	
	  	<div id="mensajesUsuario" title=" ">		
			<p style="text-indent: 0px; font-size: 12px;"></p>
		</div>
		
		<div id="mensajeConfirmacion" title="Confirmar eliminación de datos">
	    	<p style="text-indent: 0px; font-size: 12px;"></p>
		</div>
	  	  	
	  	<script type="text/javascript">	
			$(function() {
				
				// crear ventana para mensajes
				$( "#mensajesUsuario" ).dialog({
		    	    autoOpen: false,
		    	    height: 200,
		    		width: 500,
		    	    modal: true,
		    	    resizable: false,
		    	    buttons: {
		    	        "Aceptar": function() {    	         
		    	            $( this ).dialog( "close" );
		    	        }
		    	    }
		    	});
		   
				// boton de buscar
				$( '#btnBuscar' ).button({		    	
		 	        icons: {	 	        	
		 	        	primary: "ui-icon-disk"
		 	        }								
				}).click( function() {		   
					var action = "/gestion/busquedas";
					var params = {
									sinNumero : $("#numSiniestro").val()
					 			 };
					$.post(action, params, function( data ) {				
						// resultado
						console.log("=>" + data.test);
					}).error(function() {
						$( "#mensajesUsuario" ).dialog( "option", "title", "Error" );
						$( "#mensajesUsuario" ).children("p").html( "Se ha producido un error en la b&uacute;squeda" );
						$( "#mensajesUsuario" ).dialog( "open" );				 
					});
					
				});	
				
			});
			
		</script>
	
	  	