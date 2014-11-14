package com.oklink.util;

import java.util.ArrayList;
import java.util.List;

public class DbTableNameUtil {
	
	private static DbTableNameUtil instance = null; 
	private static synchronized void syncInit() {
        if (instance == null) {
        	instance = new DbTableNameUtil();
        }
    }
    public static DbTableNameUtil getInstance() {
        if (instance == null) {
        	syncInit();
        }
        return instance;
    }
    
    public DbTableNameUtil(){

    }
    
    public static String[] table_main = { 
		"test","test1"
	};
	public static String[] bbs_server_table = {};
    
    /**
	 * 返回SQL语句里所有的表名.
	 * 
	 * @param sql
	 * @return
	 */
	public static String[] getTableNameBySql(String sql) {
		String[] table_name_array = null;
		List<String> tableList = new ArrayList<String>();
		try {
			String sql_type = sql.trim().substring(0, sql.trim().indexOf(" "));
			if (sql_type == null) {
				return null;
			}
			if ("select".equals(sql_type.toLowerCase()) || "delete".equals(sql_type.toLowerCase())) {
				List<String> tableName = getSelectORDelete(sql);
				if (tableName != null && tableName.size() > 0) {
					tableList.addAll(tableName);
				}
			} else if ("insert".equals(sql_type.toLowerCase()) || "replace".equals(sql_type.toLowerCase())) {
				String temp[] = sql.trim().split("into");
				if (temp == null || temp.length <= 1) {
					temp = sql.trim().split("INTO");
				}
				if (temp != null && temp.length > 1) {
					String tmp = temp[1].trim();
					int length = 0;
					length = tmp.indexOf("values");
					if (length == -1) {
						length = tmp.indexOf("VALUES");
						if (length == -1) {
							length = tmp.indexOf("value");
							if (length == -1) {
								length = tmp.indexOf("VALUE");
								if (length == -1){
									length = tmp.indexOf("set");
									if (length == -1)
										length = tmp.indexOf("SET");
										if(length == -1)
											length = tmp.indexOf("select");
											if (length == -1)
												length = tmp.indexOf("SELECT");
								}
							}
						}
					}
					if (length <= 0)
						return null;

					tmp = tmp.substring(0, length);
					if (tmp != null && tmp.length() > 0) {
						tmp = splitStr(tmp, '(')[0].trim();
						if (tmp.length() > 0) {
							tableList.add(tmp);
						}
					}
				}

			} else if ("update".equals(sql_type.toLowerCase())) {
				String strSql = sql.trim();
				String temp[] = sql.trim().split(" ");
				int count = 0;
				for (int i = 0; temp != null && i < temp.length; i++) {
					String tmp = temp[i].trim();
					if (tmp.length() > 0 && !"from".equals(tmp.toLowerCase())) {
						count++;
					}
					if (count == 2) {
						tableList.add(tmp);
						strSql = sql.substring(sql.indexOf(tmp) + tmp.length(),
								sql.length());
						break;
					}
				}
				if (strSql.length() > 0) {
					List<String> tableName = getSelectORDelete(strSql);
					if (tableName != null && tableName.size() > 0) {
						tableList.addAll(tableName);
					}
				}
			} else {
				List<String> tableName = getSelectORDelete(sql);
				if (tableName != null && tableName.size() > 0) {
					tableList.addAll(tableName);
				}
			}

			if (tableList != null) {
				int listCount = tableList.size();
				if (listCount > 0) {
					table_name_array = new String[listCount];
					for (int i = 0; i < listCount; i++) {
						table_name_array[i] = tableList.get(i);
					}
				}
			}
		} catch (Exception ex) {
			Logs.geterrorLogger().error(ex.getMessage(),ex);  
		}

		return table_name_array;
	}

