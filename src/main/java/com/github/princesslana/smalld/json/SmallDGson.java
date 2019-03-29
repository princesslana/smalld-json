package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.SmallD;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.function.Consumer;

/** Uses <a href="https://github.com/google/gson">gson</a> to parse payloads and responses. */
public class SmallDGson extends SmallDDataBind<JsonObject> {

  private final Gson gson = new Gson();

  /**
   * Creates an instance wrapping the provided {@link SmallD}.
   *
   * @param smalld the SmallD isntance to wrap
   */
  public SmallDGson(SmallD smalld) {
    super(smalld, JsonObject.class);
  }

  protected String fromBind(Object payload) {
    return gson.toJson(payload);
  }

  protected <R> R toBind(String payload, Class<R> cls) {
    return gson.fromJson(payload, cls);
  }

  /**
   * Create an instance and run a bot using it.
   *
   * @param token the Discord bot token to authenticate with
   * @param bot {@link Consumer} to setup the bot
   */
  public static void run(String token, Consumer<SmallDGson> bot) {
    SmallDAdapter.run(token, SmallDGson::new, bot);
  }
}
