package es.sinjava.superhero.repos;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import es.sinjava.superhero.audit.SmartAudit;
import es.sinjava.superhero.beans.SuperheroBean;

// crea su propio controller... lo dejamos
@RepositoryRestResource(collectionResourceRel = "superherobeans", path = "superheros")
public interface SuperheroRepository extends PagingAndSortingRepository<SuperheroBean, Long> {

	@RestResource(rel = "name-contains", path = "name-contains")
	@SmartAudit
	List<SuperheroBean> findByNameContaining(@Param("query") String query);

}
