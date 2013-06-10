(ns csv-transactions.core

	(:require [clojure.java.io :as io]
	          [clojure-csv.core :as csv]
	          [clj-time.core :as time]
	          [clj-time.format :as format]
	          [clojure.contrib.math :as math])

	(:use [clojure.string :only [split]])

	(:gen-class ))

(def csv-formatter (format/formatter "MM/dd/yyyy"))
(def month-formatter (format/formatter "MMM.yyyy"))

(defn- strip-header-footer
	"Removes header and footer rows from CSV sequence of lines"
	([lines]
		(strip-header-footer lines 5 1))
	([lines ^Number n-strip-header ^Number n-strip-footer]
		(drop-last n-strip-footer (drop n-strip-header lines))))

(defn- parse-csv-date
	"Parse date represented as string"
	[date-string]
	(format/parse csv-formatter date-string))

(defn- parse-number-default
	"Parse numeric string or return default value if it is empty"
	[numeric-string default]
	(if (empty? numeric-string) default (read-string (.replace numeric-string "," ""))))

(defn- parse-csv-row
	"Parses text line and returns map of typed values"
	[row]
	(let [cells (split row #";"), dt (parse-csv-date (nth cells 0))]
		{:month (vector (time/year dt) (time/month dt))
		 :income (parse-number-default (nth cells 4) 0)
		 :expense (math/abs (parse-number-default (nth cells 3) 0))}))

(defn- parse-csv-rows
	"Parses text lines and returns sequence of maps of typed values"
	[rows]
	(map parse-csv-row rows))

(defn- group-records
	[f rec1 rec2]
	(let [income-group (f (get rec1 :income 0) (:income rec2)),
        expense-group (f (get rec1 :expense 0) (:expense rec2))]
	  {:income income-group, :expense expense-group}))

(defn- aggregate-by-month
	"Groups row maps by month, sums up income and expens fields"
	[records]
	(let [groups (group-by #(get % :month) records)]
		(for [[month group] groups]
			(assoc (reduce (partial group-records +) {} group) :month month))))

(defn- print-values
	"Outputs all results to console"
	[records]
	(doseq [r records]
		(println (:month r) (:expense r) (:income r))))

(defn -main
	"Parse Deutsche Bank transactions in CSV format and aggregates incomes and expenses by month"
	[& args]

	(with-open [file-reader (io/reader (first args))]
		(-> file-reader
			line-seq
			strip-header-footer
			parse-csv-rows
			aggregate-by-month
			print-values)))
