<%@page import="com.reyma.gestion.dao.Provincia"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
       			
		  			<h3>Trabajo</h3>
				    <div class="cont-trabajos">				    	
				    	<div class="formtra-separador-filas">
						  	<div class="formtra-separador-3col-izq">
						  		<!-- operario  -->						  		
						  		<label class="formtra-label-izq" for="opeNombrePila-1">Operario:</label>
						    	<input type="text" class="formtra-input-izq" name="opeNombrePila" id="opeNombrePila-1" 
						    		   value="${item.traOpeId.opeNombrePila}" />
						  	</div>	  			
							<div class="formtra-separador-3col-izq">
								<!-- oficio  -->
						  		<label class="formtra-label-der" for="ofiDescripcion-1">Oficio:</label>
						  		<input type="text" class="formtra-input-izq" name="ofiDescripcion" id="ofiDescripcion-1" 
						  			   value="${item.traOfiId.ofiDescripcion}" />
						  	</div>
						  	<div class="formtra-separador-3col-izq">
								<!-- fecha  -->
						  		<label class="formtra-label-der" for="traFecha-1">Fecha:</label>
						  		<input type="text" class="formtra-input-izq" name="traFecha" id="traFecha-1"
						  			   value="${item.traFecha}" />
						  	</div>
						</div>						  		
						<div class="formtra-separador-filas">		
							<div class="formafec-botonera">
								<button id="btnGuardarDatosTrabajo-1">Guardar</button>
							</div>				  			
							<div class="formtra-botonera">
								<button id="btnEliminarDatosTrabajo-1">Eliminar</button>
							</div>	
						</div>			  		
				    </div>
		