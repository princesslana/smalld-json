package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.Attachment;
import com.github.princesslana.smalld.SmallD;
import java.util.function.Consumer;

public abstract class SmallDDataBind<T> extends SmallDAdapter<T> {

  private final Class<T> dataBindClass;

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

  public <R> void onGatewayPayload(Class<R> cls, Consumer<R> listener) {
    getSmallD().onGatewayPayload(p -> listener.accept(toBind(p, cls)));
  }

  @Override
  public void sendGatewayPayload(Object payload) {
    getSmallD().sendGatewayPayload(fromBind(payload));
  }

  public <R> R get(String path, Class<R> cls) {
    return toBind(getSmallD().get(path), cls);
  }

  public <R> R post(String path, Object body, Class<R> cls, Attachment... as) {
    return toBind(getSmallD().post(path, fromBind(body), as), cls);
  }

  public <R> R put(String path, Object body, Class<R> cls) {
    return toBind(getSmallD().put(path, fromBind(body)), cls);
  }

  public <R> R patch(String path, Object body, Class<R> cls) {
    return toBind(getSmallD().patch(path, fromBind(body)), cls);
  }
}
