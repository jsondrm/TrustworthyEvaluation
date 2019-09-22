package com.soft.eva.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.soft.eva.dto.BaseSearchCondition;
import com.soft.eva.dto.Page;
import com.soft.eva.service.BaseService;
import com.soft.eva.util.EmptyUtil;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang JunJie
 * @date 2018/6/26.
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    /**
     * 获取具体的类类型
     *
     * @return 返回类类型
     */
    protected abstract Class<T> getEntityClass();

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public Object save(T entity) {
        mongoTemplate.save(entity);
        return Reflect.on(entity).field(ID_FIELD_NAME).get();
    }

    @Override
    public Object save(T entity, String collectionName) {
        mongoTemplate.save(entity, collectionName);
        return Reflect.on(entity).field(ID_FIELD_NAME).get();
    }

    @Override
    public void save(List<T> entityList) {
        mongoTemplate.insert(entityList, getEntityClass());
    }

    @Override
    public void save(List<T> entityList, String collectionName) {
        mongoTemplate.insert(entityList, collectionName);
    }

    @Override
    public void upsert(T entity) {
        Object softwareBelongsToValue = Reflect.on(entity).field(SOFTWAREBELONGSTO_FIELD_NAME).get();
        Object nameValue = Reflect.on(entity).field(NAME_FIELD_NAME).get();
        Object typeValue = Reflect.on(entity).field(TYPE_FIELD_NAME).get();
        Object phaseBelongsToValue = Reflect.on(entity).field(PHASEBELONGSTO_FIELD_NAME).get();

        Update update = createUpdate(entity);
        mongoTemplate.upsert(new Query().addCriteria(Criteria.where("softwareBelongsTo").is(softwareBelongsToValue).and("name").is(nameValue).and("type").is(typeValue).and("phaseBelongsTo").is(phaseBelongsToValue)), update,
                getEntityClass());
    }

    @Override
    public void upsert(List<T> entityList) {
        for(int i = 0; i < entityList.size(); i++){
            T entity = entityList.get(i);
//            Object idValue = Reflect.on(entity).field(ID_FIELD_NAME).get();
            Object softwareBelongsToValue = Reflect.on(entity).field(SOFTWAREBELONGSTO_FIELD_NAME).get();
            Object nameValue = Reflect.on(entity).field(NAME_FIELD_NAME).get();
            Object typeValue = Reflect.on(entity).field(TYPE_FIELD_NAME).get();
            Object phaseBelongsToValue = Reflect.on(entity).field(PHASEBELONGSTO_FIELD_NAME).get();

            Update update = createUpdate(entity);
            mongoTemplate.upsert(new Query().addCriteria(Criteria.where("softwareBelongsTo").is(softwareBelongsToValue).and("name").is(nameValue).and("type").is(typeValue).and("phaseBelongsTo").is(phaseBelongsToValue)), update,
                    getEntityClass());
        }
    }

    @Override
    public UpdateResult update(T entity) {
        Object idValue = Reflect.on(entity).field(ID_FIELD_NAME).get();
        Update update = createUpdate(entity);
        return mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("id").is(idValue)), update,
                getEntityClass());
    }

    @Override
    public UpdateResult update(String collectionName, T entity) {
        Object idValue = Reflect.on(entity).field(ID_FIELD_NAME).get();
        Update update = createUpdate(entity);
        return mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("id").is(idValue).and("")), update,
                getEntityClass(), collectionName);
    }

    @Override
    public UpdateResult update(T entity, String... fields) {
        Object idValue = Reflect.on(entity).field(ID_FIELD_NAME).get();
        Update update = createUpdate(entity, fields);
        return mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("id").is(idValue)), update,
                getEntityClass());
    }

    @Override
    public UpdateResult update(String collectionName, T entity, String... fields) {
        Object idValue = Reflect.on(entity).field(ID_FIELD_NAME).get();
        Update update = createUpdate(entity, fields);
        return mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("id").is(idValue)), update,
                getEntityClass(), collectionName);
    }

    @Override
    public UpdateResult updateMulti(List<T> entityList) {
        Update update = new Update();
        List<Object> ids = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(entityList)) {
            update = createUpdate(entityList.get(0));
            ids = getIdLst(entityList);
        }
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    @Override
    public UpdateResult updateMulti(List<T> entityList, String... fields) {
        Update update = new Update();
        List<Object> ids = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(entityList)) {
            update = createUpdate(entityList.get(0), fields);
            ids = getIdLst(entityList);
        }
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    @Override
    public UpdateResult updateMulti(String collectionName, List<T> entityList) {
        Update update = new Update();
        List<Object> ids = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(entityList)) {
            update = createUpdate(entityList.get(0));
            ids = getIdLst(entityList);
        }
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.updateMulti(query, update, getEntityClass(), collectionName);
    }

    @Override
    public UpdateResult updateMulti(String collectionName, List<T> entityList, String... fields) {
        Update update = new Update();
        List<Object> ids = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(entityList)) {
            update = createUpdate(entityList.get(0), fields);
            ids = getIdLst(entityList);
        }
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.updateMulti(query, update, getEntityClass(), collectionName);
    }

    @Override
    public UpdateResult updateMulti(Object[] ids, String field, Object value) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        Update update = createUpdate(field, value);
        return mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    @Override
    public UpdateResult updateMulti(Object[] ids, String field, Object value, String collectionName) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        Update update = createUpdate(field, value);
        return mongoTemplate.updateMulti(query, update, getEntityClass(), collectionName);
    }

    @Override
    public UpdateResult updateMulti(Object[] ids, String[] fields, Object[] values) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        Update update = createUpdate(fields, values);
        return mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    @Override
    public UpdateResult updateMulti(Object[] ids, String[] fields, Object[] values, String collectionName) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        Update update = createUpdate(fields, values);
        return mongoTemplate.updateMulti(query, update, getEntityClass(), collectionName);
    }

    @Override
    public DeleteResult deleteById(Object id) {
        Query query = new Query(Criteria.where(ID_FIELD_NAME).is(id));
        return mongoTemplate.remove(query, getEntityClass());
    }

    @Override
    public DeleteResult deleteById(String collectionName, Object id) {
        Query query = new Query(Criteria.where(ID_FIELD_NAME).is(id));
        return mongoTemplate.remove(query, getEntityClass(), collectionName);
    }

    @Override
    public DeleteResult deleteByIds(Object... ids) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.remove(query, getEntityClass());
    }

    @Override
    public DeleteResult deleteByIds(String collectionName, Object... ids) {
        Query query = new Query(Criteria.where(ID_FIELD_NAME).in(ids));
        return mongoTemplate.remove(query, getEntityClass(), collectionName);
    }

    @Override
    public DeleteResult delete(String field, Object value) {
        Query query = createQuery(field, value, null);
        return mongoTemplate.remove(query, getEntityClass());
    }

    @Override
    public DeleteResult delete(String field, Object value, String collectionName) {
        Query query = createQuery(field, value, null);
        return mongoTemplate.remove(query, getEntityClass(), collectionName);
    }

    @Override
    public DeleteResult delete(String[] fields, Object[] values) {
        Query query = createQuery(fields, values);
        return mongoTemplate.remove(query, getEntityClass());
    }

    @Override
    public DeleteResult delete(String[] fields, Object[] values, String collectionName) {
        Query query = createQuery(fields, values);
        return mongoTemplate.remove(query, getEntityClass(), collectionName);
    }

    @Override
    public List<T> findAll(String... sortFields) {
        return findAll(Direction.ASC, sortFields);
    }

    @Override
    public List<T> findAll(String collectionName, String... sortFields) {
        return findAll(collectionName, Direction.ASC, sortFields);
    }

    @Override
    public List<T> findAll(Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findAll(orders);
    }

    @Override
    public List<T> findAll(String collectionName, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findAll(collectionName, orders);
    }

    @Override
    public List<T> findAll(List<Order> orders) {
        Query query = new Query().with(Sort.by(orders));
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> findAll(String collectionName, List<Order> orders) {
        Query query = new Query().with(Sort.by(orders));
        return mongoTemplate.find(query, getEntityClass(), collectionName);
    }

    @Override
    public List<T> findAll() {
        return mongoTemplate.findAll(getEntityClass());
    }

    @Override
    public List<T> findAll(String collectionName) {
        return mongoTemplate.findAll(getEntityClass(), collectionName);
    }

    @Override
    public T findById(Object id) {
        return mongoTemplate.findById(id, getEntityClass());
    }

    @Override
    public T findById(Object id, String collectionName) {
        return mongoTemplate.findById(id, getEntityClass(), collectionName);
    }

    @Override
    public List<T> findByIds(Object... ids) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> findByIds(String collectionName, Object... ids) {
        Query query = createInQuery(ID_FIELD_NAME, ids);
        return mongoTemplate.find(query, getEntityClass(), collectionName);
    }

    @Override
    public T findOne(String field, Object value) {
        Query query = createQuery(field, value);
        return mongoTemplate.findOne(query, getEntityClass());
    }

    @Override
    public T findOne(String field, Object value, String collectionName) {
        Query query = createQuery(field, value);
        return mongoTemplate.findOne(query, getEntityClass(), collectionName);
    }

    @Override
    public T findOne(String[] fields, Object[] values) {
        Query query = createQuery(fields, values);
        return mongoTemplate.findOne(query, getEntityClass());
    }

    @Override
    public T findOne(String[] fields, Object[] values, String collectionName) {
        Query query = createQuery(fields, values);
        return mongoTemplate.findOne(query, getEntityClass(), collectionName);
    }

    @Override
    public List<T> find(Criteria criteria) {
        return mongoTemplate.find(createQuery(criteria), getEntityClass());
    }

    @Override
    public List<T> find(Criteria criteria, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        Query query = createQuery(criteria).with(Sort.by(orders));
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> find(String field, Object value) {
        return find(field, value, "");
    }

    @Override
    public List<T> find(String collectionName, String field, Object value) {
        return find(collectionName, field, value, "");
    }

    @Override
    public List<T> find(String field, Object value, String... sortFields) {
        return find(field, value, Direction.ASC, sortFields);
    }

    @Override
    public List<T> find(String collectionName, String field, Object value, String... sortFields) {
        return find(collectionName, field, value, Direction.ASC, sortFields);
    }

    @Override
    public List<T> find(String field, Object value, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return find(field, value, orders);
    }

    @Override
    public List<T> find(String collectionName, String field, Object value, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return find(collectionName, field, value, orders);
    }

    @Override
    public List<T> find(String field, Object value, List<Order> orders) {
        Query query = createQuery(field, value, orders);
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> find(String collectionName, String field, Object value, List<Order> orders) {
        Query query = createQuery(field, value, orders);
        return mongoTemplate.find(query, getEntityClass(), collectionName);
    }

    @Override
    public List<T> find(String[] fields, Object[] values) {
        Query query = createQuery(fields, values);
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> find(String collectionName, String[] fields, Object[] values) {
        Query query = createQuery(fields, values);
        return mongoTemplate.find(query, getEntityClass(), collectionName);
    }

    @Override
    public List<T> find(String[] fields, Object[] values, String... sortFields) {
        return find(fields, values, Direction.ASC, sortFields);
    }

    @Override
    public List<T> find(String collectionName, String[] fields, Object[] values, String... sortFields) {
        return find(collectionName, fields, values, Direction.ASC, sortFields);
    }

    @Override
    public List<T> find(String[] fields, Object[] values, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return find(fields, values, orders);

    }

    @Override
    public List<T> find(String collectionName, String[] fields, Object[] values, Direction direction,
                        String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return find(collectionName, fields, values, orders);

    }

    @Override
    public List<T> find(String[] fields, Object[] values, List<Order> orders) {
        Query query = createQuery(fields, values, orders);
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> find(String collectionName, String[] fields, Object[] values, List<Order> orders) {
        Query query = createQuery(fields, values, orders);
        return mongoTemplate.find(query, getEntityClass(), collectionName);
    }

    @Override
    public Page<T> findByPage(Criteria criteria, Page page) {
        return findByPage(criteria, page, "");
    }

    @Override
    public Page<T> findByPage(String collectionName, Criteria criteria, Page page) {
        return findByPage(collectionName, criteria, page, "");
    }

    @Override
    public Page<T> findByPage(Criteria criteria, Page page, String... sortFields) {
        return findByPage(criteria, page, Direction.ASC, "");
    }

    @Override
    public Page<T> findByPage(String collectionName, Criteria criteria, Page page, String... sortFields) {
        return findByPage(collectionName, criteria, page, Direction.ASC, "");
    }

    @Override
    public Page<T> findByPage(Criteria criteria, Page page, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findByPage(criteria, page, orders);
    }

    @Override
    public Page<T> findByPage(String collectionName, Criteria criteria, Page page, Direction direction,
                              String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findByPage(collectionName, criteria, page, orders);
    }

    @Override
    public Page<T> findByPage(Criteria criteria, Page page, List<Order> orders) {
        return findByPage("", criteria, page, orders);
    }

    @Override
    public Page<T> findByPage(String collectionName, Criteria criteria, Page page, List<Order> orders) {
        // 查询总记录数
        int count = 0;
        if (EmptyUtil.isNotEmpty(collectionName)) {
            count = countByCriteria(criteria, collectionName);
        } else {
            count = countByCriteria(criteria);
        }
        page.setTotalCount(count);
        // 设置总页数
        int totalPage = (count - 1) / page.getPageSize() + 1;
        page.setTotalPage(totalPage);
        // 查询数据列表
        Query query = createQuery(criteria);
        // 设置分页信息
        query.skip(page.getFirstResult());
        query.limit(page.getPageSize());
        // 设置排序信息
        if (EmptyUtil.isNotEmpty(orders)) {
            query.with(Sort.by(orders));
        }
        // 封装结果数据
        if (EmptyUtil.isNotEmpty(collectionName)) {
            page.setList(mongoTemplate.find(query, getEntityClass(), collectionName));
        } else {
            page.setList(mongoTemplate.find(query, getEntityClass()));
        }
        return page;
    }

    @Override
    public Page<T> findByPage(Page page) {
        return findByPage(page, "");
    }

    @Override
    public Page<T> findByPage(Page page, String... sortFields) {
        return findByPage(page, Direction.ASC, sortFields);
    }

    @Override
    public Page<T> findByPage(Page page, Direction direction, String... sortFields) {
        // 排序信息
        List<Order> orders = createOrders(direction, sortFields);
        return findByPage(page, orders);
    }

    @Override
    public Page<T> findByPage(Page page, List<Order> orders) {
        return findByPage("", "", page, orders);
    }

    @Override
    public Page<T> findByPage(String field, Object value, Page page) {
        return findByPage(field, value, page, "");
    }

    @Override
    public Page<T> findByPage(String field, Object value, Page page, String... sortFields) {
        return findByPage(field, value, page, Direction.ASC, sortFields);
    }

    @Override
    public Page<T> findByPage(String field, Object value, Page page, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findByPage(field, value, page, orders);
    }

    @Override
    public Page<T> findByPage(String[] fields, Object[] values, Page page) {
        return findByPage(fields, values, page, "");
    }

    @Override
    public Page<T> findByPage(String[] fields, Object[] values, Page page, String... sortFields) {
        return findByPage(fields, values, page, Direction.ASC, "");
    }

    @Override
    public Page<T> findByPage(String[] fields, Object[] values, Page page, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findByPage(fields, values, page, orders);
    }

    @Override
    public Page<T> findByPage(String field, Object value, Page page, List<Order> orders) {
        String[] fields = new String[]{field};
        Object[] values = new Object[]{value};
        return findByPage(fields, values, page, orders);
    }

    @Override
    public Page<T> findByPage(String[] fields, Object[] values, Page page, List<Order> orders) {

        // 查询总记录数
        int count = countByCondition(fields, values);
        page.setTotalCount(count);
        // 设置总页数
        int totalPage = (count - 1) / page.getPageSize() + 1;
        page.setTotalPage(totalPage);
        // 查询数据列表
        Query query = createQuery(fields, values);
        // 设置分页信息
        query.skip(page.getFirstResult());
        query.limit(page.getPageSize());
        // 排序信息
        if (EmptyUtil.isNotEmpty(orders)) {
            query.with(Sort.by(orders));
        }
        // 封装结果数据
        page.setList(mongoTemplate.find(query, getEntityClass()));
        return page;
    }

    @Override
    public Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition) {
        return findBySearchCondition(criteria, baseSearchCondition, "");
    }

    @Override
    public Page<T> findBySearchCondition(String collectionName, Criteria criteria,
                                         BaseSearchCondition baseSearchCondition) {
        return findBySearchCondition(collectionName, criteria, baseSearchCondition, "");
    }

    @Override
    public Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition,
                                         String... sortFields) {
        return findBySearchCondition(criteria, baseSearchCondition, Direction.ASC, "");
    }

    @Override
    public Page<T> findBySearchCondition(String collectionName, Criteria criteria,
                                         BaseSearchCondition baseSearchCondition, String... sortFields) {
        return findBySearchCondition(collectionName, criteria, baseSearchCondition, Direction.ASC, "");
    }

    @Override
    public Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition,
                                         Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findBySearchCondition(criteria, baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(String collectionName, Criteria criteria,
                                         BaseSearchCondition baseSearchCondition, Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findBySearchCondition(collectionName, criteria, baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition,
                                         List<Order> orders) {
        return findBySearchCondition("", criteria, baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(String collectionName, Criteria criteria,
                                         BaseSearchCondition baseSearchCondition, List<Order> orders) {
        Page<T> page = new Page<>(baseSearchCondition.getPage(), baseSearchCondition.getLimit());
        // 查询总记录数
        int count = 0;
        if (EmptyUtil.isNotEmpty(collectionName)) {
            count = countByCriteria(criteria, collectionName);
        } else {
            count = countByCriteria(criteria);
        }
        page.setTotalCount(count);
        // 设置总页数
        int totalPage = (count - 1) / page.getPageSize() + 1;
        page.setTotalPage(totalPage);
        // 查询数据列表
        Query query = createQuery(criteria);
        // 设置分页信息
        query.skip(page.getFirstResult());
        query.limit(page.getPageSize());
        // 设置排序信息
        if (EmptyUtil.isNotEmpty(orders)) {
            query.with(Sort.by(orders));
        }
        // 封装结果数据
        if (EmptyUtil.isNotEmpty(collectionName)) {
            page.setList(mongoTemplate.find(query, getEntityClass(), collectionName));
        } else {
            page.setList(mongoTemplate.find(query, getEntityClass()));
        }
        return page;
    }

    @Override
    public Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition) {
        return findBySearchCondition(baseSearchCondition, "");
    }

    @Override
    public Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition, String... sortFields) {
        return findBySearchCondition(baseSearchCondition, Direction.ASC, sortFields);
    }

    @Override
    public Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition, Direction direction,
                                         String... sortFields) {
        // 排序信息
        List<Order> orders = createOrders(direction, sortFields);
        return findBySearchCondition(baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition, List<Order> orders) {
        return findBySearchCondition("", "", baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition) {
        return findBySearchCondition(field, value, baseSearchCondition, "");
    }

    @Override
    public Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition,
                                         String... sortFields) {
        return findBySearchCondition(field, value, baseSearchCondition, Direction.ASC, sortFields);
    }

    @Override
    public Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition,
                                         Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findBySearchCondition(field, value, baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition) {
        return findBySearchCondition(fields, values, baseSearchCondition, "");
    }

    @Override
    public Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition,
                                         String... sortFields) {
        return findBySearchCondition(fields, values, baseSearchCondition, Direction.ASC, "");
    }

    @Override
    public Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition,
                                         Direction direction, String... sortFields) {
        List<Order> orders = createOrders(direction, sortFields);
        return findBySearchCondition(fields, values, baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition,
                                         List<Order> orders) {
        String[] fields = new String[]{field};
        Object[] values = new Object[]{value};
        return findBySearchCondition(fields, values, baseSearchCondition, orders);
    }

    @Override
    public Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition,
                                         List<Order> orders) {
        Page<T> page = new Page<>(baseSearchCondition.getPage(), baseSearchCondition.getLimit());
        // 查询总记录数
        int count = countByCondition(fields, values);
        page.setTotalCount(count);
        // 设置总页数
        int totalPage = (count - 1) / page.getPageSize() + 1;
        page.setTotalPage(totalPage);
        // 查询数据列表
        Query query = createQuery(fields, values);
        // 设置分页信息
        query.skip(page.getFirstResult());
        query.limit(page.getPageSize());
        // 排序信息
        if (EmptyUtil.isNotEmpty(orders)) {
            query.with(Sort.by(orders));
        }
        // 封装结果数据
        page.setList(mongoTemplate.find(query, getEntityClass()));
        return page;
    }

    @Override
    public int count() {
        Query query = createQuery("", null);
        Long count = mongoTemplate.count(query, getEntityClass());
        return count.intValue();
    }

    @Override
    public int countByCondition(String field, Object value) {
        Query query = createQuery(field, value);
        Long count = mongoTemplate.count(query, getEntityClass());
        return count.intValue();
    }

    @Override
    public int countByCondition(String[] fields, Object[] values) {
        Query query = createQuery(fields, values);
        Long count = mongoTemplate.count(query, getEntityClass());
        return count.intValue();
    }

    @Override
    public int countByCriteria(Criteria criteria) {
        Long count = mongoTemplate.count(new Query(criteria), getEntityClass());
        return count.intValue();
    }

    @Override
    public int countByCriteria(Criteria criteria, String collectionName) {
        Long count = mongoTemplate.count(new Query(criteria), getEntityClass(), collectionName);
        return count.intValue();
    }

    /**
     * Query对象
     *
     * @param field 字段
     * @param value 字段值
     * @return Query对象
     */
    private Query createQuery(String field, Object value) {
        Query query = new Query();
        // where 条件
        if (EmptyUtil.isNotEmpty(field)) {
            query.addCriteria(Criteria.where(field).is(value));
        }
        return query;
    }

    /**
     * 创建带有in查询Query对象
     *
     * @param field 字段
     * @param value 字段对应值范围
     * @return Query对象
     */
    private Query createInQuery(String field, Object... value) {
        Query query = new Query();
        // where 条件
        if (EmptyUtil.isNotEmpty(field)) {
            query.addCriteria(Criteria.where(field).in(value));
        }
        return query;
    }

    /**
     * Query对象
     *
     * @param field  字段
     * @param value  字段值
     * @param orders 排序集合
     * @return Query对象
     */
    private Query createQuery(String field, Object value, List<Order> orders) {
        Query query = new Query();
        // where 条件
        if (EmptyUtil.isNotEmpty(field)) {
            query.addCriteria(Criteria.where(field).is(value));
        }
        // 排序
        if (EmptyUtil.isNotEmpty(orders)) {
            query.with(Sort.by(orders));
        }
        return query;
    }

    /**
     * 创建Query对象
     *
     * @param fields 字段数组
     * @param values 字段数组对应值
     * @return Query对象
     */
    private Query createQuery(String[] fields, Object[] values) {
        Query query = new Query();
        // where 条件
        if (EmptyUtil.isNotEmpty(fields) && EmptyUtil.isNotEmpty(values)) {
            for (int i = 0; i < fields.length; i++) {
                query.addCriteria(Criteria.where(fields[i]).is(values[i]));
            }
        }
        return query;
    }

    /**
     * 创建Query对象
     *
     * @param fields 字段数组
     * @param values 字段数组对应值
     * @param orders 排序集合
     * @return Query对象
     */
    private Query createQuery(String[] fields, Object[] values, List<Order> orders) {
        Query query = new Query();
        // where 条件
        if (EmptyUtil.isNotEmpty(fields) && EmptyUtil.isNotEmpty(values)) {
            for (int i = 0; i < fields.length; i++) {
                query.addCriteria(Criteria.where(fields[i]).is(values[i]));
            }
        }
        // 排序
        if (EmptyUtil.isNotEmpty(orders)) {
            query.with(Sort.by(orders));
        }
        return query;
    }

    /**
     * 创建Query对象
     *
     * @param criteria Criteria对象
     * @return Query对象
     */
    private Query createQuery(Criteria criteria) {
        return new Query(criteria);
    }

    /**
     * 创建Update对象
     *
     * @param entity 要更新的实体
     * @return Update对象
     */
    private Update createUpdate(T entity) {
        Update update = new Update();
        for (Map.Entry<String, Object> entry : beanToMap(entity).entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        return update;
    }

    /**
     * 根据要更新的属性创建Update对象
     *
     * @param entity       要更新的实体
     * @param updateFields 要更新的属性
     * @return Update对象
     */
    private Update createUpdate(T entity, String... updateFields) {
        Update update = new Update();
        Map<String, Object> propertyMap = beanToMap(entity);
        for (String updateField : updateFields) {
            if (EmptyUtil.isNotEmpty(updateField)) {
                update.set(updateField, propertyMap.get(updateField));
            }
        }
        return update;
    }

    /**
     * 创建update对象
     *
     * @param field 要更新的字段名
     * @param value 要更新的字段值
     * @return update对象
     */
    private Update createUpdate(String field, Object value) {
        Update update = new Update();
        if (EmptyUtil.isNotEmpty(field)) {
            update.set(field, value);
        }
        return update;
    }

    /**
     * 创建update对象
     *
     * @param fields 要更新的字段名数组
     * @param values 要更新的字段名数组对应值
     * @return update对象
     */
    private Update createUpdate(String[] fields, Object[] values) {
        Update update = new Update();
        int len = fields.length;
        for (int i = 0; i < len; i++) {
            if (EmptyUtil.isNotEmpty(fields[i])) {
                update.set(fields[i], values[i]);
            }
        }
        return update;
    }

    /**
     * 实体类转为map
     *
     * @param entity 要转换的实体
     * @return 实体的属性-值的map集合
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> beanToMap(T entity) {
        BeanMap beanMap = BeanMap.create(entity);
        Map<String, Object> propertyMap = new HashMap<>(beanMap);
        propertyMap.remove(ID_FIELD_NAME);
        return propertyMap;
    }

    /**
     * 创建Order排序对象集合
     *
     * @param direction  排序方式，不指定的情况下按默认升序排序
     * @param sortFields 排序字段
     * @return Order排序对象集合
     */
    private List<Order> createOrders(Direction direction, String... sortFields) {
        List<Order> orders = new ArrayList<>();
        for (String field : sortFields) {
            if (EmptyUtil.isNotEmpty(field)) {
                orders.add(new Order(direction, field));
            }
        }
        return orders;
    }

    /**
     * 根据entityList获取idList
     *
     * @param entityList 实体集合
     * @return id的集合
     */
    private List<Object> getIdLst(List<T> entityList) {
        List<Object> idList = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(entityList)) {
            for (T t : entityList) {
                Object idValue = Reflect.on(t).field(ID_FIELD_NAME).get();
                idList.add(idValue);
            }
        }
        return idList;
    }
}
