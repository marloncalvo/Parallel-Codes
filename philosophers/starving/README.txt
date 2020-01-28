To get around the fact that greedily grabbing your left chopstick
leads to a deadlock situtation; the philosophers now will drop their
right chopstick in the case that they cannot grab their right chopstick
after they aqcuire their left chopstick.

This avoids having one chopstick, while waiting for another, which
prevents ever deadlocking, as since if the same situation were to occur
with the deadlock program, after the last philosopher grabs a lock on their
left chopstick, whichever philosopher comes next, must drop their left chopstick
as they cannot acquire their right chopstick; prevent deadlock, and even giving
the possibility of another philosopher to eat.

By always dropping your right chopstick, you introduce the possibility of a 
philosopher being starved. If you are philosopher P, between P - 1, and P + 1;
it can be the case such that P - 1 and P + 1 cycle their turns so that you grab
your left chopstick, but have to drop it, since the right chopstick can be taken.
This occurs when P - 1 either has their left chopstick locked, or not locked, but
P + 1 has their chopstick locked. If P then grabs the left chopstick, P would drop
their left chopstick. P + 1 can start thinking, dropping both, but P - 1 can start
eating right after, if P - 1 was called before P and executed both locks, then P would
be disallowed from locking. When P acquires the lock for the left chopstick, 
it can be that P + 1 acquires the lock on P + 1's left chopstick, causing P to drop his.
