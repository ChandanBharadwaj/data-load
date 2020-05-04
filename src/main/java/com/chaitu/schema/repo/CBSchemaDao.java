package com.chaitu.schema.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.chaitu.schema.model.TableSchema;

@Repository
@Transactional(readOnly = true)
public class CBSchemaDao {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public TableSchema save(TableSchema table) {

		em.persist(table);
		if (table.getId() == null) {
			return table;
		} else {
			return em.merge(table);
		}
	}
}
