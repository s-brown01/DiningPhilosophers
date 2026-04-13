# Dining Philosophers Simulation

## Overview

This project is an implementation of the classic computer science problem: **Dining Philosophers**.

It demonstrates how multiple threads compete for shared resources (chopsticks) and explores issues such as:
- Deadlock
- Resource contention
- Thread synchronization
- Race conditions

Each philosopher alternates between **thinking** and **eating**, 
and must acquire the chopsticks on their left and right to eat.

---

## Problem Description

There are five philosophers sitting around a circular table. 
Between each pair of philosophers is one chopstick.
This project defaults to 5 philosophers and 5 chopsticks.

To eat, a philosopher must pick up:
- Their left chopstick
- Their right chopstick

After eating, they put both chopsticks back down.

This setup can lead to concurrency problems if not managed correctly.

---

## Implementation Details

### Key Classes

- `MainTable`
    - Sets up and runs the simulation
    - Creates philosophers and chopsticks
    - Controls runtime duration

- `Philosopher`
    - Implements `Runnable`
    - Alternates between thinking and eating
    - Attempts to acquire both chopsticks before eating
    - Tracks number of successful eating turns

- `Chopstick`
    - Uses `ReentrantLock`
    - Ensures only one philosopher can hold it at a time
    - Uses `tryLock` with timeout to avoid indefinite blocking

---

## Concurrency Design

- Each philosopher runs on its own thread
- Chopsticks are shared resources protected by locks
- `tryLock` is used to prevents deadlock by preventing philosophers from blocking indefinitely while waiting for chopsticks.
- Philosophers release chopsticks immediately if they cannot acquire both

---

## How to Run

### Compile:
```bash
javac main/*.java
```
### Run:
```bash
java main.MainTable
```

---

## Configuration

You can modify simulation settings in `MainTable`:

```java
private static final int NO_OF_PHILOSOPHER = 5;
private static final int SIMULATION_MILLIS = 1000 * 10; // 10 seconds
```

---

## Sample Output
```
Philosopher-0 picked up left Chopstick-0
Philosopher-0 picked up right Chopstick-1
Philosopher-0 is eating
Philosopher-4 picked up left Chopstick-4
Philosopher-4 put down left Chopstick-4
Philosopher-2 put down right Chopstick-3
Philosopher-2 put down left Chopstick-2
Philosopher-0 put down right Chopstick-1
Philosopher-0 put down left Chopstick-0
Philosopher-0 => Number of Turns ate = 8
Philosopher-1 => Number of Turns ate = 5
Philosopher-2 => Number of Turns ate = 6
Philosopher-3 => Number of Turns ate = 8
Philosopher-4 => Number of Turns ate = 4
```

## Concepts Demonstrated
* Multithreading in Java
* Runnable interface
* ReentrantLock synchronization
* Deadlock avoidance strategies
* Shared resource management


# Challenges Faced
## Deadlock Prevention
One of the biggest challenges faced while building this project was preventing deadlock.
If each philosopher picks up one chopstick and waits for the other (hold-and-wait), the system will freeze indefinitely. 
This was addressed by using `tryLock()` with a timeout instead of blocking locks.
Philosophers will release partially acquired resources and retry to acquire them at a later time rather than becoming stuck.

## Resource Contention
Another challenge was managing resource contention between threads. 
Since each chopstick is shared between two philosophers, multiple threads often attempt to acquire the same locks at the same time. 
This required careful synchronization using `ReentrantLock` to ensure mutual exclusivity: chopsticks can only be held by one philosopher at a time.
The system can still make progress even while under high contention.

## Starvation
Although deadlock was avoided, starvation remains a possible edge case. 
Because there is no strict fairness or scheduling policy in place, some philosophers may occasionally fail to acquire both chopsticks repeatedly, especially under heavy contention. 
This is an accepted trade-off in favor of simplicity and demonstrating basic concurrency control rather than implementing a more complex fairness mechanism.

This design also respects the No Preemption condition, since chopsticks cannot be forcibly taken. 
Once acquired, a chopstick remains under the control of the owning philosopher until it is voluntarily released.
However, this simplicity comes at the cost of fairness guarantees.
The system does not enforce ordering or prioritization when multiple philosophers compete for a resource.
Thus, although deadlock is fully eliminated, starvation is still possible.

## Overall 
More advanced solutions could improve fairness, such as including a resource controller or order resource acquisition.
However, the current solution balances correctness and simplicity.
The current implementation keeps the design straightforward while still effectively demonstrating key concurrency concepts such as thread synchronization, shared resource management, and deadlock avoidance.

# Author
Sean-Paul Brown and Andrias Zelele

## References

- OldCurmudgeon (StackExchange), Dining Philosophers solution  
  https://codereview.stackexchange.com/a/26007

- Andrias Zelele, GitHub implementation reference  
  https://github.com/andriastheI/Dining-Philosophers/tree/main

