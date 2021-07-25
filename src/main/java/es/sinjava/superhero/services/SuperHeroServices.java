package es.sinjava.superhero.services;


import org.springframework.stereotype.Service;

import es.sinjava.superhero.beans.SuperheroBean;
import es.sinjava.superhero.repos.SuperheroRepository;

@Service
public class SuperHeroServices extends  BaseServiceImpl<SuperheroBean, Long, SuperheroRepository > 
		implements ISuperHeroService {

}
