package com.example.demo.comtrollers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Clientes;
import com.example.demo.models.Region;
import com.example.demo.services.IClientesService;
import com.example.demo.services.IUploadFileService;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping(value = "/api")
public class ClientesRestController {
	@Autowired
	private IClientesService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@GetMapping("/clientes")
	public List<Clientes>index(){		
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Clientes>index(@PathVariable Integer page){	
		Pageable pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable);
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Clientes cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
		response.put("mensaje", "Error al realizar la consulta en la base de datos!");
		response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		}
		
		if(cliente==null) {
			response.put("mensaje","El cliente con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Clientes>(cliente,HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Clientes cliente, BindingResult result) {
		Clientes clientesNew =null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			clientesNew = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido creado con éxito");
		response.put("cliente",clientesNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Clientes cliente,BindingResult result, @PathVariable Long id) {
		Clientes clienteActual = clienteService.findById(id);
		Clientes clienteUpdated = null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(clienteActual==null) {
			response.put("mensaje","El cliente con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setCreateAt(cliente.getCreateAt());
			clienteActual.setRegion(cliente.getRegion());
			
			clienteUpdated = clienteService.save(clienteActual);		
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con éxito");
		response.put("cliente",clienteUpdated);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		Map<String,Object> response = new HashMap<>();
		try {
			Clientes cliente = clienteService.findById(id);
			String nombreFotoAnterior = cliente.getFoto();
			
			uploadFileService.eliminar(nombreFotoAnterior);
			
			clienteService.delete(id);		
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido eliminado con exito");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}	
	
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String,Object> response = new HashMap<>();
		Clientes cliente = clienteService.findById(id);
	
		
		if(!archivo.isEmpty()) {		
			String nombreArchivo=null;
			try {
				nombreArchivo= uploadFileService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				response.put("error", e.getMessage().concat(": ".concat(e.getCause().getMessage())));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = cliente.getFoto();
			
			uploadFileService.eliminar(nombreFotoAnterior);
			
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);
			
			response.put("cliente", cliente);
			response.put("mensaje", "Has subído correctamente la imagen"+ nombreArchivo);
			
		}
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
	
		Resource recurso =null;
		
		try {
			recurso = uploadFileService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = \""+ recurso.getFilename()+"\"");
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
	
	@GetMapping("/clientes/regiones")
	public List<Region> listarRegiones(){
		return clienteService.findAllRegiones();
	}
}

