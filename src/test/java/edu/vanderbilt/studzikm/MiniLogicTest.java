package edu.vanderbilt.studzikm;

import com.microsoft.z3.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
