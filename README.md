# Project Reactor Basic BackPressure Strategies in Action (Overflow)
Overflow Strategies DROP, LATEST, ERROR, IGNORE, BUFFER

- Overflow DROP Strategy 
  Default buffer size 256, so after 256 publishes then start to drop elements.
  SubscribeOn PublishOn different threads.

boundedElastic-2| Publishing = 1
boundedElastic-2| Publishing = 2
boundedElastic-2| Publishing = 3
...
boundedElastic-2| Publishing = 240
boundedElastic-1 | Received = 1
boundedElastic-2| Publishing = 241
...
boundedElastic-2| Publishing = 257
boundedElastic-2 | Dropped = 257
boundedElastic-2| Publishing = 258
boundedElastic-2 | Dropped = 258
boundedElastic-2| Publishing = 259
boundedElastic-2 | Dropped = 259
...
boundedElastic-2| Publishing = 998
boundedElastic-2 | Dropped = 998
boundedElastic-2| Publishing = 999
boundedElastic-2 | Dropped = 999
boundedElastic-1 | Received = 2
boundedElastic-1 | Received = 3
boundedElastic-1 | Received = 4
...
boundedElastic-1 | Received = 255
boundedElastic-1 | Received = 256    
    
- Overflow LATEST Strategy
  After Received 256 take the latests (Received = 999)
  
boundedElastic-2| Publishing = 1
boundedElastic-2| Publishing = 2
boundedElastic-2| Publishing = 3
...
boundedElastic-2| Publishing = 177
boundedElastic-1 | Received = 1
boundedElastic-2| Publishing = 178
...
boundedElastic-2| Publishing = 998
boundedElastic-2| Publishing = 999
boundedElastic-1 | Received = 2
boundedElastic-1 | Received = 3
...
boundedElastic-1 | Received = 255
boundedElastic-1 | Received = 256
boundedElastic-1 | Received = 999

- Overflow ERROR Strategy

- Overflow IGNORE Strategy

- Overflow BUFFER Strategy