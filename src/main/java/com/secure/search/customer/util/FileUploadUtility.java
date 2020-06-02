package com.secure.search.customer.util;

import com.secure.search.customer.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileUploadUtility {
	
	public static void uploadProductDetails(MultipartFile file, ProductDTO product) {
		try {
			
			byte[] bytes = file.getBytes();	
			Path path = Paths.get("/assets/images/" + file.getOriginalFilename());
			Files.write(path, bytes); 
			product.setFileName(file.getOriginalFilename());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
