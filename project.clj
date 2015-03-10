(defproject rumshot "0.1.0-SNAPSHOT"
  :description "Example Rum App"
  :url "http://github.com/scgilardi/rumshot"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3058"]
                 [compojure "1.3.2"]
                 [hiccup "1.0.5"]
                 [rum "0.2.6"]
                 [liberator "0.12.2"]]
  :ring {:init rumshot.core/ring-init
         :handler rumshot.core/ring-handler
         :destroy rumshot.core/ring-destroy}
  :cljsbuild {:builds
              [{:id "none"
                :source-paths  ["src"]
                :compiler
                {:optimizations :none
                 :output-to     "resources/public/js/rumshot.js"
                 :output-dir    "resources/public/js/out"
                 :main          rumshot.core
                 :asset-path    "js/out"
                 :source-map    true
                 :warnings      {:single-segment-namespace false}}}
               {:id "advanced"
                :source-paths  ["src"]
                :compiler
                {:optimizations :advanced
                 :output-to     "resources/public/js/rumshot.js"
                 :elide-asserts true
                 :pretty-print  false
                 :warnings      {:single-segment-namespace false}}}]}
  :profiles {:dev
             {:plugins [[lein-cljsbuild "1.0.5"]
                        [lein-ring "0.8.11"]]}})
