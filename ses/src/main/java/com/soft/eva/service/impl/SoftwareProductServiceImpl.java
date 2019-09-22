package com.soft.eva.service.impl;

import com.soft.eva.dao.SoftwareProductDao;
import com.soft.eva.domain.SoftwareProduct;
import com.soft.eva.service.SoftwareProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service(value = "softwareProductService")
public class SoftwareProductServiceImpl implements SoftwareProductService {

    @Autowired
    SoftwareProductDao softwareProductMapper;


    @Override
    public SoftwareProduct get(String number) {
        SoftwareProduct softwareProduct = softwareProductMapper.get(number);
        return softwareProduct;
    }

    @Override
    public List<SoftwareProduct> list(Map<String, Object> map) {
        return softwareProductMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return softwareProductMapper.count(map);
    }

    @Override
    public int save(SoftwareProduct softwareProduct) {
        return softwareProductMapper.save(softwareProduct);
    }

    @Override
    public int update(SoftwareProduct softwareProduct) {
        return softwareProductMapper.update(softwareProduct);
    }

    @Override
    public int remove(String number) {
        return softwareProductMapper.remove(number);
    }

    @Override
    public int batchRemove(String[] numbers) {
        return softwareProductMapper.batchRemove(numbers);
    }
}
