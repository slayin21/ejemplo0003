package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
		/*
		 * ingresar a la siguiente pagina desde el navegador
		 * 
		 * http://localhost:8080/demo/swagger-ui/index.html
		 * 
		 * cambiar la barra del explorer por la siguiente
		 * 
		 * /demo/v3/api-docs
		 * 
		 * para ver desde la pag web
		 * 
		 * http://localhost:8088/demo/
		 * 
		 * */
	}

}
