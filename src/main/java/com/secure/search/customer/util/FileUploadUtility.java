package com.secure.search.customer.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import com.secure.search.customer.model.Product;


public class FileUploadUtility {
	
	public static void uploadProductDetails(HttpServletRequest request,MultipartFile file, Product product) {
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
