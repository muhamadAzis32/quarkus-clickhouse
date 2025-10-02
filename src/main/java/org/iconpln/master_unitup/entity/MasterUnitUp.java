package org.iconpln.master_unitup.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Entity untuk table master_unitup di ClickHouse
 *
 * Lombok Annotations:
 * @Data - Generate getters, setters, toString, equals, hashCode
 * @Builder - Enable builder pattern
 * @NoArgsConstructor - Constructor tanpa parameter (untuk Jackson)
 * @AllArgsConstructor - Constructor dengan semua parameter
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterUnitUp {

    /**
     * Kode  unitup (Primary Key)
     */
    private String unitup;

    private String alamat;
    private String kota;
    private String manager;
    private String nama;

    @JsonProperty("nama_singkat")
    private String namaSingkat;

    private String satuan;
    private String telepon;
    private String unitap;

    @JsonProperty("__deleted")
    @Builder.Default
    private Integer deleted = 0;
}
