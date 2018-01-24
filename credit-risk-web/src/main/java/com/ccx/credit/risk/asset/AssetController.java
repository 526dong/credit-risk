package com.ccx.credit.risk.asset;

import com.ccx.credit.risk.api.asset.AssetApi;
import com.ccx.credit.risk.api.asset.AssetEnterpriseApi;
import com.ccx.credit.risk.api.asset.AssetOperateApi;
import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.asset.Asset;
import com.ccx.credit.risk.model.asset.AssetEnterprise;
import com.ccx.credit.risk.model.asset.AssetOperate;
import com.ccx.credit.risk.model.asset.EnterpriseList;
import com.ccx.credit.risk.util.ControllerUtil;
import com.ccx.credit.risk.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 资产创建
 * @author xzd
 * @date 2017/6/24
 */
@Controller
@RequestMapping(value="/absasset")
public class AssetController extends BasicController{
	private static Logger logger = LogManager.getLogger(AssetController.class);

	@Autowired
	private AssetApi api;
	
	@Autowired
	private AssetEnterpriseApi assetEnterpriseApi;
	
	@Autowired
	private EnterpriseApi enterpriseApi;
	
	@Autowired
	private AssetOperateApi assetOperateApi;
	
	/**
	 * 资产创建列表页
	 * @return
	 */
	@GetMapping("/list")
	public String list(HttpServletRequest request){
		//查询创建人列表
		try {
			List<User> allUser = api.findAllUser();

			if (allUser != null && allUser.size() > 0) {
				request.setAttribute("allUser", allUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询创建人列表异常", e);
		}

		return "asset/assetList";
	}
	
	/**
	 * 查询资产创建列表
	 * @return
	 */
	@PostMapping("/findAll")
	@ResponseBody
	public Map<String, Object> findAll(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);

		PageInfo<Asset> pages = new PageInfo<Asset>();
		PageHelper.startPage(getPageNum(),getPageSize());
		
		try {
			pages = api.findAll(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("资产创建列表展示信息查询失败", e);
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 验证资产名称/资产编号的唯一性
	 * @return
	 */
	@PostMapping("/validateAssetNameAndCode")
	@ResponseBody
	public Map<String, Object> validateAssetNameAndCode(HttpServletRequest request, Integer assetId,
			   @RequestParam(required = true) String name, @RequestParam(required = true) String code){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		//校验资产名称唯一性
		Map<String, Object> nameMap = validateName(name, assetId);
		//校验资产编号唯一性
		Map<String, Object> codeMap = validateCode(code, assetId);

		if (nameMap != null && codeMap != null && nameMap.get("code") != null && codeMap.get("code") != null) {
			String nameCode = String.valueOf(nameMap.get("code"));
			String codeCode = String.valueOf(codeMap.get("code"));

			if ("200".equals(nameCode) && "200".equals(codeCode)) {
				resultMap.put("code", 200);
			} else if ("200".equals(nameCode)) {
				if ("500".equals(codeCode)) {
					resultMap.put("code", 500);
					resultMap.put("msg", codeMap.get("msg"));
				}
			} else if ("500".equals(nameCode)) {
				resultMap.put("code", 500);
				resultMap.put("msg", nameMap.get("msg"));
			}
		} else {
			resultMap.put("code", 500);
			resultMap.put("msg", "查询资产信息异常");
		}

		return resultMap;
	}

	//验证资产名称的唯一性
	public Map<String, Object> validateName(String name, Integer assetId){
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int nameCount = 0;

		try {
			nameCount = api.findByName(name.trim());

			//更新
			if (assetId > 0) {
				if (nameCount > 1) {
					resultMap.put("code", 500);
					resultMap.put("msg", "该资产名称已存在，请重新添加！");
					return resultMap;
				} else {
					if (nameCount == 1) {
						Asset asset = api.findById(assetId);
						//更新时，数据库里查询到一个，说明是当前输入的，可以保存
						if (name.equals(asset.getName())) {
							resultMap.put("code", 200);
						} else {
							//不是当前查询到的，重复，不保存
							resultMap.put("code", 500);
							resultMap.put("msg", "该资产名称已存在，请重新添加！");
							return resultMap;
						}
					} else {
						resultMap.put("code", 200);
					}
				}
			} else {
				//新增
				if (nameCount > 0) {
					resultMap.put("code", 500);
					resultMap.put("msg", "该资产名称已存在，请重新添加！");
					return resultMap;
				} else {
					resultMap.put("code", 200);
				}
			}

		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("通过name查询资产信息失败", e);
			e.printStackTrace();
		}
		return resultMap;
	}

	///验证资产编号的唯一性
	public Map<String, Object> validateCode(String code, Integer assetId){
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int codeCount = 0;

		try {
			codeCount = api.findByCode(code.trim());

			if (assetId > 0) {
				if (codeCount > 1) {
					resultMap.put("code", 500);
					resultMap.put("msg", "该资产编号已存在，请重新添加！");
					return resultMap;
				} else {
					if (codeCount == 1) {
						Asset asset = api.findById(assetId);
						//更新时，数据库里查询到一个，说明是当前输入的，可以保存
						if (code.equals(asset.getCode())) {
							resultMap.put("code", 200);
						} else {
							//不是当前查询到的，重复，不保存
							resultMap.put("code", 500);
							resultMap.put("msg", "该资产编号已存在，请重新添加！");
							return resultMap;
						}
					} else {
						resultMap.put("code", 200);
					}
				}
			} else {
				if (codeCount > 0) {
					resultMap.put("code", 500);
					resultMap.put("msg", "该资产编号已存在，请重新添加！");
					return resultMap;
				} else {
					resultMap.put("code", 200);
				}
			}
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("通过code查询资产信息失败", e);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 跳转资产创建添加页面
	 * @return
	 */
	@GetMapping("/add")
	public String add(){
		return "asset/assetAdd";
	}
	
	/**
	 * 保存资产创建信息到数据库
	 */
	@PostMapping("/doAdd")
	@ResponseBody
	public Map<String,Object> doAdd(HttpServletRequest request, Asset asset){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//资产申请编号：生成一个10位随机数
		String applyCode = getApplyCode();

		//向资产表中添加创建时间
		asset.setCreateDate(new Date());
		asset.setApplyCode(applyCode);

		//创建人
		User user = ControllerUtil.getSessionUser(request);
		if (user != null) {
			asset.setCreatorName(user.getLoginName());
		}
		
		try {
			api.insert(asset);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("资产创建失败", e);
			e.printStackTrace();
		}
		
		return resultMap;
	}

	/**
	 * 获取资产申请编号-递归判断重复
	 * @return
	 */
	public String getApplyCode () {
		//资产申请编号：生成一个10位随机数
		String applyCode = ((int)((Math.random()*9+1)*1000000000))+"";

		int applyCodeCount = 0;

		//通过资产申请编号进行查询
		try {
			applyCodeCount = api.findByApplyCode(applyCode.trim());
			//有重复记录
			if (applyCodeCount > 0) {
				applyCode = getApplyCode();
			}
		} catch (Exception e) {
			logger.error("通过资产申请编号进行查询", e);
			e.printStackTrace();
		}

		return  applyCode;
	};
	
	/**
	 * 跳转资产创建查看页面
	 * @return
	 */
	@GetMapping("/detail")
	public String detail(HttpServletRequest request){
	    //获取添加的企业信息
        getEnterpriseData(request);

		return "asset/assetDetail";
	}

    /**
     * 获取添加的企业信息
     * @param request
     * xzd
     */
    public void getEnterpriseData(HttpServletRequest request){
        //资产id
        int id = Integer.parseInt((String)request.getParameter("id"));

        //获取添加的企业信息
        getAssetData(request);

        Map<String,Object> param1 = new HashMap<>();
        param1.put("assetId", id);
        param1.put("personType", "0");

        try {
            //借款人添加的企业
            List<AssetEnterprise> borrowEnterprise = assetEnterpriseApi.findEnterpriseByAssetId(param1);

            if(borrowEnterprise != null && borrowEnterprise.size()>0){
                request.setAttribute("borrowEnterprise", borrowEnterprise);
            }
        } catch (Exception e) {
            logger.error("查询借款人添加的企业失败", e);
            e.printStackTrace();
        }

        Map<String,Object> param2 = new HashMap<>();
        param2.put("assetId", id);
        param2.put("personType", "1");

        try {
            //增级方添加的企业
            List<AssetEnterprise> zjfEnterprise = assetEnterpriseApi.findEnterpriseByAssetId(param2);

            if(zjfEnterprise != null && zjfEnterprise.size()>0){
                request.setAttribute("zjfEnterprise", zjfEnterprise);
            }
        } catch (Exception e) {
            logger.error("查询增级方添加的企业失败", e);
            e.printStackTrace();
        }
    }
	
	/**
	 * 查询资产操作日志信息
	 */
	@ResponseBody
	@PostMapping("/findOperateLogInfo")
	public Map<String,Object> findOperateLogInfo(HttpServletRequest request){
		//result jsp map
		Map<String,Object> resultMap = new HashMap<>();

		//资产id
		String assetId = request.getParameter("assetId");
		
		if (!StringUtils.isEmpty(assetId)) {
			try {
				List<AssetOperate> operateLogList = assetOperateApi.findByAssetId(Integer.parseInt(assetId));
				resultMap.put("operateLogList", operateLogList);
				resultMap.put("code", 200);
			} catch (Exception e) {
				resultMap.put("code", 500);
				logger.error("查询资产操作日志信息失败", e);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 跳转资产创建录入信息页面
	 * @return
	 */
	@GetMapping("/enter")
	public String enter(HttpServletRequest request){
        //获取资产信息
        getAssetData(request);
		
		return "asset/assetEnter";
	}

	/**
	 * 查询资产关联的企业信息
	 */
	@ResponseBody
	@PostMapping("/findEnterprise")
	public Map<String,Object> findEnterprise(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		
		//资产id
		int assetId = Integer.parseInt((String)request.getParameter("assetId"));
		
		Map<String,Object> param1 = new HashMap<>();
		param1.put("assetId", assetId);
		param1.put("personType", "0");

		try {
			//借款人添加的企业
			List<AssetEnterprise> borrowEnterprise = assetEnterpriseApi.findEnterpriseByAssetId(param1);

			resultMap.put("borrowEnterprise", borrowEnterprise);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("查询借款人添加企业失败！", e);
			e.printStackTrace();
		}

		Map<String,Object> param2 = new HashMap<>();
		param2.put("assetId", assetId);
		param2.put("personType", "1");
		
		try {
			//增级方添加的企业
			List<AssetEnterprise> zjfEnterprise = assetEnterpriseApi.findEnterpriseByAssetId(param2);

			resultMap.put("zjfEnterprise", zjfEnterprise);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("查询增级方添加企业失败！", e);
			e.printStackTrace();
		}

		return resultMap;
	}
	
	/**
	 * 保存资产创建信息到数据库
	 */
	@ResponseBody
	@PostMapping("/doEnter")
	public Map<String,Object> doEnter(HttpServletRequest request, EnterpriseList enterpriseList, Integer assetId, String assetName){
		//result jsp map
		Map<String,Object> resultMap = new HashMap<>();

		//jsp get list
		List<AssetEnterprise> list = enterpriseList.getEnterpriseList();
		//database save batchList
		List<AssetEnterprise> batchList = new ArrayList<>();

		//先删除
        try {
            assetEnterpriseApi.deleteById(assetId);
            resultMap.put("code", 200);
        } catch (Exception e) {
            resultMap.put("code", 500);
            logger.error("资产删除失败！", e);
            e.printStackTrace();
        }

		//1、更新资产
		try {
			Asset asset = new Asset();
			asset.setId(assetId);
			//asset.setType(1);
			asset.setName(assetName);
			asset.setUpdateDate(new Date());

			api.update(asset);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("资产更新失败！", e);
			e.printStackTrace();
		}

		//2、循环放到batchist中
		for (AssetEnterprise ent : list ) {
			ent.setAssetId(assetId);
			ent.setCreateTime(new Date());

			batchList.add(ent);
		}

		//3、批量添加到数据库
		try{
			assetEnterpriseApi.batchInsertEnterprise(batchList);
			resultMap.put("code", 200);
		}catch(Exception e){
			resultMap.put("code", 500);
			logger.error("借款人批量添加企业失败!", e);
			e.printStackTrace();
		}

		return resultMap;
	}

    /**
     * 跳转资产创建更新页面
     * @return
     */
    @GetMapping("/update")
    public String update(HttpServletRequest request){
        //获取资产信息
        getAssetData(request);

        return "asset/assetUpdate";
    }

    /**
     * 获取资产信息
     * @return
     */
    public void getAssetData(HttpServletRequest request){
        //资产id
        int id = Integer.parseInt((String)request.getParameter("id"));

        try {
            Asset asset = api.findById(id);

            if (asset != null){
                request.setAttribute("asset", asset);
                request.setAttribute("assetId", id);
            }
        } catch (Exception e) {
            logger.error("查询资产失败", e);
            e.printStackTrace();
        }
    }

	/**
	 * 修改-借款人添加企业
	 */
	@ResponseBody
	@PostMapping("/doUpdate")
	public Map<String,Object> doUpdate(HttpServletRequest request, EnterpriseList enterpriseList){
		//result jsp map
	    Map<String,Object> resultMap = new HashMap<>();
		
		//页面获取的list
		List<AssetEnterprise> list = enterpriseList.getEnterpriseList();
		
		//保存到数据库的借款人list
		List<AssetEnterprise> batchBorrowlist = new ArrayList<AssetEnterprise>();
		//保存到数据库的增级方list
		List<AssetEnterprise> batchZjflist = new ArrayList<AssetEnterprise>();
		
		//资产id-前台已经做了判空处理
		String assetId = request.getParameter("assetId");

		//中文乱码
		String name = request.getParameter("assetName");
		String code = request.getParameter("assetCode");

		Asset asset = new Asset();

		asset.setId(Integer.parseInt(assetId));
		asset.setName(name);
		asset.setCode(code);
		asset.setUpdateDate(new Date());

		try  {
			api.update(asset);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("更新资产创建失败", e);
			e.printStackTrace();
			return  resultMap;
		}
		
		//资产操作日志
		String assetOperatelog = "";
		
		//原则：先删除，再增加，再修改
		//一、留痕处理-删除借款人企业
		String borrowDelLog = getDelLog(request, "0", assetId);
				
		//二、留痕处理-增加借款人企业
		String borrowAddLog = getAddLog(request, "0", list, batchBorrowlist, assetId);
		
		//三、留痕处理-更新借款人企业
		String borrowUpdateLog = getUpdateLog(request, "0", list, assetId);

        //原则：先删除，再增加，再修改
        //一、留痕处理-删除借款人企业
        String zjfDelLog = getDelLog(request, "1", assetId);

        //二、留痕处理-增加借款人企业
        String zjfAddLog = getAddLog(request, "1", list, batchZjflist, assetId);

        //三、留痕处理-更新借款人企业
        String zjfUpdateLog = getUpdateLog(request, "1", list, assetId);

        String borrowLog = borrowDelLog + "" + borrowAddLog + "" + borrowUpdateLog;
        String zjfLog = zjfDelLog +""+ zjfAddLog +""+ zjfUpdateLog;;
        //拼接日志信息
		assetOperatelog = borrowLog + "" + zjfLog;
		/*//拼接字符串
		if (("借款人操作".equals(borrowLog) && "增级方操作".equals(zjfLog))) {
			assetOperatelog = borrowLog + "" + zjfLog;
		} else {
			if ("借款人操作".equals(borrowLog)) {
				assetOperatelog = zjfLog;
			} else if ("增级方操作".equals(zjfLog)) {
				assetOperatelog = borrowLog;
			} else {
				assetOperatelog = borrowLog + "" + zjfLog;
			}
		}*/
		if (!"".equals(assetOperatelog)) {
			//记录资产操作
			AssetOperate operate = new AssetOperate();

			//创建人
			User user = ControllerUtil.getSessionUser(request);
			if (user != null) {
				operate.setOperator(user.getLoginName());
			}

			operate.setOperateTime(new Date());
			operate.setOperateRecord(assetOperatelog);
			operate.setAssetId(Integer.parseInt(assetId));
			
			try {
				assetOperateApi.insert(operate);
                resultMap.put("code", 200);
			} catch (Exception e) {
                resultMap.put("code", 500);
                logger.error("资产操作日志增加记录失败！", e);
                e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * @author xzd
	 * 留痕-修改-借款人/增级方-删除企业
	 * @param type 借款人/增级方
	 * @param assetId 资产id
	 */
	public String getDelLog(HttpServletRequest request, String type, String assetId){
		//资产操作日志信息
		String assetOperatelog = "";
		
		String delId = "";

        if ("0".equals(type)) {
            //借款人删除-企业id集合
            delId = request.getParameter("borrowDelEnterpriseIds");
        } else {
            //增级方删除-企业id集合
            delId = request.getParameter("zjfDelEnterpriseIds");
        }
		
		if (StringUtils.isNotBlank(delId)) {
			delId = delId.substring(0, delId.length()-1);
			String[] delIds = delId.split(",");
			
			for (int i = 0; i < delIds.length; i++) {
				if (StringUtils.isNotBlank(delIds[i])) {
					Map<String,Object> param = new HashMap<>();
					
					param.put("assetId", assetId);
					param.put("enterpriseId", delIds[i]);
					//借款人
					param.put("personType", type);
					
					try {
						//通过id查询资产企业关联信息
						AssetEnterprise bean =  assetEnterpriseApi.findByEnterpriseId(param);
						
						if (bean != null) {
							//删除
							try {
								assetEnterpriseApi.deleteById(bean.getId());
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("资产信息删除借款人关联企业失败", e);
							}
							
							//记录-向abs_asset_operate表中添加留痕记录-删除借款人/增级方企业
							if ("0".equals(type)) {
								assetOperatelog += "借款人删除"+bean.getEnterpriseName()+" ; ";
							} else if ("1".equals(type)) {
								assetOperatelog += "增级方删除"+bean.getEnterpriseName()+" ; ";
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("查询资产关联企业信息失败", e);
					}
				}	
			}
		}
		
		return assetOperatelog;
	}
	
	/**
	 * @author xzd
	 * 留痕-修改-借款人/增级方-添加企业
	 * @param type 借款人/增级方
	 * @param list 页面获取的list
	 * @param batchlist 保存到数据库的list
	 * @param assetId 资产id
	 */
	public String getAddLog(HttpServletRequest request, String type, List<AssetEnterprise> list,
			List<AssetEnterprise> batchlist, String assetId){
		//资产操作日志信息
		String assetOperatelog = "";
		
		String addId = "";
		
		if ("0".equals(type)) {
			//借款人添加-企业id集合
			addId = request.getParameter("borrowAddEnterpriseIds");
		} else {
			//增级方添加-企业id集合
			addId = request.getParameter("zjfAddEnterpriseIds");
		}
		
		if (StringUtils.isNotBlank(addId)) {
			addId = addId.substring(0, addId.length()-1);
			String[] addIds = addId.split(",");
			
			for (int i = 0; i < addIds.length; i++) {
				if (StringUtils.isNotBlank(addIds[i])) {
					if (list != null && list.size()>0) {
						for (AssetEnterprise bean:list){
							if (bean != null) {
								if (bean.getEnterpriseId() != null) {
									if (Integer.parseInt(addIds[i]) == bean.getEnterpriseId()) {

										bean.setCreateTime(new Date());
										bean.setPersonType(type);
										bean.setAssetId(Integer.parseInt(assetId));

										batchlist.add(bean);

										//记录-向abs_asset_operate表中添加留痕记录-添加借款人/增级方企业主体
										if ("0".equals(type)) {
											if (bean.getAffordAllDebt() != null) {
												if (bean.getAffordAllDebt() == 1) {
													assetOperatelog += "添加借款人"+bean.getEnterpriseName()+",承担全部债务 ; ";
												} else {
													assetOperatelog += "添加借款人"+bean.getEnterpriseName()+",不承担全部债务 ; ";
												}
											}
										} else if ("1".equals(type)){
											if (bean.getAffordAllDebt() != null) {
												if (bean.getAffordAllDebt() == 1) {
													assetOperatelog += "添加增级方"+bean.getEnterpriseName()+",承担全部债务 ; ";
												} else {
													assetOperatelog += "添加增级方"+bean.getEnterpriseName()+",不承担全部债务 ; ";
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			//添加
			try{
				assetEnterpriseApi.batchInsertEnterprise(batchlist);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("批量添加企业失败!", e);
			}
		}
		
		return assetOperatelog;
	}
	
	/**
	 * @author xzd
	 * 留痕-修改-借款人/增级方-更新企业
	 * @param type 借款人/增级方
	 * @param list 页面获取的list
	 * @param assetId 资产id
	 */
	public String getUpdateLog(HttpServletRequest request, String type, List<AssetEnterprise> list, String assetId){
		//资产操作日志信息
		String assetOperatelog = "";
		
		String updateId = "";
		
		if ("0".equals(type)) {
			//借款人添加-企业id集合
			updateId = request.getParameter("borrowUpdateEnterpriseIds");
		} else {
			//增级方添加-企业id集合
			updateId = request.getParameter("zjfUpdateEnterpriseIds");
		}
		
		if (StringUtils.isNotBlank(updateId)) {
			updateId = updateId.substring(0, updateId.length()-1);
			String[] updateIds = updateId.split(",");
			
			for (int i = 0; i < updateIds.length; i++) {
				if (StringUtils.isNotBlank(updateIds[i])) {
					if (list != null && list.size()>0) {
						for (AssetEnterprise bean:list){
							if (bean != null) {
								if (bean.getEnterpriseId() != null && Integer.parseInt(updateIds[i]) == bean.getEnterpriseId()) {
									Map<String,Object> param = new HashMap<>();

									param.put("assetId", assetId);
									param.put("enterpriseId", updateIds[i]);
									//借款人
									param.put("personType", type);
									AssetEnterprise assetEnterprise = assetEnterpriseApi.findByEnterpriseId(param);

									boolean affordFlag = false;
									//判断企业主体，是否承担全部债务是否修改过
									if (assetEnterprise.getAffordAllDebt() == null) {
										if (bean.getAffordAllDebt() != null) {
											affordFlag = true;//修改
										} else {
											affordFlag = false;//未修改
										}
									}  else  {
										if (bean.getAffordAllDebt() == null) {
											affordFlag = true;
										} else {
											if (assetEnterprise.getAffordAllDebt().equals(bean.getAffordAllDebt())) {
												affordFlag = false;
											} else {
												affordFlag = true;
											}
										}
									}

									boolean typeFlag = false;
									if ("0".equals(type)) {
										if (affordFlag) {
											assetEnterprise.setAffordAllDebt(bean.getAffordAllDebt());

											try {
												assetEnterpriseApi.updateByAsset(assetEnterprise);
											} catch (Exception e) {
												e.printStackTrace();
												logger.error("资产企业更新失败！", e);
											}

											//记录-向abs_asset_operate表中添加留痕记录-修改借款人企业
											assetOperatelog += "修改借款人信息 ; ";
										}
									} else if ("1".equals(type)) {
										//判断企业主体，增级方是否修改过
										if (assetEnterprise.getType() == null) {
											if (bean.getType() != null) {
												typeFlag = true;//修改
											} else {
												typeFlag = false;//未修改
											}
										}  else  {
											if (bean.getType() == null) {
												typeFlag = true;
											}else {
												if (assetEnterprise.getType().equals(bean.getType())) {
													typeFlag = false;
												} else {
													typeFlag = true;
												}
											}
										}
										//是否承担全部债务修改
										if (affordFlag) {
											assetEnterprise.setAffordAllDebt(bean.getAffordAllDebt());
										}
										//增级方类型修改
										if (typeFlag) {
											assetEnterprise.setType(bean.getType());
										}

										try {
											assetEnterpriseApi.updateByAsset(assetEnterprise);
										} catch (Exception e) {
											e.printStackTrace();
											logger.error("资产企业更新失败！", e);
										}

										//记录-向abs_asset_operate表中添加留痕记录-修改增级方企业
										assetOperatelog += "修改增级方信息 ; ";
									}
								}
							}
						}
					}
				}
			}
		}
		
		return assetOperatelog;
	}
	
	/**
	 * 资产定价
	 */
	@GetMapping("/assetPrice")
    public String assetPrice(HttpServletRequest request){
		return "asset/assetPrice";
   }

	/**
	 * 资产定价列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@PostMapping("/findAssetPrice")
	public Map<String, Object> findAssetPrice(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		List<Asset> priceList = new ArrayList<>();

		try {
			priceList = api.findAllAsset();
			resultMap.put("priceList", priceList);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("资产定价列表查询失败", e);
			e.printStackTrace();
		}

		return resultMap;
	}
	
}
