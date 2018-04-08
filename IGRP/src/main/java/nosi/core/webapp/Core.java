package nosi.core.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.bind.JAXB;
import org.hibernate.criterion.Restrictions;
import org.modelmapper.ModelMapper;

import com.google.gson.Gson;
import nosi.core.config.Config;
import nosi.core.config.Connection;
import nosi.core.gui.components.IGRPForm;
import nosi.core.gui.fields.Field;
import nosi.core.gui.fields.HiddenField;
import nosi.core.webapp.activit.rest.CustomVariableIGRP;
import nosi.core.webapp.activit.rest.HistoricTaskService;
import nosi.core.webapp.activit.rest.ProcessInstancesService;
import nosi.core.webapp.activit.rest.Rows;
import nosi.core.webapp.activit.rest.TaskService;
import nosi.core.webapp.activit.rest.TaskVariables;
import nosi.core.webapp.databse.helpers.QueryDelete;
import nosi.core.webapp.databse.helpers.QueryHelper;
import nosi.core.webapp.databse.helpers.QueryInsert;
import nosi.core.webapp.databse.helpers.QuerySelect;
import nosi.core.webapp.databse.helpers.QueryUpdate;
import nosi.core.webapp.helpers.DateHelper;
import nosi.core.webapp.helpers.EncrypDecrypt;
import nosi.core.webapp.helpers.Permission;
import nosi.core.webapp.webservices.biztalk.GenericService_DevProxy;
import nosi.core.webapp.webservices.biztalk.dao.PesquisaBI;
import nosi.core.webapp.webservices.biztalk.dao.PesquisaGeografia;
import nosi.core.webapp.webservices.biztalk.dao.PesquisaHierarquicaCAE;
import nosi.core.webapp.webservices.biztalk.dao.PesquisaNIF;
import nosi.core.webapp.webservices.biztalk.dao.PesquisaNascimento;
import nosi.core.webapp.webservices.biztalk.dao.PesquisaSNIAC;
import nosi.core.webapp.webservices.biztalk.dao.Request;
import nosi.core.webapp.webservices.biztalk.dao.ServiceSerach;
import nosi.core.webapp.webservices.biztalk.message.GenericServiceRequest;
import nosi.core.webapp.webservices.biztalk.message.GenericServiceResponse;
import nosi.core.xml.XMLWritter;
import nosi.webapps.igrp.dao.Application;
import nosi.webapps.igrp.dao.CLob;
import nosi.webapps.igrp.dao.Organization;
import nosi.webapps.igrp.dao.ProfileType;
import nosi.webapps.igrp.dao.Transaction;
import static nosi.core.i18n.Translator.gt;
/**
 * @author: Emanuel Pereira
 * 13 Nov 2017
 */
