
package nosi.webapps.igrp.pages.gestaodeacesso;
/*----#START-PRESERVED-AREA(PACKAGES_IMPORT)----*/
import nosi.core.webapp.Controller;
import java.io.IOException;
import nosi.core.webapp.Core;
import nosi.core.webapp.Response;
import nosi.core.webapp.Igrp;
import nosi.webapps.igrp.dao.Application;
import nosi.webapps.igrp.dao.Organization;
import java.util.List;
import java.util.ArrayList;
/*----#END-PRESERVED-AREA----*/

public class GestaodeacessoController extends Controller {		


	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(INDEX)----*/
      Gestaodeacesso model = new Gestaodeacesso();

      	List<Gestaodeacesso.Org_table> data = new ArrayList<>();
      	String ichange = Igrp.getInstance().getRequest().getParameter("ichange");	
      model.load();
		if(Igrp.getMethod().equalsIgnoreCase("post")){
		
          	if(ichange.equalsIgnoreCase("p_aplicacao") && model.getAplicacao()!=null && !model.getAplicacao().equals("")){
              model.setAdicionar_organica1("igrp","NovaOrganica","index&target=_blank&id_app="+model.getAplicacao());
              model.setGestao_de_utilizadores1("igrp","PesquisarUtilizador","index");
              for(Organization org:new Organization().find().andWhere("application","=",Integer.parseInt(model.getAplicacao())).all()){
                Gestaodeacesso.Org_table table = new Gestaodeacesso.Org_table();
               table.setP_id(""+org.getId());
                table.setOrg_nome(org.getName());
                table.setEstado(org.getStatus());
                 table.setEstado_check(1);
               // table.setAdicionar_perfil("igrp","NovoPerfil","index&id_app="+org.getApplication().getId()+"&id_org="+org.getId()+"&target=_blank");
                //table.setAdicionar_perfil_desc("Adicionar Perfil");
                table.setMostrar_perfis("igrp","pesquisar-perfil","index&id_app="+org.getApplication().getId()+"&id_org="+org.getId()+"&target=_blank");
                table.setMostrar_perfis_desc("Mostrar Perfil");
                data.add(table);
              }
            }else
               model.setAdicionar_organica1("igrp","NovaOrganica","index&target=_blank");
		}
		GestaodeacessoView view = new GestaodeacessoView(model);
      	view.p_id.setParam(true);
      	view.aplicacao.setParam(true);
      	view.aplicacao.setValue(new Application().getListApps());
        view.org_table.addData(data);
        view.setPageTitle("Gestão de Acesso");
      //	if(ichange!=null && ichange.equalsIgnoreCase("p_aplicacao") && model.getAplicacao()!=null && !model.getAplicacao().equals("")){
        //  view.btn_adicionar_organica.setPage("NovaOrganica");
          //view.btn_adicionar_organica.setLink("index&target=_blank&id_app="+model.getAplicacao());
      	//}
      view.btn_eliminar.setVisible(false);
		return this.renderView(view);
		/*----#END-PRESERVED-AREA----*/
	}


	public Response actionEditar() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(EDITAR)----*/
      	String p_id = Igrp.getInstance().getRequest().getParameter("p_id");
      	if(p_id!=null){
      		Organization org = new Organization().findOne(Integer.parseInt(p_id));
      		if(org!=null)
      			return this.redirect("igrp","NovaOrganica","editar&target=_blank&p_id="+p_id);
      	}
		return this.redirect("igrp","error-page","exception");	      	
		/*----#END-PRESERVED-AREA----*/
	}
	

	public Response actionMenu() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(MENU)----*/
		String p_id = Igrp.getInstance().getRequest().getParameter("p_id");
		if(p_id!=null){
      		Organization org = new Organization().findOne(Integer.parseInt(p_id));
      		org = org.find().andWhere("application.id", "=", Integer.parseInt(p_id));
      		if(org!=null)
      			return this.redirect("igrp", "MenuOrganica", "index&target=_blank","id="+p_id+"&type=org&env_fk="+org.getApplication().getId());
      	}
		return this.redirect("igrp","error-page","exception");	 
		/*----#END-PRESERVED-AREA----*/
	}
	

	public Response actionTransaction() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(TRANSACTION)----*/
		String p_id = Igrp.getInstance().getRequest().getParameter("p_id");
		if(p_id!=null){
      		Organization org = new Organization().findOne(Integer.parseInt(p_id));
      		if(org!=null)
      			return this.redirect("igrp", "TransacaoOrganica", "index&target=_blank","id="+p_id+"&type=org");
      	}
		return this.redirect("igrp","error-page","exception");	 
		/*----#END-PRESERVED-AREA----*/
	}
	

	public Response actionEliminar() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(ELIMINAR)----*/
		String p_id = Igrp.getInstance().getRequest().getParameter("p_id");
		if(p_id!=null){
      		Organization org = new Organization().findOne(Integer.parseInt(p_id));
      		if(org!=null && org.delete()){
      			Core.setMessageSuccess();
      			return this.redirect("igrp","gestaodeacesso","index");
      		}      			
      	}
		return this.redirect("igrp","error-page","exception");	 
		/*----#END-PRESERVED-AREA----*/
	}
	
	public Response actionEtapas() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(ETAPAS)----*/
		String id = Igrp.getInstance().getRequest().getParameter("p_id");
        if(id!=null && !id.equals("")){      
        	Organization org = new Organization().findOne(id);    
          return this.redirect("igrp", "Task", "index&target=_blank&type=org&p_id="+id+"&p_aplicacao="+org.getApplication().getId());
        }
        return this.redirectError();
		/*----#END-PRESERVED-AREA----*/
	}
	/*----#START-PRESERVED-AREA(CUSTOM_ACTIONS)----*/
	
	/*----#END-PRESERVED-AREA----*/
}
