(deftemplate persona
   (slot edad)
   (slot peso)
   (slot salud-fisica)
   (slot salud-mental)
   (slot diabetes))

(deftemplate recomendacion
   (slot texto))

(defrule regla-salud
   (persona (edad ?edad&:(> ?edad 50)) (salud-mental "estres"))
   =>
   (assert (recomendacion (texto "Hacer ejercicio y mejorar el manejo del estrés."))))

(defrule regla-diabetes
   (persona (diabetes "si"))
   =>
   (assert (recomendacion (texto "Seguir una dieta controlada en carbohidratos y consultar al médico regularmente."))))
