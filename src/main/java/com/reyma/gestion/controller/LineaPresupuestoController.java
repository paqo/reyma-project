package com.reyma.gestion.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.service.IvaService;
import com.reyma.gestion.service.LineaPresupuestoService;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.PresupuestoService;
import com.reyma.gestion.ui.LineaPresupuestoDTO;
import com.reyma.gestion.ui.MensajeErrorJson;
import com.reyma.gestion.ui.PresupuestoDTO;

import flexjson.JSONSerializer;

@RequestMapping("/lineaspresupuesto")
@Controller
public class LineaPresupuestoController {

	@Autowired
    LineaPresupuestoService lineaPresupuestoService;

	@Autowired
	PresupuestoService presupuestoService;

	@Autowired
    IvaService ivaService;
	
	@Autowired
	OficioService oficioService;

	@RequestMapping(value = "/cargarpres", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String list(@RequestParam(value = "idPres", required = true) Integer idPresupuesto, Model uiModel) {
		
		cargarCombos(uiModel);
		
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError = null;
		if ( idPresupuesto == null ){
			mensajeError = new MensajeErrorJson("Se ha producido un error obteniendo las l&iacute;neas del presupuesto");
			mensajeError.setTitulo("Error obteniendo presupuesto");
			return serializer.exclude("class").serialize(mensajeError);
		}
		List<LineaPresupuestoDTO> lineasDto = new ArrayList<LineaPresupuestoDTO>();
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();
		Converter<LineaPresupuesto, LineaPresupuestoDTO> converter = acsf.getLineaPresupuestoToLineaPresupuestoListadoDTOConverter();		
		
		List<LineaPresupuesto> lineas = lineaPresupuestoService.findLineasPresupuestoByIdPresupuesto(idPresupuesto);
		
		PresupuestoDTO res = new PresupuestoDTO();
		if ( lineas.size() > 0 ){
			Presupuesto pres = lineas.get(0).getLinPresId();
			for (LineaPresupuesto linea : lineas) {
				lineasDto.add(converter.convert(linea));
			}
			res.setFechaPresupuesto( pres.getPresFecha() );
			res.setLineasPresupuesto( lineasDto.toArray(new LineaPresupuestoDTO[0]));
		}		
		 
		return serializer.exclude("*.class").serialize(res);
		
		//return serializer.exclude("*.class").serialize(lineasDto);
    }
	
	private void cargarCombos(Model uiModel) {		
		uiModel.addAttribute("oficios", oficioService.findAllOficios() );
	}
	
}
