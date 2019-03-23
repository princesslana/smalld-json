package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.Attachment;
import com.github.princesslana.smalld.SmallD;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SmallDAdapter<T> {

  private final SmallD smalld;

  private List<Consumer<T>> gatewayListeners = new ArrayList<>();

  public SmallDAdapter(SmallD smalld) {
    this.smalld = smalld;

    smalld.onGatewayPayload(this::notifyPayload);
  }

  protected abstract String from(T payload);

  protected abstract T to(String str);

  private void notifyPayload(String str) {
    T payload = to(str);

    gatewayListeners.forEach(l -> l.accept(payload));
  }

  public void onGatewayPayload(Consumer<T> listener) {
    gatewayListeners.add(listener);
  }

  public void sendGatewayPayload(T payload) {
    smalld.sendGatewayPayload(from(payload));
  }

  public T get(String path) {
    return to(smalld.get(path));
  }

  public T post(String path, T body, Attachment... as) {
    return to(smalld.post(path, from(body), as));
  }

  public T put(String path, T body) {
    return to(smalld.put(path, from(body)));
  }

  public T patch(String path, T body) {
    return to(smalld.patch(path, from(body)));
  }

  public void delete(String path) {
    smalld.delete(path);
  }

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
