package com.soft.eva.web;

import com.mongodb.MongoWriteException;
import com.soft.eva.domain.*;
import com.soft.eva.dto.Page;
import com.soft.eva.service.MeasurementDataService;
import com.soft.eva.service.OperateRecordService;
import com.soft.eva.service.SoftDataService;
import com.soft.eva.service.SoftwareProductService;
import com.soft.eva.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping(value = "/softData")
public class SoftDataController {

    ZhenghufanMatrix curZhenghufanMatrixInRequirePhase;
    ZhenghufanMatrix curZhenghufanMatrixInDesignPhase;
    ZhenghufanMatrix curZhenghufanMatrixInCodingPhase;
    ZhenghufanMatrix curZhenghufanMatrixInTestPhase;

    List<List<String>> matrixListsInRequirePhase = new ArrayList<>();//需求阶段，度量元、子属性、属性的正互反矩阵list
    List<List<String>> matrixListsInDesignPhase = new ArrayList<>();//设计阶段，度量元、子属性、属性的正互反矩阵list
    List<List<String>> matrixListsInCodingPhase = new ArrayList<>();//编码阶段，度量元、子属性、属性的正互反矩阵list
    List<List<String>> matrixListsInTestPhase = new ArrayList<>();//测试阶段，度量元、子属性、属性的正互反矩阵list

    @Autowired
    private SoftDataService softDataService;

    @Autowired
    private MeasurementDataService measurementDataService;

    @Autowired
    private SoftwareProductService softwareProductService;

    //当前正在测评的软件产品
    SoftwareProduct softwareProductCurrent;
    String softwareNumber;

    List<SoftData> softDataListsInRequirePhase;
    List<SoftData> softDataListsInDesignPhase;
    List<SoftData> softDataListsInCodingPhase;
    List<SoftData> softDataListsInTestPhase;

//    List<ChartData> chartDataList = new ArrayList<>();
//    List<ChartData_SubAttribute> chartData_SubAttributeList = new ArrayList<>();

    Chart chart = new Chart();

    public  void getSoftwareProductCurrent(){
        softwareProductCurrent = softwareProductService.getByStatus(1);
        if(softwareProductCurrent != null){
            softwareNumber = softwareProductCurrent.getNumber();
        }
    }

    public Map<String,Map<String,Double>> getSubAttributeToAttributeKeXinDu(Map<String,List<String>> attributeMap, Map<String,Double> subAttributeKeXinDuMap ){
        Map<String,Map<String,Double>> subAttributeToAttributeKeXinDuMap = new LinkedHashMap<>();
        for(Map.Entry<String,List<String>> entry : attributeMap.entrySet()){
            Map<String,Double> subAttributeKeXinDuMap1 = new LinkedHashMap<>();
            String attribute = entry.getKey();
            List<String> subAttributeList = entry.getValue();
            for(int i = 0; i < subAttributeList.size(); i++){
                String subAttribute = subAttributeList.get(i);
                if(subAttributeKeXinDuMap.containsKey(subAttribute)){
                    subAttributeKeXinDuMap1.put(subAttribute, subAttributeKeXinDuMap.get(subAttribute));
                    subAttributeToAttributeKeXinDuMap.put(attribute, subAttributeKeXinDuMap1);
                }
            }
        }
        return subAttributeToAttributeKeXinDuMap;
    }


    @RequestMapping(value = "/requirePhase",method = RequestMethod.GET)
    public String softDataInputInRequirePhase(){
        return "softData/requirePhase/softDataList";
    }

    /**
     * 需求阶段 上传度量数据的excel文件
     * @param file
     * @return
     */
    @RequestMapping(value = "/requirePhase/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadRequirePhaseFile(@RequestParam("requirePhaseFile")MultipartFile file){
        getSoftwareProductCurrent();
        String uploadRes = new UploadFile().upload(file);
        if(softwareNumber == null){
            uploadRes = "请选择某一软件进行测评！";
            return uploadRes;
        }
        String fileName = file.getOriginalFilename();
        if(fileName.indexOf("\\") != -1){
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        String path = "src/main/resources/static/files/"+fileName;
        List<SoftData> softDataList = new ReadExcel().getAllData(path);
        try{
            if(softDataList == null){
                uploadRes = "上传的数据格式有误,请检查单元格y的值是否为0";
            }else{
                String softwareNumberInExcel = softDataList.get(0).getSoftwareNumber();
                if(!softwareNumber.equals(softwareNumberInExcel)){
                    uploadRes = "请上传当前测评软件的数据";
                }else{
                    softDataService.save(softDataList);
                }
            }
        }catch (MongoWriteException e){
            return uploadRes = "请检查excel表中是否有重复记录";
        }
        return uploadRes;
    }
    /**
     * 需求阶段度量数据
     * @param limit
     * @param offset
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/requirePhase/softDataList",method = RequestMethod.GET)
    public PageUtils softDataListInRequirePhase(@RequestParam(value = "limit", required = false) Integer limit,@RequestParam(value = "offset", required = false) Integer offset,@RequestParam(value = "curSoftwareNumber", required = false) String curSoftwareNumber){
        //查询列表数据
        //因为是服务端分页，返回值必须告诉前端总记录的条数total和当前页的记录数rows

        Page page = new Page(offset/limit+1, limit);
//        page = softDataService.findByPage(page);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {curSoftwareNumber,"需求阶段"};
        page = softDataService.findByPage(fields, values, page);
//        page = softDataService.findByPage(field,value,page);
        List<SoftData> softDataList = page.getList();
        int total = page.getTotalCount();
        PageUtils pageUtils = new PageUtils(softDataList, total);
        return pageUtils;
    }*/

    /**
     * 需求阶段度量数据
     * @param limit
     * @param offset
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/requirePhase/softDataList",method = RequestMethod.GET)
    public PageUtils softDataListInRequirePhase(@RequestParam(value = "limit", required = false) Integer limit,@RequestParam(value = "offset", required = false) Integer offset){
        //查询列表数据
        //因为是服务端分页，返回值必须告诉前端总记录的条数total和当前页的记录数rows
        getSoftwareProductCurrent();

        Page page = new Page(offset/limit+1, limit);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"需求阶段"};
        page = softDataService.findByPage(fields, values, page);
        List<SoftData> softDataList = page.getList();
        int total = page.getTotalCount();
        PageUtils pageUtils = new PageUtils(softDataList, total);
        return pageUtils;
    }

    /**
     * 需求阶段 增加度量数据 跳转到增加页面
     * @return
     */
    @RequestMapping(value = "/requirePhase/add",method = RequestMethod.GET)
    public String addSoftDataInRequirePhase(){
        return "softData/requirePhase/add";
    }

    /**
     * 需求阶段 保存度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/requirePhase/save",method = RequestMethod.POST)
    public ReturnUtil saveSoftDataInRequirePhase(SoftData softData){
        ReturnUtil returnUtil = new ReturnUtil();
        try{
            softDataService.save(softData);
        }catch(Exception e){
            returnUtil = ReturnUtil.error("不能输入重复的数据");
        }finally {
            return returnUtil;
        }
    }

    /**
     * 需求阶段 编辑度量数据 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/requirePhase/edit/{id}",method = RequestMethod.GET)
    public String editSoftDataInRequirePhase(@PathVariable("id") String id, Model model){
        SoftData softData = softDataService.findById(id);
        model.addAttribute("softData", softData);
        return "/softData/requirePhase/edit";
    }

    /**
     * 需求阶段 编辑度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/requirePhase/update",method = RequestMethod.POST)
    public ReturnUtil updateSoftDataInRequirePhase(SoftData softData){
        softDataService.update(softData);
        return ReturnUtil.ok();
    }

    /**
     * 需求阶段 删除度量数据
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/requirePhase/remove",method = RequestMethod.POST)
    public ReturnUtil removeSoftDataInRequirePhase(String id){
        softDataService.deleteById(id);
        return ReturnUtil.ok();
    }

    /**
     * 需求阶段 批量删除度量数据
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/requirePhase/batchRemove",method = RequestMethod.POST)
    public ReturnUtil batchRemoveSoftDataInRequirePhase(@RequestParam("ids[]") String[] ids){
        softDataService.deleteByIds(ids);
        return ReturnUtil.ok();
    }

    /**
     * 需求阶段 输入正互反矩阵(属性）
     * @param model
     * @return
     */
    @RequestMapping(value = "/requirePhase/inputAttributeMatrix",method = RequestMethod.GET)
    public String showAttributeInRequirePhase(Model model){
//        List<SoftData> softDataList = softDataService.find("phase","需求阶段");
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"需求阶段"};
        softDataListsInRequirePhase = softDataService.find(fields,values);
        Map<String,List<String>> resMap = new FindAll().findAllAttributeToEachPhase(softDataListsInRequirePhase);
        model.addAttribute("resMap", resMap);
        return "/softData/requirePhase/inputAttributeMatrix";
    }

