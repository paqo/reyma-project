<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="reyma" uri="/WEB-INF/tags/reymasur/reymasur.tld"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>	
	<style type="text/css">
		
		/*		
		Para embeber una fuente:
		
		(poner la fuente en otra ruta)
		
		@font-face {
	        font-family: "Arial";
	        src: url("http://localhost:8080/reymasur/resources/images/win-arial.ttf");
	        -fs-pdf-font-embed: embed;
	        -fs-pdf-font-encoding: Identity-H; 
		}*/		
		
		* {
			font-family: sans-serif;			
		}
		
		.div-titulo-logo {
			float: left; width: auto; padding-top: 30px;
		}
		
		.div-img-logo {
			float: left; width: 70px; padding-left: 10px; padding-top: 18px;
		}
		
		.titulo-factura {
			font-size: 14px;
			width: 100%;
			float: left;
		}
		
		.titulo-logo {
			font-size: 18px; font-weight: bold; color: white;
		}
		
		div.espaciador10px, div.espaciador {
			width: 100%;
			float: left;			
		}
		
		div.espaciador10px {
			margin-top: 5px;
			margin-bottom: 5px;
		}
		
		div.espaciador {
			margin-bottom: 3px;
		}
		
		.tabla-lin-fac {
			border-collapse: separate; width: 100%;
		}
		
		.fila-cabecera {
			height: 14px; font-size: 12px;
		}
						
		.cabecera1-txt, .cabecera2-txt, .cabecera3-txt, .cabecera4-txt, .cabecera5-txt {			
			direction: ltr;	font-weight: bold; color: white;	
		}
		
		.cabecera-desc {
			text-align: left; width: 55%; background-color: #20A627;
		}
		
		.cabecera-prec, .cabecera-iva, .cabecera-base, .cabecera-sub {
			text-align: center;	
			width: 10%;
			background-color: #20A627;
		}
		
		.cabecera-porc {
			text-align: center;	
			width: 5%;
			background-color: #20A627;
		}
		
		.tipo-oficio {
			background-color: #75E57B;
			font-size: 12px;
			font-weight: bold;
		}
		
		.datos-fac {
			font-size:11px; width: 100%; float: left;
		}
		
		.dato-lin-desc {
			text-align: left;
			font-size: 12px;
			font-weight: lighter !important;
		}
		
		.dato-lin-prec, .dato-lin-iva, .dato-lin-porc, .dato-lin-sub {
			text-align: center;
			font-size: 12px;
			font-weight: lighter !important;
		}
		
		.info-nombre, .info-cif, .info-fecha-hoy, .info-tlf, .info-domicilio {
			font-size: 13px;
		}
		
		.info-fecha-hoy {
			padding-top: 100px;
		}
		
		.pie {
			float: left; 
			width: 100%; 
			font-size: 11px; 
			font-weight: bold; 
			text-align: center; 
			color: white; 
			background-color: #69A322; 
			padding: 2px;
			margin-top: 10px;
		}
		
	</style>
	
	<title>Factura ${factura.numFactura}</title>
</head>
<body style="margin-top: 2px;">

<%-- color de fondo cabecera: #69A322 --%>

<div style="width: 100%; float: left; height: 5em; background-color: #69A322; margin-bottom: 15px;">
	<div class="div-img-logo">
		<img alt="LOGO" src="${servidor}/reymasur/resources/images/logo.png" /> 
		<!-- <img alt="LOGO" src="http://test-reymasur.rhcloud.com/reymasur/resources/images/logo.png" /> --> 
	</div>	
	<div class="div-titulo-logo">
		<span class="titulo-logo">REYMASUR</span>
	</div>
</div>

