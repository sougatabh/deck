(ns ^{:author "Sougata Bhattacharya"
      :doc "A Cassandra Admin Tool"}  
  deck.utils
  (:use [clojure.string :only (join split)])
  (:require [clojure.java.io :as io]))


(def validator-class-type           {:ascii             "AsciiType"
                      :bytes             "BytesType"
                      :integer           "IntegerType"
                      :lexical-uuid      "LeixcalUUIDType"
                      :local-partitioner "LocalByPartionerType"
                      :long              "LongType"
                      :time-uuid         "TimeUUIDType"
                      :utf-8             "UTF8Type"
                      :composite         "CompositeType"
                      :dynamic-composite "DynamicCompositeTYpe"
                      :uuid              "UUIDType"
                      :counter           "CounterColumnType"})

(def types {:simple "Simple"
             :standard "Standard"})


(defn get-comparator-dropdown
  "This is to generate a comparator Drop Down List"
  []
  (str "<select name='comparator'>"
       "<option value=':ascii'>Ascii</option>"
       "<option value=':bytes'>Byte</option>"
       "<option value=':integer'>Integer</option>"
       "<option value=':lexical-uuid'>Laxical UUID</option>"
       "<option value=':local-partitioner'>Local By Partition</option>"
       "<option value=':long'>Long</option>"
       "<option value=':time-uuid '>Time UUID</option>"
       "<option value=':utf8'>UTF8</option>"
       "<option value=':composite'>Composite</option>"
       "<option value=':dynamic-composite'>Dynamic Composite</option>"
       "<option value=':uuid'>UUID</option>"
       "<option value=':counter'>Coumnter Column</option>"
       "</select>"))


(defn get-subcomparator-dropdown
  "This is to generate a comparator Drop Down List"
  []
  (str "<select name='subcomparator'>"
       "<option value=':ascii'>Ascii</option>"
       "<option value=':bytes'>Byte</option>"
       "<option value=':integer'>Integer</option>"
       "<option value=':lexical-uuid'>Laxical UUID</option>"
       "<option value=':local-partitioner'>Local By Partition</option>"
       "<option value=':long'>Long</option>"
       "<option value=':time-uuid '>Time UUID</option>"
       "<option value=':utf8'>UTF8</option>"
       "<option value=':composite'>Composite</option>"
       "<option value=':dynamic-composite'>Dynamic Composite</option>"
       "<option value=':uuid'>UUID</option>"
       "<option value=':counter'>Coumnter Column</option>"
       "</select>"))



(defn get-comlumnfamily-type-dropdown
  "This is to generate a column family type Drop Down List"
  []
  (str "<select name='columnfamilytype'>"
       "<option value='standard'>Standard</option>"
       "<option value='super'>Super</option>"
       "</select>"))



(defn get-validation-class-dropdown
  "This is to generate a column family type Drop Down List"
  []
  (str 
       "<option value=':bytes'>ByteType</option>"
       "<option value=':ascii'>AsciiType</option>"
       "<option value=':long'>LongType</option>"
       "<option value=':lexical-uuid'>LexicalUUIDType</option>"
       "<option value=':time-uuid'>TimeUUIDType</option>"
       ))

(defn make-connections-list
  [row]
  (str row))

(defn read-all-settings []
  "Read the settings file"
  (str(slurp "settings")))



(defn write-settings [line]
  (let [wrtr (io/writer "settings" :append true)]
  (.write wrtr line)
  (.write wrtr "\n")
  (.close wrtr)))