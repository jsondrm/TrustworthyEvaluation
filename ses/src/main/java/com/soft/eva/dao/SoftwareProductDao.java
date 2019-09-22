package com.soft.eva.dao;

import com.soft.eva.domain.SoftwareProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SoftwareProductDao {
    SoftwareProduct get(String number);

    List<SoftwareProduct> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SoftwareProduct softwareProduct);

    int update(SoftwareProduct softwareProduct);

    int remove(String number);

    int batchRemove(String[] numbers);
}
