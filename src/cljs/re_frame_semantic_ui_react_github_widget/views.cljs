(ns re-frame-semantic-ui-react-github-widget.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
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
(def message-header (component "Message" "Header"))
(def input (component "Input"))
(def button (component "Button"))

(defn username-field
  "Render an input field for accepting a GitHub username"
  []
  (let [username (re-frame/subscribe [:username])
        loading  (re-frame/subscribe [:loading])
        disabled (re-frame/subscribe [:fetch-user-disabled])]
    (fn []
      [:div
       [:> input
        {:placeholder "GitHub username"
         :loading     @loading
         :disabled    @loading
         :value       (or @username "")
         :onChange    #(re-frame/dispatch [:username-changed (-> %2
                                                                 (.-value)
                                                                 (js->clj))])}]
       [:> button
        {:primary  true
         :loading  @loading
         :disabled @disabled
         :onClick  #(re-frame/dispatch [:fetch-repos])}
        "Fetch"]])))

(defn main-panel []
  (let [loading (re-frame/subscribe [:loading])
        error   (re-frame/subscribe [:error])]
    (fn []
      [:> container
       [:> segment {:basic true}
        [:> loader {:active @loading}]

        [:> message "To get started, enter a GitHub username below"]
        (if @error
          [:> message
           {:negative true}
           [:> message-header "Something went wrong :("]
           @error])
        [username-field]]])))
