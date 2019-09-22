package com.soft.eva.web;

import com.soft.eva.domain.SoftwareProduct;
import com.soft.eva.service.MeasurementDataService;
import com.soft.eva.service.SoftwareProductService;
import com.soft.eva.util.PageUtils;
import com.soft.eva.util.QueryCondition;
import com.soft.eva.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(value = "/softwareProduct")
@Controller
public class SoftwareProductController {
    @Autowired
    SoftwareProductService softwareProductService;

    @Autowired
    MeasurementDataService measurementDataService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String softwareProduct(){
        return "softwareProduct/softwareProductList";
    }

    /**
     * 查询软件产品列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/softwareProductList",method = RequestMethod.GET)
    @ResponseBody
    public PageUtils softwareProductList(@RequestParam Map<String,Object> params){
        QueryCondition queryCondition = new QueryCondition(params);
        List<SoftwareProduct> softwareProductList = softwareProductService.list(queryCondition);
        int total = softwareProductService.count(queryCondition);
        PageUtils pageUtils = new PageUtils(softwareProductList, total);
        return pageUtils;
    }

    /**
     * 添加软件产品
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(){
        return "softwareProduct/add";
    }

    /**
     * 编辑软件产品
     * @param model
     * @param number
     * @return
     */
    @RequestMapping(value = "/edit/{number}",method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("number") String number){
        SoftwareProduct softwareProduct = softwareProductService.get(number);
        model.addAttribute("softwareProduct", softwareProduct);
        return "softwareProduct/edit";
    }

    /**
     * 保存软件产品
     * @param softwareProduct
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil save(SoftwareProduct softwareProduct){
        if(softwareProductService.save(softwareProduct) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }

    /**
     * 更新软件产品
     * @param softwareProduct
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil update(SoftwareProduct softwareProduct){
        if(softwareProductService.update(softwareProduct) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }
    /**
     * 删除软件产品
     * @param number
     * @return
     */
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil remove(String number){
        measurementDataService.delete("softwareBelongsTo",number );
        if(softwareProductService.remove(number) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }
    /**
     * 批量删除软件产品
     * @param numbers
     * @return
     */
    @RequestMapping(value = "/batchRemove",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil batchRemove(@RequestParam("ids[]") String[] numbers){
        if(softwareProductService.batchRemove(numbers) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }
}
