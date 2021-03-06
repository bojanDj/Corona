(ns corona.store
  (:require [com.stuartsierra.component :as component]
            [clojure.data.json :as json])
  (:import java.beans.Expression
           java.net.HttpURLConnection
           java.net.URL
           java.util.Scanner))

(defn insert-country-in-db 
  "Inserts country in store and returns that country UUID"
  [store content]
  (let [uuid (.toString (java.util.UUID/randomUUID))]
    (swap! (:data store) assoc (keyword uuid) content)
    uuid))

(defn get-data
  "Returns country data"
  [url stri] 
  (let [sc (new java.util.Scanner (.openStream url))]
   (def inline "")
   (while (.hasNext sc) (do (def inline (str inline (.nextLine sc)))))
   (.close sc)
   (let [json (json/read-str inline :key-fn keyword)
         stri (str (.toUpperCase (.substring stri 0 1)) (.toLowerCase (.substring stri 1)))
         confirmed (:confirmed (:latest json))
         deaths (:deaths (:latest json))
         population (get-in (:locations json) [0 :country_population])
         casePerMil (float (/ (* confirmed 1000000) population))
         deadPerMil (float (/ (* deaths 1000000) population))
         map1 {:resp (str stri " has " (:confirmed (:latest json)) " people infected with the coronavirus, which is " 
	                        casePerMil " cases per milion of people. \nAlso " stri " has " 
	                        (:deaths (:latest json)) " people died from corona, which is  " 
	                        deadPerMil " dead people per one milion.") 
	             :name stri
	             :la (:latitude (get-in (:locations json) [0 :coordinates])) 
	             :lo (:longitude (get-in (:locations json) [0 :coordinates]))}]
	   map1)))

(defn open-connection
  "Opens connection with api for specific country"
  [stri] 
  (let [url (new java.net.URL (str "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country=" stri))
        map {:resp "Country name isn't well inserted."
             :name "Sorry..."}
        con (doto (.. url openConnection)
                  (.setInstanceFollowRedirects false)
                  (.connect))]
    (if (.startsWith (str (.getResponseCode con)) "200")
        (get-data url stri)
        map)))

(defn get-data-map 
  "Returns news data"
  [url rb] 
	 (let [sc (new java.util.Scanner (.openStream url))]
	  (def inline "")
	  (while (.hasNext sc) (do (def inline (str inline (.nextLine sc)))))
	  (.close sc)
	  (let [json (json/read-str inline :key-fn keyword)
	        map1 {:title (get-in (:articles json) [rb :title]) 
	               :desc (get-in (:articles json) [rb :description]) 
	               :img (get-in (:articles json) [rb :urlToImage]) 
	               :content (get-in (:articles json) [rb :content])}]
	    map1)))

(defn get-updates
  "Opens connection with api"
  [rb url-str] 
	 (let [url (new java.net.URL url-str)
	       con (doto (.. url openConnection)
	                 (.setInstanceFollowRedirects false)
	                 (.connect))]
	   (if (.startsWith (str (.getResponseCode con)) "200")
	       (get-data-map url rb)
	       nil)))

(defn get-country-by-uuid 
  "Returns statistic data for specific country from store"
  [store uuid]
   (open-connection ((keyword uuid) @(:data store))))

(defrecord InMemoryStore [data]

  component/Lifecycle

  (start [this]
    (assoc this :data (atom {})))

  (stop [this] this))

(defn make-store
  "Returns a new instance of the store component"
  []
  (map->InMemoryStore {}))
