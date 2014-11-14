package com.oklink.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.sql.DataSource;


public class Db {
	private static final ThreadLocal<Integer> serverPool = new ThreadLocal<Integer>();
	private static final ThreadLocal<Connection> connPooMain = new ThreadLocal<Connection>();
	private static final ThreadLocal<Connection> connPoolBBS = new ThreadLocal<Connection>();


	public static Map<String, DataSource> dsmap = new HashMap<String, DataSource>();

	private static String [] SERVERS_NAME = {"SERVER_NOTSET","SERVER_MAIN","SERVER_BBS"}; 

	public static final int SERVER_NOTSET = 0;
	public static final int SERVER_MAIN = 1;
	public static final int SERVER_BBS = 2;	//bbs库连接

	
	private static Set<String> tablename_main = new HashSet<String>();
	private static Set<String> bbs_set = new HashSet<String>();
		
	static {
		for (String str : DbTableNameUtil.table_main)		{	tablename_main.add(str);	}
		for (String str : DbTableNameUtil.bbs_server_table) 	{	bbs_set.add(str);			} 
		
		try {
			InitialContext ctx = new InitialContext();
			IniReader reader = IniReader.getInstance(2);
			Properties p = reader.getSection("database");
			if (p != null) {
				Enumeration<Object> keys = p.keys();
				Map<String, DataSource> _dsmap = new HashMap<String, DataSource>();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					String value = p.getProperty(key);
					System.out.println(key + " = " + value);
					DataSource dsW = _dsmap.get(value);
					if (dsW==null){
						dsW = (DataSource) ctx.lookup(value);
						_dsmap.put(value, dsW);
					}
					dsmap.put(key, dsW);
				}
			}

		} catch (Exception ex) {
			Logs.geterrorLogger().error(ex.getMessage(), ex);
		}
	}
	
	public static Connection getConn(int server) {// 写或有读有写
		try{
			if (server == SERVER_MAIN|| server == SERVER_NOTSET ) {
				Connection connMain = (Connection) connPooMain.get();
				if (connMain == null||connMain.isClosed()) {
					connMain = getConnMain();
					connPooMain.set(connMain);
				}
				return connMain;
			}
	
			if (SERVER_BBS == server) { // bbs库，不区分读写
				Connection connBBS = (Connection) connPoolBBS.get();
				if (connBBS == null||connBBS.isClosed()) {
					connBBS = getConnBBS();
					connPoolBBS.set(connBBS);
				}
				return connBBS;
			}
		}catch(Exception ex){
			Logs.geterrorLogger().error(ex.getMessage(), ex);
		}

		return null;
	}

	public static Connection getConn_notry(int server) {// 写或有读有写
		try{
			if (server == SERVER_MAIN|| server == SERVER_NOTSET ) {
				Connection connMain = (Connection) connPooMain.get();
				return connMain;
			}
	
			if (SERVER_BBS == server) { // bbs库，不区分读写
				Connection connBBS = (Connection) connPoolBBS.get();
				return connBBS;
			}
		}catch(Exception ex){
			Logs.geterrorLogger().error(ex.getMessage(), ex);
		}

		return null;
	}
	
	public static Connection getConnMain() {// 纯写或有读有
		try {
			DataSource dsMain = dsmap.get("datasourcecoin");
			long start = new Date(System.currentTimeMillis()).getTime();
			Connection conn = dsMain.getConnection();
			long end = new Date(System.currentTimeMillis()).getTime();
			if((end-start)>2000){
				Logs.getslowLogger().warn("---getConnMain() 1 time is slow"+",time is "+(end-start));
			}
			if(conn.isValid(5)){
				return conn;
			}else{
				conn.close();
				start = new Date(System.currentTimeMillis()).getTime();
				conn = dsMain.getConnection();
				end = new Date(System.currentTimeMillis()).getTime();
				if((end-start)>2000){
					Logs.getslowLogger().warn("---getConnMain() 2 time is slow"+",time is "+(end-start));
				}
				if(conn.isValid(5)){
					return conn;
				}else{
					conn.close();
					start = new Date(System.currentTimeMillis()).getTime();
					conn = dsMain.getConnection();
					end = new Date(System.currentTimeMillis()).getTime();
					if((end-start)>2000){
						Logs.getslowLogger().warn("---getConnMain() 3 time is slow"+",time is "+(end-start));
					}
					if(conn.isValid(5)){
						return conn;
					}else{
						return null;
					}
				}
			}
		} catch (Exception ex) {
			Logs.geterrorLogger().error(ex.getMessage(), ex);
		}
		return null;
	}

	public static Connection getConnBBS() { // BBS库
		try {
			DataSource dsBbsDb = dsmap.get("datasourcebbs");
			long start = new Date(System.currentTimeMillis()).getTime();
			Connection conn = dsBbsDb.getConnection();
			long end = new Date(System.currentTimeMillis()).getTime();
			if((end-start)>2000){
				Logs.getslowLogger().warn("---getConnBBS() is slow"+",time is "+(end-start));
			}
			return conn;
		} catch (Exception ex) {
			Logs.geterrorLogger().error(ex.getMessage(), ex);
		}
		return null;
	}
	
	private static int getServer(String sql, Object[] parm) { 
		int server = SERVER_NOTSET;
		String[] tablename = DbTableNameUtil.getTableNameBySql(sql);
		if (tablename == null || tablename.length < 1) {
			return SERVER_NOTSET;
		}
		for (int i = 0; i < tablename.length; i++) {
			String tmp = tablename[i];
			tmp = StringUtil.replaceStr(tmp, "`", "").trim();
			if (tablename_main.contains(tmp)) {
				server = SERVER_MAIN;
				break;
			}else if (bbs_set.contains(tmp)) {
				server = SERVER_BBS;
				break;
			} 
			/* else if (tablename_set_109.contains(tmp)) {
				server = SERVER_109;
				break;
			} else if (tablename_set_109_hash.contains(tmp)) {
				server = SERVER_109;
				break;
			}*/
		}
		return server; 
	}

	
	

	public static long executeUpdate(String sql) {
		return executeUpdate(sql, null, SERVER_NOTSET);
	}

	public static long executeUpdate(String sql, Object[] parm) {
		return executeUpdate(sql, parm, SERVER_NOTSET);
	}


	public static long executeUpdate(String sql, Object[] parm , int server){
		if(!DbTableNameUtil.checkSql(sql)){
			Logs.geterrorLogger().error("危险的sql不予执行 ： " + sql);
			return 0;
		}
		PreparedStatement pstmt = null;
		Connection conn = null;
		if (server == SERVER_NOTSET){
			Integer s = (Integer) serverPool.get();
			if(s == null){
				server = getServer(sql,parm);
			}else{
				server = s.intValue();
			}
		}

		try {
			conn = getConn(server);

			if(sql == null){
				return 0;
			}
			boolean ifinsert = false;
			if (sql.indexOf("insert") > -1 || sql.indexOf("INSERT") > -1)
				ifinsert = true;
			if (ifinsert == true) {
				pstmt = conn.prepareStatement(sql,	Statement.RETURN_GENERATED_KEYS);
			} else {
				pstmt = conn.prepareStatement(sql);
			}
			if (parm != null && parm.length > 0) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			if (ifinsert) {
				long result = pstmt.executeUpdate();
				ResultSet keys = pstmt.getGeneratedKeys();
				long id = 0;
				if (keys.next()) {
					id = keys.getLong(1);
				}
				if(id != 0){
					result = id;
				}
				return result;
			} else {
				if(DbTableNameUtil.filterSql(sql)){
					return pstmt.executeUpdate();
				}else{
					throw new Exception("delete or update sql is error : " + sql);
				}
			}

		} catch (Exception ex) {
			String em = ex.getMessage();
			if (em != null && em.indexOf("Duplicate entry") >= 0) {
				String param = "param : ";
				if(parm != null){
					for(int i = 0 ; i < parm.length ; i++){
						param += parm[i] + ",";
					}
				}
				if(sql != null && sql.indexOf("insert into btc_user") < 0){
					Logs.geterrorLogger().error("Duplicate entry ,error sql:" + sql +" param :"+param);
				}
			} else {
				String[] tbName= DbTableNameUtil.getTableNameBySql(sql);
				String tb = "";
				if (tbName!=null){
					for (String string : tbName) { 
						tb += string + ",";
					}
				}else{
					tb = "NULL table name";
				}
				String param = "param : ";
				if(parm != null){
					for(int i = 0 ; i < parm.length ; i++){
						param += parm[i] + ",";
					}
				}
				Logs.geterrorLogger().error("db.executeUpdate error \r\n" + sql +"\r\ntable name "+ tb + "\r\n" + SERVERS_NAME[server] + "\r\n" + " " + param + "\r\n", ex);
			}
			return -1;
		} finally {
			closePstmt(pstmt);
			releaseConnection(); 
		}
	}
	
	public static long executeUpdate_notry(String sql, Object[] parm) throws Exception {
		return executeUpdate_notry(sql, parm, SERVER_NOTSET);
	}
	public static long executeUpdate_notry(String sql, Object[] parm , int server) throws Exception{
		if(!DbTableNameUtil.checkSql(sql)){
			Logs.geterrorLogger().error("危险的sql不予执行 ： " + sql);
			return 0;
		}
		PreparedStatement pstmt = null;
		Connection conn = null;
		if (server == SERVER_NOTSET){
			Integer s = (Integer) serverPool.get();
			if(s == null){
				server = getServer(sql,parm);
			}else{
				server = s.intValue();
			}
		}

		try {
			conn = getConn_notry(server);

			if(sql == null){
				return 0;
			}
			boolean ifinsert = false;
			if (sql.indexOf("insert") > -1 || sql.indexOf("INSERT") > -1)
				ifinsert = true;
			if (ifinsert == true) {
				pstmt = conn.prepareStatement(sql,	Statement.RETURN_GENERATED_KEYS);
			} else {
				pstmt = conn.prepareStatement(sql);
			}
			if (parm != null && parm.length > 0) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			if (ifinsert) {
				long result = pstmt.executeUpdate();
				ResultSet keys = pstmt.getGeneratedKeys();
				long id = 0;
				if (keys.next()) {
					id = keys.getLong(1);
				}
				if(id != 0){
					result = id;
				}
				return result;
			} else {
				if(DbTableNameUtil.filterSql(sql)){
					return pstmt.executeUpdate();
				}else{
					throw new Exception("delete or update sql is error : " + sql);
				}
			}

		} 
		finally {
			closePstmt(pstmt);
		}
	}

	/**
	 * 批量更新
	 * 
	 * 用户表不能批量更新,因为所操作的记录在当前的连接里可能找不到.
	 * 
	 * @param sql
	 * @param parmList
	 * @return
	 */
	public static boolean executeBatchUpdate(String sql,List<Object[]> parmList) {
		if(!DbTableNameUtil.checkSql(sql)){
			Logs.geterrorLogger().error("危险的sql不予执行 ： " + sql);
			return false;
		}
		PreparedStatement pstmt = null;
		Connection conn = null;
		boolean b = false;
		try {
			int server = getServer(sql,null);
			conn = getConn(server);
			pstmt = conn.prepareStatement(sql);
			if (parmList != null && parmList.size() > 0) {
				for (int i = 0; i < parmList.size(); i++) {
					Object[] parm = parmList.get(i);
					if (parm != null && parm.length > 0) {
						for (int j = 0; j < parm.length; j++) {
							pstmt.setObject(j + 1, parm[j]);
						}
						pstmt.addBatch();
					}
				}
			}
			int[] num = pstmt.executeBatch();
			if (num != null && num.length > 0) {
				b = true;
			}
		} catch (Exception ex) {
			Logs.geterrorLogger().error("db.executeBatchUpdate error", ex);
			Logs.geterrorLogger().error("error sql:" + sql);
		} finally {
			closePstmt(pstmt);
			releaseConnection();
		}
		return b;
	}

	public static List<Map<String, Object>> executeQuery(String sql) {
		return executeQuery(sql, null);
	}
	public static List<Map<String, Object>> executeQuery(String sql,Object[] parm) {
		return executeQuery(sql, parm, SERVER_NOTSET);
	}
	public static List<Map<String, Object>> executeQuery(String sql,Object[] parm, int server) {
		//String tmpSql = sql;
		if(!DbTableNameUtil.checkSql(sql)){
			Logs.geterrorLogger().error("危险的sql不予执行 ： " + sql);
			return null;
		}
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet res = null;
		if (server == SERVER_NOTSET){
			Integer s = (Integer) serverPool.get();
			if(s == null){
				server = getServer(sql,parm);
			}else{
				server = s.intValue();
			}
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			long start = new Date(System.currentTimeMillis()).getTime();
			conn = getConn(server);
			long conntime = new Date(System.currentTimeMillis()).getTime()-start;
			
			if(sql == null){
				//error.error("in sql : " + tmpSql);
				return null;
			}
			pstmt = conn.prepareStatement(sql);
			if (parm != null && parm.length > 0) {
				for (int i = 0; i < parm.length; i++) {
					if (parm[i] == null) {
						break;
					}
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			res = pstmt.executeQuery();
			long end = new Date(System.currentTimeMillis()).getTime();
			if((end-start)>2000){
				String param = "param : ";
				if(parm != null){
					for(int i = 0 ; i < parm.length ; i++){
						param += parm[i] + ",";
					}
				}
				Logs.getslowLogger().error("server is :"+SERVERS_NAME[server]+",time is "+(end-start)+" ,get conn time is:"+conntime+" long time sql :" + sql+"   "+param);
			}
			if (res != null) {
				int columnCount = res.getMetaData().getColumnCount();
				while (res.next()) {
					Map<String, Object> resultRow = new HashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						resultRow.put(res.getMetaData().getColumnLabel(i), res.getObject(i));
					}
					result.add(resultRow);
					resultRow = null;
				}
			}
			return result;

		} catch (Exception ex) {
			String debug = "##DEBUG:";
			String [] tableName = DbTableNameUtil.getTableNameBySql(sql);
			for (String str : tableName)
				debug += str + "\r\n";
			String param = "param : ";
			if(parm != null){
				for(int i = 0 ; i < parm.length ; i++){
					param += parm[i] + ",";
				}
			}
			Logs.geterrorLogger().error(debug + "error sql:" + sql + "server is:" + SERVERS_NAME[server] + "\r\ndb.executeQuery error , " + param, ex);
			return null;
		} finally {
			closeRes(res);
			closePstmt(pstmt);
			releaseConnection();
		}
	}

	public static List<Map<String, Object>> executeQuery_notry(String sql,Object[] parm) throws SQLException {
		return executeQuery_notry(sql, parm, SERVER_NOTSET);
	}
	
	public static List<Map<String, Object>> executeQuery_notry(String sql,Object[] parm, int server) throws SQLException {
		//String tmpSql = sql;
		if(!DbTableNameUtil.checkSql(sql)){
			Logs.geterrorLogger().error("危险的sql不予执行 ： " + sql);
			return null;
		}
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet res = null;
		if (server == SERVER_NOTSET){
			Integer s = (Integer) serverPool.get();
			if(s == null){
				server = getServer(sql,parm);
			}else{
				server = s.intValue();
			}
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			long start = new Date(System.currentTimeMillis()).getTime();
			conn = getConn_notry(server);
			long conntime = new Date(System.currentTimeMillis()).getTime()-start;
			if(conn == null||conn.isClosed()){
				Logs.geterrorLogger().error("conn == null||conn.isClosed()" + sql);
				return null;
			}
			if(sql == null){
				//error.error("in sql : " + tmpSql);
				return null;
			}
			pstmt = conn.prepareStatement(sql);
			if (parm != null && parm.length > 0) {
				for (int i = 0; i < parm.length; i++) {
					if (parm[i] == null) {
						break;
					}
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			res = pstmt.executeQuery();
			long end = new Date(System.currentTimeMillis()).getTime();
			if((end-start)>2000){
				String param = "param : ";
				if(parm != null){
					for(int i = 0 ; i < parm.length ; i++){
						param += parm[i] + ",";
					}
				}
				Logs.getslowLogger().error("server is :"+SERVERS_NAME[server]+",time is "+(end-start)+" ,get conn time is:"+conntime+" long time sql :" + sql+" "+param);
			}
			if (res != null) {
				int columnCount = res.getMetaData().getColumnCount();
				while (res.next()) {
					Map<String, Object> resultRow = new HashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						resultRow.put(res.getMetaData().getColumnLabel(i), res.getObject(i));
					}
					result.add(resultRow);
					resultRow = null;
				}
			}
			return result;

		} 
		finally {
			closeRes(res);
			closePstmt(pstmt);
		}
	}

	/**
	 * 开启主库事物
	 * @throws SQLException
	 */
	public static void tran_begin()throws SQLException{
		Connection conn = null;
		serverPool.set(SERVER_MAIN);
		conn = getConn(SERVER_MAIN);
		conn.setAutoCommit(false);
	}
	/**
	 * 提交主库事物
	 */
	public static void tran_commit(){
		Integer server = (Integer) serverPool.get();
		if(server == null){
			server = SERVER_MAIN;
		}
		Connection conn = null;
		try {
			conn = getConn(server.intValue());
			serverPool.remove();
			conn.commit();
			conn.setAutoCommit(true);
//			userLog.error(Thread.currentThread().getName() + ",tran_commit conn = " + conn.hashCode());
		} catch(Exception ex){
			Logs.geterrorLogger().error(ex.getMessage() , ex);
		}finally{
			releaseConnection();
		}
	}
	/**
	 * 提交主库事物
	 */
	public static void tran_rollback(){
		Integer server = (Integer) serverPool.get();
		if(server == null){
			server = SERVER_MAIN;
		}
		Connection conn = null;
		try {
			conn = getConn(server.intValue());
			serverPool.remove();
			conn.rollback();
			conn.setAutoCommit(true);
//			userLog.error(Thread.currentThread().getName() + ",tran_rollback conn = " + conn.hashCode());
//			Logs.geterrorLogger().warn("transaction rollback"+sql);
		} catch(Exception ex){
			Logs.geterrorLogger().error(ex.getMessage() , ex);
		}finally{
			releaseConnection();
		}
	}
	public static int tran_begin(String sql)throws SQLException{
		int	server = getServer(sql,null);
		Connection conn = null;
		serverPool.set(server);
		conn = getConn(server);
		conn.setAutoCommit(false);
//			userLog.error(Thread.currentThread().getName() + ",tran_begin conn = " + conn.hashCode());
		return server;
	}
	public static void tran_commit(String sql){
		Integer server = (Integer) serverPool.get();
		if(server == null){
			server = getServer(sql,null);
		}
		Connection conn = null;
		try {
			conn = getConn_notry(server.intValue());
			serverPool.remove();
			conn.commit();
			conn.setAutoCommit(true);
//			userLog.error(Thread.currentThread().getName() + ",tran_commit conn = " + conn.hashCode());
		} catch(Exception ex){
			Logs.geterrorLogger().error(ex.getMessage() , ex);
		}finally{
			releaseConnection();
		}
	}
	public static void tran_rollback(String sql){
		Integer server = (Integer) serverPool.get();
		if(server == null){
			server = getServer(sql,null);
		}
		Connection conn = null;
		try {
			conn = getConn_notry(server.intValue());
			serverPool.remove();
			conn.rollback();
			conn.setAutoCommit(true);
//			userLog.error(Thread.currentThread().getName() + ",tran_rollback conn = " + conn.hashCode());
//			Logs.geterrorLogger().warn("transaction rollback"+sql);
		} catch(Exception ex){
			Logs.geterrorLogger().error(ex.getMessage() , ex);
		}finally{
			releaseConnection();
		}
	}
	

	
	public static void closePstmt(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception ex) {
				Logs.geterrorLogger().error("close stmt error", ex);
			}
		}
	}

	public static void closeRes(ResultSet res) {
		if (res != null) {
			try {
				res.close();
			} catch (Exception ex) {
				Logs.geterrorLogger().error("close res error", ex);
			}
		}
	}

	public static void releaseConnection() {
		releaseConnection(connPooMain);
		releaseConnection(connPoolBBS);
		serverPool.remove();
	}

	private static void releaseConnection(ThreadLocal<Connection> connPool) {
		Connection conn = (Connection) connPool.get();
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.setAutoCommit(true);
					conn.close();
				}
				conn = null;
			} catch (Exception ex) {
				Logs.geterrorLogger().warn("", ex);
			}
		}
		connPool.set(null);
	}

	

}