    /**
     * 需求阶段 输入正互反矩阵（子属性）
     * 跳转到输入页面 resMap 是存储子属性的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/requirePhase/inputSubAttributeMatrix",method = RequestMethod.GET)
    public String showSubAttributeInRequirePhase(Model model){
//        List<SoftData> softDataList = softDataService.find("phase","需求阶段");
        Map<String,List<String>> resMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInRequirePhase);
        model.addAttribute("resMap", resMap);
        return "/softData/requirePhase/inputSubAttributeMatrix";
    }

    /**
     * 需求阶段 输入正互反矩阵（度量元）
     * 跳转到输入页面 resMap 是存储度量元的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/requirePhase/inputMetricElementMatrix",method = RequestMethod.GET)
    public String showMetricElementInRequirePhase(Model model){
        Map<String,List<String>> resMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInRequirePhase);
        model.addAttribute("resMap", resMap);
        return "/softData/requirePhase/inputMetricElementMatrix";
    }

    /**
     * 需求阶段 根据度量元，子属性，属性正互反矩阵分别计算权重
     * @param zhenghufanMatrix
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/requirePhase/countWeightByMatrix",method = RequestMethod.POST)
    public ReturnUtil countWeightByMatrixInRequirePhase(@RequestBody ZhenghufanMatrix zhenghufanMatrix){
        curZhenghufanMatrixInRequirePhase = zhenghufanMatrix;
        String metricElementMatrixArray = zhenghufanMatrix.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = zhenghufanMatrix.getSubAttributeMatrixArray();
        String attributeMatrixArray = zhenghufanMatrix.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
        List<SoftData> softDataList = softDataService.find("phase","需求阶段");
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataList);
        if(metricElementWeightMap == null){
            return ReturnUtil.error(1,"度量元正互反矩阵输入格式有误，请重新输入");
        }else if(metricElementWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"度量元正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataList);
        if(subAttributeWeightMap == null){
            return ReturnUtil.error(1,"子属性正互反矩阵输入格式有误，请重新输入");
        }else if(subAttributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"子属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataList);
        if(attributeWeightMap == null){
            return ReturnUtil.error(1,"属性正互反矩阵输入格式有误，请重新输入");
        }else if(attributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        return ReturnUtil.ok();
    }

    /**
     * 需求阶段 显示所有的权重
     * @return
     */
    @RequestMapping(value = "/requirePhase/displayWeight",method = RequestMethod.GET)
    public String displayWeightInRequirePhase(Model model){
        String metricElementMatrixArray = curZhenghufanMatrixInRequirePhase.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = curZhenghufanMatrixInRequirePhase.getSubAttributeMatrixArray();
        String attributeMatrixArray = curZhenghufanMatrixInRequirePhase.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
        if(matrixListsInRequirePhase.size() == 0){
            matrixListsInRequirePhase.add(metricElementMatrixList);
            matrixListsInRequirePhase.add(subAttributeMatrixList);
            matrixListsInRequirePhase.add(attributeMatrixList);
        }else{
            matrixListsInRequirePhase.set(0, metricElementMatrixList);
            matrixListsInRequirePhase.set(1, subAttributeMatrixList);
            matrixListsInRequirePhase.set(2, attributeMatrixList);
        }
//        List<SoftData> softDataList = softDataService.find("phase","需求阶段");
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInRequirePhase);
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInRequirePhase);
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInRequirePhase);

        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInRequirePhase);
        Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInRequirePhase);
        Map<String,Map<String,Double>> subAttributeToAttributeWeightMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeWeightMap);//每个属性对应子属性的权重
        Map<String,Map<String,Double>> metricElementToSubAttributeWeightMap = getSubAttributeToAttributeKeXinDu(subAttributeMap, metricElementWeightMap);//每个子属性对应度量元的权重
        model.addAttribute("subAttributeToAttributeWeightMap", subAttributeToAttributeWeightMap);
        model.addAttribute("metricElementToSubAttributeWeightMap", metricElementToSubAttributeWeightMap);
        model.addAttribute("metricElementWeightMap", metricElementWeightMap);
        model.addAttribute("subAttributeWeightMap", subAttributeWeightMap);
        model.addAttribute("attributeWeightMap", attributeWeightMap);
        return "/softData/requirePhase/displayWeight";
    }

    /**
     * 需求阶段 根据度量元的可信值和权重，计算子属性的可信值
     * 根据子属性的可信值和权重，计算属性的可信值
     * 根据属性的可信值和权重，计算阶段的可信值
     *
     * 分别将计算的可信值和权重存到数据库中
     * @param model
     * @return
     */
    @RequestMapping(value = "/requirePhase/countKeXinDuInRequirePhase",method = RequestMethod.GET)
    public String countKeXinDuInRequirePhase(Model model){
//        List<SoftData> softDataList = softDataService.find("phase","需求阶段");

        if(softDataListsInRequirePhase != null && matrixListsInRequirePhase.size() != 0){
            softwareNumber = softDataListsInRequirePhase.get(0).getSoftwareNumber();
            Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInRequirePhase);
            Map<String,Double> metricElementKeXinDuMap = new CountKeXinDu().countMetricElementKeXinDu(softDataListsInRequirePhase);

            Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement( matrixListsInRequirePhase.get(0), softDataListsInRequirePhase);
            Map<String,Double> subAttributeKeXinDuMap = new CountKeXinDu().countSubAttributeKeXinDu(subAttributeMap, metricElementKeXinDuMap, metricElementWeightMap);
            List<MeasurementData> measurementDataListForMetricElement = new MeasurementData().getMeasurementList(softwareNumber,"度量元", "需求阶段",metricElementKeXinDuMap, metricElementWeightMap);
            measurementDataService.upsert(measurementDataListForMetricElement);

            Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInRequirePhase);
            Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute( matrixListsInRequirePhase.get(1), softDataListsInRequirePhase);
            Map<String,Double> attributeKeXinDuMap = new CountKeXinDu().countParentKeXinDu(attributeMap, subAttributeKeXinDuMap, subAttributeWeightMap);
            List<MeasurementData> measurementDataListForSubAttribute = new MeasurementData().getMeasurementList(softwareNumber,"子属性", "需求阶段",subAttributeKeXinDuMap, subAttributeWeightMap);
            measurementDataService.upsert(measurementDataListForSubAttribute);

            Map<String,Map<String,Double>> subAttributeToAttributeKeXinDuMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeKeXinDuMap);

            Map<String,List<String>> phaseMap = new FindAll().findAllAttributeToEachPhase(softDataListsInRequirePhase);
            Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute( matrixListsInRequirePhase.get(2), softDataListsInRequirePhase);
            Map<String,Double> phaseKeXinDuMap = new CountKeXinDu().countParentKeXinDu(phaseMap, attributeKeXinDuMap, attributeWeightMap);
            List<MeasurementData> measurementDataListForAttribute = new MeasurementData().getMeasurementList(softwareNumber,"属性", "需求阶段",attributeKeXinDuMap, attributeWeightMap);
            measurementDataService.upsert(measurementDataListForAttribute);

            MeasurementData measurementDataForPhase = new MeasurementData(softwareNumber,"需求阶段","阶段", "需求阶段",phaseKeXinDuMap.get("需求阶段"),Double.valueOf(0.25));
            measurementDataService.upsert(measurementDataForPhase);

            chart.setAttributeKeXinDuMapInRequirePhase(attributeKeXinDuMap);

