package edu.vanderbilt.studzikm;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class WorldTradeParser {

    public static final int DEFAULT_DEPTH = 25;
    public static final double DEFAULT_GAMMA = .9;
    public static final double DEFAULT_FAILURE_PENALTY = -0.5;
    public static final double DEFAULT_LOGISTIC_GROWTH_RATE = 1.0;
    public static final double DEFAULT_SIGMOID_MIDPOINT = 0.0;
    public static final double DEFAULT_INITIAL_PROPORTION = 0.025;
    public static final double DEFAULT_PROPORTION_STEP = 0.025;
    public static final int DEFAULT_NUMBER_OF_SCHEDULES = 1;

    private Logger log = LogManager.getLogger(WorldTradeParser.class);

    private String countryFileName;
    private String resourceFileName;
    private String outputFileName;
    private Integer depth;
    private Double gamma;
    private Double failurePenalty;
    private Double logisticGrowthRate;
    private Double sigmoidMidpoint;
    private String frontierType;
    private Double initialProportion;
    private Double proportionStep;
    private Integer numberOfSchedules;

    public static WorldTradeParser parse(String[] args) {
        WorldTradeParser wtp = new WorldTradeParser();
        wtp.countryFileName = args[0];
        wtp.resourceFileName = args[1];

        for (int i = 2; i < args.length; i++) {
            String option = args[i];
            System.out.println("Found option: " + option);

            if (option.equals("--output") || option.equals("-o")) {
                wtp.outputFileName = args[++i];
            } else if (option.equals("--depth") || option.equals("-d")) {
                wtp.depth = Integer.valueOf(args[++i]);
            } else if (option.equals("--gamma") || option.equals("-g")) {
                wtp.gamma = Double.valueOf(args[++i]);
            } else if (option.equals("--failure-penalty") || option.equals("-f")) {
                wtp.failurePenalty = Double.valueOf(args[++i]);
            } else if (option.equals("--logistic-growth-rate") ||
                    option.equals("-l")) {
                wtp.logisticGrowthRate = Double.valueOf(args[++i]);
            } else if (option.equals("--sigmoid-midpoint") ||
                    option.equals("-s")) {
                wtp.sigmoidMidpoint = Double.valueOf(args[++i ]);
            } else if (option.equals("--frontier-type") || option.equals("-t")) {
                wtp.frontierType = args[++i];
            } else if (option.equals("--proportion") || option.equals("-p")) {
                wtp.initialProportion = Double.valueOf(args[++i]);
            } else if (option.equals("--proportion-step") || option.equals("-ps")) {
                wtp.proportionStep = Double.valueOf(args[++i]);
            } else if (option.equals("--schedules") || option.equals("-c")) {
                wtp.numberOfSchedules = Integer.valueOf(args[++i]);
            }
        }

        return wtp;
    }

    public File getCountryFile() {
        return new File(countryFileName);
    }

    public File getResourcesFile() {
        return new File(resourceFileName);
    }

    public OutputStream getOutputFile() throws FileNotFoundException {
        if (outputFileName != null) {
            return new FileOutputStream(outputFileName);
        }
        return System.out;
    }

    public int getDepth() {
        Preconditions.checkArgument(depth == null || depth > 0,
                "Search depth must be greater than 0");

        if (depth == null) {
            return DEFAULT_DEPTH;
        } else {
            if (depth >= 200) {
                log.warn(String.format("Search depth of %i may lead to long search tim", depth));
            }
            return depth;
        }
    }

    public double getGamma() {
        Preconditions.checkArgument(gamma == null ||
                        gamma >= 0 && gamma <= 1,
                "Extended schedule discount gamma must be between 0 and 1, inclusive");
        if (gamma == null) {
            return DEFAULT_GAMMA;
        }
        return gamma;
    }

    public double getFailurePenalty() {
        Preconditions.checkArgument(failurePenalty == null ||
                        failurePenalty <= 0,
                "Failure penalty must be between 0 and 1.");
        if (failurePenalty == null) {
            return DEFAULT_FAILURE_PENALTY;
        }
        return failurePenalty;
    }

    public double getLogisticGrowthRate() {
        Preconditions.checkArgument(logisticGrowthRate == null ||
                        logisticGrowthRate > 0,
                "logistic growth rate, probability country will agree " +
                        "to transfer, must be greater than 0");
        if (logisticGrowthRate == null ) {
            return DEFAULT_LOGISTIC_GROWTH_RATE;
        }
        return logisticGrowthRate;
    }

    public Double getSigmoidMidpoint() {
        if (sigmoidMidpoint == null) {
            return DEFAULT_SIGMOID_MIDPOINT;
        }
        return sigmoidMidpoint;
    }

    public Supplier<Frontier> getFrontierSupplier() {
        Preconditions.checkArgument(frontierType == null ||
                        frontierType.equals("bfs") ||
                        frontierType.equals("hdfs"),
                "Valid frontier types are bfs and hdfs");
        if (frontierType == null || frontierType.equals("hdfs")) {
            return HeuristicDepthFirstFrontier::new;
        }
        return () -> new BreadthFirstFrontier(100);
    }

    public Double getInitialProportion() {
        Preconditions.checkArgument(initialProportion == null ||
                initialProportion >= 0 && initialProportion <= 1);
        if (initialProportion == null) {
            return DEFAULT_INITIAL_PROPORTION;
        }
        return initialProportion;
    }

    public Double getProportionStep() {
        Preconditions.checkArgument(proportionStep == null ||
                proportionStep > 0 && proportionStep <= 1);
        if (proportionStep == null) {
            return DEFAULT_PROPORTION_STEP;
        }
        return proportionStep;
    }

    public Integer getNumberOfSchedules() {
        Preconditions.checkArgument(numberOfSchedules == null ||
                numberOfSchedules > 0);
        if (numberOfSchedules == null) {
            return DEFAULT_NUMBER_OF_SCHEDULES;
        }
        return numberOfSchedules;
    }
}
