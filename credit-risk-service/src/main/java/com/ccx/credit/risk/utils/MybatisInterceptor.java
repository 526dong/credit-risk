package com.ccx.credit.risk.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;  
import org.apache.ibatis.mapping.MappedStatement;  
import org.apache.ibatis.plugin.*;  
import org.apache.ibatis.reflection.MetaObject;  
import org.apache.ibatis.reflection.SystemMetaObject;

import com.ccx.credit.risk.enums.INSIDEnums;
import com.ccx.credit.risk.enums.INSIDINSERTEnums;
import com.ccx.credit.risk.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.*;  

/**
 * 
 * @ClassName:  MybatisInterceptor   
 * @Description:TODO(mybatis拦截器,sql补充检验)   
 * @author: lilong
 * @date:   2017年7月14日 下午5:50:51
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) }) 
public class MybatisInterceptor implements Interceptor {  
    private static final List<String> speciallist=new ArrayList<>();
   Logger logger = LogManager.getLogger(MybatisInterceptor.class);
    static{
    	speciallist.add("com.ccx.credit.risk.mapper.RoleMapper.selectRoleById");
    }
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof StatementHandler) {
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			String selectId = mappedStatement.getId();
			//logger.debug("selectId::::"+selectId);
			if(speciallist.contains(selectId)||selectId.contains("com.ccx.credit.risk.mapper.UserMapper")) {
				return invocation.proceed();
			}
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			metaStatementHandler.setValue("delegate.boundSql.sql", checkAndReturn(boundSql));
		}
		return invocation.proceed();
	}
	

	/**
	 * 拦截类型StatementHandler
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}
	
	
	public String checkAndReturn(BoundSql bound){
		String sql=bound.getSql();
		logger.debug(sql);
		if (sql.trim().toLowerCase().startsWith("select")) {			
			sql = checkInsSelectAndreturnNewSql(sql);
			sql = sql.replace(" and 1=1", " ");
		}
		else if(sql.trim().toLowerCase().startsWith("insert ")){
			sql =checkInsInsertAndreturnNewSql(sql,bound);
		}
		else if(sql.trim().toLowerCase().startsWith("update ")){
			sql =checkInsUpdateAndreturnNewSql(sql);
		}
		return sql;
	}

	/**
	 * 
	 * @Title: checkInsSelectAndreturnNewSql   
	 * @Description: TODO(sql select机构注入主方法)   
	 * @param: @param sql
	 * @param: @param co
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String checkInsSelectAndreturnNewSql(String sql) {
		//if(1==1) return  sql;
		sql = replaceSelectAll(sql);
		Set<INSIDEnums> li = INSIDEnums.checkAndGetIdName(sql);
		if (li.size() > 0) {
			User user = getUser();
			// User user=new User();user.setInstitutionId(1);
			if (li.size() > 1 || sql.split(" from ").length > 2 || sql.contains(" join ")
					|| sql.toLowerCase().split("abs_").length > 2) {
				for (INSIDEnums str : li) {
					String tableName = str.toString();
					sql = sql.replace(tableName.toLowerCase() + " ",
							new StrBuilder(" (select * from ").append(tableName).append(" where ").append(str.getInsidname())
									.append(" =").append(user.getInstitutionId()).append(") new_tb ").toString());
				}
				return checkSql(sql);
			}
			INSIDEnums table = li.iterator().next();
			if (sql.toLowerCase().contains(" where ")) {
				if (sql.replace(" ","").contains(table.getInsidname()+"=")) {
					return sql;
				}
				String sqls[] = sql.split(" where ");
				return new StringBuilder(sqls[0]).append(" where ").append(table.getInsidname()).append(" = ")
						.append(user.getInstitutionId()).append(" and ").append(sqls[1]).toString();
			}

			String name = table.toString().toLowerCase();
			String sqls[] = (sql + " ").split(" " + name + " ");
			if (sqls.length == 1 || sqls[1].trim().length() == 0) {
				return new StringBuilder(sqls[0]).append(" ").append(name).append(" ").append(" where ")
						.append(table.getInsidname()).append(" = ").append(user.getInstitutionId()).append(" ").toString();
			}
			String first[] = sqls[1].trim().split(" ");
			if ("group".equals(first[0].toLowerCase()) || "limit".equals(first[0].toLowerCase())
					|| "order".equals(first[0].toLowerCase()) || "union".equals(first[0].toLowerCase())) {
				return new StringBuilder(sqls[0]).append(" ").append(name).append(" ").append(" where ")
						.append(table.getInsidname()).append(" = ").append(user.getInstitutionId()).append(" ").append(sqls[1])
						.toString();
			}
			if (!"as".equals(first[0].toLowerCase())) {
				return new StringBuilder(sqls[0]).append(" ").append(name).append(" ").append(first[0])
						.append(" where ").append(table.getInsidname()).append(" = ").append(user.getInstitutionId()).append(" ")
						.append(sqls[1].split(first[0])[1]).toString();
			}
			StringBuilder newsql = new StringBuilder(sqls[0]).append(" ").append(name).append(" as ");
			String asstring = sqls[1].substring(sqls[1].toLowerCase().indexOf(" as ") + 4, sqls[1].length()) + " ";
			String newfirst[] = asstring.split(" ");
			return newsql.append(newfirst[0]).append(" ").append(" where ").append(table.getInsidname()).append(" = ")
					.append(user.getInstitutionId()).append(" ").append(asstring.split(newfirst[0])[1]).toString();
		}
		return sql;
	}
	/**
	 * 
	 * @Title: checkInsInsertAndreturnNewSql   
	 * @Description: TODO(insert机构入驻)   
	 * @param: @param sql
	 * @param: @param co
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String checkInsInsertAndreturnNewSql(String sql,BoundSql bound) {
		sql = replaceInsertAll(sql);
		Set<INSIDINSERTEnums> set=INSIDINSERTEnums.checkAndGetIdName(sql);
		if(set.size()!=1){
			return sql;
		}
		INSIDINSERTEnums li = set.iterator().next();
		StringBuilder newsql = new StringBuilder();
		if(! sql.contains(li.getInsidname())){
			
			String sqls[] = sql.split("\\(");
			if (sqls.length <= 2) {
				return sql;
			}
			//"insert into abs_role_fg (id, name,description, status, company_id,create_time) values (?,?,?,?,?,?,?)";
			newsql.append(sqls[0]).append("(").append(li.getInsidname())
					.append(",").append(sqls[1]).append("(").append(getUser().getInstitutionId()).append(",");
			for (int i = 2; i < sqls.length; i++) {
				if(i!=2) newsql.append("(");
				newsql.append(sqls[i]);
			}
			return newsql.toString();
		}
		String keyId=li.getInsidname();
		 if(keyId.contains("_")){
			 int i=keyId.indexOf("_");
			 keyId=new StringBuilder(keyId.substring(0,i))
					 .append(keyId.substring(i+1,i+2).toUpperCase())
					 .append(keyId.substring(i+2)).toString();
		 }
		 bound.setAdditionalParameter(keyId, getUser().getInstitutionId());
		return sql;
	}
	public int getIndexForKeyWords(String keystr,String bywath, String sql,String findwhat){
		
		String str[]=keystr.split(bywath);
		for (int i = 0; i < str.length; i++) {
			if(str[i].toLowerCase().trim().equals(findwhat.toLowerCase())){
				return i;
			}
		}
		return 0;
	}
	/**
	 * 
	 * @Title: replaceSpecial   
	 * @Description: TODO(替换 例如“?,?,?,?,?,?,?”  )   
	 * @param: @param str
	 * @param: @param bywath 通过什么符号来split
	 * @param: @param index 第几个替换
	 * @param: @param replaceWhat 替换成什么
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String replaceSpecial(String str,String bywath,int index,String replaceWhat){
		String st[] =str.split(bywath);
		StringBuilder buld=new StringBuilder();
		for (int i = 0; i < st.length; i++) {
			if(i==index){
				buld.append(replaceWhat);
			}else{
				buld.append(st[i]);
			}
			if(i!=st.length-1){
				buld.append(bywath);
			}
		}
		return buld.toString();
	}
	/**
	 * 
	 * @Title: checkInsInsertAndreturnNewSql   
	 * @Description: TODO(insert机构入驻)   
	 * @param: @param sql
	 * @param: @param co
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String checkInsUpdateAndreturnNewSql(String sql) {
		sql = replaceInsertAll(sql);
		Set<INSIDINSERTEnums> set=INSIDINSERTEnums.checkAndGetIdName(sql);
		if(set.size()!=1){
			return sql;
		}
		INSIDINSERTEnums li = set.iterator().next();
		String sqls[] = sql.toLowerCase().split(" where ");
		if (sqls.length != 2) {
			return sql;
		}
		int index= sql.toLowerCase().indexOf(" where")+6;
		StringBuilder newsql = new StringBuilder(sql.substring(0, index)).append(" ");
		newsql.append(li.getInsidname()).append(" = ").append(getUser().getInstitutionId()).append(" and ").append(sql.substring(index));
		logger.debug(">>>>>>>>>>>>>"+newsql);
		return newsql.toString();
	}
	
	/**
	 * 
	 * @Title: checkSql   
	 * @Description: TODO(sql校验)   
	 * @param: @param sql
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String checkSql(String sql) {
		int ti = 1;
		String[] s = sql.split("new_tb");
		StringBuilder newsql = new StringBuilder(s[0]);
		for (int i = 1; i < s.length; i++) {
			String keywords = s[i].trim().split(" ")[0];
			if ("join".equals(keywords) || ",".equals(keywords) || ")".equals(keywords) || "union".equals(keywords)) {
				newsql.append("ntb_").append(ti++).append(" ");
			}
			newsql.append(s[i]);
		}
		return newsql.toString();
	}

	/**
	 * 
	 * @Title: replaceAll   
	 * @Description: TODO(sql中关键词转换)   
	 * @param: @param sql
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String replaceSelectAll(String sql) {
		return sql.replace("\n", " ").replace("\t", " ").replace("\r", "").replace(" FROM ", " from ")
				.replace(" WHERE ", " where ").replace(" as ", " AS ").replace(" JOIN "," join ")+" ";
	}
	public String replaceInsertAll(String sql) {
		return sql.replace("\n", " ").replace("\t", " ").replace("\r", "").replace(" VALUES ", " values ");
	}
	
	/**
	 * 
	 * @Title: getUser   
	 * @Description: TODO(获取用户信息)   
	 * @param: @return      
	 * @return: User      
	 * @throws
	 */
	public static User getUser(){
		ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return(User) request.getRequest().getSession().getAttribute("risk_crm_user");
//		User u=new User();
//		u.setInstitutionId(1);
//		return u;
	}

	/*public static void main(String[] args) {
		String keyId="com_id";
		 int i=keyId.indexOf("_");
		 System.out.println(keyId.substring(0,i)+keyId.substring(i+1,i+2).toUpperCase()+keyId.substring(i+2));
		//String sql="update abs_role_fg  set id =? where a=b";
		//new MybatisInterceptor().checkInsUpdateAndreturnNewSql(sql);
		String aa="insert into abs_role_fg (id, name,description, status, company_id,create_time) values (?,?,?,?,?,?)";
		//System.out.println(new MybatisInterceptor().checkInsInsertAndreturnNewSql(aa));
		//System.out.println(new MybatisInterceptor().getIndexForKeyWords("insert into abs_role_fg (id, name,description, status, company_id,create_time) values (?,?,?,?,?,?,?)", "company_id"));
		//System.out.println("values (xx".split("\\(")[0]);
//		String aa = "select  id, login_name, name, password, phone, email, user_type, institution_id, status,   is_del, creater, loginNum, "
//				+ "loginTime, create_time	from abs_user_fg where 1=1" + " and is_del = 0 and login_name = ?";
//		System.out.println(new MybatisInterceptor().checkInsSelectAndreturnNewSql(aa, null));
	}*/

	public static void main(String[] args) {
	}
}