package es.sinjava.superhero.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		log.debug("TracedItemController {} byId", id);
		Optional<SuperheroBean> item = superHeroService.findById(id);
		return ResponseEntity.of(item);
	}

	@GetMapping("/superhero")
	public ResponseEntity<?> all() {
		Iterable<SuperheroBean> item = superHeroService.findAll();
		return ResponseEntity.ok(item);
	}

}
