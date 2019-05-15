package com.google.dataflow.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Instant;

@Slf4j
public class LogTiming<T> extends PTransform<PCollection<T>, PCollection<T>> {

  @Override
  public PCollection<T> expand(PCollection<T> input) {
    return input.apply(ParDo.of(new DoFn<T, T>() {

      @ProcessElement
      public void processElement(ProcessContext context) {
        String timing = context.pane().getTiming().toString();
        Instant timestamp = context.timestamp();
        T element = context.element();
        log.info("timing={}, timestamp={}, currentTime={} value={}",
            timing,
            timestamp,
            new Instant(System.currentTimeMillis()),
            element);
        context.output(element);
      }

    }));
  }
}
