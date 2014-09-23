package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.service.LineaFacturaService;

@Service
@Transactional
public class LineaFacturaServiceImpl implements LineaFacturaService {

	public long countAllLineaFacturas() {
        return LineaFactura.countLineaFacturas();
    }

	public void deleteLineaFactura(LineaFactura lineaFactura) {
        lineaFactura.remove();
    }

	public LineaFactura findLineaFactura(Integer id) {
        return LineaFactura.findLineaFactura(id);
    }

	public List<LineaFactura> findAllLineaFacturas() {
        return LineaFactura.findAllLineaFacturas();
    }

	public List<LineaFactura> findLineaFacturaEntries(int firstResult, int maxResults) {
        return LineaFactura.findLineaFacturaEntries(firstResult, maxResults);
    }

	public void saveLineaFactura(LineaFactura lineaFactura) {
        lineaFactura.persist();
    }

	public LineaFactura updateLineaFactura(LineaFactura lineaFactura) {
        return lineaFactura.merge();
    }
}
