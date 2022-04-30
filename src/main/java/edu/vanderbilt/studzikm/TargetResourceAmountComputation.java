package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.*;

/**
 * Given a countries resources, computes the amounts that must be acquired
 * to attain maximum quality.
 *
 * People resource will never have required additions
 */
public class TargetResourceAmountComputation {

    private Collection<Transform> transforms;
    private Context ctx;
    private Solver solver;
    private final IntExpr exstg_house;
    private final IntExpr exstg_popln;
    private final IntExpr exstg_elctr;
    private final IntExpr elctr_rqrmt;
    private final IntExpr house_rqrmt;
    private final IntExpr house_timbr;
    private final IntExpr total_alloy;
    private final IntExpr total_elmts;
    private final IntExpr alloy_waste;
    private final IntExpr elctr_waste;
    private final IntExpr house_waste;

    /**
     * Creates a target resource amount computation.
     *
     * Ideally the information for this computation would be extracted from the transforms,
     * but currently is not in place. The transform logic is encoded here directly
     * @param transformFactory the factory to build the transforms (currently unused, but should not be null)
     * @param ctx the Z3 context to use
     */
    public TargetResourceAmountComputation(TransformFactory transformFactory, Context ctx) {
        // TODO: Encapsulate transforms as logic
        this.transforms = transformFactory.getTransforms();
        this.ctx = ctx;
        this.solver = this.ctx.mkSolver();

        // Add base computations to solver in the constructor to reduce computation time
        // during program execution

        // Create constants for existing amounts
        this.exstg_house = ctx.mkIntConst("exstg_house");
        this.exstg_popln = ctx.mkIntConst("exstg_popln");
        this.exstg_elctr = ctx.mkIntConst("exstg_elctr");

        // Create constants for amount needed for each final product
        IntExpr house_alloy = ctx.mkIntConst("house_alloy");    // Amount of alloys needed for the houses
        this.house_timbr = ctx.mkIntConst("house_timbr");       // Amount of timber needed for the houses
        IntExpr house_elmts = ctx.mkIntConst("house_elmts");    // Amount of elements needed for the houses
        this.house_waste = ctx.mkIntConst("house_waste");       // Amount of waste expected from the houses

        IntExpr elctr_elmts = ctx.mkIntConst("elctr_elmts");    // Amount of elements needed for electronics
        IntExpr elctr_alloy = ctx.mkIntConst("elctr_alloy");    // Amount of alloy needed for electronics
        this.elctr_waste = ctx.mkIntConst("elctr_waste");       // Amount of waste expected from electronics

        IntExpr alloy_elmts = ctx.mkIntConst("alloy_elmts");    // Amount of elements needed for alloys
        this.alloy_waste = ctx.mkIntConst("alloy_waste");       // Amount of waste expected from electronics

        this.total_elmts = ctx.mkIntConst("total_elmts");       // Total elements required for all final products
        this.total_alloy = ctx.mkIntConst("total_alloy");       // Total alloy required for all final products


        RealExpr house_popln_ratio = ctx.mkRealConst("house_popln_ratio");  // Ideal ration of houses to people
        this.house_rqrmt = ctx.mkIntConst("house_rqrmt");       // Number of houses to be built

        IntExpr elctr_popln_ratio = ctx.mkIntConst("elctr_popln_ratio");    // Ideal ratio of electronics to people
        this.elctr_rqrmt = ctx.mkIntConst("elctr_rqrmt");       // Number of electronics to be produced

        // Ratio of inputs and outputs to a single house production
        IntNum zero = ctx.mkInt(0);
        IntNum house_alloy_mult = ctx.mkInt(3);
        IntNum house_timbr_mult = ctx.mkInt(5);
        IntNum house_popln_rqmt = ctx.mkInt(5);

        // Ratio of inputs and outputs to a single electronic item production
        RatNum elctr_elmts_mult = ctx.mkReal(3, 2);
        RatNum elctr_waste_mult = ctx.mkReal(1, 2);
        IntNum elctr_popln_rqmt = ctx.mkInt(1);

        // Ratio of inputs and outputs to a single alloy production
        IntNum alloy_elmts_mult = ctx.mkInt(2);
        IntNum alloy_popln_rqmt = ctx.mkInt(1);

        // Set the value of the final product to population ratio
        solver.add(ctx.mkEq(house_popln_ratio, ctx.mkReal(1, 4)));
        solver.add(ctx.mkEq(elctr_popln_ratio, ctx.mkInt(25)));

        // Use ratios to find amounts of resources required to build houses
        solver.add(ctx.mkEq(house_alloy, ctx.mkMul(house_alloy_mult, house_rqrmt)));
        solver.add(ctx.mkEq(house_timbr, ctx.mkMul(house_timbr_mult, house_rqrmt)));
        solver.add(ctx.mkEq(house_elmts, house_rqrmt));
        solver.add(ctx.mkEq(house_waste, house_rqrmt));
        solver.add(ctx.mkGe(ctx.mkAdd(house_rqrmt, exstg_house),
                ctx.mkMul(house_popln_ratio, exstg_popln)));
        solver.add(ctx.mkImplies(ctx.mkGt(house_rqrmt, zero),
                ctx.mkGe(exstg_popln, house_popln_rqmt)));
        solver.add(ctx.mkGe(house_rqrmt,  zero));

        // Use ratios to find amounts of resources required to build houses
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

        // For resources that are inputs to multiple final products, compute the sum
        // of amounts required in each
        solver.add(ctx.mkEq(total_elmts, ctx.mkAdd(house_elmts, elctr_elmts)));
        solver.add(ctx.mkEq(total_alloy, ctx.mkAdd(house_alloy, elctr_alloy)));

        solver.add(ctx.mkEq(alloy_elmts, ctx.mkMul(alloy_elmts_mult, total_alloy)));
        solver.add(ctx.mkEq(alloy_waste, total_alloy));
        solver.add(ctx.mkImplies(ctx.mkGt(total_alloy, zero),
                ctx.mkGe(exstg_popln, alloy_popln_rqmt)));
    }

