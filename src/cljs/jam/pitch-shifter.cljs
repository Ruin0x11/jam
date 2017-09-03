(ns jam.pitch-shifter)

;; (defn set-delay-p [delay-time mod-gain1 mod-gain2]
;;   (.setTargetAtTime (-> mod-gain1 .-gain) (* delay-time 0.5) 0 0.010)
;;   (.setTargetAtTime (-> mod-gain2 .-gain) (* delay-time 0.5) 0 0.010))
;;      ;; (doseq [v [val1 val2]]

;;      ;;   (.log js/console (type v))
;;      ;;   (.setTargetAtTime v (* delay-time 0.5) 0 0.010)))))

;; (defn set-pitch-offset-p [mult mod1-gain mod2-gain mod3-gain mod4-gain]
;;   (if (> mult 0)
;;     (do
;;       (set! (-> mod1-gain .-gain .-value) 0)
;;       (set! (-> mod2-gain .-gain .-value) 0)
;;       (set! (-> mod3-gain .-gain .-value) 1)
;;       (set! (-> mod4-gain .-gain .-value) 1)
;;       )
;;     (do
;;       (set! (-> mod1-gain .-gain .-value) 1)
;;       (set! (-> mod2-gain .-gain .-value) 1)
;;       (set! (-> mod3-gain .-gain .-value) 0)
;;       (set! (-> mod4-gain .-gain .-value) 0)
;;       )

;;     )
;;   )


;; (defn fade-buffer [context active-time fade-time]
;;   (let [sample-rate (.-sampleRate context)
;;         length1 (* active-time sample-rate)
;;         length2 (* sample-rate (- active-time (* 2 fade-time)))
;;         length (+ length1 length2)
;;         buffer (.createBuffer context 1 length sample-rate)
;;         p (.getChannelData buffer 0)
;;         fade-length (* fade-time sample-rate)
;;         fade-index1 fade-length
;;         fade-index2 (- length1 fade-length)]
;;     (dotimes [i length1]
;;       (let [val (cond
;;                   (< i fade-index1) (Math/sqrt (/ i fade-length))
;;                   (>= i fade-index2) (Math/sqrt (/ (- 1 (- i fade-index2)) fade-length))
;;                    :else 1
;;                    )]
;;         (aset p i val))
;;       )

;;     (for [i (range length1 length)]
;;       (aset p i 0))
;;     buffer
;;     ))

;; (defn delay-time-buffer [context active-time fade-time is-up]
;;   (let [sample-rate (.-sampleRate context)
;;         length1 (* active-time sample-rate)
;;         length2 (* sample-rate (- active-time (* 2 fade-time)))
;;         length (+ length1 length2)
;;         buffer (.createBuffer context 1 length sample-rate)
;;         p (.getChannelData buffer 0)]
;;     (dotimes [i length1]
;;       (let [val (if is-up
;;                   (/ (- length1 i) length)
;;                   (/ i length1))]
;;         (aset p i val))
;;       )

;;     (for [i (range length1 length)]
;;       (aset p i 0))
;;     buffer
;; ))

;; (def default-buffer-time 0.1)
;; (def default-fade-time 0.05)
;; (def default-delay-time 0.1)

