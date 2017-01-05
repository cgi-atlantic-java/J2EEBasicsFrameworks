package me.bantling.j2ee.basics.model.mao.jdbc;

import static java.util.Objects.requireNonNull;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.DDL_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.DML_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.INSERT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.MULTIPLE_ROWS;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NO_ROWS;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.SELECT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.UPDATE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.bantling.j2ee.basics.model.entity.AddressEntity;
import me.bantling.j2ee.basics.model.entity.Country;
import me.bantling.j2ee.basics.model.entity.Region;
import me.bantling.j2ee.basics.model.exception.ModelException;
import me.bantling.j2ee.basics.model.mao.api.AddressMAO;
import me.bantling.j2ee.basics.model.transaction.Transaction;
import me.bantling.j2ee.basics.model.validation.Validators;
import me.bantling.j2ee.basics.view.bean.AddressBean;

/**
 * Persist an {@link AddressBean} to the database
 */
public class AddressJDBCMAO implements AddressMAO {
  /**
   * Create the table
   * 
   * Wouldn't normally have this in an MAO, but since the database is in memory, the table has to be created at
   * every startup.
   * 
   * @throws ModelException
   */
  @Override
  public void createTable(
  ) throws ModelException {      
    try (
      final Statement stmt = Transaction.getTransaction().getJDBCConnection().createStatement();
    ) {
      stmt.execute(
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
    } catch (final Exception e) {
      throw new ModelException(DDL_FAILED, "CREATE TABLE address", e);
    }
  }
  
  @Override
  public void validate(
    final AddressEntity entity
  ) throws ModelException {
    Validators.get(AddressEntity.class).validate(entity);
  }
  
  /**
   * Insert an Address
   * 
   * @param entity
   * @throws SQLException
   */
  @Override
  public void insert(
    final AddressEntity entity
  ) throws ModelException {
    requireNonNull(entity, "address");
    validate(entity);
    
    // We can prevent SQL injection attacks by always using a PreparedStatement for DML
    try (
      final PreparedStatement stmt = Transaction.getTransaction().getJDBCConnection().prepareStatement(
        "insert into address (" +
        "  id," +
        "  line1," +
        "  line2," +
        "  city," +
        "  country," +
        "  region," +
        "  postal_code" +
        ") values (?, ?, ?, ?, ?, ?, ?)"
      );
    ) {
      int i = 1;
      stmt.setInt(i++, entity.getId());
      stmt.setString(i++, entity.getLine1());
      stmt.setString(i++, entity.getLine2());
      stmt.setString(i++, entity.getCity());
      stmt.setString(i++, entity.getCountry().name());
      stmt.setString(i++, entity.getRegion().name());
      stmt.setString(i++, entity.getPostalCode());

      final int affected = stmt.executeUpdate();
      if (affected != 1) {
        throw new ModelException(DML_FAILED, affected == 0 ? NO_ROWS: MULTIPLE_ROWS, "Address");
      }
    } catch (final ModelException e) {
      throw e;
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, INSERT, "INSERT INTO address", e);
    }
  }
  
  /**
   * Select an Address by id
   * 
   * @param conn
   * @param id
   * @return
   * @throws SQLException
   */
  @Override
  public AddressEntity select(
    final int id
  ) throws ModelException {
    AddressEntity address;
    
    try (
      final PreparedStatement stmt = Transaction.getTransaction().getJDBCConnection().prepareStatement(
        "select id, line1, line2, city, country, region, postal_code" +
        "  from address" +
        " where id = ?"
      );
    ) {
      stmt.setInt(1, id);
      
      try (
        final ResultSet rs = stmt.executeQuery();
      ) {
        if (rs.next()) {
          int i = 1;
          
          address = new AddressEntity();
          address.setId(rs.getInt(i++));
          address.setLine1(rs.getString(i++));
          address.setLine2(rs.getString(i++));
          address.setCity(rs.getString(i++));
          address.setCountry(Country.valueOf(rs.getString(i++)));
          address.setRegion(Region.valueOf(rs.getString(i++)));
          address.setPostalCode(rs.getString(i++));
          
          if (rs.next()) {
            // Must never have multiple addresses
            throw new ModelException(DML_FAILED, MULTIPLE_ROWS, "Address");
          }
        } else {
          throw new ModelException(DML_FAILED, NO_ROWS, "Address");
        }
      }
    } catch (final ModelException e) {
      throw e;
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, SELECT, "SELECT FROM address", e);
    }
    
    return address;
  }
  
  /**
   * Update an Address by id
   * 
   * @param entity
   * @throws SQLException
   */
  @Override
  public void update(
    final AddressEntity entity
  ) throws ModelException {
    requireNonNull(entity, "address");
    validate(entity);

    try (
      final PreparedStatement stmt = Transaction.getTransaction().getJDBCConnection().prepareStatement(
        "update address set" +
        "       line1 = ?," +
        "       line2 = ?," +
        "       city = ?," +
        "       country = ?," +
        "       region = ?," +
        "       postal_code = ?" +
        " where id = ?"
      );
    ) {
      int i = 1;
      stmt.setString(i++, entity.getLine1());
      stmt.setString(i++, entity.getLine2());
      stmt.setString(i++, entity.getCity());
      stmt.setString(i++, entity.getCountry().name());
      stmt.setString(i++, entity.getRegion().name());
      stmt.setString(i++, entity.getPostalCode());
      stmt.setInt(i++, entity.getId());

      final int affected = stmt.executeUpdate();
      if (affected != 1) {
        throw new ModelException(DML_FAILED, affected == 0 ? NO_ROWS : MULTIPLE_ROWS, "Address");
      }
    } catch (final ModelException e) {
      throw e;
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, UPDATE, "Address", e);
    }
  }
  
  /**
   * Delete an Address by id
   * 
   * @param id
   * @throws SQLException
   */
  @Override
  public void delete(
    final int id
  ) throws ModelException {
    try (
      final PreparedStatement stmt = Transaction.getTransaction().getJDBCConnection().prepareStatement(
        "delete from address where id = ?"
      );
    ) {
      
      stmt.setInt(1, id);

      final int affected = stmt.executeUpdate();
      if (affected != 1) {
        throw new ModelException(DML_FAILED, affected == 0 ? NO_ROWS : MULTIPLE_ROWS, "Address");
      }
    } catch (final ModelException e) {
      throw e;
    } catch (final Exception e) {
      throw new ModelException(DML_FAILED, UPDATE, "Address", e);
    }
  }
}