	private static List<String> getSelectORDelete(String sql) throws Exception {
		List<String> tableList = new ArrayList<String>();
		sql = replaceStr(sql, "FROM", "from");
		String temp[] = sql.trim().split("from");
		for (int i = 0; temp != null && i < temp.length; i++) {
			if (i != 0) {
				String tmp = temp[i].trim();
				if(tmp.length() == 0){
					continue;
				}
				int length = tmp.indexOf(" ");
				if (length != -1) {
					tmp = tmp.substring(0, tmp.indexOf(" "));
					length = tmp.indexOf(")");
					if (length != -1) {
						tmp = tmp.substring(0, tmp.indexOf(")"));
					}
				} else {
					length = tmp.indexOf(")");
					if (length != -1) {
						tmp = tmp.substring(0, tmp.indexOf(")"));
					}
				}
				/*
				 * 开始到第一个空格之间如果不是select的话，一定是表名
				 */
				if (tmp != null) {
					if ((tmp.charAt(0) + "").equals("(")) {

					} else {
						tableList.add(tmp);
					}
				}
			}
		}
		// join
		temp = sql.split("join");
		for (int i = 0; temp != null && i < temp.length; i++) {
			if (i != 0) {
				String tmp = temp[i].trim();
				int length = tmp.indexOf(" ");
				if (length == -1)
					continue;
				tmp = tmp.substring(0, length);
				if (tmp != null) {
					if ((tmp.charAt(0) + "").equals("(")) {

					} else {
						tableList.add(tmp);
					}
				}
			}
		}
		return tableList;
	}
	
	/**
     * 字符串替换，将 source 中的 oldString 全部换成 newString
     *
     * @param source 源字符串
     * @param oldString 老的字符串
     * @param newString 新的字符串
     * @return 替换后的字符串
     */
    public static String replaceStr(String source, String oldString, String newString) {
        StringBuffer output = new StringBuffer();
        int lengthOfSource = source.length();   // 源字符串长度
        int lengthOfOld = oldString.length();   // 老字符串长度
        int posStart = 0;   // 开始搜索位置
        int pos;            // 搜索到老字符串的位置
        String lower_s=source.toLowerCase();		//不区分大小写
        String lower_o=oldString.toLowerCase();
        while ((pos = lower_s.indexOf(lower_o, posStart)) >= 0) {
            output.append(source.substring(posStart, pos));
            output.append(newString);
            posStart = pos + lengthOfOld;
        }
        if (posStart < lengthOfSource) {
            output.append(source.substring(posStart));
        }
        return output.toString();
    }
    
    /*分割字符串*/
    public static String[] splitStr(String str,char c){
    	str+=c;
    	int n=0;
    	for(int i=0;i<str.length();i++){
    		if(str.charAt(i)==c)n++;
    	}
    	String out[] = new String[n];
    	for(int i=0;i<n;i++){
    		int index = str.indexOf(c);
    		out[i] = str.substring(0,index);
    		str = str.substring(index+1,str.length());
    	}
    	return out;
	}
    
    public static boolean filterSql(String sql){
    	/*sql = sql.toLowerCase();
    	if(sql.indexOf("delete") > -1 || sql.indexOf("update") > -1){
    		if(sql.indexOf("limit") > -1 || sql.indexOf("where") == -1){
    			return false;
    		}
    	}*/
    	return true;
    }
    
    public static boolean checkSql(String sql){
    	if(sql.toLowerCase().indexOf("insert ") > -1 && sql.toLowerCase().indexOf("into") > -1){
    		return true;
    	}
    	if(sql.toLowerCase().indexOf("select ") > -1){
    		if(sql.toLowerCase().indexOf("where") > -1 || sql.toLowerCase().indexOf("limit") > -1){
    			return true;
    		}else{
    			return false;
    		}
    	}
    	if((sql.toLowerCase().indexOf("update ") > -1 && sql.toLowerCase().indexOf(" set ") > -1) || (sql.toLowerCase().indexOf("delete ") > -1 && sql.toLowerCase().indexOf(" from ") > -1)){
    		if(sql.toLowerCase().indexOf("where") > -1){
    			return true;
    		}else{
    			return false;
    		}
    	}
    	return true;
    }
    
    
    public static String getTable(Long id,int count){
		Double ud = new Double(id);
		int rs = (int)Math.ceil(ud / count);
		String suffix = "" + rs;
		if(rs < 10){
			suffix = "00" + rs;
		}else if(rs < 100){
			suffix = "0" + rs;
		}
		return suffix;
	}
    
	public static String getTable(Long userId){
		Double ud = new Double(userId);
		int rs = (int)Math.ceil(ud / 1000000d);
		String suffix = "" + rs;
		if(rs < 10){
			suffix = "00" + rs;
		}else if(rs < 100){
			suffix = "0" + rs;
		}
		return suffix;
	}
	
	
	
}
