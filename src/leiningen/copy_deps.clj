(ns leiningen.copy-deps
  (:require
    [leiningen.core.classpath :as classpath]
    [leiningen.core.main :as main]
    [leiningen.core.project :as project])
  (:import (java.io File FileInputStream FileOutputStream InputStream)
           (java.nio.file Files Path FileSystemException)
           (java.nio.file.attribute FileAttribute)))


(defn- copy
  [^File source-file ^File dest-dir]
  {:pre [(instance? File source-file)
         (instance? File dest-dir)
         (.exists source-file)
         (.isFile source-file)
         (or (.exists dest-dir) (.mkdirs dest-dir))
         (.isDirectory dest-dir)]}
  (let [dest-file (File. dest-dir (.getName source-file))]
    (with-open [in (FileInputStream. source-file)
                out (FileOutputStream. dest-file)]
      (.transferTo ^InputStream in out))
    dest-file))

(defn- symlink-with-fallback
  [^File source-file ^File dest-dir]
  {:pre [(instance? File source-file)
         (instance? File dest-dir)
         (.exists source-file)
         (or (.exists dest-dir) (.mkdirs dest-dir))
         (.isDirectory dest-dir)]}

  (let [source-path (.toPath source-file)
        dest-file (File. dest-dir (.getName source-file))
        dest-path (.toPath dest-file)]
    (when-not (.exists dest-file)
      (try
        (Files/createSymbolicLink dest-path source-path (into-array FileAttribute []))
        (catch FileSystemException e
          (main/info (format "Symlink permission denied for %s, copying instead" (.getName source-file)))
          (copy source-file dest-dir))))
    dest-file))

(defn- copy-deps-internal
  [project target-dir copy-strategy]
  (let [project (project/unmerge-profiles project [:dev :provided])
        deps (->> (classpath/resolve-dependencies :dependencies project)
                  (filter #(.exists ^File %)))]
    (doseq [file deps]
      (case copy-strategy
        :link (symlink-with-fallback file target-dir)
        :copy (copy file target-dir)
        (copy file target-dir)))                            ; default to copy
    (main/info "Copied" (count deps) "file(s) to:" (.toString (.normalize ^Path (.toPath ^File target-dir))))))

(defn copy-deps
  "Copies or symlinks project dependencies to a directory.

  Config: :copy-deps {:path \"./lib\" :strategy :copy}
  - :path - destination directory (default: ./target/lib)
  - :strategy - :copy (default) or :link for symlinks (with fallback to copy)"
  [project & args]
  (let [target-dir (File. "./target")
        {:keys [path strategy]
         :or   {strategy :copy}}
        (get project :copy-deps {})
        directory (if path
                    (File. ^String path)
                    (File. ^String (:root project) "./target/lib"))]
    (when (not (.exists ^File target-dir))
      (.mkdir ^File target-dir))
    (copy-deps-internal project directory strategy)))