    /**
     * Computes the amount to acquire of each resource
     * @param country the country to compute amounts for
     * @return a map of resource name to amount to acquire
     */
    Map<String, Integer> compute(Country country) {

        // Create rollback point so new information can be removed from the solver
        solver.push();

        // Set the existing amounts
        solver.add(ctx.mkEq(exstg_house, ctx.mkInt(country.getResource("R23"))));
        solver.add(ctx.mkEq(exstg_popln, ctx.mkInt(country.getResource("R1"))));
        solver.add(ctx.mkEq(exstg_elctr, ctx.mkInt(country.getResource("R22"))));

        // Check that there is a solution
        Status status = solver.check();
        if (status != status.SATISFIABLE) {
            throw new RuntimeException("Resource target proportion model was not satisfied");
        }

        // Get the model
        Model model = solver.getModel();

        // Extract the required values from the model
        Expr<IntSort> elctr_final = model.evaluate(elctr_rqrmt, true);
        Expr<IntSort> house_final = model.evaluate(house_rqrmt, true);

        Expr<IntSort> timbr_rqrmt = model.evaluate(house_timbr, true);
        Expr<IntSort> alloy_rgrmt = model.evaluate(total_alloy, true);
        Expr<IntSort> elmts_rqrmt = model.evaluate(total_elmts, true);
        Expr<IntSort> alloy_waste_expct = model.evaluate(alloy_waste, true);
        Expr<IntSort> elctr_waste_expct = model.evaluate(elctr_waste, true);
        Expr<IntSort> house_waste_expct = model.evaluate(house_waste, true);

        // Put final values into map to return
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

        // Remove objects added expressions from the solver
        solver.pop();
        return proportions;
    }

}
