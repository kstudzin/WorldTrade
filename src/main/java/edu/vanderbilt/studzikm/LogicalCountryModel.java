package edu.vanderbilt.studzikm;

import com.microsoft.z3.*;

public class LogicalCountryModel {

    private Context ctx;
    private Solver solver;
    private Country country;
    private Integer time = 0;

    public LogicalCountryModel(Context context, Country country) {
        this.ctx = context;
        this.country = country;
        this.solver = context.mkSolver();
        initialize();
    }

    /**
     * Add initial axioms to model
     */
    private void initialize() {
        IntNum exstg_popln = ctx.mkInt(country.getResource("R1"));
        IntNum extsg_elmts = ctx.mkInt(country.getResource("R2"));
        IntNum extsg_timbr = ctx.mkInt(country.getResource("R3"));
        IntNum extsg_alloy = ctx.mkInt(country.getResource("R21"));
        IntNum extsg_elctr = ctx.mkInt(country.getResource("R22"));
        IntNum extsg_house = ctx.mkInt(country.getResource("R23"));
        
        Sort[] goalInSorts = {ctx.getStringSort(), ctx.getIntSort()};
        FuncDecl<BoolSort> goal = ctx.mkFuncDecl("Goal", goalInSorts, ctx.getBoolSort());
        SeqExpr<CharSort> house = ctx.mkString("house");
        Expr<BoolSort> goalf = ctx.mkApp(goal, house, ctx.mkInt(0));

        ArithExpr<ArithSort> house_rqrmt = ctx.mkMul(exstg_popln, ctx.mkReal(1, 4));
        BoolExpr house_goal = ctx.mkLt(extsg_house, house_rqrmt);
        solver.add(ctx.mkImplies(house_goal, ctx.mkEq(goalf, ctx.mkTrue())));
//        solver.add(ctx.mkImplies(goalf, ));

        FuncDecl<IntSort> score = ctx.mkFuncDecl("Score", ctx.getIntSort(), ctx.getIntSort());
        IntExpr score_input = ctx.mkIntConst("score_input");
        IntExpr score_otput = ctx.mkIntConst("score_otput");
        Expr<IntSort> sapp = ctx.mkApp(score, score_input);
        Expr<IntSort> sapp2 = ctx.mkApp(score, ctx.mkInt(4));

        solver.add(ctx.mkImplies(ctx.mkEq(score_input, ctx.mkInt(1)), ctx.mkEq(sapp, ctx.mkInt(5))));
        solver.add(ctx.mkImplies(ctx.mkEq(score_input, ctx.mkInt(2)), ctx.mkEq(sapp, ctx.mkInt(6))));
        solver.add(ctx.mkImplies(ctx.mkEq(score_input, ctx.mkInt(3)), ctx.mkEq(sapp, ctx.mkInt(7))));
        solver.add(ctx.mkEq(sapp2, ctx.mkInt(8)));
        solver.add(ctx.mkEq(ctx.mkApp(score, ctx.mkInt(5)), ctx.mkInt(8)));


        solver.add(ctx.mkEq(score_input, ctx.mkInt(2)));
        solver.add(ctx.mkEq(score.apply(ctx.mkInt(4)), ctx.mkInt(8)));


        UninterpretedSort resourceSort = ctx.mkUninterpretedSort("ResourceSort");
        Expr<UninterpretedSort> house_resrc = ctx.mkConst("house_resrc", resourceSort);
        Expr<UninterpretedSort> elctr_resrc = ctx.mkConst("elctr_resrc", resourceSort);
        Expr<UninterpretedSort> popln_resrc = ctx.mkConst("popln_resrc", resourceSort);
        Expr<UninterpretedSort> elmts_resrc = ctx.mkConst("elmts_resrc", resourceSort);
        Expr<UninterpretedSort> alloy_resrc = ctx.mkConst("alloy_resrc", resourceSort);
        Expr<UninterpretedSort> timbr_resrc = ctx.mkConst("timbr_resrc", resourceSort);


        Sort[] goal2Sorts = {ctx.getIntSort()};
        FuncDecl<UninterpretedSort> goal2 = ctx.mkFuncDecl("Goal2", goal2Sorts, resourceSort);
        solver.add(ctx.mkAnd(
                ctx.mkImplies( ctx.mkLt(extsg_house, house_rqrmt), ctx.mkEq(ctx.mkApp(goal2, ctx.mkInt(0)), house_resrc)),
                ctx.mkImplies( ctx.mkNot(ctx.mkLt(extsg_house, house_rqrmt)), ctx.mkEq(ctx.mkApp(goal2, ctx.mkInt(0)), elctr_resrc))));

        Sort[] inputInSort = {resourceSort, resourceSort};
        FuncDecl<IntSort> input = ctx.mkFuncDecl("Input", inputInSort, ctx.getIntSort());
        solver.add(
                ctx.mkAnd(
                        ctx.mkEq(ctx.mkApp(input, house_resrc, timbr_resrc), ctx.mkInt(5))),
                        ctx.mkEq(ctx.mkApp(input, house_resrc, popln_resrc), ctx.mkInt(5)),
                        ctx.mkEq(ctx.mkApp(input, house_resrc, elmts_resrc), ctx.mkInt(1)),
                        ctx.mkEq(ctx.mkApp(input, house_resrc, alloy_resrc), ctx.mkInt(3))
        );



        System.out.println(solver.check());
        System.out.println(solver.getModel());
        System.out.println(solver.getModel().getFuncInterp(score));
        System.out.println(solver.getModel().evaluate(sapp, true));
        System.out.println(score.apply(ctx.mkInt(4)));

        // If new action is input to goal,
    }

    public Double score(ActionResult<?> result){

        System.out.println(solver.check());
        System.out.println(solver.getModel());

        return null;
    }

}
