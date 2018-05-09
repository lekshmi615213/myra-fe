(ns myra.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [myra.core-test]))

(doo-tests 'myra.core-test)
