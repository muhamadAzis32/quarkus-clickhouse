package org.iconpln.mon_janji_bayar.service;

import org.iconpln.mon_janji_bayar.entity.MonJanjiBayar;
import org.iconpln.util.PagedResultDto;

public interface MonJanjiBayarService {
    PagedResultDto<MonJanjiBayar> getAll(int page, int size);
}
