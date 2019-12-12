package com.soft.eva.service.impl;

import com.soft.eva.domain.OperateRecord;
import com.soft.eva.service.OperateRecordService;
import org.springframework.stereotype.Service;

@Service(value = "operateRecordService")
public class OperateRecordServiceImpl extends BaseServiceImpl<OperateRecord> implements OperateRecordService {
    @Override
    protected Class<OperateRecord> getEntityClass() {
        return OperateRecord.class;
    }
}
