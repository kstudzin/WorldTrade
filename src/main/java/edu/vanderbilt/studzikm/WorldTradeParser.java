package edu.vanderbilt.studzikm;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import edu.vanderbilt.studzikm.planning.Planner;
import edu.vanderbilt.studzikm.planning.prophc.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class WorldTradeParser {

    public static final int DEFAULT_DEPTH = 25;
    public static final double DEFAULT_GAMMA = .9;
    public static final double DEFAULT_FAILURE_PENALTY = -0.5;
    public static final double DEFAULT_LOGISTIC_GROWTH_RATE = 1.0;
    public static final double DEFAULT_SIGMOID_MIDPOINT = 0.0;
    public static final double DEFAULT_INITIAL_PROPORTION = 0.025;
    public static final double DEFAULT_PROPORTION_STEP = 0.025;
    public static final int DEFAULT_NUMBER_OF_SCHEDULES = 1;

    public static final Task<Country> houseTask =
            new Task<>(country -> country.getTargetAmount("R23") <= 0);
    public static final Task<Country> electronicsTask =
            new Task<>(country -> country.getTargetAmount("R22") <= 0);

    private static final Predicate<ActionResult> onlyRecieverTransfers =
            actionResult ->  (actionResult.getType() != Action.Type.TRANSFER ||
                    ((TransferResult) actionResult).getRole() == TransferResult.Role.RECIEVER);
    public static final SubTask<ActionResult<?>> elementAction =
            new SubTask<>(actionResult -> onlyRecieverTransfers.test(actionResult)
                    && actionResult.getName().equals("R2"));
    public static final SubTask<ActionResult<?>> timberAction =
            new SubTask<>(actionResult -> onlyRecieverTransfers.test(actionResult)
                    && actionResult.getName().equals("R3"));
    public static final SubTask<ActionResult<?>> alloyAction =
            new SubTask<>(actionResult -> onlyRecieverTransfers.test(actionResult)
                    && actionResult.getName().equals("R21"));
    public static final SubTask<ActionResult<?>> electronicsAction =
            new SubTask<>(actionResult -> onlyRecieverTransfers.test(actionResult)
                    && actionResult.getName().equals("R22"));
    public static final SubTask<ActionResult<?>> houseAction =
            new SubTask<>(actionResult -> onlyRecieverTransfers.test(actionResult)
                    && actionResult.getName().equals("R23"));

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
    private String plannerType;

    private List<SubTask<ActionResult<?>>> housingSubTasks = Arrays.asList(new SubTask[]{
            elementAction,
            timberAction,
            alloyAction,
            houseAction
    });

    private List<SubTask<ActionResult<?>>> electronicsSubTasks = Arrays.asList(new SubTask[]{
            elementAction,
            alloyAction,
            electronicsAction
    });

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
            } else if (option.equals("--planner") || option.equals("-a")) {
                wtp.plannerType = args[++i];
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

    public Supplier<Planner> getPlannerSupplier() {
        // TODO Make scoring strategy an option
        ScoringStrategy scoringStrategy = new FunctionScoringStrategy();
        Supplier<Planner> plannerSupplier;
        if (plannerType == null || plannerType.equals("prophc")) {
            plannerSupplier = () -> {
                // TODO Make history length configurable
                PartialOrderPlanner pop = new PartialOrderPlanner(5, scoringStrategy);
                pop.register(houseTask, housingSubTasks);
                pop.register(electronicsTask, electronicsSubTasks);
                return new ProphcPlanner(pop);
            };
        } else if (plannerType.equals("rdf")) {
            plannerSupplier = null;
        } else {
            throw new RuntimeException(
                    String.format("Planner type %s not valid", plannerType));
        }

        return plannerSupplier;
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
