package com.google.dataflow.example;

import javax.annotation.Nullable;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation.Required;

public interface CountPubSubOptions extends PipelineOptions {

  @Description("PubSub subscription to read json messages from")
  @Required
  String getSubscription();

  void setSubscription(String value);

  @Description("If true add an early firing trigger of processing time aligned to window size")
  @Required
  boolean isEarlyFiring();

  void setEarlyFiring(boolean value);

  @Description("Duration in seconds of fixed windows")
  @Default.Long(60)
  long getWindowDurationSeconds();

  void setWindowDurationSeconds(long value);

  @Description("Duration in seconds of allowed lateness")
  @Default.Long(60)
  long getAllowedLatenessSeconds();

  void setAllowedLatenessSeconds(long value);

  @Description("Count by this key, if not set we will just count all messages")
  @Nullable
  String getKey();

  void setKey(String value);
}
