<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    
    	<div><!-- contenedor general -->
    	
	  		<div id="accordion-afectado">
	  			<c:choose>
	  				<c:when test="${fn:length(afectadodomiciliosiniestroes) == 0}">
	  					<jsp:include flush="false" page="afectado_inicial.jsp"></jsp:include>
	  				</c:when>
	  				<c:otherwise>						
						<c:forEach items="${afectadodomiciliosiniestroes}" var="item" varStatus="num">
				  			<c:set var="contador" value="${num.count}" />
				  			<c:set var="tituloH3" value="${item.adsTafId.tafDescripcion}" />
				  			<c:if test="${item.adsTafId.tafId == 5}">				  				
				  				<c:set var="tituloH3"><spring:message code="afectados_ambos" /></c:set>
				  			</c:if>
				  			<h3><c:out value="${tituloH3}" /></h3>
						    <div class="cont-afectados">
						    	<input type="hidden" name="adsId" id="adsId-${contador}" value="${item.adsId}" />
						    	<!-- afectado  -->						    				  			
						  		<div class="formafec-separador-filas">
						  			<div class="formafec-separador-col-izq" id="cont_chk-${contador}">
						  				<c:choose>
						  					<c:when test="${item.adsTafId.tafId == 3 || item.adsTafId.tafId == 5}">
						  						<input type="checkbox" id="chkAsegurado-${contador}"><label for="chkAsegurado-${contador}">Asegurado</label>
						  					</c:when>
						  					<c:otherwise>
						  						<input type="checkbox" checked="checked" id="chkAsegurado-${contador}"><label for="chkAsegurado-${contador}">Asegurado</label>
						  					</c:otherwise>
						  				</c:choose>
						  				<c:choose>
						  					<c:when test="${item.adsTafId.tafId == 4 || item.adsTafId.tafId == 5}">
						  						<input type="checkbox" id="chkPerjudicado-${contador}"><label for="chkPerjudicado-${contador}">Perjudicado</label>
						  					</c:when>
						  					<c:otherwise>
						  						<input type="checkbox" checked="checked" id="chkPerjudicado-${contador}"><label for="chkPerjudicado-${contador}">Perjudicado</label>
						  					</c:otherwise>
						  				</c:choose>
						  			</div>
						  		</div>
						  		<input type="hidden" name="perId" id="perId-${contador}" value="${item.adsPerId.perId}" />
						  		<div class="formafec-separador-filas">
						  			<div class="formafec-separador-col-izq">
						  				<label class="formafec-label-izq" for="perNombre-${contador}">Nombre:</label>
						    			<input type="text" class="formafec-input-izq" name="perNombre" id="perNombre-${contador}" value="${item.adsPerId.perNombre}" />
						  			</div>	  			
									<div class="formafec-separador-col-der">
						  				<label class="formafec-label-der" for="perNif-${contador}">NIF:</label>
						  				<input type="text" name="perNif" id="perNif-${contador}" value="${item.adsPerId.perNif}" />
						  			</div>
						  		</div>
						  		<div class="formafec-separador-filas">		  			
						  			<div class="formafec-separador-col-izq">
						  				<label class="formafec-label-izq" for="perTlf1-${contador}">Tel&eacute;fono:</label>
						    			<input type="text" style="width: 35%;" name="perTlf1" id="perTlf1-${contador}" value="${item.adsPerId.perTlf1}" />
						  			</div>	  			
						  			<div class="formafec-separador-col-der">
						  				<label class="formafec-label-der" for="perTlf2-${contador}">Móvil:</label>
						  				<input type="text" name="perTlf2" id="perTlf2-${contador}" value="${item.adsPerId.perTlf2}" />
						  			</div>
						  		</div>		
						  		<!-- domicilio  -->
						  		<input type="hidden" name="domId" id="domId-${contador}" value="${item.adsDomId.domId}" />
						    	<div class="formafec-separador-filas">
						  			<div class="formafec-separador-col-izq">
						  				<label class="formafec-label-izq" for="domDireccion-${contador}">Dirección:</label>
						    			<input type="text" class="formafec-input-izq" name="domDireccion" id="domDireccion-${contador}" value="${item.adsDomId.domDireccion}" />
						  			</div>	  			
						  			<div class="formafec-separador-col-der">
						  				<label class="formafec-label-der" for="domCp-${contador}">Código Postal:</label>
						  				<input type="text" name="domCp" id="domCp-${contador}" value="${item.adsDomId.domCp}" />
						  			</div>
						  		</div>
						  		<div class="formafec-separador-filas">		
						  			<div class="formafec-separador-3col-izq">
						  				<label class="formafec-separador-3col-label" for="domProvId-${contador}">Provincia:</label>
						  				<form:select cssStyle="width: 200px;" items="${provincias}" id="domProvId-${contador}"				  						
						  						path="domicilio-${contador}.domProvId" itemLabel="prvDescripcion" itemValue="prvId" />
						  							  				
						  			</div>	
						  			<div class="formafec-separador-3col-der">
						  				<label class="formafec-separador-3col-label" for="domMunId-${contador}">Municipio:</label>
						    			<%--
						    			<form:select cssStyle="width: 200px;" items="${municipios}" id="domMunId-${contador}" 
						  						path="domicilio-${contador}.domMunId" itemLabel="munDescripcion" itemValue="munId" />
										--%>	
										<input id="domMunId-${contador}" value="${item.adsDomId.domMunId.munDescripcion}" />
										<input id="IDdomMunId-${contador}" type="hidden" value="${item.adsDomId.domMunId.munId}" />			  									    			
						  			</div>			  							  					  			
						  		</div>
						  		<div class="formafec-separador-filas">
						  			<div class="formafec-botonera">
							  			<button id="btnGuardarDatosAfec-${contador}">Guardar</button>
							  		</div>
							  		<div class="formafec-botonera">
							  			<button id="btnLimpiarDatosAfec-${contador}">Limpiar</button>
							  		</div>
							  		<div class="formafec-botonera">
							  			<button id="btnEliminarDatosAfec-${contador}">Eliminar</button>
							  		</div>	
						  		</div>			  		
						    </div>
						</c:forEach>	
	  				</c:otherwise>
	  			</c:choose>				
			</div>
			<div style="margin-top: 1em;">
				<button id="addAfectado">Añadir afectado</button>
			</div>
	  	</div>	<!-- fin contenedor general -->
	  	