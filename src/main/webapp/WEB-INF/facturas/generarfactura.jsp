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
		div, span {			
			text-decoration: none;
			font-family: verdana;
		}
		
		span.titulo-factura {
			font-size: 14px;
		}
		
		div.espaciador10px {
			width: 100%;
			float: left;
			margin-top: 5px;
			margin-bottom: 5px;
		}
		
		.tabla-lin-fac {
			border-collapse: separate;
			width: 100%;
		}
		
		.fila-cabecera {
			height: 14px;
			font-size: 12px;
		}
						
		.cabecera1-txt, .cabecera2-txt, .cabecera3-txt, .cabecera4-txt {			
			direction: ltr;		
			font-weight: bold;
			color: white;	
		}
		
		.cabecera-desc {
			text-align: left;	
			width: 55%;
			background-color: #20A627;		
		}
		
		.cabecera-prec, .cabecera-iva, .cabecera-sub {
			text-align: center;	
			width: 15%;
			background-color: #20A627;
		}
		
	</style>
	
	<title>Factura ${factura.facNumFactura}</title>
</head>
<body>

<div style="width: 100%; float: left; height: 5em; background-color: #61A023; margin-bottom: 15px;">
	<span style="font-weight: bold; color: white;">LOGO</span>
</div>

<div style="width: 100%; float: left;"> <%-- cont principal --%>

	<div style="width: 100%;"> <%-- cont datos personales --%>
		<div style="float: left; width: 65%;">
			<span class="tituloFactura">FACTURA</span>
		</div>
		<div style="float: right; font-size:10px; padding:3px; border: 1px solid black; width: 35%;">
			MARIA MERCEDES COLCHERO GONZ&#193;LEZ<br/>
			CALLE ALFONSO XII, 93<br/>
			CP 41920, SAN JUAN DE AZNALFARACHE, SEVILLA<br/>
			NIF: 46778922P<br/>
		</div>
	</div> 	<%-- fin cont datos personales --%>
	
	<div class="espaciador10px">&#160;</div>
	
	<div style="width: 100%; float: left;"> <%-- lineas de factura --%>
		<div style="width: 2%; float: left;">&#160;</div>
		<div style="width: 96%; float: left;">
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
				</tbody>
			</table>
		</div>
		<div style="width: 2%; float: left;">&#160;</div>		
	</div> <%-- fin lineas de factura --%>

</div>



<div style="width: 100%; float: left;">Fecha de la factura: <fmt:formatDate type="date" value="${factura.facFecha.time}" /></div>



</body>
</html>