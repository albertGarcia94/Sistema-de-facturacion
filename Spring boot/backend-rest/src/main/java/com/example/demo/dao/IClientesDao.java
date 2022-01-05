package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Clientes;
import com.example.demo.models.Region;

public interface IClientesDao extends JpaRepository<Clientes, Long>{

	@Query("from Region")
	public List<Region> findAllRegiones();
}
