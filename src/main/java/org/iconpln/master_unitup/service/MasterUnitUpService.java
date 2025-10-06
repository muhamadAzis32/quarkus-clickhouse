package org.iconpln.master_unitup.service;

import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.util.PagedResultDto;

public interface MasterUnitUpService {

    PagedResultDto<MasterUnitUp> getAll(int page, int size);

}
