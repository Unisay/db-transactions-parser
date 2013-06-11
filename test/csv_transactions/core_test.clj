(ns csv-transactions.core-test
	(:use midje.sweet
	      csv-transactions.core
	      [midje.util :only [testable-privates]]
        [clj-time.core :only [year-month date-time]]))

(testable-privates csv-transactions.core
	strip-header-footer parse-csv-date parse-number-default parse-csv-rows aggregate-by-month)

(facts "about formatters"
	(fact "it normally returns the Joda-time formatter"
		(.getClass csv-formatter) => org.joda.time.format.DateTimeFormatter
		(.getClass month-formatter) => org.joda.time.format.DateTimeFormatter) )

(facts "about `strip-header-footer`"
		(strip-header-footer (range 10)) => '(5 6 7 8)
		(strip-header-footer (range 10) 5 4) => '(5) )

(facts "about `parse-csv-date`"
	(parse-csv-date "05/30/2013") => (date-time 2013 5 30))

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
	  [{:date (date-time 2013 3 4) :income 0 :expense 11.8}
		 {:date (date-time 2013 2 26) :income 5329.6 :expense 0}])

(facts "about `aggregate-by-month`"
	(aggregate-by-month [ { :date (date-time 2013 3) :income 2 :expense 5 }
											  { :date (date-time 2013 3) :income 3 :expense 6 }
											  { :date (date-time 2013 4) :income 4 :expense 7 }
											  { :date (date-time 2014 4) :income 5 :expense 8 } ]) =>
	[ { :month (year-month 2013 3) :income 5 :expense 11 }
	  { :month (year-month 2013 4) :income 4 :expense 7  }
	  { :month (year-month 2014 4) :income 5 :expense 8  } ])
