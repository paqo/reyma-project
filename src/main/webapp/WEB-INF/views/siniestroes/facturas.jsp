<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

    	<div><!-- contenedor general -->
    	
	  		<div id="contenedor-facturas">	  	
	  			<c:choose>
	  				<c:when test="${fn:length(facturas) gt 0}">
			  			<c:forEach items="${facturas}" var="item" varStatus="num">		
							<div style="width: 100%;">
								<input type="radio" id="facId_${item.id}" name="idFactura" value="${item.id}" />
								<label style="width: 30em !important; padding-top: 0.5em; background-color: #F7DBB6;" 
										for="facId_${item.id}">Factura del d&iacute;a <span id="ffac-${item.id}">${item.fechaFactura}</span> (<span id="nfac-${item.id}">${item.numFactura}</span>)
								</label>
								<input type="hidden" value="${item.idAfectado}" id="afectadoFactura-${item.id}" />
							</div>											
						</c:forEach>
						<div style="margin-top: 1em;">						
							<button id="verFactura">Ver factura</button>
							<button id="eliminarFactura" style="margin-left: 3em;">Eliminar factura</button>
						</div>
	  				</c:when>
	  				<c:otherwise>
	  					No se ha generado ninguna factura para el siniestro
	  				</c:otherwise>
	  			</c:choose>
			</div>
			<div style="margin-top: 1.8em; text-align: right;">
				<button id="generarFactura">Generar factura</button>
			</div>			
									
			<div id="formulario-facturas" title="Dar de alta nueva factura">		
				<input type="hidden" id="idFacturaAbierta" name="idFacturaAbierta" value="" />
				
				<input type="hidden" id="valoresCboIva" name="valoresCboIva" value='${valoresCboIva}' />
				<input type="hidden" id="valoresCboOficios" name="valoresCboOficios" value='${valoresCboOficios}' />
				
				<div style="float: left; width: 100%;">
					<div style="float: left; width: 100%;">
						<div style="float: left; width: 30%;">
							Fecha Factura:&nbsp;<input type="text" size="15" id="facFecha" name="facFecha"/>
						</div>
						<div style="float: left; width: 30%;">
							N&uacute;mero Factura:&nbsp;<input type="text" size="15" id="facNumero" name="facNumero"/>
						</div>
						<div style="float: left; padding-top: 2px;">
							Asegurado/Perjudicado:&nbsp;
							<form:select path="afectadodomiciliosiniestroes" id="facAfectado">
					        	<form:options itemLabel="adsPerId.perNombre" itemValue="adsId" items="${afectadodomiciliosiniestroes}" />
					        </form:select>
						</div>
					</div>
					<div style="float: left; width: 100%; margin-top: 1.8em;">
						<table id="tablaFactura" class="table table-striped">
				            <thead>
				            	<tr>
				            		<th style="width: 25%;">Oficio</th>
				            		<th style="width: 50%;">Concepto</th>
				            		<th style="width: 10%;">Coste</th>
				            		<th style="width: 10%;">Iva</th>
				            		<th style="width: 5%;"></th>
				            	</tr>
				            </thead>
					        <tbody>
					        	<tr>
					        		<td>
					        			<form:select path="oficios" id="cbOficio-1">
					        				<form:options itemLabel="ofiDescripcion" itemValue="ofiId" items="${oficios}" />
					        			</form:select>
					        		</td>
					            	<td></td>
					            	<td>0</td>
					            	<td>
					            		<form:select path="ivas" id="cbIva-1">
						        			<form:options itemLabel="ivaValor" itemValue="ivaId" items="${ivas}" />
						        		</form:select>
					            	</td>
					            	<td style="text-align: center;">
					            		<button class="eliminarLineaFactura"></button>
					            		<input type="hidden" name="idLinea-1" id="idLinea-1" value="" />
					            	</td>
					            </tr>				              			              
					        </tbody>
							<tfoot><tr><th><span style="font-weight: bold;">TOTAL</span></th><th></th><th></th><th></th><th></th></tr></tfoot>
				        </table>
					</div>					
					<div style="float: left; width: 100%; margin-top: 1em;">
						<button id="addLineaFactura">A&ntilde;adir L&iacute;nea</button>
					</div>			        
				</div>
			</div>
			
	  	</div>	<!-- fin contenedor general -->
	  	