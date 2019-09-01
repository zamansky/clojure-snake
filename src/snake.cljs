(ns snake
  (:require [reagent.core :as r ]
            [taoenso.timbre :as t]
            ))



(def state (r/atom {:alive? true
                    :dx 1
                    :dy 0
                    :body [ [10 10] ]
                    :growcount 5
                    :delay 500
		    :target nil})
  )



(defn move-snake [{:keys [dx dy body growcount] :as state}]
  (t/info "TESTIN")

  )
