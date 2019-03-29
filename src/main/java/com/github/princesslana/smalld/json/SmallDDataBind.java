package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.Attachment;
import com.github.princesslana.smalld.SmallD;
import java.util.function.Consumer;

/**
 * Allows binding of payloads and responses to POJOs. This is functionality supported by most JSON
 * parsing libraries.
 *
 * @param <T> the basic JSON Object type supported by the parsing library
 */
public abstract class SmallDDataBind<T> extends SmallDAdapter<T> {

  private final Class<T> dataBindClass;

  /**
   * Creates an instace that wraps the given smalld and uses the provided class for databinding to
   * JSON. {@code dataBindClass} should be the basic class that is used to represent a JSON Object.
   * It will be used to implement {@link #from(T)} and {@link #to(String)}
   *
   * @param smalld the {@link SmallD} instance to wrap
   * @param dataBindClass the base JSON Object type that JSON is parsed from/to
   */
  public SmallDDataBind(SmallD smalld, Class<T> dataBindClass) {
    super(smalld);

    this.dataBindClass = dataBindClass;
  }

  protected String from(T payload) {
    return fromBind(payload);
  }

  protected T to(String str) {
    return toBind(str, dataBindClass);
  }

  protected abstract String fromBind(Object payload);

  protected abstract <R> R toBind(String str, Class<R> cls);

  /**
   * Add a listener for messages from the Gateway. If the payload can not be bound to the provided
   * class, the listener is not called.
   *
   * @param cls the class to bind the payload to
   * @param listener listener to execute when payload received
   * @param <R> the type of the bound payload
   * @see SmallD#onGatewayPayload(String)
   */
  public <R> void onGatewayPayload(Class<R> cls, Consumer<R> listener) {
    getSmallD().onGatewayPayload(p -> listener.accept(toBind(p, cls)));
  }

  @Override
  public void sendGatewayPayload(Object payload) {
    getSmallD().sendGatewayPayload(fromBind(payload));
  }

  /**
   * Make a HTTP GET request and bind the response to the given class.
   *
   * @param path the path to make the request to
   * @param cls the class to bind the payload to
   * @param <R> the type of the bound payload
   * @return the response bound to the provided class
   * @see SmallD#get(String)
   */
  public <R> R get(String path, Class<R> cls) {
    return toBind(getSmallD().get(path), cls);
  }

  /**
   * Make a HTTP POST request converting {@code body} to JSON and binding the response to the given
   * class.
   *
   * @param path the path to make the request to
   * @param body the body to send with the request as JSON
   * @param cls the class to bind the payload to
   * @param as attachments for a mulitpart request
   * @param <R> the type of the bound payload
   * @return the response bound to the provided class
   * @see SmallD#post(String, String, Attachment...)
   */
  public <R> R post(String path, Object body, Class<R> cls, Attachment... as) {
    return toBind(getSmallD().post(path, fromBind(body), as), cls);
  }

  /**
   * Make a HTTP PUT request converting {@code body} to JSON and binding the response to the given
   * class.
   *
   * @param path the path to make the request to
   * @param body the body to send with the request as JSON
   * @param cls the class to bind the payload to
   * @param <R> the type of the bound payload
   * @return the response bound to the provided class
   * @see SmallD#put(String, String)
   */
  public <R> R put(String path, Object body, Class<R> cls) {
    return toBind(getSmallD().put(path, fromBind(body)), cls);
  }

  /**
   * Make a HTTP PATCH request converting {@code body} to JSON and binding the response to the given
   * class.
   *
   * @param path the path to make the request to
   * @param body the body to send with the request as JSON
   * @param cls the class to bind the payload to
   * @param <R> the type of the bound payload
   * @return the response bound to the provided class
   * @see SmallD#patch(String, String)
   */
  public <R> R patch(String path, Object body, Class<R> cls) {
    return toBind(getSmallD().patch(path, fromBind(body)), cls);
  }
}
