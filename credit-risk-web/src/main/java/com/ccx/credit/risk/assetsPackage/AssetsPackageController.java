package com.ccx.credit.risk.assetsPackage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccx.credit.risk.api.assetsPackage.AssetsPackageApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.assetsPackage.AssetsPakege;
import com.ccx.credit.risk.util.ControllerUtil;
import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.Record;
import com.ccx.credit.risk.util.UsedUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WXN
 * @ClassName: AssetsPackageController
 * @Description: 资产包管理
 * @date 2017年10月19日 下午5:07:05
 */
@Controller
@RequestMapping(value = "/assetsPackage")
public class AssetsPackageController extends BasicController {
    private static Logger logger = LogManager.getLogger(AssetsPackageController.class);

    @Autowired
    private AssetsPackageApi assetsPackageApi;

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toAssetsPackagePage
     * @Description: 资产包管理页面
     */
    @RequestMapping(value = "/toAssetsPackagePage", method = RequestMethod.GET)
    @Record(operationType = "查看资产包管理页面",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "资产包管理")
    public String toAssetsPackagePage() {
        return "/assetsPackage/assetsPackageList";
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toAddAssetsPackagePage
     * @Description: 跳转新增资产包页面
     */
    @RequestMapping(value = "/toAddAssetsPackagePage", method = RequestMethod.GET)
    @Record(operationType = "跳转新增资产包页面",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "资产包创建")
    public String toAddAssetsPackagePage() {
        return "/assetsPackage/assetPackageAdd";
    }

    /**
     * @throws
     * @Title: findAllAssetsPackageList
     * @author: WXN
     * @Description: 查询资产表列表list（分页）
     * @param: @param request
     * @param: @return
     * @return: PageInfo<AssetsPakege>
     */
    @RequestMapping(value = "/findAllAssetsPackageList", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo<AssetsPakege> findAllAssetsPackageList(HttpServletRequest request) {
        PageInfo<AssetsPakege> pages = new PageInfo<AssetsPakege>();
        // 获取查询条件
        Map<String, Object> params = ControllerUtil.requestMap(request);
        logger.info("查询资产表列表参数====" + params);
        User user = ControllerUtil.getSessionUser(request);
        if (UsedUtil.isNotNull(user)) {
            int insId = user.getInstitutionId();
            params.put("insId", insId);
        }
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (UsedUtil.isNotNull(startTime)) {
            params.put("startTime", startTime.replaceAll("/", "-"));
        }
        if (UsedUtil.isNotNull(endTime)) {
            params.put("endTime", endTime.replaceAll("/", "-"));
        }

        // 获取当前页数
        String currentPage = (String) params.get("currentPage");
        //获取每页展示数
        String pageSize = (String) params.get("pageSize");
        //当前页数
        int pageNum = 1;
        if (UsedUtil.isNotNull(currentPage)) {
            pageNum = Integer.valueOf(currentPage);
        }
        //设置每页展示数
        int pageSizes = 10;
        if (UsedUtil.isNotNull(pageSize)) {
            pageSizes = Integer.valueOf(pageSize);
        }
        PageHelper.startPage(pageNum, pageSizes);
        pages = assetsPackageApi.findAllAssetsPackageList(params);
        return pages;
    }

    /**
     * @param @param  request
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: checkAssetPackageName
     * @Description: 验证资产包名称是否唯一
     */
    @PostMapping("checkAssetPackageName")
    @ResponseBody
    public String checkAssetPackageName(HttpServletRequest request) {
        String assetPackageName = null == request.getParameter("assetPackageName") ? "" : request.getParameter("assetPackageName").trim();
        //0标识未查找到数据，即唯一 ；1表示已有数据，不唯一 ；2表示传参错误或者失败;3表示验证失败
        String result = "0";
        if (UsedUtil.isNotNull(assetPackageName)) {
            try {
                AssetsPakege assetsPakege = assetsPackageApi.checkAssetPackageName(assetPackageName);
                if (null != assetsPakege) {//不唯一
                    result = "1";
                } else {
                    result = "0";//唯一
                }
            } catch (Exception e) {
                result = "3";
                return result;
            }
        } else {
            result = "2";
        }
        return result;
    }

    /**
     * @param @param  request
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: checkAssetPackageNo
     * @Description: 验证资产包编号是否唯一
     */
    @PostMapping("checkAssetPackageNo")
    @ResponseBody
    public String checkAssetPackageNo(HttpServletRequest request) {
        String assetPackageNo = null == request.getParameter("assetPackageNo") ? "" : request.getParameter("assetPackageNo").trim();
        String result = "0"; //0标识未查找到数据，即唯一 ；1表示已有数据，不唯一 ；2表示传参错误或者失败;3表示验证失败
        if (UsedUtil.isNotNull(assetPackageNo)) {
            try {
                AssetsPakege assetsPakege = assetsPackageApi.checkAssetPackageNo(assetPackageNo);
                if (null != assetsPakege) {//不唯一
                    result = "1";
                } else {
                    result = "0";//唯一
                }
            } catch (Exception e) {
                result = "3";
                return result;
            }
        } else {
            result = "2";
        }
        return result;
    }

    /**
     * @param @param  request
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: saveAddAssetPackage
     * @Description: 保存新增的资产包
     */
    @PostMapping("saveAddAssetPackage")
    @ResponseBody
    @Record(operationType = "保存新增资产包",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "创建资产包")
    public String saveAddAssetPackage(HttpServletRequest request) {
        String assetPackageNo = null == request.getParameter("assetPackageNo") ? "" : request.getParameter("assetPackageNo").trim();
        String assetPackageName = null == request.getParameter("assetPackageName") ? "" : request.getParameter("assetPackageName").trim();
        String assetType = null == request.getParameter("assetType") ? "" : request.getParameter("assetType").trim();
        //999表示新增失败 ；1000表示新增成功 ；888表示传参错误或者失败;777表示新增失败
        String result = "999";
        if (UsedUtil.isNotNull(assetPackageNo) && UsedUtil.isNotNull(assetPackageName) && UsedUtil.isNotNull(assetType) && !"0000".equals(assetType)) {
            User user = ControllerUtil.getSessionUser(request);
            AssetsPakege assetsPakege = new AssetsPakege();
            assetsPakege.setAssetType(Integer.parseInt(assetType));
            assetsPakege.setAssetPackageNo(assetPackageNo);
            assetsPakege.setAssetPackageName(assetPackageName);
            assetsPakege.setIsDel(0);
            if (UsedUtil.isNotNull(user)) {
                assetsPakege.setCreatorName(user.getLoginName());
                assetsPakege.setInstitutionId(user.getInstitutionId());
            }
            assetsPakege.setCreateTime(new Date());
            result = assetsPackageApi.saveAddAssetPackage(assetsPakege);
        } else {
            result = "888";//888表示传参错误或者失败
        }
        return result;
    }

    /**
     * @param @param  request
     *                result = "888";  传参不正确 |未获取到参数
     *                result = "999";  删除失败
     *                result = "1000";  成功标识
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: deleteAssetsPackage
     * @Description: 删除资产包
     */
    @PostMapping("deleteAssetsPackage")
    @ResponseBody
    @Record(operationType = "删除资产包",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "资产包管理")
    public String deleteAssetsPackage(HttpServletRequest request) {
        String assetsPackageId = null == request.getParameter("assetsPackageId") ? "" : request.getParameter("assetsPackageId").trim();
        String result = "999";
        if (UsedUtil.isNotNull(assetsPackageId)) {
            result = assetsPackageApi.deleteAssetsPackage(Integer.valueOf(assetsPackageId));
        } else {
            result = "888";//传参不正确
        }
        return result;
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toAssetsEnteringPage
     * @Description: 跳转资产录入页面
     */
    @RequestMapping(value = "/toAssetsEnteringPage", method = RequestMethod.GET)
    @Record(operationType = "跳转资产包录入界面",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "选择资产")
    public String toAssetsEnteringPage(HttpServletRequest request) {
        String assetsPackageId = null == request.getParameter("assetsPackageId") ? "" : request.getParameter("assetsPackageId").trim();
        if (UsedUtil.isNotNull(assetsPackageId)) {
            AssetsPakege assetsPakege = new AssetsPakege();
            try {
                //根据资产包id查询资产包信息
                assetsPakege = assetsPackageApi.findAssetsPackage(Integer.valueOf(assetsPackageId));
                request.setAttribute("assetsPakege", assetsPakege);
            } catch (Exception e) {
                logger.info("根据资产包id查询资产包信息失败！", e);
                return "/assetsPackage/assetEntering";
            }
        }
        request.setAttribute("assetsPackageId", assetsPackageId);
        return "/assetsPackage/assetEntering";
    }

    /**
     * @param assetPackageId   资产包id
     * @param assetPackageName 资产包id
     * @param assetIdsStr      资产id集合
     * @Description: 保存更新后的资产包
     */
    @PostMapping("saveUpdateAssetPackage")
    @ResponseBody
    @Record(operationType = "保存更新的资产包",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "选择资产")
    public Map<String, Object> saveUpdateAssetPackage(HttpServletRequest request, @RequestParam(required = true) Integer assetPackageId,
                                                      @RequestParam(required = true) String assetPackageName,
                                                      @RequestParam(required = true) String assetIdsStr) {
        //jsp result
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //一、保存资产包修改
            assetsPackageApi.updateAssetPackageNameById(assetPackageId, assetPackageName, assetIdsStr);

            resultMap.put("code", 200);
        } catch (Exception e) {
            resultMap.put("code", 500);
            logger.error("更新资产包和资产关联表失败！");
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * @param @param  request
     * @param @return 设定文件
     * @return PageInfo<Asset>    返回类型
     * @throws
     * @Title: findAssetsListByPackageId
     * @Description: 根据资产包id查询当前资产包下所有的资产list（分页）
     */
    @PostMapping("findAssetsListByPackageId")
    @ResponseBody
    public PageInfo<AbsAsset> findAssetsListByPackageId(HttpServletRequest request) {
        PageInfo<AbsAsset> pages = new PageInfo<AbsAsset>();
        // 获取查询条件
        Map<String, Object> params = ControllerUtil.requestMap(request);
        logger.info("查询资产列表参数====" + params);

        // 获取当前页数
        String currentPage = (String) params.get("currentPage");
        //获取每页展示数
        String pageSize = (String) params.get("pageSize");
        //当前页数
        int pageNum = 1;
        if (UsedUtil.isNotNull(currentPage)) {
            pageNum = Integer.valueOf(currentPage);
        }
        //设置每页展示数
        int pageSizes = 10;
        if (UsedUtil.isNotNull(pageSize)) {
            pageSizes = Integer.valueOf(pageSize);
        }
        PageHelper.startPage(pageNum, pageSizes);
        pages = assetsPackageApi.findAssetsListByPackageId(params);
        return pages;
    }

    /**
     * @param @param  request
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: deleteAssetsOfPackage
     * @Description: 删除资产包下的资产
     */
    @PostMapping("deleteAssetsOfPackage")
    @ResponseBody
    @Record(operationType = "删除资产包下的资产",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "选择资产")
    public String deleteAssetsOfPackage(HttpServletRequest request) {
        String assetsId = null == request.getParameter("assetsId") ? "" : request.getParameter("assetsId").trim();
        String assetsPackageId = null == request.getParameter("assetsPackageId") ? "" : request.getParameter("assetsPackageId").trim();
        String result = "999";
        if (UsedUtil.isNotNull(assetsId) && UsedUtil.isNotNull(assetsPackageId)) {
            result = assetsPackageApi.deleteAssetsOfPackage(Integer.valueOf(assetsId), Integer.valueOf(assetsPackageId));
        } else {
            result = "888";//传参不正确
        }
        return result;
    }

    /**
     * @param @param  request
     * @param @return 设定文件
     * @return PageInfo<Asset>    返回类型
     * @throws
     * @Title: findAllAssetsList
     * @Description: 查询可以选择的资产list（分页）
     */
    @PostMapping("findAllAssetsList")
    @ResponseBody
    @Record(operationType = "查询可选资产",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "资产包管理")
    public PageInfo<AbsAsset> findAllAssetsList(HttpServletRequest request) {
        PageInfo<AbsAsset> pages = new PageInfo<AbsAsset>();
        // 获取查询条件
        Map<String, Object> params = ControllerUtil.requestMap(request);
        logger.info("查询资产列表参数====" + params);
        User user = ControllerUtil.getSessionUser(request);
        if (UsedUtil.isNotNull(user)) {
            int insId = user.getInstitutionId();
            params.put("insId", insId);
        }
        // 获取当前页数
        String currentPage = (String) params.get("currentPage");
        //获取每页展示数
        String pageSize = (String) params.get("pageSize");
        //当前页数
        int pageNum = 1;
        if (UsedUtil.isNotNull(currentPage)) {
            pageNum = Integer.valueOf(currentPage);
        }
        //设置每页展示数
        int pageSizes = 10;
        if (UsedUtil.isNotNull(pageSize)) {
            pageSizes = Integer.valueOf(pageSize);
        }
        PageHelper.startPage(pageNum, pageSizes);
        pages = assetsPackageApi.findAllAssetsList(params);
        return pages;
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toAssetsEnteringPage
     * @Description: 跳转概览分析页面
     */
    @RequestMapping(value = "/toAssetsPackageAnalysisPage", method = RequestMethod.GET)
    @Record(operationType = "跳转概览分析页面",
            operationBasicModule = "资产风险管理",
            operationConcreteModule = "概览分析")
    public String toAssetsPackageAnalysisPage(HttpServletRequest request) {
        String assetsPackageId = null == request.getParameter("assetsPackageId") ? "" : request.getParameter("assetsPackageId").trim();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //查询当前资产包信息
        AssetsPakege assetsPakege = new AssetsPakege();
        //资产包本金（万元）
        BigDecimal assetsPackageAmount = new BigDecimal("0.00");
        //资产包剩余本金（万元） 剩余本金：资产包内各笔资产在封包日之后应还的本金之和
        BigDecimal assetsPackageResidueAmount = new BigDecimal("0.00");
        //资产包剩余本息（万元） 剩余本息：资产包内各笔资产在封包日之后应还的本金和利息之和
        BigDecimal residueAmountAndInterest = new BigDecimal("0.00");
        //单笔资产平均本金余额（万元）
        BigDecimal singleAvgAssetsAmount = new BigDecimal("0.00");
        //单笔资产最高本金金额（万元）
        BigDecimal singleAssetsMaxAmount = new BigDecimal("0.00");
        //单笔资产最低本金金额（万元）
        BigDecimal singleAssetsMinAmount = new BigDecimal("0.00");
        //单笔资产最高本金金额占比
        double singleAssetsMaxAmountPercent = 0.0;
        //单笔资产最低本金金额占比
        double singleAssetsMinAmountPercent = 0.0;
        //加权平均期限（年）
        double weightedAvgYear = 0.0;
        //加权平均剩余期限（年）
        double weightedAvgResidueYear = 0.0;
        //加权平均资产账龄（年）
        double weightedAvgAssetYear = 0.0;
        if (UsedUtil.isNotNull(assetsPackageId)) {
            //查询当前资产包下所有的资产信息
            List<AbsAsset> assetList = new ArrayList<AbsAsset>();
            //查询当前资产包下所有的还款计划信息
            List<LinkedHashMap<String, Object>> repaymentList = new ArrayList<LinkedHashMap<String, Object>>();
            try {
                assetsPakege = assetsPackageApi.findAssetsPackage(Integer.valueOf(assetsPackageId));
                assetList = assetsPackageApi.findAssetsListById(Integer.valueOf(assetsPackageId));
                repaymentList = assetsPackageApi.findRepaymentList(Integer.valueOf(assetsPackageId));
            } catch (Exception e) {
                logger.info("查询现金流数据失败！", e);
            }
            if (null != assetsPakege) {
                //资产包创建日
                Date assetsPackageCreateTime = assetsPakege.getCreateTime();
                //资产包封包日 封包日=资产包创建日期－1日  例：资产包创建日为2017-08-02，则封包日默认为2017-08-01
                String assetsPackageCloseTimeStr = DateUtils.getCountedDay(sd.format(assetsPackageCreateTime), -1);
                Date assetsPackageCloseTime = null;
                try {
                    assetsPackageCloseTime = sd.parse(assetsPackageCloseTimeStr);
                } catch (Exception e) {
                    logger.info("时间格式化报错", e);
                }
                if (null != repaymentList && !repaymentList.isEmpty()) {
                    for (int i = 0; i < repaymentList.size(); i++) {
                        LinkedHashMap<String, Object> repayment = repaymentList.get(i);
                        //每个还款日期的本金
                        BigDecimal repaymentAmount = (BigDecimal) repayment.get("repaymentAmount");
                        //每个还款日期的利息
                        BigDecimal repaymentInterest = (BigDecimal) repayment.get("repaymentInterest");
                        //每个还款日期
                        Date repaymentDate = (Date) repayment.get("repaymentDate");
                        //计算资产包本金（万元）
                        assetsPackageAmount = assetsPackageAmount.add(repaymentAmount);
                        //还款日期>=封包日
                        if (repaymentDate.compareTo(assetsPackageCloseTime) >= 0) {
                            //资产包剩余本金（万元）
                            assetsPackageResidueAmount = assetsPackageResidueAmount.add(repaymentAmount);
                            //资产包剩余本息（万元）
                            residueAmountAndInterest = residueAmountAndInterest.add(repaymentInterest).add(repaymentAmount);
                        }
                    }
                    //单笔资产平均本金余额（万元）= 资产包本金（万元）/资产笔数
                    singleAvgAssetsAmount = new BigDecimal(String.valueOf(assetsPackageAmount.divide(new BigDecimal(assetsPakege.getAssetsNum()), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                    //单笔资金本金list
                    List<BigDecimal> singleAssetsAmountList = new ArrayList<BigDecimal>();
                    //单笔资金剩余本金list
                    List<BigDecimal> singleAssetsResidueAmountList = new ArrayList<BigDecimal>();
                    //总时长（日）list
                    List<Integer> sumTimeList = new ArrayList<Integer>();
                    //距封包日已过（日）list
                    List<Integer> pastTimeList = new ArrayList<Integer>();
                    //剩余偿还周期（日)list
                    List<Integer> residueTimeList = new ArrayList<Integer>();
                    if (null != assetList && !assetList.isEmpty()) {
                        for (int i = 0; i < assetList.size(); i++) {
                            int assetId = assetList.get(i).getId();
                            BigDecimal singleAssetsAmount = new BigDecimal("0.00");
                            BigDecimal singleAssetsResidueAmount = new BigDecimal("0.00");
                            //一种资产的还款计划日期list 用于计算该资产的期限时间
                            List<Date> singleAssetsDateListLin = new ArrayList<Date>();
                            //总时长（日）
                            int sumTime = 0;
                            //距封包日已过（日）
                            int pastTime = 0;
                            //剩余偿还周期（日)
                            int residueTime = 0;
                            for (int j = 0; j < repaymentList.size(); j++) {
                                int assetId2 = (int) repaymentList.get(j).get("assetsId");
                                BigDecimal repaymentAmount = (BigDecimal) repaymentList.get(j).get("repaymentAmount");
                                Date repaymentDate = (Date) repaymentList.get(j).get("repaymentDate");
                                if (assetId == assetId2) {
                                    if (repaymentAmount.compareTo(BigDecimal.ZERO) != 0) {
                                        singleAssetsAmount = singleAssetsAmount.add(repaymentAmount);
                                    }
                                    if (null != repaymentDate) {
                                        singleAssetsDateListLin.add(repaymentDate);
                                    }
                                    //还款日期>=封包日
                                    if (repaymentDate.compareTo(assetsPackageCloseTime) >= 0) {
                                        singleAssetsResidueAmount = singleAssetsResidueAmount.add(repaymentAmount);
                                    }
                                }
                            }
                            //投放日期
                            Date putDate = assetList.get(i).getPutDate();
                            if (null != putDate) {
                                try {
                                    putDate = sd.parse(sd.format(putDate));
                                } catch (Exception e) {
                                    logger.info("时间格式化报错", e);
                                }
                            }
                            //计算时间
                            //总时长（日）= 还款计划的最后一个日期 - 投放日期
                            sumTime = DateUtils.dateDiff(singleAssetsDateListLin.get(singleAssetsDateListLin.size() - 1), putDate);
                            sumTimeList.add(sumTime);
                            //距封包日已过（日）= 封包日 - 投放日期
                            pastTime = DateUtils.dateDiff(assetsPackageCloseTime, putDate);
                            pastTimeList.add(pastTime);
                            //距封包日已过（日）= 封包日 - 投放日期
                            residueTime = DateUtils.dateDiff(singleAssetsDateListLin.get(singleAssetsDateListLin.size() - 1), assetsPackageCloseTime);
                            residueTimeList.add(residueTime);
                            singleAssetsAmountList.add(singleAssetsAmount);
                            singleAssetsResidueAmountList.add(singleAssetsResidueAmount);
                        }
                    }
                    if (null != singleAssetsAmountList && !singleAssetsAmountList.isEmpty()) {
                        BigDecimal weightedAvgLin1 = new BigDecimal("0.00");
                        BigDecimal weightedAvgLin2 = new BigDecimal("0.00");
                        BigDecimal weightedAvgLin3 = new BigDecimal("0.00");
                        for (int i = 0; i < singleAssetsAmountList.size(); i++) {
                            //资产包本金不为0
                            if (assetsPackageAmount.compareTo(BigDecimal.ZERO) != 0) {
                                weightedAvgLin1 = weightedAvgLin1.add(singleAssetsAmountList.get(i).divide(assetsPackageAmount, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(sumTimeList.get(i))));
                                weightedAvgLin2 = weightedAvgLin2.add(singleAssetsAmountList.get(i).divide(assetsPackageAmount, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(pastTimeList.get(i))));
                            }
                            //资产包剩余本金不为0
                            if (assetsPackageResidueAmount.compareTo(BigDecimal.ZERO) != 0) {
                                weightedAvgLin3 = weightedAvgLin3.add(singleAssetsResidueAmountList.get(i).divide(assetsPackageResidueAmount, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(residueTimeList.get(i))));
                            }
                        }
                        //加权平均期限（年）
                        weightedAvgYear = weightedAvgLin1.divide(new BigDecimal(365), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //加权平均剩余期限（年）
                        weightedAvgResidueYear = weightedAvgLin2.divide(new BigDecimal(365), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //加权平均资产账龄（年）
                        weightedAvgAssetYear = weightedAvgLin3.divide(new BigDecimal(365), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //对单笔资金本金list排序（从小到大）
                        Collections.sort(singleAssetsAmountList);
                        //单笔资产最低本金金额（万元）
                        singleAssetsMinAmount = singleAssetsAmountList.get(0);
                        //单笔资产最高本金金额（万元）
                        singleAssetsMaxAmount = singleAssetsAmountList.get(singleAssetsAmountList.size() - 1);
                    }
                    //资产包本金不为0
                    if (assetsPackageAmount.compareTo(BigDecimal.ZERO) != 0) {
                        //单笔资产最高本金金额占比=单笔资产最高本金金额（万元）/资产包本金（万元）
                        singleAssetsMaxAmountPercent = singleAssetsMaxAmount.divide(assetsPackageAmount, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //单笔资产最低本金金额占比=单笔资产最低本金金额（万元）/资产包本金（万元）
                        singleAssetsMinAmountPercent = singleAssetsMinAmount.divide(assetsPackageAmount, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                }
            }
        }
        //资产包实体信息
        resultMap.put("assetsPakege", assetsPakege);
        //本金（万元）
        resultMap.put("assetsPackageAmount", new BigDecimal(String.valueOf(assetsPackageAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        //资产包剩余本金（万元）
        resultMap.put("assetsPackageResidueAmount", new BigDecimal(String.valueOf(assetsPackageResidueAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        //资产包剩余本息（万元）
        resultMap.put("residueAmountAndInterest", new BigDecimal(String.valueOf(residueAmountAndInterest.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        //单笔资产平均本金余额（万元）
        resultMap.put("singleAvgAssetsAmount", new BigDecimal(String.valueOf(singleAvgAssetsAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        //单笔资产最高本金金额（万元）
        resultMap.put("singleAssetsMaxAmount", new BigDecimal(String.valueOf(singleAssetsMaxAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        //单笔资产最高本金金额占比
        resultMap.put("singleAssetsMaxAmountPercent", singleAssetsMaxAmountPercent);
        //单笔资产最低本金金额（万元）
        resultMap.put("singleAssetsMinAmount", new BigDecimal(String.valueOf(singleAssetsMinAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        //单笔资产最低本金金额（万元）
        resultMap.put("singleAssetsMinAmountPercent", singleAssetsMinAmountPercent);
        resultMap.put("weightedAvgYear", weightedAvgYear);
        resultMap.put("weightedAvgResidueYear", weightedAvgResidueYear);
        resultMap.put("weightedAvgAssetYear", weightedAvgAssetYear);

        System.err.println(JSONObject.toJSONString(resultMap));
        request.setAttribute("assetsPackageId", assetsPackageId);
        request.setAttribute("resultMap", resultMap);
        return "/assetsPackage/assetPackageAnalysis";
    }

    /**
     * @Description: 获取还款信息
     * @Author: wxn
     * @Date: 2017/12/29 14:13:11
     * @Param:
     * @Return
     */
    @PostMapping("getRepaymentDataList")
    @ResponseBody
    public Map<String, Object> getRepaymentDataList(HttpServletRequest request, @RequestParam("assetsPackageId") String assetsPackageId) {
        Map<String, Object> repaymentMap = new HashMap<String, Object>();
        if (UsedUtil.isNotNull(assetsPackageId)) {
            /**
             * 按月统计当前资产包下所有的还款计划信息
             */
            List<LinkedHashMap<String, Object>> countRepaymentList = new ArrayList<LinkedHashMap<String, Object>>();
            try {
                countRepaymentList = assetsPackageApi.countRepaymentList(Integer.valueOf(assetsPackageId));
            } catch (Exception e) {
                logger.info("根据资产包id查询还款计划信息报错，报错原因：", e);
            }
            //连续日期list 即横坐标list
            List<String> dateList = new ArrayList<String>();
            //流入金额list = 本金 + 利息
            List<BigDecimal> amountInterestSumList = new ArrayList<BigDecimal>();
            //本金list
            List<BigDecimal> amountList = new ArrayList<BigDecimal>();
            //利息list
            List<BigDecimal> interestList = new ArrayList<BigDecimal>();
            if (null != countRepaymentList && !countRepaymentList.isEmpty()) {
                String startDate = (String) countRepaymentList.get(0).get("repaymentDate");
                String endDate = (String) countRepaymentList.get(countRepaymentList.size() - 1).get("repaymentDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                if (UsedUtil.isNotNull(startDate) && UsedUtil.isNotNull(endDate)) {
                    try {
                        dateList = DateUtils.getBetweenDates(sdf.parse(startDate), sdf.parse(endDate));
                    } catch (Exception e) {
                        logger.info("根据两个时间获取中间日期报错，报错原因：", e);
                    }
                }
                if (null != dateList && !dateList.isEmpty()) {
                    for (int i = 0; i < dateList.size(); i++) {
                        String date = dateList.get(i);
                        if (UsedUtil.isNotNull(date)) {
                            date = date.substring(0, date.lastIndexOf("-"));
                        }
                        amountList.add(new BigDecimal("0.00"));
                        interestList.add(new BigDecimal("0.00"));
                        amountInterestSumList.add(new BigDecimal("0.00"));
                        for (int j = 0; j < countRepaymentList.size(); j++) {
                            String date2 = (String) countRepaymentList.get(j).get("repaymentDate");
                            if (UsedUtil.isNotNull(date2)) {
                                if (date == date2 || date.equals(date2)) {
                                    amountList.set(i, (BigDecimal) countRepaymentList.get(j).get("repaymentAmount"));
                                    interestList.set(i, (BigDecimal) countRepaymentList.get(j).get("repaymentInterest"));
                                    amountInterestSumList.set(i, (BigDecimal) countRepaymentList.get(j).get("repaymentAmountInterestSum"));
                                }
                            }
                        }
                    }
                }
            }
            System.err.println("日期list=======" + dateList.size() + "===========" + JSON.toJSON(dateList));
            System.err.println("本金list=======" + amountList.size() + "===========" + JSON.toJSON(amountList));
            System.err.println("利息list=======" + interestList.size() + "===========" + JSON.toJSON(interestList));
            System.err.println("本息list=======" + amountInterestSumList.size() + "===========" + JSON.toJSON(amountInterestSumList));
            repaymentMap.put("dateList", dateList);
            repaymentMap.put("amountList", amountList);
            repaymentMap.put("interestList", interestList);
            repaymentMap.put("amountInterestSumList", amountInterestSumList);
        }
        return repaymentMap;
    }

    /**
     * @Description: 获取获取资产行业分布信息
     * @Author: wxn
     * @Date: 2017/12/29 14:13:11
     * @Param:
     * @Return
     */
    @PostMapping("getAssetsInsdustryList")
    @ResponseBody
    public Map<String, Object> getAssetsInsdustryList(HttpServletRequest request, @RequestParam("assetsPackageId") String assetsPackageId) {
        Map<String, Object> assetsInsdustryMap = new HashMap<String, Object>();
        if (UsedUtil.isNotNull(assetsPackageId)) {
            /**
             * 资产行业分布
             */
            List<LinkedHashMap<String, Object>> assetsInsdustryList = new ArrayList<LinkedHashMap<String, Object>>();
            try {
                assetsInsdustryList = assetsPackageApi.countAssetsInsdustryList(Integer.valueOf(assetsPackageId));
            } catch (Exception e) {
                logger.info("获取资产行业分布报错，报错原因：", e);
            }
            System.err.println("资产行业分布:====" + assetsInsdustryList);
            assetsInsdustryMap.put("assetsInsdustryList", assetsInsdustryList);
        }
        return assetsInsdustryMap;
    }

    /**
     * @Description: 获取资产区域分布
     * @Author: wxn
     * @Date: 2017/12/29 14:13:11
     * @Param:
     * @Return
     */
    @PostMapping("getAssetsAreaList")
    @ResponseBody
    public Map<String, Object> getAssetsAreaList(HttpServletRequest request, @RequestParam("assetsPackageId") String assetsPackageId) {
        Map<String, Object> assetsInsdustryMap = new HashMap<String, Object>();
        if (UsedUtil.isNotNull(assetsPackageId)) {
            /**
             * 资产区域分布
             */
            List<LinkedHashMap<String, Object>> assetsAreaList = new ArrayList<LinkedHashMap<String, Object>>();
            try {
                assetsAreaList = assetsPackageApi.countAssetsAreaList(Integer.valueOf(assetsPackageId));
            } catch (Exception e) {
                logger.info("获取资产区域分布报错，报错原因：", e);
            }
            System.err.println("资产区域分布:====" + assetsAreaList);
            assetsInsdustryMap.put("assetsAreaList", assetsAreaList);
        }
        return assetsInsdustryMap;
    }


}
