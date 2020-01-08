package com.soft.eva.web;

import com.soft.eva.domain.OperateRecord;
import com.soft.eva.dto.Page;
import com.soft.eva.service.OperateRecordService;
import com.soft.eva.util.PageUtils;
import com.soft.eva.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/operateRecord")
public class OperateRecordController {

    @Autowired
    private OperateRecordService operateRecordService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String user(){
        return "operateRecord/operateRecordList";
    }

    /**
     * 显示操作记录列表
     * @param limit
     * @param offset
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/operateRecordList",method = RequestMethod.GET)
    public PageUtils operateRecordList(@RequestParam(value = "limit", required = false) Integer limit,@RequestParam(value = "offset", required = false) Integer offset){
        //查询列表数据
        //因为是服务端分页，返回值必须告诉前端总记录的条数total和当前页的记录数rows
        Page page = new Page(offset/limit+1, limit);
        page = operateRecordService.findByPage(page);
        List<OperateRecord> operateRecordList = page.getList();
        int total = page.getTotalCount();
        PageUtils pageUtils = new PageUtils(operateRecordList, total);
        return pageUtils;
    }
    /**
     * 批量删除 操作记录
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batchRemove",method = RequestMethod.POST)
    public ReturnUtil batchRemoveSoftDataInRequirePhase(@RequestParam("ids[]") String[] ids){
        operateRecordService.deleteByIds(ids);
        return ReturnUtil.ok();
    }

    /**
     *  修改正互反矩阵，跳到操作记录说明填写页面
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String changeAttributeMatrix(){
        return "operateRecord/uploadOperateRecordModifyMatrix";
    }

    /**
     * 四个阶段
     * 点击添加操作记录，再执行具体操作
     * @return
     */
    @RequestMapping(value = "/uploadOperateRecord",method = RequestMethod.GET)
    public String uploadOperateRecordInRequirePhase(){
        return "operateRecord/uploadOperateRecord";
    }

    /**
     *  提交凭证说明
     * @param operateRecord
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public ReturnUtil submitProofInDesignPhase(OperateRecord operateRecord){
        operateRecordService.save(operateRecord);
        return ReturnUtil.ok();
    }


}
