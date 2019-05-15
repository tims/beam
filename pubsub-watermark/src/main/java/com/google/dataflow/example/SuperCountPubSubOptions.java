package com.google.dataflow.example;

import javax.annotation.Nullable;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation.Required;

public interface SuperCountPubSubOptions extends PipelineOptions {

  @Description("PubSub subscriptions to read json messages from")
  @Required
  String[] getSubscriptions();

  void setSubscriptions(String[] values);

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
