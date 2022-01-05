package com.example.demo.services.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.IUploadFileService;

@Service
public class UploadFileServiceImpl implements IUploadFileService{
	
	private final Logger log = org.slf4j.LoggerFactory.getLogger(UploadFileServiceImpl.class);
	private final static String DIRECTORIO_UPLOAD = "uploads";
	
	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		Path rutaArchivo = getPath(nombreFoto);
		Resource recurso = new UrlResource(rutaArchivo.toUri());
				
		if(!recurso.exists() && !recurso.isReadable()){
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("not-user.png").toAbsolutePath();			
		}
		
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		String nombreArchivo = UUID.randomUUID().toString() +"_"+ archivo.getOriginalFilename().replace(" ", "");
		Path rutaArchivo = getPath(nombreArchivo);
		
		Files.copy(archivo.getInputStream(), rutaArchivo);

		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		
		if(nombreFoto !=null && nombreFoto.length()> 0) {
			Path rutaArchivoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			File archivoAnterior = rutaArchivoAnterior.toFile();
			if(archivoAnterior.exists() && archivoAnterior.canRead()) {
				archivoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
