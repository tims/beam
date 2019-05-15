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



10000ms test, on time panes are fired ~3-5 minutes past what would be a perfect watermark, Late pane is fired ~1-3 minutes past the on time pane:

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


Run pipeline that consumes from many topics ta once and computes the lag between pane timestamps and their processing time.
 
    mvn compile exec:java \
     -Dexec.mainClass="com.google.dataflow.example.SuperCountPubSub" \
     -Dexec.args="--runner=DirectRunner --project=$PROJECT --windowDurationSeconds=60  --allowedLatenessSeconds=60 --subscriptions=projects/$PROJECT/subscriptions/test-10000ms,projects/$PROJECT/subscriptions/test-5000ms,projects/$PROJECT/subscriptions/test-1000ms,projects/$PROJECT/subscriptions/test-500ms,projects/$PROJECT/subscriptions/test-100ms" \
     -Pdirect-runner
    python3 src/main/python/publish.py $PROJECT test-10000ms --sleep 10000 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-5000ms --sleep 5000 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-1000ms --sleep 1000 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-500ms --sleep 500 --limit 0 --num_players=10
    python3 src/main/python/publish.py $PROJECT test-100ms --sleep 100 --limit 0 --num_players=10
