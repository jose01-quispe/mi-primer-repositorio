package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.model.Venta;
import pe.edu.upeu.sysventas.model.VentaDetalle;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.repository.VentaRepository;
import pe.edu.upeu.sysventas.service.IVentaService;

@RequiredArgsConstructor
public class VentaServiceImp extends CrudGenericoServiceImp<Venta, Long> implements IVentaService {
    private final VentaRepository ventaRepository;

    @Override
    protected ICrudGenericoRepository<Venta, Long> getRepo() {
        return ventaRepository;
    }

    @Override
    public Venta save(Venta venta) {
        if (venta.getDetalles() != null) {
            for (VentaDetalle detalle : venta.getDetalles()) {
                detalle.setIdDetalle(ventaRepository.siguienteIdDetalle());
            }
        }
        return ventaRepository.save(venta);
    }
}
