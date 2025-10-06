package org.iconpln.mon_janji_bayar.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.mon_janji_bayar.entity.MonJanjiBayar;
import org.iconpln.mon_janji_bayar.repository.MonJanjiBayarRepository;
import org.iconpln.util.PagedResultDto;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@ApplicationScoped
public class MonJanjiBayarServiceImpl implements MonJanjiBayarService {

    @Inject
    MonJanjiBayarRepository repository;

    @Override
    public PagedResultDto<MonJanjiBayar> getAll(int page, int size)  {
        try {
            // Validasi parameter
            if (page < 1) {
                throw new IllegalArgumentException("Page harus >= 1");
            }
            if (size < 1 || size > 100) {
                throw new IllegalArgumentException("Size harus antara 1-100");
            }

            // Get paginated data
            List<MonJanjiBayar> units = repository.findAll(page, size);

            // Get total count
            long totalElements = repository.count();

            // Calculate pagination info
            int totalPages = (int) Math.ceil((double) totalElements / size);

            log.info("Berhasil mengambil {} mon janji bayar = (page {} of {})", units.size(), page, totalPages);

            return PagedResultDto.<MonJanjiBayar>builder()
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
            log.error("Error saat mengambil data mon janji bayar dengan pagination", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }
}
