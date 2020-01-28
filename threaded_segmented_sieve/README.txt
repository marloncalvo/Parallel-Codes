To compile and run the Primes generator, use JDK / JRE 8 from Oracle.
compile > javac Primes.java
run     > java Primes

I chose to modify the segmented sieve algorithm to provide a good
balance between each thread, and a performant algorithm.

Since this algorithm is basically a Sieve of Eratosthenes,
one knows that most of the time is spending iterating and flipping
the bits for the rest of p*k <= primes. where p is a prime, and k is the k'th factor of p.

One can chunck this iteration step, into multiple N/8 blocks, where N is the
number of threads. Then, each thread would simply iterate through each k'th factor
for each p, until all p's have been covered.

Except the generator thread, which will be explained later, the "worker" or
iterator threads all share the same work, and are allocate very similar workloads.
The "generator" thread however, has two jobs.

It generates all the p's that the other threads will iterate on, and iterates
through the first [0 ... N/8] block. Since the worker threads rely on this thread,
I chose to provide a few starting prime numbers, such that the worker threads can begin
iterating while the generator thread starts and finishes generating all base primes.

Notice, that because of the Sieve I chose, the generator only needs to generate the 
first sqrt(PN), where PN is number to find all the primes up to. The sieve will be
done for O(sqrt(PN) log(sqrt(PN))), which is significantly lower than (N/8)/p iterations,
where p is the current prime being iterated by a thread.
This means that the generator thread will be able to always calculate the required
base primes, and the worker threads will not be waiting; furthermore, it will
finish at nearly the same time as the workers threads, as for each base prime,
it needs sqrt(N) less elements to iterate on.

Each thread will then conclude each own's results, which will be combined by the last living thread,
and create the output.

A further optimization could have been to chunk the iteration of the threads, so that we maintain
locality, and improve our cache hits. Also, the use of a busy-wait / shared list, may introduce
some performance loss instead of a messaging paradigm. 
