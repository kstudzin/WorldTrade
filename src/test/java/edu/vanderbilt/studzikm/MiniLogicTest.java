package edu.vanderbilt.studzikm;

import com.microsoft.z3.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class MiniLogicTest {

    @Test
    void testUnsat() {
        Context ctx = new Context();
        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);

        Sort[] inputInSort = {resourceSort, resourceSort};
        FuncDecl<BoolSort> input = ctx.mkFuncDecl("Input", inputInSort, ctx.getBoolSort());

        Solver s = ctx.mkSolver();
        s.add(ctx.mkApp(input, house_resrc, popln_resrc));
        s.add(ctx.mkNot(ctx.mkApp(input, house_resrc, popln_resrc)));

        Status status = s.check();
        assertEquals(Status.UNSATISFIABLE, status);
    }

    @Test
    void testImplicationWithQuantifier() {
        Context ctx = new Context();
        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);

        Sort[] inputInSort = {resourceSort, resourceSort};
        FuncDecl<BoolSort> input = ctx.mkFuncDecl("Input", inputInSort, ctx.getBoolSort());


        Expr<UninterpretedSort> x = ctx.mkConst("x", resourceSort);
        Expr<UninterpretedSort> y = ctx.mkConst("y", resourceSort);
        Expr<UninterpretedSort> z = ctx.mkConst("z", resourceSort);
        Sort[] sorts = {resourceSort, resourceSort, resourceSort};
        Symbol[] names = {ctx.mkSymbol("x"), ctx.mkSymbol("y"), ctx.mkSymbol("z")};
        BoolExpr body = ctx.mkImplies(ctx.mkAnd(ctx.mkApp(input, x, y), ctx.mkApp(input, y, z)), ctx.mkApp(input, x, z));
        Quantifier forall = ctx.mkForall(sorts, names, body, 1, null, null, null, null);

        Solver s = ctx.mkSolver();

        s.add(forall);
        s.add(ctx.mkDistinct(house_resrc, elctr_resrc, popln_resrc));
        s.add(ctx.mkDistinct(x, y, z));

        s.add(ctx.mkApp(input, house_resrc, elctr_resrc));
        s.add(ctx.mkApp(input, elctr_resrc, popln_resrc));

        Status status = s.check();
        assertEquals(Status.SATISFIABLE, status);

        Model model = s.getModel();
        System.out.println(model);
        System.out.println(s);
        assertEquals(ctx.mkTrue(), model.evaluate(ctx.mkApp(input, house_resrc, popln_resrc), true));
    }

    @Test
    void testUnsatWithImplication() {
        Context ctx = new Context();
        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);

        Sort[] inputInSort = {resourceSort, resourceSort};
        FuncDecl<BoolSort> input = ctx.mkFuncDecl("Input", inputInSort, ctx.getBoolSort());

        Solver s = ctx.mkSolver();
