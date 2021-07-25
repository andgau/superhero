package es.sinjava.superhero.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.sinjava.superhero.beans.SuperheroBean;
import es.sinjava.superhero.services.ISuperHeroService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@AllArgsConstructor
@Log4j2
public class SuperheroController {

	private ISuperHeroService superHeroService;

	@GetMapping("/superhero/{id}")
	public ResponseEntity<?> byId(@PathVariable Long id) {
		log.info("SuperHeroController {} byId", id);
		Optional<SuperheroBean> item = superHeroService.findById(id);
		return ResponseEntity.of(item);
	}

	@GetMapping("/superhero")
	public ResponseEntity<?> all() {
		log.info("SuperHeroController All");
		Iterable<SuperheroBean> item = superHeroService.findAll();
		return ResponseEntity.ok(item);
	}

	@PostMapping("/superhero")
	public ResponseEntity<?> create(@RequestBody SuperheroBean superhero) {
		log.info("SuperHeroController create");
		SuperheroBean item = superHeroService.save(superhero);
		return ResponseEntity.ok(item);
	}

	@PutMapping("/superhero")
	public ResponseEntity<?> update(@RequestBody SuperheroBean superhero) {
		log.info("SuperHeroController update");
		SuperheroBean item = superHeroService.edit(superhero);
		return ResponseEntity.ok(item);
	}

	@DeleteMapping("/superhero/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		log.info("SuperHeroController {} byId", id);
		superHeroService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
