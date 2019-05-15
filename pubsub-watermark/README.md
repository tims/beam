# Tests


Setup python message generation script

    python3 -m venv env
    source env/bin/activate
    pip3 install -r src/main/python/requirements.txt

Setup the project

    gcloud auth login
    export PROJECT=<project>

Created the topics and subscriptions:

    gcloud pubsub topics create test-10000ms --project=$PROJECT
    gcloud pubsub topics create test-5000ms --project=$PROJECT
    gcloud pubsub topics create test-2000ms --project=$PROJECT
    gcloud pubsub topics create test-1000ms --project=$PROJECT
    gcloud pubsub topics create test-500ms --project=$PROJECT
    gcloud pubsub topics create test-100ms --project=$PROJECT
    gcloud pubsub topics create test-50ms --project=$PROJECT
    gcloud pubsub topics create test-10ms --project=$PROJECT
    gcloud pubsub subscriptions create test-10000ms --topic=test-10000ms --project=$PROJECT
    gcloud pubsub subscriptions create test-5000ms --topic=test-5000ms --project=$PROJECT
    gcloud pubsub subscriptions create test-2000ms --topic=test-2000ms --project=$PROJECT
    gcloud pubsub subscriptions create test-1000ms --topic=test-1000ms --project=$PROJECT
    gcloud pubsub subscriptions create test-500ms --topic=test-500ms --project=$PROJECT
    gcloud pubsub subscriptions create test-100ms --topic=test-100ms --project=$PROJECT
    gcloud pubsub subscriptions create test-50ms --topic=test-50ms --project=$PROJECT
    gcloud pubsub subscriptions create test-10ms --topic=test-10ms --project=$PROJECT


# CountPubSub

10000ms test

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-10000ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-10000ms --sleep 10000 --limit 0 --num_players=10


5000ms test, on time panes are fired ~3-5 minutes past what would be a perfect watermark, Late pane is fired ~1-3 minutes past the on time pane:

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-5000ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-5000ms --sleep 5000 --limit 0 --num_players=10


2000ms test, on time panes are fired ~3-5 minutes past what would be a perfect watermark, Late pane is fired ~1-3 minutes past the on time pane:

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-2000ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-2000ms --sleep 2000 --limit 0 --num_players=10


1000ms test, on time panes are fired ~1-2 minutes past what would be a perfect watermark, Late pane is fired ~1-2 minutes past the on time pane:

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-1000ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-1000ms --sleep 1000 --limit 0 --num_players=10


500ms test, on time panes are fired ~1-2 minutes past what would be a perfect watermark, Late pane is fired ~1-2 minutes past the on time pane:

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-500ms --windowDurationSeconds=10 --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-500ms --sleep 500 --limit 0 --num_players=10

100ms test, on time panes are fired ~1-2 minutes past what would be a perfect watermark. Late pane is fired ~1-2 minutes past the on time pane.

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-100ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-100ms --sleep 100 --limit 0 --num_players=10

50ms test, on time panes are fired ~1-2 minutes past what would be a perfect watermark. Late pane is fired ~1-2 minutes past the on time pane.

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-50ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-50ms --sleep 50 --limit 0 --num_players=10

100ms test, on time panes are fired ~1-2 minutes past what would be a perfect watermark. Late pane is fired ~1-2 minutes past the on time pane.

    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.CountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --subscription=projects/$PROJECT/subscriptions/test-10ms --windowDurationSeconds=10  --allowedLatenessSeconds=10 --earlyFiring" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-10ms --sleep 10 --limit 0 --num_players=10


## SuperCountPubSub

Run pipeline that consumes from many topics ta once and computes the lag between pane timestamps and their processing time.
 
    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.SuperCountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --windowDurationSeconds=60  --allowedLatenessSeconds=60 --subscriptions=projects/$PROJECT/subscriptions/test-10000ms,projects/$PROJECT/subscriptions/test-5000ms,projects/$PROJECT/subscriptions/test-1000ms,projects/$PROJECT/subscriptions/test-500ms" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-10000ms --sleep 10000 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-5000ms --sleep 5000 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-1000ms --sleep 1000 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-500ms --sleep 500 --limit 0 --num_players=10
    

