package com.ccx.credit.risk.assetlayer;

import com.ccx.credit.risk.api.asset.AssetApi;
import com.ccx.credit.risk.api.assetlayer.*;
import com.ccx.credit.risk.api.assetsPackage.AssetsPackageApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.assetlayer.*;
import com.ccx.credit.risk.model.assetsPackage.AssetsPakege;
import com.ccx.credit.risk.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 分层controller
 * zhaotm
 */

@Controller
@RequestMapping(value="/assetLayer")
public class AsserLayerController extends BasicController {

    private static Logger logger = LogManager.getLogger(AsserLayerController.class);

    @Autowired
    private LayerApi layerApi;

    @Autowired
    private AssetApi assetApi;

    @Autowired
    private AssetsPackageApi assetsPackageApi;

    @Autowired
    private LayerAssetsRelevantApi layerAssetsRelevantApi;

    @Autowired
    private LayerLevelApi layerLevelApi;

    @Autowired
    private LayerSetupApi layerSetupApi;

    @Autowired
    private LayerAssetLevelApi assetLevelApi;

    @Autowired
    private LayerResultApi layerResultApi;

    /**
     * 分层首页
     */
    @GetMapping("/layerIndex")
    public ModelAndView layerIndex(){
        ModelAndView mnv = new ModelAndView("assetlayer/layerList");
        return mnv;
    }

