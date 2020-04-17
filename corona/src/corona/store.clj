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
   (str stri "\nRegistrovano slucajeva: " (:confirmed (:latest json)) "\nBroj mrtvih: " (:deaths (:latest json)))
  )
)
(defn open-connection [stri] 
  (let [url (new java.net.URL (str "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country=" stri))
        con (doto (.. url openConnection)
                  (.setInstanceFollowRedirects false)
                  (.connect))]
        (if (.startsWith (str (.getResponseCode con)) "200")
            (get-data url stri)
            "Uneseno ime drzave nije validno.")
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

(defn get-updates [rb] 
     (let [url (new java.net.URL  "http://newsapi.org/v2/top-headlines?q=virus&country=rs&from=2020-03-30&apiKey=5c9694ca2b7147039cd6614c21cb361c" )
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
