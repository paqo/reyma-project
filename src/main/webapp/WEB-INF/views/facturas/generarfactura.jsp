<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
		
		.titulo-factura {
			font-size: 14px;
			width: 100%;
			float: left;
			margin-bottom: 4px;
		}
		
		div.espaciador10px {
			width: 100%;
			float: left;
			margin-top: 5px;
			margin-bottom: 5px;
		}
		
		.tabla-lin-fac {
			border-collapse: separate; width: 100%;
		}
		
		.fila-cabecera {
			height: 14px; font-size: 12px;
		}
						
		.cabecera1-txt, .cabecera2-txt, .cabecera3-txt, .cabecera4-txt {			
			direction: ltr;	font-weight: bold; color: white;	
		}
		
		.cabecera-desc {
			text-align: left; width: 55%; background-color: #20A627;
		}
		
		.cabecera-prec, .cabecera-iva, .cabecera-sub {
			text-align: center;	
			width: 15%;
			background-color: #20A627;
		}
		
		.tipo-oficio {
			background-color: #75E57B;
			font-size: 11px;
			font-weight: bold;
		}
		
		.datos-fac {
			font-size:10px; width: 100%; float: left;
		}
		
		.dato-lin-desc {
			text-align: left;
			font-size: 11px;
			font-weight: lighter !important;
		}
		
		.dato-lin-prec, .dato-lin-iva, .dato-lin-sub {
			text-align: center;
			font-size: 11px;
			font-weight: lighter !important;
		}
		
	</style>
	
	<title>Factura ${factura.numFactura}</title>
</head>
<body>

<%-- color de fondo cabecera: #61A023 --%>

<div style="width: 100%; float: left; height: 5em; background-color: #00FF40; margin-bottom: 15px;">
	<img alt="LOGO" src="http://localhost:8080/reymasur/resources/images/logo.png" />
</div>

<div style="width: 100%; float: left;"> <%-- cont principal --%>

	<div style="width: 100%;"> <%-- cont datos personales --%>
		<div style="float: left; width: 65%;">
			<div class="tituloFactura">FACTURA</div>
			<div class="datos-fac">Fecha factura: ${factura.fechaEncargo}</div>	
			<div class="datos-fac">Fecha finalizaci&#243;n: ${factura.fechaFin}</div>
			<div class="datos-fac">N&#250;m. encargo: ${factura.numEncargo}</div>
			<div class="datos-fac">N&#250;m. factura: ${factura.numFactura}</div>		
		</div>
		<div style="float: right; width: 30%; font-size:10px; padding:3px; border: 1px solid black;">
			${factura.nombre}<br/>
			${factura.domicilio}<br/>
			CP: ${factura.cp}<br/>
			NIF: ${factura.nif}<br/>
		</div>
	</div> 	<%-- fin cont datos personales --%>
		
	<div class="espaciador10px">&#160;</div>
	
	<div style="width: 100%; float: left; min-height: 700px;"> <%-- lineas de factura --%>
		<div style="width: 1%; float: left;">&#160;</div>
		<div style="width: 98%; float: left;">
			<table class="tabla-lin-fac">
				<tbody>
					<tr class="fila-cabecera">
						<td class="cabecera-desc">
							<span class="cabecera1-txt">DESCRIPCION</span>
						</td>
						<td class="cabecera-prec">
							<span class="cabecera2-txt">PRECIO</span>
						</td>
						<td class="cabecera-iva">
							<span class="cabecera3-txt">IVA</span>
						</td>
						<td class="cabecera-sub">
							<span class="cabecera4-txt">SUBTOTAL</span>
						</td>
					</tr>
					<%-- lineas --%>
					<c:forEach var="ofi" items="${factura.lineasFactura}">
						<tr>
							<td class="tipo-oficio" colspan="4">${ofi.key}</td>
						</tr>
						<c:forEach var="linea" items="${ofi.value}">
							<tr>
								<td class="dato-lin-desc">${linea.linConcepto}</td>
								<td class="dato-lin-prec">${linea.linImporte}</td>
								<td class="dato-lin-iva">${linea.linIvaId.ivaValor}</td>
								<td class="dato-lin-sub">${linea.linImporte + (linea.linIvaId.ivaValor * linea.linImporte)/100}</td>
							</tr>
						</c:forEach>					    
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div style="width: 1%; float: left;">&#160;</div>		
	</div> <%-- fin lineas de factura --%>
	
	<div style="width: 100%; float: left; height:100px; background-color: #FAFCF7;">
		${factura.nombreR}
	</div>

</div>


<%--
	<fmt:formatDate type="date" value="${factura.facFecha.time}" />
 --%>


</body>
</html>