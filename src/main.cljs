(ns main
  (:require [reagent.core :as r]
            [cljs.core.async :refer (chan put! <! go go-loop timeout)]
            [snake :as s]))

(def maxx 500)
(def maxy 300)
(def init-state {:running? false
                 :alive? true
                 :dx 1
                 :dy 0
                 :body [ [10 10]  ]
                 :growcount 5
                 :delay 250
		 :target [20 20]
                 :maxx 49
                 :maxy 29})
(def state (r/atom init-state))


(defn generate-loop []
  (go-loop []
    ( <! (timeout (:delay @state)))
    (swap! state s/full-turn @state)
    (if (:running? @state)
      (recur)
      )
    
    ))

(defn start-stop-loop []
  (let [running? (:running? @state)]
    (print running?)
    (if running?
      (swap! state assoc :running? false)
      (do
        (swap! state assoc :running? true)
        (generate-loop)
        )
      )))


(defn draw-cells [comp]
(let [node (r/dom-node comp)
      ctx (.getContext node "2d")
      ]
  (set! (. ctx -fillStyle) (str "rgb(" 255 "," 255"," 255 ")"))
  (.fillRect ctx 0 0 maxx maxy)
  
  (set! (. ctx -fillStyle) (str "rgb(" 0 "," 255"," 0 ")"))
  (if (:target @state)
    (let [ [x y] (:target @state)] (.fillRect ctx (* 10 x) (* 10 y) 10 10)))
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
  (let [k (.-keyCode e)
        newdelta     (case k
                       38 [0 -1]
                       40 [0 1]
                       39 [1 0]
                       37 [-1 0]
                       "default" [0 0]
                       )
        [ndx ndy] newdelta
        ]
    
    (print k)  
    (swap! state assoc :dx ndx :dy ndy)
    )

  )
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
                                  :tabindex '0' :id "c" :width maxx :height maxy :style {:border "2px solid green"}}])
      }
     )))

(defn move [e]
  (swap! state s/move-snake)
  )
(defn main-component []
  [:div 
   [canvas state]
   [:hr]
   [:p "Interface here"]
   [:button.button {:on-click move} "move"]
   [:buttton.button.is-warning {:on-click #(reset! state init-state)} "REset"]
   [:buttton.button.is-alert {:on-click start-stop-loop} "go/stop"]
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
