Trabajo realizado por:
## Adesso Vittorio C.I. 26838989
## Ustariz Gabriela C.I 26956071

## Aclaratorias

##      1. Asegurarse de rellenar los TextFields y presionar la tecla "Enter"
        cuando se quieran aplicar cualquier operación a la imagen para habilitar 
        los botones de dicha aplicación. En muchos casos los valores serán
        seteados a 0 de no colocar un valor predeterminado.

##      2. Las aplicaciones de operaciones se reaizan a una imagen de manera
        independiente una vez se seleccione en la ventana inicial de la aplicación.
        Es decir, operaciones como el guardado, modificado, acciones de deshacer 
        y rehacer se harán por imagen de manera individual en la ventana de Editor de
        Imagen.

##      3. Los métodos escogidos para la modificación del brillo y contraste son tres:
        - Por un valor constante (en el caso de brillo se realiza una suma de dicho valor, 
        en el caso de contraste se aplica una regla de tres considerando el mismo). 
        - Con un valor Gamma.
        - Ecualización de la Imagen
        
##      4. Al mostrar un histograma, si la imagen es en escala de grises o blanco y
        negro se mostrará solo un gráfico. Si por el contrario está compuesta por los tres
        canales rgb, se mostrarán tres histogramas.

##      5. En lo referente a la umbralización, la misma se puede aplicar de dos maneras: por
        valor o por rango. Si es por valor, los pixeles cuyo color esté por encima de dicho 
        umbral se colocarán en blanco, de lo contrario, negro. En el caso de umbralización 
        por rango si están dentro del rango se setearán a blanco, en caso contrario negro.
        
##      6. Importante agregar la libreria JavaFx para que funciona la aplicación

