package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.models.Clientes;
import com.example.demo.models.Region;

public interface IClientesService {
	
public List<Clientes> findAll();

public Page<Clientes> findAll(Pageable pageable);

public Clientes findById(Long id);

public Clientes save(Clientes cliente);

public List<Region> findAllRegiones();

public void delete(Long id);
}
