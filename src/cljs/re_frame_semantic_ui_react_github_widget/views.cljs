(ns re-frame-semantic-ui-react-github-widget.views
  (:require [re-frame.core :as re-frame]
            [cljsjs.semantic-ui-react]
            [goog.object]))

;; Easy handle to the top-level extern for semantic-ui-react
(def semantic-ui js/semanticUIReact)

(defn component
  "Get a component from sematic-ui-react:

    (component \"Button\")
    (component \"Menu\" \"Item\")"
  [k & ks]
  (if (seq ks)
    (apply goog.object/getValueByKeys semantic-ui k ks)
    (goog.object/get semantic-ui k)))

;; Common references
(def container (component "Container"))
(def segment (component "Segment"))
(def dimmer (component "Dimmer"))
(def loader (component "Loader"))
(def message (component "Message"))
(defn main-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div "Hello from " @name])))
