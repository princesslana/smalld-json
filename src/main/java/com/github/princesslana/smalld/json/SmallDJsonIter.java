package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.SmallD;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import java.util.function.Consumer;

/**
 * Uses <a href="https://jsoniter.com/">json iterator</a> parser to parse payloads and responses.
 */
public class SmallDJsonIter extends SmallDDataBind<Any> {

  /**
   * Creates an instance wrapping the provided {@link SmallD}.
   *
   * @param smalld the SmallD isntance to wrap
   */
  public SmallDJsonIter(SmallD smalld) {
    super(smalld, Any.class);
  }

  protected String fromBind(Object payload) {
    return JsonStream.serialize(payload);
  }

  protected <R> R toBind(String str, Class<R> cls) {
    return JsonIterator.deserialize(str, cls);
  }

  /**
   * Create an instance and run a bot using it.
   *
   * @param token the Discord bot token to authenticate with
   * @param bot {@link Consumer} to setup the bot
   */
  public static void run(String token, Consumer<SmallDJsonIter> bot) {
    SmallDAdapter.run(token, SmallDJsonIter::new, bot);
  }
}