    /**
     * 分层列表
     */
    @PostMapping("/layerList")
    @ResponseBody
    @Record(operationType="查看资产分层列表",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设计")
    public JsonResult layerList(HttpServletRequest request){
        Map<String, Object> params = ControllerUtil.requestMap(request);

        try {
            User user =ControllerUtil.getSessionUser(request);
            if(UsedUtil.isNotNull(user)){
                int insId = user.getInstitutionId();
                params.put("insId", insId);
            }
            PageHelper.startPage(getPageNum(), getPageSize());
            PageInfo<Layer> page = layerApi.getPageList(params);

            return JsonResult.ok(page);
        } catch (Exception e) {
            logger.error("查询分层列表异常", e);

            return  JsonResult.error("查询失败");
        }
    }

    /**
     * 分层设置
     */
    @GetMapping("/layerSet")
    @Record(operationType="重新分层",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设置")
    public ModelAndView layerSet(Integer assetPackageId, Integer layerId){
        ModelAndView mnv = new ModelAndView("assetlayer/layerSet");

        try {
            AssetsPakege assetsPackage = assetsPackageApi.findAssetsPackage(assetPackageId);
            List<LayerLeve> levelList = layerLevelApi.findLevelList();
            //封包日
            Date closeTime =  new Date(assetsPackage.getCreateTime().getTime() - 24*3600*1000);
            //资产最大还款日期
            Date maxTime = layerApi.findMaxDateByPackageId(assetPackageId);
            //资产最小投放日期
            Date minTime = layerApi.findMinDateByPackageId(assetPackageId);

            mnv.addObject("assetsPackage", assetsPackage);
            mnv.addObject("levelList", JsonUtils.toJson(levelList));
            mnv.addObject("closeTime", closeTime == null ? null : DateUtils.formatDateStr(closeTime));
            mnv.addObject("maxTime",  maxTime == null ? null : DateUtils.formatDateStr(maxTime));
            mnv.addObject("minTimeLong", minTime == null ? null : minTime.getTime());

            if (-1 != layerId.intValue()) {
                Layer layer = layerApi.getById(layerId);
                LayerSetup setup = layerSetupApi.getByLayerId(layerId);
                List<LayerAssetLevel> assetLevelList = assetLevelApi.getListByLayerId(layerId);

                mnv.addObject("layer", layer);
                mnv.addObject("setup", setup);
                mnv.addObject("assetLevelList", JsonUtils.toJson(assetLevelList));
            }
            mnv.addObject("layerId", layerId);
        } catch (Exception e) {
            logger.error("查询分层设置页面异常", e);
        }
        return mnv;
    }

    /**
     * 分层资产列表
     */
    @PostMapping("/layerRelationAeestList")
    @ResponseBody
    public JsonResult layerRelationAeestList(HttpServletRequest request){
        Integer none = -1;
        Map<String, Object> params = ControllerUtil.requestMap(request);
        Map<Integer, LayerAssetsRelevant> linkedMap = new LinkedHashMap<>();

        try {
            User user =ControllerUtil.getSessionUser(request);
            if(UsedUtil.isNotNull(user)){
                int insId = user.getInstitutionId();
                params.put("insId", insId);
            }
            List<LayerAssetsRelevant> list = layerAssetsRelevantApi.getAssetList(params);

            for (LayerAssetsRelevant relevant : list) {
                if (null != relevant.getRelevantValue() && StringUtils.isNotBlank(String.valueOf(relevant.getRelevantValue()))) {
                    linkedMap.put(relevant.getRelevantValue(), relevant);
                }
            }
            //分类列表
            List< LayerAssetRelevantJson.Parent> parentList = new ArrayList<>();
            for (Integer parentId : linkedMap.keySet()) {
                LayerAssetRelevantJson.Parent parent = new LayerAssetRelevantJson.Parent();
                parent.setId(parentId);
                //parent.setLevel(linkedMap.get(parentId).getRelevantValue());
                parentList.add(parent);
            }
            if (parentList.size() == 0) {
                LayerAssetRelevantJson.Parent parent0 = new LayerAssetRelevantJson.Parent();
                parent0.setId(0);
                //parent.setLevel(linkedMap.get(parentId).getRelevantValue());
                parentList.add(parent0);
            }

            //关联列表
            List<LayerAssetRelevantJson.Tags > tagsList = new ArrayList<>();
            for (LayerAssetsRelevant level : list) {
                LayerAssetRelevantJson.Tags tags = new  LayerAssetRelevantJson.Tags();
                tags.setId(level.getAssetId());
                LayerAssetRelevantJson.Tags.Data data = new LayerAssetRelevantJson.Tags.Data();
                data.setName(level.getName());
                data.setContent(level.getCreatorName());
                tags.setData(data);
                Integer relevantValue = level.getRelevantValue();
                if (null == relevantValue || StringUtils.isBlank(String.valueOf(relevantValue))) {
                    tags.setpId(none);
                } else {
                    tags.setpId(relevantValue);
                }
                tagsList.add(tags);
            }
            LayerAssetRelevantJson json = new LayerAssetRelevantJson(parentList, tagsList);

            return JsonResult.ok(json);
        } catch (Exception e) {
            logger.error("查询分层设置资产列表异常", e);

            return  JsonResult.error("查询分层设置资产列表异常");
        }
    }

    /**
     * 分层资产相关性
     */
    @PostMapping("/layerRelationSave")
    @ResponseBody
    public JsonResult layerRelationSave(HttpServletRequest request, LayerAssetsRelevant relation, String assetIdArr, String relationValueArr){
        Map<String, Object> paramMap = ControllerUtil.requestMap(request);

        try {
            Map<String, Integer> resultMap = layerAssetsRelevantApi.saveRelation(relation, paramMap, assetIdArr, relationValueArr);
            return JsonResult.ok(resultMap);
        } catch (Exception e) {
            logger.error("分层资产相关性保存失败", e);

            return  JsonResult.error("分层资产相关性保存失败");
        }
    }

    /**
     * 分层设置
     */
    @PostMapping("/layerSetSave")
    @ResponseBody
    @Record(operationType="保存分层设置",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设置")
    public JsonResult layersetSave(HttpServletRequest request, LayerSetup setup){
        Map<String, Object> paramMap = ControllerUtil.requestMap(request);

        try {
            Map<String, Integer> resultMap =  layerSetupApi.saveLayerSet(request, paramMap, setup);
            return JsonResult.ok(resultMap);
        } catch (Exception e) {
            logger.error("保存分层资产设置失败", e);

            return  JsonResult.error("保存分层资产设置失败");
        }
    }

    /**
     * 执行分层
     */
    @PostMapping("/layerStart")
    @ResponseBody
    @Record(operationType="执行分层",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设置")
    public JsonResult layerStart(Integer id){
        try {
            Map<String, Object> map = layerSetupApi.saveLayerStart(id);
            return JsonResult.ok(null);
        } catch (Exception e) {
            logger.error("分层失败", e);

            return  JsonResult.error("分层失败");
        }
    }

    /**
     * 分层历史
     */
    @GetMapping("/layerHistory")
    @Record(operationType="分层历史",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设计")
    public ModelAndView layerHistory(Integer packageId){
        ModelAndView mnv = new ModelAndView("assetlayer/layerHistory");

        try {
            AssetsPakege assetsPakege = assetsPackageApi.findAssetsPackage(packageId);
            mnv.addObject("assetsPakege", assetsPakege);
        } catch (Exception e) {
            logger.error("分层历史失败", e);

        }
        return mnv;
    }

    /**
     * 分层历史
     */
    @PostMapping("/layerHistoryList")
    @ResponseBody
    public JsonResult layerHistoryList(Integer packageId){
        try {
            PageHelper.startPage(getPageNum(), getPageSize());
            PageInfo<Layer> page = layerApi.getLayerHistory(packageId);
            return JsonResult.ok(page);
        } catch (Exception e) {
            logger.error("分层历史查询失败", e);

            return JsonResult.error("分层历史查询失败");
        }
    }

    /**
     * 分层详细
     */
    @GetMapping("/layerShow")
    @Record(operationType="详细分层",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设置")
    public ModelAndView layerShow(Integer layerId){
        ModelAndView mnv = new ModelAndView("assetlayer/layerShow");
        try {
            Layer layer = layerApi.getById(layerId);
            AssetsPakege assetsPakege = assetsPackageApi.findAssetsPackage(layer.getAssetPakegeId());
            LayerSetup setup = layerSetupApi.getByLayerId(layerId);
            List<LayerAssetLevel> assetLevelList = assetLevelApi.getListByLayerId(layerId);
            List<LayerResult> layerResultList = layerResultApi.selectListByLayerId(layerId);

            mnv.addObject("layer", layer);
            mnv.addObject("assetPackage", assetsPakege);
            mnv.addObject("setup", setup);
            mnv.addObject("assetLevelList", assetLevelList);
        } catch (Exception e) {
            logger.error("分层详细失败", e);
        }
        return mnv;
    }

    /**
     * 分层资产
     */
    @GetMapping("/layerAsset")
    @Record(operationType="查询分层资产",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设置")
    public ModelAndView layerAsset(Integer layerId){
        ModelAndView mnv = new ModelAndView("assetlayer/layerAsset");
        try {
            Layer layer = layerApi.getById(layerId);
            AssetsPakege assetsPakege = assetsPackageApi.findAssetsPackage(layer.getAssetPakegeId());
            LayerSetup setup = layerSetupApi.getByLayerId(layerId);

            mnv.addObject("layer", layer);
            mnv.addObject("assetPackage", assetsPakege);
            mnv.addObject("setup", setup);
        } catch (Exception e) {
            logger.error("分层资产", e);
        }
        return mnv;
    }

    /**
     * 分层资产lsit
     */
    @PostMapping("/layerAssetList")
    @ResponseBody
    @Record(operationType="查询分层资产列表",
            operationBasicModule="资产风险管理",
            operationConcreteModule ="分层设计")
    public JsonResult layerAssetList(String assetIds){
        try {
            if (StringUtils.isNotBlank(assetIds)) {
                PageHelper.startPage(getPageNum(), getPageSize());
                PageInfo<Map<String, Object>> page = assetApi.findByIds(assetIds);
                return JsonResult.ok(page);
            } else {
                return JsonResult.error("id为空");
            }

        } catch (Exception e) {
            logger.error("分层资产lsit", e);

            return JsonResult.error("分层资产lsit");
        }
    }
}