//            ChartData chartDataInRequirePhase = new ChartData(attributeKeXinDuMap);
//            chartDataInRequirePhase.setPhase("需求阶段");
//            chartDataList.add(chartDataInRequirePhase);
//
//            ChartData_SubAttribute chartData_subAttributeInRequirePhase = new ChartData_SubAttribute(subAttributeKeXinDuMap);
//            chartData_subAttributeInRequirePhase.setPhase("需求阶段");
//            chartData_SubAttributeList.add(chartData_subAttributeInRequirePhase);

            model.addAttribute("subAttributeToAttributeKeXinDuMap", subAttributeToAttributeKeXinDuMap);
            model.addAttribute("subAttributeKeXinDuMap", subAttributeKeXinDuMap);
            model.addAttribute("attributeKeXinDuMap", attributeKeXinDuMap);
            model.addAttribute("phaseKeXinDuMap", phaseKeXinDuMap);


            return "/softData/requirePhase/displayKeXinDu";
        }else{
            return "/softData/noCountWeightError";
        }
    }

    /**
     * 设计阶段
     * @return
     *//*
    @RequestMapping(value = "/designPhase",method = RequestMethod.GET)
    public String softDataInputInDesignPhase(){
        return "softData/designPhase/softDataList";
    }

    @RequestMapping(value = "/designPhase/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadDesignPhaseFile(@RequestParam("designPhaseFile")MultipartFile file){
        String uploadRes = new UploadFile().upload(file);
        String path = "src/main/resources/static/files/designPhase.xlsx";
        List<SoftData> softDataList = new ReadExcel().getAllData(path);
        if(softDataList == null){
            uploadRes = "上传的数据格式有误,请检查单元格内容是否有空值或者y的值是否为0";
        }else{
            softDataService.save(softDataList);
        }
        return uploadRes;
    }*/

    /**
     * 设计阶段
     * @return
     */
    @RequestMapping(value = "/designPhase",method = RequestMethod.GET)
    public String softDataInputInDesignPhase(){
        return "softData/designPhase/softDataList";
    }

    @RequestMapping(value = "/designPhase/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadDesignPhaseFile(@RequestParam("designPhaseFile")MultipartFile file){
        getSoftwareProductCurrent();

        String uploadRes = new UploadFile().upload(file);
        if(softwareNumber == null){
            uploadRes = "请选择某一软件进行测评！";
            return uploadRes;
        }
        String fileName = file.getOriginalFilename();
        if(fileName.indexOf("\\") != -1){
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        String path = "src/main/resources/static/files/"+fileName;
        List<SoftData> softDataList = new ReadExcel().getAllData(path);
        if(softDataList == null){
            uploadRes = "上传的数据格式有误,请检查单元格内容是否有空值或者y的值是否为0";
        }else{
            String softwareNumberInExcel = softDataList.get(0).getSoftwareNumber();
            if(!softwareNumber.equals(softwareNumberInExcel)){
                uploadRes = "请上传当前测评软件的数据";
            }else{
                softDataService.save(softDataList);
            }
        }
        return uploadRes;
    }

    /**
     * 设计阶段 查询度量数据
     * @param limit
     * @param offset
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/designPhase/softDataList",method = RequestMethod.GET)
    public PageUtils softDataListInDesignPhase(@RequestParam(value = "limit", required = false) Integer limit,@RequestParam(value = "offset", required = false) Integer offset){
        //查询列表数据
        //因为是服务端分页，返回值必须告诉前端总记录的条数total和当前页的记录数rows
        getSoftwareProductCurrent();

        Page page = new Page(offset/limit+1, limit);
//        String field = "phase";
//        String value = "设计阶段";
//        page = softDataService.findByPage(field,value,page);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"设计阶段"};
        page = softDataService.findByPage(fields, values, page);

        List<SoftData> softDataList = page.getList();
        int total = page.getTotalCount();
        PageUtils pageUtils = new PageUtils(softDataList, total);
        return pageUtils;
    }

    /**
     * 设计阶段 增加度量数据 跳转到增加页面
     * @return
     */
    @RequestMapping(value = "/designPhase/add",method = RequestMethod.GET)
    public String addSoftDataInDesignPhase(){
        return "softData/designPhase/add";
    }

    /**
     * 设计阶段 保存度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/designPhase/save",method = RequestMethod.POST)
    public ReturnUtil saveSoftDataInDesignPhase(SoftData softData){
        softDataService.save(softData);
        return ReturnUtil.ok();
    }

    /**
     * 设计阶段 编辑度量数据 跳转到编辑页面
     * @param editId
     * @param model
     * @return
     */
    @RequestMapping(value = "/designPhase/edit/{editId}",method = RequestMethod.GET)
    public String editSoftDataInDesignPhase(@PathVariable("editId") String editId, Model model){
        SoftData softData = softDataService.findById(editId);
        model.addAttribute("softData", softData);
        return "/softData/designPhase/edit";
    }

    /**
     * 设计阶段 编辑度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/designPhase/update",method = RequestMethod.POST)
    public ReturnUtil updateSoftDataInDesignPhase(SoftData softData){
        softDataService.update(softData);
        return ReturnUtil.ok();
    }

    /**
     * 设计阶段 删除度量数据
     * @param removeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/designPhase/remove",method = RequestMethod.POST)
    public ReturnUtil removeSoftDataInDesignPhase(String removeId){
        softDataService.deleteById(removeId);
        return ReturnUtil.ok();
    }

    /**
     * 设计阶段 批量删除度量数据
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/designPhase/batchRemove",method = RequestMethod.POST)
    public ReturnUtil batchRemoveSoftDataInDesignPhase(@RequestParam("ids[]") String[] ids){
        softDataService.deleteByIds(ids);
        return ReturnUtil.ok();
    }

    /**
     * 设计阶段 输入正互反矩阵(属性）
     * @param model
     * @return
     */
    @RequestMapping(value = "/designPhase/inputAttributeMatrix",method = RequestMethod.GET)
    public String showAttributeInDesignPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"设计阶段"};
        softDataListsInDesignPhase = softDataService.find(fields,values);
        Map<String,List<String>> resMap = new FindAll().findAllAttributeToEachPhase(softDataListsInDesignPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/designPhase/inputAttributeMatrix";
    }

    /**
     * 设计阶段 输入正互反矩阵（子属性）
     * 跳转到输入页面 resMap 是存储子属性的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/designPhase/inputSubAttributeMatrix",method = RequestMethod.GET)
    public String showSubAttributeInDesignPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,List<String>> resMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInDesignPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/designPhase/inputSubAttributeMatrix";
    }

    /**
     * 设计阶段 输入正互反矩阵（度量元）
     * 跳转到输入页面 resMap 是存储度量元的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/designPhase/inputMetricElementMatrix",method = RequestMethod.GET)
    public String showMetricElementInDesignPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,List<String>> resMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInDesignPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/designPhase/inputMetricElementMatrix";
    }


    /**
     * 设计阶段 根据度量元，子属性，属性的正互反矩阵分别计算权重
     * @param zhenghufanMatrix
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/designPhase/countWeightByMatrix",method = RequestMethod.POST)
    public ReturnUtil countWeightByMatrixInDesignPhase(@RequestBody ZhenghufanMatrix zhenghufanMatrix){
        curZhenghufanMatrixInDesignPhase = zhenghufanMatrix;
        String metricElementMatrixArray = zhenghufanMatrix.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = zhenghufanMatrix.getSubAttributeMatrixArray();
        String attributeMatrixArray = zhenghufanMatrix.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInDesignPhase);
        if(metricElementWeightMap == null){
            return ReturnUtil.error(1,"度量元正互反矩阵输入格式有误，请重新输入");
        }else if(metricElementWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"度量元正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInDesignPhase);
        if(subAttributeWeightMap == null){
            return ReturnUtil.error(1,"子属性正互反矩阵输入格式有误，请重新输入");
        }else if(subAttributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"子属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInDesignPhase);
        if(attributeWeightMap == null){
            return ReturnUtil.error(1,"属性正互反矩阵输入格式有误，请重新输入");
        }else if(attributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        return ReturnUtil.ok();
    }

    /**
     * 设计阶段 显示所有的权重
     * @return
     */
    @RequestMapping(value = "/designPhase/displayWeight",method = RequestMethod.GET)
    public String displayWeightInDesignPhase(Model model){
        String metricElementMatrixArray = curZhenghufanMatrixInDesignPhase.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = curZhenghufanMatrixInDesignPhase.getSubAttributeMatrixArray();
        String attributeMatrixArray = curZhenghufanMatrixInDesignPhase.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
        if(matrixListsInDesignPhase.size() == 0){
            matrixListsInDesignPhase.add(metricElementMatrixList);
            matrixListsInDesignPhase.add(subAttributeMatrixList);
            matrixListsInDesignPhase.add(attributeMatrixList);
        }else{
            matrixListsInDesignPhase.set(0, metricElementMatrixList);
            matrixListsInDesignPhase.set(1, subAttributeMatrixList);
            matrixListsInDesignPhase.set(2, attributeMatrixList);
        }

//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInDesignPhase);
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInDesignPhase);
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInDesignPhase);
        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInDesignPhase);
        Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInDesignPhase);
        Map<String,Map<String,Double>> subAttributeToAttributeWeightMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeWeightMap);//每个属性对应子属性的权重
        Map<String,Map<String,Double>> metricElementToSubAttributeWeightMap = getSubAttributeToAttributeKeXinDu(subAttributeMap, metricElementWeightMap);//每个子属性对应度量元的权重
        model.addAttribute("subAttributeToAttributeWeightMap", subAttributeToAttributeWeightMap);
        model.addAttribute("metricElementToSubAttributeWeightMap", metricElementToSubAttributeWeightMap);
        model.addAttribute("metricElementWeightMap", metricElementWeightMap);
        model.addAttribute("subAttributeWeightMap", subAttributeWeightMap);
        model.addAttribute("attributeWeightMap", attributeWeightMap);
        return "/softData/designPhase/displayWeight";
    }

    /**
     * 设计阶段 根据度量元的可信值和权重，计算子属性的可信值
     * 根据子属性的可信值和权重，计算属性的可信值
     * 根据属性的可信值和权重，计算阶段的可信值
     * @param model
     * @return
     */
    @RequestMapping(value = "/designPhase/countKeXinDuInDesignPhase",method = RequestMethod.GET)
    public String countKeXinDuInDesignPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        if(softDataListsInDesignPhase != null && matrixListsInDesignPhase.size() != 0){
            softwareNumber = softDataListsInDesignPhase.get(0).getSoftwareNumber();
            Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInDesignPhase);
            Map<String,Double> metricElementKeXinDuMap = new CountKeXinDu().countMetricElementKeXinDu(softDataListsInDesignPhase);
            Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement( matrixListsInDesignPhase.get(0), softDataListsInDesignPhase);
            Map<String,Double> subAttributeKeXinDuMap = new CountKeXinDu().countSubAttributeKeXinDu(subAttributeMap, metricElementKeXinDuMap, metricElementWeightMap);
            List<MeasurementData> measurementDataListForMetricElement = new MeasurementData().getMeasurementList(softwareNumber,"度量元", "设计阶段",metricElementKeXinDuMap, metricElementWeightMap);
            measurementDataService.upsert(measurementDataListForMetricElement);

            Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInDesignPhase);
            Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute( matrixListsInDesignPhase.get(1), softDataListsInDesignPhase);
            Map<String,Double> attributeKeXinDuMap = new CountKeXinDu().countParentKeXinDu(attributeMap, subAttributeKeXinDuMap, subAttributeWeightMap);
            List<MeasurementData> measurementDataListForSubAttribute = new MeasurementData().getMeasurementList(softwareNumber,"子属性", "设计阶段",subAttributeKeXinDuMap, subAttributeWeightMap);
            measurementDataService.upsert(measurementDataListForSubAttribute);

            Map<String,Map<String,Double>> subAttributeToAttributeKeXinDuMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeKeXinDuMap);

            Map<String,List<String>> phaseMap = new FindAll().findAllAttributeToEachPhase(softDataListsInDesignPhase);
            Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute( matrixListsInDesignPhase.get(2), softDataListsInDesignPhase);
            Map<String,Double> phaseKeXinDuMap = new CountKeXinDu().countParentKeXinDu(phaseMap, attributeKeXinDuMap, attributeWeightMap);
            List<MeasurementData> measurementDataListForAttribute = new MeasurementData().getMeasurementList(softwareNumber,"属性", "设计阶段",attributeKeXinDuMap, attributeWeightMap);
            measurementDataService.upsert(measurementDataListForAttribute);

            MeasurementData measurementDataForPhase = new MeasurementData(softwareNumber,"设计阶段","阶段", "设计阶段",phaseKeXinDuMap.get("设计阶段"),Double.valueOf(0.25));
            measurementDataService.upsert(measurementDataForPhase);

            chart.setAttributeKeXinDuMapInDesignPhase(attributeKeXinDuMap);

