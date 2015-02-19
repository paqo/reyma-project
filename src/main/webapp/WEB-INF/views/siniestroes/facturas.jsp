<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

    	<div><!-- contenedor general -->
    	
	  		<div id="contenedor-facturas">	  	
	  			<c:if test="${fn:length(facturas) gt 0}">
		  			<div id="navcontainer">
						<ul id="listaFacturas">
							<c:forEach items="${facturas}" var="item" varStatus="num">
								<c:choose>
									<c:when test="${num.count eq 1}">
										<li><a class="current" id="facId_${item.id}" href="#">Factura ${item.numFactura}&nbsp;&nbsp;&nbsp;${item.fechaFactura}</a></li>
									</c:when>
									<c:otherwise>
										<li><a id="facId_${item.id}" href="#">Factura ${item.numFactura}&nbsp;&nbsp;&nbsp;${item.fechaFactura}</a></li>
									</c:otherwise>
								</c:choose>																
							</c:forEach>							
						</ul>
					</div>
					<div style="margin-top: 1em;">						
						<button id="verFactura" id="generarFactura">Ver factura</button>
						<button id="eliminarFactura" style="margin-left: 3em;" id="generarFactura">Eliminar factura</button>
					</div>
	  			</c:if>		
			</div>
			<div style="margin-top: 1.8em; text-align: center;">
				<button id="generarFactura">Generar factura</button>
			</div>
			
			<div id="formulario-facturas" title="Dar de alta nueva factura">		
				<div style="float: left; width: 100%;">
					<div style="float: left; width: 100%;">
						<table id="tablaFactura" class="table table-striped">
				            <thead><tr><th>Concepto</th><th>Coste</th><th>Iva</th><th></th></tr></thead>
					            <tbody>
					              <tr>
					              	<td></td><td>0</td><td>0</td><td><button class="eliminarFactura"></button></td>
					              </tr>				              			              
					            </tbody>
							<tfoot><tr><th><strong>TOTAL</strong></th><th></th><th></th><th></th></tr></tfoot>
				        </table>
					</div>					
					<div style="float: left; width: 100%; margin-top: 1em;">
						<button id="addLineaFactura">A&ntilde;adir L&iacute;nea</button>
					</div>			        
				</div>
			</div>
			
	  	</div>	<!-- fin contenedor general -->
	  	