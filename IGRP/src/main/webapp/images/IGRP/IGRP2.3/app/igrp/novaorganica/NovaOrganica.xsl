<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"><xsl:output method="html" omit-xml-declaration="yes" encoding="utf-8" indent="yes" doctype-system="about:legacy-compat"/><xsl:template match="/"><html><head><xsl:call-template name="IGRP-head"/><link rel="stylesheet" type="text/css" href="{$path}/plugins/view/igrp.view.css?v={$version}"/><link rel="stylesheet" type="text/css" href="{$path}/core/igrp/toolsbar/toolsbar.css?v={$version}"/><link rel="stylesheet" type="text/css" href="{$path}/plugins/select2/select2.min.css?v={$version}"/><link rel="stylesheet" type="text/css" href="{$path}/plugins/select2/select2.style.css?v={$version}"/><style/></head><body class="{$bodyClass} sidebar-off"><xsl:call-template name="IGRP-topmenu"/><form method="POST" class="IGRP-form" name="formular_default" enctype="multipart/form-data"><div class="container-fluid"><div class="row"><xsl:call-template name="IGRP-sidebar"/><div class="col-sm-9 col-md-10 col-md-offset-2 col-sm-offset-3 main" id="igrp-contents"><div class="content"><div class="row row-msg"><div class="gen-column col-md-12"><div class="gen-inner"><xsl:apply-templates mode="igrp-messages" select="rows/content/messages"/></div></div></div><div class="row " id="row-4146607a"><div class="gen-column col-sm-6"><div class="gen-inner"><xsl:if test="rows/content/sectionheader_1"><section class="content-header gen-container-item " gen-class="" item-name="sectionheader_1"><h2 class="disable-output-escaping"><xsl:value-of disable-output-escaping="yes" select="rows/content/sectionheader_1/fields/sectionheader_1_text/value"/></h2></section></xsl:if><xsl:if test="rows/content/view_1"><div class="box clearfix view-block gen-container-item " has-img="false" template="info" item-separator-border="true" gen-class="" item-name="view_1"><div class="box-body"><xsl:apply-templates mode="form-hidden-fields" select="rows/content/view_1/fields"/><xsl:if test="rows/content/view_1/fields/view_1_img"><img src="{rows/content/view_1/fields/view_1_img/value}"/></xsl:if><div class="view-body clearfix "><xsl:if test="rows/content/view_1/fields/documento"><div class="view-item gen-fields-holder" item-name="documento"><a href="{rows/content/view_1/fields/documento/value}" target="_newtab" target-fields="" request-fields=""><i class="fa fa-question-circle"/><span><span><xsl:value-of select="rows/content/view_1/fields/documento/label"/></span></span></a></div></xsl:if></div></div></div></xsl:if></div></div><div class="gen-column col-sm-6"><div class="gen-inner"><xsl:if test="rows/content/toolsbar_1"><div class="toolsbar-holder default gen-container-item " gen-structure="toolsbar" gen-fields=".btns-holder&gt;a.btn" gen-class="" item-name="toolsbar_1"><div class="btns-holder  pull-right" role="group"><xsl:apply-templates select="rows/content/toolsbar_1" mode="gen-buttons"><xsl:with-param name="vertical" select="'true'"/><xsl:with-param name="outline" select="'false'"/></xsl:apply-templates></div></div></xsl:if></div></div></div><div class="row " id="row-2d31a0d5"><div class="gen-column col-sm-12"><div class="gen-inner"><xsl:if test="rows/content/form_1"><div class="box igrp-forms gen-container-item " gen-class="" item-name="form_1"><div class="box-body"><div role="form"><xsl:apply-templates mode="form-hidden-fields" select="rows/content/form_1/fields"/><xsl:if test="rows/content/form_1/fields/nome"><div class="form-group col-sm-3   gen-fields-holder" item-name="nome" item-type="text" required="required"><label for="{rows/content/form_1/fields/nome/@name}"><span><xsl:value-of select="rows/content/form_1/fields/nome/label"/></span></label><input type="text" value="{rows/content/form_1/fields/nome/value}" class="form-control  " id="{rows/content/form_1/fields/nome/@name}" name="{rows/content/form_1/fields/nome/@name}" required="required" maxlength="60" placeholder="{rows/content/form_1/fields/nome/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/nome"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/codigo"><div class="form-group col-sm-3   gen-fields-holder" item-name="codigo" item-type="text" required="required"><label for="{rows/content/form_1/fields/codigo/@name}"><span><xsl:value-of select="rows/content/form_1/fields/codigo/label"/></span></label><input type="text" value="{rows/content/form_1/fields/codigo/value}" class="form-control  " id="{rows/content/form_1/fields/codigo/@name}" name="{rows/content/form_1/fields/codigo/@name}" required="required" maxlength="100" placeholder="{rows/content/form_1/fields/codigo/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/codigo"/></xsl:call-template></input></div></xsl:if><xsl:if test="rows/content/form_1/fields/ativo"><div class="col-sm-3  gen-fields-holder" item-name="ativo" item-type="checkbox"><div class="form-group"><div class="checkbox form-check-offset"><label class="container-box checkbox-switch switch"><xsl:value-of select="rows/content/form_1/fields/ativo/label"/><input type="checkbox" name="{rows/content/form_1/fields/ativo/@name}" value="1" class="checkbox " label="{rows/content/form_1/fields/ativo/label}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/ativo"/></xsl:call-template><xsl:if test="rows/content/form_1/fields/ativo/value = '1'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if></input><span class="slider round"/><span class="checkmark"/></label></div></div></div></xsl:if><xsl:if test="rows/content/form_1/fields/nada"><div item-name="nada" class="box-head subtitle gen-fields-holder" text-color="1"><span><xsl:value-of select="rows/content/form_1/fields/nada/label"/></span></div></xsl:if><xsl:if test="rows/content/form_1/fields/aplicacao"><div class="col-sm-3 form-group  gen-fields-holder" item-name="aplicacao" item-type="select" required="required"><label for="{rows/content/form_1/fields/aplicacao/@name}"><xsl:value-of select="rows/content/form_1/fields/aplicacao/label"/></label><select class="form-control select2 IGRP_change" id="form_1_aplicacao" name="{rows/content/form_1/fields/aplicacao/@name}" required="required"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/aplicacao"/></xsl:call-template><xsl:for-each select="rows/content/form_1/fields/aplicacao/list/option"><option value="{value}" label="{text}"><xsl:if test="@selected='true'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><span><xsl:value-of select="text"/></span></option></xsl:for-each></select></div></xsl:if><xsl:if test="rows/content/form_1/fields/organizacao_pai"><div class="col-sm-3 form-group  gen-fields-holder" item-name="organizacao_pai" item-type="select"><label for="{rows/content/form_1/fields/organizacao_pai/@name}"><xsl:value-of select="rows/content/form_1/fields/organizacao_pai/label"/></label><select class="form-control select2 " id="form_1_organizacao_pai" name="{rows/content/form_1/fields/organizacao_pai/@name}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/organizacao_pai"/></xsl:call-template><xsl:for-each select="rows/content/form_1/fields/organizacao_pai/list/option"><option value="{value}" label="{text}"><xsl:if test="@selected='true'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><span><xsl:value-of select="text"/></span></option></xsl:for-each></select></div></xsl:if><xsl:if test="rows/content/form_1/fields/extras"><div item-name="extras" class="box-head subtitle gen-fields-holder" text-color="1"><span><xsl:value-of select="rows/content/form_1/fields/extras/label"/></span></div></xsl:if><xsl:if test="rows/content/form_1/fields/plsql_codigo"><div class="form-group col-sm-3   gen-fields-holder" item-name="plsql_codigo" item-type="text"><label for="{rows/content/form_1/fields/plsql_codigo/@name}"><span><xsl:value-of select="rows/content/form_1/fields/plsql_codigo/label"/></span></label><input type="text" value="{rows/content/form_1/fields/plsql_codigo/value}" class="form-control  " id="{rows/content/form_1/fields/plsql_codigo/@name}" name="{rows/content/form_1/fields/plsql_codigo/@name}" maxlength="250" placeholder="{rows/content/form_1/fields/plsql_codigo/@placeholder}"><xsl:call-template name="setAttributes"><xsl:with-param name="field" select="rows/content/form_1/fields/plsql_codigo"/></xsl:call-template></input></div></xsl:if></div></div><xsl:apply-templates select="rows/content/form_1/tools-bar" mode="form-buttons"/></div></xsl:if></div></div></div></div></div></div></div><xsl:call-template name="IGRP-bottom"/></form><script type="text/javascript" src="{$path}/core/igrp/form/igrp.forms.js?v={$version}"/><script type="text/javascript" src="{$path}/plugins/select2/select2.full.min.js?v={$version}"/><script type="text/javascript" src="{$path}/plugins/select2/select2.init.js?v={$version}"/></body></html></xsl:template><xsl:include href="../../../xsl/tmpl/IGRP-functions.tmpl.xsl?v=19"/><xsl:include href="../../../xsl/tmpl/IGRP-variables.tmpl.xsl?v=19"/><xsl:include href="../../../xsl/tmpl/IGRP-home-include.tmpl.xsl?v=19"/><xsl:include href="../../../xsl/tmpl/IGRP-utils.tmpl.xsl?v=19"/><xsl:include href="../../../xsl/tmpl/IGRP-form-utils.tmpl.xsl?v=19"/></xsl:stylesheet>
