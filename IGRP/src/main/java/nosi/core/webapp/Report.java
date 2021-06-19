package nosi.core.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import nosi.core.config.Config;
import nosi.core.webapp.helpers.GUIDGenerator;
import nosi.core.webapp.helpers.ReflectionHelper;
import nosi.core.webapp.helpers.Route;
import nosi.core.webapp.helpers.TransformHelper;
import nosi.webapps.igrp.dao.CLob;
import nosi.webapps.igrp.dao.RepInstance;
import nosi.webapps.igrp.dao.RepTemplate;
import nosi.webapps.igrp.pages.file.File;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Use the functions getLinkReport and other to manage and invoke reports.
 * 
 * @author: Emanuel Pereira
 * 9 Oct 2017
 */
public class Report extends Controller{

	private Map<String,Object> params = new HashMap<>();
	private String qs = "";
	private String link;
	private String contraProva;	

	public Response invokeReport(String code_report,Report rep){
		return invokeReport(code_report,rep,"1");
	}	
	public Response invokeReportPDF(String code_report,Report rep){		
		return invokeReport(code_report,rep,"2");		
	}
	@SuppressWarnings("unchecked")
	public Response invokeReport(String code_report,Report rep,String type){
		
	qs+="&p_type="+type; // se for 0 - preview, se for 1 - registar ocorencia , 2 - retornar PDF
	RepTemplate rt = new RepTemplate().find().andWhere("code", "=", code_report).one();
	qs+="&p_rep_id="+rt.getId();
	String contra_prova=rep.getContraProva();
	if(Core.isNull(contra_prova))
		 contra_prova = Report.generateContraProva("nosi.webapps."+rt.getApplication().getDad().toLowerCase());
	
	qs+="&ctpr="+Core.encrypt(contra_prova);
		if(rep!=null) 
			for(Entry<String, Object> p : rep.getParams().entrySet()) 
				if(!(p.getValue() instanceof List)) {
					if(p.getValue() != null && !p.getValue().toString().equals("?")) { 
						if (p.getKey().equals("isPublic") && p.getValue().equals("1")) 
							qs += "&" + p.getKey() + "=" + p.getValue(); // isPublic=1 :-) 
						else 
							qs += ("&name_array="+p.getKey() + "&value_array="+p.getValue()); 
					}
				}else {
					List<Object> parms = (List<Object>) p.getValue(); 
					for(Object v : parms) 
						qs += ("&name_array="+p.getKey() + "&value_array="+v.toString());
				}
		
		try {
			
			Response redirect = this.redirect("igrp_studio", "WebReport", "preview"+qs,this.queryString());
			redirect.setContent(contra_prova);
			return redirect;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	

	
	public Report getLinkReport(String code_report){
		return getLinkReport(code_report, false); 
	}

	public Report getLinkReport(String code_report, boolean isPublic){
		Report rep = new Report(); 
		RepTemplate rt = new RepTemplate().find().andWhere("code", "=", code_report).one();
		String contra_prova = Report.generateContraProva("nosi.webapps."+rt.getApplication().getDad().toLowerCase());
		if(isPublic) 
			Core.setAttribute("isPublic", "1"); 	
		
		rep.setLink(Route.getResolveUrl("igrp_studio", "WebReport", "preview&ctpr="+Core.encrypt(contra_prova)+"&p_rep_id="+rt.getId()+"&p_type=1")); 
		rep.setContraProva(contra_prova);
		return rep;
	}
	
	public static Response getLinkReport(String code_report,Report rep){
		return new Report().invokeReport(code_report, rep);
	}
	
	public Report addParam(String name,Object value){
		this.params.put(name, value);
		return this;
	}

	public Map<String, Object> getParams() {
		return params;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public Report getLinkReport(String code_report, QueryString<String, Object> queryString) {
		Report rep = new Report();
		RepTemplate rt = new RepTemplate().find().andWhere("code", "=", code_report).one();
		String contra_prova = Report.generateContraProva("nosi.webapps."+rt.getApplication().getDad().toLowerCase());
		
		rep.setLink(Route.getResolveUrl("igrp_studio", "WebReport", "preview&ctpr="+Core.encrypt(contra_prova)+"&p_rep_id="+rt.getId()+"&p_type=1")); 
		if(queryString!=null) {
			queryString.getQueryString().entrySet().stream().forEach(q->
				rep.addParam(q.getKey(), q.getValue().get(0))
			);
		}
		rep.setContraProva(contra_prova);
		return rep;
	}
	
	public String getLinkContraProva(String contraProva) {		
		return getLinkContraProva(contraProva,null, false,null);
	}
	
	public String getLinkContraProva(String contraProva,String appCodeDAD, Boolean outPDF,Boolean pdfToDownload) {
		contraProva=Core.encryptPublicPage(contraProva);
		StringBuilder qs = new StringBuilder("&ctprov="+contraProva);
		if(Core.isNotNull(appCodeDAD))
			qs.append("&codad="+appCodeDAD);
		if(Boolean.TRUE.equals(outPDF)) {
			qs.append("&outpdf="+outPDF);
			if(Core.isNotNull(pdfToDownload))
				qs.append("&todownld="+pdfToDownload);
		}		
		return Core.getHostName()+"?r=igrp_studio/web-report/get-contraprova"+qs;
	}
	
	public Response getRepContraProvaPDF(String contraProva,String appCodeDAD,Boolean pdfToDownload) throws TransformerFactoryConfigurationError, IOException {
		return processRepContraProva(contraProva, appCodeDAD, "true", pdfToDownload+"");
	}

	public static String generateContraProva(String packageFind) {
		List<Class<?>> allClasses = ReflectionHelper.findClassesByInterface(ReportKey.class,packageFind);
		if(allClasses != null) {
			for(Class<?> c:allClasses) {
				try {
					ReportKey key = (ReportKey) c.newInstance(); 
					return key.getKeyGenerate();
				} catch (Exception e) {
					return  GUIDGenerator.getGUIDUpperCase();
				}	
			}
		}
		return GUIDGenerator.getGUIDUpperCase();
	}



	/**
	 * @return the contraProva
	 */
	public String getContraProva() {
		return contraProva;
	}

	/**
	 * @param contraProva the contraProva to set
	 */
	public Report setContraProva(String contraProva) {
		this.contraProva = contraProva;
		return this;
	}
	
	/**
	 * @param id
	 * @param xml
	 * @return 
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException 
	 */
	public Response processPDF(String filename, CLob cLobXSL, String xml, String toDownload) throws TransformerFactoryConfigurationError, IOException {
		try {
			ByteArrayOutputStream actual;
			String dadBase = new Config().getLinkImgBase().replaceFirst("/", "");
			xml = xml.replace("<link_img>/", "<link_img>");
			xml = xml.replace("../images/", dadBase + "images/");			
			String xsl = new String(cLobXSL.getC_lob_content());
//			TODO: mudar para regex para sempre apanhar o nome
			xsl = xsl.replace(
					"?r=igrp_studio/WebReport/get-image&amp;p_file_name=logo.PNG&amp;env=sistema_protecao_social",
					dadBase + "images/IGRP/IGRP2.3/assets/img/sistema_protecao_social/reports/logo.PNG")
					.replaceFirst("3.4.1/css/", "4.0/css/")		
				//	.replace("<div class=\"page\" hasfooter=\"Y\" size=\"A4\" height=\"297\" layout=\"P\">", "<div class=\"page\" hasfooter=\"N\" size=\"A4L\" height=\"210\" layout=\"L\"> ")		
					.replace("../images/", dadBase + "images/")
					.replace("/IGRP/images/IGRP/IGRP2.3/", "IGRP/images/IGRP/IGRP2.3/")						
					.replaceFirst("@page \\{", "@page {margin:0;")
					;
			
		//	System.err.println("xsl "+xsl+"\n\n\n\n");
		//XML + XSL >> HTML
			StreamResult result = new TransformHelper().transformXMLXSL2HTML(xml, xsl);
			String baseUri4 = FileSystems.getDefault().getPath(new Config().basePathServer()).toUri().toString();
			baseUri4 = baseUri4.replace(dadBase, "");
			Document doc = html5ParseDocument(result.getWriter().toString(), baseUri4);	
			
			//HTML >> PDF
			actual = new TransformHelper().transformHTML2PDF(doc, baseUri4);
			
//				if(true)	this.format = Response.FORMAT_HTML;	
//					return this.renderView(doc.toString());
			
		//Ver PDF ou Download direto
		
			return this.xSend(actual.toByteArray(),filename, "application/pdf",Boolean.parseBoolean(toDownload));
			
		} catch (TransformerConfigurationException tce) {
			// Error generated by the parser
			System.err.println("\n** Transformer Factory error");
			System.err.println("   " + tce.getMessage());

			// Use the contained exception, if any
			Throwable x = tce;

			if (tce.getException() != null) {
				x = tce.getException();
			}

			x.printStackTrace();
		} catch (TransformerException te) {
			// Error generated by the parser
			System.err.println("\n** Transformation error");
			System.err.println("   " + te.getMessage());

			// Use the contained exception, if any
			Throwable x = te;

			if (te.getException() != null) {
				x = te.getException();
			}

			x.printStackTrace();
		} catch (SAXException sxe) {
			// Error generated by this application
			// (or a parser-initialization error)
			Exception x = sxe;

			if (sxe.getException() != null) {
				x = sxe.getException();
			}

			x.printStackTrace();
		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();
		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}
		return this.redirect("igrp", "ErrorPage", "exception");
	}
	
	public Document html5ParseDocument(String inputHTML, String baseUri) throws IOException{
	    org.jsoup.nodes.Document doc;
	   // System.out.println("parsing ...");
	  //  doc = Jsoup.parse(new File(inputHTML), "UTF-8",baseUri); // se link para um ficheiro
	   //doc = Jsoup.parse(new ByteArrayInputStream(inputHTML.getBytes("utf-8")), "UTF-8",baseUri);
	   doc = Jsoup.parse(inputHTML,baseUri);
		
	    
		Elements imgs = doc.select("img");
		imgs.forEach(i -> {
			if (i.attr("src").contains("?r=")) {
				// System.err.println("src imagem ds "+i.attr("src"));
				// webapps?r=igrp/File/get-file&uuid=821ccc01f84143b68df9ecc6fa2bb9d4&dad=sistema_de_avaliacao_igrpweb
				String uuid = StringUtils.substringBetween(i.attr("src"), "&uuid=", "&");
				CLob file;
				if (Core.isNotNull(uuid))
					file = Core.getFileByUuid(uuid);
				else
					file = Core.getFile(Core.getParamInt("p_id").intValue());
				if (file != null) {
					i.attr("src", "data:" + file.getMime_type() + ";base64, "+ Base64.getEncoder().encodeToString(file.getC_lob_content()));
				}
			}
		});
		Element content = doc.getElementById("content");
		Elements styleD = null;
		if (content != null) {
			styleD = content.getElementsByTag("style");
			if (styleD != null)
				doc.getElementsByTag("head").append(styleD + "");

			content.attr("style", "padding: 10mm 0mm");
			Elements qrcode = content.select("div.containerQrcode").tagName("object").attr("type", "image/barcode")
					.attr("style", "width:100px;height:100px;");
			qrcode.attr("value", Core.isNotNull(qrcode.attr("url")) ? qrcode.attr("url") : "Nothing/Nada");
			qrcode.removeAttr("url");
		}

		Element footer = doc.getElementById("footer");
		if (footer != null) {
			footer.attr("style", "" + "  bottom: 0;" + "  background-color: #267199;");
			// footer.getElementsByClass("holder-footer").attr("style", "width: 100%;"+
			// "background-color: #ccc;"+ "display: block;"+ "height: 55px;");

			footer.getElementsByClass("containerQrcode")

			// .append("<object
			// value=\"http://localhost:8080/IGRP-Template/app/webapps?r=igrp_studio/web-report/get-contraprova&amp;ctprov=L6ReshXo2HDpvDfyuWwE8Q==\"
			// url=\"\" type=\"image/barcode\"
			// style=\"width:100px;height:100px;margin:0;padding:0;\" ></object>\n")
			;
//			   footer.getElementsByClass("rfooter").attr("style", ""+ "float: left;"+ "    padding: 60px 10px 0 10px;");

		}
//		  	
		if (styleD != null) {
			// System.out.print("style \n"+styleD+"\n");
			styleD.remove();
		}

		final Document fromJsoup = new W3CDom().fromJsoup(doc);
		 System.out.println("parsing done ..." + doc+"");
		return fromJsoup;
	  }
	/**
	 * @param contraprova
	 * @param dad
	 * @param outInPDF
	 * @param toDownload
	 * @return
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException
	 */
	public Response processRepContraProva(String contraprova, String dad, String outInPDF, String toDownload)
			throws TransformerFactoryConfigurationError, IOException {
		RepInstance ri = new RepInstance().find().where("contra_prova", "=",contraprova).andWhere("application.dad", "=",dad).orderByDesc("id").one();
		String content = "";
		if(ri!=null && ri.getTemplate()!=null && !ri.hasError()){			
			if(outInPDF.equals("true")) {
				return new Report().processPDF(ri.getXsl_content().getName().replace(".xsl",""),ri.getXsl_content(), new String(ri.getXml_content().getC_lob_content()),toDownload);				
			}else {
				content = new String(ri.getXml_content().getC_lob_content());
				return this.renderView(content);
			}
			
		}
		return this.redirect("igrp", "ErrorPage", "exception");
	}
	
}
