
(assert (distinct house_resrc elctr_resrc popln_resrc
        elmts_resrc alloy_resrc timbr_resrc
        alloy_waste_resrc elctr_waste_resrc house_waste_resrc
        null_resrc))

(assert (distinct oneOfOne twoOfTwo threeOfThree))

(assert (exists ((x ResourceSort) (y ResourceSort) (z ResourceSort))
  (and (Input x y) (Input y z) (Input x z))))

(assert (forall ((t TimeStep)) (exists ((x Int)) (and (= (Time t) x) (= (InvTime x) t)))))

(assert (forall ((x ResourceSort) (y ResourceSort) (z ResourceSort) (t TimeStep))
  (and (ite (and (Action y t) (Goal x t) (Input x y)) (= (Score oneOfOne t) 0.85) (= (Score oneOfOne t) 0.15))
       (ite (and (= (Score oneOfOne t) 0.85) (Action z (InvTime (- (Time t) 1))) (Goal x (InvTime (- (Time t) 1))) (Input x z))
                   (= (Score twoOfTwo t) 0.9) (= (Score twoOfTwo t) 0.1))
  )))

(assert (forall ((w ResourceSort) (x ResourceSort) (t TimeStep))
  (ite (and (= (Score twoOfTwo t) 0.9) (Action w (InvTime (- (Time t) 2))) (Goal x (InvTime (- (Time t) 2))) (Input x w))
              (= (Score threeOfThree t) 0.95) (= (Score threeOfThree t) 0.05))))

(declare-const tn1 TimeStep)
(assert (= (Time tn1) -1))
(assert (= (InvTime -1) tn1))
