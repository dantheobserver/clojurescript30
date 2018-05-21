(ns starter.macros)

(defmacro create-lesson [component]
  `(def ~'lesson (if style
                  (with-meta `'~component {:style style})
                  `'~component)))
