To simulate a deadlock behavior, allowing the philosophers to quickly
switch between their states, you will achieve the following steps.

Assume P -> P - 1, is clockwise orientation.
Philosopher P, grabs a lock on their left chopstick.
The thread for philosopher P switches to philosopher P - 1,
and he grabs his left chopstick.

In this situation, P - 1 will be waiting for P's left chopstick.

If P - 1's thread were to switch, to P - 2, and the same continues on,
until P, then all threads will be deadlocked, as each will be waiting for their
right chopstick.
