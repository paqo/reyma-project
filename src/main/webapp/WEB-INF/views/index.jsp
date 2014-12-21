<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<spring:message var="app_name" code="application_name" htmlEscape="false" />
  	<spring:message var="title" code="welcome_titlepane" arguments="${app_name}" htmlEscape="false" />
  
  	<div id="contenedorIndex">
  		<div style="width: 100%; text-align: center; height:1.5em; padding-top:1em; padding-bottom:0.25em; background-color: #61A023;">
  			<span style="font-weight: bold; color:white;">SINIESTROS DEL D&Iacute;A</span>
  		</div>
  		<div style="width: 100%;">
  			<table id="sin-hoy">
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
		<div style="width: 100%; text-align: center; height:1.5em; margin-top: 1em; padding-top:1em; padding-bottom:0.25em; background-color: #61A023;">
  			<span style="font-weight: bold; color:white;">SINIESTROS DE AYER</span>
  		</div>
		<div style="width: 100%;">
    		<table id="sin-ayer">
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
  	</div>
        
    <script type="text/javascript">	  
    		//var test = [{"asegurado":"nombre","compania":"AXA","descripcion":"sale agua","domicilio":"actualiza bien?","estado":"Finalizado","fecha":"17/09/2014 00:00","fechaEntrada":"01/09/2014 00:00","id":"1","numeroSiniestro":"111111111111","tipo":"Carpintería"}];
		  	$(document).ready(function(){	            
	            // cargar datos hoy	            
	           	var action = "/gestion/busquedas/inicio";
				var params = {};
				$.post(action, params, function( data ) {
					cargarTablaResultados(data, "sin-hoy");
					formatearResultados("sin-hoy");
				});	
				// ayer
				params = {fecha : "ayer"};
				$.post(action, params, function( data ) {
					cargarTablaResultados(data, "sin-ayer");
					formatearResultados("sin-ayer");
				});		                 
		  	});
	</script>
  