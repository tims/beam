# Generate PubSub data

## Usage example

Publish messages continuously, with 1 second gaps, publish messages for just one player  

```
python3 publish.py $PROJECT $TOPIC --sleep 1000 --limit 0 --num_players=10
```

Publish 100 messages, with 50 millisecond gaps, select from 10 players randomly  
```
python3 publish.py $PROJECT $TOPIC --sleep=50 --limit 10 --num_players=10
```
sample messages:
```
{"player": "e4da3b7", "score": 8}
{"player": "c81e728", "score": 2}
{"player": "e4da3b7", "score": 9}
{"player": "45c48cc", "score": 4}
{"player": "a87ff67", "score": 3}
{"player": "e4da3b7", "score": 10}
{"player": "eccbc87", "score": 6}
{"player": "c4ca423", "score": 10}
{"player": "8f14e45", "score": 6}
{"player": "e4da3b7", "score": 1}
```