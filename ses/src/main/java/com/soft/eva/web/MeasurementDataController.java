package com.soft.eva.web;

import com.soft.eva.domain.MeasurementData;
import com.soft.eva.domain.SoftData;
import com.soft.eva.domain.SoftwareProduct;
import com.soft.eva.service.MeasurementDataService;
import com.soft.eva.service.SoftDataService;
import com.soft.eva.service.SoftwareProductService;
import com.soft.eva.util.FindAll;
import com.soft.eva.util.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(value = "/measurementData")
public class MeasurementDataController{

    @Autowired
    private MeasurementDataService measurementDataService;

    @Autowired
    private SoftDataService softDataService;

    @Autowired
    private  SoftwareProductService softwareProductService;

    //当前正在测评的软件产品

     SoftwareProduct softwareProductCurrent ;
     String softwareNumber;

    public  void getSoftwareProductCurrent(){
        softwareProductCurrent = softwareProductService.getByStatus(1);
        if(softwareProductCurrent != null){
            softwareNumber = softwareProductCurrent.getNumber();
        }
    }

    public List<MeasurementData> showTable(String phase){
        List<MeasurementData> measurementDataList = new ArrayList<>();

//        List<SoftData> softDataListInPhase = softDataService.find("phase", phase);
        String[] fields = {"softwareNumber", "phase"};
        String[] values = {softwareNumber, phase};
        List<SoftData> softDataListInPhase = softDataService.find(fields, values);

        List<String> phaseList = new FindAll().findPhase(softDataListInPhase);
        Map<String,List<String>> phase_attributesMap = new FindAll().findAllAttributeToEachPhase(softDataListInPhase);
        Map<String,List<String>> attribute_subAttributesMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListInPhase);
        Map<String,List<String>> subAttribute_metricElementsMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListInPhase);
        for(int i = 0; i < phaseList.size(); i++){
            String curPhase = phaseList.get(i);
            String[] fields_Phase = {"softwareBelongsTo","name","phaseBelongsTo"};
            String[] values_Phase = {softwareNumber,curPhase,phase};
            MeasurementData measurementData_Phase = measurementDataService.findOne(fields_Phase, values_Phase);
            if(measurementData_Phase != null){
                measurementDataList.add(measurementData_Phase);
            }
            List<String> attributeList = phase_attributesMap.get(curPhase);
            for(int j = 0; j < attributeList.size(); j++){
                String curAttribute = attributeList.get(j);
                String[] fields_Attribute = {"softwareBelongsTo","name","phaseBelongsTo"};
                String[] values_Attribute = {softwareNumber,curAttribute,phase};
                MeasurementData measurementData_Attribute = measurementDataService.findOne(fields_Attribute, values_Attribute);
                if(measurementData_Attribute != null){
                    measurementDataList.add(measurementData_Attribute);
                }
                List<String> subAttributeList = attribute_subAttributesMap.get(curAttribute);
                for(int k = 0; k < subAttributeList.size(); k++){
                    String curSubAttribute = subAttributeList.get(k);
                    String[] fields_SubAttribute = {"softwareBelongsTo","name","phaseBelongsTo"};
                    String[] values_SubAttribute = {softwareNumber,curSubAttribute,phase};
                    MeasurementData measurementData_SubAttribute = measurementDataService.findOne(fields_SubAttribute, values_SubAttribute);
                    if(measurementData_SubAttribute != null){
                        measurementDataList.add(measurementData_SubAttribute);
                    }
                    List<String> metricElementList = subAttribute_metricElementsMap.get(curSubAttribute);
                    for(int l = 0; l < metricElementList.size(); l++){
                        String curMetricElement = metricElementList.get(l);
                        String[] fields_MetricElement = {"softwareBelongsTo","name","phaseBelongsTo"};
                        String[] values_MetricElement = {softwareNumber,curMetricElement ,phase};
                        MeasurementData measurementData_MetricElement = measurementDataService.findOne(fields_MetricElement, values_MetricElement);
                        if(measurementData_MetricElement != null){
                            measurementDataList.add(measurementData_MetricElement);
                        }
                    }
                }
            }
        }
        return measurementDataList;
    }

    public List<SoftData> getKeXinDuLessThan(double softwareTrustedValue){
        List<SoftData> softDataList = new ArrayList<>();
//        List<MeasurementData> measurementDataList = measurementDataService.find("type", "阶段");
        String[] fields = {"softwareBelongsTo", "type"};
        String[] values = {softwareNumber, "阶段"};
        List<MeasurementData> measurementDataList = measurementDataService.find(fields,values);

        List<MeasurementData> measurementDataLessThanSoftwareKeXinDuList = new ArrayList<>();
        List<String> phaseList = new ArrayList<>();
        for(int i = 0; i < measurementDataList.size(); i++){
            String curPhase = measurementDataList.get(i).getName();
            if(!phaseList.contains(curPhase)){
                phaseList.add(curPhase);
            }
        }
        for(int i = 0; i < phaseList.size(); i++){
            String curPhase = phaseList.get(i);

//            List<SoftData> softDataListInCurPhase = softDataService.find("phase", curPhase);
            String[] fieldsSoftData = {"softwareNumber", "phase"};
            String[] valuesSoftData = {softwareNumber, curPhase};
            List<SoftData> softDataListInCurPhase = softDataService.find(fieldsSoftData, valuesSoftData);

            Map<String,List<String>> phase_attributesMap = new FindAll().findAllAttributeToEachPhase(softDataListInCurPhase);
            List<String> attributeList = phase_attributesMap.get(curPhase);
            for(int j = 0; j < attributeList.size(); j++){
                String curAttribute = attributeList.get(j);
                Map<String,List<String>> attribute_subAttributesMap = new FindAll().findAllSubAttributeToEachAttribute(softDataListInCurPhase);
                List<String> subAttributeList = attribute_subAttributesMap.get(curAttribute);
                for(int k = 0; k < subAttributeList.size(); k++){
                    String curSubAttribute = subAttributeList.get(k);
                    Map<String,List<String>> subAttribute_metricElementsMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataListInCurPhase);
                    List<String> metricElementList = subAttribute_metricElementsMap.get(curSubAttribute);
                    for(int l = 0; l < metricElementList.size(); l++){
                        String curMetricElement = metricElementList.get(l);
                        String[] fields_MetricElement = {"softwareBelongsTo","name","phaseBelongsTo"};
                        String[] values_MetricElement = {softwareNumber, curMetricElement,curPhase};
                        MeasurementData measurementData = measurementDataService.findOne(fields_MetricElement, values_MetricElement);
                        double curKeXinDu = measurementData.getKeXinDu();
                        if(curKeXinDu <= softwareTrustedValue){
                            measurementDataLessThanSoftwareKeXinDuList.add(measurementData);
//                            String[] fields = {"phase","metricElement"};
//                            String[] values = {curPhase,curMetricElement};
//                            SoftData softData = softDataService.findOne(fields,values);
//                            softDataList.add(softData);
                        }
                    }
                }
            }
        }
        Collections.sort(measurementDataLessThanSoftwareKeXinDuList, new Comparator<MeasurementData>() {
            @Override
            public int compare(MeasurementData m1, MeasurementData m2) {
                double trustedValue1 = m1.getKeXinDu();
                double trustedValue2 = m2.getKeXinDu();
                if(trustedValue1 < trustedValue2){
                    return -1;
                }else if(trustedValue1 > trustedValue2){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
        for(int i = 0; i < measurementDataLessThanSoftwareKeXinDuList.size(); i++){
            MeasurementData measurementData = measurementDataLessThanSoftwareKeXinDuList.get(i);
            String phase = measurementData.getPhaseBelongsTo();
            String metricElement = measurementData.getName();
            double trustworthyValue = measurementData.getKeXinDu();
            String[] fieldsSoftData = {"softwareNumber","phase", "metricElement"};
            String[] valuesSoftData = {softwareNumber,phase, metricElement};
            SoftData softData = softDataService.findOne(fieldsSoftData, valuesSoftData);
            softData.setTrustworthyValue(trustworthyValue);
            softDataList.add(softData);
        }
        return softDataList;
    }

    @RequestMapping(value = "/showMeasurementDataListInRequirePhase",method = RequestMethod.GET)
    public String showMeasurementDataListInRequirePhase(Model model){
        getSoftwareProductCurrent();
        List<MeasurementData>  measurementDataListInRequirePhase = showTable("需求阶段");
//        System.out.println(measurementDataListInRequirePhase.size());
        if(measurementDataListInRequirePhase.size() != 0){
            model.addAttribute("measurementDataListInRequirePhase", measurementDataListInRequirePhase);
            return "measurementData/requirePhase";
        }else{
            return "measurementData/requirePhaseErr";
        }
    }
    @RequestMapping(value = "/showMeasurementDataListInDesignPhase",method = RequestMethod.GET)
    public String showMeasurementDataListInDesignPhase(Model model){
        getSoftwareProductCurrent();

        List<MeasurementData> measurementDataListInDesignPhase = showTable("设计阶段");
        if(measurementDataListInDesignPhase.size() != 0){
            model.addAttribute("measurementDataListInDesignPhase", measurementDataListInDesignPhase);
            return "measurementData/designPhase";
        }else{
            return "measurementData/designPhaseErr";
        }
    }
    @RequestMapping(value = "/showMeasurementDataListInCodingPhase",method = RequestMethod.GET)
    public String showMeasurementDataListInCodingPhase(Model model){
        getSoftwareProductCurrent();

        List<MeasurementData> measurementDataListInCodingPhase = showTable("编码阶段");
        if(measurementDataListInCodingPhase.size() != 0){
            model.addAttribute("measurementDataListInCodingPhase", measurementDataListInCodingPhase);
            return "measurementData/codingPhase";
        }else{
            return "measurementData/codingPhaseErr";
        }

    }

    @RequestMapping(value = "/showMeasurementDataListInTestPhase",method = RequestMethod.GET)
    public String showMeasurementDataListInTestPhase(Model model){
        getSoftwareProductCurrent();

        List<MeasurementData> measurementDataListInTestPhase = showTable("测试阶段");
        if (measurementDataListInTestPhase.size() != 0){
            model.addAttribute("measurementDataListInTestPhase", measurementDataListInTestPhase);
            return "measurementData/testPhase";
        }else{
            return "measurementData/testPhaseErr";
        }
    }

    public Map<String,Object> getReportContent(){
        Map<String,Object> resMap = new LinkedHashMap<>();
        List<Double> phaseKeXinDuList = new ArrayList<>();
        double softwareKeXinDu = 1;
        String keXinLevel;

//        List<MeasurementData> measurementDataList = measurementDataService.find("type", "阶段");
        String[] fields = {"softwareBelongsTo", "type"};
        String[] values = {softwareNumber, "阶段"};
        List<MeasurementData> measurementDataList = measurementDataService.find(fields,values);

        for(int i = 0; i < measurementDataList.size(); i++){
            double curKeXinDu = measurementDataList.get(i).getKeXinDu();
            double curWeight = measurementDataList.get(i).getWeight();
            phaseKeXinDuList.add(curKeXinDu);
            double curValue = Math.pow(curKeXinDu, curWeight);
            BigDecimal b   =   new   BigDecimal(curValue);
            double   value   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
            softwareKeXinDu = new Round().round(softwareKeXinDu*value, 2) ;
        }
        int count_keXinDuLessThan95 = 0;
        int count_keXinDuLessThan85 = 0;
        int count_keXinDuLessThan70 = 0;
        int count_keXinDuLessThan45 = 0;
        for(int i = 0; i < phaseKeXinDuList.size(); i++){
            double curKeXinDu = phaseKeXinDuList.get(i);
            if(curKeXinDu <= 4.5){
                count_keXinDuLessThan45++;
                count_keXinDuLessThan70++;
                count_keXinDuLessThan85++;
                count_keXinDuLessThan95++;
            }else if(curKeXinDu > 4.5 && curKeXinDu <= 7.0){
                count_keXinDuLessThan70++;
                count_keXinDuLessThan85++;
                count_keXinDuLessThan95++;
            }else if(curKeXinDu > 7.0 && curKeXinDu <= 8.5){
                count_keXinDuLessThan85++;
                count_keXinDuLessThan95++;
            }else if(curKeXinDu > 8.5 && curKeXinDu <= 9.5){
                count_keXinDuLessThan95++;
            }
        }
        if(softwareKeXinDu >= 9.5 && count_keXinDuLessThan95 <= 1 && count_keXinDuLessThan85 == 0){
            keXinLevel = "5级";
        }else if(softwareKeXinDu >= 8.5 && count_keXinDuLessThan85 <= 1 && count_keXinDuLessThan70 == 0){
            keXinLevel = "4级";
        }else if(softwareKeXinDu >= 7.0 && count_keXinDuLessThan70 <= 1 && count_keXinDuLessThan45 == 0){
            keXinLevel = "3级";
        }else if(softwareKeXinDu >= 4.5 && count_keXinDuLessThan45 == 0){
            keXinLevel = "2级";
        }else{
            keXinLevel = "1级";
        }

        //显示四个阶段属性的可信度
        String[] fieldsInRequirePhase = {"softwareBelongsTo","phaseBelongsTo","type"};
        String[] valuesInRequirePhase = {softwareNumber,"需求阶段","属性"};
        List<MeasurementData> measurementDataListInRequirePhase = measurementDataService.find(fieldsInRequirePhase, valuesInRequirePhase);

        String[] fieldsInDesignPhase = {"softwareBelongsTo","phaseBelongsTo","type"};
        String[] valuesInDesignPhase = {softwareNumber,"设计阶段","属性"};
        List<MeasurementData> measurementDataListInDesignPhase = measurementDataService.find(fieldsInDesignPhase, valuesInDesignPhase);

        String[] fieldsInCodingPhase = {"softwareBelongsTo","phaseBelongsTo","type"};
        String[] valuesInCodingPhase = {softwareNumber,"编码阶段","属性"};
        List<MeasurementData> measurementDataListInCodingPhase = measurementDataService.find(fieldsInCodingPhase, valuesInCodingPhase);

        String[] fieldsInTestPhase = {"softwareBelongsTo","phaseBelongsTo","type"};
        String[] valuesInTestPhase = {softwareNumber,"测试阶段","属性"};
        List<MeasurementData> measurementDataListInTestPhase = measurementDataService.find(fieldsInTestPhase, valuesInTestPhase);

        List<SoftData> softDataList = getKeXinDuLessThan(softwareKeXinDu);

//        String softwareNumber = "";
//        if(softDataList.size() > 0){
//            softwareNumber   = softDataList.get(0).getSoftwareNumber();
//        }

        resMap.put("softwareNumber", softwareNumber);
        resMap.put("softwareKeXinDu", softwareKeXinDu);
        resMap.put("keXinLevel", keXinLevel);
        resMap.put("measurementDataListInRequirePhase", measurementDataListInRequirePhase);
        resMap.put("measurementDataListInDesignPhase", measurementDataListInDesignPhase);
        resMap.put("measurementDataListInCodingPhase", measurementDataListInCodingPhase);
        resMap.put("measurementDataListInTestPhase", measurementDataListInTestPhase);
        resMap.put("softDataList", softDataList);
        return resMap;
    }

    @RequestMapping(value = "/showReportInNav",method = RequestMethod.GET)
    public String showReportInNav(Model model){
        getSoftwareProductCurrent();

        Map<String,Object> map = getReportContent();
        double softwareKeXinDu = (double)map.get("softwareKeXinDu");
        String keXinLevel = (String)map.get("keXinLevel");
        List<MeasurementData> measurementDataListInRequirePhase = (ArrayList)map.get("measurementDataListInRequirePhase");
        List<MeasurementData> measurementDataListInDesignPhase = (ArrayList)map.get("measurementDataListInDesignPhase");
        List<MeasurementData> measurementDataListInCodingPhase = (ArrayList)map.get("measurementDataListInCodingPhase");
        List<MeasurementData> measurementDataListInTestPhase = (ArrayList)map.get("measurementDataListInTestPhase");
        List<SoftData> softDataList = (ArrayList)map.get("softDataList");

//        String softwareNumber = (String)map.get("softwareNumber");
//        SoftwareProduct softwareProduct = softwareProductService.get(softwareNumber);

        List<List<String>> attributeValueListAllPhase = new ArrayList<>();
        for(int i = 0; i < measurementDataListInRequirePhase.size(); i++){
            String curAttribute = measurementDataListInRequirePhase.get(i).getName();
            List<String> attributeValueList = new ArrayList<>();
            attributeValueList.add(curAttribute);
            attributeValueList.add(String.valueOf(measurementDataListInRequirePhase.get(i).getKeXinDu()));
            for(int j = 0; j < measurementDataListInDesignPhase.size(); j++){
                String curAttributeDesign = measurementDataListInDesignPhase.get(j).getName();
                if(curAttribute.equals(curAttributeDesign)){
                    attributeValueList.add(String.valueOf(measurementDataListInDesignPhase.get(j).getKeXinDu()));
                    break;
                }
            }
            for(int p = 0; p < measurementDataListInCodingPhase.size(); p++){
                String curAttributeCoding = measurementDataListInCodingPhase.get(p).getName();
                if(curAttribute.equals(curAttributeCoding)){
                    attributeValueList.add(String.valueOf(measurementDataListInCodingPhase.get(p).getKeXinDu()));
                    break;
                }
            }
            for(int q = 0; q < measurementDataListInTestPhase.size(); q++){
                String curAttributeTest = measurementDataListInTestPhase.get(q).getName();
                if(curAttribute.equals(curAttributeTest)){
                    attributeValueList.add(String.valueOf(measurementDataListInTestPhase.get(q).getKeXinDu()));
                    break;
                }
            }
            attributeValueListAllPhase.add(attributeValueList);
        }

        if(softwareNumber != null){
            softwareProductCurrent.setKeXinDu(softwareKeXinDu);
            softwareProductCurrent.setStatus(Integer.valueOf(2));//将status设置为2，表示评估结束
            softwareProductService.update(softwareProductCurrent);


            model.addAttribute("softwareKeXinDu", softwareKeXinDu);
            model.addAttribute("keXinLevel", keXinLevel);
            model.addAttribute("measurementDataListInRequirePhase", measurementDataListInRequirePhase);
            model.addAttribute("measurementDataListInDesignPhase", measurementDataListInDesignPhase);
            model.addAttribute("measurementDataListInCodingPhase", measurementDataListInCodingPhase);
            model.addAttribute("measurementDataListInTestPhase", measurementDataListInTestPhase);
            model.addAttribute("softDataList", softDataList);
            model.addAttribute("softwareProduct", softwareProductCurrent);
            model.addAttribute("attributeValueListAllPhase", attributeValueListAllPhase);

            return "measurementData/report";
        }else{
            return "measurementData/reportErr";
        }
    }

    @RequestMapping(value = "/showReport/{number}",method = RequestMethod.GET)
    public String showReport(@PathVariable("number") String number, Model model){
        getSoftwareProductCurrent();
        Map<String,Object> map = getReportContent();
        double softwareKeXinDu = (double)map.get("softwareKeXinDu");
        String keXinLevel = (String)map.get("keXinLevel");
        List<MeasurementData> measurementDataListInRequirePhase = (ArrayList)map.get("measurementDataListInRequirePhase");
        List<MeasurementData> measurementDataListInDesignPhase = (ArrayList)map.get("measurementDataListInDesignPhase");
        List<MeasurementData> measurementDataListInCodingPhase = (ArrayList)map.get("measurementDataListInCodingPhase");
        List<MeasurementData> measurementDataListInTestPhase = (ArrayList)map.get("measurementDataListInTestPhase");
        List<SoftData> softDataList = (ArrayList)map.get("softDataList");

//        String softwareNumber = (String)map.get("softwareNumber");
//        SoftwareProduct softwareProduct = softwareProductService.get(softwareNumber);

        List<List<String>> attributeValueListAllPhase = new ArrayList<>();
        for(int i = 0; i < measurementDataListInRequirePhase.size(); i++){
            String curAttribute = measurementDataListInRequirePhase.get(i).getName();
            List<String> attributeValueList = new ArrayList<>();
            attributeValueList.add(curAttribute);
            attributeValueList.add(String.valueOf(measurementDataListInRequirePhase.get(i).getKeXinDu()));
            for(int j = 0; j < measurementDataListInDesignPhase.size(); j++){
                String curAttributeDesign = measurementDataListInDesignPhase.get(j).getName();
                if(curAttribute.equals(curAttributeDesign)){
                    attributeValueList.add(String.valueOf(measurementDataListInDesignPhase.get(j).getKeXinDu()));
                    break;
                }
            }
            for(int p = 0; p < measurementDataListInCodingPhase.size(); p++){
                String curAttributeCoding = measurementDataListInCodingPhase.get(p).getName();
                if(curAttribute.equals(curAttributeCoding)){
                    attributeValueList.add(String.valueOf(measurementDataListInCodingPhase.get(p).getKeXinDu()));
                    break;
                }
            }
            for(int q = 0; q < measurementDataListInTestPhase.size(); q++){
                String curAttributeTest = measurementDataListInTestPhase.get(q).getName();
                if(curAttribute.equals(curAttributeTest)){
                    attributeValueList.add(String.valueOf(measurementDataListInTestPhase.get(q).getKeXinDu()));
                    break;
                }
            }
            attributeValueListAllPhase.add(attributeValueList);
        }

        SoftwareProduct softwareProduct = softwareProductService.get(number);
//        softwareProduct.setKeXinDu(softwareKeXinDu);
//        softwareProduct.setStatus(Integer.valueOf(2));
//        softwareProductService.update(softwareProduct);
        model.addAttribute("softwareKeXinDu", softwareKeXinDu);
        model.addAttribute("keXinLevel", keXinLevel);
        model.addAttribute("measurementDataListInRequirePhase", measurementDataListInRequirePhase);
        model.addAttribute("measurementDataListInDesignPhase", measurementDataListInDesignPhase);
        model.addAttribute("measurementDataListInCodingPhase", measurementDataListInCodingPhase);
        model.addAttribute("measurementDataListInTestPhase", measurementDataListInTestPhase);
        model.addAttribute("softDataList", softDataList);
        model.addAttribute("softwareProduct", softwareProduct);
        model.addAttribute("attributeValueListAllPhase", attributeValueListAllPhase);

        return "measurementData/report";
    }
}
