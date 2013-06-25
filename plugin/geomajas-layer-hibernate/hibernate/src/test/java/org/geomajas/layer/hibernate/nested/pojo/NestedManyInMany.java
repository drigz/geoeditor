package org.geomajas.layer.hibernate.nested.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "nestedManyInMany")
public class NestedManyInMany {
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "textAttr")
	private String textAttr;

	
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public String getTextAttr() {
		return textAttr;
	}

	
	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}
	
	

}
