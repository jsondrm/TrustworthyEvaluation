package com.soft.eva.service;

import com.soft.eva.domain.SoftwareProduct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SoftwareProductService {
    SoftwareProduct get(String number);

    List<SoftwareProduct> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SoftwareProduct softwareProduct);

    int update(SoftwareProduct softwareProduct);

    int remove(String number);

    int batchRemove(String[] numbers);
}
