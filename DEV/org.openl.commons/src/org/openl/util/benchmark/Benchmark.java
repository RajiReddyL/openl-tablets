package org.openl.util.benchmark;

import org.openl.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Benchmark {

    HashMap<String, BenchmarkInfo> _measurements = null;

    public List<BenchmarkInfo> measureAllInList(BenchmarkUnit[] units, int ms) throws Exception {
        _measurements = new HashMap<String, BenchmarkInfo>();
        List<BenchmarkInfo> list = new ArrayList<BenchmarkInfo>();
        for (BenchmarkUnit bu: units) {
            Log.info("Benchmarking Unit " + bu.getName());
            BenchmarkInfo bi = runUnit(bu, ms);
            list.add(bi);
        }

        return list;
    }

    public BenchmarkInfo runUnit(BenchmarkUnit bu, int ms) throws Exception {

        if (_measurements == null) {
            _measurements = new HashMap<String, BenchmarkInfo>();
        }

        BenchmarkInfo bi = _measurements.get(bu.getName());

        if (bi == null) {
            bi = new BenchmarkInfo(null, bu, bu.getName());

            bi.firstRunms = bu.millisecondsToRun();
            _measurements.put(bu.getName(), bi);
        }

        if (once) {
            return bi;
        }

        int minMillis = ms == -1 ? bu.getMinms() : ms;
        long runs = bu.getMinRuns();
        while (true) {
            long time = bu.millisecondsToRun(runs);
            if (time > minMillis || runs >= Integer.MAX_VALUE) {
                bi.collect(runs, time);
                return bi;
            }

            // Calculate a growth rate for runs
            // division by zero is Double.POSITIVE_INFINITY
            double mult = Math.min(200.0, 1.1 * minMillis / time);
            // Calculate new quantity of runs
            runs = Math.max(runs + 1, (long) (runs * mult));
            // To avoid overflowing of Integer bits
            runs = Math.min(runs, Integer.MAX_VALUE);
        }
    }
}
