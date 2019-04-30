package nosi.core.gui.fields;
/**
 * @author: Emanuel Pereira
 * 
 * Apr 13, 2017
 *
 * Description: abstract class to configure any field
 */

import java.util.Map;
import static nosi.core.i18n.Translator.gt;
import nosi.core.config.ConfigApp;
import nosi.core.webapp.Model;
import nosi.core.webapp.databse.helpers.BaseQueryInterface;
import nosi.core.webapp.helpers.IgrpHelper;
import nosi.core.webapp.helpers.Route;

public abstract class AbstractField implements Field{

	private String name;
	private String tag_name;
	private Object value="";
	private String label = "";
	private String lookup = "";
	private boolean isVisible=true;
	private boolean isParam = false;
	private String sql;
	private String connectionName = ConfigApp.getInstance().getBaseConnection();
	private Map<?,?> comboBox;
	
	public FieldProperties propertie;
	
	public FieldProperties propertie(){
		return this.propertie;
	}
	public AbstractField(){
		this.propertie = new FieldProperties();
	}
	public String getLabel() {
		return gt(this.label);
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTagName() {
		tag_name = this.propertie.get("tag")!=null?this.propertie.get("tag").toString().toLowerCase():tag_name;
		return tag_name;
	}
	public void setTagName(String tag_name) {
		this.tag_name = tag_name;
	}	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		if(value instanceof Model) {
			this.configValue(value);			
			if(this.propertie != null && this.getValue() != null)
				this.propertie.put("value", this.getValue());
		}else if(value instanceof Map) {
			this.setListOptions((Map<?, ?>) value);
		}
		else {
			this.value = value;
		}		
	}
	public void setValue(int value) {
		this.value = value;
	}	
	public void setValue(float value) {
		this.value = value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getLookup() {
		return lookup;
	}
	public void setLookup(String app,String page,String action) {
		this.lookup = Route.getResolveUrl(app, page, action).replace("?", "").replace("webapps", "");
	}	
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public boolean isParam() {
		return isParam;
	}
	public void setParam(boolean isParam) {
		this.isParam = isParam;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setSqlQuery(String sql){
		this.sql = sql;
	}

	public void setSqlQuery(String connectionName,String sql){
		if(connectionName!=null && !connectionName.equals("")){
			this.connectionName = connectionName;
		}
		this.setSqlQuery(sql);
	}

	public void setSqlQuery(String connectionName, String tableName, String key, String value) {
		String sql ="";
		if(connectionName!=null && !connectionName.equals("") && tableName!=null && key!=null && value!=null){
			this.connectionName = connectionName;
			sql = "SELECT "+key+", "+value+" FROM "+tableName;
		}
		this.setSqlQuery(sql);
	}
	

	public void setSqlQuery(String connectionName,String schemaName, String tableName, String key, String value) {
		if(schemaName!=null && !schemaName.equals("")) {
			tableName = schemaName+"."+tableName;
		}
		this.setSqlQuery(connectionName, tableName, key, value);
	}	
	
	public String getSqlQuery(){
		return this.sql;
	}
	
	protected String getConnectionName(){
		return this.connectionName;
	}
	
	protected void configValue(Object model){
		this.value = IgrpHelper.getValue(model, this.getName());
	}
	
	@Override
	public void addParam(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setValue(Map<?, ?> map) {
		this.comboBox = map;
	}
	
	@Override
	public void setListOptions(Map<?, ?> map) {
		this.comboBox = map;
	}
	@Override
	public Map<?, ?> getListOptions() {
		return this.comboBox;
	}
	
	@Override
	public void setQuery(BaseQueryInterface query,String prompt) {
	}
	
	@Override
	public void setQuery(BaseQueryInterface query) {
	}
	
	@Override
	public void setDefaultValue(Object defaultValue) {
		// Not set yet 
	}
}
