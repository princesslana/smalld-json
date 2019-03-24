package com.github.princesslana.smalld.json.examples;

import com.github.princesslana.smalld.json.SmallDGson;
import com.google.gson.annotations.SerializedName;

public class PingBotGson {

  private final SmallDGson smalld;

  private PingBotGson(SmallDGson smalld) {
    this.smalld = smalld;
    smalld.onGatewayPayload(CreateMessage.class, this::onCreateMessage);
  }

  private void onCreateMessage(CreateMessage createMsg) {
    if (createMsg.isMessageCreate() && createMsg.isContent("++ping")) {

      String channelId = createMsg.getMessage().getChannelId();

      smalld.post("/channels/" + channelId + "/messages", Message.of("pong"), Void.class);
    }
  }

  /**
   * Entrypoint to run bot.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SmallDGson.run(System.getenv("SMALLD_TOKEN"), PingBotGson::new);
  }

  private static class CreateMessage {
    private String t;
    private Message d;

    public boolean isMessageCreate() {
      return t != null && t.equals("MESSAGE_CREATE");
    }

    public boolean isContent(String rhs) {
      return d.getContent().equals(rhs);
    }

    public Message getMessage() {
      return d;
    }
  }

  private static class Message {
    @SerializedName("channel_id")
    private String channelId;

    private String content;

    public String getContent() {
      return content;
    }

    public String getChannelId() {
      return channelId;
    }

    public static Message of(String content) {
      Message m = new Message();
      m.content = content;
      return m;
    }
  }
}
