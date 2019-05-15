package com.google.dataflow.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import org.apache.beam.sdk.transforms.DoFn;

public class ParseMessages extends DoFn<String, Map<String, String>> {

  private transient Gson gson;
  private transient TypeToken typeToken;

  public Gson getGson() {
    if (gson == null) {
      gson = new Gson();
    }
    return gson;
  }

  public TypeToken getTypeToken() {
    if (typeToken == null) {
      typeToken = new TypeToken<Map<String, String>>() {
      };
    }
    return typeToken;
  }

  @ProcessElement
  public void processElement(@Element String element, OutputReceiver<Map<String, String>> out) {
    Map<String, String> message = getGson().fromJson(element, getTypeToken().getType());
    out.output(message);
  }
}
