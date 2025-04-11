package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Clase base para entidades que requieren auditoría.
 * <p>
 * Esta clase abstracta proporciona campos para el seguimiento de la creación y la última modificación de una entidad,
 * así como información sobre el usuario que realizó estas operaciones.
 * Además, incluye campos para el borrado lógico.
 * </p>
 *
 * <ul>
 *     <li>{@code createdDate}: Fecha y hora en que se creó la entidad.</li>
 *     <li>{@code createdBy}: Usuario que creó la entidad.</li>
 *     <li>{@code modifiedDate}: Fecha y hora de la última modificación.</li>
 *     <li>{@code modifiedBy}: Usuario que realizó la última modificación.</li>
 *     <li>{@code eliminado}: Indicador de borrado lógico (true si la entidad ha sido eliminada).</li>
 *     <li>{@code eliminadoDate}: Fecha y hora en que se realizó el borrado lógico.</li>
 *     <li>{@code eliminadoBy}: Usuario que realizó el borrado lógico.</li>
 * </ul>
 *
 * <p>
 * Las anotaciones de Spring Data (como {@code @CreatedDate} y {@code @LastModifiedDate}) se encargan de poblar
 * estos campos automáticamente durante el ciclo de vida de la entidad.
 * </p>
 */
@Data
public abstract class AuditableEntity {

    /**
     * Fecha y hora en que se creó la entidad.
     */
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * Usuario que creó la entidad.
     */
    @CreatedBy
    private String createdBy;

    /**
     * Fecha y hora de la última modificación de la entidad.
     */
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    /**
     * Usuario que realizó la última modificación de la entidad.
     */
    @LastModifiedBy
    private String modifiedBy;

    /**
     * Indicador de borrado lógico. Si es {@code true}, la entidad se considera eliminada.
     */
    private boolean deleted;

    /**
     * Fecha y hora en que se realizó el borrado lógico de la entidad.
     */
    private LocalDateTime deletedDate;

    /**
     * Usuario que realizó el borrado lógico de la entidad.
     */
    private String deletedBy;
}

