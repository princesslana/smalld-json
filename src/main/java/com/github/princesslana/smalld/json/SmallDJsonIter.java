package com.github.princesslana.smalld.json;

import com.github.princesslana.smalld.SmallD;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import java.util.function.Consumer;

public class SmallDJsonIter extends SmallDDataBind<Any> {

  public SmallDJsonIter(SmallD smalld) {
    super(smalld, Any.class);
  }

  protected String fromBind(Object payload) {
    return JsonStream.serialize(payload);
  }

  protected <R> R toBind(String str, Class<R> cls) {
    return JsonIterator.deserialize(str, cls);
  }

  public static void run(String token, Consumer<SmallDJsonIter> bot) {
    SmallDAdapter.run(token, SmallDJsonIter::new, bot);
  }
}
