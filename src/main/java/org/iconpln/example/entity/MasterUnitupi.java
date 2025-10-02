package org.iconpln.example.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Entity untuk table master_unitupi di ClickHouse
 *
 * Table Schema:
 * CREATE TABLE IF NOT EXISTS master_unitupi (
 *     unitupi String,
 *     alamat String,
 *     kota String,
 *     manager String,
 *     nama String,
 *     nama_singkat String,
 *     satuan String,
 *     telepon String,
 *     __ts_ms UInt64 DEFAULT 0,
 *     deleted UInt8 DEFAULT 0
 * ) ENGINE = ReplacingMergeTree(__ts_ms)
 * ORDER BY unitupi;
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
public class MasterUnitupi {

    /**
     * Kode unit UPI (Primary Key)
     */
    private String unitupi;

    /**
     * Alamat lengkap unit
     */
    private String alamat;

    /**
     * Nama kota
     */
    private String kota;

    /**
     * Nama manager yang bertanggung jawab
     */
    private String manager;

    /**
     * Nama lengkap unit
     */
    private String nama;

    /**
     * Nama singkat unit
     * Mapping ke kolom nama_singkat di database
     */
    @JsonProperty("nama_singkat")
    private String namaSingkat;

    /**
     * Satuan unit
     */
    private String satuan;

    /**
     * Nomor telepon
     */
    private String telepon;

    /**
     * Timestamp untuk ReplacingMergeTree
     * Mapping ke kolom __ts_ms di database
     */
    @JsonProperty("__ts_ms")
    @Builder.Default
    private Long tsMs = System.currentTimeMillis();

    /**
     * Flag soft delete
     * 0 = active, 1 = deleted
     */
    @JsonProperty("__deleted")
    @Builder.Default
    private Integer deleted = 0;

}