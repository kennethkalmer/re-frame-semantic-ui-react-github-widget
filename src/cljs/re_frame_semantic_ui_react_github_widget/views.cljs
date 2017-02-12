(ns re-frame-semantic-ui-react-github-widget.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljsjs.semantic-ui-react]
            [goog.object])
  (:require-macros [reagent.ratom :refer [reaction]]))

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
(def icon (component "Icon"))

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

(defn repo-dropdown
  "Render a dropdown component for selecting a repo"
  []
  (let [repos     (re-frame/subscribe [:repos])
        options   (reaction
                   (map (fn [repo]
                          {:key         (:id repo)
                           :value       (:id repo)
                           :text        (:name repo)
                           :description (:description repo)})
                        @repos))
        dropdown  (component "Dropdown")
        on-change (fn [_ data]
                    (let [selected (js->clj (.-value data))]
                      (re-frame/dispatch [:repo-selected selected])))]

    (fn []
      [:> dropdown
       {:placeholder "Select a repo"
        :fluid       true
        :selection   true
        :search      true
        :options     @options
        :onChange    on-change}])))

(defn repo-tree []
  (let [repo        (re-frame/subscribe [:repo])
        has-tree?   (re-frame/subscribe [:has-tree])
        truncated?  (re-frame/subscribe [:tree-truncated])
        files       (re-frame/subscribe [:sorted-tree])
        loading     (re-frame/subscribe [:loading])
        table       (component "Table")
        header      (component "Table" "Header")
        body        (component "Table" "Body")
        row         (component "Table" "Row")
        header-cell (component "Table" "HeaderCell")
        cell        (component "Table" "Cell")]
    (fn []
      (if @has-tree?
        [:> table
         {:celled  true
          :striped true}

         [:> header
          [:> row
           [:> header-cell
            ;; {:colSpan 3}
            (:name @repo)]]]

         [:> body
          (if @loading
            [:> row
             [:> cell
              [:> loader
               {:active true
                :inline "centered"}
               "Loading repo tree"]]]

            (for [obj @files]
              ^{:key (:path obj)}
              [:> row
               [:> cell
                {:collapsing true}
                [:a {:href   (:url obj)
                     :target "_blank"}
                 [:> icon {:name (if (= "blob" (:type obj)) "file outline" "folder")}]
                 (:path obj)]]]))

          (if @truncated?
            [:> row
             [:> cell
              [:a {:href   (:url repo)
                   :target "_blank"}
               "Browse full repo"]]])]]))))

(defn main-panel []
  (let [loading   (re-frame/subscribe [:loading])
        error     (re-frame/subscribe [:error])
        repos?    (re-frame/subscribe [:has-repos])
        selected? (re-frame/subscribe [:has-selected-repo])
        header    (component "Header")]
    (fn []
      [:> container
       [:> segment
        [:> header {:as "h1"} "GitHub Tree Widget"]
        [:> message "To get started, enter a GitHub username below"]
        (if @error
          [:> message
           {:negative true}
           [:> message-header "Something went wrong :("]
           @error])
        [username-field]

        (if repos?
          (list
           ^{:key (rand)} [:> message "Choose a repo"]
           ^{:key (rand)} [repo-dropdown]))

        (if selected?
          [repo-tree])]])))
