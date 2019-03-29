package com.github.princesslana.smalld.json.examples;

import com.eclipsesource.json.Json;
import com.github.princesslana.smalld.json.SmallDMinimalJson;

/** Sample ping bot that uses {@link SmallDMinimalJson}. */
public class PingBotMinimalJson {

  /**
   * Entrypoint to run bot.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SmallDMinimalJson.run(
        System.getenv("SMALLD_TOKEN"),
        smalld -> {
          smalld.onGatewayPayload(
              json -> {
                if (json.getInt("op", -1) == 0
                    && json.getString("t", "").equals("MESSAGE_CREATE")
                    && json.get("d").asObject().getString("content", "").equals("++ping")) {

                  String channelId = json.get("d").asObject().getString("channel_id", null);

                  smalld.post(
                      "/channels/" + channelId + "/messages", Json.object().add("content", "pong"));
                }
              });
        });
  }
}
