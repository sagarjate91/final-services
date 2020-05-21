package com.secure.search.customer.util;


import com.secure.search.customer.model.Product;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


public class FileUploadUtility {
	
	private static String REAL_PATH = null;

	public static void uploadProductDetails(HttpServletRequest request,MultipartFile file, Product product) {

		REAL_PATH = request.getSession().getServletContext().getRealPath("/assets/images/");

		if(!new File(REAL_PATH).exists()) {
			new File(REAL_PATH).mkdirs();
		}
		try {
			File localFile = new File(REAL_PATH + file.getOriginalFilename());
			file.transferTo(localFile);
			product.setFileName(file.getOriginalFilename());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
