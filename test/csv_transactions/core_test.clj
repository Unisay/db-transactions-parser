(ns csv-transactions.core-test
	(:use midje.sweet
	      csv-transactions.core
	      [midje.util :only [testable-privates]]))

(testable-privates csv-transactions.core
	strip-header-footer parse-csv-date parse-number-default parse-csv-rows)

(facts "about formatters"
	(fact "it normally returns the Joda-time formatter"
		(.getClass csv-formatter) => org.joda.time.format.DateTimeFormatter
		(.getClass month-formatter) => org.joda.time.format.DateTimeFormatter) )

(facts "about `strip-header-footer`"
		(strip-header-footer (range 10)) => '(5 6 7 8)
		(strip-header-footer (range 10) 5 4) => '(5) )

(facts "about `parse-csv-date`"
	(parse-csv-date "05/30/2013") =>
	  (new org.joda.time.DateTime 2013 5 30 0 0 org.joda.time.DateTimeZone/UTC))

(facts "about `parse-number-default`"
	(fact "returns default value"
		(parse-number-default "" :default) => :default
		(parse-number-default nil :default) => :default)
	(fact "returns parsed number"
		(parse-number-default "2" :default) => 2
		(parse-number-default "-2,329.60" :default) => -2329.6))

(facts "about `parse-csv-rows`"
	(parse-csv-rows ["03/04/2013;04/05/2013;\"DESC\";-11.80;;EUR",
	                 "02/26/2013;02/26/2013;\"LOHN/GEHALT\";;5,329.60;EUR"]) =>
	  [{:date (new org.joda.time.DateTime 2013 3 4 0 0 org.joda.time.DateTimeZone/UTC) :income 0 :expense 11.8}
		 {:date (new org.joda.time.DateTime 2013 2 26 0 0 org.joda.time.DateTimeZone/UTC) :income 5329.6 :expense 0}])
