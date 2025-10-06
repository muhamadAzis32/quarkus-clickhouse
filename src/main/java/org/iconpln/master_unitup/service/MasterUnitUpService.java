package org.iconpln.master_unitup.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.enterprise.context.ApplicationScoped;
import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.master_unitup.repository.MasterUnitUpRepository;
import org.iconpln.util.PagedResultDto;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer untuk business logic master_unitupi
 * Menggunakan Lombok
 */
@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class MasterUnitUpService {

    private final MasterUnitUpRepository repository;

    /**
     * Mendapatkan semua data unit UP
     */
    public List<MasterUnitUp> getAllUnits() {
        try {
            log.info("Mengambil semua data unit UP");
            return repository.findAll();
        } catch (SQLException e) {
            log.error("Error saat mengambil semua data", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }


    public PagedResultDto<MasterUnitUp> getAllUnitsPaginated(int page, int size) {
        try {
            log.info("Mengambil data unit UP dengan pagination: page={}, size={}", page, size);

            // Validasi parameter
            if (page < 1) {
                throw new IllegalArgumentException("Page harus >= 1");
            }
            if (size < 1 || size > 100) {
                throw new IllegalArgumentException("Size harus antara 1-100");
            }

            // Get paginated data
            List<MasterUnitUp> units = repository.findAllPaginated(page, size);

            // Get total count
            long totalElements = repository.count();

            // Calculate pagination info
            int totalPages = (int) Math.ceil((double) totalElements / size);

            log.info("Berhasil mengambil {} unit UPI= (page {} of {})", units.size(), page, totalPages);

            return PagedResultDto.<MasterUnitUp>builder()
                    .content(units)
                    .page(page)
                    .size(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .first(page == 1)
                    .last(page >= totalPages)
                    .build();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (SQLException e) {
            log.error("Error saat mengambil data unit UP dengan pagination", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }
}
