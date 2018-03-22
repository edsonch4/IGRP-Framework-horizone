package nosi.core.webapp.databse.helpers;


import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import nosi.core.config.Config;
import nosi.core.webapp.Core;
import nosi.core.webapp.databse.helpers.DatabaseMetadaHelper.Column;
import nosi.core.webapp.helpers.DateHelper;


/**
 * Emanuel
 * 21 Dec 2017
 */
public abstract class QueryHelper implements IFQuery{

	protected String sql = "";
	protected String schemaName;
	protected String tableName;
	protected List<DatabaseMetadaHelper.Column> columnsValue;
	protected String connectionName;
	protected String condition;
	
	public QueryHelper(String connectionName) {
		this.columnsValue = new ArrayList<>();
		this.connectionName = Core.isNotNull(connectionName)?connectionName:Config.getBaseConnection();
	}	
	
	public QueryHelper where(String condition) {
		this.condition = condition;
		return this;
	}
	
	public QueryHelper addLong(String columnName,Long value) {
		this.addColumn(columnName, value, Long.class);
		return this;
	}

	public QueryHelper addDouble(String columnName,Double value) {
		this.addColumn(columnName, value, Double.class);
		return this;
	}

	public QueryHelper addFloat(String columnName,Float value) {
		this.addColumn(columnName, value, Float.class);
		return this;
	}

	public QueryHelper addShort(String columnName,Short value) {
		this.addColumn(columnName, value, Short.class);
		return this;
	}

	public QueryHelper addDate(String columnName,String value,String format) {
		this.addColumn(columnName, value, java.sql.Date.class,format);
		return this;
	}
	
	public QueryHelper addDate(String columnName,String value) {
		return addDate(columnName, value, "yyyy-mm-dd");
	}
	
	public QueryHelper addDate(String columnName,java.sql.Date value) {
		return addDate(columnName, value, "yyyy-mm-dd");
	}
	
	public QueryHelper addDate(String columnName,java.sql.Date value,String format) {
		this.addColumn(columnName, value, java.sql.Date.class,format);
		return this;
	}
	
	public QueryHelper addString(String columnName,String value) {
		this.addColumn(columnName, value, String.class);
		return this;
	}
	
	public QueryHelper addInt(String columnName,Integer value) {
		this.addColumn(columnName, value, Integer.class);
		return this;
	}

	public QueryHelper addBinaryStream(String columnName,FileInputStream value) {
		this.addColumn(columnName, value, FileInputStream.class);
		return this;
	}
	public QueryHelper addBinaryStream(String columnName,InputStream value) {
		this.addColumn(columnName, value, InputStream.class);
		return this;
	}

    public QueryHelper addObject(String columnName,Object value) {
        this.addColumn(columnName, value, Object.class);
        return this;
    }
    
    public QueryHelper addTimestamp(String columnName,Timestamp value) {
        this.addColumn(columnName, value, Timestamp.class);
        return this;
    }

    public QueryHelper addArray(String columnName,Array value) {
        this.addColumn(columnName, value, Array.class);
        return this;
    }

    public QueryHelper addAsciiStream(String columnName,InputStream value) {
        this.addColumn(columnName, value, InputStream.class);
        return this;
    }

    public QueryHelper addClob(String columnName,Clob value) {
        this.addColumn(columnName, value, Clob.class);
        return this;
    } 

    public QueryHelper addBlob(String columnName,Blob value) {
        this.addColumn(columnName, value, Blob.class);
        return this;
    } 

    public QueryHelper addByte(String columnName,byte[] value) {
        this.addColumn(columnName, value, Byte[].class);
        return this;
    }  
    
    public QueryHelper addByte(String columnName,byte value) {
        this.addColumn(columnName, value, Byte.class);
        return this;
    }  
    
    public QueryHelper addBoolean(String columnName,boolean value) {
        this.addColumn(columnName, value, Boolean.class);
        return this;
    }  
    
    public QueryHelper addBigDecimal(String columnName,BigDecimal value) {
        this.addColumn(columnName, value, BigDecimal.class);
        return this;
    }
    
    public QueryHelper addTime(String columnName,Time value) {
        this.addColumn(columnName, value, Time.class);
        return this;
    }
    
	protected void addColumn(String name,Object value,Object type) {
		this.addColumn(name, value, type, null);
	}
	
	protected void addColumn(String name,Object value,Object type,String format) {
		Column c = new Column();
		c.setName(name);
		c.setDefaultValue(value);
		c.setType(type);
		c.setFormat(format);
		this.columnsValue.add(c);
	}
	
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public List<DatabaseMetadaHelper.Column> getColumnsValue() {
		return columnsValue;
	}

	public void setColumnsValue(List<DatabaseMetadaHelper.Column> columnsValue) {
		this.columnsValue = columnsValue;
	}

	public String getSqlInsert(String schemaName, List<DatabaseMetadaHelper.Column> colmns, String tableName) {
		tableName = (schemaName!=null && !schemaName.equals(""))?schemaName+"."+tableName:tableName;//Adiciona schema
		String inserts = "";
		String values = "";
		for(DatabaseMetadaHelper.Column col:colmns) {
			if(!col.isAutoIncrement()) {
				inserts += col.getName().toLowerCase()+",";
				values += ":"+col.getName().toLowerCase()+",";
			}
		}	
		inserts = inserts.substring(0, inserts.length()-1);
		values = values.substring(0, values.length()-1);
		this.sql = "INSERT INTO "+tableName+" ("+inserts+") VALUES ("+values+")";
		return this.sql;
	}
	

