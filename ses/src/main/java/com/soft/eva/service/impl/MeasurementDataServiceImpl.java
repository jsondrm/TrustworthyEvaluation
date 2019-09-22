package com.soft.eva.service.impl;

import com.soft.eva.domain.MeasurementData;
import com.soft.eva.service.MeasurementDataService;
import org.springframework.stereotype.Service;

@Service(value = "measurementDataService")
public class MeasurementDataServiceImpl extends BaseServiceImpl<MeasurementData> implements MeasurementDataService {

    @Override
    protected Class<MeasurementData> getEntityClass() {
        return MeasurementData.class;
    }
}
