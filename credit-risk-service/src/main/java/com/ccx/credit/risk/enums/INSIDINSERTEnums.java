package com.ccx.credit.risk.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @ClassName:  INSIDEnums   
 * @Description:TODO(用于插入sql需要修改的数据库表)   
 * @author: 李龙 
 * @date:   2017年7月16日 下午4:27:40
 */
public enum INSIDINSERTEnums {
	// 主体表
			ABS_ENTERPRISE("company_id"),
			// 资产表
			ABS_ASSET("company_id"),
			// 公司表
			ABS_COMPANY("company_id"),
			// ip表
			ABS_IP("institution_id"),
			//报表概况表
			ABS_ENTERPRISE_REPORT("company_id"),
			// 前台用表
			 ABS_USER_FG("institution_id"),
			// 前台角色表
			ABS_ROLE_FG("company_id");

			private String insidname;

			private INSIDINSERTEnums(String insidname) {
				this.insidname = insidname;
			}

			public String getInsidname() {
				return insidname;
			}

			public static Set<INSIDINSERTEnums> checkAndGetIdName(String sql) {
				Set<INSIDINSERTEnums> set = new HashSet<>();
				for (INSIDINSERTEnums str : INSIDINSERTEnums.values()) {
					if (sql.contains(" "+str.toString().toLowerCase()+" ")
							||sql.contains(" "+str.toString().toLowerCase()+"(")
							) {
						set.add(str);
					}
				}
				return set;
			}
}
