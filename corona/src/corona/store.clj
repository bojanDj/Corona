(ns corona.store
  (:require [com.stuartsierra.component :as component]
            [clojure.data.json :as json])
  (:import java.beans.Expression
           java.net.HttpURLConnection
           java.net.URL
           java.util.Scanner
           ))

(defn insert-country-in-db [store content]
  (let [uuid (.toString (java.util.UUID/randomUUID))]
    (swap! (:data store) assoc (keyword uuid) content)
    uuid))

(defn get-data [url stri] 
  (let [sc (new java.util.Scanner (.openStream url))]
   (def inline "")
   (while (.hasNext sc) (do (def inline (str inline (.nextLine sc)))))
   (.close sc)
   (def json (json/read-str inline :key-fn keyword))
   (let [stri (str (.toUpperCase (.substring stri 0 1)) (.substring stri 1))
         confirmed (:confirmed (:latest json))
         deaths (:deaths (:latest json))
         population (get-in (:locations json) [0 :country_population])
         casePerMil (/ (* confirmed 1000000) population)
         deadPerMil (/ (* deaths 1000000) population)
         mapa {:resp (str stri " has " (:confirmed (:latest json)) " people infected with the coronavirus, which is " 
	                        (/ (* confirmed 1000000) population) " cases per milion of people. \nAlso " stri " has " 
	                        (:deaths (:latest json)) " people died from corona, which is  " 
	                        deadPerMil " dead people per one milion.") 
	             :name stri
	             :la (:latitude (get-in (:locations json) [0 :coordinates])) 
	             :lo (:longitude (get-in (:locations json) [0 :coordinates]))}]
	   mapa
	   )
   )
)
(defn open-connection [stri] 
  (let [url (new java.net.URL (str "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country=" stri))
        map {:resp "Country name isn't good inserted."
             :name "Sorry..."}
        con (doto (.. url openConnection)
                  (.setInstanceFollowRedirects false)
                  (.connect))]
        (if (.startsWith (str (.getResponseCode con)) "200")
            (get-data url stri)
            map)
 )
)

(defn get-data-map [url rb] 
           (let [sc (new java.util.Scanner (.openStream url))]
            (def inline "")
            (while (.hasNext sc) (do (def inline (str inline (.nextLine sc)))))
            (.close sc)
            (def json (json/read-str inline :key-fn keyword))
            (def mapa {:title (get-in (:articles json) [rb :title]) 
                       :desc (get-in (:articles json) [rb :description]) 
                       :img (get-in (:articles json) [rb :urlToImage]) 
                       :content (get-in (:articles json) [rb :content])
                       })
            mapa
           )
         )

(defn get-updates [rb url-str] 
     (let [url (new java.net.URL url-str)
           con (doto (.. url openConnection)
                     (.setInstanceFollowRedirects false)
                     (.connect))]
           (if (.startsWith (str (.getResponseCode con)) "200")
               (get-data-map url rb)
               nil)
    )
   )

(defn get-country-by-uuid [store uuid]
   (open-connection ((keyword uuid) @(:data store))) 
)


(defrecord InMemoryStore [data]

  component/Lifecycle

  (start [this]
    (assoc this :data (atom {})))

  (stop [this] this))

(defn make-store
  []
  (map->InMemoryStore {}))
