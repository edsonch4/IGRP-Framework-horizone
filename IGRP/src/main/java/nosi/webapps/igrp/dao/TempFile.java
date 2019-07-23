package nosi.webapps.igrp.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * joao.augoncalves
 * 12/07/2019
 */

@Entity
@Table(name="tbl_temp_file")
public class TempFile extends IGRPBaseActiveRecord<TempFile> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1451788451461124034L;
	@Id
	@Column(updatable = false, nullable = false)
	private String uuid;
	private String name;
	private String mime_type;
	@Lob
	@Column(nullable=false)
	@Type(type="org.hibernate.type.BinaryType")
	private byte[] content;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMime_type() {
		return mime_type;
	}
	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	@Override
	public TempFile findOne(Object value) {
		return this.find().where("uuid","=",value).one();
	}
}
