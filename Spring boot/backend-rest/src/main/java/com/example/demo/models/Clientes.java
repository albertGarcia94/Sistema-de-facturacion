package com.example.demo.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "clientes")
public class Clientes implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min=3, max=12)
	@Column(nullable = false)
	private String nombre;
	
	@NotEmpty
	private String apellido;
	@Column(nullable = false, unique = true)
	
	@NotEmpty
	@Email
	private String email;
	
	@NotNull(message = "No puede estar Vacio")
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	private String foto;
	
	@NotNull(message = "La region no puede estar vacia")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="region_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Region region;
	
//	@PrePersist
//	public void prePersist() {
//		createAt = new Date();
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}


	private static final long serialVersionUID = 1L;
}
