package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_documento")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("2")
public class TipoDocumento extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    public TipoDocumento() {
    }
}