Example output

    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:16:59Z","processed":"2019-05-15T14:21:59Z","value":"KV{default, 32}","lagSeconds":299}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:17:59Z","processed":"2019-05-15T14:21:59Z","value":"KV{default, 60}","lagSeconds":239}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:18:59Z","processed":"2019-05-15T14:21:59Z","value":"KV{default, 60}","lagSeconds":179}
    {"timing":"LATE","subscription":"test-1000ms","pane":"2019-05-15T14:18:59Z","processed":"2019-05-15T14:22:42Z","value":"KV{default, 0}","lagSeconds":222}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:19:59Z","processed":"2019-05-15T14:22:42Z","value":"KV{default, 59}","lagSeconds":162}
    {"timing":"LATE","subscription":"test-1000ms","pane":"2019-05-15T14:19:59Z","processed":"2019-05-15T14:24:49Z","value":"KV{default, 0}","lagSeconds":289}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:20:59Z","processed":"2019-05-15T14:24:49Z","value":"KV{default, 60}","lagSeconds":229}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:21:59Z","processed":"2019-05-15T14:24:49Z","value":"KV{default, 60}","lagSeconds":169}
    {"timing":"LATE","subscription":"test-1000ms","pane":"2019-05-15T14:21:59Z","processed":"2019-05-15T14:27:39Z","value":"KV{default, 0}","lagSeconds":339}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:22:59Z","processed":"2019-05-15T14:27:39Z","value":"KV{default, 60}","lagSeconds":279}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:23:59Z","processed":"2019-05-15T14:27:39Z","value":"KV{default, 59}","lagSeconds":219}
    {"timing":"LATE","subscription":"test-1000ms","pane":"2019-05-15T14:23:59Z","processed":"2019-05-15T14:28:04Z","value":"KV{default, 0}","lagSeconds":244}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:24:59Z","processed":"2019-05-15T14:28:04Z","value":"KV{default, 60}","lagSeconds":184}
    {"timing":"LATE","subscription":"test-1000ms","pane":"2019-05-15T14:24:59Z","processed":"2019-05-15T14:28:29Z","value":"KV{default, 0}","lagSeconds":209}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:25:59Z","processed":"2019-05-15T14:28:29Z","value":"KV{default, 60}","lagSeconds":149}
    {"timing":"LATE","subscription":"test-1000ms","pane":"2019-05-15T14:25:59Z","processed":"2019-05-15T14:29:59Z","value":"KV{default, 0}","lagSeconds":239}
    {"timing":"ON_TIME","subscription":"test-1000ms","pane":"2019-05-15T14:26:59Z","processed":"2019-05-15T14:29:59Z","value":"KV{default, 60}","lagSeconds":179}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:16:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 6}","lagSeconds":617}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:17:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":557}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:18:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":497}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:19:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":437}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:20:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":377}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:21:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":317}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:22:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":257}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:23:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":197}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:24:59Z","processed":"2019-05-15T14:27:17Z","value":"KV{default, 12}","lagSeconds":137}
    {"timing":"LATE","subscription":"test-5000ms","pane":"2019-05-15T14:24:59Z","processed":"2019-05-15T14:28:29Z","value":"KV{default, 0}","lagSeconds":209}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:25:59Z","processed":"2019-05-15T14:28:29Z","value":"KV{default, 12}","lagSeconds":149}
    {"timing":"LATE","subscription":"test-5000ms","pane":"2019-05-15T14:25:59Z","processed":"2019-05-15T14:29:59Z","value":"KV{default, 0}","lagSeconds":239}
    {"timing":"ON_TIME","subscription":"test-5000ms","pane":"2019-05-15T14:26:59Z","processed":"2019-05-15T14:29:59Z","value":"KV{default, 12}","lagSeconds":179}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:16:59Z","processed":"2019-05-15T14:21:10Z","value":"KV{default, 61}","lagSeconds":250}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:16:59Z","processed":"2019-05-15T14:21:59Z","value":"KV{default, 0}","lagSeconds":299}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:17:59Z","processed":"2019-05-15T14:21:59Z","value":"KV{default, 119}","lagSeconds":239}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:17:59Z","processed":"2019-05-15T14:22:19Z","value":"KV{default, 0}","lagSeconds":259}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:18:59Z","processed":"2019-05-15T14:22:19Z","value":"KV{default, 119}","lagSeconds":199}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:18:59Z","processed":"2019-05-15T14:24:00Z","value":"KV{default, 0}","lagSeconds":300}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:19:59Z","processed":"2019-05-15T14:24:00Z","value":"KV{default, 119}","lagSeconds":240}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:19:59Z","processed":"2019-05-15T14:25:12Z","value":"KV{default, 0}","lagSeconds":312}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:20:59Z","processed":"2019-05-15T14:25:12Z","value":"KV{default, 120}","lagSeconds":252}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:21:59Z","processed":"2019-05-15T14:25:12Z","value":"KV{default, 119}","lagSeconds":192}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:21:59Z","processed":"2019-05-15T14:26:56Z","value":"KV{default, 0}","lagSeconds":296}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:22:59Z","processed":"2019-05-15T14:26:56Z","value":"KV{default, 119}","lagSeconds":236}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:22:59Z","processed":"2019-05-15T14:27:39Z","value":"KV{default, 0}","lagSeconds":279}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:23:59Z","processed":"2019-05-15T14:27:39Z","value":"KV{default, 119}","lagSeconds":219}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:23:59Z","processed":"2019-05-15T14:29:14Z","value":"KV{default, 0}","lagSeconds":314}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:24:59Z","processed":"2019-05-15T14:29:14Z","value":"KV{default, 119}","lagSeconds":254}
    {"timing":"LATE","subscription":"test-500ms","pane":"2019-05-15T14:24:59Z","processed":"2019-05-15T14:29:35Z","value":"KV{default, 0}","lagSeconds":275}
    {"timing":"ON_TIME","subscription":"test-500ms","pane":"2019-05-15T14:25:59Z","processed":"2019-05-15T14:29:35Z","value":"KV{default, 119}","lagSeconds":215}
    
