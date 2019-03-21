package nosi.webapps.igrp_studio.pages.pesquisa_nacionalidade;

import nosi.core.webapp.Controller;
import nosi.core.webapp.databse.helpers.ResultSet;
import nosi.core.webapp.databse.helpers.QueryInterface;
import java.io.IOException;
import nosi.core.webapp.Core;
import nosi.core.webapp.Response;
/*----#start-code(packages_import)----*/
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import nosi.core.webapp.webservices.rest.ConsumeJson;
import nosi.core.config.Config;
/*----#end-code----*/
		
public class Pesquisa_nacionalidadeController extends Controller {
	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		Pesquisa_nacionalidade model = new Pesquisa_nacionalidade();
		model.load();
		Pesquisa_nacionalidadeView view = new Pesquisa_nacionalidadeView();
		/*----#start-code(index)----*/
		model.setTreemenu_1(this.chamarServico("0"));
		
		/*----#end-code----*/
		view.setModel(model);
		return this.renderView(view);	
	}
	
/*----#start-code(custom_actions)----*/

	public List<Pesquisa_nacionalidade.Treemenu_1>  chamarServico(String id) throws IOException {
		Properties setting = this.loadConfig("common", "main.xml");
		String url = setting.getProperty("link.rest.pesquisa_geografia")+"?id="+id;
		String authorization = setting.getProperty("authorization.rest.pesquisa_geografia");
		ConsumeJson json_obj = new ConsumeJson();
		String json  = json_obj.getJsonFromUrl(url, authorization);
		List<Pesquisa_nacionalidade.Treemenu_1> list_nac = new ArrayList<>();
		JSONObject obj = new JSONObject(json);
		JSONObject Entries = obj.getJSONObject("Entries");
		try {
			JSONArray Entry = Entries.getJSONArray("Entry");
			for(int i = 0; i < Entry.length(); i++) {
				JSONObject local = Entry.getJSONObject(i);
				Pesquisa_nacionalidade.Treemenu_1 tab_nac = new Pesquisa_nacionalidade.Treemenu_1();
				tab_nac.setTreemenu_1_tmid(local.getString("id"));
				tab_nac.setTreemenu_1_child("1");
				try{
					tab_nac.setTreemenu_1_link_desc(local.getString("nacionalidade"));
					list_nac.add(tab_nac);
					}catch (Exception e) {
						tab_nac.setTreemenu_1_link_desc("");
					};
			}
		}catch (Exception e) {
			
		}
		list_nac.sort(Comparator.comparing(Pesquisa_nacionalidade.Treemenu_1::getTreemenu_1_link_desc));
		return list_nac;
	}
	

	private Properties loadConfig(String filePath, String fileName) {
		String path = new Config().getBasePathConfig() + File.separator + filePath;
		File file = new File(getClass().getClassLoader().getResource(path + File.separator + fileName).getPath().replaceAll("%20", " "));
		
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(file)) {
			props.loadFromXML(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

/*----#end-code----*/
}