;; (defn jungle [context]
;;   (let [ctx context
;;         [input output
;;          mod1-gain mod2-gain mod3-gain mod4-gain
;;          mod-gain1 mod-gain2 mix1 mix2] (repeatedly 10 #(.createGain context))
;;         shift-up-buffer (delay-time-buffer ctx default-buffer-time default-fade-time false)
;;         shift-down-buffer (delay-time-buffer ctx default-buffer-time default-fade-time true)
;;         fade-buffer (fade-buffer ctx default-buffer-time default-fade-time)
;;         [mod1 mod2 mod3 mod4 fade1 fade2] (repeatedly 6 #(.createBufferSource context))
;;         [delay1 delay2] (repeatedly 2 #(.createDelay context))
;;         t1 (+ (.-currentTime ctx) 0.05)
;;         t2 (+ t1 default-buffer-time (- default-fade-time))
;;         ]
;;     (set! (-> mod1 .-buffer) shift-down-buffer)
;;     (set! (-> mod2 .-buffer) shift-down-buffer)
;;     (set! (-> mod3 .-buffer) shift-up-buffer)
;;     (set! (-> mod4 .-buffer) shift-up-buffer)
;;     (doseq [m [mod1 mod2 mod3 mod4]]
;;       (set! (-> m .-loop) true))
;;     (set! (-> mod3-gain .-gain .-value) 0)
;;     (set! (-> mod4-gain .-gain .-value) 0)

;;     (.connect mod1 mod1-gain)
;;     (.connect mod2 mod2-gain)
;;     (.connect mod3 mod3-gain)
;;     (.connect mod4 mod4-gain)

;;     (.connect mod1-gain mod-gain1)
;;     (.connect mod2-gain mod-gain2)
;;     (.connect mod3-gain mod-gain1)
;;     (.connect mod4-gain mod-gain2)
;;     (.connect mod-gain1 (-> delay1 .-delayTime))
;;     (.connect mod-gain2 (-> delay2 .-delayTime))

;;     (set! (-> fade1 .-buffer) fade-buffer)
;;     (set! (-> fade2 .-buffer) fade-buffer)
;;     (set! (-> fade1 .-loop) true)
;;     (set! (-> fade2 .-loop) true)

;;     (set! (-> mix1 .-gain .-value) 0)
;;     (set! (-> mix2 .-gain .-value) 0)

;;     (.connect fade1 (-> mix1 .-gain))
;;     (.connect fade2 (-> mix2 .-gain))

;;     (.connect input delay1)
;;     (.connect input delay2)
;;     (.connect delay1 mix1)
;;     (.connect delay2 mix2)
;;     (.connect mix1 output)
;;     (.connect mix2 output)

;;     (.start mod1 t1)
;;     (.start mod2 t2)
;;     (.start mod3 t1)
;;     (.start mod4 t2)
;;     (.start fade1 t1)
;;     (.start fade2 t2)

;;     (set-delay-p default-delay-time mod-gain1 mod-gain2)
;;     (reify
;;       Object
;;       (setDelay [this time]
;;         (set-delay-p time mod-gain1 mod-gain2)
;;         )
;;       (setPitchOffset [this mult]
;;         (set-pitch-offset-p mult mod1-gain mod2-gain mod3-gain mod4-gain)
;;         (.setDelay this (* default-delay-time (Math/abs mult))))
;;       (input [this] input)
;;       (output [this] output)
;;       )))


;; (defn horner [coeffs x]
;;   (reduce #(-> %1 (* x) (+ %2)) (reverse coeffs)))

;; (def coeffs [0.02278153473118749
;;              0.23005591195033048
;;              -0.014147877819596033
;;              0.0009795096626987743
;;              -0.000019413043101157434
;;              1.8149080040913423e-7])


;; (defn get-multiplier
;;   [x]
;;   (if (< x 0)
;;     (/ x 12)
;;     (horner coeffs x)))

;; (defn pitch-shift [context]
;;   (let [instance (jungle context)
;;         [input wet dry output] (repeatedly 4 #(.createGain context))
;;         node (.createGain context)]
;;         (set! (-> dry .-gain .-value) 0)
;;         (set! (-> wet .-gain .-value) 0)

;;         (.setPitchOffset instance (get-multiplier 11))

;;         (.connect node input)
;;         (.connect input wet)
;;         (.connect input dry)
;;         (.connect wet (-> instance .-input))
;;         (.connect (-> instance .-output) output)
;;         (.connect dry output)
;;         (.connect output (.-destination context))

;;         ;; (set! (-> node .-connect) (fn [dest chan] (.connect output dest chan)))

;;         node))
