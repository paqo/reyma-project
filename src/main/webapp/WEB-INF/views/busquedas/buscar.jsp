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
    				<input id="numSiniestro" name="sinNumero" value="" />
    			</div>
    			<div style="height: 1.4em;">&nbsp;</div>
    			<div>    				
    				<button id="btnBuscar">Buscar</button>
    			</div>
    		</div>
    		
    		<div id="contenedorResultados">
    			<table id="resultados">
    				<tr id="resultados-cabecera">
			            <th>compa&ntilde;&iacute;a</th>
			            <th>fecha</th>
			            <th>n&uacute;m siniestro</th>			            
			            <th>domicilio</th>
			            <th>asegurado/perjudicado</th>			            
			            <th></th>
			        </tr>
			        <tr>
			            <td colspan="5">No hay resultados</td>
			            <td><div class="arrow"></div></td>
			        </tr>
    			</table>
    		</div>

	  	</div>	<!-- fin contenedor general -->
	  	
	  	<div id="mensajesUsuario" title=" ">		
			<p style="text-indent: 0px; font-size: 12px;"></p>
		</div>
		
		<div id="mensajeConfirmacion" title="Confirmar eliminación de datos">
	    	<p style="text-indent: 0px; font-size: 12px;"></p>
		</div>
	  	  	
	  	<script type="text/javascript">	  	
		  	$(document).ready(function(){
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
						var filaCabecera = $("#resultados").find("#resultados-cabecera").clone();
						$("#resultados").empty();
						$( filaCabecera ).appendTo( "#resultados" );
						var filaRaw;						
						$.each(data, function( index, value ) {
							  filaRaw = "<tr>";
							  filaRaw += "<td>" + data[index].compania + "</td>";
							  filaRaw += "<td>" + data[index].fecha + "</td>";
							  filaRaw += "<td>" + data[index].numeroSiniestro + "</td>";
							  filaRaw += "<td>" + data[index].domicilio + "</td>";
							  filaRaw += "<td>" + data[index].asegurado + "</td>";
							  filaRaw += "<td><div class='arrow'></div></td>";							  
							  filaRaw += "</tr>"
							  $( filaRaw ).appendTo( "#resultados" );
							  filaRaw = "<tr>";
							  filaRaw += "<td colspan='6'>"; 
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
							  filaRaw += "</td>";							  
							  filaRaw += "</tr>";
							  $( filaRaw ).appendTo( "#resultados" );
						});
						formatearResultados("resultados");
					}).error(function() {
						$( "#mensajesUsuario" ).dialog( "option", "title", "Error" );
						$( "#mensajesUsuario" ).children("p").html( "Se ha producido un error en la b&uacute;squeda" );
						$( "#mensajesUsuario" ).dialog( "open" );				 
					});
					
				});	
	            formatearResultados("resultados");
		  	});

		</script>
	
	  	