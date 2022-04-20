package edu.vanderbilt.studzikm;

import com.microsoft.z3.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogicalCountryModel {

    private Context ctx;
    private Solver solver;
    private Country country;
    private Integer time = 0;

    private UninterpretedSort resourceSort;
    private UninterpretedSort scoreSort;
    private UninterpretedSort timeStep;

    private Sort[] sorts;
    private Symbol[] sortNames;
    private Symbol[] symbols;
    private FuncDecl<?>[] decls;
    private Map<String, String> nameMap = new HashMap<>();

    public LogicalCountryModel(Context context, Country country, String assertions) {
        this.ctx = context;
        this.country = country;
        this.solver = context.mkSolver();

        this.nameMap.put("R1", "popln_resrc");
        this.nameMap.put("R2", "elmts_resrc");
        this.nameMap.put("R3", "timbr_resrc");
        this.nameMap.put("R21", "alloy_resrc");
        this.nameMap.put("R22", "elctr_resrc");
        this.nameMap.put("R23", "house_resrc");

        initialize(assertions);
    }

    /**
     * Add initial axioms to model
     */
    private void initialize(String asserts) {
        resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        scoreSort = ctx.mkUninterpretedSort("ScoreType");
        timeStep = ctx.mkUninterpretedSort("TimeStep");

        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);
        Expr<UninterpretedSort> elmts_resrc = ctx.mkConst("elmts_resrc", resourceSort);
        Expr<UninterpretedSort> alloy_resrc = ctx.mkConst("alloy_resrc", resourceSort);
        Expr<UninterpretedSort> timbr_resrc = ctx.mkConst("timbr_resrc", resourceSort);
        Expr<UninterpretedSort> house_waste_resrc = ctx.mkConst("house_waste_resrc", resourceSort);
        Expr<UninterpretedSort> alloy_waste_resrc = ctx.mkConst("alloy_waste_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_waste_resrc = ctx.mkConst("elctr_waste_resrc", resourceSort);

        Expr<UninterpretedSort> null_resrc = ctx.mkConst("null_resrc", resourceSort);


        Sort[] inputInSort = {resourceSort, resourceSort};
        FuncDecl<BoolSort> input = ctx.mkFuncDecl("Input", inputInSort, ctx.getBoolSort());

        Sort[] resourceSortArr = {resourceSort};
        FuncDecl<BoolSort> action = ctx.mkFuncDecl("Action", resourceSortArr, ctx.getBoolSort());
        FuncDecl<BoolSort> goal = ctx.mkFuncDecl("Goal", resourceSortArr, ctx.getBoolSort());

        FuncDecl<IntSort> time = ctx.mkFuncDecl("Time", timeStep, ctx.getIntSort());
        FuncDecl<UninterpretedSort> invTime = ctx.mkFuncDecl("InvTime", ctx.getIntSort(), timeStep);

        Sort[] scoreInSort = {scoreSort, timeStep};
        FuncDecl<RealSort> score = ctx.mkFuncDecl("Score", scoreInSort, ctx.getRealSort());

        FuncDecl<UninterpretedSort> oneOfOne = ctx.mkFuncDecl("oneOfOne", new Sort[]{}, scoreSort);
        FuncDecl<UninterpretedSort> twoOfTwo = ctx.mkFuncDecl("twoOfTwo", new Sort[]{}, scoreSort);
        FuncDecl<UninterpretedSort> threeOfThree = ctx.mkFuncDecl("threeOfThree", new Sort[]{}, scoreSort);

        sorts = new Sort[]{
                resourceSort,
                scoreSort,
                timeStep
        };
        sortNames = Arrays.stream(sorts)
                .map(Sort::getName)
                .toArray(Symbol[]::new);

        Supplier<Stream<FuncDecl<?>>> funcDeclSupplier = () ->
                Stream.concat(
                        // Add expressions
                        Stream.of(house_resrc,
                                        elctr_resrc,
                                        popln_resrc,
                                        elmts_resrc,
                                        alloy_resrc,
                                        timbr_resrc,
                                        house_waste_resrc,
                                        alloy_waste_resrc,
                                        elctr_waste_resrc,
                                        null_resrc
                                )

                                // Get function declaration from expression
                                .map(Expr::getFuncDecl),

                        // Add function declarations
                        Stream.of(input,
                                action,
                                goal,
                                time,
                                invTime,
                                score,
                                oneOfOne,
                                twoOfTwo,
                                threeOfThree)
                );
        symbols = funcDeclSupplier
                .get()
                .map(FuncDecl::getName)
                .toArray(Symbol[]::new);
        decls = funcDeclSupplier.get()
                .toArray(FuncDecl[]::new);

        BoolExpr[] assertObjects = ctx.parseSMTLIB2String(asserts, sortNames, sorts, symbols, decls);
        Arrays.stream(assertObjects)
                .forEach(solver::add);
    }

    public Double score(ActionResult<?> result){
        Action action = result.getAction();
        String name = nameMap.get(action.getName());
        Action.Type type = action.getType();
        String timeStr = "t" + time.toString();

        Expr<UninterpretedSort> timeConst = ctx.mkConst(timeStr, timeStep);
        symbols[symbols.length] = timeConst.getFuncDecl().getName();
        decls[decls.length] = timeConst.getFuncDecl();

        if (type == Action.Type.TRANSFER) {
            TransferResult transferResult = (TransferResult) result;
            if (transferResult.getRole() == TransferResult.Role.SENDER) {
                name = "null_resrc";
            }
        }

        String goal;
        if (result.getSelf().getTargetAmount("R23") > 0) {
            goal = "house_resrc";
        } else if (result.getSelf().getTargetAmount("R22") > 0) {
            goal = "elctr_resrc";
        } else {
            goal = "null_resrc";
        }

        String[] updateTemplate = {
                "(assert (Action %s %s))",
                "(assert (Goal %s %s))",
                "(assert (= (Time %s) %d))",
                "(assert (= (InvTime %d) %s))",
        };
        String updates = String.format(Arrays.stream(updateTemplate)
                .collect(Collectors.joining("\n")),
                name, timeStr,
                goal, timeStr,
                timeStr, time,
                time, timeStr);

        BoolExpr[] updateExpr = ctx.parseSMTLIB2String(updates, sortNames, sorts, symbols, decls);

        time++;
        return null;
    }

}
