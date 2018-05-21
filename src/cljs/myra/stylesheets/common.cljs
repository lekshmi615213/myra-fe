(ns myra.stylesheets.common
  (:require [garden.stylesheet :refer [at-media]]
            [garden-basscss.vars :refer [vars]]))

(defn stylesheet []
[
  [:.form-wrapper-item
	{:margin-top "10px"
	 :align-items "flex-start"
	 :flex-grow "1"
	}
	[:>div {:position "relative"
	  :margin-right "10px"
	  :width "39%"}]
	[:input {:margin-bottom "5px"}]
	[:select {:margin-bottom "5px"}]
  ]
  [:.error-message {:margin "0"}]
  [:.api-error-message {
	:margin-bottom ".67em"
	:margin-top ".67em"
	:display "flex"
	:justify-content "center"
	:color "red"
  }]
]
)