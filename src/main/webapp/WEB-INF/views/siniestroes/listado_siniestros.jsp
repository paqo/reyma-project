<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/plug-ins/1.10.7/integration/jqueryui/dataTables.jqueryui.css">

<script type="text/javascript" src="https://cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/plug-ins/1.10.7/integration/jqueryui/dataTables.jqueryui.js"></script>

<div style="margin-top: 2em; float: left; width: 100%;">
	<table id="listado-siniestros" class="display">
        <thead>
            <tr>           
                <th>Compa&ntilde;ia</th>
                <th>Num. Siniestro</th>
                <th>Direcci&oacute;n</th>
                <th>Afectado(s)</th>
                <th>Fecha</th>
                <th></th> <%-- icono de ir a edicion --%>
            </tr>
        </thead>        
    </table>
</div>



<script type="text/javascript">

$(function() {
	
	// ordenacion personalizada para columna fecha
	crearOrdenacionFechasDT();
	// internacionalizacion
	var i18n = obtenerI18NParaDT();		
	  		    
	$('#listado-siniestros').dataTable({
	   	"oLanguage": i18n,
	   	"sAjaxSource": "/reymasur/siniestroes?listado",
	   	"sServerMethod": "POST",
	   	"bDeferRender": true,
	   	"aoColumns": [	    	            
	   	            { "mData": "compania", "bSortable": true },
	   	            { "mData": "numeroSiniestro", "bSortable": true },
	   	            { "mData": "direccion", "bSortable": true },
	   	            { "mData": "afectado", "bSortable": true },
	   	            { "mData": "fecha", "sType": "fecha", "bSortable": true },
	   	            { "mData": "id", "bSortable": false, "bSearchable": false,
	   	            				 "mRender": function ( data, type, full ) {							
							    	    			return '<a href="/reymasur/siniestroes/' + data +'?form">' + 
							    	    						'<img src="/reymasur/images/update.png" alt="editar" />' + 
							    	    				   '</a>';
							    	    		}
					}
	   	        ]	    	
	});	    
	
});
  
</script>