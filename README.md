# Finagle-Censorinus

[ ![Codeship Status for crispywalrus/finagle-censorinus](https://codeship.com/projects/506cbb10-17c5-0134-523d-7a446e54894e/status?branch=master)](https://codeship.com/projects/158594)
Use [cencorinus]

# finagle stats into stats

This project forwards metrics to either statsd or dogstatsd
using [cencorinus](https://github.com/gphat/censorinus). It has some
simple knobs settable via command line flags

+ com.crispywalrus.finagle.stats.hostname sets the (dog)statsd host
+ com.crispywalrus.finagle.stats.port the (dog)statsd port
+ com.crispywalrus.finagle.stats.implementation chooses between dog/statsd


