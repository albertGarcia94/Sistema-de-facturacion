package com.example.demo.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.IClientesDao;
import com.example.demo.models.Clientes;
import com.example.demo.models.Region;
import com.example.demo.services.IClientesService;

@Service
public class ClienteServiceImpl implements IClientesService{
	@Autowired
	private IClientesDao clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Clientes> findAll() {
		return (List<Clientes>) clienteDao.findAll();
	}
	@Override
	@Transactional
	public Clientes findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}
	@Override
	@Transactional
	public Clientes save(Clientes cliente) {
		return clienteDao.save(cliente);
	}
	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}
	@Override
	@Transactional(readOnly = true)
	public Page<Clientes> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegiones() {
		return clienteDao.findAllRegiones();
	}

}
