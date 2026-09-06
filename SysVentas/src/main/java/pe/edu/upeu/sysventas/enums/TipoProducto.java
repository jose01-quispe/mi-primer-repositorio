package pe.edu.upeu.sysventas.enums;

import lombok.Getter;

@Getter

public enum TipoProducto {
    PRODUCTO("producto"),
    PREPARADO("preparado"),
    SERVICIO("servicio");
    String descripcion;
    TipoProducto(String descripcion){
        this.descripcion=descripcion;
    }
}
