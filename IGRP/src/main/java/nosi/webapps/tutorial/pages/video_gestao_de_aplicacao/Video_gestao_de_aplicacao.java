package nosi.webapps.tutorial.pages.video_gestao_de_aplicacao;

import nosi.core.webapp.annotation.RParam;
import nosi.core.webapp.mvc.Model;





public class Video_gestao_de_aplicacao extends Model{		
	@RParam(rParamName = "p_paragraph_1_text")
	private String paragraph_1_text;
	@RParam(rParamName = "p_video_1_text")
	private String video_1_text;
	
	public void setParagraph_1_text(String paragraph_1_text){
		this.paragraph_1_text = paragraph_1_text;
	}
	public String getParagraph_1_text(){
		return this.paragraph_1_text;
	}
	
	public void setVideo_1_text(String video_1_text){
		this.video_1_text = video_1_text;
	}
	public String getVideo_1_text(){
		return this.video_1_text;
	}



}
