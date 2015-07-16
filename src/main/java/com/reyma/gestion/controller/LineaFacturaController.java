package com.reyma.gestion.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.IvaService;
import com.reyma.gestion.service.LineaFacturaService;
import com.reyma.gestion.ui.LineaFacturaDTO;
import com.reyma.gestion.ui.MensajeErrorJson;

import flexjson.JSONSerializer;

@RequestMapping("/lineasfactura")
@Controller
public class LineaFacturaController {

	@Autowired
    LineaFacturaService lineaFacturaService;

	@Autowired
    FacturaService facturaService;

	@Autowired
    IvaService ivaService;


	@RequestMapping(value = "/cargarfac", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String list(@RequestParam(value = "idFactura", required = true) Integer idFactura) {
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError = null;
		if ( idFactura == null ){
			mensajeError = new MensajeErrorJson("Se ha producido un error obteniendo las l&iacute;neas de la factura");
			mensajeError.setTitulo("Error obteniendo factura");
			return serializer.exclude("class").serialize(mensajeError);
		}
		List<LineaFacturaDTO> lineasDto = new ArrayList<LineaFacturaDTO>();
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();
		Converter<LineaFactura, LineaFacturaDTO> converter = acsf.getLineaFacturaToLineaFacturaListadoDTOConverter();
		
		List<LineaFactura> lineas = lineaFacturaService.findLineasFacturaByIdFactura(idFactura);		
		for (LineaFactura linea : lineas) {
			lineasDto.add(converter.convert(linea));
		}		
		return serializer.exclude("*.class").serialize(lineasDto);
    }
	
}
