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

        solver.add(ctx.mkEq(score_input, ctx.mkInt(2)));

        System.out.println(solver.check());
        System.out.println(solver.getModel());
        System.out.println(solver.getModel().getFuncInterp(score));
        System.out.println(solver.getModel().evaluate(sapp, true));

        // If new action is input to goal,
    }

    public Double score(ActionResult<?> result){

        System.out.println(solver.check());
        System.out.println(solver.getModel());

        return null;
    }

}
