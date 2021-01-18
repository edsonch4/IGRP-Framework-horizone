<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"><xsl:output method="html" omit-xml-declaration="yes" encoding="utf-8" indent="yes" doctype-system="about:legacy-compat"/><xsl:template match="/"><html><head><xsl:call-template name="IGRP-head"/><link rel="stylesheet" type="text/css" href="{$path}/core/igrp/toolsbar/toolsbar.css?v={$version}"/><style/></head><body class="{$bodyClass} sidebar-off"><xsl:call-template name="IGRP-topmenu"/><form method="POST" class="IGRP-form" name="formular_default" enctype="multipart/form-data"><div class="container-fluid"><div class="row"><xsl:call-template name="IGRP-sidebar"/><div class="col-sm-9 col-md-10 col-md-offset-2 col-sm-offset-3 main" id="igrp-contents"><div class="content"><div class="row row-msg"><div class="gen-column col-md-12"><div class="gen-inner"><xsl:apply-templates mode="igrp-messages" select="rows/content/messages"/></div></div></div><div class="row " id="row-1774c7c7"><div class="gen-column col-sm-6"><div class="gen-inner"><xsl:if test="rows/content/titulo"><section class="content-header gen-container-item " gen-class="" item-name="titulo"><h2 class="disable-output-escaping"><xsl:value-of disable-output-escaping="yes" select="rows/content/titulo/fields/titulo_text/value"/></h2></section></xsl:if></div></div><div class="gen-column col-sm-6"><div class="gen-inner"><xsl:if test="rows/content/toolsbar_1"><div class="toolsbar-holder default gen-container-item " gen-structure="toolsbar" gen-fields=".btns-holder&gt;a.btn" gen-class="" item-name="toolsbar_1"><div class="btns-holder  pull-right" role="group"><xsl:apply-templates select="rows/content/toolsbar_1" mode="gen-buttons"><xsl:with-param name="vertical" select="'true'"/><xsl:with-param name="outline" select="'false'"/></xsl:apply-templates></div></div></xsl:if></div></div></div><div class="row " id="row-2f081f51"><div class="gen-column col-md-12"><div class="gen-inner"><xsl:if test="rows/content/form_1"><div class="box igrp-forms gen-container-item " gen-class="" item-name="form_1"><div class="box-body"><div role="form"><xsl:apply-templates mode="form-hidden-fields" select="rows/content/form_1/fields"/><xsl:if test="rows/content/form_1/fields/nome"><div class="form-group col-sm-3   gen-fields-holder" item-name="nome" item-type="text" required="required"><label for="{rows/content/form_1/fields/nome/@name}"><span><xsl:value-of select="rows/content/form_1/fields/nome/label"/></span></label><input type="text" value="{rows/content/form_1/fields/nome/value}" class="form-control  " id="{rows/content/form_1/fields/nome/@name}" name="{rows/content/form_1/fields/nome/@name}" required="required" maxlength="100" placeholder="{rows/content/form_1/fields/nome/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/nome"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/username"><div class="form-group col-sm-3   gen-fields-holder" item-name="username" item-type="text" required="required"><label for="{rows/content/form_1/fields/username/@name}"><span><xsl:value-of select="rows/content/form_1/fields/username/label"/></span></label><input type="text" value="{rows/content/form_1/fields/username/value}" class="form-control  " id="{rows/content/form_1/fields/username/@name}" name="{rows/content/form_1/fields/username/@name}" required="required" maxlength="50" placeholder="{rows/content/form_1/fields/username/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/username"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/email"><div class="form-group col-sm-3   gen-fields-holder" item-name="email" item-type="email" required="required"><label for="{rows/content/form_1/fields/email/@name}"><span><xsl:value-of select="rows/content/form_1/fields/email/label"/></span></label><input type="email" value="{rows/content/form_1/fields/email/value}" class="form-control  " id="{rows/content/form_1/fields/email/@name}" name="{rows/content/form_1/fields/email/@name}" required="required" maxlength="100" placeholder="{rows/content/form_1/fields/email/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/email"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/telefone"><div class="form-group col-sm-3   gen-fields-holder" item-name="telefone" item-type="text"><label for="{rows/content/form_1/fields/telefone/@name}"><span><xsl:value-of select="rows/content/form_1/fields/telefone/label"/></span></label><input type="text" value="{rows/content/form_1/fields/telefone/value}" class="form-control  " id="{rows/content/form_1/fields/telefone/@name}" name="{rows/content/form_1/fields/telefone/@name}" maxlength="250" placeholder="{rows/content/form_1/fields/telefone/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/telefone"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/telemovel"><div class="form-group col-sm-3   gen-fields-holder" item-name="telemovel" item-type="text"><label for="{rows/content/form_1/fields/telemovel/@name}"><span><xsl:value-of select="rows/content/form_1/fields/telemovel/label"/></span></label><input type="text" value="{rows/content/form_1/fields/telemovel/value}" class="form-control  " id="{rows/content/form_1/fields/telemovel/@name}" name="{rows/content/form_1/fields/telemovel/@name}" maxlength="250" placeholder="{rows/content/form_1/fields/telemovel/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/telemovel"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/nada"><div item-name="nada" class="box-head subtitle gen-fields-holder" text-color="1"><span><xsl:value-of select="rows/content/form_1/fields/nada/label"/></span></div></xsl:if><xsl:if test="rows/content/form_1/fields/password"><div class="form-group col-sm-3   gen-fields-holder" item-name="password" item-type="password" required="required"><label for="{rows/content/form_1/fields/password/@name}"><span><xsl:value-of select="rows/content/form_1/fields/password/label"/></span></label><input type="password" value="{rows/content/form_1/fields/password/value}" class="form-control  " id="{rows/content/form_1/fields/password/@name}" name="{rows/content/form_1/fields/password/@name}" required="required" maxlength="20" placeholder="{rows/content/form_1/fields/password/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/password"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/confirmar_password"><div class="form-group col-sm-3   gen-fields-holder" item-name="confirmar_password" item-type="password" required="required"><label for="{rows/content/form_1/fields/confirmar_password/@name}"><span><xsl:value-of select="rows/content/form_1/fields/confirmar_password/label"/></span></label><input type="password" value="{rows/content/form_1/fields/confirmar_password/value}" class="form-control  " id="{rows/content/form_1/fields/confirmar_password/@name}" name="{rows/content/form_1/fields/confirmar_password/@name}" required="required" maxlength="20" placeholder="{rows/content/form_1/fields/confirmar_password/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/confirmar_password"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/s_ass"><div item-name="s_ass" class="box-head subtitle gen-fields-holder" text-color="1"><span><xsl:value-of select="rows/content/form_1/fields/s_ass/label"/></span></div></xsl:if><xsl:if test="rows/content/form_1/fields/form_1_img_1"><div class="form-group col-sm-3  gen-fields-holder" item-name="form_1_img_1" item-type="file"><label for="{rows/content/form_1/fields/form_1_img_1/@name}"><span><xsl:value-of select="rows/content/form_1/fields/form_1_img_1/label"/></span></label><div class="input-group"><input type="text" class="form-control not-form" readonly=""><xsl:if test="rows/content/form_1/fields/form_1_img_1/@temp-value"><xsl:attribute name="value"><xsl:value-of select="rows/content/form_1/fields/form_1_img_1/@temp-value"/></xsl:attribute></xsl:if></input><span class="input-group-btn"><span class="btn btn-black file-btn-holder"><i class="fa fa-upload"/><input id="{rows/content/form_1/fields/form_1_img_1/@name}" name="{rows/content/form_1/fields/form_1_img_1/@name}" value="{rows/content/form_1/fields/form_1_img_1/value}" target-rend="" class="transparent " type="file" accept="image/*"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/form_1_img_1"/></xsl:call-template></input></span></span></div></div></xsl:if></div></div><xsl:apply-templates select="rows/content/form_1/tools-bar" mode="form-buttons"/></div></xsl:if></div></div></div></div></div></div></div><xsl:call-template name="IGRP-bottom"/></form><script type="text/javascript" src="{$path}/core/igrp/form/igrp.forms.js?v={$version}"/></body></html></xsl:template><xsl:include href="../../../xsl/tmpl/IGRP-functions.tmpl.xsl?v=16"/><xsl:include href="../../../xsl/tmpl/IGRP-variables.tmpl.xsl?v=16"/><xsl:include href="../../../xsl/tmpl/IGRP-home-include.tmpl.xsl?v=16"/><xsl:include href="../../../xsl/tmpl/IGRP-utils.tmpl.xsl?v=16"/><xsl:include href="../../../xsl/tmpl/IGRP-form-utils.tmpl.xsl?v=16"/></xsl:stylesheet>
