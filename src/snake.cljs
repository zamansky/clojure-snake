(ns snake
  (:require [reagent.core :as r ]
            [taoensso.timbre :as t]
            ))



(def state (r/atom {:alive? true
                    :dx 1
                    :dy 0
                    :body [ [10 10] ]
                    :growcount 5
                    :delay 500
		    :target nil
                    :maxx 100
                    :maxy 100})
  )
(def state2 (r/atom {:alive? true
                     :dx 1
                     :dy 0
                     :body [ [10 10] [11 10] [12 10] ]
                     :growcount 1
                     :delay 500
		     :target nil
                     :maxx 100
                     :maxy 100})
  )
(def state3 (r/atom {:alive? true
                     :dx 1
                     :dy 0
                     :body [ [100 10] [11 10] [12 10] ]
                     :growcount 1
                     :delay 500
		     :target nil
                     :maxx 100
                     :maxy 100})
  )


(defn hit-wall? [{:keys [body maxx maxy] :as state}]
  (let [ [x y] (first body)]
    (or (>= x maxx) (<= x 0) (>= y maxy) (<= y 0))
    )
  )

(defn hit-self? [{:keys [body] :as state}]
  (let [head (first body)
        tail (set (rest body))]
    (print head)
    (print tail)
    (contains? tail head)
    )
  )

(defn spawn-food [{:keys [target maxx maxy]:as state}]
  (let [x (rand-int maxx)
        y (rand-int maxy)
        newstate (assoc state :target [x y])
        ]
    newstate
    )

  )

(defn move-snake [{:keys [dx dy body growcount] :as state}]
  "Does not check to see if snake hits wall or self"
  (let [[x y] (first body)
        newhead [(+ x dx) (+ y dy)]
        nb (cons newhead body)
        newbody (if (pos? growcount) nb (drop-last nb) )
        n (assoc state :body newbody)
        newstate (assoc n :growcount (if (pos? growcount) (dec growcount) growcount))
        ]
    newstate
    )

  )

(defn full-turn [{:keys [target body] :as state}]
  (let [ moved-state (move-snake state)
        [x y] (first (:body moved-state))
        ate-state (if (= [x y] target)
                    (-> moved-state
                        spawn-food
                        (assoc :growcount 5)
                        )
                    moved-state
                    )
        new-state (if (or (hit-wall? ate-state) (hit-self? ate-state))
                    (-> ate-state
                        (assoc :running? false)
                        (assoc :alive? false)
                        )
                    ate-state
                    )
        ]
    new-state
    ))
