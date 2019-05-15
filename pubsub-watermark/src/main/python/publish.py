import hashlib
import json
import random
import time

from google.cloud import pubsub_v1


def generate_players(num_players: int):
    players = set()
    i = 0
    while len(players) < num_players:
        player = hashlib.md5(str(i).encode("utf-8")).hexdigest()[:7]
        players.add(player)
        i += 1
    return players


def publish_loop(project_id: str, topic_name: str, num_players: int, limit: int,
        sleep: int):
    publisher = pubsub_v1.PublisherClient()
    topic_path = publisher.topic_path(project_id, topic_name)
    rand = random.Random()
    players = list(generate_players(num_players))
    i = 0
    while limit <= 0 or i < limit:
        i += 1
        score = rand.randint(1, 10)
        player = rand.choice(players)
        message = json.dumps({
            "player": player,
            "score": score,
        })
        print("%s" % message)
        if sleep > 0:
            time.sleep(sleep / 1000.0)
        publisher.publish(topic_path, data=message.encode("utf-8"))


# #
# # players = ["eufy"]
#
# def main(args):

#   publisher = pubsub_v1.PublisherClient()
#   topic_path = publisher.topic_path(project_id, topic_name)
#
#   rand = random.Random()
#   n = int(args[1])
#   for i in range(n):
#     score = rand.randint(1, 10)
#     player = rand.choice(players)
#     message = json.dumps({
#       "player": player,
#       "score": score,
#     })
#     print("'%s'" % message)
#     future = publisher.publish(topic_path, data=message.encode("utf-8"))
#


if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument("project",
                        help="project to publish to",
                        type=str)
    parser.add_argument("topic",
                        help="topic to publish to",
                        type=str)
    parser.add_argument("--num_players",
                        help="project to publish to",
                        required=False,
                        default=1,
                        type=int)
    parser.add_argument("--limit",
                        help="limit how many messages to publish",
                        required=False,
                        default=0,
                        type=int)
    parser.add_argument("--sleep",
                        help="milliseconds to sleep between each message",
                        required=False,
                        default=1000,
                        type=int)
    args = parser.parse_args()
    publish_loop(args.project, args.topic, args.num_players, args.limit,
                 args.sleep)
