(ns re-frame-semantic-ui-react-github-widget.events
    (:require [re-frame.core :as re-frame]
              [re-frame-semantic-ui-react-github-widget.db :as db]
              [day8.re-frame.http-fx]
              [ajax.core :as ajax]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :username-changed
 (fn [db [_ username]]
   (assoc db :username username)))

(re-frame/reg-event-fx
 :fetch-repos
 (fn [{db :db} _]
   (let [user (:username db)]
     {:db         (-> db
                      (assoc :loading true)
                      (dissoc :error))
      :http-xhrio {:method          :get
                   :uri             (str "https://api.github.com/users/" user "/repos")
                   :timeout         5000
                   :response-format (ajax/json-response-format {:keywordize-keys true})
                   :on-success      [:repos-loaded]
                   :on-failure      [:repos-failed]}})))

(re-frame/reg-event-db
 :repos-loaded
 (fn [db [_ response]]
   (-> db
       (dissoc :loading :error)
       (assoc :repos response))))

(re-frame/reg-event-db
 :repos-failed
 (fn [db [_ error]]
   (-> db
       (dissoc :loading)
       (assoc :error error))))
