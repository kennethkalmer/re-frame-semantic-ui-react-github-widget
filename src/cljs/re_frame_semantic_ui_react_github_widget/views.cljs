(ns re-frame-semantic-ui-react-github-widget.views
  (:require [re-frame.core :as re-frame]
            [cljsjs.semantic-ui-react]
            [goog.object]))

(defn main-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div "Hello from " @name])))
