(ns re-frame-semantic-ui-react-github-widget.subs
  (:require [re-frame.core :as re-frame]
            [clojure.string :as str])
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
