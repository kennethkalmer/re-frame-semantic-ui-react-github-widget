(ns re-frame-semantic-ui-react-github-widget.events
    (:require [re-frame.core :as re-frame]
              [re-frame-semantic-ui-react-github-widget.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
