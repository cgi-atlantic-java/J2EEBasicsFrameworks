package me.bantling.j2ee.basics.view.form;

import javax.servlet.http.HttpServletRequest;

/**
 * Create an instance of some type T from the request. The data could be received via the parameters or the body.
 * 
 * @param <T> the type to instantiate
 */
@FunctionalInterface
public interface ObjectCreator<T> {
  public T create(
    HttpServletRequest request
  );
}
