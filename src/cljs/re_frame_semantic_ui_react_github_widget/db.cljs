(ns re-frame-semantic-ui-react-github-widget.db)

(def default-db
  {:name "re-frame"})

(defn selected-repo [repos selected]
  (some #(when (= (:id %) selected) %) repos))
