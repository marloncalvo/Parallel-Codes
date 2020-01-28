By utilizing a a global lock, we create a queue of to-eat philosophers;
so that each philosopher eventually has a chance to eat.

In this version, I setup a global lock, so that you must acquire that lock
before you acquire the left and right chopstick. After you acquire both
chopsticks, you unlock the global so that others can grab it.

When a philosopher P acquires a lock, it is important to note that no
other philosopher can grab a chopstick, since they must have had the lock
to grab either chopstick. Furthermore, if P cannot grab a chopstick now, since
no other chopsticks will be locked, and whichever chopsticks are locked currently
will eventually have to be unlocked (they must think), then it is the case
that they will eventually be able to acquire both chopsticks.

Furthermore, this applies to any philosopher P. When P attempts to grab the lock,
they will be K'th in line. During this time, any philosopher in the queue will not be
obstructing the ability for another philosopher to grab the lock. Since any philosopher
with the current lock will eventually finish, then it is the case that philosopher P
will also grab both chopsticks: thus satisfying starve-free.
