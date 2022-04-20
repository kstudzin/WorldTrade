package edu.vanderbilt.studzikm;

import com.microsoft.z3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LogicalCountryModel {

    private Context ctx;
    private Solver solver;
    private Country country;
    private Integer time = 0;

    public LogicalCountryModel(Context context, Country country, String assertions) {
        this.ctx = context;
        this.country = country;
        this.solver = context.mkSolver();

        initialize(assertions);
    }

    /**
     * Add initial axioms to model
     */
    private void initialize(String asserts) {
        IntNum exstg_popln = ctx.mkInt(country.getResource("R1"));
        IntNum extsg_elmts = ctx.mkInt(country.getResource("R2"));
        IntNum extsg_timbr = ctx.mkInt(country.getResource("R3"));
        IntNum extsg_alloy = ctx.mkInt(country.getResource("R21"));
        IntNum extsg_elctr = ctx.mkInt(country.getResource("R22"));
        IntNum extsg_house = ctx.mkInt(country.getResource("R23"));

        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        UninterpretedSort scoreSort = ctx.mkUninterpretedSort("ScoreType");
        UninterpretedSort timeStep = ctx.mkUninterpretedSort("TimeStep");

        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);
        Expr<UninterpretedSort> elmts_resrc = ctx.mkConst("elmts_resrc", resourceSort);
        Expr<UninterpretedSort> alloy_resrc = ctx.mkConst("alloy_resrc", resourceSort);
        Expr<UninterpretedSort> timbr_resrc = ctx.mkConst("timbr_resrc", resourceSort);
        Expr<UninterpretedSort> house_waste_resrc = ctx.mkConst("house_waste_resrc", resourceSort);
        Expr<UninterpretedSort> alloy_waste_resrc = ctx.mkConst("alloy_waste_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_waste_resrc = ctx.mkConst("elctr_waste_resrc", resourceSort);

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

        Sort[] sorts = {
                resourceSort,
                scoreSort,
                timeStep
        };
        Symbol[] sortNames = Arrays.stream(sorts)
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
                                        elctr_waste_resrc
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
        Symbol[] symbols = funcDeclSupplier
                .get()
                .map(FuncDecl::getName)
                .toArray(Symbol[]::new);
        FuncDecl<?>[] decls = funcDeclSupplier.get()
                .toArray(FuncDecl[]::new);

        BoolExpr[] assertObjects = ctx.parseSMTLIB2String(asserts, sortNames, sorts, symbols, decls);
        Solver s = ctx.mkSolver();
        Arrays.stream(assertObjects)
                .forEach(s::add);

        System.out.println(s.check());
    }

    public Double score(ActionResult<?> result){
        Action action = result.getAction();
        String name = action.getName();
        Action.Type type = action.getType();

        System.out.println(solver.check());
        System.out.println(solver.getModel());

        return null;
    }

}
