package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Factura;
import com.reyma.gestion.service.FacturaService;

@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

	public long countAllFacturas() {
        return Factura.countFacturas();
    }

	public void deleteFactura(Factura factura) {
        factura.remove();
    }

	public Factura findFactura(Integer id) {
        return Factura.findFactura(id);
    }

	public List<Factura> findAllFacturas() {
        return Factura.findAllFacturas();
    }

	public List<Factura> findFacturaEntries(int firstResult, int maxResults) {
        return Factura.findFacturaEntries(firstResult, maxResults);
    }

	public void saveFactura(Factura factura) {
        factura.persist();
    }

	public Factura updateFactura(Factura factura) {
        return factura.merge();
    }
}
