package org.iconpln.master_unitap.service;

import org.iconpln.master_unitap.entity.MasterUnitAp;
import org.iconpln.util.PagedResultDto;

public interface MasterUnitApService {
    PagedResultDto<MasterUnitAp> getAll(int page, int size);
}
