package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.*;

public class TargetResourceAmountComputation {

    private Collection<Transform> transforms;
    private Context ctx;

    public TargetResourceAmountComputation(TransformFactory transformFactory, Context ctx) {
        // TODO: Encapsulate transforms as logic
        this.transforms = transformFactory.getTransforms();
        this.ctx = ctx;
    }

    Map<String, Integer> compute(Country country) {
        IntNum exstg_house = ctx.mkInt(country.getResource("R23"));
        IntNum exstg_popln = ctx.mkInt(country.getResource("R1"));
        IntNum exstg_elctr = ctx.mkInt(country.getResource("R22"));

        IntExpr house_alloy = ctx.mkIntConst("house_alloy"); // Amount of alloys needed for the houses
        IntExpr house_timbr = ctx.mkIntConst("house_timbr"); // Amount of timber needed for the houses
        IntExpr house_elmts = ctx.mkIntConst("house_elmts"); // Amount of elements needed for the houses
        IntExpr house_waste = ctx.mkIntConst("house_waste");

        IntExpr elctr_elmts = ctx.mkIntConst("elctr_elmts");
        IntExpr elctr_alloy = ctx.mkIntConst("elctr_alloy");
        IntExpr elctr_waste = ctx.mkIntConst("elctr_waste");

        IntExpr total_elmts = ctx.mkIntConst("total_elmts");
        IntExpr total_alloy = ctx.mkIntConst("total_alloy");

        IntExpr alloy_elmts = ctx.mkIntConst("alloy_elmts");
        IntExpr alloy_waste = ctx.mkIntConst("alloy_waste");

        RealExpr house_popln_ratio = ctx.mkRealConst("house_popln_ratio");
        IntExpr house_rqrmt = ctx.mkIntConst("house_rqrmt"); // Number of houses to be built

        IntExpr elctr_popln_ratio = ctx.mkIntConst("elctr_popln_ratio");
        IntExpr elctr_rqrmt = ctx.mkIntConst("elctr_rqrmt");

        IntNum zero = ctx.mkInt(0);
        IntNum house_alloy_mult = ctx.mkInt(3);
        IntNum house_timbr_mult = ctx.mkInt(5);
        IntNum house_popln_rqmt = ctx.mkInt(5);

        RatNum elctr_elmts_mult = ctx.mkReal(3, 2);
        RatNum elctr_waste_mult = ctx.mkReal(1, 2);
        IntNum elctr_popln_rqmt = ctx.mkInt(1);

        IntNum alloy_elmts_mult = ctx.mkInt(2);
        IntNum alloy_popln_rqmt = ctx.mkInt(1);

        Solver solver = ctx.mkSolver();
        solver.add(ctx.mkEq(house_popln_ratio, ctx.mkReal(1, 4)));
        solver.add(ctx.mkEq(elctr_popln_ratio, ctx.mkInt(25)));

        solver.add(ctx.mkEq(house_alloy, ctx.mkMul(house_alloy_mult, house_rqrmt)));
        solver.add(ctx.mkEq(house_timbr, ctx.mkMul(house_timbr_mult, house_rqrmt)));
        solver.add(ctx.mkEq(house_elmts, house_rqrmt));
        solver.add(ctx.mkEq(house_waste, house_rqrmt));
        solver.add(ctx.mkGe(ctx.mkAdd(house_rqrmt, exstg_house),
                            ctx.mkMul(house_popln_ratio, exstg_popln)));
        solver.add(ctx.mkImplies(ctx.mkGt(house_rqrmt, zero),
                                 ctx.mkGe(exstg_popln, house_popln_rqmt)));
        solver.add(ctx.mkGe(house_rqrmt,  zero));

        solver.add(ctx.mkAnd(
                ctx.mkEq(elctr_elmts, ctx.mkMul(elctr_elmts_mult, elctr_rqrmt)),
                ctx.mkEq(elctr_waste, ctx.mkMul(elctr_waste_mult, elctr_rqrmt)),
                ctx.mkEq(elctr_alloy, elctr_rqrmt),
                ctx.mkGe(ctx.mkAdd(elctr_rqrmt, exstg_elctr),
                        ctx.mkMul(elctr_popln_ratio, exstg_popln)),
                ctx.mkImplies(ctx.mkGt(elctr_rqrmt, zero),
                        ctx.mkGe(exstg_popln, elctr_popln_rqmt))
        ));
        solver.add(ctx.mkGe(elctr_rqrmt,  zero));


        solver.add(ctx.mkEq(total_elmts, ctx.mkAdd(house_elmts, elctr_elmts)));
        solver.add(ctx.mkEq(total_alloy, ctx.mkAdd(house_alloy, elctr_alloy)));

        solver.add(ctx.mkEq(alloy_elmts, ctx.mkMul(alloy_elmts_mult, total_alloy)));
        solver.add(ctx.mkEq(alloy_waste, total_alloy));
        solver.add(ctx.mkImplies(ctx.mkGt(total_alloy, zero),
                                 ctx.mkGe(exstg_popln, alloy_popln_rqmt)));

        Status status = solver.check();
        if (status != status.SATISFIABLE) {
            throw new RuntimeException("Resource target proportion model was not satisfied");
        }

        Model model = solver.getModel();

        Expr<IntSort> elctr_final = model.evaluate(elctr_rqrmt, true);
        Expr<IntSort> house_final = model.evaluate(house_rqrmt, true);

        Expr<IntSort> timbr_rqrmt = model.evaluate(house_timbr, true);
        Expr<IntSort> alloy_rgrmt = model.evaluate(total_alloy, true);
        Expr<IntSort> elmts_rqrmt = model.evaluate(total_elmts, true);
        Expr<IntSort> alloy_waste_expct = model.evaluate(alloy_waste, true);
        Expr<IntSort> elctr_waste_expct = model.evaluate(elctr_waste, true);
        Expr<IntSort> house_waste_expct = model.evaluate(house_waste, true);

        Map<String, Integer> proportions = new HashMap<>();
        proportions.put("R1", country.getResource("R1"));
        proportions.put("R2", Integer.valueOf(elmts_rqrmt.toString()));
        proportions.put("R3", Integer.valueOf(timbr_rqrmt.toString()));
        proportions.put("R21", Integer.valueOf(alloy_rgrmt.toString()));
        proportions.put("R22", Integer.valueOf(elctr_final.toString()));
        proportions.put("R23", Integer.valueOf(house_final.toString()));
        proportions.put("R21'", Integer.valueOf(alloy_waste_expct.toString()));
        proportions.put("R22'", Integer.valueOf(elctr_waste_expct.toString()));
        proportions.put("R23'", Integer.valueOf(house_waste_expct.toString()));
        return proportions;
    }

}
