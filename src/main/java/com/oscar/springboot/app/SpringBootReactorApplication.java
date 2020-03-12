package com.oscar.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oscar.springboot.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Oscar", "Ary", "Miguel", "Omar")
				.map(nombre -> new Usuario(nombre.toUpperCase(), ""))
				.doOnNext(e -> {
					if(e.getNombre().isEmpty()) {
						throw new RuntimeException("nombre no puede ser vacio");
					}
					System.out.println(e.getNombre());
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					
					return usuario;
				});
		
		nombres.subscribe(e -> log.info(e.getNombre()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion con exito");
					}
				});
	}

}
