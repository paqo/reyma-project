<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

    	<div><!-- contenedor general -->
    	
	  		<div id="contenedor-presupuestos">	  	
	  			<c:choose>
	  				<c:when test="${fn:length(presupuestos) gt 0}">
			  			<c:forEach items="${presupuestos}" var="item" varStatus="num">							
							<div data-id="${item.id}" style="float: left; width: 100%; margin-bottom: 1.3em;">
								<c:set var="labelBoton" value="" />
								<c:if test="${not empty item.numPresupuesto}">
									<c:set var="labelBoton" value="${item.numPresupuesto}" />
								</c:if>
								<c:if test="${not empty item.fechaPresupuesto}">
									<c:set var="labelBoton" value="${labelBoton} ${item.fechaPresupuesto}" />
								</c:if>
								<button id="btn-pres-${item.id}">Prespuesto ${labelBoton}</button>							
							</div>
						</c:forEach>
	  				</c:when>
	  				<c:otherwise>
	  					No se ha generado ning&uacute;n presupuesto
	  				</c:otherwise>
	  			</c:choose>
			</div>
			<div style="margin-top: 2em; text-align: right;">
				<button id="generarPresupuesto">Nuevo Prespuesto</button>
			</div>			
					
			<div id="formulario-presupuesto" title="Crar un nuevo presupuesto">
				<input type="hidden" id="idPresupuesto" name="idPresupuesto" value="" />
				<input type="hidden" id="valoresCboIva" name="valoresCboIva" value='${valoresCboIva}' />
				<input type="hidden" id="valoresCboOficios" name="valoresCboOficios" value='${valoresCboOficios}' />
				
				<div style="float: left; width: 100%;">
					<div style="float: left; width: 100%;">
						<div style="float: left; width: 30%;">
							Fecha Presupuesto:&nbsp;<input type="text" size="15" id="presFecha" name="presFecha"/>
						</div>
						<div style="float: left; width: 30%;">
							N&uacute;mero Presupuesto:&nbsp;<input type="text" size="15" id="presNumero" name="presNumero"/>
						</div>
						<div style="float: left; padding-top: 2px;">
							Cliente:&nbsp;
							<form:select cssStyle="width: 170px;" path="afectadodomiciliosiniestroes" id="presAfectado">
					        	<form:options itemLabel="adsPerId.perNombre" itemValue="adsId" items="${afectadodomiciliosiniestroes}" />
					        </form:select>
						</div>
					</div>
					
					<div id="pres-cont">
						<%-- lineas del presupuesto  --%>
					</div> 	
					
					<div style="float: left; width: 100%; margin-top: 0.7em;">
						<div style="float: left;">
							<button id="addCabeceraPresupuesto">A&ntilde;adir Cabecera</button>
						</div>
						<div style="float: left;">
							<form:select cssStyle="width: 14em;" path="oficios" id="presOficios">
					        	<form:options itemLabel="ofiDescripcion" itemValue="ofiId" items="${oficios}" />
							</form:select>
						</div>						
					</div>														
					<div style="float: left; width: 100%; margin-top: 3em;">						
						<button id="addLineaPresupuesto">A&ntilde;adir Concepto</button>
					</div>
					
				</div>
			</div>			
	  	</div>	<!-- fin contenedor general -->
	  	