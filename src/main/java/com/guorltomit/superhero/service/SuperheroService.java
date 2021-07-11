package com.guorltomit.superhero.service;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.guorltomit.superhero.audit.SmartAudit;
import com.guorltomit.superhero.beans.SuperheroBean;

@RepositoryRestResource(collectionResourceRel = "superherobeans", path = "superheros")
public interface SuperheroService extends PagingAndSortingRepository<SuperheroBean, Long> {

	@RestResource(rel = "name-contains", path = "name-contains")
	@SmartAudit
	List<SuperheroBean> findByNameContaining(@Param("query") String query);

}
