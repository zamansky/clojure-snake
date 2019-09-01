(ns main
  (:require [reagent.core :as r]
            [cljs.core.async :refer (chan put! <! go go-loop timeout)]
            [snake :as s]))
(def state (r/atom {:alive? true
                    :dx 1
                    :dy 0
                    :body [ [10 10]  ]
                    :growcount 5
                    :delay 500
		    :target nil
                    :maxx 100
                    :maxy 100})
  )

(defn draw-cells [comp]
  (let [node (r/dom-node comp)
        ctx (.getContext node "2d")
        ]
    (set! (. ctx -fillStyle) (str "rgb(" 255 "," 255"," 255 ")"))
    (.fillRect ctx 0 0 500 500)
    ()
    (set! (. ctx -fillStyle) (str "rgb(" 255 "," 0"," 0 ")"))
    (doseq [ [x y] (:body @state)]
      (.fillRect ctx (* 10 x) (* 10 y) 10 10)                                 
      )

    )
  )

(defn handle-keys [e]
  ;; up 38
  ;; down 40 
  ;; left 39
  ;; right 17
  (let [k (.-keyCode e)]
    (print k))
  (print "SOMETHING"))
(defn get-focus [e]
  (print "HELLO"))
  
(defn canvas [state]
  (let [state state]
    (r/create-class
     {:display-name "canvas"
      :component-did-mount draw-cells 
      :component-did-update draw-cells
      :reagent-render (fn  []
                        @state
                        [:canvas { :on-keyDown handle-keys
                                  ;;:on-mouseOver get-focus
                                  :tabindex '0' :id "c" :width 500 :height 500 :style {:border "2px solid green"}}])
      }
     )))

(defn main-component []
  [:div 
   [canvas state]
   [:hr]
   [:p "Interface here"]
   ])


(defn mount [c]
  (r/render-component [c] (.getElementById js/document "app"))
  )

(defn reload! []
  (mount main-component)
  (print "Hello Main!"))

(defn main! []
  (mount main-component)
  (print "Hello Main"))
