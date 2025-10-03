package org.iconpln.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic DTO untuk Pagination Response
 *
 * Lombok Annotations:
 * @Data - Generate getters, setters, toString, equals, hashCode
 * @Builder - Enable builder pattern
 * @NoArgsConstructor - Constructor tanpa parameter
 * @AllArgsConstructor - Constructor dengan semua parameter
 *
 * @param <T> Type of content
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResult<T> {

    /**
     * List of data untuk halaman ini
     */
    private List<T> content;

    /**
     * Nomor halaman saat ini (mulai dari 1)
     */
    private int page;

    /**
     * Jumlah data per halaman
     */
    private int size;

    /**
     * Total semua data (tidak hanya halaman ini)
     */
    private long totalElements;

    /**
     * Total jumlah halaman
     */
    private int totalPages;

    /**
     * Apakah ini halaman pertama
     */
    private boolean first;

    /**
     * Apakah ini halaman terakhir
     */
    private boolean last;

    /**
     * Jumlah data di halaman ini
     */
    public int getNumberOfElements() {
        return content != null ? content.size() : 0;
    }

    /**
     * Apakah ada data di halaman ini
     */
    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }

    /**
     * Apakah masih ada halaman berikutnya
     */
    public boolean hasNext() {
        return page < totalPages;
    }

    /**
     * Apakah ada halaman sebelumnya
     */
    public boolean hasPrevious() {
        return page > 1;
    }
}
