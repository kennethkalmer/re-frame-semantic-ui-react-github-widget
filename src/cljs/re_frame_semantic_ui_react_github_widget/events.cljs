(ns re-frame-semantic-ui-react-github-widget.events
  (:require [re-frame.core :as re-frame]
            [re-frame-semantic-ui-react-github-widget.db :as db]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [clojure.string :as str]))

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
                   :params          {:sort :pushed}
                   :timeout         5000
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:repos-loaded]
                   :on-failure      [:ajax-failed]}})))

(re-frame/reg-event-db
 :repos-loaded
 (fn [db [_ response]]
   (-> db
       (dissoc :loading :error)
       (assoc :repos response))))

(re-frame/reg-event-db
 :ajax-failed
 (fn [db [_ error]]
   (-> db
       (dissoc :loading)
       (assoc :error error))))

(re-frame/reg-event-fx
 :repo-selected
 (fn [{db :db} [_ selected]]
   (let [repo (db/selected-repo (:repos db) selected)
         uri  (str/replace (:trees_url repo) "{/sha}" "/HEAD")]
     (assert (= selected (:id repo)) (str "Mismatching repo:" (pr-str repo)))
     {:db         (-> db
                      (assoc :selected-repo repo)
                      (assoc :loading true)
                      (dissoc :tree :error))
      :http-xhrio {:method          :get
                   :uri             uri
                   :timeout         10000
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:tree-loaded]
                   :on-failure      [:ajax-failed]}})))

(re-frame/reg-event-db
 :tree-loaded
 (fn [db [_ response]]
   (-> db
       (dissoc :loading :error)
       (assoc :tree response))))
