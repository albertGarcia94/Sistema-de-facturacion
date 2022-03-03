package com.example.demo.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	public Usuario findByUsername(String username);
}
