(ns re-frame-semantic-ui-react-github-widget.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [re-frisk.core :refer [enable-re-frisk!]]
              [re-frame-semantic-ui-react-github-widget.events]
              [re-frame-semantic-ui-react-github-widget.subs]
              [re-frame-semantic-ui-react-github-widget.views :as views]
              [re-frame-semantic-ui-react-github-widget.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
