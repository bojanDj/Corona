(ns corona.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :refer [form-to text-area submit-button]]
            [hiccup.element :refer [link-to]]))

(defn header [text]
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
					[:link {:rel "stylesheet", :href "css/bootstrap.min.css"}]
					[:link {:rel "stylesheet", :href "css/style.css"}]
          [:script {:src "js/jquery-3.3.1.min.js"}]
					[:script {:src "js/jquery-3.2.1.min.js"}]
					[:script {:src "js/jquery.slicknav.min.js"}]
					[:script {:src "js/owl.carousel.min.js"}]
					[:script {:src "js/aos.js"}]
					[:script {:src "js/main.js"}]]
         
         [:body
          [:div {:class "site-wrap", :id "home-section"}  
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
					      [:a {:class "font-weight-bold"} "CORONA STATS"]]]
					    [:div {:class "col-9  text-right"} 
					     [:span {:class "d-inline-block d-lg-none"}
					      [:a {:class "text-white site-menu-toggle js-menu-toggle py-5 text-white"}
					       [:span {:class "icon-menu h3 text-white"}]]]
					     [:nav {:class "site-navigation text-right ml-auto d-none d-lg-block", :role "navigation"} 
					      [:ul {:class "site-menu main-menu js-clone-nav ml-auto "}
					       [:li {:class "active"}
					        (link-to {:class "nav-link"} "/index" "Početna")]
                 [:li 
					        (link-to {:class "nav-link"} "/search" "Druga")]
					       [:li 
					        [:a {:class "nav-link"} "Stop server"]]]]]]]]]
          text
          ]))

(defn render-result [result]
  (header
	  [:div {:id "result"}
	       [:pre [:code result]]]))

(defn render-form []
  (header
	  [:div {:id "form"}
	          (form-to [:post "/search"]
	                   (text-area {:cols 7
	                               :rows 3} "content")
	                   [:div]
	                   (submit-button "Pretrazi"))]))
  
  (defn news []
	  (header
		  [:div {:class "ftco-blocks-cover-1"}   
			 [:section {:class "hero-section"}
			  [:div {:class "hero-slider owl-carousel"}
			   [:div {:class "hs-item"}
			    [:div {:class "hs-bg set-bg sm-overlay", :id "hero1", :style "margin-top:10%; background-size: 100%; background-repeat: no-repeat;", :data-setbg "https://www.serbianmonitor.com/wp-content/uploads/2020/03/Coronavirus.png"}]
			    [:div {:class "sp-container"}
			     [:div {:class "hs-text"}
			      [:h2 "Prva" 
			       [:br]"Vest"]
			      [:p "Naslov" 
			       [:br]"Vest"]
			      [:p 
			       [:a {:class "btn btn-secondary"} "Pogledajte"]]]]]
			   [:div {:class "hs-item"}
			    [:div {:class "hs-bg set-bg sm-overlay", :style "background-size: 100%; margin-top:10%; background-repeat: no-repeat;", :data-setbg "https://www.serbianmonitor.com/wp-content/uploads/2020/03/Coronavirus.png"}]
			    [:div {:class "sp-container"}
			     [:div {:class "hs-text"}
			      [:h2 "Druga" 
			       [:br]"Vest"]
			      [:p "Naslov" 
			       [:br]"Vest"] 
			      [:p 
			       [:a {:href "galerija.php?rez=BMW 328", :class "btn btn-secondary"} "Pogledajte"]]]]]
			   [:div {:class "hs-item"}
			    [:div {:class "hs-bg set-bg sm-overlay", :style "background-size: 100%; margin-top:10%; background-repeat: no-repeat;", :data-setbg "https://www.serbianmonitor.com/wp-content/uploads/2020/03/Coronavirus.png"}]
			    [:div {:class "sp-container"}
			     [:div {:class "hs-text"}
			      [:h2 "Druga" 
			       [:br]"Vest"]
			      [:p "Naslov" 
			       [:br]"Vest"]
			      [:p 
			       [:a {:class "btn btn-secondary"} "Pogledajte"]]]]]]]]))
  
  
  
