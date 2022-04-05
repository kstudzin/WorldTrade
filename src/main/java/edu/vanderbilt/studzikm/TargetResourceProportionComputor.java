package edu.vanderbilt.studzikm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.*;

public class TargetResourceProportionComputor {

    private Collection<Transform> transforms;
    private Context ctx;

    public TargetResourceProportionComputor(TransformFactory transformFactory, Context ctx) {
        // TODO: Encapsulate transforms as logic
        this.transforms = transformFactory.getTransforms();
        this.ctx = ctx;
    }

    Map<String, Double> compute(Country country) {
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

        RealExpr timbr_popln_ratio = ctx.mkRealConst("timbr_popln_ratio");
        RealExpr elmts_popln_ratio = ctx.mkRealConst("elmts_popln_ratio");
        RealExpr alloy_popln_ratio = ctx.mkRealConst("alloy_popln_ratio");

        RealExpr house_waste_popln_ratio = ctx.mkRealConst("house_waste_popln_ratio");
        RealExpr alloy_waste_popln_ratio = ctx.mkRealConst("alloy_waste_popln_ratio");
        RealExpr elctr_waste_popln_ratio = ctx.mkRealConst("elctr_waste_popln_ratio");

        solver.add(ctx.mkEq(house_timbr, ctx.mkMul(timbr_popln_ratio, exstg_popln)));
        solver.add(ctx.mkEq(total_alloy, ctx.mkMul(alloy_popln_ratio, exstg_popln)));
        solver.add(ctx.mkEq(total_elmts, ctx.mkMul(elmts_popln_ratio, exstg_popln)));

        solver.add(ctx.mkEq(house_waste, ctx.mkMul(house_waste_popln_ratio, exstg_popln)));
        solver.add(ctx.mkEq(alloy_waste, ctx.mkMul(alloy_waste_popln_ratio, exstg_popln)));
        solver.add(ctx.mkEq(elctr_waste, ctx.mkMul(elctr_waste_popln_ratio, exstg_popln)));

        Status status = solver.check();
        if (status != status.SATISFIABLE) {
            throw new RuntimeException("Resource target proportion model was not satisfied");
        }

        Model model = solver.getModel();

        Expr<RealSort> timbr_ratio_logic = model.evaluate(timbr_popln_ratio, true);
        Expr<RealSort> alloy_ratio_logic = model.evaluate(alloy_popln_ratio, true);
        Expr<RealSort> elmts_ratio_logic = model.evaluate(elmts_popln_ratio, true);
        Expr<RealSort> house_ratio_logic = model.evaluate(house_popln_ratio, true);
        Expr<IntSort>  elctr_ratio_logic = model.evaluate(elctr_popln_ratio, true);
        Expr<RealSort> alloy_waste_ratio_logic = model.evaluate(alloy_waste_popln_ratio, true);
        Expr<RealSort> elctr_waste_ratio_logic = model.evaluate(elctr_waste_popln_ratio, true);
        Expr<RealSort> house_waste_ratio_logic = model.evaluate(house_waste_popln_ratio, true);

        Map<String, Double> proportions = new HashMap<>();
        proportions.put("R1", 1.0);
        proportions.put("R2", parse(elmts_ratio_logic.toString()));
        proportions.put("R3", parse(timbr_ratio_logic.toString()));
        proportions.put("R21", parse(alloy_ratio_logic.toString()));
        proportions.put("R22", parse(elctr_ratio_logic.toString()));
        proportions.put("R23", parse(house_ratio_logic.toString()));
        proportions.put("R21'", parse(alloy_waste_ratio_logic.toString()));
        proportions.put("R22'", parse(elctr_waste_ratio_logic.toString()));
        proportions.put("R23'", parse(house_waste_ratio_logic.toString()));
        return proportions;
    }

    // From https://stackoverflow.com/questions/13249858/how-to-convert-from-fraction-formatted-string-to-decimal-or-float-in-java
    private double parse(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

}
