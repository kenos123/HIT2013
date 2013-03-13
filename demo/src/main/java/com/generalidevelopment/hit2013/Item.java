package com.generalidevelopment.hit2013;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Item {

	@NotNull
	private String name;

	@NotNull
	private BigDecimal price;

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		final EntityManager em = new Item().entityManager;
		if (em == null) {
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		}
		return em;
	}

	public static long countItems() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Item o", Long.class).getSingleResult();
	}

	public static List<Item> findAllItems() {
		return entityManager().createQuery("SELECT o FROM Item o", Item.class).getResultList();
	}

	public static Item findItem(final Long id) {
		if (id == null) {
			return null;
		}
		return entityManager().find(Item.class, id);
	}

	public static List<Item> findItemEntries(final int firstResult, final int maxResults) {
		return entityManager().createQuery("SELECT o FROM Item o", Item.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			final Item attached = Item.findItem(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		this.entityManager.clear();
	}

	@Transactional
	public Item merge() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		final Item merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}
}
