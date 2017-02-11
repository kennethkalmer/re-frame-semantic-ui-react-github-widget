(ns re-frame-semantic-ui-react-github-widget.events
    (:require [re-frame.core :as re-frame]
              [re-frame-semantic-ui-react-github-widget.db :as db]
              [day8.re-frame.http-fx]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
