package com.soft.eva.service.impl;

import com.soft.eva.domain.SoftData;
import com.soft.eva.service.SoftDataService;
import org.springframework.stereotype.Service;

@Service(value = "softDataService")
public class SoftDataServiceImpl extends BaseServiceImpl<SoftData> implements SoftDataService {
    @Override
    protected Class<SoftData> getEntityClass() {
        return SoftData.class;
    }

}