//            ChartData chartDataInDesignPhase = new ChartData(attributeKeXinDuMap);
//            chartDataInDesignPhase.setPhase("设计阶段");
//            chartDataList.add(chartDataInDesignPhase);
//
//            ChartData_SubAttribute chartData_subAttributeInDesignPhase = new ChartData_SubAttribute(subAttributeKeXinDuMap);
//            chartData_subAttributeInDesignPhase.setPhase("设计阶段");
//            chartData_SubAttributeList.add(chartData_subAttributeInDesignPhase);
            model.addAttribute("subAttributeToAttributeKeXinDuMap", subAttributeToAttributeKeXinDuMap);
            model.addAttribute("subAttributeKeXinDuMap", subAttributeKeXinDuMap);
            model.addAttribute("attributeKeXinDuMap", attributeKeXinDuMap);
            model.addAttribute("phaseKeXinDuMap", phaseKeXinDuMap);
            return "/softData/designPhase/displayKeXinDu";
        }else{
            return "/softData/noCountWeightError";
        }

    }

    /**
     * 设计阶段 子属性可信值图表显示
     * @return
     */
    @RequestMapping(value = "/designPhase/showCharts",method = RequestMethod.GET)
    public String showChartsInDesignPhase(){
        return "softData/requirePhase/barChart_SubAttributeKeXinDu";
    }

    /**
     * 编码阶段
     * @return
     */
    @RequestMapping(value = "/codingPhase",method = RequestMethod.GET)
    public String softDataInputInCodingPhase(){
        return "softData/codingPhase/softDataList";
    }

    /**
     * 编码阶段
     * 上传EXCEL文件，并判断上传的数据是否满足要求
     * @param file
     * @return
     */
    @RequestMapping(value = "/codingPhase/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadCodingPhaseFile(@RequestParam("codingPhaseFile")MultipartFile file){
        getSoftwareProductCurrent();

        String uploadRes = new UploadFile().upload(file);
        if(softwareNumber == null){
            uploadRes = "请选择某一软件进行测评！";
            return uploadRes;
        }
        String fileName = file.getOriginalFilename();
        if(fileName.indexOf("\\") != -1){
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        String path = "src/main/resources/static/files/"+fileName;
        List<SoftData> softDataList = new ReadExcel().getAllData(path);
        if(softDataList == null){
            uploadRes = "上传的数据格式有误,请检查是否有空值或者y的值是否为0";
        }else{
            String softwareNumberInExcel = softDataList.get(0).getSoftwareNumber();
            if(!softwareNumber.equals(softwareNumberInExcel)){
                uploadRes = "请上传当前测评软件的数据";
            }else{
                softDataService.save(softDataList);
            }
        }
        return uploadRes;
    }

    /**
     * 编码阶段 查询度量数据
     * @param limit
     * @param offset
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/codingPhase/softDataList",method = RequestMethod.GET)
    public PageUtils softDataListInCodingPhase(@RequestParam(value = "limit", required = false) Integer limit,@RequestParam(value = "offset", required = false) Integer offset){
        //查询列表数据
        //因为是服务端分页，返回值必须告诉前端总记录的条数total和当前页的记录数rows
        getSoftwareProductCurrent();

        Page page = new Page(offset/limit+1, limit);
//        String field = "phase";
//        String value = "编码阶段";
//        page = softDataService.findByPage(field,value,page);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"编码阶段"};
        page = softDataService.findByPage(fields, values, page);

        List<SoftData> softDataList = page.getList();
        int total = page.getTotalCount();
        PageUtils pageUtils = new PageUtils(softDataList, total);
        return pageUtils;
    }

    /**
     * 编码阶段 增加度量数据 跳转到增加页面
     * @return
     */
    @RequestMapping(value = "/codingPhase/add",method = RequestMethod.GET)
    public String addSoftDataInCodingPhase(){
        return "softData/codingPhase/add";
    }

    /**
     * 编码阶段 保存度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/codingPhase/save",method = RequestMethod.POST)
    public ReturnUtil saveSoftDataInCodingPhase(SoftData softData){
        softDataService.save(softData);
        return ReturnUtil.ok();
    }

    /**
     * 编码阶段 编辑度量数据 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/codingPhase/edit/{id}",method = RequestMethod.GET)
    public String editSoftDataInCodingPhase(@PathVariable("id") String id, Model model){
        SoftData softData = softDataService.findById(id);
        model.addAttribute("softData", softData);
        return "/softData/codingPhase/edit";
    }

    /**
     * 编码阶段 编辑度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/codingPhase/update",method = RequestMethod.POST)
    public ReturnUtil updateSoftDataInCodingPhase(SoftData softData){
        softDataService.update(softData);
        return ReturnUtil.ok();
    }

    /**
     * 编码阶段 删除度量数据
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/codingPhase/remove",method = RequestMethod.POST)
    public ReturnUtil removeSoftDataInCodingPhase(String id){
        softDataService.deleteById(id);
        return ReturnUtil.ok();
    }

    /**
     * 编码阶段 批量删除度量数据
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/codingPhase/batchRemove",method = RequestMethod.POST)
    public ReturnUtil batchRemoveSoftDataInCodingPhase(@RequestParam("ids[]") String[] ids){
        softDataService.deleteByIds(ids);
        return ReturnUtil.ok();
    }

    /**
     * 编码阶段 输入正互反矩阵(属性）
     * @param model
     * @return
     */
    @RequestMapping(value = "/codingPhase/inputAttributeMatrix",method = RequestMethod.GET)
    public String showAttributeInCodingPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"编码阶段"};
        softDataListsInCodingPhase = softDataService.find(fields, values);
        Map<String,List<String>> resMap = new FindAll().findAllAttributeToEachPhase(softDataListsInCodingPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/codingPhase/inputAttributeMatrix";
    }

    /**
     * 编码阶段 输入正互反矩阵（子属性）
     * 跳转到输入页面 resMap 是存储子属性的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/codingPhase/inputSubAttributeMatrix",method = RequestMethod.GET)
    public String showSubAttributeInCodingPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,List<String>> resMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInCodingPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/codingPhase/inputSubAttributeMatrix";
    }

    /**
     * 编码阶段 输入正互反矩阵（度量元）
     * 跳转到输入页面 resMap 是存储度量元的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/codingPhase/inputMetricElementMatrix",method = RequestMethod.GET)
    public String showMetricElementInCodingPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,List<String>> resMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInCodingPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/codingPhase/inputMetricElementMatrix";
    }


    /**
     * 编码阶段 根据度量元，子属性，属性的正互反矩阵分别计算权重
     * @param zhenghufanMatrix
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/codingPhase/countWeightByMatrix",method = RequestMethod.POST)
    public ReturnUtil countWeightByMatrixInCodingPhase(@RequestBody ZhenghufanMatrix zhenghufanMatrix){
        curZhenghufanMatrixInCodingPhase = zhenghufanMatrix;
        String metricElementMatrixArray = zhenghufanMatrix.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = zhenghufanMatrix.getSubAttributeMatrixArray();
        String attributeMatrixArray = zhenghufanMatrix.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInCodingPhase);
        if(metricElementWeightMap == null){
            return ReturnUtil.error(1,"度量元正互反矩阵输入格式有误，请重新输入");
        }else if(metricElementWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"度量元正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInCodingPhase);
        if(subAttributeWeightMap == null){
            return ReturnUtil.error(1,"子属性正互反矩阵输入格式有误，请重新输入");
        }else if(subAttributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"子属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInCodingPhase);
        if(attributeWeightMap == null){
            return ReturnUtil.error(1,"属性正互反矩阵输入格式有误，请重新输入");
        }else if(attributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        return ReturnUtil.ok();
    }

    /**
     * 编码阶段 显示所有的权重
     * @return
     */
    @RequestMapping(value = "/codingPhase/displayWeight",method = RequestMethod.GET)
    public String displayWeightInCodingPhase(Model model){
        String metricElementMatrixArray = curZhenghufanMatrixInCodingPhase.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = curZhenghufanMatrixInCodingPhase.getSubAttributeMatrixArray();
        String attributeMatrixArray = curZhenghufanMatrixInCodingPhase.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
        if(matrixListsInCodingPhase.size() == 0){
            matrixListsInCodingPhase.add(metricElementMatrixList);
            matrixListsInCodingPhase.add(subAttributeMatrixList);
            matrixListsInCodingPhase.add(attributeMatrixList);
        }else{
            matrixListsInCodingPhase.set(0, metricElementMatrixList);
            matrixListsInCodingPhase.set(1, subAttributeMatrixList);
            matrixListsInCodingPhase.set(2, attributeMatrixList);
        }

//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInCodingPhase);
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInCodingPhase);
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInCodingPhase);

        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInCodingPhase);
        Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInCodingPhase);
        Map<String,Map<String,Double>> subAttributeToAttributeWeightMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeWeightMap);//每个属性对应子属性的权重
        Map<String,Map<String,Double>> metricElementToSubAttributeWeightMap = getSubAttributeToAttributeKeXinDu(subAttributeMap, metricElementWeightMap);//每个子属性对应度量元的权重
        model.addAttribute("subAttributeToAttributeWeightMap", subAttributeToAttributeWeightMap);
        model.addAttribute("metricElementToSubAttributeWeightMap", metricElementToSubAttributeWeightMap);
        model.addAttribute("metricElementWeightMap", metricElementWeightMap);
        model.addAttribute("subAttributeWeightMap", subAttributeWeightMap);
        model.addAttribute("attributeWeightMap", attributeWeightMap);
        return "/softData/designPhase/displayWeight";
    }

    /**
     * 编码阶段 根据度量元的可信值和权重，计算子属性的可信值
     * 根据子属性的可信值和权重，计算属性的可信值
     * 根据属性的可信值和权重，计算阶段的可信值
     * @param model
     * @return
     */
    @RequestMapping(value = "/codingPhase/countKeXinDuInCodingPhase",method = RequestMethod.GET)
    public String countKeXinDuInCodingPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        if(softDataListsInCodingPhase != null && matrixListsInCodingPhase.size() != 0){
            softwareNumber = softDataListsInCodingPhase.get(0).getSoftwareNumber();
            Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInCodingPhase);
            Map<String,Double> metricElementKeXinDuMap = new CountKeXinDu().countMetricElementKeXinDu(softDataListsInCodingPhase);
            Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement( matrixListsInCodingPhase.get(0), softDataListsInCodingPhase);
            Map<String,Double> subAttributeKeXinDuMap = new CountKeXinDu().countSubAttributeKeXinDu(subAttributeMap, metricElementKeXinDuMap, metricElementWeightMap);
            List<MeasurementData> measurementDataListForMetricElement = new MeasurementData().getMeasurementList(softwareNumber,"度量元", "编码阶段",metricElementKeXinDuMap, metricElementWeightMap);
            measurementDataService.upsert(measurementDataListForMetricElement);

            Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInCodingPhase);
            Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute( matrixListsInCodingPhase.get(1), softDataListsInCodingPhase);
            Map<String,Double> attributeKeXinDuMap = new CountKeXinDu().countParentKeXinDu(attributeMap, subAttributeKeXinDuMap, subAttributeWeightMap);
            List<MeasurementData> measurementDataListForSubAttribute = new MeasurementData().getMeasurementList(softwareNumber,"子属性", "编码阶段",subAttributeKeXinDuMap, subAttributeWeightMap);
            measurementDataService.upsert(measurementDataListForSubAttribute);

            Map<String,Map<String,Double>> subAttributeToAttributeKeXinDuMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeKeXinDuMap);

            Map<String,List<String>> phaseMap = new FindAll().findAllAttributeToEachPhase(softDataListsInCodingPhase);
            Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute( matrixListsInCodingPhase.get(2), softDataListsInCodingPhase);
            Map<String,Double> phaseKeXinDuMap = new CountKeXinDu().countParentKeXinDu(phaseMap, attributeKeXinDuMap, attributeWeightMap);
            List<MeasurementData> measurementDataListForAttribute = new MeasurementData().getMeasurementList(softwareNumber,"属性", "编码阶段",attributeKeXinDuMap, attributeWeightMap);
            measurementDataService.upsert(measurementDataListForAttribute);

            MeasurementData measurementDataForPhase = new MeasurementData(softwareNumber,"编码阶段","阶段", "编码阶段",phaseKeXinDuMap.get("编码阶段"),Double.valueOf(0.25));
            measurementDataService.upsert(measurementDataForPhase);

            chart.setAttributeKeXinDuMapInCodingPhase(attributeKeXinDuMap);

