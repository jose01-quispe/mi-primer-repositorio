package pe.edu.upeu.sysventas.model;

import lombok.Getter;
import lombok.Setter;
import pe.edu.upeu.sysventas.enums.TipoDocumento;
import pe.edu.upeu.sysventas.enums.TipoProducto;
@Getter
@Setter
public class Cliente {
    String dniruc;
    String nombres;
    String redLegal;
    String direccion;
    TipoDocumento tipoDocumento;
}
