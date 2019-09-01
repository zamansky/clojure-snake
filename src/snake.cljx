(ns snake
  (:require [reagent.core :as r ]))



(def state (r/atom {:dx 1
                    :dy 0
                    :body [ [10 10] ]
                    :growcount 5
                    :delay 500)))


