(defproject test-clj "0.1.0-SNAPSHOT"
	:description "FIXME: write description"
	:url "http://example.com/FIXME"
	:license {:name "Eclipse Public License"
	          :url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies [
		              [org.clojure/clojure "1.5.1"]
		              [clojure-csv/clojure-csv "2.0.0-alpha1"]
		              [clj-time "0.5.1"]
	                [org.clojure/clojure-contrib "1.2.0"]
  ]
	:profiles {:dev {:dependencies [[midje "1.5.0"]]}}
	:main test-clj.core
	:jvm-opts [])
