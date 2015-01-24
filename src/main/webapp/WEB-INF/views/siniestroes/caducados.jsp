<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="reymasur" tagdir="/WEB-INF/tags/form/fields"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<reymasur:tabla data="${caducados}" id="l_com_reyma_gestion_dao_Siniestro" path="/siniestroes" delete="false" 
				mostrarOrdenacion="false" mostrarPie="false" typeIdFieldName="id" z="">	
    <reymasur:column id="label_com_reyma_gestion_dto_numerosiniestro" property="numeroSiniestro" z=""/>
	<reymasur:column id="label_com_reyma_gestion_dto_fecha" property="fecha" z=""/>
	<reymasur:column id="label_com_reyma_gestion_dto_direccion" property="domicilio" z=""/>
	<reymasur:column id="label_com_reyma_gestion_dto_afectado" property="asegurado" z=""/>
	<reymasur:column id="label_com_reyma_gestion_dto_dias" property="dias" z=""/>
</reymasur:tabla>

<div style="margin-top: 2em; float: left; width: 100%;">
	<div style="float: left; width: 15em">
		Cambiar caducidad a 
		<c:choose>
			<c:when test="${not empty param.caducados}">
				<input type="text" id="ndias" value="${param.caducados}" readonly="readonly" class="diasCaducidad">
			</c:when>
			<c:otherwise>
				<input type="text" id="ndias" value="20" readonly="readonly" class="diasCaducidad">
			</c:otherwise>
		</c:choose>d&iacute;as
	</div>
	<div style="float: left; width: 43em; margin-top: 0.4em;" id="rango-dias"></div>
</div>

<div style="margin-top: 2em;">
	<button id="btnActualizar">Cambiar</button>
</div>
<div style="margin-top: 1em;" id="rango-dias"></div>

<script type="text/javascript">
  $(function() {
    $( "#rango-dias" ).slider({
      min: 0,
      max: 180,
      slide: function( event, ui ) {
        $( "#ndias" ).val( ui.value );
      }
    });
    // inicialmente a 20
    $( "#rango-dias" ).slider( "value", $( "#ndias" ).val() );
  });
  	$( "#btnActualizar" ).button({		    	
       icons: {	 	        	
           primary: "ui-icon-refresh"
       }
	}).click(function(event) {
		event.preventDefault();
		document.location = "/reymasur/siniestroes?caducados=" + $( "#rango-dias" ).slider( "value");	  	
	});	
  </script>