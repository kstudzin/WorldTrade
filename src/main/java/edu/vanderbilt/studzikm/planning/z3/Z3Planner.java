package edu.vanderbilt.studzikm.planning.z3;

import com.microsoft.z3.*;
import edu.vanderbilt.studzikm.Action;
import edu.vanderbilt.studzikm.ActionResult;
import edu.vanderbilt.studzikm.Country;
import edu.vanderbilt.studzikm.TransferResult;
import edu.vanderbilt.studzikm.planning.Planner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Z3Planner implements Planner {

    private Logger log = LogManager.getLogger(Z3Planner.class);

    private Context ctx;
    private Solver solver;
    private Country country;
    private Integer time = 0;

    private UninterpretedSort resourceSort;
    private UninterpretedSort scoreSort;

    private Sort[] sorts;
    private Symbol[] sortNames;
    private Symbol[] symbols;
    private FuncDecl<?>[] decls;
    private Map<String, String> nameMap = new HashMap<>();

    public final Expr<UninterpretedSort> oneOfOne;
    public final Expr<UninterpretedSort> twoOfTwo;
    public final Expr<UninterpretedSort> threeOfThree;

    private FuncDecl<RealSort> score;
    private FuncDecl<BoolSort> input;
    private FuncDecl<BoolSort> action;
    private FuncDecl<BoolSort> goal;

    public Z3Planner(Context context, Country country, String assertions) {
        this.ctx = context;
        this.country = country;
        this.solver = context.mkSolver();

        this.nameMap.put("R1", "popln_resrc");
        this.nameMap.put("R2", "elmts_resrc");
        this.nameMap.put("R3", "timbr_resrc");
        this.nameMap.put("R21", "alloy_resrc");
        this.nameMap.put("R22", "elctr_resrc");
        this.nameMap.put("R23", "house_resrc");

        resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        scoreSort = ctx.mkUninterpretedSort("ScoreType");

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
        input = ctx.mkFuncDecl("Input", inputInSort, ctx.getBoolSort());

        Sort[] resourceSortArr = {resourceSort, ctx.mkIntSort()};
        action = ctx.mkFuncDecl("Action", resourceSortArr, ctx.getBoolSort());
        goal = ctx.mkFuncDecl("Goal", resourceSortArr, ctx.getBoolSort());

        Sort[] scoreInSort = {scoreSort, ctx.mkIntSort()};
        score = ctx.mkFuncDecl("Score", scoreInSort, ctx.getRealSort());

        oneOfOne = ctx.mkConst("oneOfOne", scoreSort);
        twoOfTwo = ctx.mkConst("twoOfTwo", scoreSort);
        threeOfThree = ctx.mkConst("threeOfThree", scoreSort);

        sorts = new Sort[]{
                resourceSort,
                scoreSort,
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
                                        null_resrc,
                                        oneOfOne,
                                        twoOfTwo,
                                        threeOfThree
                                )

                                // Get function declaration from expression
                                .map(Expr::getFuncDecl),

                        // Add function declarations
                        Stream.of(input,
                                action,
                                goal,
                                score)
                );
        symbols = funcDeclSupplier
                .get()
                .map(FuncDecl::getName)
                .toArray(Symbol[]::new);
        decls = funcDeclSupplier.get()
                .toArray(FuncDecl[]::new);

        System.out.println(assertions);
        //BoolExpr[] assertObjects = ctx.parseSMTLIB2String(assertions, sortNames, sorts, symbols, decls);

        //Arrays.stream(assertObjects)
        //        .forEach(solver::add);

        solver.add(mkInput(input, house_resrc, popln_resrc));
        solver.add(mkInput(input, house_resrc, elmts_resrc));
        solver.add(mkInput(input, house_resrc, alloy_resrc));
        solver.add(mkInput(input, house_resrc, timbr_resrc));
        solver.add(mkInput(input, elctr_resrc, popln_resrc));
        solver.add(mkInput(input, elctr_resrc, elmts_resrc));
        solver.add(mkInput(input, elctr_resrc, alloy_resrc));
        solver.add(mkInput(input, alloy_resrc, popln_resrc));
        solver.add(mkInput(input, alloy_resrc, elmts_resrc));

        Expr<UninterpretedSort> x = ctx.mkConst("x", resourceSort);
        Expr<UninterpretedSort> y = ctx.mkConst("y", resourceSort);
        IntExpr t = ctx.mkIntConst("t");
        Quantifier score11 = ctx.mkExists(new Expr[]{x, y, t},
                ctx.mkAnd(
                        ctx.mkApp(action, x, t),
                        ctx.mkApp(goal, y, t),
                        ctx.mkApp(input, y, x),
                        ctx.mkEq(
                                ctx.mkApp(score, oneOfOne, t),
                                ctx.mkReal(17, 20))),
                1, null, null, null, null);
        solver.add(score11);

        ArithExpr<IntSort> tMinus1 = ctx.mkSub(t, ctx.mkInt(1));
        Quantifier score12 = ctx.mkExists(new Expr[]{x, y, t},
                ctx.mkAnd(
                        ctx.mkApp(action, x, tMinus1),
                        ctx.mkApp(goal, y, tMinus1),
                        ctx.mkApp(input, y, x),
                        ctx.mkEq(
                                ctx.mkApp(score, oneOfOne, t),
                                ctx.mkReal(17, 20)),
                        ctx.mkEq(
                                ctx.mkApp(score, twoOfTwo, t),
                                ctx.mkReal(18, 20))),
                1, null, null, null, null);
    }

    private Expr<BoolSort> mkInput(FuncDecl<BoolSort> input,
                                   Expr<UninterpretedSort> output_resrc,
                                   Expr<UninterpretedSort> input_resrc) {
        return ctx.mkEq(ctx.mkApp(input, output_resrc, input_resrc), ctx.mkTrue());
    }

    public Double score(ActionResult<?> result){
        Action action = result.getAction();
        String name = action.getName();
        Action.Type type = action.getType();

        IntExpr timeConst = ctx.mkInt(time);
        symbols[symbols.length - 1] = timeConst.getFuncDecl().getName();
        decls[decls.length - 1] = timeConst.getFuncDecl();

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
                "(assert (Action %s %d))",
                "(assert (Goal %s %d))"
        };
        String updates = String.format(Arrays.stream(updateTemplate)
                .collect(Collectors.joining("\n")),
                name, time,
                goal, time);

        log.info(updates);

        BoolExpr[] updateExpr = ctx.parseSMTLIB2String(updates, sortNames, sorts, symbols, decls);
        Arrays.stream(updateExpr)
                .forEach(solver::add);

        time++;

        if (solver.check() != Status.SATISFIABLE) {
            throw new RuntimeException("Model not satisfiable");
        }
        Expr<RealSort> finalScore = solver.getModel().evaluate(ctx.mkApp(score, oneOfOne, timeConst), true);

        System.out.println( solver.getModel());
        return parse(finalScore.toString());
    }

    double parse(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }
}
