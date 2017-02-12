(ns re-frame-semantic-ui-react-github-widget.subs
  (:require [re-frame.core :as re-frame]
            [clojure.string :as str]
            [re-frame-semantic-ui-react-github-widget.db :refer [selected-repo]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(re-frame/reg-sub
 :loading
 (fn [db]
   (:loading db)))

(re-frame/reg-sub
 :username
 (fn [db]
   (:username db)))

(re-frame/reg-sub
 :fetch-user-disabled
 (fn [_ _]
   [(re-frame/subscribe [:username])
    (re-frame/subscribe [:loading])])
 (fn [[username loading] _]
   (or (str/blank? username)
       loading)))

(re-frame/reg-sub
 :error
 (fn [db]
   (:error db)))

(re-frame/reg-sub
 :repos
 (fn [db]
   (:repos db)))

(re-frame/reg-sub
 :has-repos
 (fn [_]
   (re-frame/subscribe [:repos]))
 (fn [repos]
   (boolean (seq repos))))

(re-frame/reg-sub
 :selected-repo
 (fn [db]
   (:selected-repo db)))

(re-frame/reg-sub
 :repo
 (fn [_]
   [(re-frame/subscribe [:repos])
    (re-frame/subscribe [:selected-repo])])
 (fn [[repos selected]]
   (selected-repo repos selected)))

(re-frame/reg-sub
 :has-selected-repo
 (fn [_]
   (re-frame/subscribe [:selected-repo]))
 (fn [repo]
   (some? repo)))

(re-frame/reg-sub
 :tree
 (fn [db]
   (:tree db)))

(re-frame/reg-sub
 :has-tree
 (fn [_]
   (re-frame/subscribe [:tree]))
 (fn [tree]
   (some? tree)))

(re-frame/reg-sub
 :tree-truncated
 (fn [_]
   (re-frame/subscribe [:tree]))
 (fn [tree]
   (:truncated tree)))

(re-frame/reg-sub
 :sorted-tree
 (fn [_]
   (re-frame/subscribe [:tree]))
 (fn [tree]
   (sort-by (juxt :type :name) > (:tree tree))))