	public String getSqlUpdate(String schemaName, List<DatabaseMetadaHelper.Column> colmns, String tableName,String condition) {
		tableName = (schemaName!=null && !schemaName.equals(""))?schemaName+"."+tableName:tableName;//Adiciona schema
		String updates = "";
		for(DatabaseMetadaHelper.Column col:colmns) {
			if(!col.isAutoIncrement()) {
				updates += col.getName().toLowerCase()+"=:"+col.getName().toLowerCase()+",";
			}
		}	
		updates = updates.substring(0, updates.length()-1);
		this.sql = "UPDATE "+tableName +" SET "+updates+" WHERE "+condition;
		return this.sql;
	}
	

	public String getSqlDelete(String schemaName, String tableName, String condition) {
		tableName = (schemaName!=null && !schemaName.equals(""))?schemaName+"."+tableName:tableName;//Adiciona schema
		this.sql = "DELETE FROM "+tableName +" WHERE "+condition;
		return this.sql;
	}
	
	public void setParameter(Query query, Object value, Column col) {
		if(col.getType().equals(java.lang.Integer.class)) {
			query.setParameter(col.getName(),Integer.parseInt(value.toString()));
		}else if(col.getType().equals(java.lang.Double.class)){
			query.setParameter(col.getName(), Double.parseDouble(value.toString()));
		}else if(col.getType().equals(java.lang.Float.class)){
			query.setParameter(col.getName(), Float.parseFloat(value.toString()));
		}else if(col.getType().equals(java.lang.Character.class)){
			query.setParameter(col.getName(), (Character)value);
		}else if(col.getType().equals(java.lang.Long.class)){
			query.setParameter(col.getName(), (Long)value);
		}else if(col.getType().equals(java.lang.Short.class)){
			query.setParameter(col.getName(), (Short)value);
		}else if(col.getType().equals(java.sql.Date.class)){
			if((value instanceof String) && Core.isNotNull(value))
				query.setParameter(col.getName(),Core.ToDate(value.toString(), col.getFormat()));
			else
				query.setParameter(col.getName(),value);
		}else {
			query.setParameter(col.getName(),value);
		}
	}

	public void setParameter(NamedParameterStatement query, Object value, Column col) throws SQLException {
		
		if(col.getType().equals(java.lang.Integer.class)) {
			query.setInt(col.getName(),Integer.parseInt(value.toString()));
		}
		if(col.getType().equals(java.lang.Integer.class)) {
			query.setInt(col.getName(),Integer.parseInt(value.toString()));
		}else if(col.getType().equals(java.lang.Double.class)){
			query.setDouble(col.getName(), Double.parseDouble(value.toString()));
		}else if(col.getType().equals(java.lang.Float.class)){
			query.setFloat(col.getName(), Float.parseFloat(value.toString()));
		}else if(col.getType().equals(java.lang.Long.class)){
			query.setLong(col.getName(), (Long)value);
		}else if(col.getType().equals(java.lang.Short.class)){
			query.setShort(col.getName(), (Short)value);
		}else if(col.getType().equals(java.sql.Date.class) && Core.isNotNull(value)){
			query.setDate(col.getName(),DateHelper.formatDate(value.toString(), col.getFormat()));
		}else if(col.getType().equals(java.lang.String.class) || col.getType().equals(java.lang.Character.class) && Core.isNotNull(value)){
			query.setString(col.getName(),value.toString());
		}else {
			query.setObject(col.getName(), value);
		}
	}
	
	@Override
	public Object execute() {
		Object r = null;
		if(this instanceof QueryInsert) {
			this.sql = this.getSqlInsert(this.getSchemaName(),this.getColumnsValue(), this.getTableName());
		}
		else if(this instanceof QueryUpdate) {
			this.sql = this.getSqlUpdate(this.getSchemaName(),this.getColumnsValue(), this.getTableName(),this.condition);
		}
		else if(this instanceof QueryDelete) {
			this.sql = this.getSqlDelete(this.getSchemaName(), this.getTableName(),this.condition);
		}
		Connection conn =nosi.core.config.Connection.getConnection(this.getConnectionName());
		if(this instanceof QueryInsert) {
			try {
				NamedParameterStatement q = new NamedParameterStatement(conn , this.sql,PreparedStatement.RETURN_GENERATED_KEYS);
				this.setParameters(q);	
				Core.log("SQL:"+q.getSql());
				r = q.executeInsert(this.tableName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			try {
				NamedParameterStatement q = new NamedParameterStatement(conn, this.sql);
				this.setParameters(q);
				Core.log("SQL:"+q.getSql());
				int rr = q.executeUpdate();
				r = rr > 0?rr:null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return r;
	}
	
	private void setParameters(NamedParameterStatement q) throws SQLException {
		for(DatabaseMetadaHelper.Column col:this.getColumnsValue()) {		 
			 if(col.getDefaultValue()!=null) {
				 this.setParameter(q,col.getDefaultValue(),col);					
			 }else {
				 q.setObject(col.getName(), null);
			 }
		}
	}

	public List<Tuple> getResultList() {
		return null;
	}
	
	public Object getSigleResult() {
		return null;
	}
	
	public TypedQuery<?> getSingleResult(){
		return null;
	}
	
	public <T> List<T> getResultList(Class<T> type){
		return null;
	}
}
