package com.github.princesslana.smalld.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.github.princesslana.smalld.SmallD;
import java.util.function.Consumer;

/**
 * Uses the <a href="https://github.com/ralfstx/minimal-json">minimal-json</a> parser to parse
 * payloads and responses.
 */
public class SmallDMinimalJson extends SmallDAdapter<JsonObject> {

  /**
   * Creates an instance wrapping the provided {@link SmallD}.
   *
   * @param smalld the SmallD isntance to wrap
   */
  public SmallDMinimalJson(SmallD smalld) {
    super(smalld);
  }

  protected JsonObject to(String s) {
    return Json.parse(s).asObject();
  }

  protected String from(JsonObject payload) {
    return payload.toString();
  }

  /**
   * Create an instance and run a bot using it.
   *
   * @param token the Discord bot token to authenticate with
   * @param bot {@link Consumer} to setup the bot
   */
  public static void run(String token, Consumer<SmallDMinimalJson> bot) {
    SmallDAdapter.run(token, SmallDMinimalJson::new, bot);
  }
}