//        s.add(ctx.mkApp(input, house_resrc, popln_resrc));
        s.add(ctx.mkNot(ctx.mkApp(input, house_resrc, popln_resrc)));

        Expr<UninterpretedSort> x = ctx.mkConst("x", resourceSort);
        Expr<UninterpretedSort> y = ctx.mkConst("y", resourceSort);
        Expr<UninterpretedSort> z = ctx.mkConst("z", resourceSort);
        Sort[] sorts = {resourceSort, resourceSort, resourceSort};
        Symbol[] names = {ctx.mkSymbol("x"), ctx.mkSymbol("y"), ctx.mkSymbol("z")};
        BoolExpr body = ctx.mkImplies(ctx.mkAnd(ctx.mkApp(input, x, y), ctx.mkApp(input, y, z)), ctx.mkApp(input, x, z));
        Quantifier forall = ctx.mkForall(sorts, names, body, 1, null, null, null, null);
        System.out.println(forall);

        s.add(forall);
        s.add(ctx.mkDistinct(house_resrc, elctr_resrc, popln_resrc));
        s.add(ctx.mkDistinct(x, y, z));

        s.add(ctx.mkApp(input, house_resrc, elctr_resrc));
        s.add(ctx.mkApp(input, elctr_resrc, popln_resrc));


        Status status = s.check();
        assertEquals(Status.UNSATISFIABLE, status);
    }


    @Test
    void testStringAssertions() {
        Context ctx = new Context();
        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);

        Sort[] inputInSort = {resourceSort, resourceSort};
        FuncDecl<BoolSort> input = ctx.mkFuncDecl("Input", inputInSort, ctx.getBoolSort());

        Expr<UninterpretedSort> x = ctx.mkConst("x", resourceSort);
        Expr<UninterpretedSort> y = ctx.mkConst("y", resourceSort);
        Expr<UninterpretedSort> z = ctx.mkConst("z", resourceSort);

        String asserts =
                "(assert (forall ((x ResourceSort) (y ResourceSort) (z ResourceSort))\n" +
                "  (=> (and (Input x y) (Input y z)) (Input x z))))\n" +
                "(assert (distinct house_resrc elctr_resrc popln_resrc))\n" +
                "(assert (distinct x y z))\n" +
                "(assert (Input house_resrc elctr_resrc))\n" +
                "(assert (Input elctr_resrc popln_resrc))\n" +
                "(assert (not (Input house_resrc popln_resrc)))";

        Symbol[] sortNames = {ctx.mkSymbol("ResourceSort")};
        Sort[] sorts = {resourceSort};
        Symbol[] symbols = {
                ctx.mkSymbol("x"), ctx.mkSymbol("y"), ctx.mkSymbol("z"),
                ctx.mkSymbol("house_resrc"), ctx.mkSymbol("elctr_resrc"), ctx.mkSymbol("popln_resrc"),
                ctx.mkSymbol("Input")};
        FuncDecl<?>[] decls = {
                x.getFuncDecl(), y.getFuncDecl(), z.getFuncDecl(),
                house_resrc.getFuncDecl(), elctr_resrc.getFuncDecl(), popln_resrc.getFuncDecl(),
                input};
        BoolExpr[] assertObjects = ctx.parseSMTLIB2String(asserts, sortNames, sorts, symbols, decls);

        Solver s = ctx.mkSolver();
        Arrays.stream(assertObjects)
                .forEach(s::add);

        Status status = s.check();
        assertEquals(Status.UNSATISFIABLE, status);
    }

    @Test
    void testNames() {
        Context ctx = new Context();
        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);

        Symbol house_resrc_resrc_fdecl_name = house_resrc.getFuncDecl().getName();
        assertEquals(ctx.mkSymbol("house_resrc"), house_resrc_resrc_fdecl_name);

        Symbol resourceSortSymbol = resourceSort.getName();
        assertEquals(ctx.mkSymbol("ResourceSort"), resourceSortSymbol);
    }

    @Test
    void testScoreBasic() {
        Context ctx = new Context();
        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
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

        RealExpr score = ctx.mkRealConst("score");

        Symbol[] sortNames = {ctx.mkSymbol("ResourceSort")};
        Sort[] sorts = {resourceSort};
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
                                        score
                                )

                                // Get function declaration from expression
                                .map(Expr::getFuncDecl),

                        // Add function declarations
                        Stream.of(input,
                                action,
                                goal)
        );
        Symbol[] symbols = funcDeclSupplier
                .get()
                .map(FuncDecl::getName)
                .toArray(Symbol[]::new);
        FuncDecl<?>[] decls = funcDeclSupplier.get()
                .toArray(FuncDecl[]::new);

        String asserts = "(assert (distinct house_resrc elctr_resrc popln_resrc\n" +
                "        elmts_resrc alloy_resrc timbr_resrc\n" +
                "        alloy_waste_resrc elctr_waste_resrc house_waste_resrc))\n" +
                "\n" +
                "(assert (forall ((x ResourceSort) (y ResourceSort) (z ResourceSort))\n" +
                "  (=> (and (Input x y) (Input y z)) (Input x z))))\n" +
                "\n" +
                "(assert (Input house_resrc popln_resrc))\n" +
                "(assert (Input house_resrc elmts_resrc))\n" +
                "(assert (Input house_resrc alloy_resrc))\n" +
                "(assert (Input house_resrc timbr_resrc))\n" +
                "\n" +
                "(assert (Input elctr_resrc popln_resrc))\n" +
                "(assert (Input elctr_resrc elmts_resrc))\n" +
                "(assert (Input elctr_resrc alloy_resrc))\n" +
                "\n" +
                "(assert (Input alloy_resrc popln_resrc))\n" +
                "(assert (Input alloy_resrc elmts_resrc))\n" +
                "\n" +
                "(assert (Action popln_resrc))\n" +
                "(assert (Goal house_resrc))\n" +
                "\n" +
                ";; if action creates required element, score = .9\n" +
                "(assert (forall ((x ResourceSort) (y ResourceSort))\n" +
                "        (=> (and (Action y) (Goal x) (Input x y))\n" +
                "            (= score 0.9))))\n";
        BoolExpr[] assertObjects = ctx.parseSMTLIB2String(asserts, sortNames, sorts, symbols, decls);

        Solver s = ctx.mkSolver();
        Arrays.stream(assertObjects)
                .forEach(s::add);

        Status status = s.check();
        assertEquals(Status.SATISFIABLE, status);

        Model model = s.getModel();
        assertEquals(ctx.mkReal(9,10), model.getConstInterp(score));
    }
}
