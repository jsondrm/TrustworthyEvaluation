package com.soft.eva.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.soft.eva.dto.BaseSearchCondition;
import com.soft.eva.dto.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T> {

	String ID_FIELD_NAME = "id";
	String SOFTWAREBELONGSTO_FIELD_NAME = "softwareBelongsTo";
	String NAME_FIELD_NAME = "name";
	String TYPE_FIELD_NAME = "type";
	String PHASEBELONGSTO_FIELD_NAME = "phaseBelongsTo";


	/* 保存实体*/

	/**
	 * 保存实体 备注：执行完成本方法后，所引用实体的主键id会自动赋上值
	 *
	 * @param entity 要保存的实体
	 * @return 返回实体主键id值
	 */
	Object save(T entity);

	/**
	 * 保存实体 备注：执行完成本方法后，所引用实体的主键id会自动赋上值
	 *
	 * @param entity         要保存的实体
	 * @param collectionName 集合名称，不能为空
	 * @return 返回实体主键id值
	 */
	Object save(T entity, String collectionName);

	/**
	 * 保存多个实体
	 *
	 * @param entityList 多个实体
	 */
	void save(List<T> entityList);

	/**
	 * 保存多个实体
	 *
	 * @param entityList     多个实体
	 * @param collectionName 集合名称，不能为空
	 */
	void save(List<T> entityList, String collectionName);


	void upsert(T entity);

	void upsert(List<T> entityList);

	/*修改实体*/

	/**
	 * 修改实体
	 *
	 * @param entity 要修改的实体
	 * @return 修改结果
	 */
	UpdateResult update(T entity);

	/**
	 * 修改实体
	 *
	 * @param entity         要修改的实体
	 * @param collectionName 集合名称，不能为空
	 * @return 修改结果
	 */
	UpdateResult update(String collectionName, T entity);

	/**
	 * 修改实体指定的属性
	 *
	 * @param entity 要修改的实体
	 * @param fields 要修改的属性
	 * @return 修改结果
	 */
	UpdateResult update(T entity, String... fields);

	/**
	 * 修改实体指定的属性
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param entity         要修改的实体
	 * @param fields         要修改的属性
	 * @return 修改结果
	 */
	UpdateResult update(String collectionName, T entity, String... fields);

	/**
	 * 批量更新
	 *
	 * @param entityList 实体集合
	 * @return 更新结果
	 */
	UpdateResult updateMulti(List<T> entityList);

	/**
	 * 批量更新
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param entityList     实体集合
	 * @return 更新结果
	 */
	UpdateResult updateMulti(String collectionName, List<T> entityList);

	/**
	 * 批量更新
	 *
	 * @param entityList 实体集合
	 * @param fields     要更新的字段
	 * @return 更新结果
	 */
	UpdateResult updateMulti(List<T> entityList, String... fields);

	/**
	 * 批量更新
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param entityList     实体集合
	 * @param fields         要更新的字段
	 * @return 更新结果
	 */
	UpdateResult updateMulti(String collectionName, List<T> entityList, String... fields);

	/**
	 * 批量修改某一个字段
	 *
	 * @param ids   主键数组
	 * @param field 要修改的字段
	 * @param value 要修改的字段对应值
	 * @return 修改结果
	 */
	UpdateResult updateMulti(Object[] ids, String field, Object value);

	/**
	 * 批量修改某一个字段（待集合名称）
	 *
	 * @param ids            主键数组
	 * @param field          要修改的字段
	 * @param value          要修改的字段对应值
	 * @param collectionName 集合名称，不能为空
	 * @return 修改结果
	 */
	UpdateResult updateMulti(Object[] ids, String field, Object value, String collectionName);

	/**
	 * 批量修改多个字段
	 *
	 * @param ids    主键数组
	 * @param fields 要修改的字段数组
	 * @param values 要修改的字段数组对应值
	 * @return 修改结果
	 */
	UpdateResult updateMulti(Object[] ids, String[] fields, Object[] values);

	/**
	 * 批量修改多个字段（带集合名）
	 *
	 * @param ids            主键数组
	 * @param fields         要修改的字段数组
	 * @param values         要修改的字段数组对应值
	 * @param collectionName 集合名称，不能为空
	 * @return 修改结果
	 */
	UpdateResult updateMulti(Object[] ids, String[] fields, Object[] values, String collectionName);



	/* 删除实体*/

	/**
	 * 根据主键删除
	 *
	 * @param id 主键
	 * @return 删除结果
	 */
	DeleteResult deleteById(Object id);

	/**
	 * 根据主键删除（带集合名）
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param id             主键
	 * @return 删除结果
	 */
	DeleteResult deleteById(String collectionName, Object id);

	/**
	 * 删除实体[数组]
	 *
	 * @param ids 实体ID或数组
	 * @return 删除结果
	 */
	DeleteResult deleteByIds(Object... ids);

	/**
	 * 删除实体[数组]（带集合名称）
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param ids            实体ID或数组
	 * @return 删除结果
	 */
	DeleteResult deleteByIds(String collectionName, Object... ids);

	/**
	 * 根据单个字段进行删除
	 *
	 * @param field 字段
	 * @param value 字段值
	 * @return 删除结果
	 */
	DeleteResult delete(String field, Object value);

	/**
	 * 根据单个字段进行删除（带集合名称）
	 *
	 * @param field          字段
	 * @param value          字段值
	 * @param collectionName 集合名称，不能为空
	 * @return 删除结果
	 */
	DeleteResult delete(String field, Object value, String collectionName);

	/**
	 * 根据多个字段进行删除
	 *
	 * @param fields 字段数组
	 * @param values 字段数组对应值
	 * @return 删除结果
	 */
	DeleteResult delete(String[] fields, Object[] values);

	/**
	 * 根据多个字段进行删除（带集合名称）
	 *
	 * @param fields         字段数组
	 * @param values         字段数组对应值
	 * @param collectionName 集合名称，不能为空
	 * @return 删除结果
	 */
	DeleteResult delete(String[] fields, Object[] values, String collectionName);




	/*查询实体*/

	/**
	 * 根据字段排序查询，默认升序
	 *
	 * @param sortFields 排序字段
	 * @return 查询结果集
	 */
	List<T> findAll(String... sortFields);

	/**
	 * 根据字段排序查询，默认升序（带集合名）
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param sortFields     排序字段
	 * @return 查询结果集
	 */
	List<T> findAll(String collectionName, String... sortFields);

	/**
	 * 根据字段排序查询，可以指定排序方式
	 *
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 查询结果集
	 */
	List<T> findAll(Direction direction, String... sortFields);

	/**
	 * 根据字段排序查询，可以指定排序方式（带集合名称）
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param direction      排序方式
	 * @param sortFields     排序字段
	 * @return 查询结果集
	 */
	List<T> findAll(String collectionName, Direction direction, String... sortFields);

	/**
	 * 排序查询
	 *
	 * @param orders 排序方式集合
	 * @return 查询结果集
	 */
	List<T> findAll(List<Order> orders);

	/**
	 * 排序查询（带集合名称）
	 *
	 * @param orders         排序方式集合
	 * @param collectionName 集合名称，不能为空
	 * @return 查询结果集
	 */
	List<T> findAll(String collectionName, List<Order> orders);

	/**
	 * [不分页]
	 *
	 * @return 查询所有记录
	 */
	List<T> findAll();

	/**
	 * [不分页]（带集合名称）
	 *
	 * @param collectionName 集合名称，不能为空
	 * @return 查询所有记录
	 */
	List<T> findAll(String collectionName);

	/**
	 * 查找实体
	 *
	 * @param id 根据ID查询
	 * @return 返回要查找的实体
	 */
	T findById(Object id);

	/**
	 * 查找实体
	 *
	 * @param id             根据ID查询
	 * @param collectionName 集合名称，不能为空
	 * @return 返回要查找的实体
	 */
	T findById(Object id, String collectionName);

	/**
	 * 根据主键数组查询，将数组中匹配的结果查询出来
	 *
	 * @param ids 主键值数组
	 * @return 结果列表或null
	 */
	List<T> findByIds(Object... ids);

	/**
	 * 根据主键数组查询，将数组中匹配的结果查询出来
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param ids            主键值数组
	 * @return 结果列表或null
	 */
	List<T> findByIds(String collectionName, Object... ids);

	/**
	 * 查找单一实体
	 *
	 * @param field 查询字段名
	 * @param value 查询字段值
	 * @return 返回要查找的实体
	 */
	T findOne(String field, Object value);

	/**
	 * 查找单一实体
	 *
	 * @param field          查询字段名
	 * @param value          查询字段值
	 * @param collectionName 集合名称，不能为空
	 * @return 返回要查找的实体
	 */
	T findOne(String field, Object value, String collectionName);

	/**
	 * 根据多个字段查找单一实体
	 *
	 * @param fields 查询字段数组
	 * @param values 查询字段数组对应值
	 * @return 返回要查找的实体
	 */
	T findOne(String[] fields, Object[] values);

	/**
	 * 根据多个字段查找单一实体（带集合名）
	 *
	 * @param fields         查询字段数组
	 * @param values         查询字段数组对应值
	 * @param collectionName 集合名称，不能为空
	 * @return 返回要查找的实体
	 */
	T findOne(String[] fields, Object[] values, String collectionName);

	/**
	 * 根据Criteria对象查询
	 *
	 * @param criteria Criteria对象
	 * @return 结果列表 或 null
	 */
	List<T> find(Criteria criteria);

	/**
	 * 根据Criteria对象查询并排序
	 * @param criteria Criteria对象
	 * @param direction 排序方式
	 * @param sortFields 排序字段
	 * @return 结果列表 或 null
	 */
	List<T> find(Criteria criteria, Direction direction, String... sortFields);

	/**
	 * 根据单一参数查询记录
	 *
	 * @param field 字段
	 * @param value 字段值
	 * @return 结果列表 或 null
	 */
	List<T> find(String field, Object value);

	/**
	 * 根据单一参数查询记录
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param field          字段
	 * @param value          字段值
	 * @return 结果列表 或 null
	 */
	List<T> find(String collectionName, String field, Object value);

	/**
	 * 根据单一参数查询记录并排序
	 *
	 * @param field      字段
	 * @param value      字段值
	 * @param sortFields 排序字段，默认排序方式：升序
	 * @return 结果集合或null
	 */
	List<T> find(String field, Object value, String... sortFields);

	/**
	 * 根据单一参数查询记录并排序
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param field          字段
	 * @param value          字段值
	 * @param sortFields     排序字段，默认排序方式：升序
	 * @return 结果集合或null
	 */
	List<T> find(String collectionName, String field, Object value, String... sortFields);

	/**
	 * 根据单一参数查询记录并排序,可以指定排序方式
	 *
	 * @param field      字段
	 * @param value      字段值
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 结果集合或null
	 */
	List<T> find(String field, Object value, Direction direction, String... sortFields);

	/**
	 * 根据单一参数查询记录并排序,可以指定排序方式
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param field          字段
	 * @param value          字段值
	 * @param direction      排序方式
	 * @param sortFields     排序字段
	 * @return 结果集合或null
	 */
	List<T> find(String collectionName, String field, Object value, Direction direction, String... sortFields);

	/**
	 * 根据单一参数查询记录并排序,可以指定排序集合，可以不指定
	 *
	 * @param field  查询字段
	 * @param value  字段对应值
	 * @param orders 排序集合,可为空
	 * @return 结果集合或null
	 */
	List<T> find(String field, Object value, List<Order> orders);

	/**
	 * 根据单一参数查询记录并排序,可以指定排序集合，可以不指定
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param field          查询字段
	 * @param value          字段对应值
	 * @param orders         排序集合,可为空
	 * @return 结果集合或null
	 */
	List<T> find(String collectionName, String field, Object value, List<Order> orders);

	/**
	 * 根据多个参数查询记录[不分页]
	 *
	 * @param fields 字段数组
	 * @param values 字段数组对应值
	 * @return 结果集合 或 null
	 */
	List<T> find(String[] fields, Object[] values);

	/**
	 * 根据多个参数查询记录[不分页]
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param fields         字段数组
	 * @param values         字段数组对应值
	 * @return 结果集合 或 null
	 */
	List<T> find(String collectionName, String[] fields, Object[] values);

	/**
	 * 根据多个参数查询记录并排序
	 *
	 * @param fields     字段数组
	 * @param values     字段数组对应值
	 * @param sortFields 排序字段，默认升序
	 * @return 结果集合 或 null
	 */
	List<T> find(String[] fields, Object[] values, String... sortFields);

	/**
	 * 根据多个参数查询记录并排序
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param fields         字段数组
	 * @param values         字段数组对应值
	 * @param sortFields     排序字段，默认升序
	 * @return 结果集合 或 null
	 */
	List<T> find(String collectionName, String[] fields, Object[] values, String... sortFields);

	/**
	 * 根据多个参数查询记录并排序，可以指定排序方式
	 *
	 * @param fields     字段数组
	 * @param values     字段数组对应值
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 结果集合 或 null
	 */
	List<T> find(String[] fields, Object[] values, Direction direction, String... sortFields);

	/**
	 * 根据多个参数查询记录并排序，可以指定排序方式
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param fields         字段数组
	 * @param values         字段数组对应值
	 * @param direction      排序方式
	 * @param sortFields     排序字段
	 * @return 结果集合 或 null
	 */
	List<T> find(String collectionName, String[] fields, Object[] values, Direction direction, String... sortFields);

	/**
	 * 根据多个参数查询记录并排序，可以指定排序方式，可以不指定
	 *
	 * @param fields 字段数组
	 * @param values 字段数组对应值
	 * @param orders 排序集合，可以为空
	 * @return 结果集合 或 null
	 */
	List<T> find(String[] fields, Object[] values, List<Order> orders);

	/**
	 * 根据多个参数查询记录并排序，可以指定排序方式，可以不指定
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param fields         字段数组
	 * @param values         字段数组对应值
	 * @param orders         排序集合，可以为空
	 * @return 结果集合 或 null
	 */
	List<T> find(String collectionName, String[] fields, Object[] values, List<Order> orders);

	/**
	 * 分页查询
	 *
	 * @param criteria criteria对象
	 * @param page     分页对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Criteria criteria, Page page);

	/**
	 * 分页查询
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param criteria       criteria对象
	 * @param page           分页对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String collectionName, Criteria criteria, Page page);

	/**
	 * 分页查询
	 *
	 * @param criteria   criteria对象
	 * @param page       分页对象
	 * @param sortFields 排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Criteria criteria, Page page, String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param criteria       criteria对象
	 * @param page           分页对象
	 * @param sortFields     排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String collectionName, Criteria criteria, Page page, String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param criteria   criteria对象
	 * @param page       分页对象
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Criteria criteria, Page page, Direction direction, String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param criteria       criteria对象
	 * @param page           分页对象
	 * @param direction      排序方式
	 * @param sortFields     排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String collectionName, Criteria criteria, Page page, Direction direction,
					   String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param criteria criteria对象
	 * @param page     分页对象
	 * @param orders   排序集合
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Criteria criteria, Page page, List<Order> orders);

	/**
	 * 分页查询
	 *
	 * @param collectionName 集合名称，不能为空
	 * @param criteria       criteria对象
	 * @param page           分页对象
	 * @param orders         排序集合
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String collectionName, Criteria criteria, Page page, List<Order> orders);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param page 分页对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Page page);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param page       分页对象
	 * @param sortFields 排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Page page, String... sortFields);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param page       分页对象
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Page page, Direction direction, String... sortFields);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param page   分页对象
	 * @param orders 排序集合，可以为空
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(Page page, List<Order> orders);

	/**
	 * 根据参数分页查询结果集合
	 *
	 * @param field 查询字段
	 * @param value 查询字段值
	 * @param page  分页对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String field, Object value, Page page);

	/**
	 * 根据参数分页查询结果集合并排序
	 *
	 * @param field      查询字段名
	 * @param value      查询字段值
	 * @param page       分页对象
	 * @param sortFields 排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String field, Object value, Page page, String... sortFields);

	/**
	 * 根据参数分页查询结果集合并排序
	 *
	 * @param field      查询字段名
	 * @param value      查询字段值
	 * @param page       分页对象
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String field, Object value, Page page, Direction direction, String... sortFields);

	/**
	 * 根据参数分页查询结果集合并排序
	 *
	 * @param field  查询字段名
	 * @param value  查询字段值
	 * @param page   分页对象
	 * @param orders 排序集合，可以为空
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String field, Object value, Page page, List<Order> orders);

	/**
	 * 根据参数分页查询结果集合
	 *
	 * @param fields 查询字段数组
	 * @param values 查询字段数组对应值
	 * @param page   分页对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String[] fields, Object[] values, Page page);

	/**
	 * 根据参数分页查询结果集合 并排序
	 *
	 * @param fields     查询字段数组
	 * @param values     查询字段数组对应值
	 * @param page       分页对象
	 * @param sortFields 排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String[] fields, Object[] values, Page page, String... sortFields);

	/**
	 * 根据参数分页查询结果集合 并排序
	 *
	 * @param fields     查询字段数组
	 * @param values     查询字段数组对应值
	 * @param page       分页对象
	 * @param direction  排序方式
	 * @param sortFields 排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String[] fields, Object[] values, Page page, Direction direction,
                       String... sortFields);

	/**
	 * 根据参数分页查询结果集合 并排序
	 *
	 * @param fields 查询字段数组
	 * @param values 查询字段数组对应值
	 * @param page   分页对象
	 * @param orders 排序集合，可以为空
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findByPage(String[] fields, Object[] values, Page page, List<Order> orders);


	/**
	 * 分页查询
	 *
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition);

	/**
	 * 分页查询
	 *
	 * @param collectionName      集合名称，不能为空
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String collectionName, Criteria criteria, BaseSearchCondition baseSearchCondition);

	/**
	 * 分页查询
	 *
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @param sortFields          排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition, String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param collectionName      集合名称，不能为空
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @param sortFields          排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String collectionName, Criteria criteria, BaseSearchCondition baseSearchCondition, String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @param direction           排序方式
	 * @param sortFields          排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition, Direction direction, String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param collectionName      集合名称，不能为空
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @param direction           排序方式
	 * @param sortFields          排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String collectionName, Criteria criteria, BaseSearchCondition baseSearchCondition, Direction direction,
                                  String... sortFields);

	/**
	 * 分页查询
	 *
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @param orders              排序集合
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(Criteria criteria, BaseSearchCondition baseSearchCondition, List<Order> orders);

	/**
	 * 分页查询
	 *
	 * @param collectionName      集合名称，不能为空
	 * @param criteria            criteria对象
	 * @param baseSearchCondition 查询条件对象
	 * @param orders              排序集合
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String collectionName, Criteria criteria, BaseSearchCondition baseSearchCondition, List<Order> orders);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param baseSearchCondition 查询条件对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param baseSearchCondition 查询条件对象
	 * @param sortFields          排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition, String... sortFields);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param baseSearchCondition 查询条件对象
	 * @param direction           排序方式
	 * @param sortFields          排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition, Direction direction, String... sortFields);

	/**
	 * 分页查询所有结果集合并排序
	 *
	 * @param baseSearchCondition 查询条件对象
	 * @param orders              排序集合，可以为空
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(BaseSearchCondition baseSearchCondition, List<Order> orders);

	/**
	 * 根据参数分页查询结果集合
	 *
	 * @param field               查询字段
	 * @param value               查询字段值
	 * @param baseSearchCondition 查询条件对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition);

	/**
	 * 根据参数分页查询结果集合并排序
	 *
	 * @param field               查询字段名
	 * @param value               查询字段值
	 * @param baseSearchCondition 查询条件对象
	 * @param sortFields          排序字段，默认升序
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition, String... sortFields);

	/**
	 * 根据参数分页查询结果集合并排序
	 *
	 * @param field               查询字段名
	 * @param value               查询字段值
	 * @param baseSearchCondition 查询条件对象
	 * @param direction           排序方式
	 * @param sortFields          排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition, Direction direction, String... sortFields);

	/**
	 * 根据参数分页查询结果集合并排序
	 *
	 * @param field               查询字段名
	 * @param value               查询字段值
	 * @param baseSearchCondition 查询条件对象
	 * @param orders              排序集合，可以为空
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String field, Object value, BaseSearchCondition baseSearchCondition, List<Order> orders);

	/**
	 * 根据参数分页查询结果集合
	 *
	 * @param fields              查询字段数组
	 * @param values              查询字段数组对应值
	 * @param baseSearchCondition 查询条件对象
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition);

	/**
	 * 根据参数分页查询结果集合 并排序
	 *
	 * @param fields              查询字段数组
	 * @param values              查询字段数组对应值
	 * @param baseSearchCondition 查询条件对象
	 * @param sortFields          排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition, String... sortFields);

	/**
	 * 根据参数分页查询结果集合 并排序
	 *
	 * @param fields              查询字段数组
	 * @param values              查询字段数组对应值
	 * @param baseSearchCondition 查询条件对象
	 * @param direction           排序方式
	 * @param sortFields          排序字段
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition, Direction direction,
                                  String... sortFields);

	/**
	 * 根据参数分页查询结果集合 并排序
	 *
	 * @param fields              查询字段数组
	 * @param values              查询字段数组对应值
	 * @param baseSearchCondition 查询条件对象
	 * @param orders              排序集合，可以为空
	 * @return 分页模型对象（不会为null）
	 */
	Page<T> findBySearchCondition(String[] fields, Object[] values, BaseSearchCondition baseSearchCondition, List<Order> orders);


	/**
	 * 查询总记录数
	 *
	 * @return 总记录数
	 */
	int count();

	/**
	 * 根据条件查询总记录数
	 *
	 * @param field 查询字段
	 * @param value 查询字段对应值
	 * @return 总记录数
	 */
	int countByCondition(String field, Object value);

	/**
	 * 根据条件查询总记录数
	 *
	 * @param fields 查询字段数组
	 * @param values 查询字段数组对应值
	 * @return 总记录数
	 */
	int countByCondition(String[] fields, Object[] values);

	/**
	 * 查询满足条件的总记录数
	 *
	 * @param criteria Criteria对象
	 * @return 满足条件的总记录数
	 */
	int countByCriteria(Criteria criteria);

	/**
	 * 查询满足条件的总记录数
	 *
	 * @param criteria       Criteria对象
	 * @param collectionName 集合名称，不能为空
	 * @return 满足条件的总记录数
	 */
	int countByCriteria(Criteria criteria, String collectionName);
}