public final class Core {	/** Not inherit 


	
	private Core() {} /** Not instantiate  
	
	  /**
	   * This method is used to add a message log 	 
	   * @param msg This is the message
	   * 
	   */
	public static void log(String msg) {
		
		Igrp.getInstance().getLog().addMessage(msg);
	}
	/**
	   * Shows a success flash message "Operação efetuada com sucesso!"
	   */
	public static void setMessageSuccess(){
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.SUCCESS, gt(FlashMessage.MESSAGE_SUCCESS));
	}
	/**
	   * Shows a custom success flash message 
	   * @param msg Custom message string
	   */	
	public static void setMessageSuccess(String msg){
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.SUCCESS, gt(msg));	
	}
	/**
	   * Shows a error flash message "Falha ao tentar efetuar esta operação!"
	   */	
	public static void setMessageError(){
		nosi.core.servlet.IgrpServlet.LOGGER.error(FlashMessage.MESSAGE_ERROR);
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.ERROR,gt( FlashMessage.MESSAGE_ERROR));
	}	
	/**
	   * Shows a custom error flash message
	   * @param msg Custom message string
	   */	
	public static void setMessageError(String msg){
		nosi.core.servlet.IgrpServlet.LOGGER.error(gt(msg));
		
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.ERROR, gt(msg));
	}	



	

	
	/**Add Message Info
	 * 
	 * @param msg
	 */
	public static void setMessageInfo(String msg){
		nosi.core.servlet.IgrpServlet.LOGGER.info(gt(msg));
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.INFO, gt(msg));
	}

	/**Add Message Info With Link
	 * 
	 * @param msg
	 * @param link
	 */
	public static void setMessageInfoLink(String msg,String link){
		nosi.core.servlet.IgrpServlet.LOGGER.info(gt(msg)+"/#RESERVE#/"+link);
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.INFO_LINK, gt(msg)+"/#RESERVE#/"+link);		
	}
	
	/**Add Message Info With Link
	 * 
	 * @param msg
	 * @param app
	 * @param page
	 * @param action
	 */
	public static void setMessageInfoLink(String msg,String app,String page,String action){
		nosi.core.servlet.IgrpServlet.LOGGER.info(gt(msg)+"/#RESERVE#/"+new Config().getResolveUrl(app, page, action));
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.INFO_LINK, gt(msg)+"/#RESERVE#/"+new Config().getResolveUrl(app, page, action));
	}
		
	/**Add Message Warning
	 * 
	 * @param msg
	 */
	public static void setMessageWarning(String msg){
		nosi.core.servlet.IgrpServlet.LOGGER.warn(gt(msg));
		Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.WARNING, gt(msg));		
	}	
	
	/**Get Config Property
	 * 
	 * @param name
	 * @return
	 */
	public static String getConfig(String name){
		nosi.webapps.igrp.dao.Config c = new nosi.webapps.igrp.dao.Config().find().andWhere("name", "=", name).one();
		return c!=null?c.getValue():null;
	}
	
	/**Get Current User
	 * 
	 * @return
	 */
	public static nosi.webapps.igrp.dao.User getCurrentUser(){
		return (nosi.webapps.igrp.dao.User) Igrp.getInstance().getUser().getIdentity();
	}

	/**Find User by ID
	 * 
	 * @param id
	 * @return
	 */
	public static nosi.webapps.igrp.dao.User findUserById(Integer id){
		return new nosi.webapps.igrp.dao.User().findOne(id);
	}
	
	/**Find User by Username
	 * 
	 * @param userName
	 * @return
	 */
	public static nosi.webapps.igrp.dao.User findUserByUsername(String userName){
		return new nosi.webapps.igrp.dao.User().find().andWhere("username", "=", userName).one();
	}
	
	/**Find User by email
	 * 
	 * @param email
	 * @return
	 */
	public static nosi.webapps.igrp.dao.User findUserByEmail(String email){
		return new nosi.webapps.igrp.dao.User().find().andWhere("username", "=", email).one();
	}
	
	/**Format date and return to Type String
	 * 
	 * @param date
	 * @param formatIn
	 * @param outputFormat
	 * @return
	 */
	public static String convertDate(String date, String formatIn, String outputFormat) {
		return DateHelper.convertDate(date,formatIn,outputFormat);
	}
	
	/**Format date and return to Type Date
	 * 
	 * @param data
	 * @param inputFormat
	 * @param outputFormat
	 * @return
	 */
	public static java.sql.Date formatDate(String data,String inputFormat,String outputFormat){
		return DateHelper.formatDate(data,inputFormat,outputFormat);
	}
	
	/**Get Current Datetime (dd-MM-yyyy)
	 * 
	 * @return
	 */
	public static String getCurrentDate(){
		return DateHelper.getCurrentDate("dd-MM-yyyy");
	}

	/**Get Current Datetime (User defined output format)
	 * 
	 * @param outputFormat
	 * @return
	 */
	public static String getCurrentDate(String outputFormat){
		return DateHelper.getCurrentDate(outputFormat);
	}
	
	/**Get Current Datetime (dd/MM/yyyy HH:mm:ss)
	 * 
	 * @return
	 */
	public static String getCurrentDataTime() {
		return DateHelper.getCurrentDataTime();
	}
	
	/**Get Current Application Dad on Cookie
	 * 
	 * @return
	 */
	public static String getCurrentDad(){
		return new Permission().getCurrentEnv();
	}

	/**Get Current Organization on Cookie
	 * 
	 * @return
	 */
	public static Integer getCurrentOrganization(){
		return new Permission().getCurrentOrganization();
	}

	/**Get Current Profile on Cookie
	 * 
	 * @return
	 */
	public static Integer getCurrentProfile(){
		return new Permission().getCurrentPerfilId();
	}
	
	/**Find Application By ID
	 * 
	 * @param id
	 * @return
	 */
	public static Application findApplicationById(Integer id){
		return new Application().findOne(id);
	}
	
	/**Find Application By Dad
	 * 
	 * @param dad
	 * @return
	 */
	public static Application findApplicationByDad(String dad){
		return new Application().find().andWhere("dad", "=", dad).one();
	}
	
	/**Find Organization By ID
	 * 
	 * @param id
	 * @return
	 */
	public static Organization findOrganizationById(Integer id){
		return new Organization().findOne(id);
	}
	
	/**Find Organization By Code
	 * 
	 * @param code
	 * @return
	 */
	public static Organization findOrganizationByCode(String code){
		return new Organization().find().andWhere("code", "=", code).one();
	}
	
	/**Find Profile By ID
	 * 
	 * @param id
	 * @return
	 */
	public static ProfileType findProfileById(Integer id){
		return new ProfileType().findOne(id);
	}
	
	/**Find Profile By Code
	 * 
	 * @param code
	 * @return
	 */
	public static ProfileType findProfileByCode(String code){
		return new ProfileType().find().andWhere("code", "=", code).one();
	}
	
	/**Check permition transaction for current user
	 * 
	 * @param transaction
	 * @return
	 */
	public static boolean checkUserTransaction(String transaction){
		return new Transaction().getPermission(transaction);
	}
	
	public static Response getLinkReport(String code_report,Report rep){
		return new Report().invokeReport(code_report, rep);
	}
	
	public static GenericServiceResponse getBizTalkClient(String clientId,String transaction,String service,String args){
		GenericService_DevProxy proxy = new GenericService_DevProxy(); 
		GenericServiceRequest part = new GenericServiceRequest(clientId, transaction, service, args);
		try {
			return proxy.getGenericService_Dev().genericRequest(part);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GenericServiceResponse getBizTalkClientService(ServiceSerach service){
		String args = new Request().prepare(service,"xml");
		System.out.println("args: "+args);
		return getBizTalkClient(service.getClientID(), service.getTransactionID(), service.getServiceID(), args);			
	}
	
	private static ServiceSerach processRequestBiztalkClientService(GenericServiceResponse response,ServiceSerach service){
		if(response.getStatus().equals("true")){
			String xml = response.getResult();
			StringReader input = new StringReader(xml);
			nosi.core.webapp.webservices.biztalk.dao.Response r = JAXB.unmarshal(input,nosi.core.webapp.webservices.biztalk.dao.Response.class);
			if(r.getRow()!=null){
				if(r.getRow().isStatus()){
					xml = xml.replaceAll("lista_nifs", "lista");
					xml = xml.substring(xml.indexOf("<lista>"), xml.indexOf("</lista>")+"</lista>".length());
					input = new StringReader(xml);
					service = JAXB.unmarshal(input, service.getClass());
					return service;
				}
			}
		}
		return null;
	}
	
	/**Pesquia SNIAC via Biztalk
	 * 
	 * @param pesquisa
	 * @return
	 */
	public static PesquisaSNIAC getBizTalkPesquisaSNIAC(PesquisaSNIAC pesquisa){
		return (PesquisaSNIAC) processRequestBiztalkClientService(getBizTalkClientService(pesquisa),pesquisa);
	}
	
	public static PesquisaSNIAC getBizTalkPesquisaSNIAC(Integer num_idnt_civil_pes, String num_registo_pes, String nome_pes, String data_nasc_pes,
			Integer id_tp_doc_pes){
		return getBizTalkPesquisaSNIAC(new PesquisaSNIAC(num_idnt_civil_pes, num_registo_pes, nome_pes, data_nasc_pes, id_tp_doc_pes));
	}	

	/**Pesquia BI via Biztalk
	 * 
	 * @param pesquisa
	 * @return
	 */
	public static PesquisaBI getBizTalkPesquisaBI(PesquisaBI pesquisa){
		return (PesquisaBI) processRequestBiztalkClientService(getBizTalkClientService(pesquisa),pesquisa);
	}
	
	public static PesquisaBI getBizTalkPesquisaBI(Integer bi, String nome){
		return getBizTalkPesquisaBI(new PesquisaBI(bi, nome));
	}	

	/**Pesquia NIF via Biztalk
	 * 
	 * @param pesquisa
	 * @return
	 */
	public static PesquisaNIF getBizTalkPesquisaNIF(PesquisaNIF pesquisa){
		return (PesquisaNIF) processRequestBiztalkClientService(getBizTalkClientService(pesquisa),pesquisa);
	}
	
	public static PesquisaNIF getBizTalkPesquisaNIF(Integer numero, String nome){
		return getBizTalkPesquisaNIF(new PesquisaNIF(numero, nome));
	}
	
	/**Pesquia Nascimento via Biztalk
	 * 
	 * @param pesquisa
	 * @return
	 */
	public static PesquisaNascimento getBizTalkPesquisaNascimento(PesquisaNascimento pesquisa){
		return (PesquisaNascimento) processRequestBiztalkClientService(getBizTalkClientService(pesquisa),pesquisa);
	}
	
	public static PesquisaNascimento getBizTalkPesquisaNascimento(String nome, Integer numero_registo, String data_nascimento){
		return getBizTalkPesquisaNascimento(new PesquisaNascimento(nome, numero_registo, data_nascimento));
	}	

	/**Pesquia Hierarquia CAE via Biztalk
	 * 
	 * @param pesquisa
	 * @return
	 */
	public static PesquisaHierarquicaCAE getBizTalkPesquisaHierarquiaCAE(PesquisaHierarquicaCAE pesquisa){
		return (PesquisaHierarquicaCAE) processRequestBiztalkClientService(getBizTalkClientService(pesquisa),pesquisa);
	}
	
	public static PesquisaHierarquicaCAE getBizTalkPesquisaHierarquiaCAE(String id, String codigo, String crpcae_id, String self_id){
		return getBizTalkPesquisaHierarquiaCAE(new PesquisaHierarquicaCAE(id, codigo, crpcae_id, self_id));
	}
	
	/**Pesquia Geografia via Biztalk
	 * 
	 * @param pesquisa
	 * @return
	 */
	public static PesquisaGeografia getBizTalkPesquisaGeografia(PesquisaGeografia pesquisa){
		return (PesquisaGeografia) processRequestBiztalkClientService(getBizTalkClientService(pesquisa),pesquisa);
	}
	
	public static PesquisaGeografia getBizTalkPesquisaGeografia(String id, String zona, String freguesia, String concelho, String ilha, String pais,
			String nivel_detalhe, String tp_geog_cd, String codigo_ine, String codigo, String self_id){
		return getBizTalkPesquisaGeografia(new PesquisaGeografia(id, zona, freguesia, concelho, ilha, pais, nivel_detalhe, tp_geog_cd, codigo_ine, codigo, self_id));
	}
	
	public static String encrypt(String content,String secretKey){
		return EncrypDecrypt.encrypt(content,secretKey);
	}
	
	public static String decrypt(String content,String secretKey){
		return EncrypDecrypt.decrypt(content,secretKey);
	}
	public static String encrypt(String content){
		return EncrypDecrypt.encrypt(content);
	}
	
	public static String decrypt(String content){
		return EncrypDecrypt.decrypt(content);
	}
	
	public static boolean isNotNull(Object value) {
		return value!=null && !value.equals("");
	}
	
	public static boolean isNull(Object value) {
		return value==null || value.equals("");
	}	
	
	public static boolean isNullOrZero(Object value) {
		if(value instanceof Number)
			return value ==null || new Integer(value.toString()) ==0;
		return value==null || value.equals("");
	}	
	
	public static boolean isNotNullOrZero(Object value) {
		if(!(value instanceof Number)) {
			return Core.isNotNull(value);
		}
		if(Core.isNotNull(value)) {
			return new Integer(value.toString())!=0;
		}
		return false;
	}	
	
	public static boolean isInt(Object value) {
		if(isNotNull(value)) {
			try {
				double v = Integer.parseInt(value.toString());
				return ((v == Math.floor(v)) && !Double.isInfinite(v));
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isDouble(Object value) {
		if(isNotNull(value)) {
			try {
				double v = Double.parseDouble(value.toString());
				return !((v == Math.floor(v)) && !Double.isInfinite(v));
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isFloat(Object value) {
		if(isNotNull(value)) {
			try {
				float v = Float.parseFloat(value.toString());
				return !((v == Math.floor(v)) && !Float.isInfinite(v));
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	/**Verifies if the String is a Integer, than returns the parse of it, else returns 0
	 *  
	 * @param value
	 * @return Integer.parseInt(value) or 0
	 * 
	 */
	public static Integer toInt(String value) {
		if(Core.isInt(value))
			return Integer.parseInt(value);
		return 0;
	}

	public static Long toLong(String value) {
		if(Core.isInt(value))
			return Long.parseLong(value);
		return (long) 0;
	}
	
	public static Short toShort(String value) {
		if(Core.isInt(value))
			return Short.parseShort(value);
		return 0;
	}
	/**Verifies if the String is a Double, than returns the parse of it, else returns 0
	 *  
	 * @param value
	 * @return Double.parseDouble(value) or 0
	 * 
	 */
	public static Double toDouble(String value) {
		if(Core.isDouble(value))
			return Double.parseDouble(value);
		return 0.0;
	}
	/**Verifies if the String is a Float, than returns the parse of it, else returns 0
	 * {@value}
	 * @ 
	 * @param value
	 * @return Float.parseFloat(value) or 0
	 * 
	 */
	public static Float toFloat(String value) {
		if(Core.isFloat(value))
			return Float.parseFloat(value);
		return (float) 0;
	}
	
	public static QueryHelper insert(String connectionName,String tableName) {
		return new QueryInsert(connectionName).insert(tableName);
	}
	
	public static QueryHelper insert(String connectionName,String schemaName,String tableName) {
		return new QueryInsert(connectionName).insert(schemaName,tableName);
	}
	
	public static QueryHelper update(String connectionName,String tableName) {
		return new QueryUpdate(connectionName).update(tableName);
	}
	
	public static QueryHelper update(String connectionName,String schemaName,String tableName) {
		return new QueryUpdate(connectionName).update(schemaName,tableName);
	}
	
	public static QueryHelper delete(String connectionName,String tableName) {
		return new QueryDelete(connectionName).delete(tableName);
	}
	
	public static QueryHelper delete(String connectionName,String schemaName,String tableName) {
		return new QueryDelete(connectionName).delete(schemaName,tableName);
	}
	
	public static QueryHelper query(String connectionName,String sql) {
		return new QuerySelect(connectionName).select(sql);
	}
	public static QueryHelper query(String connectionName,String sql,Class<?> className) {
		return new QuerySelect(connectionName).select(sql,className);
	}
	public static QueryHelper query(String sql) {
		return new QuerySelect().select(sql);
	}
	
	public static java.sql.Date ToDate(String date,String formatIn){
		return DateHelper.convertStringToDate(date, formatIn);
	}
	
	public static java.sql.Date ToDate(String date,String formatIn,String formatOut){
		return DateHelper.formatDate(date, formatIn,formatOut);
	}
	
	public static String ToChar(java.sql.Date date,String formatIn) {
		return DateHelper.convertDateToString(date, formatIn);
	}

	public static String ToChar(String date, String formatIn, String formatOut) {
		return DateHelper.convertDate(date, formatIn, formatOut);
	}
	
	public static String ToChar(String date,String formatOut) {
		return DateHelper.convertDate(date, "yyyy-MM-dd", formatOut);
	}
	
	public static class Restriction extends Restrictions{
		
	}

	public static void addHiddenField(String name,Object value) {
		Field f = new HiddenField(name, value!=null?value.toString():"");
		f.setValue(value);
		IGRPForm.hiddenFields.add(f);
	}

	public static String getXMLParams() {
		Map<String,String[]> params = Igrp.getInstance().getRequest().getParameterMap();
		XMLWritter xml = new XMLWritter();
		xml.startElement("rows");
		params.entrySet().stream()
						 .filter(p->!p.getKey().equalsIgnoreCase("r"))
						 .filter(p->!p.getKey().equalsIgnoreCase("prm_app"))
						 .filter(p->!p.getKey().equalsIgnoreCase("prm_page"))
						 .forEach(
								p->{									
									for(String v:p.getValue()) {
										xml.setElement(p.getKey(), v);
									}
								}
						  );
		xml.endElement();
		return xml.toString();
	}
	
	public static String getJsonParams() {
		Map<String,String[]> params = Igrp.getInstance().getRequest().getParameterMap();
		CustomVariableIGRP customV = new CustomVariableIGRP();
		Gson gson = new Gson();		
		params.entrySet().stream()
						 .filter(p->!p.getKey().equalsIgnoreCase("r"))
						 .filter(p->!p.getKey().equalsIgnoreCase("prm_app"))
						 .filter(p->!p.getKey().equalsIgnoreCase("prm_page"))
						 .forEach(
								p->{
									Rows row = new Rows();
									row.setName(p.getKey());
									row.setValue(p.getValue());
									customV.add(row);
								}
						  );
		String json = gson.toJson(customV);
		return json;
	}
	public static Map<String,String[]> getParameters() {
		return Igrp.getInstance().getRequest().getParameterMap();
	}
	
	public static void setParam(String name,Object value) {
		if(Igrp.getInstance().getRequest()!=null)
			Igrp.getInstance().getRequest().setAttribute(name, value);
	}
	
	public static void setParam(String name,Object[] value) {
		if(Igrp.getInstance().getRequest()!=null)
			Igrp.getInstance().getRequest().setAttribute(name, value);
	}
	
	public static String getParam(String name) {
		Object v = Igrp.getInstance().getRequest().getAttribute(name);
		return v!=null?v.toString():"";
	}

	public static Integer getParamInt(String name) {
		String x = Core.getParam(name);
		return Core.isNotNull(x)?Core.toInt(x):0;
	}
	
	public static Double getParamDouble(String name) {
		String x = Core.getParam(name);
		return Core.isNotNull(x)?Core.toDouble(x):0;
	}

	public static Short getParamShort(String name) {
		String x = Core.getParam(name);
		return Core.isNotNull(x)?Core.toShort(x):0;
	}
	public static Float getParamFloat(String name) {
		String x = Core.getParam(name);
		return Core.isNotNull(x)?Core.toFloat(x):0;
	}
	public static Long getParamLong(String name) {
		String x = Core.getParam(name);
		return Core.isNotNull(x)?Core.toLong(x):0;
	}
	public static String[] getParamArray(String name) {
		if(Igrp.getInstance().getRequest().getAttribute(name) instanceof String[])
			return (String[]) Igrp.getInstance().getRequest().getAttribute(name);
		return new String[] {Igrp.getInstance().getRequest().getAttribute(name).toString()};
	}
	
	public static String getTaskVariable(String taskDefinitionKey,String variableName) {		
		String id = getTaskId();
		if(Core.isNotNull(id)) {
			List<HistoricTaskService> task1 = new HistoricTaskService().getHistory(taskDefinitionKey,id);
			if(task1!=null && task1.size() > 0) {
				List<TaskVariables> vars = task1.get(0).getVariables();
				List<TaskVariables> var = vars.stream().filter(v->v.getName().equalsIgnoreCase(taskDefinitionKey+"_"+variableName)).collect(Collectors.toList());
				return (var!=null && var.size() > 0)?(String) var.get(0).getValue():"";
			}
		}
		return "";
	}
	
	public static Integer getTaskVariableInt(String taskDefinitionKey,String variableName) {
		String v = Core.getTaskVariable(taskDefinitionKey, variableName);
		return Core.isNotNull(v)?Core.toInt(v):0;
	}
	
	public static Short getTaskVariableShort(String taskDefinitionKey,String variableName) {
		String v = Core.getTaskVariable(taskDefinitionKey, variableName);
		return Core.isNotNull(v)?Core.toShort(v):0;
	}

	public static Long getTaskVariableLong(String taskDefinitionKey,String variableName) {
		String v = Core.getTaskVariable(taskDefinitionKey, variableName);
		return Core.isNotNull(v)?Core.toLong(v):0;
	}
	public static Double getTaskVariableDouble(String taskDefinitionKey,String variableName) {
		String v = Core.getTaskVariable(taskDefinitionKey, variableName);
		return Core.isNotNull(v)?Core.toDouble(v):0;
	}

	
	public static Float getTaskVariableFloat(String taskDefinitionKey,String variableName) {
		String v = Core.getTaskVariable(taskDefinitionKey, variableName);
		return Core.isNotNull(v)?Core.toFloat(v):0;
	}

	
	public static Boolean getTaskVariableBoolean(String taskDefinitionKey,String variableName) {
		String v = Core.getTaskVariable(taskDefinitionKey, variableName);
		return Core.isNotNull(v)?true:false;
	}
	
	private static String getTaskId() {
		String taskId = Core.getParam("taskId");
		String taskExecutionId = Core.getParam("taskExecutionId");
		if(Core.isNotNull(taskExecutionId)) {
			return taskExecutionId;
		}
		List<HistoricTaskService> task = new HistoricTaskService().getHistory(taskId);
		return (task!=null && task.size() > 0)?task.get(0).getExecutionId():null;
	}
	
	public static void addTaskVariableLong(String taskDefinitionKey,String variableName,Object value) {
		String taskId = Igrp.getInstance().getRequest().getParameter("taskId");
		if(Core.isNotNull(taskId)) {
			TaskService task = new TaskService().getTask(taskId);
			task.setId(taskId);
			task.addVariable(task.getTaskDefinitionKey()+"_"+variableName, "local", "string", value.toString());
			task.submitVariables();
			ProcessInstancesService p = new ProcessInstancesService();
			p.setId(task.getProcessInstanceId());	
			p.addVariable(task.getTaskDefinitionKey()+"_"+variableName, "local", "string", value.toString());
			p.submitVariables();
		}
	}
	
	public static void mapper(Object source,Object destination) {
		ModelMapper mapper = new ModelMapper();
		mapper.map(source, destination);
	}
	
	public static String getPinkColor() {
		return "1";
	}

	public static String getAmberColor() {
		return "2";
	}
	
	public static String getYellowColor() {
		return "3";
	}

	public static String getGreenColor() {
		return "4";
	}
	
	public static String getBlueGreyColor() {
		return "5";
	}

	public static String getPurpleColor() {
		return "6";
	}
	
	public static String getBlueColor() {
		return "7";
	}

	public static String getBrownColor() {
		return "8";
	}
	
	public static String getDeepPurpleColor() {
		return "9";
	}

	/**Receive multiple params and get one of these params that's not null 
	 * 
	 * @param strings
	 * @return
	 */
	public static String getSwitchNotNullValue(String ...strings) {
		if(strings.length > 1) {
			if(Core.isNotNull(strings[0]))
				return strings[0];
			String[] newStrings = new String[strings.length-1];
			System.arraycopy(strings, 1, newStrings,0, newStrings.length);
			return getSwitchNotNullValue(newStrings);
		}else if(strings.length==1) {
			if(Core.isNotNull(strings[0]))
				return strings[0];
		}
		return "";
	}
	
	/** Insert a file to the Igrp core DataBase and return an Id ... **/
	
	public static int saveFile(File file, String name, String mime_type) {
		String igrpCoreConnection = Config.getBaseConnection();
		java.sql.Connection conn = Connection.getConnection(igrpCoreConnection);
		int lastInsertedId = 0;
		if(conn != null) {
			name = (name == null || name.trim().isEmpty() ? file.getName() : name);
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			mime_type = (mime_type == null || mime_type.trim().isEmpty() ? fileNameMap.getContentTypeFor(file.getPath()) : mime_type);
			String sysdate = LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
			String standardSql = "insert into tbl_clob(c_lob_content, dt_created, mime_type, name) values(?, ?, ?, ?)";
			try {
				java.sql.PreparedStatement ps = conn.prepareStatement(standardSql, java.sql.PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setBinaryStream(1, new FileInputStream(file));
				ps.setString(2, sysdate);
				ps.setString(3, mime_type);
				ps.setString(4, name);
				if(ps.executeUpdate() > 0) {			
					try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
				        if (rs.next()) {
				        	lastInsertedId = rs.getInt(1);
				        }
					}
					ps.close();
				}
				conn.commit();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return lastInsertedId;
	}
	
	public static int saveFile(File file) {
		return saveFile(file, null, null);
	}
	
	public static CLob getFile(int fileId) {
		CLob cLob = null;
		java.sql.Connection conn = null;
		
		try {
			String igrpCoreConnection = Config.getBaseConnection();
			conn = Connection.getConnection(igrpCoreConnection);
			String sql = "select * from tbl_clob where id = ?";
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, fileId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				cLob = new CLob();
				cLob.setC_lob_content(rs.getBytes("c_lob_content"));
				cLob.setDt_created(rs.getString("dt_created"));
				cLob.setName(rs.getString("name"));
				cLob.setMime_type(rs.getString("mime_type"));
				cLob.setId(rs.getInt("id"));
			}
			rs.close();
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
			cLob = null;
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return cLob;
	}

	public static Map<Object, Object> mapArray(Object[] array1,Object[] array2,Predicate<? super Integer> filter) {
		if(array1 !=null && array1.length >0 && array2 !=null && array2.length > 0)
			return (Map<Object, Object>) IntStream.range(0, array1.length).boxed().filter(filter).collect(Collectors.toMap(i ->array1[i], i -> array2[i]));
		return null;
	}
	public static Map<Object, Object> mapArray(Object[] array1,Object[] array2) {
		if(array1 !=null && array1.length >0 && array2 !=null && array2.length > 0 )
			return (Map<Object, Object>) IntStream.range(0, array1.length).boxed().collect(Collectors.toMap(i ->array1[i], i -> array2[i]));
		return null;
	}
	/** **/
}
