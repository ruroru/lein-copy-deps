(defproject org.clojars.jj/copy-deps  "1.0.2-SNAPSHOT"
  :description "lein-copy-deps is a Leiningen plugin that copies all your project's dependencies to a single directory. It supports both hard copy and symlink"
  :url "https://github.com/ruroru/lein-copy-deps"
  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [leiningen-core/leiningen-core "2.12.0"]]

  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]

  :plugins    [[org.clojars.jj/bump "1.0.4"]
               [org.clojars.jj/bump-md "1.0.0"]])
