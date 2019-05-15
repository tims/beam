package com.google.dataflow.example;

import static org.apache.beam.sdk.values.TypeDescriptors.strings;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.windowing.AfterProcessingTime;
import org.apache.beam.sdk.transforms.windowing.AfterWatermark;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Never;
import org.apache.beam.sdk.transforms.windowing.Trigger;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.transforms.windowing.Window.ClosingBehavior;
import org.joda.time.Duration;

@Slf4j
public class CountPubSub {

  public static void main(String[] args) {
    PipelineOptionsFactory.register(CountPubSubOptions.class);
    CountPubSubOptions options = PipelineOptionsFactory.fromArgs(args).create()
        .as(CountPubSubOptions.class);
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
    pipeline
        .apply(PubsubIO.readStrings().fromSubscription(options.getSubscription()))
        .apply(ParDo.of(new ParseMessages()))
        .apply(Window.<Map<String, String>>into(FixedWindows.of(fixedWindowSize))
            .triggering(trigger)
            .withAllowedLateness(allowedLateness, ClosingBehavior.FIRE_ALWAYS)
            .discardingFiredPanes())
        .apply(WithKeys.of(
            (Map<String, String> map) -> map.getOrDefault(key, "default"))
            .withKeyType(strings()))
        .apply(Count.perKey())
        .apply(new LogTiming<>());
    pipeline.run();
  }
}