//            ChartData chartDataInCodingPhase = new ChartData(attributeKeXinDuMap);
//            chartDataInCodingPhase.setPhase("编码阶段");
//            chartDataList.add(chartDataInCodingPhase);
//
//            ChartData_SubAttribute chartData_subAttributeInCodingPhase = new ChartData_SubAttribute(subAttributeKeXinDuMap);
//            chartData_subAttributeInCodingPhase.setPhase("编码阶段");
//            chartData_SubAttributeList.add(chartData_subAttributeInCodingPhase);

            model.addAttribute("subAttributeKeXinDuMap", subAttributeKeXinDuMap);
            model.addAttribute("subAttributeToAttributeKeXinDuMap", subAttributeToAttributeKeXinDuMap);
            model.addAttribute("attributeKeXinDuMap", attributeKeXinDuMap);
            model.addAttribute("phaseKeXinDuMap", phaseKeXinDuMap);
            return "/softData/codingPhase/displayKeXinDu";
        }else{
            return "/softData/noCountWeightError";
        }

    }

    /**
     * 编码阶段 子属性可信值图表显示
     * @return
     */
    @RequestMapping(value = "/codingPhase/showCharts",method = RequestMethod.GET)
    public String showChartsInCodingPhase(){
        return "softData/codingPhase/barChart_SubAttributeKeXinDu";
    }

    /**
     * 测试阶段
     * @return
     */
    @RequestMapping(value = "/testPhase",method = RequestMethod.GET)
    public String softDataInputInTestPhase(){
        return "softData/testPhase/softDataList";
    }

    @RequestMapping(value = "/testPhase/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadTestPhaseFile(@RequestParam("testPhaseFile") MultipartFile file){
        getSoftwareProductCurrent();

        String uploadRes = new UploadFile().upload(file);
        if(softwareNumber == null){
            uploadRes = "请选择某一软件进行测评！";
            return uploadRes;
        }
        String fileName = file.getOriginalFilename();
        if(fileName.indexOf("\\") != -1){
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        String path = "src/main/resources/static/files/"+fileName;
        List<SoftData> softDataList = new ReadExcel().getAllData(path);
        if(softDataList == null){
            uploadRes = "上传的数据格式有误,请检查单元格内容是否有空值或者y的值是否为0";
        }else{
            String softwareNumberInExcel = softDataList.get(0).getSoftwareNumber();
            if(!softwareNumber.equals(softwareNumberInExcel)){
                uploadRes = "请上传当前测评软件的数据";
            }else{
                softDataService.save(softDataList);
            }
        }
        return uploadRes;
    }

    /**
     * 测试阶段 查询度量数据
     * @param limit
     * @param offset
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testPhase/softDataList",method = RequestMethod.GET)
    public PageUtils softDataListInTestPhase(@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "offset", required = false) Integer offset){
        //查询列表数据
        //因为是服务端分页，返回值必须告诉前端总记录的条数total和当前页的记录数rows
        getSoftwareProductCurrent();

        Page page = new Page(offset/limit+1, limit);
//        page = softDataService.findByPage(page);
//        String field = "phase";
//        String value = "测试阶段";
//        page = softDataService.findByPage(field,value,page);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"测试阶段"};
        page = softDataService.findByPage(fields, values, page);

        List<SoftData> softDataList = page.getList();
        int total = page.getTotalCount();
        PageUtils pageUtils = new PageUtils(softDataList, total);
        return pageUtils;
    }

    /**
     * 测试阶段 增加度量数据 跳转到增加页面
     * @return
     */
    @RequestMapping(value = "/testPhase/add",method = RequestMethod.GET)
    public String addSoftDataInTestPhase(){
        return "softData/testPhase/add";
    }

    /**
     * 测试阶段 保存度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testPhase/save",method = RequestMethod.POST)
    public ReturnUtil saveSoftDataInTestPhase(SoftData softData){
        softDataService.save(softData);
        return ReturnUtil.ok();
    }

    /**
     * 测试阶段 编辑度量数据 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/testPhase/edit/{id}",method = RequestMethod.GET)
    public String editSoftDataInTestPhase(@PathVariable("id") String id, Model model){
        SoftData softData = softDataService.findById(id);
        model.addAttribute("softData", softData);
        return "/softData/testPhase/edit";
    }

    /**
     * 测试阶段 编辑度量数据
     * @param softData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testPhase/update",method = RequestMethod.POST)
    public ReturnUtil updateSoftDataInTestPhase(SoftData softData){
        softDataService.update(softData);
        return ReturnUtil.ok();
    }

    /**
     * 测试阶段 删除度量数据
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testPhase/remove",method = RequestMethod.POST)
    public ReturnUtil removeSoftDataInTestPhase(String id){
        softDataService.deleteById(id);
        return ReturnUtil.ok();
    }

    /**
     * 测试阶段 批量删除度量数据
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testPhase/batchRemove",method = RequestMethod.POST)
    public ReturnUtil batchRemoveSoftDataInTestPhase(@RequestParam("ids[]") String[] ids){
        softDataService.deleteByIds(ids);
        return ReturnUtil.ok();
    }



    /**
     * 测试阶段 输入正互反矩阵(属性）
     * @param model
     * @return
     */
    @RequestMapping(value = "/testPhase/inputAttributeMatrix",method = RequestMethod.GET)
    public String showAttributeInTestPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        String[] fields = {"softwareNumber","phase"};
        String[] values = {softwareNumber,"测试阶段"};
        softDataListsInTestPhase = softDataService.find(fields, values);
        Map<String,List<String>> resMap = new FindAll().findAllAttributeToEachPhase(softDataListsInTestPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/testPhase/inputAttributeMatrix";
    }

    /**
     * 测试阶段 输入正互反矩阵（子属性）
     * 跳转到输入页面 resMap 是存储子属性的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/testPhase/inputSubAttributeMatrix",method = RequestMethod.GET)
    public String showSubAttributeInTestPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,List<String>> resMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInTestPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/testPhase/inputSubAttributeMatrix";
    }

    /**
     * 测试阶段 输入正互反矩阵（度量元）
     * 跳转到输入页面 resMap 是存储度量元的哈希表
     * @param model
     * @return
     */
    @RequestMapping(value = "/testPhase/inputMetricElementMatrix",method = RequestMethod.GET)
    public String showMetricElementInTestPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,List<String>> resMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInTestPhase);
        model.addAttribute("resMap", resMap);
        return "/softData/testPhase/inputMetricElementMatrix";
    }

    /**
     * 设计阶段 根据度量元，子属性，属性的正互反矩阵分别计算权重
     * @param zhenghufanMatrix
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testPhase/countWeightByMatrix",method = RequestMethod.POST)
    public ReturnUtil countWeightByMatrixInTestPhase(@RequestBody ZhenghufanMatrix zhenghufanMatrix){
        curZhenghufanMatrixInTestPhase = zhenghufanMatrix;
        String metricElementMatrixArray = zhenghufanMatrix.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = zhenghufanMatrix.getSubAttributeMatrixArray();
        String attributeMatrixArray = zhenghufanMatrix.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInTestPhase);
        if(metricElementWeightMap == null){
            return ReturnUtil.error(1,"度量元正互反矩阵输入格式有误，请重新输入");
        }else if(metricElementWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"度量元正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInTestPhase);
        if(subAttributeWeightMap == null){
            return ReturnUtil.error(1,"子属性正互反矩阵输入格式有误，请重新输入");
        }else if(subAttributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"子属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInTestPhase);
        if(attributeWeightMap == null){
            return ReturnUtil.error(1,"属性正互反矩阵输入格式有误，请重新输入");
        }else if(attributeWeightMap.get("CR") != null){
            return ReturnUtil.error(1,"属性正互反矩阵没有通过一致性检验，请重新输入");
        }
        return ReturnUtil.ok();
    }

    /**
     * 测试阶段 显示所有的权重
     * @return
     */
    @RequestMapping(value = "/testPhase/displayWeight",method = RequestMethod.GET)
    public String displayWeightInTestPhase(Model model){
        String metricElementMatrixArray = curZhenghufanMatrixInTestPhase.getMetricElementMatrixArray();//前端传回的json字符串格式
        String subAttributeMatrixArray = curZhenghufanMatrixInTestPhase.getSubAttributeMatrixArray();
        String attributeMatrixArray = curZhenghufanMatrixInTestPhase.getAttributeMatrixArray();
        List<String> metricElementMatrixList = new HandleJsonArray().jsonToArray(metricElementMatrixArray);
        List<String> subAttributeMatrixList = new HandleJsonArray().jsonToArray(subAttributeMatrixArray);
        List<String> attributeMatrixList = new HandleJsonArray().jsonToArray(attributeMatrixArray);
        if(matrixListsInTestPhase.size() == 0){
            matrixListsInTestPhase.add(metricElementMatrixList);
            matrixListsInTestPhase.add(subAttributeMatrixList);
            matrixListsInTestPhase.add(attributeMatrixList);
        }else{
            matrixListsInTestPhase.set(0, metricElementMatrixList);
            matrixListsInTestPhase.set(1, subAttributeMatrixList);
            matrixListsInTestPhase.set(2, attributeMatrixList);
        }

//        List<SoftData> softDataList = softDataService.find(field,value);
        Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement(metricElementMatrixList, softDataListsInTestPhase);
        Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute(subAttributeMatrixList, softDataListsInTestPhase);
        Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute(attributeMatrixList, softDataListsInTestPhase);

        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInTestPhase);
        Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInTestPhase);
        Map<String,Map<String,Double>> subAttributeToAttributeWeightMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeWeightMap);//每个属性对应子属性的权重
        Map<String,Map<String,Double>> metricElementToSubAttributeWeightMap = getSubAttributeToAttributeKeXinDu(subAttributeMap, metricElementWeightMap);//每个子属性对应度量元的权重
        model.addAttribute("subAttributeToAttributeWeightMap", subAttributeToAttributeWeightMap);
        model.addAttribute("metricElementToSubAttributeWeightMap", metricElementToSubAttributeWeightMap);

        model.addAttribute("metricElementWeightMap", metricElementWeightMap);
        model.addAttribute("subAttributeWeightMap", subAttributeWeightMap);
        model.addAttribute("attributeWeightMap", attributeWeightMap);
        return "/softData/testPhase/displayWeight";
    }

    /**
     * 测试阶段 根据度量元的可信值和权重，计算子属性的可信值
     * 根据子属性的可信值和权重，计算属性的可信值
     * 根据属性的可信值和权重，计算阶段的可信值
     * @param model
     * @return
     */
    @RequestMapping(value = "/testPhase/countKeXinDuInTestPhase",method = RequestMethod.GET)
    public String countKeXinDuInTestPhase(Model model){
//        List<SoftData> softDataList = softDataService.find(field,value);
        if(softDataListsInTestPhase != null && matrixListsInTestPhase.size() != 0){
            softwareNumber = softDataListsInTestPhase.get(0).getSoftwareNumber();
            Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListsInTestPhase);
            Map<String,Double> metricElementKeXinDuMap = new CountKeXinDu().countMetricElementKeXinDu(softDataListsInTestPhase);
            Map<String,Double> metricElementWeightMap = new CountWeight().countWeight_MetricElement( matrixListsInTestPhase.get(0), softDataListsInTestPhase);
            Map<String,Double> subAttributeKeXinDuMap = new CountKeXinDu().countSubAttributeKeXinDu(subAttributeMap, metricElementKeXinDuMap, metricElementWeightMap);
            List<MeasurementData> measurementDataListForMetricElement = new MeasurementData().getMeasurementList(softwareNumber,"度量元", "测试阶段",metricElementKeXinDuMap, metricElementWeightMap);
            measurementDataService.upsert(measurementDataListForMetricElement);

            Map<String,List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListsInTestPhase);
            Map<String,Double> subAttributeWeightMap = new CountWeight().countWeight_SubAttribute( matrixListsInTestPhase.get(1), softDataListsInTestPhase);
            Map<String,Double> attributeKeXinDuMap = new CountKeXinDu().countParentKeXinDu(attributeMap, subAttributeKeXinDuMap, subAttributeWeightMap);
            List<MeasurementData> measurementDataListForSubAttribute = new MeasurementData().getMeasurementList(softwareNumber,"子属性", "测试阶段",subAttributeKeXinDuMap, subAttributeWeightMap);
            measurementDataService.upsert(measurementDataListForSubAttribute);

            Map<String,Map<String,Double>> subAttributeToAttributeKeXinDuMap = getSubAttributeToAttributeKeXinDu(attributeMap, subAttributeKeXinDuMap);

            Map<String,List<String>> phaseMap = new FindAll().findAllAttributeToEachPhase(softDataListsInTestPhase);
            Map<String,Double> attributeWeightMap = new CountWeight().countWeight_Attribute( matrixListsInTestPhase.get(2), softDataListsInTestPhase);
            Map<String,Double> phaseKeXinDuMap = new CountKeXinDu().countParentKeXinDu(phaseMap, attributeKeXinDuMap, attributeWeightMap);
            List<MeasurementData> measurementDataListForAttribute = new MeasurementData().getMeasurementList(softwareNumber,"属性", "测试阶段",attributeKeXinDuMap, attributeWeightMap);
            measurementDataService.upsert(measurementDataListForAttribute);

            MeasurementData measurementDataForPhase = new MeasurementData(softwareNumber,"测试阶段","阶段", "测试阶段",phaseKeXinDuMap.get("测试阶段"),Double.valueOf(0.25));
            measurementDataService.upsert(measurementDataForPhase);

            chart.setAttributeKeXinDuMapInTestPhase(attributeKeXinDuMap);

