(defproject csv-transactions "0.1.0-SNAPSHOT"
	:description "Parse Deutsche Bank transactions in CSV format and aggregates incomes and expenses by month"
	:url "https://bitbucket.org/unisay/csv-transactions"
	:license {:name "Eclipse Public License"
	          :url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies [
		[org.clojure/clojure "1.5.1"]
		[clojure-csv/clojure-csv "2.0.0-alpha1"]
		[clj-time "0.5.1"]
		[org.clojure/clojure-contrib "1.2.0"]
  ]
	:profiles {:dev {:dependencies [[midje "1.5.0"]]}}
	:main csv-transactions.core
	:jvm-opts [])
