package com.oscar.springboot.app;

import java.util.ArrayList;
import java.util.List;

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
		List<String> usuariosList = new ArrayList<String>();
		usuariosList.add("Oscar Hernandez");
		usuariosList.add("Ary Luna");
		usuariosList.add("Miguel Luna");
		usuariosList.add("Omar Cruz");
		usuariosList.add("Maria Sabina");
		usuariosList.add("Diego Luna");
		usuariosList.add("Bruce lee");
		
		Flux<String> nombres = Flux.fromIterable(usuariosList);//Flux.just("Oscar Hernandez", "Ary Luna", "Miguel Luna", "Omar Cruz", "Maria Sabina", "Diego Luna", "Bruce lee");
		
		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("Bruce"))
				.doOnNext(e -> {
					if(e.getNombre().isEmpty()) {
						throw new RuntimeException("nombre no puede ser vacio");
					}
					System.out.println(e);
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					
					return usuario;
				});
		
		usuarios.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion con exito");
					}
				});
	}

}
