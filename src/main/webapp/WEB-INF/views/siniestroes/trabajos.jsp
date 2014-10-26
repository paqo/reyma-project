<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

    	<div><!-- contenedor general -->
    	
	  		<div id="accordion-trabajos">
	  			<c:choose>
	  				<c:when test="${fn:length(trabajos) == 0}">
	  					<jsp:include flush="false" page="trabajos_inicial.jsp"></jsp:include>
	  				</c:when>
	  				<c:otherwise>
			  			<c:forEach items="${trabajos}" var="item" varStatus="num">
				  			<c:set var="contador" value="${num.count}" />
				  			<h3>Operario</h3>
						    <div class="cont-trabajos">
						    	<input type="hidden" name="traId" id="traId-${contador}" value="${item.traId}" />						    	
						  		<div class="formtra-separador-filas">
						  			<div class="formtra-separador-3col-izq">
						  			<!-- operario  -->
						  				<input type="hidden" name="opeId" id="opeId-${contador}" value="${item.traOpeId.opeId}" />
						  				<label class="formtra-label-izq" for="opeNombrePila-${contador}">Operario:</label>
						    			<form:select cssStyle="width: 200px;" items="${operarios}" id="traOpeId-${contador}"				  						
						  						path="trabajo-${contador}.traOpeId" itemLabel="opeNombrePila" itemValue="opeId" />						    			
						  			</div>	  			
									<div class="formtra-separador-3col-izq">
									<!-- oficio  -->
										<input type="hidden" name="ofiId" id="ofiId-${contador}" value="${item.traOfiId.ofiId}" />
						  				<label class="formtra-label-der" for="ofiDescripcion-${contador}">Oficio:</label>						  				
						  				<form:select cssStyle="width: 200px;" items="${oficios}" id="traOfiId-${contador}"				  						
						  						path="trabajo-${contador}.traOfiId" itemLabel="ofiDescripcion" itemValue="ofiId" />
						  			</div>
						  			<div class="formtra-separador-3col-izq">
									<!-- fecha  -->
						  				<label class="formtra-label-der" for="traFecha-${contador}">Fecha:</label>						  				
						  				<input type="text" class="formtra-input-izq" name="traFecha" id="traFecha-${contador}" 
						  					   value="<fmt:formatDate pattern='dd/MM/yyyy HH:mm' value='${item.traFecha.time}' />" />
						  			</div>
						  		</div>						  		
						  		<div class="formtra-separador-filas">						  			
							  		<div class="formafec-botonera">
							  			<button id="btnGuardarDatosTrabajo-${contador}">Guardar</button>
							  		</div>
							  		<div class="formtra-botonera">
							  			<button id="btnEliminarDatosTrabajo-${contador}">Eliminar</button>
							  		</div>	
						  		</div>			  		
						    </div>
						</c:forEach>	
	  				</c:otherwise>
	  			</c:choose>				
			</div>
			<div style="margin-top: 1em;">
				<button id="addTrabajo">Añadir trabajo</button>
			</div>
	  	</div>	<!-- fin contenedor general -->
	  	