<div style="width: 100%; float: left;"> <%-- cont principal --%>

	<div style="width: 100%;"> <%-- cont datos personales --%>
		<div style="float: left; width: 65%;">
			<c:choose>
				<c:when test="${presupuesto eq true}">
					<div class="tituloFactura">PRESUPUESTO</div>
					<div class="espaciador">&#160;</div>   
					<div class="datos-fac">N&#250;m. encargo: ${factura.numEncargo}</div>
					<div class="datos-fac">&#160;</div>
				</c:when>
				<c:otherwise>
					<div class="tituloFactura">FACTURA</div>
					<div class="espaciador">&#160;</div>   
					<div class="datos-fac">N&#250;m. encargo: ${factura.numEncargo}</div>
					<div class="datos-fac">N&#250;m. factura: ${factura.numFactura}</div>
				</c:otherwise>
			</c:choose>			
					
		</div>
		<div style="float: right; width: 30%; font-size:11px; padding:3px; border: 1px solid black;">
			${factura.nombre}<br/>
			${factura.domicilio}<br/>
			CP: ${factura.cp}<br/>
			NIF: ${factura.nif}<br/>
		</div>
	</div> 	<%-- fin cont datos personales --%>
		
	<div class="espaciador10px">&#160;</div>
	
	<div style="width: 100%; float: left; min-height: 585px;"> <%-- lineas de factura --%>
		<div style="width: 1%; float: left;">&#160;</div>
		<div style="width: 98%; float: left;">
			<table class="tabla-lin-fac">
				<tbody>
					<tr class="fila-cabecera">
						<td class="cabecera-desc">
							<span class="cabecera1-txt">DESCRIPCION</span>
						</td>
						<td class="cabecera-base">
							<span class="cabecera2-txt">BASE IMP.</span>
						</td>
						<td class="cabecera-porc">
							<span class="cabecera3-txt">%</span>
						</td>
						<td class="cabecera-iva">
							<span class="cabecera4-txt">IVA</span>
						</td>
						<td class="cabecera-sub">
							<span class="cabecera5-txt">SUBTOTAL</span>
						</td>
					</tr>
					<%-- lineas --%>
					<c:set var="totalFactura" value="0" />
					<c:forEach var="ofi" items="${factura.lineasFactura}" varStatus="contador">
						<tr>
							<td class="tipo-oficio" colspan="5">${ofi.key}</td>
						</tr>	
												
						<c:forEach var="linea" items="${ofi.value}">
							<tr>
								<td class="dato-lin-desc">${linea.linConcepto}</td>																							
								<c:choose>
									<c:when test="${presupuesto eq true}">																				
										<c:choose>											
											<c:when test="${gremiosConCero eq true}">
												<td class="dato-lin-prec"></td>
												<td class="dato-lin-porc"></td>												
												<td class="dato-lin-iva"></td>												
												<td class="dato-lin-sub"></td>
											</c:when>
											<c:otherwise>
												<td class="dato-lin-prec">${linea.linImporte}</td>
												<td class="dato-lin-porc">${linea.linIvaId.ivaValor}%</td>
												<c:set var="porcaplicado" value="${(linea.linIvaId.ivaValor * linea.linImporte)/100}" />
												<td class="dato-lin-iva">${porcaplicado}</td>												
												<%-- <c:set var="subototal" value="${linea.linImporte + (linea.linIvaId.ivaValor * linea.linImporte)/100}" /> --%>
												<c:set var="subototal"><reyma:getSubTotal linea="${linea}" /></c:set>
												<td class="dato-lin-sub">${subototal}</td>
												<c:set var="totalFactura" value="${totalFactura + subototal}" />
											</c:otherwise>
										</c:choose>										
									</c:when>
									<c:otherwise>
										<td class="dato-lin-prec">${linea.linImporte}</td>
										<td class="dato-lin-porc">${linea.linIvaId.ivaValor}%</td>
										<c:set var="porcaplicado" value="${(linea.linIvaId.ivaValor * linea.linImporte)/100}" />
										<td class="dato-lin-iva">${porcaplicado}</td>
										<c:set var="subototal"><reyma:getSubTotal linea="${linea}" /></c:set>
										<td class="dato-lin-sub">${subototal}</td>
										<c:set var="totalFactura" value="${totalFactura + subototal}" />										
									</c:otherwise>									
								</c:choose>								
							</tr>
						</c:forEach>	
						<c:if test="${gremiosConCero eq true}">
							<tr>
								<td class="dato-lin-desc">${subtotales[contador.index][0]}</td>
								<td class="dato-lin-prec">${subtotales[contador.index][1]}</td>
								<td class="dato-lin-porc">${subtotales[contador.index][2]}</td>												
								<td class="dato-lin-iva">${subtotales[contador.index][3]}</td>												
								<td class="dato-lin-sub">${subtotales[contador.index][4]}</td>
							</tr>
						</c:if>				    
					</c:forEach>
					<%-- fin lineas --%>
					<%-- Linea de totales --%>
					<tr style="border-collapse: collapse;">
						<td colspan="4" class="tipo-oficio">
							<div style="width:100%; text-align: right;">TOTAL:&#160;</div>
						</td>						
						<c:choose>
							<%-- caso especial cuando es presupuesto con importes a 0 --%>
							<c:when test="${gremiosConCero eq true}">
								<td class="tipo-oficio" style="text-align: center;">
									<fmt:formatNumber value="${totalGremios}" pattern="#.00" />
								</td>
							</c:when>
							<c:otherwise>
								<td class="tipo-oficio" style="text-align: center;">${totalFactura}</td>
							</c:otherwise>
						</c:choose>						
					</tr>
					<%-- Fin linea de totales --%>
				</tbody>
			</table>
		</div>
		<div style="width: 1%; float: left;">&#160;</div>		
	</div> <%-- fin lineas de factura --%>
	
	<jsp:useBean class="java.util.Date" id="hoy" scope="page" />
	
	<div style="width: 100%; float: left; height:150px; background-color: #EDEFEA;">
		<div style="width: 40%; float: left; padding: 15px;">
			<div class="info-fecha-hoy">FECHA:&#160;${factura.fechaFactura}</div>	
		</div>
		<div style="width: 40%; float: right; padding: 15px;">
			<div class="info-nombre">${factura.nombreR}</div>
			<div class="info-domicilio">${factura.domicilioR}</div>
			<div class="info-domicilio">CP:&#160;${factura.cpR}, ${factura.localidadR}</div>			
			<div class="info-cif">CIF:&#160;${factura.nifR}</div>			
			<div class="info-tlf">TLF:&#160;${factura.telefonoR},&#160;FAX:&#160;${factura.faxR}</div>
		</div>		
	</div>
	
	<div class="pie">
		<span style="margin-left: 5px; margin-right: 5px;">${factura.nombreCortoR}</span>&#8226;
		<span style="font-size: 10px; margin-left: 5px; margin-right: 5px;">${factura.emailR}</span>&#8226;
		<span style="margin-left: 5px; margin-right: 5px;">${factura.urlR}</span>
	</div>
	
</div>

</body>
</html>