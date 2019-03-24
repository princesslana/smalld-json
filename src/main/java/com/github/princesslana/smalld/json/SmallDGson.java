package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.SmallD;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.function.Consumer;

public class SmallDGson extends SmallDDataBind<JsonObject> {

  private final Gson gson = new Gson();

  public SmallDGson(SmallD smalld) {
    super(smalld, JsonObject.class);
  }

  public String fromBind(Object payload) {
    return gson.toJson(payload);
  }

  public <R> R toBind(String payload, Class<R> cls) {
    return gson.fromJson(payload, cls);
  }

  public static void run(String token, Consumer<SmallDGson> bot) {
    SmallDAdapter.run(token, SmallDGson::new, bot);
  }
}
