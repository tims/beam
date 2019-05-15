package com.google.dataflow.example;

import static org.apache.beam.sdk.values.TypeDescriptors.strings;

import com.google.gson.Gson;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.windowing.AfterProcessingTime;
import org.apache.beam.sdk.transforms.windowing.AfterWatermark;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Never;
import org.apache.beam.sdk.transforms.windowing.Trigger;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.transforms.windowing.Window.ClosingBehavior;
import org.apache.beam.sdk.values.KV;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.format.ISODateTimeFormat;

@Slf4j
public class SuperCountPubSub {

  public static void main(String[] args) {
    PipelineOptionsFactory.register(SuperCountPubSubOptions.class);
    SuperCountPubSubOptions options = PipelineOptionsFactory.fromArgs(args).create()
        .as(SuperCountPubSubOptions.class);
    Pipeline pipeline = Pipeline.create(options);
    log.info(options.toString());

    Duration fixedWindowSize = Duration.standardSeconds(options.getWindowDurationSeconds());
    Duration allowedLateness = Duration.standardSeconds(options.getAllowedLatenessSeconds());

    // Note .withLateFirings(Never.ever()), ensure that all late data will be fired in a single pane when the window closes
    Trigger trigger = options.isEarlyFiring() ?
        AfterWatermark.pastEndOfWindow()
            .withEarlyFirings(AfterProcessingTime.pastFirstElementInPane()
                .alignedTo(fixedWindowSize))
            .withLateFirings(Never.ever())
        : AfterWatermark.pastEndOfWindow()
            .withLateFirings(Never.ever());

    final String key = options.getKey();

    for (String subscription : options.getSubscriptions()) {
      final String subscriptionName = subscription.substring(subscription.lastIndexOf("/") + 1);
      log.info(subscription);
      pipeline
          .apply(PubsubIO.readStrings().fromSubscription(subscription))
          .apply(ParDo.of(new ParseMessages()))
          .apply(Window.<Map<String, String>>into(FixedWindows.of(fixedWindowSize))
              .triggering(trigger)
              .withAllowedLateness(allowedLateness, ClosingBehavior.FIRE_ALWAYS)
              .discardingFiredPanes())
          .apply(WithKeys.of(
              (Map<String, String> map) -> map.getOrDefault(key, "default"))
              .withKeyType(strings()))
          .apply(Count.perKey())
          .apply(ParDo.of(new DoFn<KV<String, Long>, String>() {

                @ProcessElement
                public void processElement(ProcessContext context) {
                  Instant paneTimestamp = context.timestamp();
                  Instant processedTimestamp = new Instant(System.currentTimeMillis());

                  CountWithTiming event = new CountWithTiming();
                  event.setSubscription(subscriptionName);
                  event.setPane(paneTimestamp.toString(ISODateTimeFormat.dateTimeNoMillis()));
                  event.setProcessed(
                      processedTimestamp.toString(ISODateTimeFormat.dateTimeNoMillis()));
                  event.setLagSeconds(
                      (processedTimestamp.getMillis() - paneTimestamp.getMillis()) / 1000);
                  event.setTiming(context.pane().getTiming().toString());
                  event.setValue(context.element().toString());
                  context.output(new Gson().toJson(event));
                }
              })
          )
          .apply(TextIO.write().withWindowedWrites().withNumShards(1)
              .to("output/" + subscriptionName + "/data"));
    }
    pipeline.run();
  }

  @DefaultCoder(AvroCoder.class)
  @Getter
  @Setter
  @ToString
  public static class CountWithTiming {

    private String timing;
    private String subscription;
    private String pane;
    private String processed;
    private String value;
    private long lagSeconds;
  }


}
