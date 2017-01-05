package me.bantling.j2ee.basics.model.mao.jpa;

import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.DDL_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.DML_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.INSERT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.MULTIPLE_ROWS;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NO_ROWS;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.SELECT;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import me.bantling.j2ee.basics.model.entity.AddressEntity;
import me.bantling.j2ee.basics.model.exception.ModelException;
import me.bantling.j2ee.basics.model.mao.api.AddressMAO;
import me.bantling.j2ee.basics.model.transaction.Transaction;
import me.bantling.j2ee.basics.model.validation.Validators;

public class AddressJPAMAO implements AddressMAO {
  @Override
  public void createTable(
  ) throws ModelException {
    try {
      final EntityManager em = Transaction.getTransaction().getConnection();
      
      final Query query = em.createNativeQuery(
        "create table address(" +
        "  id          integer primary key," +
        "  line1       varchar(32) not null," +
        "  line2       varchar(32) default null," +
        "  city        varchar(32) not null," +
        "  country     varchar(32) not null," +
        "  region      varchar(2) not null," +
        "  postal_code varchar(16) not null," +
        "  constraint address_id_pos check(id > 0)," +
        "  constraint address_line1_ne check(line1 != '')," +
        "  constraint address_line2_ne check(line2 != '')," +
        "  constraint address_city_ne check(city != '')," +
        "  constraint address_country_ne check(country != '')," +
        "  constraint region_ne check(region != '')," +
        "  constraint address_postal_code_ne check(postal_code != '')" +
        ")"
      );
      
      query.executeUpdate();
    } catch (final Exception e) {
      throw new ModelException(DDL_FAILED, "CREATE TABLE address", e);
    }
  }
  
  @Override
  public void validate(
    final AddressEntity entity
  ) throws ModelException{
    Validators.get(AddressEntity.class).validate(entity);
  }

  @Override
  public void insert(
    final AddressEntity entity
  ) throws ModelException {
    validate(entity);
    
    try {       
      // An insert is an attempt to persist an object that *was not* read in by a select operation
      final EntityManager em = Transaction.getTransaction().getConnection();
      em.persist(entity);
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, INSERT, "INSERT INTO address", e);
    }
  }

  @Override
  public AddressEntity select(
    final int id
  ) throws ModelException {
    AddressEntity address;
    
    try {
      final EntityManager em = Transaction.getTransaction().getConnection();
      final CriteriaBuilder builder = em.getCriteriaBuilder();
      final CriteriaQuery<AddressEntity> query = builder.createQuery(AddressEntity.class);
      final Root<AddressEntity> root = query.from(AddressEntity.class);
      final ParameterExpression<Integer> byId = builder.parameter(Integer.class);
      query.select(root).where(builder.equal(root.get("id"), byId));
      
      final TypedQuery<AddressEntity> typed = em.createQuery(query);
      typed.setParameter(byId, new Integer(id));
      
      final List<AddressEntity> results = typed.getResultList();
      
      if (results.isEmpty()) {
        throw new ModelException(DML_FAILED, NO_ROWS, "Address");
      }
      
      final Iterator<AddressEntity> iter = results.iterator();
      address = iter.next();
      
      if (iter.hasNext()) {
        throw new ModelException(DML_FAILED, MULTIPLE_ROWS, "Address");  
      }
    } catch (final ModelException e) {
      throw e;
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, SELECT, "SELECT FROM address", e);
    }
    
    return address;
  }

  @Override
  public void update(
    final AddressEntity entity
  ) throws ModelException {
    validate(entity);
    
    try {
      // An insert is an attempt to persist an object that *was* read in by a select operation
      final AddressEntity entity_ = select(entity.getId());
      
      // Update object read in with details passed
      entity_.setLine1(entity.getLine1());
      entity_.setLine2(entity.getLine2());
      entity_.setCity(entity.getCity());
      entity_.setCountry(entity.getCountry());
      entity_.setRegion(entity.getRegion());
      entity_.setPostalCode(entity.getPostalCode());
      
      final EntityManager em = Transaction.getTransaction().getConnection();
      em.persist(entity_);
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, INSERT, "UPDATE address", e);
    }
  }

  @Override
  public void delete(
    final int id
  ) throws ModelException {
    try {
      final EntityManager em = Transaction.getTransaction().getConnection();
      
      final Query query = em.createNativeQuery(
        "delete from address where id = ?"
      );
      query.setParameter(1, new Integer(id));

      final int affected = query.executeUpdate();
      if (affected != 1) {
        throw new ModelException(DML_FAILED, affected == 0 ? NO_ROWS : MULTIPLE_ROWS, "Address");
      }
    } catch (final Exception e) {
      throw new ModelException(DDL_FAILED, "CREATE TABLE address", e);
    }      
  }
}
