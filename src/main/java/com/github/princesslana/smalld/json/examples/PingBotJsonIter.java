package com.github.princesslana.smalld.json.examples;

import com.jsoniter.any.Any;
import com.github.princesslana.smalld.json.SmallDJsonIter;

public class PingBotJsonIter {

  private final SmallDJsonIter smalld;

  private PingBotJsonIter(SmallDJsonIter smalld) {
    this.smalld = smalld;
    smalld.onGatewayPayload(this::onPayload);
  }

  private void onPayload(Any json) {
    if (json.toInt("op") == 0 && json.toString("t").equals("MESSAGE_CREATE")) {
      onMessageCreate(json.get("d"));
    }
  }

  private void onMessageCreate(Any json) {
    if (json.toString("content").equals("++ping")) {

      String channelId = json.toString("channel_id");

      smalld.post("/channels/" + channelId + "/messages", Message.of("pong"), Void.class);
    }
  }

  /**
   * Entrypoint to run bot.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SmallDJsonIter.run(System.getenv("SMALLD_TOKEN"), PingBotJsonIter::new);
  }

  private static class Message {
    private String content;

    public static Message of(String content) {
      Message m = new Message();
      m.content = content;
      return m;
    }
  }
}
