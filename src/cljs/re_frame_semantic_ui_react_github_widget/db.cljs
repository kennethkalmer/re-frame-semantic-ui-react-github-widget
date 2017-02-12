(ns re-frame-semantic-ui-react-github-widget.db)

(def default-db
  {:username ""})

(defn selected-repo [repos selected]
  (some #(when (= (:id %) selected) %) repos))
