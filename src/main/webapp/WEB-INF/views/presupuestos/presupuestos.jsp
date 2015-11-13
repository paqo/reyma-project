<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

    	<div><!-- contenedor general -->
    	
	  		<div id="contenedor-presupuestos">	  	
	  			<ul>
	  				<li>presupuesto 1</li>
	  				<li>presupuesto 2</li>
	  			</ul>
			</div>
			<div style="margin-top: 2em; text-align: right;">
				<button id="generarPresupuesto">Crear Prespuesto</button>
			</div>			
					
			<div id="formulario-presupuesto" title="Crar un nuevo presupuesto">
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
					
					<div id="pres-cont" style="float: left; width: 95%; margin-top: 1.8em;">
						<%-- lineas del presupuesto  --%>
					</div> 	
														
					<div style="float: left; width: 100%; margin-top: 3em;">
						<button id="addCabeceraPresupuesto">A&ntilde;adir Cabecera</button>
						<button id="addLineaPresupuesto">A&ntilde;adir Concepto</button>
					</div>			        
				</div>
			</div>
			
	  	</div>	<!-- fin contenedor general -->
	  	