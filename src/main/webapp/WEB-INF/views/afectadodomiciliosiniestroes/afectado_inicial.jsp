<%@page import="com.reyma.gestion.dao.Provincia"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
       			
		  			<h3>Afectado</h3>
				    <div class="cont-afectados">
				    	<input type="hidden" name="adsId" id="adsId-1" value="" />
				    	<!-- afectado  -->			  			
				  		<div class="formafec-separador-filas">
				  			<div class="formafec-separador-col-izq" id="cont_chk-1">
				  				<input type="checkbox" checked="checked" id="chkAsegurado-1"><label for="chkAsegurado-1">Asegurado</label>
	  							<input type="checkbox" checked="checked" id="chkPerjudicado-1"><label for="chkPerjudicado-1">Perjudicado</label>
				  			</div>
				  		</div>
				  		<div class="formafec-separador-filas">
				  			<div class="formafec-separador-col-izq">
				  				<label class="formafec-label-izq" for="perNombre-1">Nombre:</label>
				    			<input type="text" class="formafec-input-izq" name="perNombre" id="perNombre-1" value="" />
				  			</div>	  			
							<div class="formafec-separador-col-der">
				  				<label class="formafec-label-der" for="perNif-1">NIF:</label>
				  				<input type="text" name="perNif" id="perNif-1" value="" />
				  			</div>
				  		</div>
				  		<div class="formafec-separador-filas">		  			
				  			<div class="formafec-separador-col-izq">
				  				<label class="formafec-label-izq" for="perTlf1-1">Tel&eacute;fono:</label>
				    			<input type="text" style="width: 35%;" name="perTlf1" id="perTlf1-1" value="" />
				  			</div>	  			
				  			<div class="formafec-separador-col-der">
				  				<label class="formafec-label-der" for="perTlf2-1">Móvil:</label>
				  				<input type="text" name="perTlf2" id="perTlf2-1" value="" />
				  			</div>
				  		</div>		
				  		<!-- domicilio  -->
				    	<div class="formafec-separador-filas">
				  			<div class="formafec-separador-col-izq">
				  				<label class="formafec-label-izq" for="domDireccion-1">Dirección:</label>
				    			<input type="text" class="formafec-input-izq" name="domDireccion" id="domDireccion-1" value="" />
				  			</div>	  			
				  			<div class="formafec-separador-col-der">
				  				<label class="formafec-label-der" for="domCp-1">Código Postal:</label>
				  				<input type="text" name="domCp" id="domCp-1" value="" />
				  			</div>
				  		</div>
				  		<div class="formafec-separador-filas">		
				  			<div class="formafec-separador-3col-izq">
				  				<label class="formafec-separador-3col-label" for="domProvId-1">Provincia:</label>
				  				<form:select cssStyle="width: 200px;" items="${provincias}" id="domProvId-1"				  						
				  						path="domicilio.domProvId" itemLabel="prvDescripcion" itemValue="prvId" />	  				
				  			</div>	
				  			<div class="formafec-separador-3col-der">
				  				<label class="formafec-separador-3col-label" for="domMunId-1">Municipio:</label>
				    			<%--
				    			<form:select cssStyle="width: 200px;" items="${municipios}" id="domMunId-1" 
				  						path="domicilio.domMunId" itemLabel="munDescripcion" itemValue="munId" />
		  						 --%>		
		  						 <input id="domMunId-1" />				  
		  						 <input id="IDdomMunId-1" type="hidden" value="" />											    			
				  			</div>			  							  					  			
				  		</div>
				  		<div class="formafec-separador-filas">
				  			<div class="formafec-botonera">
					  			<button id="btnGuardarDatosAfec-1">Guardar</button>
					  		</div>
					  		<div class="formafec-botonera">
					  			<button id="btnLimpiarDatosAfec-1">Limpiar</button>
					  		</div>
					  		<div class="formafec-botonera">
					  			<button id="btnEliminarDatosAfec-1">Eliminar</button>
					  		</div>	
				  		</div>			  		
				    </div>
		