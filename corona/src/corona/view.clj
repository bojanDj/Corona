(ns corona.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :refer [form-to text-area submit-button text-field]]
            [hiccup.element :refer [link-to]]))

(defn header 
  "Header of pages"
  [text]
  (html5 [:head
          [:title "CORONA"]"\t" 
					[:meta {:charset "UTF-8"}]"\t" 
					[:meta {:name "description", :content "Instyle Fashion HTML Template"}] 
					[:meta {:name "keywords", :content "instyle, fashion, html"}]
					[:meta {:name "viewport", :content "width=device-width, initial-scale=1.0"}]
					[:link {:href "https://fonts.googleapis.com/css?family=Lato:300,300i,400,400i,700,700i&display=swap", :rel "stylesheet"}]
					[:link {:rel "stylesheet", :href "css/owl.carousel.min.css"}]
					[:link {:href "https://fonts.googleapis.com/css?family=Muli:400,700|Hepta+Slab:400,700&display=swap", :rel "stylesheet"}]
					[:link {:rel "stylesheet", :href "fonts/icomoon/style.css"}]
					[:link {:rel "stylesheet", :href "/css/bootstrap.min.css"}]
					[:link {:rel "stylesheet", :href "/css/style.css"}]
          [:script {:src "js/jquery-3.3.1.min.js"}]
					[:script {:src "js/jquery-3.2.1.min.js"}]
					[:script {:src "js/jquery.slicknav.min.js"}]
					[:script {:src "js/owl.carousel.min.js"}]
					[:script {:src "js/aos.js"}]
					[:script {:src "js/main.js"}]
			    [:link {:rel "stylesheet", :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css"}]
					[:script {:src "https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"}]
					[:script {:src "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"}]
					[:script {:src "https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"}]]
         
         [:body
					 [:div {:class "site-mobile-menu site-navbar-target"} 
					  [:div {:class "site-mobile-menu-header"} 
					   [:div {:class "site-mobile-menu-close mt-3"} 
					    [:span {:class "icon-close2 js-menu-toggle"}]]]
					  [:div {:class "site-mobile-menu-body"}]]
					 [:header {:class "site-navbar site-navbar-target", :role "banner"} 
					  [:div {:class "container"} 
					   [:div {:class "row align-items-center position-relative"}  
					    [:div {:class "col-3 "} 
					     [:div {:class "site-logo"} 
					      [:a {:class "font-weight-bold" :id "big-title"} "CORONA STATS"]]]
					    [:div {:class "col-9  text-right"} 
					     [:span {:class "d-inline-block d-lg-none"}
					      [:a {:class "text-white site-menu-toggle js-menu-toggle py-5 text-white"}
					       [:span {:class "icon-menu h3 text-white"}]]]
					     [:nav {:class "site-navigation text-right ml-auto d-none d-lg-block", :role "navigation"} 
					      [:ul {:class "site-menu main-menu js-clone-nav ml-auto "}
					       [:li {:class "active"}
					        (link-to {:id "home-menu" :class "nav-link"} "/index" "Home")]
                 [:li 
					        (link-to {:id "search-menu":class "nav-link"} "/search" "Search")]
                 [:li 
                  [:div {:class "dropdown"}
									 [:div {:class "dropbtn"} "News"]
									 [:div {:class "dropdown-content"}
									  (link-to {:id "top-news-menu" :class "nav-link" :style "float: left;"} "/top-news" "Top news")
									  (link-to {:id "updates-menu" :class "nav-link" :style "float: left;"} "/updates/1" "All news")]]]]]]]]]
          text]))

(defn slider-item 
  "Result page searched country news item"
  [news] 
  [:div {:class "col-sm-12 col-lg-4"} 
         [:div {:class "card", :style "width: 300px;margin: auto;"} 
          [:img {:src (:img news), :class "card-img-top"}]
          [:div {:class "card-body"} 
           [:h4 {:class "card-title"} (:title news)]
           [:p {:class "card-text"} (:desc news)]
           [:button {:type "button", :class "btn btn-secondary"} "Read more"]]]])

(defn slider
  "Result page searched country news"
  [map1] 
  [:div {:class "container-fluid"} 
   [:div {:id "slider-title"} "News in searched county"] 
		 [:div {:class "row"}
		  [:div {:class "col-sm-12"}  
		   [:div {:id "inam", :class "carousel slide", :data-ride "carousel"}
		    [:div {:class "carousel-inner"}
		     [:div {:class "carousel-item active"} 
		      [:div {:class "container"} 
		       [:div {:class "row"}  
		        (for [x (range 1 4)]
		          (slider-item ((keyword (java.lang.Long/toString x)) map1)))]]]
     [:div {:class "carousel-item"} 
      [:div {:class "container"} 
       [:div {:class "row"} 
         (for [x (range 4 7)]
          (slider-item ((keyword (java.lang.Long/toString x)) map1)))]]]]
      [:a {:href "#inam", :class "carousel-control-prev", :data-slide "prev"} 
      [:span {:class "carousel-control-prev-icon" :style "background-color: black; color: black;"}]]
      [:a {:href "#inam", :class "carousel-control-next", :data-slide "next"} 
      [:span {:class "carousel-control-next-icon" :style "background-color: black; color: black;"}]]]]]])

(defn render-result 
  "Result page"
  [result mapa]
  (header
    [:div 
	     [:iframe {:id "iframe" :scrolling "no", :marginheight "0", :marginwidth "0", :src (str "https://maps.google.com/maps?q=" (:la result) "," (:lo result) "&hl=en&z=3&output=embed"), :frameborder "0"}]
	     [:div {:id "result"}
	      [:p {:id "country-name"} (:name result)]
		    [:p (:resp result)] 
	      [:p {:id "stay-home"} "#StayHome!"]]
	      (slider mapa)]))

(defn render-form
  "Search page"
  []
  (header
	  [:div {:id "search-div"}
	   [:div {:class "google"}
	    [:img {:id "google_logo" :src "https://image.freepik.com/free-photo/coronavirus-text-white-background_69593-8167.jpg" :alt "image"}]]
	    [:div {:class "form1"}
		    (form-to [:post "/search"]
         (text-field {:id "form-search" :placeholder "Type country name..."} "content")
         [:div]
         (submit-button {:class "buttons"} "Search for country..."))]]))

(defn news-item 
  "Index page news item"
  [news] 
  [:div {:class "hs-item"}
	  [:div {:class "hs-bg set-bg sm-overlay", :id "hero1", :style "background-size: 100%; background-repeat: no-repeat;", :data-setbg (:img news)}]
	  [:div {:class "sp-container"}
	   [:div {:class "hs-text"}
	    [:h2 {:id "news-header"} "Latest" [:br]"News"]
	    [:p {:id "news-title"} (:title news)]
	    [:p 
	     (link-to {:class "btn btn-secondary"} "/top-news" "Read more")]]]])

(defn news 
  "Index page news"
  [news1 news2 news3]
	(header  
		[:section {:class "hero-section"}
		[:div {:class "hero-slider owl-carousel"}
    (news-item news1) 
    (news-item news2) 
    (news-item news3)]]))
  
(defn list-item 
  "All news and top news page item"
  [map1] 
  [:div {:class "col-xs-12 col-sm-12 col-md-12 col-lg-12"}
	 [:div {:class "thumbnail"}
	  [:div {:class "post_title"} (:title map1)]
	  [:img {:src (:img map1), :class "img-responsive", :alt "image", :style "border-radius:8px; height: 50%;"}]
	  [:div {:class "caption"}
	   [:p (:desc map1)]
	   [:details 
	    [:summary "Read more"]
	    [:p (:content map1)]]
	   [:div {:class "text-right"}
	    [:p "16.04.2020."]]]]])

(defn updates 
  "All news page"
  [& args] 
  (html5 [:head][:body (header "") [:div {:id "ten-news"} args]
   [:iframe {:id "iframe-yt" :src "https://www.youtube.com/embed/NMre6IAAAiU?autoplay=1"} ]
   [:ul {:class "next-page-buttons"}
    (for [x (range 1 6)]
      [:li 
       (link-to {:class "btn btn-secondary"} (str "/updates/" x) x)])]]))

(defn updatesTop
  "Top news page"
  [& args] 
  (html5 [:head][:body (header "") [:div {:id "ten-news"} args]
   [:iframe {:id "iframe-yt" :src "https://www.youtube.com/embed/NMre6IAAAiU?autoplay=1"}]]))
  
