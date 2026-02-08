package com.athackctf.chall2025.jestersblog;

import com.vaadin.flow.component.dependency.CssImport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.athackctf.chall2025.jestersblog")
@EntityScan(basePackages = "com.athackctf.chall2025.jestersblog")
public class JestersBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(JestersBlogApplication.class, args);
	}

}
