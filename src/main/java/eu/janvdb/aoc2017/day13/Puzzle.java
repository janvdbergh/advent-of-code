package eu.janvdb.aoc2017.day13;

import eu.janvdb.util.Holder;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;

public class Puzzle {

    private static final Map<Integer, Integer> DEPTHS0 = HashMap.of(
            0, 3,
            1, 2,
            4, 4,
            6, 4
    );

    private static final Map<Integer, Integer> DEPTHS1 = HashMap.ofEntries(
            Tuple.of(1, 2),
            Tuple.of(0, 3),
            Tuple.of(2, 4),
            Tuple.of(4, 4),
            Tuple.of(6, 5),
            Tuple.of(8, 6),
            Tuple.of(10, 8),
            Tuple.of(12, 8),
            Tuple.of(14, 6),
            Tuple.of(16, 6),
            Tuple.of(18, 8),
            Tuple.of(20, 8),
            Tuple.of(22, 6),
            Tuple.of(24, 12),
            Tuple.of(26, 9),
            Tuple.of(28, 12),
            Tuple.of(30, 8),
            Tuple.of(32, 14),
            Tuple.of(34, 12),
            Tuple.of(36, 8),
            Tuple.of(38, 14),
            Tuple.of(40, 12),
            Tuple.of(42, 12),
            Tuple.of(44, 12),
            Tuple.of(46, 14),
            Tuple.of(48, 12),
            Tuple.of(50, 14),
            Tuple.of(52, 12),
            Tuple.of(54, 10),
            Tuple.of(56, 14),
            Tuple.of(58, 12),
            Tuple.of(60, 14),
            Tuple.of(62, 14),
            Tuple.of(66, 10),
            Tuple.of(68, 14),
            Tuple.of(74, 14),
            Tuple.of(76, 12),
            Tuple.of(78, 14),
            Tuple.of(80, 20),
            Tuple.of(86, 18),
            Tuple.of(92, 14),
            Tuple.of(94, 20),
            Tuple.of(96, 18),
            Tuple.of(98, 17)
    );

    private static final Map<Integer, Integer> DEPTHS = DEPTHS1;
    private static final int NUMBER_OF_LAYERS = DEPTHS.keySet().max().getOrElse(0) + 1;

    public static void main(String[] args) {
        part1();
        part2(); // >33600
    }

    private static void part1() {
        System.out.println(calculateSeverity(getInitialState()));
    }

    private static void part2() {
        Vector<ScannerState> scannerStates = getInitialState();

        while(wasCaught(scannerStates)) {
            scannerStates = scannerStates.removeAt(0).append(scannerStates.last().nextState());
        }

        System.out.println(scannerStates.get(0).getDelay());
    }

    private static Vector<ScannerState> getInitialState() {
        ScannerState initialState = new ScannerState(DEPTHS);
        Holder<ScannerState> stateHolder = new Holder<>(initialState);
        return Stream.range(0, NUMBER_OF_LAYERS)
                .map(layer -> {
                    ScannerState value = stateHolder.getValue();
                    stateHolder.setValue(value.nextState());
                    return value;
                })
                .toVector();
    }

    private static int calculateSeverity(Vector<ScannerState> states) {
        return states.toStream()
                .zip(Stream.from(0))
                .map(tuple -> getSeverity(tuple._1, tuple._2))
                .sum()
                .intValue();
    }

    private static int getSeverity(ScannerState scannerState, Integer layer) {
        if (wasCaught(scannerState, layer)) {
            return layer * scannerState.getDepth(layer);
        }
        else return 0;
    }

    private static boolean wasCaught(Vector<ScannerState> states) {
        return states.toStream()
                .zip(Stream.from(0))
                .map(tuple -> wasCaught(tuple._1, tuple._2))
                .contains(true);
    }

    private static boolean wasCaught(ScannerState scannerState, Integer layer) {
        return scannerState.getPosition(layer) == 0;
    }

}
