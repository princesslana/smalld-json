package com.github.princesslana.smalld.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.github.princesslana.smalld.SmallD;
import java.util.function.Consumer;

public class SmallDMinimalJson extends SmallDAdapter<JsonObject> {

  public SmallDMinimalJson(SmallD smalld) {
    super(smalld);
  }

  protected JsonObject to(String s) {
    return Json.parse(s).asObject();
  }

  protected String from(JsonObject payload) {
    return payload.toString();
  }

  public static void run(String token, Consumer<SmallDMinimalJson> bot) {
    SmallDAdapter.run(token, SmallDMinimalJson::new, bot);
  }
}