//            ChartData chartDataInTestPhase = new ChartData(attributeKeXinDuMap);
//            chartDataInTestPhase.setPhase("测试阶段");
//            chartDataList.add(chartDataInTestPhase);
//
//            ChartData_SubAttribute chartData_subAttributeInTestPhase = new ChartData_SubAttribute(subAttributeKeXinDuMap);
//            chartData_subAttributeInTestPhase.setPhase("测试阶段");
//            chartData_SubAttributeList.add(chartData_subAttributeInTestPhase);

            model.addAttribute("subAttributeKeXinDuMap", subAttributeKeXinDuMap);
            model.addAttribute("subAttributeToAttributeKeXinDuMap", subAttributeToAttributeKeXinDuMap);
            model.addAttribute("attributeKeXinDuMap", attributeKeXinDuMap);
            model.addAttribute("phaseKeXinDuMap", phaseKeXinDuMap);
            return "/softData/testPhase/displayKeXinDu";
        }else{
            return "/softData/noCountWeightError";
        }

    }

    /**
     * 测试阶段 子属性可信值图表显示
     * @return
     */
    @RequestMapping(value = "/testPhase/showCharts",method = RequestMethod.GET)
    public String showChartsInTestPhase(){
        return "softData/testPhase/barChart_SubAttributeKeXinDu";
    }

    /**
     * 显示雷达图
     * @param model
     * @return
     */
    /*@RequestMapping(value = "/showRadarOfAttributes",method = RequestMethod.GET)
    public String showRadar(Model model){
        model.addAttribute("chartDataList", chartDataList);
        model.addAttribute("chartData_SubAttributeList", chartData_SubAttributeList);
        return "softData/charts/radar.html";
    }*/

    /**
     * 显示雷达图
     * @param model
     * @return
     */
    @RequestMapping(value = "/showRadarOfAttributes",method = RequestMethod.GET)
    public String showRadar(Model model){
        List<MeasurementData> measurementDataList = measurementDataService.findAll();
        Map<String,Double> attributeKeXinDuRequire = new LinkedHashMap<>();
        Map<String,Double> attributeKeXinDuDesign = new LinkedHashMap<>();
        Map<String,Double> attributeKeXinDuCoding = new LinkedHashMap<>();
        Map<String,Double> attributeKeXinDuTest = new LinkedHashMap<>();

        for(int i = 0; i < measurementDataList.size(); i++){
            MeasurementData measurementData = measurementDataList.get(i);
            if(measurementData.getPhaseBelongsTo().equals("需求阶段") && measurementData.getType().equals("属性")){
                attributeKeXinDuRequire.put(measurementData.getName(), measurementData.getKeXinDu());
            }else if(measurementData.getPhaseBelongsTo().equals("设计阶段") && measurementData.getType().equals("属性")){
                attributeKeXinDuDesign.put(measurementData.getName(), measurementData.getKeXinDu());
            }else if(measurementData.getPhaseBelongsTo().equals("编码阶段") && measurementData.getType().equals("属性")){
                attributeKeXinDuCoding.put(measurementData.getName(), measurementData.getKeXinDu());
            }else if(measurementData.getPhaseBelongsTo().equals("测试阶段") && measurementData.getType().equals("属性")){
                attributeKeXinDuTest.put(measurementData.getName(), measurementData.getKeXinDu());
            }
        }

        Map<String,Double> subAttributeKeXinDuRequire = new LinkedHashMap<>();
        Map<String,Double> subAttributeKeXinDuDesign = new LinkedHashMap<>();
        Map<String,Double> subAttributeKeXinDuCoding = new LinkedHashMap<>();
        Map<String,Double> subAttributeKeXinDuTest = new LinkedHashMap<>();

        for(int i = 0; i < measurementDataList.size(); i++){
            MeasurementData measurementData = measurementDataList.get(i);
            if(measurementData.getPhaseBelongsTo().equals("需求阶段") && measurementData.getType().equals("子属性")){
                subAttributeKeXinDuRequire.put(measurementData.getName(), measurementData.getKeXinDu());
            }else if(measurementData.getPhaseBelongsTo().equals("设计阶段") && measurementData.getType().equals("子属性")){
                subAttributeKeXinDuDesign.put(measurementData.getName(), measurementData.getKeXinDu());
            }else if(measurementData.getPhaseBelongsTo().equals("编码阶段") && measurementData.getType().equals("子属性")){
                subAttributeKeXinDuCoding.put(measurementData.getName(), measurementData.getKeXinDu());
            }else if(measurementData.getPhaseBelongsTo().equals("测试阶段") && measurementData.getType().equals("子属性")){
                subAttributeKeXinDuTest.put(measurementData.getName(), measurementData.getKeXinDu());
            }
        }

        if(attributeKeXinDuRequire.size() == 0 || attributeKeXinDuDesign.size() == 0 || attributeKeXinDuCoding.size() == 0 || attributeKeXinDuTest.size() == 0 || subAttributeKeXinDuRequire.size() == 0 || subAttributeKeXinDuDesign.size() == 0 || subAttributeKeXinDuCoding.size() == 0 || subAttributeKeXinDuTest.size() == 0){
            return "measurementData/radarErr";
        }else{
            model.addAttribute("attributeKeXinDuRequire", attributeKeXinDuRequire);
            model.addAttribute("attributeKeXinDuDesign", attributeKeXinDuDesign);
            model.addAttribute("attributeKeXinDuCoding", attributeKeXinDuCoding);
            model.addAttribute("attributeKeXinDuTest", attributeKeXinDuTest);

            model.addAttribute("subAttributeKeXinDuRequire", subAttributeKeXinDuRequire);
            model.addAttribute("subAttributeKeXinDuDesign", subAttributeKeXinDuDesign);
            model.addAttribute("subAttributeKeXinDuCoding", subAttributeKeXinDuCoding);
            model.addAttribute("subAttributeKeXinDuTest", subAttributeKeXinDuTest);

            return "softData/charts/showRadar.html";
        }
    }
}
