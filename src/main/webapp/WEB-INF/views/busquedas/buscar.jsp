<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    
    	<div><!-- contenedor general -->
    		<div id="contenedorBusquedas">
    			<div class="fila-formulario-busqueda">
    				<div class="celda-formulario-busqueda">
	    				<label for="numSiniestro">N&uacute;mero de siniestro:&nbsp;</label>
	    				<input id="numSiniestro" name="sinNumero" value="" />
	    			</div>
	    			<div class="celda-formulario-busqueda">
	    				<label for="numPoliza">N&uacute;mero de poliza:&nbsp;</label>
	    				<input id="numPoliza" name="sinPoliza" value="" />
	    			</div>
    			</div>
    			<div class="espaciador">&nbsp;</div>
    			<div class="fila-formulario-busqueda">
    				<div class="celda-formulario-busqueda">
	    				<label for="fechaDesde">Fecha desde:&nbsp;</label>
	    				<input id="fechaDesde" name="fechaDesde" value="" />
	    			</div>
	    			<div class="celda-formulario-busqueda">
	    				<label for="fechaHasta">Fecha hasta:&nbsp;</label>
	    				<input id="fechaHasta" name="fechaHasta" value="" />
	    			</div>
    			</div>   
    			<div class="espaciador">&nbsp;</div>
    			<div class="fila-formulario-busqueda">
    				<div class="celda-formulario-busqueda">
	    				<label for="nombre">Nombre:&nbsp;</label>
	    				<input id="nombre" name="nombre" value="" />
	    			</div>
	    			<div class="celda-formulario-busqueda">
	    				<label for="nif">Nif:&nbsp;</label>
	    				<input id="nif" name="nif" value="" />
	    			</div>
    			</div>	
    			<div class="espaciador">&nbsp;</div>
    			<div class="fila-formulario-busqueda">
    				<div class="celda-formulario-busqueda">
	    				<label for="direccion">Direcci&oacute;n:&nbsp;</label>
	    				<input id="direccion" name="direccion" value="" />
	    			</div>
	    			<div class="celda-formulario-busqueda">
	    				<label for="tlf">Tel&eacute;fono:&nbsp;</label>
	    				<input id="tlf" name="tlf" value="" />
	    			</div>
    			</div>	
    			<div class="espaciador">&nbsp;</div>
    			<div class="fila-formulario-busqueda">
    				<div class="celda-formulario-busqueda">
	    				<label for="compania">Compa&ntilde;&iacute;a:&nbsp;</label>
	    				<select name="compania" id="compania" style="width: 11.5em;">
	    					<option value="">-- cualquiera --</option>
					      	<c:forEach items="${companias}" var="compania" >
					      		<option value="${compania.comId}">${compania.comCodigo}</option>					      		
					      	</c:forEach>
					    </select>
	    			</div>
    			</div>
    			<%--
    			
    			 --%>	
    			<div style="height: 1.4em; float: left; width: 100%;">&nbsp;</div>
    			<div>    				
    				<button id="btnBuscar">Buscar</button>
    			</div>
    		</div>
    		
    		<div id="contenedorResultados">
    			<table id="resultados">
    				<tr class="resultados-cabecera">
			            <th>compa&ntilde;&iacute;a</th>
			            <th>fecha</th>
			            <th>n&uacute;m siniestro</th>			            
			            <th>domicilio</th>
			            <th>asegurado/perjudicado</th>			            
			            <th></th>
			        </tr>
			        <tr>
			            <td colspan="5">No hay resultados que mostrar</td>
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
		 	        	primary: "ui-icon-search"
		 	        }								
				}).click( function() {		   
					var action = "/reymasur/busquedas";
					var params = {
									sinNumero : $("#numSiniestro").val(),
									sinPoliza : $("#numPoliza").val(),									
									fechaIni : $("#fechaDesde").val(),	
									fechaFin : $("#fechaHasta").val(),
									perNif: $("#nif").val(),									
									perNombre: $("#nombre").val(),
									domDireccion : $("#direccion").val(),
									perTlf1 : $("#tlf").val(),
									sinComId : $("#compania").val()
					 			 };
					$.post(action, params, function( data ) {	
						if ( data.excedido ){
							$( "#mensajesUsuario" ).dialog( "option", "title", "Aviso" );
							$( "#mensajesUsuario" ).children("p").html( "El n&uacute;mero de resultados es demasiado grande, se debe refinar la b&uacute;squeda." );
							$( "#mensajesUsuario" ).dialog( "open" );
						} else {
							cargarTablaResultados(data, "resultados");
							formatearResultados("resultados");				
						}								
					}).error(function() {
						$( "#mensajesUsuario" ).dialog( "option", "title", "Error" );
						$( "#mensajesUsuario" ).children("p").html( "Se ha producido un error en la b&uacute;squeda" );
						$( "#mensajesUsuario" ).dialog( "open" );				 
					});
					
				});	
	            formatearResultados("resultados");
	            
	            // datepickers y combo de companias
	            $( "#fechaDesde" ).datepicker();
	            $( "#fechaHasta" ).datepicker();
	            $( "#compania" ).selectmenu();
	            
		  	});

		</script>
	
	  	