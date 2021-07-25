package es.sinjava.superhero.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import es.sinjava.superhero.audit.SmartAudit;
import lombok.extern.log4j.Log4j2;

// Clase que elimina código repetivo en los servicios
@Log4j2
public abstract class BaseServiceImpl<T, ID, R extends CrudRepository<T, ID>> implements IBaseService<T, ID> {

	@Autowired
	protected R repo;

	@Override
	@SmartAudit
	public T save(T t) {
		log.debug("Save entity  {} ", t.getClass().getName());
		return repo.save(t);
	}

	@Override
	@SmartAudit
	public Optional<T> findById(ID id) {
		log.debug("Find entity  {} ", id);
		return repo.findById(id);
	}

	@Override
	@SmartAudit
	public Iterable<T> findAll() {
		log.debug("Find All ");
		return repo.findAll();
	}

	@Override
	@SmartAudit
	public T edit(T t) {
		log.debug("Update entity  {}", t.getClass().getName());
		return repo.save(t);
	}

	@Override
	@SmartAudit
	public void delete(T t) {
		log.debug("Delete entity  {}", t.getClass());
		repo.delete(t);
	}

	@Override
	@SmartAudit
	public void deleteById(ID id) {
		log.debug("Delete entity by Id  {}", id);
		if (repo.existsById(id)) {
			repo.deleteById(id);
		}
	}

}
