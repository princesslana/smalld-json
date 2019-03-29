package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.Attachment;
import com.github.princesslana.smalld.SmallD;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Adapts request and responses for {@link SmallD} to a different type. Classes extending this must
 * implement methods to convert that type to and from {@link String}.
 *
 * @param <T> the type that responses and payloads are bound to
 */
public abstract class SmallDAdapter<T> {

  private final SmallD smalld;

  private List<Consumer<T>> gatewayListeners = new ArrayList<>();

  /**
   * Creats an instance using the provided {@link SmallD}.
   *
   * @param smalld SmallD instance to wrap
   */
  public SmallDAdapter(SmallD smalld) {
    this.smalld = smalld;

    smalld.onGatewayPayload(this::notifyPayload);
  }

  protected SmallD getSmallD() {
    return smalld;
  }

  protected abstract String from(T payload);

  protected abstract T to(String str);

  private void notifyPayload(String str) {
    T payload = to(str);

    gatewayListeners.forEach(l -> l.accept(payload));
  }

  /**
   * Add a listener for messages from the Gateway. Will convert those messages using {@link
   * #to(String)} before passing to the listener.
   *
   * @param listener listener to execute when payload received
   * @see SmallD#onGatewayPayload(Consumer)
   */
  public void onGatewayPayload(Consumer<T> listener) {
    gatewayListeners.add(listener);
  }

  /**
   * Send a payload to the gateway.
   *
   * @param payload the payload to send
   * @see SmallD#sendGatewayPayload(String)
   */
  public void sendGatewayPayload(T payload) {
    smalld.sendGatewayPayload(from(payload));
  }

  /**
   * Make a HTTP GET request and convert the response to the adapted class.
   *
   * @param path the path to make the request to
   * @return the response bound to the provided class
   * @see SmallD#get(String)
   */
  public T get(String path) {
    return to(smalld.get(path));
  }

  /**
   * Make a HTTP POST request, converting the body and the response from/to the adapted class.
   *
   * @param path the path to make the request to
   * @param body the body to send with the request
   * @param as attachments for a multipart request
   * @return the response bound to the provided class
   * @see SmallD#post(String, String, Attachment...)
   */
  public T post(String path, T body, Attachment... as) {
    return to(smalld.post(path, from(body), as));
  }

  /**
   * Make a HTTP PUT request, converting the body and the response from/to the adapted class.
   *
   * @param path the path to make the request to
   * @param body the body to send with the request
   * @return the response bound to the provided class
   * @see SmallD#put(String, String)
   */
  public T put(String path, T body) {
    return to(smalld.put(path, from(body)));
  }

  /**
   * Make a HTTP PATCH request, converting the body and the response from/to the adapted class.
   *
   * @param path the path to make the request to
   * @param body the body to send with the request
   * @return the response bound to the provided class
   * @see SmallD#patch(String, String)
   */
  public T patch(String path, T body) {
    return to(smalld.patch(path, from(body)));
  }

  /**
   * Make a HTTP DELETE request.
   *
   * @param path the path to make the request to
   * @see SmallD#delete(String)
   */
  public void delete(String path) {
    smalld.delete(path);
  }

  /**
   * Create an instance using the factory provided and run a bot using it.
   *
   * @param token the Discord bot token to authenticate with
   * @param factory the factory to create an adapter
   * @param bot {@link Consumer} to setup the bot
   * @param <T> the type responses and payloads will be bound to
   * @param <A> the type of the adapter created by the factory
   */
  public static <T, A extends SmallDAdapter<T>> void run(
      String token, Function<SmallD, A> factory, Consumer<A> bot) {
    SmallD.run(
        token,
        smalld -> {
          A adapter = factory.apply(smalld);

          bot.accept(adapter);
        });
  }
}
