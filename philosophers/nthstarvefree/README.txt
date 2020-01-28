This uses the same idea from StarveFree, but implements the solution as
a chopstick per N philosophers, which is the required input argument.

The algorithm remains functionality for arbitrary input N > 0, as any 
philosopher will have a lock at one time, which makes them the only person to
grab chopsticks. In this way, we will be queueing more and more philosophers
as N grows, but should remain relatively efficient. As long as the philosophers
do not take too long eating, the queue should move fairly quicky; giving
everybody ample time to eat and think.
