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
  Subscribe received till 256 then error handler is called due to OverFlowException.
  after subscriber stops.

boundedElastic-2| Publishing = 1
boundedElastic-2| Publishing = 2
boundedElastic-1 | Received = 1
boundedElastic-2| Publishing = 3
boundedElastic-2| Publishing = 4
boundedElastic-1 | Received = 2
boundedElastic-2| Publishing = 5
boundedElastic-2| Publishing = 6
boundedElastic-1 | Received = 3
...
boundedElastic-2| Publishing = 257
boundedElastic-1 | Received = 129
boundedElastic-2| Publishing = 258
18:35:35.180 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 258
boundedElastic-2| Publishing = 259
boundedElastic-1 | Received = 130
18:35:35.196 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 259
boundedElastic-2| Publishing = 260
18:35:35.212 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 260
...
boundedElastic-1 | Received = 254
18:35:39.061 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 507
boundedElastic-2| Publishing = 508
18:35:39.076 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 508
boundedElastic-2| Publishing = 509
boundedElastic-1 | Received = 255
18:35:39.092 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 509
boundedElastic-2| Publishing = 510
18:35:39.108 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 510
boundedElastic-2| Publishing = 511
boundedElastic-1 | Received = 256
18:35:39.124 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 511
boundedElastic-2| Publishing = 512
18:35:39.139 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 512
boundedElastic-2| Publishing = 513
18:35:39.154 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 513
boundedElastic-2| Publishing = 514
18:35:39.169 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 514
boundedElastic-2| Publishing = 515
18:35:39.185 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 515
boundedElastic-2| Publishing = 516
18:35:39.200 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 516
boundedElastic-2| Publishing = 517
18:35:39.215 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 517
boundedElastic-1 | Error OverflowException The receiver is overrun by more signals than expected (bounded queue...)
boundedElastic-2| Publishing = 518
18:35:39.231 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 518
boundedElastic-2| Publishing = 519
18:35:39.247 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 519
...
boundedElastic-2| Publishing = 997
18:35:46.742 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 997
boundedElastic-2| Publishing = 998
18:35:46.758 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 998
boundedElastic-2| Publishing = 999
18:35:46.774 [boundedElastic-2] DEBUG reactor.core.publisher.Operators - onNextDropped: 999

- Overflow IGNORE Strategy
  Backpressure strategy is ignored, Eventually error will appear, subscriber must handle error
  
boundedElastic-2| Publishing = 1
boundedElastic-1 | Received = 1
boundedElastic-2| Publishing = 2
boundedElastic-2| Publishing = 3
boundedElastic-2| Publishing = 4
boundedElastic-2| Publishing = 5
boundedElastic-2| Publishing = 6
boundedElastic-2| Publishing = 7
boundedElastic-2| Publishing = 8
boundedElastic-1 | Received = 2
boundedElastic-2| Publishing = 9
...
boundedElastic-2| Publishing = 996
boundedElastic-1 | Received = 84
boundedElastic-2| Publishing = 997
boundedElastic-2| Publishing = 998
boundedElastic-2| Publishing = 999
boundedElastic-1 | Received = 85
boundedElastic-1 | Received = 86
boundedElastic-1 | Received = 87
...
boundedElastic-1 | Received = 277
boundedElastic-1 | Received = 278
boundedElastic-1 | Received = 279
boundedElastic-1 | Error OverflowException Queue is full: Reactive Streams source doesn't respect backpressure

- Overflow BUFFER Strategy
  subscriber receives all the values using Buffer, out of memory is a possibility.

boundedElastic-2| Publishing = 1
boundedElastic-2| Publishing = 2
boundedElastic-2| Publishing = 3
boundedElastic-2| Publishing = 4
...  
boundedElastic-2| Publishing = 996
boundedElastic-2| Publishing = 997
boundedElastic-2| Publishing = 998
boundedElastic-2| Publishing = 999
boundedElastic-1 | Received = 2
boundedElastic-1 | Received = 3
boundedElastic-1 | Received = 4
boundedElastic-1 | Received = 5  
...  
boundedElastic-1 | Received = 997
boundedElastic-1 | Received = 998
boundedElastic-1 | Received = 999